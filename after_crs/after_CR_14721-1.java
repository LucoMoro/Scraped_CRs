/*Remove SYM isPrintingKey False Check

Bug 2586346

Some vendors desire to share the SYM key with printable characters.
Therefore, take out the check that enforced that isPrintingKey
returned false when given SYM. Leave the other false checks though,
to make sure the method doesn't always return true.

Change-Id:I51b1a19100e62d75674de71777eeb046a93f348f*/




//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/KeyCharacterMapTest.java b/tests/tests/view/src/android/view/cts/KeyCharacterMapTest.java
//Synthetic comment -- index 38d6dfb..4ab38df 100644

//Synthetic comment -- @@ -114,7 +114,6 @@
assertFalse(mKeyCharacterMap.isPrintingKey(KeyEvent.KEYCODE_TAB));
assertFalse(mKeyCharacterMap.isPrintingKey(KeyEvent.KEYCODE_SPACE));
assertFalse(mKeyCharacterMap.isPrintingKey(KeyEvent.KEYCODE_NUM));
assertFalse(mKeyCharacterMap.isPrintingKey(KeyEvent.KEYCODE_EXPLORER));
assertFalse(mKeyCharacterMap.isPrintingKey(KeyEvent.KEYCODE_ENVELOPE));
assertFalse(mKeyCharacterMap.isPrintingKey(KeyEvent.KEYCODE_ENTER));







