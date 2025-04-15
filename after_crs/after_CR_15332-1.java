/*Replaced /sdcard with Environment.getExternalStorageDirectory()

Change-Id:Id789f44a8569e307b1b7ab15eb266c9ce7ef2029*/




//Synthetic comment -- diff --git a/core/java/android/os/Debug.java b/core/java/android/os/Debug.java
//Synthetic comment -- index b4f64b6..699306f 100644

//Synthetic comment -- @@ -92,7 +92,8 @@
/**
* Default trace file path and file
*/
    private static final String DEFAULT_TRACE_PATH_PREFIX =
        Environment.getExternalStorageDirectory().getPath() + "/";
private static final String DEFAULT_TRACE_BODY = "dmtrace";
private static final String DEFAULT_TRACE_EXTENSION = ".trace";
private static final String DEFAULT_TRACE_FILE_PATH =
//Synthetic comment -- @@ -125,7 +126,7 @@
public int otherPrivateDirty;
/** The shared dirty pages used by everything else. */
public int otherSharedDirty;

public MemoryInfo() {
}

//Synthetic comment -- @@ -135,21 +136,21 @@
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
//Synthetic comment -- @@ -177,7 +178,7 @@
otherPrivateDirty = source.readInt();
otherSharedDirty = source.readInt();
}

public static final Creator<MemoryInfo> CREATOR = new Creator<MemoryInfo>() {
public MemoryInfo createFromParcel(Parcel source) {
return new MemoryInfo(source);
//Synthetic comment -- @@ -447,7 +448,7 @@
* Like startMethodTracing(String, int, int), but taking an already-opened
* FileDescriptor in which the trace is written.  The file name is also
* supplied simply for logging.  Makes a dup of the file descriptor.
     *
* Not exposed in the SDK unless we are really comfortable with supporting
* this and find it would be useful.
* @hide
//Synthetic comment -- @@ -1010,7 +1011,7 @@
*    static {
*        // Sets all the fields
*        Debug.setFieldsOn(MyDebugVars.class);
     *
*        // Sets only the fields annotated with @Debug.DebugProperty
*        // Debug.setFieldsOn(MyDebugVars.class, true);
*    }








//Synthetic comment -- diff --git a/core/java/android/provider/Telephony.java b/core/java/android/provider/Telephony.java
//Synthetic comment -- index d8c5a53..eed3bea 100644

//Synthetic comment -- @@ -26,6 +26,7 @@
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.util.Regex;
//Synthetic comment -- @@ -1493,7 +1494,8 @@
* which streams the captured image to the uri. Internally we write the media content
* to this file. It's named '.temp.jpg' so Gallery won't pick it up.
*/
            public static final String SCRAP_FILE_PATH =
                Environment.getExternalStorageDirectory().getPath() + "/mms/scrapSpace/.temp.jpg";
}

public static final class Intents {








//Synthetic comment -- diff --git a/core/java/com/android/internal/os/SamplingProfilerIntegration.java b/core/java/com/android/internal/os/SamplingProfilerIntegration.java
//Synthetic comment -- index 51d9570..6026875 100644

//Synthetic comment -- @@ -69,7 +69,8 @@
pending = true;
snapshotWriter.execute(new Runnable() {
public void run() {
                    String dir =
                        Environment.getExternalStorageDirectory().getPath() + "/snapshots";
if (!dirMade) {
new File(dir).mkdirs();
if (new File(dir).isDirectory()) {







