/*Support for adding words with Asian characters to dictionary

TextView parsing of words is extended to include Unicode
class Letter Other which covers Asian languages among others.
Also added the possibility to add a text selection to the
dictionary if it is a valid word. Needed for languages
not using word separators, such as Chinese and Japanese.

Change-Id:I760ebbd6fb6fbe9a145ea01146b062b67fb12fdc*/




//Synthetic comment -- diff --git a/core/java/android/widget/TextView.java b/core/java/android/widget/TextView.java
//Synthetic comment -- index bdc5014..38b5bd3 100644

//Synthetic comment -- @@ -7245,45 +7245,92 @@
return -1;
}

        int start;
        int end;

        // Use the selection if it is a valid word
        if (hasSelection()) {
            start = getSelectionStart();
            end = getSelectionEnd();

            if (start > end) {
                int temp = start;
                start = end;
                end = temp;
}

            for (int i = start; i < end; i++) {
                int c = Character.codePointAt(mTransformed, i);
                int type = Character.getType(c);

                if (c >= 0x10000) { // Two Character codepoint
                    i++;
                }

                if (c != '\'' &&
                    type != Character.UPPERCASE_LETTER &&
                    type != Character.LOWERCASE_LETTER &&
                    type != Character.TITLECASE_LETTER &&
                    type != Character.MODIFIER_LETTER &&
                    type != Character.OTHER_LETTER &&
                    type != Character.DECIMAL_DIGIT_NUMBER) {
                    return -1;
                }
}

        // Use the word around the cursor if no selection
        } else {
            int len = mText.length();
            end = Math.min(offset, len);

            if (end < 0) {
                return -1;
            }

            start = end;

            for (; start > 0; start--) {
                char c = mTransformed.charAt(start - 1);
                int type = Character.getType(c);

                if (type == Character.SURROGATE) { // Two Character codepoint
                    end = start - 1; // Recheck as a pair when scanning forward
                    continue;
                }

                if (c != '\'' &&
                    type != Character.UPPERCASE_LETTER &&
                    type != Character.LOWERCASE_LETTER &&
                    type != Character.TITLECASE_LETTER &&
                    type != Character.MODIFIER_LETTER &&
                    type != Character.OTHER_LETTER &&
                    type != Character.DECIMAL_DIGIT_NUMBER) {
                    break;
                }
            }

            for (; end < len; end++) {
                int c = Character.codePointAt(mTransformed, end);
                int type = Character.getType(c);

                if (c >= 0x10000) { // Two Character codepoint
                    end++;
                }

                if (c != '\'' &&
                    type != Character.UPPERCASE_LETTER &&
                    type != Character.LOWERCASE_LETTER &&
                    type != Character.TITLECASE_LETTER &&
                    type != Character.MODIFIER_LETTER &&
                    type != Character.OTHER_LETTER &&
                    type != Character.DECIMAL_DIGIT_NUMBER) {
                    break;
                }
            }

            if (start == end) {
                return -1;
            }
}

