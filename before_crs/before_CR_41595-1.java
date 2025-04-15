/*Phone: Fix wrong dialog shown on Change PIN2 selection

FdnSetting should be stored in a persistent storage so
that correct dialog is shown on re-entry and reboot of the
device.

Change-Id:If6fb76be3550795188a9fa4dcef8ef615b5ddfbcAuthor: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 11974*/
//Synthetic comment -- diff --git a/src/com/android/phone/FdnSetting.java b/src/com/android/phone/FdnSetting.java
//Synthetic comment -- index 4f21e2f..c19a5a8 100644

//Synthetic comment -- @@ -19,6 +19,8 @@
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncResult;
import android.os.Bundle;
import android.os.Handler;
//Synthetic comment -- @@ -369,6 +371,7 @@
displayPinChangeDialog(0, false);
mOldPin = mNewPin = "";
mIsPuk2Locked = false;
}

/**
//Synthetic comment -- @@ -380,6 +383,7 @@
displayPinChangeDialog(0, false);
mOldPin = mNewPin = mPuk2 = "";
mIsPuk2Locked = true;
}

/**
//Synthetic comment -- @@ -436,18 +440,6 @@

mButtonChangePin2.setOnPinEnteredListener(this);

        // Only reset the pin change dialog if we're not in the middle of changing it.
        if (icicle == null) {
            resetPinChangeState();
        } else {
            mIsPuk2Locked = icicle.getBoolean(SKIP_OLD_PIN_KEY);
            mPinChangeState = icicle.getInt(PIN_CHANGE_STATE_KEY);
            mOldPin = icicle.getString(OLD_PIN_KEY);
            mNewPin = icicle.getString(NEW_PIN_KEY);
            mButtonChangePin2.setDialogMessage(icicle.getString(DIALOG_MESSAGE_KEY));
            mButtonChangePin2.setText(icicle.getString(DIALOG_PIN_ENTRY_KEY));
        }

ActionBar actionBar = getActionBar();
if (actionBar != null) {
// android.R.id.home will be triggered in onOptionsItemSelected()
//Synthetic comment -- @@ -460,20 +452,33 @@
super.onResume();
mPhone = PhoneApp.getPhone();
updateEnableFDN();
}

    /**
     * Save the state of the pin change.
     */
@Override
    protected void onSaveInstanceState(Bundle out) {
        super.onSaveInstanceState(out);
        out.putBoolean(SKIP_OLD_PIN_KEY, mIsPuk2Locked);
        out.putInt(PIN_CHANGE_STATE_KEY, mPinChangeState);
        out.putString(OLD_PIN_KEY, mOldPin);
        out.putString(NEW_PIN_KEY, mNewPin);
        out.putString(DIALOG_MESSAGE_KEY, mButtonChangePin2.getDialogMessage().toString());
        out.putString(DIALOG_PIN_ENTRY_KEY, mButtonChangePin2.getText());
}

@Override







