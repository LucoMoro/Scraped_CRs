//<Beginning of snippet n. 0>
import android.view.KeyEvent;
import android.text.InputType;

/**
* For entering times in a text field.
*/
public class TimeInputKeyListener extends BaseKeyListener {

    private boolean isMetaKeyPressed;

    public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            isMetaKeyPressed = event.isAltPressed() || event.isCtrlPressed();
            return handleKeyInput(keyCode, text);
        }
        return false;
    }

    private boolean handleKeyInput(int keyCode, Editable text) {
        if (isMetaKeyPressed) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_A:
                    text.append("1"); // Example numeric output for AZERTY
                    return true;
                case KeyEvent.KEYCODE_B:
                    text.append("2"); // Example numeric output for AZERTY
                    return true;
                // Add cases for other relevant keys.
            }
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_A:
                    text.append("a"); // Alphabetic output
                    return true;
                case KeyEvent.KEYCODE_B:
                    text.append("b"); // Alphabetic output
                    return true;
                // Add cases for other relevant keys.
            }
        }
        return false;
    }
}

//<End of snippet n. 0>


//<Beginning of snippet n. 1>
import android.view.KeyEvent;
import android.text.InputType;

/**
* The characters that are used.
*/
public class AlphaNumericKeyListener extends BaseKeyListener {

    private boolean isMetaKeyPressed;

    public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            isMetaKeyPressed = event.isAltPressed() || event.isCtrlPressed();
            return handleKeyInput(keyCode, text);
        }
        return false;
    }

    private boolean handleKeyInput(int keyCode, Editable text) {
        if (isMetaKeyPressed) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_1:
                    text.append("!"); // Example for meta key functionality
                    return true;
                case KeyEvent.KEYCODE_2:
                    text.append("@");
                    return true;
                // Add cases for other relevant keys.
            }
        } else {
            switch (keyCode) {
                case KeyEvent.KEYCODE_1:
                    text.append("1"); // Numeric output
                    return true;
                case KeyEvent.KEYCODE_2:
                    text.append("2");
                    return true;
                // Add cases for other relevant keys.
            }
        }
        return false;
    }
}

//<End of snippet n. 1>


//<Beginning of snippet n. 2>
import android.view.KeyEvent;
import android.text.InputType;

/**
* For entering times in a text field.
*/
public class TimeInputKeyListener extends BaseKeyListener {

    private boolean isMetaKeyPressed;

    public boolean onKeyDown(View view, Editable text, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            isMetaKeyPressed = event.isAltPressed() || event.isCtrlPressed();
            return handleKeyInput(keyCode, text);
        }
        return false;
    }

    private boolean handleKeyInput(int keyCode, Editable text) {
        if (isMetaKeyPressed) {
            // Handle meta key scenarios
            switch (keyCode) {
                case KeyEvent.KEYCODE_H:
                    text.append("12"); // Example for time entry
                    return true;
                // Add cases for other relevant keys as needed.
            }
        } else {
            // Handle normal key input
            switch (keyCode) {
                case KeyEvent.KEYCODE_H:
                    text.append("H"); // Regular input
                    return true;
                // Add cases for other relevant keys as needed.
            }
        }
        return false;
    }
}

//<End of snippet n. 2>