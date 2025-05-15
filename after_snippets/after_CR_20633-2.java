
//<Beginning of snippet n. 0>


SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

        KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A);
        str = new SpannableString("ABCD");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));

try {

//<End of snippet n. 0>








