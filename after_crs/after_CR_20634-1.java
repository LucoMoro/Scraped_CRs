/*Revert "fix keytests for azerty keyboard"

This reverts commit 444e0e6a6a04b492e828fe6d6bd976c2fd2fa64c.

The test didn't work on Nexus S after azerty fix. Probably
something like KeyCharactreMap#getEvents will need to be used
instead.

Change-Id:I8353846492265d76b5c4fdc19806a919b326b01f*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java
//Synthetic comment -- index bf1b6fb..5c0b9f1 100644

//Synthetic comment -- @@ -128,8 +128,8 @@
SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

        KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A);
        str = new SpannableString("ABCD");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));

try {







