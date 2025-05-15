//<Beginning of snippet n. 0>

import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;
import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;

import com.android.settings.R;
import com.android.settings.deviceinfo.StorageMeasurement.FileInfo;

import java.util.ArrayList;

lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
lv.setMultiChoiceModeListener(new ModeCallback(this));
setListAdapter(mAdapter);

private class ModeCallback implements ListView.MultiChoiceModeListener {
    private int mDataCount;
    private final Context mContext;
    private ArrayList<FileInfo> toRemove;

    public ModeCallback(Context context) {
        mContext = context;
        mDataCount = mAdapter.getCount();
        toRemove = new ArrayList<>();
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        // Initialize the ActionMode if needed
        return true;
    }

    public void onDestroyActionMode(ActionMode mode) {
        // Clear any selections
        mAdapter.clearSelections();
    }

    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false; // No update required for menu
    }

    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        // Track item selection
        mode.setTitle(mAdapter.getSelectedCount() + " selected");
    }

    public void onDeleteSelectedFiles() {
        for (int i = 0; i < mAdapter.getCount(); i++) {
            FileInfo file = mAdapter.getItem(i);
            if (file.isChecked()) {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    file.delete();
                }
                toRemove.add(file);
                Toast.makeText(mContext, "Deleted: " + file.getName(), Toast.LENGTH_SHORT).show();
            }
        }
        mAdapter.removeAll(toRemove);
    }

    private void deleteDir(FileInfo dir) {
        // Implementation for deleting directories
    }
}

//<End of snippet n. 0>