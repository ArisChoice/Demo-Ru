package com.app.rum_a.utils;

import android.util.Log;

public class Log4Android
{
	public static final boolean printverbosemsg = true;
	public static final String TAG = "XAL";

	public static void d(Object o, String msg)
	{
		if (printverbosemsg)
		{
			Log.d(o.getClass()+"", msg);
		}
	}

	public static void e(Object o, String msg)
	{
		if (printverbosemsg)
		{
			Log.e(o.getClass()+"", msg);
		}
	}
}
