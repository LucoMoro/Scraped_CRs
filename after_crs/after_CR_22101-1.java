/*Select cuts surrogate pairs

Selection does not consider surrogate pairs when selecting text. This
can lead to selection edges cutting complex characters in half.
setSelection now look at the char at the selection edges and move the
edge to include any complex character that might get cut by the
selection.

Change-Id:I6e790dda6c452b47a743920c0ae7c664c73ad0fc*/




//Synthetic comment -- diff --git a/core/java/android/text/Selection.java b/core/java/android/text/Selection.java
//Synthetic comment -- index 13cb5e6..2e926eb 100644

//Synthetic comment -- @@ -71,6 +71,18 @@
int oend = getSelectionEnd(text);

if (ostart != start || oend != stop) {
            // Adjust the selection edges to avoid cutting any surrogate pair
            // characters in half
            final boolean cursorAdjustment = start == stop;
            start = getAdjustedSelectStart(text, start);
            if (!cursorAdjustment) {
                // Normal selection
                stop = getAdjustedSelectStop(text, stop);
            } else {
                // This is a cursor adjustment, length of selection is zero
                stop = start;
            }

text.setSpan(SELECTION_START, start, start,
Spanned.SPAN_POINT_POINT|Spanned.SPAN_INTERMEDIATE);
text.setSpan(SELECTION_END, stop, stop,
//Synthetic comment -- @@ -79,6 +91,30 @@
}

/**
     * Get a selection start edge that considers surrogate pairs. Encountered
     * surrogate pair will be included in the selection
     */
    private static int getAdjustedSelectStart(Spannable text, int position) {
        if (position > 0 && position < text.length()
                && Character.isSurrogatePair(text.charAt(position - 1), text.charAt(position))) {
            position--;
        }
        return position;
    }

    /**
     * Get a selection end edge that considers surrogate pairs. Encountered
     * surrogate pair will be included in the selection
     */
    private static int getAdjustedSelectStop(Spannable text, int position) {
        if (position > 0 && position < text.length()
                && Character.isSurrogatePair(text.charAt(position-1), text.charAt(position))) {
            position++;
        }
        return position;
    }

    /**
* Move the cursor to offset <code>index</code>.
*/
public static final void setSelection(Spannable text, int index) {








//Synthetic comment -- diff --git a/core/tests/coretests/src/android/text/SelectionTest.java b/core/tests/coretests/src/android/text/SelectionTest.java
new file mode 100644
//Synthetic comment -- index 0000000..37574c0

//Synthetic comment -- @@ -0,0 +1,105 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.text;

import android.test.AndroidTestCase;
import android.util.Log;

public class SelectionTest extends AndroidTestCase {
    final String angryFaceEmoji = "\uDBB8\uDF20";

    SpannableStringBuilder fourEmojis;

    @Override
    protected void setUp() throws Exception {
        fourEmojis = new SpannableStringBuilder();
        fourEmojis.append(angryFaceEmoji);
        fourEmojis.append(angryFaceEmoji);
        fourEmojis.append(angryFaceEmoji);
        fourEmojis.append(angryFaceEmoji);
    }

    @Override
    protected void tearDown() throws Exception {
    }

    public void testSelectCenterSurrogatePair() throws Exception {
        // It's expected that the selection is changed to include the full
        // surrogate pairs
        int expectedStart = 2;
        int expectedEnd = 6;

        int start = 3; // Center of the second surrogate pair
        int end = 5; // Center of the third surrogate pair
        Selection.setSelection(fourEmojis, start, end);

        assertSame("Left edge of selection should be moved to the left of the surrogate pair",
                expectedStart, Selection.getSelectionStart(fourEmojis));
        assertSame("Right edge of selection should be moved to the right of the surrogate pair",
                expectedEnd, Selection.getSelectionEnd(fourEmojis));
        testSelection(fourEmojis, angryFaceEmoji + angryFaceEmoji);
    }

    public void testSelectSurrogatePair() throws Exception {
        int start = 2; // Left of the second surrogate pair
        int end = 6; // right of the third surrogate pair

        Selection.setSelection(fourEmojis, start, end);

        assertSame("Left edge of selection should not change", start,
                Selection.getSelectionStart(fourEmojis));
        assertSame("Right edge of selection should not change", end,
                Selection.getSelectionEnd(fourEmojis));
        testSelection(fourEmojis, angryFaceEmoji + angryFaceEmoji);
    }

    public void testSelectAllSurrogatePair() throws Exception {
        int start = 0;
        int end = fourEmojis.length();

        Selection.setSelection(fourEmojis, start, end);

        assertSame("Left edge of selection should not change", start,
                Selection.getSelectionStart(fourEmojis));
        assertSame("Right edge of selection should not change", end,
                Selection.getSelectionEnd(fourEmojis));
        testSelection(fourEmojis, fourEmojis);
    }

    public void testSelectAllInner() throws Exception {
        Selection.setSelection(fourEmojis, 1, fourEmojis.length() - 1);
        testSelection(fourEmojis, fourEmojis);

    }

    public void testCursorOffset() throws Exception {
        int pos = 3;
        int expected = pos - 1; // Left of surrogate pair
        Selection.setSelection(fourEmojis, pos);
        assertSame("Cursor position should have zero length",
                Selection.getSelectionStart(fourEmojis), Selection.getSelectionEnd(fourEmojis));
        assertSame("Cursor position not be in the middle of surrogate pair",
                Selection.getSelectionStart(fourEmojis), expected);
        testSelection(fourEmojis, "");
    }

    private void testSelection(Spannable text, CharSequence expected) throws Exception {
        String selectedText = text.subSequence(Selection.getSelectionStart(text),
                Selection.getSelectionEnd(text)).toString();
        assertEquals("selected text not same as expected", selectedText, expected.toString());
    }
}







