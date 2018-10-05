/**
 * Image related utility methods
 */
package com.app.rum_a.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;


import com.app.rum_a.core.RumApplication;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ImageUtility {
    private Context context;

    public ImageUtility(Context context) {
        this.context = context;
    }

    public Uri CameraGalleryIntent(final int cameraRequestCode, final int galleryRequestCode, final boolean showDialog) {
        final File root = getFile();
        root.mkdirs();
        String filename = getUniqueImageFilename();
        File sdImageMainDirectory = new File(root, filename);
        final Uri outputFileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID, sdImageMainDirectory);
//        final Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
        if (showDialog) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            CharSequence items[] = new CharSequence[]{"Camera", "Gallery"};
            dialog.setItems(items, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface d, int n) {
                    switch (n) {
                        case 0:
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            ((Activity) context).startActivityForResult(intent, cameraRequestCode);
                            break;
                        case 1:
                            Intent pickIntent = new Intent(Intent.ACTION_PICK);
                            pickIntent.setType("image/*");
                            pickIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            ((Activity) context).startActivityForResult(pickIntent, galleryRequestCode);
                            break;
                        default:
                            break;
                    }
                }

            });
            dialog.setTitle("Select Image From");
            if (outputFileUri != null) {
                dialog.show();
            }
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            intent.putExtra("android.intent.extras.CAMERA_FACING", android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
            intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
            intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
            ((Activity) context).startActivityForResult(intent, cameraRequestCode);
        }
        return outputFileUri;
    }


    /**
     * Compresses the image
     *
     * @param filePath : Location of image file
     * @return
     */
    @SuppressWarnings("deprecation")
    public String compressImage(String filePath) {
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;
        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;
            }
        }

        options.inSampleSize = ImageUtility.calculateInSampleSize(options, actualWidth, actualHeight);
        options.inJustDecodeBounds = false;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(
                Paint.FILTER_BITMAP_FLAG));

        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0);
            Log.e("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.e("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.e("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.e("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0, scaledBitmap.getWidth(), scaledBitmap.getHeight(),
                    matrix, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (bmp != null) {
                bmp.recycle();
                bmp = null;
            }
            if (scaledBitmap != null) {
                scaledBitmap.recycle();
            }
        }
        return filename;

    }

    public String getFilename() {

        final File root = getFile();
        root.mkdirs();
        final String filename = getUniqueImageFilename();
        File file = new File(root, filename);
        return file.getAbsolutePath();
    }

    private static final String JPEG_FILE_PREFIX = "IMG_";
    public static final String JPEG_FILE_SUFFIX = ".jpg";

    public static String getFileName() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = JPEG_FILE_PREFIX + timeStamp + "_";
        return imageFileName;
    }
    private File getFile() {
        File root = new File(Environment.getExternalStorageDirectory() + File.separator + "Rum-A"
                + File.separator);
        return root;
    }


    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            if (width > height) {
                inSampleSize = Math.round((float) height / (float) reqHeight);
            } else {
                inSampleSize = Math.round((float) width / (float) reqWidth);
            }
        }
        return inSampleSize;
    }

    public static Bitmap rotate(Bitmap b, float degrees) {
        if (degrees != 0 && b != null) {
            Matrix m = new Matrix();

            m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
            try {
                Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
                if (b != b2) {
                    b.recycle();
                    b = b2;
                }
            } catch (OutOfMemoryError ex) {
                throw ex;
            }
        }
        return b;
    }

    public float checckRotationForImage(Context context, Uri uri) {
        if (uri.getScheme().equals("content")) {
            String[] projection = {Images.ImageColumns.ORIENTATION};
            Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
        } else if (uri.getScheme().equals("file")) {
            try {
                ExifInterface exif = new ExifInterface(uri.getPath());
                int rotation = (int) exifOrientationToDegrees(exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL));
                return rotation;
            } catch (IOException e) {
            }
        }
        return 0f;
    }

    public float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    public static String getUniqueImageFilename() {
        return "img_" + System.currentTimeMillis() + ".jpg";
    }


    public void LoadImage(String url, ImageView imageView) {
        LoadImage(url, imageView, false);
    }

    public void LoadImage(String url, ImageView imageView, boolean isRounded) {
        ImageLoader.getInstance().displayImage(url, imageView,
                ((RumApplication) context.getApplicationContext()).getDisplayImageOptions(isRounded));
    }

    /**
     * Saves the image from bitmap
     *
     * @throws IOException
     */
    public Uri saveToSorage(Context context, Bitmap bitmapImage) throws IOException {

//        bitmapImage = rotateImageIfRequired(context,bitmapImage);

        ContextWrapper cw = new ContextWrapper(context);
        // path to save
        File directory = cw.getDir("CanGetIt", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, System.currentTimeMillis() + "image.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                fos.close();
            }
        }
        return Uri.fromFile(mypath);
    }

    /**
     * Rotate an image if required.
     *
     * @param img
     * @return
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img) {

        // Detect rotation
        int rotation = getRotation(context);
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
            img.recycle();
            return rotatedImg;
        } else {
            return img;
        }
    }

    /**
     * Get the rotation of the last image added.
     *
     * @param context
     * @return
     */
    private static int getRotation(Context context) {
        int rotation = 0;
        ContentResolver content = context.getContentResolver();


        Cursor mediaCursor = content.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{"orientation", "date_added"}, null, null, "date_added desc");

        if (mediaCursor != null && mediaCursor.getCount() != 0) {
            while (mediaCursor.moveToNext()) {
                rotation = mediaCursor.getInt(0);
                break;
            }
        }
        if (mediaCursor != null) {
            mediaCursor.close();
        }
        return rotation;
    }

    private static final int IO_BUFFER_SIZE = 4 * 1024;

    public Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(url).openStream(), IO_BUFFER_SIZE);

            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();

            final byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        } catch (IOException e) {
            Log.e("TAG", "Could not load Bitmap from: " + url);
        } finally {
            closeStream(in);
            closeStream(out);
        }

        return bitmap;
    }

    /**
     * Closes the specified stream.
     *
     * @param stream The stream to close.
     */
    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (IOException e) {
                android.util.Log.e("TAG", "Could not close stream", e);
            }
        }
    }

    /**
     * Copy the content of the input stream into the output stream, using a
     * temporary byte array buffer whose size is defined by
     * {@link #IO_BUFFER_SIZE}.
     *
     * @param in  The input stream to copy from.
     * @param out The output stream to copy to.
     * @throws IOException If any error occurs during the copy.
     */
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] b = new byte[IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
    public static String getString(Context context, Uri path) {
        String mCurrentPhotoPath = "";
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(path, filePathColumn, null, null, null);
        if (cursor == null || cursor.getCount() < 1) {
            mCurrentPhotoPath = null;
            return mCurrentPhotoPath;
        }
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        if (columnIndex < 0) {
            mCurrentPhotoPath = null;
            return mCurrentPhotoPath;
        }
        File mCurrentPhoto = new File(cursor.getString(columnIndex));
        mCurrentPhotoPath = mCurrentPhoto.getAbsolutePath();
        cursor.close();
        return mCurrentPhotoPath;
    }
}
