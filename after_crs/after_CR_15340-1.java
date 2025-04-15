/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:Ia5d58b424dffb2ef9af0d93565997dcdde102762*/




//Synthetic comment -- diff --git a/src/com/android/exchange/utility/FileLogger.java b/src/com/android/exchange/utility/FileLogger.java
//Synthetic comment -- index c5f4635..af8f156 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.exchange.utility;

import android.content.Context;
import android.os.Environment;

import java.io.FileWriter;
import java.io.IOException;
//Synthetic comment -- @@ -26,7 +27,8 @@
public class FileLogger {
private static FileLogger LOGGER = null;
private static FileWriter mLogWriter = null;
    public static String LOG_FILE_NAME =
        Environment.getExternalStorageDirectory() + "/emaillog.txt";

public synchronized static FileLogger getLogger (Context c) {
LOGGER = new FileLogger();







