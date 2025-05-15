
//<Beginning of snippet n. 0>


}

/**
     * Overrides the superclass's lookup method to prefer the number field
     * from the KeyEvent.
     */
    protected int lookup(KeyEvent event, Spannable content) {
        int KeyData = event.getUnicodeChar(getMetaState(content));

        if (ok(getAcceptedChars(), (char)keyData)) {
            return keyData;
        }
        return 0;
    }

    /**
* The characters that are used.
*
* @see KeyEvent#getMatch

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


}

/**
     * Overrides the superclass's lookup method to prefer the number field
     * from the KeyEvent.
     */
    protected int lookup(KeyEvent event, Spannable content) {
        int KeyData = event.getUnicodeChar(getMetaState(content));

        if (ok(getAcceptedChars(), (char)keyData)) {
            return keyData;
        }
        return 0;
    }

    /**
* The characters that are used.
*
* @see KeyEvent#getMatch

//<End of snippet n. 1>










//<Beginning of snippet n. 2>



import android.view.KeyEvent;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;

/**
* For entering times in a text field.
}

/**
     * Overrides the superclass's lookup method to prefer the number field
     * from the KeyEvent.
     */
    protected int lookup(KeyEvent event, Spannable content) {
        int KeyData = event.getUnicodeChar(getMetaState(content));

        if (ok(getAcceptedChars(), (char)keyData)) {
            return keyData;
        }
        return 0;
    }

    /**
* The characters that are used.
*
* @see KeyEvent#getMatch

//<End of snippet n. 2>








