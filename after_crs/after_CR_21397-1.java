/*Add key strokes in case of using 12-key keyboad

This is a new patch instead of 21311/1.

Change-Id:I4f784e92282f239aeb339944a36cb4f60604ebbf*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/PasswordTransformationMethodTest.java b/tests/tests/text/src/android/text/method/cts/PasswordTransformationMethodTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 6058cdd..ed7be70

//Synthetic comment -- @@ -28,6 +28,7 @@
import android.test.ActivityInstrumentationTestCase2;
import android.text.Editable;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyCharacterMap;
import android.view.View;
import android.view.animation.cts.DelayedCheck;
import android.widget.Button;
//Synthetic comment -- @@ -145,7 +146,16 @@
});

mMethod.reset();
        // 12-key support
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            // "HELLO" in case of 12-key(NUMERIC) keyboard
            sendKeys("6*4 6*3 7*5 DPAD_RIGHT 7*5 7*6 DPAD_RIGHT");
        }
        else {
            sendKeys("H E 2*L O");
        }
assertTrue(mMethod.hasCalledBeforeTextChanged());
assertTrue(mMethod.hasCalledOnTextChanged());
assertTrue(mMethod.hasCalledAfterTextChanged());








//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/inputmethod/cts/BaseInputConnectionTest.java b/tests/tests/view/src/android/view/inputmethod/cts/BaseInputConnectionTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index f5f2286..1ec2003

//Synthetic comment -- @@ -31,6 +31,7 @@
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
//Synthetic comment -- @@ -325,7 +326,17 @@
args = {KeyEvent.class}
)
public void testSendKeyEvent() {
        // 12-key support
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            // 'Q' in case of 12-key(NUMERIC) keyboard
            mConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_7));
            mConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_7));
        }
        else {
            mConnection.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_Q));
        }
new DelayedCheck() {
@Override
protected boolean check() {








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/AutoCompleteTextViewTest.java b/tests/tests/widget/src/android/widget/cts/AutoCompleteTextViewTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index a8c9ae0..8bf0c5b

//Synthetic comment -- @@ -29,6 +29,7 @@
import android.test.UiThreadTest;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
//Synthetic comment -- @@ -64,6 +65,7 @@
/** The m instrumentation. */
private Instrumentation mInstrumentation;
private AutoCompleteTextView mAutoCompleteTextView;
    private boolean mNumeric = false;
ArrayAdapter<String> mAdapter;
private final String[] WORDS = new String[] { "testOne", "testTwo", "testThree", "testFour" };
boolean isOnFilterComplete = false;
//Synthetic comment -- @@ -95,6 +97,11 @@
.findViewById(R.id.autocompletetv_edit);
mAdapter = new ArrayAdapter<String>(mActivity,
android.R.layout.simple_dropdown_item_1line, WORDS);
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            mNumeric = true;
        }
}

@TestTargets({
//Synthetic comment -- @@ -501,7 +508,13 @@

inflatePopup();
assertTrue(mAutoCompleteTextView.isPopupShowing());
        String testString = "";
        if (mNumeric) {
            // "tes" in case of 12-key(NUMERIC) keyboard
            testString = "8337777";
        } else {
            testString = "tes";
        }
// Test the filter if the input string is not long enough to threshold
runTestOnUiThread(new Runnable() {
public void run() {
//Synthetic comment -- @@ -517,7 +530,12 @@

inflatePopup();
assertTrue(mAutoCompleteTextView.isPopupShowing());
        if (mNumeric) {
            // "that" in case of 12-key(NUMERIC) keyboard
            testString = "84428";
        } else {
            testString = "that";
        }
mInstrumentation.sendStringSync(testString);
assertFalse(mAutoCompleteTextView.isPopupShowing());

//Synthetic comment -- @@ -529,7 +547,12 @@
mAutoCompleteTextView.setText("");
}
});
        if (mNumeric) {
            // "test" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("83377778");
        } else {
            mInstrumentation.sendStringSync("test");
        }
assertTrue(mAutoCompleteTextView.hasFocus());
assertTrue(mAutoCompleteTextView.hasWindowFocus());
// give some time for UI to settle
//Synthetic comment -- @@ -594,7 +617,16 @@

// performFiltering will be indirectly invoked by onKeyDown
assertNull(filter.getResult());
        // 12-key support
        if (mNumeric) {
            // "To be teste" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("8888866600022330008337777833");
            mInstrumentation.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_RIGHT);
            // "d" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("3");
        } else {
            mInstrumentation.sendStringSync(STRING_TEST);
        }
// give some time for UI to settle
Thread.sleep(100);
assertEquals(STRING_TEST, filter.getResult());








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/DialerFilterTest.java b/tests/tests/widget/src/android/widget/cts/DialerFilterTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 0b436b2..661d257

//Synthetic comment -- @@ -37,6 +37,7 @@
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.widget.DialerFilter;
import android.widget.EditText;
//Synthetic comment -- @@ -129,7 +130,16 @@
});
mInstrumentation.waitForIdleSync();

        // 12-key support
        KeyCharacterMap keymap
                = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            // "adg" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("234");
        }
        else {
            mInstrumentation.sendStringSync("adg");
        }
assertEquals("ADG", mDialerFilter.getLetters().toString());
assertEquals("", mDialerFilter.getDigits().toString());

//Synthetic comment -- @@ -141,7 +151,14 @@
});
mInstrumentation.waitForIdleSync();

        // 12-key support
        if (keymap.getKeyboardType() == KeyCharacterMap.NUMERIC) {
            // "adg" in case of 12-key(NUMERIC) keyboard
            mInstrumentation.sendStringSync("234");
        }
        else {
            mInstrumentation.sendStringSync("adg");
        }
assertEquals("ADG", mDialerFilter.getLetters().toString());
// A, D, K may map to numbers on some keyboards. Don't test.








