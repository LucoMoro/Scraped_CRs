/*Skip the test for alphabetic shortcut key in case of 12-key keyboard

Change-Id:I495a6af116a2afead95a88ee7e483d43e5432e45*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/WindowTest.java b/tests/tests/view/src/android/view/cts/WindowTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 527f444..5acd350

//Synthetic comment -- @@ -39,6 +39,7 @@
import android.util.DisplayMetrics;
import android.view.accessibility.AccessibilityEvent;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
//Synthetic comment -- @@ -572,6 +573,13 @@
args = {int.class, android.view.KeyEvent.class}
)
public void testIsShortcutKey() throws Throwable {
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            // Don't test.
            // because it's impossible to send 'q' & 'f' key event in NUMERIC keyboard.
            return;
        }
runTestOnUiThread(new Runnable() {
public void run() {
mActivity.openOptionsMenu();







