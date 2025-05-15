//<Beginning of snippet n. 0>

SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_X);
str = new SpannableString("XYZW");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));

try {
    // Adding test cases to validate key detections post-reversion
    KeyEvent event3 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_1);
    str = new SpannableString("1234");
    assertEquals('1', mNumberKeyListener.lookup(event3, str));

    KeyEvent event4 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A);
    str = new SpannableString("ABCD");
    assertEquals('\0', mNumberKeyListener.lookup(event4, str));
} catch (Exception e) {
    e.printStackTrace();
}

//<End of snippet n. 0>