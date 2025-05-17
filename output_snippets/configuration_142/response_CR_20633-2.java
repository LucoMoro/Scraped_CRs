//<Beginning of snippet n. 0>
SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_X);
str = new SpannableString("XYZW");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));

// Complete reversion of changes related to commit 444e0e6a6a04b492e828fe6d6bd976c2fd2fa64c
// Implementing key event handling as per KeyCharacterMap#getEvents
KeyCharacterMap keyCharacterMap = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
KeyEvent[] events = keyCharacterMap.getEvents(new char[] {'0'});

// Additional device testing would need to occur separately.
// Document any discrepancies encountered during testing of azerty keyboards.
//<End of snippet n. 0>