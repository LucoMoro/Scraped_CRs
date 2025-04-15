/*Fixing spinner dialog crash upon orientation changes.

Bug:http://code.google.com/p/android/issues/detail?id=4936Calling dismiss() on the Spinner internal dialog inside
onDetachedFromWindow() crashes since the window has been
removed. See crash stack trace details on issue's page.

Change-Id:Iead225975701fc8c2f7475f4ed6572dba2578f84Signed-off-by: David Sobreira Marques <dpsmarques@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/widget/Spinner.java b/core/java/android/widget/Spinner.java
//Synthetic comment -- index 8ddb06c..f08d9db 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;


/**
* A view that displays one child at a time and lets the user pick among them.
//Synthetic comment -- @@ -82,13 +82,12 @@
}

@Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        
if (mPopup != null && mPopup.isShowing()) {
mPopup.dismiss();
mPopup = null;
}
}

/**







