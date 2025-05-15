
//<Beginning of snippet n. 0>


import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

import com.android.settings.R;
import com.android.settings.deviceinfo.StorageMeasurement.FileInfo;
lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
lv.setMultiChoiceModeListener(new ModeCallback(this));
setListAdapter(mAdapter);
    }

private class ModeCallback implements ListView.MultiChoiceModeListener {
private int mDataCount;
private final Context mContext;


        private final class ScannerClient implements MediaScannerConnectionClient {
            ArrayList<String> mPaths = new ArrayList<String>();
            private final Context mContext;
            private MediaScannerConnection mScannerConnection;
            boolean  mConnected;
            private Object mLock = new Object();

            public ScannerClient(Context context) {
                mContext = context;
                mScannerConnection = new MediaScannerConnection(context, this);
            }

            public void setScanPath(String path) {
                synchronized (mLock) {
                    if (mConnected) {
                        mScannerConnection.scanFile(path, null);
                    } else {
                        mPaths.add(path);
                        mScannerConnection.connect();
                    }
                }
            }

            @Override
            public void onMediaScannerConnected() {
                synchronized (mLock) {
                    mConnected = true;
                    if (!mPaths.isEmpty()) {
                        for (String path: mPaths) {
                            mScannerConnection.scanFile(path, null);
                        }
                    }
                }
             }

            public void disconnect() {
                if (mScannerConnection.isConnected()) {
                    mScannerConnection.disconnect();
                }
            }

            @Override
            public void onScanCompleted(String path, Uri uri) {
                int ret = mContext.getContentResolver().delete(uri, null, null);
                synchronized (mLock) {
                    if (mPaths != null) {
                        mPaths.remove(path);
                        if (mConnected && (mPaths.size() == 0)) {
                            mConnected = false;
                            disconnect();
                        }
                    }
                }
            }

        }

        //private MediaScannerConnection mScannerConnection;
        private ScannerClient mScannerClient;

public ModeCallback(Context context) {
mContext = context;
mDataCount = mAdapter.getCount();
            mScannerClient = new ScannerClient(context);
}

public boolean onCreateActionMode(ActionMode mode, Menu menu) {
if (file.isDirectory()) {
deleteDir(file);
} else {
                            file.delete();
}

                        mScannerClient.setScanPath(file.getAbsolutePath());
toRemove.add(mAdapter.getItem(i));
}
mAdapter.removeAll(toRemove);

//<End of snippet n. 0>








