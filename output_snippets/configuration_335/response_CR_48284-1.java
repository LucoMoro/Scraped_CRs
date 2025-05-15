//<Beginning of snippet n. 0>


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ActionMode;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

import com.android.settings.R;
import com.android.settings.deviceinfo.StorageMeasurement.FileInfo;

lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
lv.setMultiChoiceModeListener(new ModeCallback(this));
setListAdapter(mAdapter);
} 

private class ModeCallback implements ListView.MultiChoiceModeListener {
    private int mDataCount;
    private final Context mContext;

    public ModeCallback(Context context) {
        mContext = context;
        mDataCount = mAdapter.getCount();
    }

    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(R.string.confirm_delete)
                .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        deleteSelectedFiles();
                        mode.finish();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        builder.show();
        return true;
    }

    private void deleteSelectedFiles() {
        SparseBooleanArray checked = lv.getCheckedItemPositions();
        ArrayList<FileInfo> toRemove = new ArrayList<>();
        for (int i = 0; i < mDataCount; i++) {
            if (checked.get(i)) {
                FileInfo file = mAdapter.getItem(i);
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    file.delete();
                }
                toRemove.add(file);
            }
        }
        mAdapter.removeAll(toRemove);
        Toast.makeText(mContext, R.string.deleted_files, Toast.LENGTH_SHORT).show();
    }
}

//<End of snippet n. 0>