//<Beginning of snippet n. 0>


/**
* Default trace file path and file
*/
private static final String DEFAULT_TRACE_PATH_PREFIX = Environment.getExternalStorageDirectory().getPath() + "/";
private static final String DEFAULT_TRACE_BODY = "dmtrace";
private static final String DEFAULT_TRACE_EXTENSION = ".trace";
private static final String DEFAULT_TRACE_FILE_PATH = DEFAULT_TRACE_PATH_PREFIX + DEFAULT_TRACE_BODY + DEFAULT_TRACE_EXTENSION;
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
};

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.text.util.Regex;
import android.os.Environment;

/**
* which streams the captured image to the uri. Internally we write the media content
* to this file. It's named '.temp.jpg' so Gallery won't pick it up.
*/
public static final String SCRAP_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + "/mms/scrapSpace/.temp.jpg";

}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import android.os.Environment;
import android.util.Log;

/**
 * Check if external storage is available for read and write
 */
private boolean isExternalStorageWritable() {
    String state = Environment.getExternalStorageState();
    if (!Environment.MEDIA_MOUNTED.equals(state)) {
        Log.e("StorageCheck", "External storage is not writable: " + state);
        return false;
    }
    return true;
}

pending = true;
snapshotWriter.execute(new Runnable() {
    public void run() {
        if (isExternalStorageWritable()) {
            String dir = Environment.getExternalStorageDirectory().getPath() + "/snapshots";
            File directory = new File(dir);
            if (!directory.exists()) {
                directory.mkdirs();
                if (directory.isDirectory()) {
                    // additional logic can be placed here if necessary
                }
            }
        }
    }
});

//<End of snippet n. 2>