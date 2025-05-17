//<Beginning of snippet n. 0>


import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.content.Context;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;

import com.android.settings.R;
import com.android.settings.deviceinfo.StorageMeasurement.FileInfo;

import java.util.ArrayList;
import java.util.List;

lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
lv.setMultiChoiceModeListener(new ModeCallback(this));
setListAdapter(mAdapter);
} 

private class ModeCallback implements ListView.MultiChoiceModeListener {
private int mDataCount;
private final Context mContext;
private final List<FileInfo> toRemove = new ArrayList<>();

public ModeCallback(Context context) {
mContext = context;
mDataCount = mAdapter.getCount();
}

public boolean onCreateActionMode(ActionMode mode, Menu menu) {
for (int i = 0; i < mAdapter.getCount(); i++) {
    FileInfo file = mAdapter.getItem(i);
    if (file != null && fileExists(file)) {
        if (file.isDirectory()) {
            deleteDir(file);
        } else {
            if (isMtpManaged(file)) {
                try {
                    // handle MTP deletion
                } catch (Exception e) {
                    Log.e("DeletionError", "Failed to delete MTP file: " + file.getPath(), e);
                    // Notify user of failure
                }
            } else {
                try {
                    if (!file.delete()) {
                        Log.e("DeletionError", "Failed to delete file: " + file.getPath());
                        // Notify user of failure
                    }
                } catch (Exception e) {
                    Log.e("DeletionError", "Error deleting file: " + file.getPath(), e);
                    // Notify user of failure
                }
            }
        }
        toRemove.add(file);
    }
}
mAdapter.removeAll(toRemove);
mDataCount = mAdapter.getCount(); // Update data count after deletion
mode.finish(); // Finish action mode
return true;
}

private boolean fileExists(FileInfo file) {
    if (file != null && file.getPath() != null) {
        File f = new File(file.getPath());
        return f.exists() && f.canRead();
    }
    return false; 
}

private boolean isMtpManaged(FileInfo file) {
    // Implement logic to determine if the file is managed under MTP
    return false; // Replace with actual check
}

//<End of snippet n. 0>