if (end - start > 48) {
//Synthetic comment -- @@ -7292,7 +7339,7 @@

boolean hasLetter = false;
for (int i = start; i < end; i++) {
            if (Character.isLetter(Character.codePointAt(mTransformed, i))) {
hasLetter = true;
break;
}
//Synthetic comment -- @@ -7441,6 +7488,14 @@
setAlphabeticShortcut('v');
added = true;
}

            String word = getWordForDictionary();
            if (word != null) {
                menu.add(1, ID_ADD_TO_DICTIONARY, 0,
                     getContext().getString(com.android.internal.R.string.addToDictionary, word)).
                     setOnMenuItemClickListener(handler);
                added = true;
            }
} else {
MenuHandler handler = new MenuHandler();

//Synthetic comment -- @@ -7602,6 +7657,11 @@

case ID_ADD_TO_DICTIONARY:
String word = getWordForDictionary();

                if (mText instanceof Spannable) {
                    stopTextSelectionMode();
                }

if (word != null) {
Intent i = new Intent("com.android.settings.USER_DICTIONARY_INSERT");
i.putExtra("word", word);








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/widget/TextViewWordLimitsTest.java b/core/tests/coretests/src/android/widget/TextViewWordLimitsTest.java
new file mode 100644
//Synthetic comment -- index 0000000..e62201b

//Synthetic comment -- @@ -0,0 +1,235 @@
/*********************************************************************
 *  ____                      _____      _                           *
 * / ___|  ___  _ __  _   _  | ____|_ __(_) ___ ___ ___  ___  _ __   *
 * \___ \ / _ \| '_ \| | | | |  _| | '__| |/ __/ __/ __|/ _ \| '_ \  *
 *  ___) | (_) | | | | |_| | | |___| |  | | (__\__ \__ \ (_) | | | | *
 * |____/ \___/|_| |_|\__, | |_____|_|  |_|\___|___/___/\___/|_| |_| *
 *                    |___/                                          *
 *                                                                   *
 *********************************************************************
 * Copyright 2010 Sony Ericsson Mobile Communications AB.            *
 * All rights, including trade secret rights, reserved.              *
 *********************************************************************/


package android.widget;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.LargeTest;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableString;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * TextViewPatchTest tests {@link TextView}'s definition of word. Finds and
 * verifies word limits to be in strings containing different kinds of
 * characters.
 */
public class TextViewWordLimitsTest extends AndroidTestCase {

    TextView mTv = null;
    Method mGetWordLimits;


    /**
     * Sets up common fields used in all test cases.
     */
    @Override
    protected void setUp() throws NoSuchMethodException {
        mTv = new TextView(getContext());
        mTv.setInputType(InputType.TYPE_CLASS_TEXT);

        mGetWordLimits = mTv.getClass().getDeclaredMethod("getWordLimitsAt",
                new Class[] {int.class});
        mGetWordLimits.setAccessible(true);
    }

    /**
     * Calculate and verify word limits. Depends on the TextView implementation.
     * Uses a private method and internal data representation.
     *
     * @param text         Text to select a word from
     * @param pos          Position to expand word around
     * @param correctStart Correct start position for the word
     * @param correctEnd   Correct end position for the word
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void verifyWordLimits(CharSequence text, int pos, int correctStart, int correctEnd)
            throws InvocationTargetException, IllegalAccessException {
        mTv.setText(text, TextView.BufferType.SPANNABLE);
        if (text instanceof Spannable) {
            // The span is not copied when setting string to TextView. Readd.
            int selectionStart = Selection.getSelectionStart(text);
            int selectionEnd = Selection.getSelectionEnd(text);
            Selection.setSelection((Spannable)mTv.getText(), selectionStart, selectionEnd);
        }

        long limits = (Long)mGetWordLimits.invoke(mTv, new Object[] {new Integer(pos)});
        int actualStart = (int)(limits >>> 32);
        int actualEnd = (int)(limits & 0x00000000FFFFFFFFL);
        assertEquals(correctStart, actualStart);
        assertEquals(correctEnd, actualEnd);
    }


    /**
     * Corner cases for string length.
     */
    @LargeTest
    public void testLengths() throws Exception {
        final String ONE_TWO = "one two";
        final String EMPTY   = "";
        final String TOOLONG = "ThisWordIsTooLongToBeDefinedAsAWordInTheSenseUsedInTextView";

        // Select first word
        verifyWordLimits(ONE_TWO, 0, 0, 3);
        verifyWordLimits(ONE_TWO, 3, 0, 3);

        // Select last word
        verifyWordLimits(ONE_TWO, 4, 4, 7);
        verifyWordLimits(ONE_TWO, 7, 4, 7);

        // Empty string
        verifyWordLimits(EMPTY, 0, -1, -1);

        // Too long word
        verifyWordLimits(TOOLONG, 0, -1, -1);
    }

    /**
     * Unicode classes included.
     */
    @LargeTest
    public void testIncludedClasses() throws Exception {
        final String LOWER          = "njlj";
        final String UPPER          = "NJLJ";
        final String TITLECASE      = "\u01C8\u01CB\u01F2"; // Lj Nj Dz
        final String OTHER          = "\u3042\u3044\u3046"; // Hiragana AIU
        final String MODIFIER       = "\u02C6\u02CA\u02CB"; // Circumflex Acute Grave

        // Each string contains a single valid word
        verifyWordLimits(LOWER, 1, 0, 4);
        verifyWordLimits(UPPER, 1, 0, 4);
        verifyWordLimits(TITLECASE, 1, 0, 3);
        verifyWordLimits(OTHER, 1, 0, 3);
        verifyWordLimits(MODIFIER, 1, 0, 3);
    }

    /**
     * Unicode classes included if combined with a letter.
     */
    @LargeTest
    public void testPartlyIncluded() throws Exception {
        final String NUMBER           = "123";
        final String NUMBER_LOWER     = "1st";
        final String APOSTROPHE       = "''";
        final String APOSTROPHE_LOWER = "'Android's'";

        // Pure decimal number is ignored
        verifyWordLimits(NUMBER, 1, -1, -1);

        // Number with letter is valid
        verifyWordLimits(NUMBER_LOWER, 1, 0, 3);

        // Stand apostrophes are ignore
        verifyWordLimits(APOSTROPHE, 1, -1, -1);

        // Apostrophes are accepted if they are a part of a word
        verifyWordLimits(APOSTROPHE_LOWER, 1, 0, 11);
    }

    /**
     * Unicode classes other than listed in testIncludedClasses and
     * testPartlyIncluded act as word separators.
     */
    @LargeTest
    public void testNotIncluded() throws Exception {
        // Selection of character classes excluded
        final String MARK_NONSPACING        = "a\u030A";       // a Combining ring above
        final String PUNCTUATION_OPEN_CLOSE = "(c)";           // Parenthesis
        final String PUNCTUATION_DASH       = "non-fiction";   // Hyphen
        final String PUNCTUATION_OTHER      = "b&b";           // Ampersand
        final String SYMBOL_OTHER           = "Android\u00AE"; // Registered
        final String SEPARATOR_SPACE        = "one two";       // Space

        // "a"
        verifyWordLimits(MARK_NONSPACING, 1, 0, 1);

        // "c"
        verifyWordLimits(PUNCTUATION_OPEN_CLOSE, 1, 1, 2);

        // "non"
        verifyWordLimits(PUNCTUATION_DASH, 1, 0, 3);

        // "b"
        verifyWordLimits(PUNCTUATION_OTHER, 1, 0, 1);

        // "Android"
        verifyWordLimits(SYMBOL_OTHER, 1, 0, 7);

        // "one"
        verifyWordLimits(SEPARATOR_SPACE, 1, 0, 3);
    }

    /**
     * Surrogate characters are treated as their code points.
     */
    @LargeTest
    public void testSurrogate() throws Exception {
        final String SURROGATE_LETTER   = "\uD800\uDC00\uD800\uDC01\uD800\uDC02"; // Linear B AEI
        final String SURROGATE_SYMBOL   = "\uD83D\uDE01\uD83D\uDE02\uD83D\uDE03"; // Three smileys

        // Letter Other is included even when coded as surrogate pairs
        verifyWordLimits(SURROGATE_LETTER, 2, 0, 6);

        // Not included classes are ignored even when coded as surrogate pairs
        verifyWordLimits(SURROGATE_SYMBOL, 2, -1, -1);
    }

    /**
     * Selection is used if present and valid word.
     */
    @LargeTest
    public void testSelection() throws Exception {
        SpannableString textLower       = new SpannableString("first second");
        SpannableString textOther       = new SpannableString("\u3042\3044\3046"); // Hiragana AIU
        SpannableString textDash        = new SpannableString("non-fiction");      // Hyphen
        SpannableString textPunctOther  = new SpannableString("b&b");              // Ampersand
        SpannableString textSymbolOther = new SpannableString("Android\u00AE");    // Registered

        // Valid selection - Letter, Lower
        Selection.setSelection(textLower, 2, 5);
        verifyWordLimits(textLower, 0, 2, 5);

        // Valid selection -- Letter, Other
        Selection.setSelection(textOther, 1, 2);
        verifyWordLimits(textOther, 0, 1, 2);

        // Zero-width selection is interpreted as a cursor and
        // the selection is ignored
        Selection.setSelection(textLower, 2, 2);
        verifyWordLimits(textLower, 0, 0, 5);

        // Invalid word, space selected and the selection is ignored
        Selection.setSelection(textLower, 4, 7);
        verifyWordLimits(textLower, 0, -1, -1);

        // Invalid word, hyphen selected and the selection is ignored
        Selection.setSelection(textDash, 2, 5);
        verifyWordLimits(textLower, 0, -1, -1);

        // Invalid word, ampersand selected and the selection is ignored
        Selection.setSelection(textPunctOther, 1, 3);
        verifyWordLimits(textLower, 0, -1, -1);

        // Invalid word, (R) selected and the selection is ignored
        Selection.setSelection(textSymbolOther, 2, 8);
        verifyWordLimits(textLower, 0, -1, -1);
    }
}







