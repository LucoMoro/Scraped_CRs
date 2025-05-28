
//<Beginning of snippet n. 0>


/**
* Default trace file path and file
*/
    private static final String DEFAULT_TRACE_PATH_PREFIX = "/sdcard/";
private static final String DEFAULT_TRACE_BODY = "dmtrace";
private static final String DEFAULT_TRACE_EXTENSION = ".trace";
private static final String DEFAULT_TRACE_FILE_PATH =
public int otherPrivateDirty;
/** The shared dirty pages used by everything else. */
public int otherSharedDirty;
        
public MemoryInfo() {
}

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
otherPrivateDirty = source.readInt();
otherSharedDirty = source.readInt();
}
        
public static final Creator<MemoryInfo> CREATOR = new Creator<MemoryInfo>() {
public MemoryInfo createFromParcel(Parcel source) {
return new MemoryInfo(source);
* Like startMethodTracing(String, int, int), but taking an already-opened
* FileDescriptor in which the trace is written.  The file name is also
* supplied simply for logging.  Makes a dup of the file descriptor.
     * 
* Not exposed in the SDK unless we are really comfortable with supporting
* this and find it would be useful.
* @hide
*    static {
*        // Sets all the fields
*        Debug.setFieldsOn(MyDebugVars.class);
     * 
*        // Sets only the fields annotated with @Debug.DebugProperty
*        // Debug.setFieldsOn(MyDebugVars.class, true);
*    }

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.util.Regex;
* which streams the captured image to the uri. Internally we write the media content
* to this file. It's named '.temp.jpg' so Gallery won't pick it up.
*/
            public static final String SCRAP_FILE_PATH = "/sdcard/mms/scrapSpace/.temp.jpg";
}

public static final class Intents {

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


pending = true;
snapshotWriter.execute(new Runnable() {
public void run() {
                    String dir = "/sdcard/snapshots";
if (!dirMade) {
new File(dir).mkdirs();
if (new File(dir).isDirectory()) {

//<End of snippet n. 2>








