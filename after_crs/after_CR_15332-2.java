/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:Id789f44a8569e307b1b7ab15eb266c9ce7ef2029*/




//Synthetic comment -- diff --git a/core/java/android/os/Debug.java b/core/java/android/os/Debug.java
//Synthetic comment -- index 2e14667..86f9a6b 100644

//Synthetic comment -- @@ -94,7 +94,8 @@
/**
* Default trace file path and file
*/
    private static final String DEFAULT_TRACE_PATH_PREFIX =
        Environment.getExternalStorageDirectory().getPath() + "/";
private static final String DEFAULT_TRACE_BODY = "dmtrace";
private static final String DEFAULT_TRACE_EXTENSION = ".trace";
private static final String DEFAULT_TRACE_FILE_PATH =
//Synthetic comment -- @@ -127,7 +128,7 @@
public int otherPrivateDirty;
/** The shared dirty pages used by everything else. */
public int otherSharedDirty;

public MemoryInfo() {
}

//Synthetic comment -- @@ -137,21 +138,21 @@
public int getTotalPss() {
return dalvikPss + nativePss + otherPss;
}

/**
* Return total private dirty memory usage in kB.
*/
public int getTotalPrivateDirty() {
return dalvikPrivateDirty + nativePrivateDirty + otherPrivateDirty;
}

/**
* Return total shared dirty memory usage in kB.
*/
public int getTotalSharedDirty() {
return dalvikSharedDirty + nativeSharedDirty + otherSharedDirty;
}

public int describeContents() {
return 0;
}
//Synthetic comment -- @@ -179,7 +180,7 @@
otherPrivateDirty = source.readInt();
otherSharedDirty = source.readInt();
}

public static final Creator<MemoryInfo> CREATOR = new Creator<MemoryInfo>() {
public MemoryInfo createFromParcel(Parcel source) {
return new MemoryInfo(source);
//Synthetic comment -- @@ -460,7 +461,7 @@
* Like startMethodTracing(String, int, int), but taking an already-opened
* FileDescriptor in which the trace is written.  The file name is also
* supplied simply for logging.  Makes a dup of the file descriptor.
     *
* Not exposed in the SDK unless we are really comfortable with supporting
* this and find it would be useful.
* @hide
//Synthetic comment -- @@ -1070,7 +1071,7 @@
*    static {
*        // Sets all the fields
*        Debug.setFieldsOn(MyDebugVars.class);
     *
*        // Sets only the fields annotated with @Debug.DebugProperty
*        // Debug.setFieldsOn(MyDebugVars.class, true);
*    }








//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
//Synthetic comment -- index bf9e854..d271e93 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import android.database.Cursor;
import android.database.sqlite.SqliteWrapper;
import android.net.Uri;
import android.os.Environment;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Config;
//Synthetic comment -- @@ -1526,7 +1527,8 @@
* which streams the captured image to the uri. Internally we write the media content
* to this file. It's named '.temp.jpg' so Gallery won't pick it up.
*/
            public static final String SCRAP_FILE_PATH =
                Environment.getExternalStorageDirectory().getPath() + "/mms/scrapSpace/.temp.jpg";
}

public static final class Intents {








//Synthetic comment -- diff --git a/core/java/com/android/internal/os/SamplingProfilerIntegration.java b/core/java/com/android/internal/os/SamplingProfilerIntegration.java
//Synthetic comment -- index 5f5c7a4..127fb23 100644

//Synthetic comment -- @@ -85,7 +85,8 @@
pending = true;
snapshotWriter.execute(new Runnable() {
public void run() {
                    String dir =
                        Environment.getExternalStorageDirectory().getPath() + "/snapshots";
if (!dirMade) {
new File(dir).mkdirs();
if (new File(dir).isDirectory()) {







