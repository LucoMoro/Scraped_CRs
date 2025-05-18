//<Beginning of snippet n. 0>
import android.view.KeyEvent;

/**
 * KeyListener for AZERTY hardware.
 */
public class AZERTYKeyListener {

    private boolean isMetaPressed;

    public void setMetaPressed(boolean state) {
        this.isMetaPressed = state;
    }

    public void lookup(int keyCode) {
        if (isMetaPressed) {
            handleAlphaInput(keyCode);
        } else {
            handleDigitInput(keyCode);
        }
    }

    protected void handleAlphaInput(int keyCode) {
        // Implementation for handling alpha input when META is pressed
        // Example: Map keyCode to corresponding alpha character
    }

    protected void handleDigitInput(int keyCode) {
        // Implementation for handling digit input when META is not pressed
        // Example: Map keyCode to corresponding digit character
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import android.view.KeyEvent;

/**
 * KeyListener for AZERTY hardware with additional functionality.
 */
public class ExtendedAZERTYKeyListener extends AZERTYKeyListener {

    @Override
    public void lookup(int keyCode) {
        if (isMetaPressed) {
            handleAlphaInput(keyCode);
        } else {
            handleDigitInput(keyCode);
        }
    }

    @Override
    protected void handleAlphaInput(int keyCode) {
        // Additional extended functionality for alpha input based on META key state
        super.handleAlphaInput(keyCode);
    }

    @Override
    protected void handleDigitInput(int keyCode) {
        // Additional extended functionality for digit input based on META key state
        super.handleDigitInput(keyCode);
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import android.view.KeyEvent;
import android.text.InputType;

/**
 * For entering times in a text field.
 */
public class TimeKeyListener {

    private boolean isMetaPressed;

    public void setMetaPressed(boolean state) {
        this.isMetaPressed = state;
    }

    public void handleInput(int keyCode) {
        if (isMetaPressed) {
            handleTimeInputWithMeta(keyCode);
        } else {
            handleNormalTimeInput(keyCode);
        }
    }

    private void handleTimeInputWithMeta(int keyCode) {
        // Implementation for time input handling when META is pressed
    }

    private void handleNormalTimeInput(int keyCode) {
        // Implementation for normal time input handling when META is not pressed
    }
}
//<End of snippet n. 2>