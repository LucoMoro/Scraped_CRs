//<Beginning of snippet n. 0>

SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_X);
str = new SpannableString("XYZW");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));

KeyCharacterMap keyMap = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
for (int keyCode = 0; keyCode < KeyEvent.KEYCODE_MEDIA; keyCode++) {
    KeyEvent[] events = keyMap.getEvents(new int[]{keyCode});
    if (events != null && events.length > 0) {
        for (KeyEvent evt : events) {
            char character = mNumberKeyListener.lookup(evt, str);
            if (character == '\0') {
                Log.e("KeyListenerError", "Invalid lookup for key code: " + keyCode);
            }
        }
    }
}

try {

//<End of snippet n. 0>