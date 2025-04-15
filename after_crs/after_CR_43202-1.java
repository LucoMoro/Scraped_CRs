/*PhoneUI hardware keyboard support

Only XLargeUi(tablet) had support for some key events such as
KEYCODE_DPAD_UP to edit the url. But a hardware keyboard can be
connected to a phone so it should have the same behavior.

The code to handle key events has been moved from XLargeUi to
BaseUI, the common super-class for XLargeUi and PhoneUi.

Change-Id:I55998979ca52e2a10abcb3ba877a2533dd18be17*/




//Synthetic comment -- diff --git a/src/com/android/browser/BaseUi.java b/src/com/android/browser/BaseUi.java
//Synthetic comment -- index c749465..bb195f5 100644

//Synthetic comment -- @@ -33,6 +33,7 @@
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
//Synthetic comment -- @@ -215,6 +216,33 @@
updateUrlBarAutoShowManagerTarget();
}

    public boolean dispatchKey(int code, KeyEvent event) {
        if (mActiveTab != null) {
            WebView web = mActiveTab.getWebView();
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                switch (code) {
                    case KeyEvent.KEYCODE_TAB:
                    case KeyEvent.KEYCODE_DPAD_UP:
                    case KeyEvent.KEYCODE_DPAD_LEFT:
                        if ((web != null) && web.hasFocus() && !mTitleBar.hasFocus()) {
                            editUrl(false, false);
                            return true;
                        }
                }
                boolean ctrl = event.hasModifiers(KeyEvent.META_CTRL_ON);
                if (!ctrl && isTypingKey(event) && !mTitleBar.isEditingUrl()) {
                    editUrl(true, false);
                    return mContentView.dispatchKeyEvent(event);
                }
            }
        }
        return false;
    }

    private boolean isTypingKey(KeyEvent evt) {
        return evt.getUnicodeChar() > 0;
    }

// Tab callbacks
@Override
public void onTabDataChanged(Tab tab) {








//Synthetic comment -- diff --git a/src/com/android/browser/PhoneUi.java b/src/com/android/browser/PhoneUi.java
//Synthetic comment -- index 89eae70..cc73389 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import android.util.Log;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
//Synthetic comment -- @@ -100,11 +99,6 @@
}

@Override
public void onProgressChanged(Tab tab) {
super.onProgressChanged(tab);
if (mNavScreen == null && getTitleBar().getHeight() > 0) {








//Synthetic comment -- diff --git a/src/com/android/browser/XLargeUi.java b/src/com/android/browser/XLargeUi.java
//Synthetic comment -- index 3b8245a..fb6142e 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import android.os.Handler;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
//Synthetic comment -- @@ -241,34 +240,6 @@
checkHideActionBar();
}

TabBar getTabBar() {
return mTabBar;
}







