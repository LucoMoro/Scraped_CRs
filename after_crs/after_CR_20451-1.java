/*fix keytests for azerty keyboard

Azerty keymap is different when compared to Qwerty. A and 1 are printed on the same key.
The fix detects if the keypad is azerty, and check for appropriate keys.

Change-Id:Ia7a570167dc875eca9365171f19f19e056113ffa*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java
//Synthetic comment -- index 5c0b9f1..6709f5f 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.ToBeFixed;

import android.os.SystemProperties;
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
//Synthetic comment -- @@ -123,12 +124,18 @@
@ToBeFixed(bug = "1695243", explanation = "Android API javadocs are incomplete.")
public void testLookup() {
mNumberKeyListener = new MockNumberKeyListener();
	String keypad = SystemProperties.get("ro.device.keypad","qwerty");

KeyEvent event1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_0);
SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

	KeyEvent event2;
	if(keypad!=null && keypad.equals("azerty")==true) {
        	event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_B);
	} else {
        	event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A);
	}
str = new SpannableString("ABCD");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));








