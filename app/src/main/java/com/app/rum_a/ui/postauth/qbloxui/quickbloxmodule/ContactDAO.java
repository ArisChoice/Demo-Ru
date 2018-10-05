package com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.app.rum_a.ui.postauth.qbloxui.quickbloxmodule.database.DatabaseHelper;
import com.quickblox.users.model.QBUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vivek on 01-05-2017.
 */

public class ContactDAO {

    static ContactDAO dao;

    public static ContactDAO getInstance() {

        if (dao == null) {
            dao = new ContactDAO();
        }
        return dao;
    }

    public void insertRecodeToContact(SQLiteDatabase database, QBUser qbUser, String isContact) {

        try {
            ContentValues values = new ContentValues();
            try {
                values.put(DatabaseHelper.KEY_FULLNAME, qbUser.getFullName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(DatabaseHelper.KEY_BLOBID, qbUser.getFileId() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(DatabaseHelper.KEY_EMAIL, qbUser.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(DatabaseHelper.KEY_PHONE, qbUser.getPhone() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                values.put(DatabaseHelper.KEY_QBID, qbUser.getId());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (!TextUtils.isEmpty(isContact.trim())) {
                try {
                    values.put(DatabaseHelper.KEY_IS_CONTACT, isContact);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            database.insert(DatabaseHelper.TABLE_CONTACTS, null, values);
        } catch (Exception e) {
            database.close();
            e.printStackTrace();
        } finally {
            database.close();
        }
    }


    public QBUser getUserRecord(SQLiteDatabase database, String id) {
        QBUser user = null;
        String[] columns = new String[]{DatabaseHelper.KEY_FULLNAME,
                DatabaseHelper.KEY_LOGIN,
                DatabaseHelper.KEY_EMAIL,
                DatabaseHelper.KEY_BLOBID,
                DatabaseHelper.KEY_PHONE,
                DatabaseHelper.KEY_QBID};
        String selection = DatabaseHelper.KEY_QBID + " =?";

        String[] argments = new String[]{id};

        try {
            Cursor cursor = database.query(DatabaseHelper.TABLE_CONTACTS, columns, selection, argments, null, null, null);

            if (cursor.moveToFirst()) {
                user = new QBUser();
                try {
                    user.setFullName(cursor.getString(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    user.setLogin(cursor.getString(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    user.setEmail(cursor.getString(2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    user.setFileId(Integer.parseInt(cursor.getString(3)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                try {
                    user.setPhone(cursor.getString(4));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    user.setId(Integer.parseInt(cursor.getString(5)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            database.close();
        } finally {
            database.close();
        }

        return user;
    }


    public void updateUser(SQLiteDatabase database, QBUser qbUser, String id, String is_contact) {

        try {
            ContentValues values = new ContentValues();
            try {
                values.put(DatabaseHelper.KEY_FULLNAME, qbUser.getFullName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(DatabaseHelper.KEY_BLOBID, qbUser.getFileId() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(DatabaseHelper.KEY_EMAIL, qbUser.getEmail());
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                values.put(DatabaseHelper.KEY_PHONE, qbUser.getPhone() + "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!TextUtils.isEmpty(is_contact.trim())) {
                try {
                    values.put(DatabaseHelper.KEY_IS_CONTACT, is_contact);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String where = DatabaseHelper.KEY_QBID + " =? ";
            String[] whereargs = {id};
            database.update(DatabaseHelper.TABLE_CONTACTS, values, where, whereargs);
        } catch (Exception e) {
            e.printStackTrace();
            database.close();
        } finally {
            database.close();
        }

    }

    public ArrayList<QBUser> getUsersByIds(Context context, List<Integer> usersIds) {
        ArrayList<QBUser> qbUsers = new ArrayList<>();

        for (Integer userId : usersIds) {
            SQLiteDatabase database = DatabaseHelper.getDatabaseInstanse(context).getReadableDatabase();
            QBUser user = getUserById(database, userId);
            if (user != null) {
                qbUsers.add(user);
            }
        }

        return qbUsers;
    }


    public QBUser getUserById(SQLiteDatabase database, Integer userId) {

        QBUser user = new QBUser();
        String[] columns = new String[]{DatabaseHelper.KEY_FULLNAME,
                DatabaseHelper.KEY_LOGIN,
                DatabaseHelper.KEY_EMAIL,
                DatabaseHelper.KEY_BLOBID,
                DatabaseHelper.KEY_PHONE,
                DatabaseHelper.KEY_QBID};
        String arguments = DatabaseHelper.KEY_QBID + " =?";
        Cursor cursor = database.query(DatabaseHelper.TABLE_CONTACTS, columns, arguments, new String[]{userId + ""}, null, null, null);
        try {

            if (cursor.moveToFirst()) {

                try {
                    user.setFullName(cursor.getString(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    user.setLogin(cursor.getString(1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    user.setEmail(cursor.getString(2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    user.setFileId(Integer.parseInt(cursor.getString(3)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                try {
                    user.setPhone(cursor.getString(4));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    user.setId(Integer.parseInt(cursor.getString(5)));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            database.close();
            cursor.close();
        } finally

        {
            cursor.close();
            database.close();
        }

        return user;
    }


    public void saveAllUsers(Context context, ArrayList<QBUser> allUsers) {
        if (allUsers == null || allUsers.size() == 0) {
            return;
        }
//        for (QBUser qbUser : allUsers) {
        for (int i = 0; i < allUsers.size(); i++) {
            SQLiteDatabase database = DatabaseHelper.getDatabaseInstanse(context).getReadableDatabase();
            QBUser user = ContactDAO.getInstance().getUserRecord(database, allUsers.get(i).getId() + "");
            SQLiteDatabase writableDatabase = DatabaseHelper.getDatabaseInstanse(context).getWritableDatabase();
            if (user == null) {
                insertRecodeToContact(writableDatabase, allUsers.get(i), "");
            } else {
                updateUser(writableDatabase, allUsers.get(i), allUsers.get(i).getId() + "", "");
            }
        }

//            SQLiteDatabase database = DatabaseHelper.getDatabaseInstanse(context).getReadableDatabase();
//            insertRecodeToContact(database, qbUser, "");
//        }
    }


    public ArrayList<QBUser> getContactListUser(SQLiteDatabase database) {
        ArrayList<QBUser> list = new ArrayList<>();
        try {
            String[] columns = new String[]{DatabaseHelper.KEY_FULLNAME,
                    DatabaseHelper.KEY_LOGIN,
                    DatabaseHelper.KEY_EMAIL,
                    DatabaseHelper.KEY_BLOBID,
                    DatabaseHelper.KEY_PHONE,
                    DatabaseHelper.KEY_QBID};
            String args = DatabaseHelper.KEY_IS_CONTACT + " =?";
            Cursor cursor = database.query(DatabaseHelper.TABLE_CONTACTS, columns, args, new String[]{"1"}, null, null, null);

            if (cursor.moveToFirst()) {
                do {
                    QBUser user = new QBUser();
                    try {
                        user.setFullName(cursor.getString(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        user.setLogin(cursor.getString(1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        user.setEmail(cursor.getString(2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        user.setFileId(Integer.parseInt(cursor.getString(3)));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    try {
                        user.setPhone(cursor.getString(4));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        user.setId(Integer.parseInt(cursor.getString(5)));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    list.add(user);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
            database.close();
        } finally {
            database.close();
        }
        return list;
    }

    public ArrayList<QBUser> getAllContact(SQLiteDatabase database) {
        ArrayList<QBUser> list = new ArrayList<>();
        try {
            String[] columns = new String[]{DatabaseHelper.KEY_FULLNAME,
                    DatabaseHelper.KEY_LOGIN,
                    DatabaseHelper.KEY_EMAIL,
                    DatabaseHelper.KEY_BLOBID,
                    DatabaseHelper.KEY_PHONE,
                    DatabaseHelper.KEY_QBID};
            Cursor cursor = database.query(DatabaseHelper.TABLE_CONTACTS, columns, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                do {

                    QBUser user = new QBUser();
                    try {
                        user.setFullName(cursor.getString(0));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        user.setLogin(cursor.getString(1));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        user.setEmail(cursor.getString(2));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        user.setFileId(Integer.parseInt(cursor.getString(3)));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    try {
                        user.setPhone(cursor.getString(4));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    try {
                        user.setId(Integer.parseInt(cursor.getString(5)));
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    list.add(user);
                } while (cursor.moveToNext());

            }
        } catch (Exception e) {
            e.printStackTrace();
            database.close();
        } finally {
            database.close();
        }
        return list;
    }

    public void deleteTable(SQLiteDatabase db) {
        try {
            db.delete(DatabaseHelper.TABLE_CONTACTS, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            db.close();
        } finally {
            db.close();
        }
    }
}
