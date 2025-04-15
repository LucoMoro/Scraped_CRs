/*Removed calls to Deprected APIs and unused Imports

Change-Id:I5e7778a4312cbe2ab6938a5bcd6b893418079c27*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/WarnOfStorageLimitsActivity.java b/src/com/android/mms/ui/WarnOfStorageLimitsActivity.java
//Synthetic comment -- index 0bd137d..b03bd0c 100644

//Synthetic comment -- @@ -23,8 +23,6 @@
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.android.internal.app.AlertActivity;
import com.android.internal.app.AlertController;
//Synthetic comment -- @@ -36,7 +34,7 @@
implements DialogInterface.OnClickListener {
private static final String LOG_TAG = "WarnOfStorageLimitsActivity";

    private static final int POSITIVE_BUTTON = AlertDialog.BUTTON_POSITIVE;

@Override
protected void onCreate(Bundle savedInstanceState) {







