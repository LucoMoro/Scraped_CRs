//<Beginning of snippet n. 0>
import android.view.KeyEvent;

public class KeyListener {
    
    private boolean isMetaPressed;

    public void setMetaPressed(boolean pressed) {
        this.isMetaPressed = pressed;
    }

    public char lookup(int keyCode) {
        char result = '\0';
        if (KeyEvent.isModifierKey(keyCode)) {
            return result;
        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_A:
                result = isMetaPressed ? 'à' : 'a';
                break;
            case KeyEvent.KEYCODE_Z:
                result = isMetaPressed ? 'ź' : 'z';
                break;
            case KeyEvent.KEYCODE_Q:
                result = isMetaPressed ? 'é' : 'q';
                break;
            case KeyEvent.KEYCODE_W:
                result = isMetaPressed ? 'è' : 'w';
                break;
            case KeyEvent.KEYCODE_M:
                result = isMetaPressed ? 'ç' : 'm';
                break;
            case KeyEvent.KEYCODE_D:
                result = isMetaPressed ? 'ô' : 'd';
                break;
            case KeyEvent.KEYCODE_X:
                result = isMetaPressed ? 'ù' : 'x';
                break;
            case KeyEvent.KEYCODE_S:
                result = isMetaPressed ? 'ß' : 's';
                break;
            case KeyEvent.KEYCODE_0:
                result = isMetaPressed ? ')' : '0';
                break;
            case KeyEvent.KEYCODE_1:
                result = isMetaPressed ? '!' : '1';
                break;
            case KeyEvent.KEYCODE_2:
                result = isMetaPressed ? '@' : '2';
                break;
            case KeyEvent.KEYCODE_3:
                result = isMetaPressed ? '#' : '3';
                break;
            case KeyEvent.KEYCODE_4:
                result = isMetaPressed ? '$' : '4';
                break;
            case KeyEvent.KEYCODE_5:
                result = isMetaPressed ? '%' : '5';
                break;
            case KeyEvent.KEYCODE_6:
                result = isMetaPressed ? '^' : '6';
                break;
            // More cases for AZERTY mappings...
            default:
                break;
        }
        return result;
    }
    
}
//<End of snippet n. 0>


//<Beginning of snippet n. 1>
import android.view.KeyEvent;

public class HardwareKeyListener {

    private boolean isMetaPressed;

    public void setMetaPressed(boolean pressed) {
        this.isMetaPressed = pressed;
    }

    public char hardwareLookup(int keyCode) {
        char result = '\0';
        if (KeyEvent.isModifierKey(keyCode)) {
            return result;
        }

        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                result = isMetaPressed ? ')' : '0';
                break;
            case KeyEvent.KEYCODE_1:
                result = isMetaPressed ? '!' : '1';
                break;
            case KeyEvent.KEYCODE_2:
                result = isMetaPressed ? '@' : '2';
                break;
            case KeyEvent.KEYCODE_3:
                result = isMetaPressed ? '#' : '3';
                break;
            case KeyEvent.KEYCODE_4:
                result = isMetaPressed ? '$' : '4';
                break;
            case KeyEvent.KEYCODE_5:
                result = isMetaPressed ? '%' : '5';
                break;
            case KeyEvent.KEYCODE_6:
                result = isMetaPressed ? '^' : '6';
                break;
            // More cases for hardware key mappings...
            default:
                break;
        }
        return result;
    }
    
}
//<End of snippet n. 1>

 
//<Beginning of snippet n. 2>
import android.view.KeyEvent;

public class TimeInputKeyListener extends KeyListener {

    @Override
    public char lookup(int keyCode) {
        char result = super.lookup(keyCode);
        if (result == '\0') {
            if (KeyEvent.isModifierKey(keyCode)) {
                return result;
            }
            switch (keyCode) {
                case KeyEvent.KEYCODE_COLON:
                    result = ':';
                    break;
                case KeyEvent.KEYCODE_NUMPAD_0:
                    result = '0';
                    break;
                case KeyEvent.KEYCODE_NUMPAD_1:
                    result = '1';
                    break;
                case KeyEvent.KEYCODE_NUMPAD_2:
                    result = '2';
                    break;
                case KeyEvent.KEYCODE_NUMPAD_3:
                    result = '3';
                    break;
                // Add more key mappings for time input...
                default:
                    break;
            }
        }
        return result;
    }
    
}
//<End of snippet n. 2>