/*Setting: DeviceInfo Deleted files also can be seen in DUT's Storage.

MiscFilesHandler doesn't consider MTP case and add support.

Change-Id:I1587ee551d389a34728624ab5f00a05835ba1666Author: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Bo Huang <bo.b.huang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 65345*/




//Synthetic comment -- diff --git a/src/com/android/settings/deviceinfo/MiscFilesHandler.java b/src/com/android/settings/deviceinfo/MiscFilesHandler.java
//Synthetic comment -- index 1e0cc46..7fce780 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageVolume;
import android.text.format.Formatter;
//Synthetic comment -- @@ -70,15 +73,77 @@
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
//Synthetic comment -- @@ -117,8 +182,10 @@
if (file.isDirectory()) {
deleteDir(file);
} else {
                            file.delete();
}

                        mScannerClient.setScanPath(file.getAbsolutePath());
toRemove.add(mAdapter.getItem(i));
}
mAdapter.removeAll(toRemove);







