/*The test branches to alphabetic or numeric one

he test branches to alphabetic or numeric one

In case of using 12-key keyboard it cannot enter alphabet key directly.
So the shortcut key test branches to alphabetic or numeric one
according to keyboard type.

This is a new patch instead of 21400/1

Change-Id:Ia49940d8caf1c88346d80d8f50de6070510b91cd*/




//Synthetic comment -- diff --git a/tests/src/android/view/cts/WindowStubActivity.java b/tests/src/android/view/cts/WindowStubActivity.java
old mode 100644
new mode 100755
//Synthetic comment -- index 24a971f..502c947

//Synthetic comment -- @@ -39,8 +39,8 @@

@Override
public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Quit").setShortcut('1', 'q');
        menu.add(Menu.NONE, Menu.NONE, Menu.NONE, "Action").setShortcut('2', 'a');
mIsOnCreateOptionsMenuCalled = true;
return super.onCreateOptionsMenu(menu);
}








//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/WindowTest.java b/tests/tests/view/src/android/view/cts/WindowTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 527f444..c851a19

//Synthetic comment -- @@ -39,6 +39,7 @@
import android.util.DisplayMetrics;
import android.view.accessibility.AccessibilityEvent;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
//Synthetic comment -- @@ -578,10 +579,19 @@
}
});
mInstrumentation.waitForIdleSync();
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            assertTrue(mWindow.isShortcutKey(KeyEvent.KEYCODE_1, new KeyEvent(KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_1)));
            assertFalse(mWindow.isShortcutKey(KeyEvent.KEYCODE_5, new KeyEvent(KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_5)));
        } else {
            assertTrue(mWindow.isShortcutKey(KeyEvent.KEYCODE_Q, new KeyEvent(KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_Q)));
            assertFalse(mWindow.isShortcutKey(KeyEvent.KEYCODE_F, new KeyEvent(KeyEvent.ACTION_DOWN,
                    KeyEvent.KEYCODE_F)));
        }
}

@TestTargets({







