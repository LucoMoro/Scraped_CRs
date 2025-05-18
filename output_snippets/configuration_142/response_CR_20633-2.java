//<Beginning of snippet n. 0>

SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_X);
str = new SpannableString("XYZW");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));

try {
    KeyCharacterMap keyMap = KeyCharacterMap.load(KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD));
    KeyEvent[] events = keyMap.getEvents(new char[]{'X'});
    
    if (events != null && events.length > 0) {
        KeyEvent event3 = events[0];
        assertEquals(KeyEvent.KEYCODE_X, event3.getKeyCode());
    }

} catch (Exception e) {
    Log.e("KeyEventError", "Failed to process key events: " + e.getMessage(), e);
}

//<End of snippet n. 0>