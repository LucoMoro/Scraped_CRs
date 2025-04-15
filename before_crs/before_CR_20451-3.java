/*fix keytests for azerty keyboard

Azerty keymap is different when compared to Qwerty.
Using "A", fails the testcase in case of azerty, hence
use "X" which is the same in both azerty and qwerty.

Change-Id:Ia7a570167dc875eca9365171f19f19e056113ffa*/
//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java
//Synthetic comment -- index 5c0b9f1..bf1b6fb 100644

//Synthetic comment -- @@ -128,8 +128,8 @@
SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

        KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A);
        str = new SpannableString("ABCD");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));

try {







