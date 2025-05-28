
//<Beginning of snippet n. 0>


import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

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
if (file.isDirectory()) {
deleteDir(file);
} else {
                            file.delete();                            
}
toRemove.add(mAdapter.getItem(i));
}
mAdapter.removeAll(toRemove);

//<End of snippet n. 0>








