//<Beginning of snippet n. 0>


import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import android.view.Menu;
import android.view.ActionMode;

import com.android.settings.R;
import com.android.settings.deviceinfo.StorageMeasurement.FileInfo;

lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
lv.setMultiChoiceModeListener(new ModeCallback(this));
setListAdapter(mAdapter);

private class ModeCallback implements ListView.MultiChoiceModeListener {
    private int mDataCount;
    private final Context mContext;
    private List<FileInfo> toRemove = new ArrayList<>();

    public ModeCallback(Context context) {
        mContext = context;
        mDataCount = mAdapter.getCount();
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    public void onDestroyActionMode(ActionMode mode) {
        toRemove.clear();
    }

    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        FileInfo file = mAdapter.getItem(position);
        if (checked) {
            toRemove.add(file);
        } else {
            toRemove.remove(file);
        }
        mode.setTitle(toRemove.size() + " selected");
    }

    public void onDeleteAction(ActionMode mode) {
        for (FileInfo file : toRemove) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                if (file.delete()) {
                    Toast.makeText(mContext, "File deleted: " + file.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Failed to delete file: " + file.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        }
        mAdapter.removeAll(toRemove);
        toRemove.clear();
        mode.finish();
    }
}

//<End of snippet n. 0>