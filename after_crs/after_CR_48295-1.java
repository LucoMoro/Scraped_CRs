/*Phone: SIM Contacts are not refreshed after SIM Hot Swap

This patch introduces a broadcast receiver for SIM Status and
when it is ABSENT resets the internal data, so as to re-read
the adn when activity is reloaded.

The new contacts will be loaded when the user re-enters the view.

Change-Id:I96587240b73a87718e13ce7a65b6081fb75ddeabAuthor: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 61192*/




//Synthetic comment -- diff --git a/src/com/android/phone/ADNList.java b/src/com/android/phone/ADNList.java
//Synthetic comment -- index b4e8ac7..389cf43 100644

//Synthetic comment -- @@ -21,9 +21,11 @@

import android.app.ListActivity;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//Synthetic comment -- @@ -34,6 +36,9 @@
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.android.internal.telephony.IccCardConstants;
import com.android.internal.telephony.TelephonyIntents;

/**
* ADN List activity for the Phone app.
*/
//Synthetic comment -- @@ -70,6 +75,23 @@

protected int mInitialSelection = -1;

    protected BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (TelephonyIntents.ACTION_SIM_STATE_CHANGED.equals(intent.getAction())) {
                String stateExtra = intent.getStringExtra(IccCardConstants.INTENT_KEY_ICC_STATE);
                if (stateExtra != null
                        && IccCardConstants.INTENT_VALUE_ICC_ABSENT.equals(stateExtra)) {
                    if (mCursor != null) {
                        mCursor.deactivate();
                    }
                    mCursorAdapter = null;
                    setListAdapter(null);
                    displayProgress(false);
                }
            }
        }
    };

@Override
protected void onCreate(Bundle icicle) {
super.onCreate(icicle);
//Synthetic comment -- @@ -82,10 +104,19 @@
@Override
protected void onResume() {
super.onResume();
        final IntentFilter intentFilter =
               new IntentFilter(TelephonyIntents.ACTION_SIM_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver, intentFilter);
query();
}

@Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mBroadcastReceiver);
    }

    @Override
protected void onStop() {
super.onStop();
if (mCursor != null) {







