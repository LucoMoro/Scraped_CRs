<<Beginning of snippet n. 0>>
SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_X);
str = new SpannableString("XYZW");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));

KeyEvent event3 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_0);
str = new SpannableString("0");
assertEquals('0', mNumberKeyListener.lookup(event3, str));

KeyEvent event4 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Y);
str = new SpannableString("Y");
assertEquals('\0', mNumberKeyListener.lookup(event4, str));
<<End of snippet n. 0>>