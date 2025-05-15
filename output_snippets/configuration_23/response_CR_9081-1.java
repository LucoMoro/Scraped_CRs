```
<<Beginning of snippet n. 0>>
case KeyEvent.KEYCODE_RINGER_ON:
    // Activate ringer mode
    setRingerMode(true);
    Toast.makeText(context, "Ringer On", Toast.LENGTH_SHORT).show();
    return true;

case KeyEvent.KEYCODE_RINGER_OFF:
    // Deactivate ringer mode
    setRingerMode(false);
    Toast.makeText(context, "Ringer Off", Toast.LENGTH_SHORT).show();
    return true;

case KeyEvent.KEYCODE_HEADSETHOOK: 
case KeyEvent.KEYCODE_PLAYPAUSE: 
<<End of snippet n. 0>>