//<Beginning of snippet n. 0>


/**
* Default trace file path and file
*/
private static final String DEFAULT_TRACE_PATH_PREFIX = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
private static final String DEFAULT_TRACE_BODY = "dmtrace";
private static final String DEFAULT_TRACE_EXTENSION = ".trace";
private static final String DEFAULT_TRACE_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/trace" + DEFAULT_TRACE_EXTENSION; // assuming trace filename

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
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.util.Regex;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.widget.Toast;

/**
* which streams the captured image to the uri. Internally we write the media content
* to this file. It's named '.temp.jpg' so Gallery won't pick it up.
*/
public static final String SCRAP_FILE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/mms/scrapSpace/.temp.jpg";

public static boolean checkPermissions(Context context) {
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions((Activity) context,
            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        return false;
    }
    return true;
}

}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>


pending = true;
snapshotWriter.execute(new Runnable() {
public void run() {
String dir = Environment.getExternalStorageDirectory().getAbsolutePath() + "/snapshots";
if (!dirMade) {
    File directory = new File(dir);
    if (!directory.exists() && !directory.mkdirs()) {
        // Handle error if the directory could not be created
        Toast.makeText(context, "Unable to create snapshots directory.", Toast.LENGTH_SHORT).show();
        return;
    }
    if (directory.isDirectory()) {
//<End of snippet n. 2>