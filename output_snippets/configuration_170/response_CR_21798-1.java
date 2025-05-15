//<Beginning of snippet n. 0>

import android.view.KeyEvent;
import android.text.InputType;

/**
 * For managing keyboard input. 
 */
public class CustomKeyListener implements View.OnKeyListener {
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            boolean metaHeld = event.isAltPressed() || event.isMetaPressed();
            handleKey(keyCode, metaHeld);
        }
        return false;
    }

    private void handleKey(int keyCode, boolean metaHeld) {
        // Dual function logic based on the META key state
        if (metaHeld) {
            // Logic for when the META key is held
        } else {
            // Logic for normal key handling
        }
    }
}

/**
 * The characters that are used.
 *
 * @see KeyEvent#getMatch
 */
//<End of snippet n. 0>

//<Beginning of snippet n. 1>

import android.view.KeyEvent;

/**
 * For managing keyboard input on AZERTY layout specifically.
 */
public class AZERTYKeyListener implements View.OnKeyListener {
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            boolean metaHeld = event.isAltPressed() || event.isMetaPressed();
            handleKey(keyCode, metaHeld);
        }
        return false;
    }

    private void handleKey(int keyCode, boolean metaHeld) {
        // Adjusted dual function logic for AZERTY layout
        if (metaHeld) {
            // Specific function when META is held on AZERTY
        } else {
            // Normal key handling
        }
    }
}

/**
 * The characters that are used.
 *
 * @see KeyEvent#getMatch
 */
//<End of snippet n. 1>

//<Beginning of snippet n. 2>

import android.view.KeyEvent;
import android.text.InputType;

/**
 * For entering times in a text field.
 */
public class TimeKeyListener implements View.OnKeyListener {
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            boolean metaHeld = event.isAltPressed() || event.isMetaPressed();
            handleKey(keyCode, metaHeld);
        }
        return false;
    }

    private void handleKey(int keyCode, boolean metaHeld) {
        // Logic for specific key input considering META state
        if (metaHeld) {
            // Handle numeric input when META is pressed
        } else {
            // Handle normal input
        }
    }
}

/**
 * The characters that are used.
 *
 * @see KeyEvent#getMatch
 */
//<End of snippet n. 2>