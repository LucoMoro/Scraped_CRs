/*Removed Calls to deprecated APIs

Change-Id:I3f9b6a8d3c8a050156a6cc7ea0eb9de33b82f79a*/




//Synthetic comment -- diff --git a/cmds/am/src/com/android/commands/am/Am.java b/cmds/am/src/com/android/commands/am/Am.java
//Synthetic comment -- index 13e6d5c..20eaf05 100644

//Synthetic comment -- @@ -160,7 +160,7 @@
String uri = nextArg();
if (uri != null) {
Intent oldIntent = intent;
            intent = Intent.parseUri(uri, 0);
if (oldIntent.getAction() != null) {
intent.setAction(oldIntent.getAction());
}








//Synthetic comment -- diff --git a/core/java/android/preference/DialogPreference.java b/core/java/android/preference/DialogPreference.java
//Synthetic comment -- index cc48aeb..bbad2b6 100644

//Synthetic comment -- @@ -33,7 +33,6 @@
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

/**
//Synthetic comment -- @@ -275,7 +274,7 @@
protected void showDialog(Bundle state) {
Context context = getContext();

        mWhichButtonClicked = DialogInterface.BUTTON_NEGATIVE;

mBuilder = new AlertDialog.Builder(context)
.setTitle(mDialogTitle)








//Synthetic comment -- diff --git a/core/java/android/widget/Gallery.java b/core/java/android/widget/Gallery.java
//Synthetic comment -- index f34823c..1ed6b16 100644

//Synthetic comment -- @@ -1087,7 +1087,7 @@
@Override
public boolean dispatchKeyEvent(KeyEvent event) {
// Gallery steals all key events
        return event.dispatch(this, null, null);
}

/**








//Synthetic comment -- diff --git a/core/java/com/android/internal/app/NetInitiatedActivity.java b/core/java/com/android/internal/app/NetInitiatedActivity.java
//Synthetic comment -- index 98fb236..36f45b2 100755

//Synthetic comment -- @@ -23,15 +23,9 @@
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;
import android.util.Log;
import android.location.LocationManager;
import com.android.internal.location.GpsNetInitiatedHandler;

/**
//Synthetic comment -- @@ -45,8 +39,8 @@
private static final boolean DEBUG = true;
private static final boolean VERBOSE = false;

    private static final int POSITIVE_BUTTON = AlertDialog.BUTTON_POSITIVE;
    private static final int NEGATIVE_BUTTON = AlertDialog.BUTTON_NEGATIVE;

// Dialog button text
public static final String BUTTON_TEXT_ACCEPT = "Accept";







