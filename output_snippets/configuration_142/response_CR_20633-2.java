//<Beginning of snippet n. 0>
SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_X);
str = new SpannableString("XYZW");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));

KeyCharacterMap kcm = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
KeyEvent[] events = kcm.getEvents("XYZW".toCharArray());
for (KeyEvent event : events) {
    assertEquals('\0', mNumberKeyListener.lookup(event, str));
}

try {
//<End of snippet n. 0>