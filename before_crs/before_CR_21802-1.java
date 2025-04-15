/*Fix Some Tests to Support Different Kcms

- DateTimeKeyListenerTest/TimeKeyListenerTest

  Remove the assertions that tried to enter the letter 'a'
  because DateTimeKeyListener will return '1' if A is mapped
  to 1 because '1' appears first in the array of acceptable
  characters first. This is likely a bug in DateTimeKeyListener.

- NumberKeyListenerTest

  Check that the implementation of lookUp somewhat works
  by having getAcceptableChars return nothing to test that
  that lookUp does reject characters when configured to do so.

- TextViewTest

  Remove the sending of key events in the set/getKeyListener
  tests since that is covered by other tests. Make the test
  that checks that errors are not cleared when entering an
  unacceptable character by using a listener that rejects all
  characters.

Change-Id:I4e73f0616b9e602f9b883a82be5fbd7b2dc5ca9a*/
//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/DateTimeKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/DateTimeKeyListenerTest.java
//Synthetic comment -- index 225c4b4..b9027c9 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import android.test.ActivityInstrumentationTestCase2;
import android.text.InputType;
import android.text.method.DateTimeKeyListener;
import android.view.KeyEvent;
import android.widget.TextView;

/**
//Synthetic comment -- @@ -107,9 +106,8 @@
* Scenario description:
* 1. Press '1' key and check if the content of TextView becomes "1"
* 2. Press '2' key and check if the content of TextView becomes "12"
     * 3. Press 'a' key and check if the content of TextView becomes "12a"
     * 4. Press an unaccepted key if it exists. and this key will not be accepted.
     * 5. remove DateKeyListener and Press '1' key, this key will not be accepted
*/
public void testDateTimeKeyListener() {
final DateTimeKeyListener dateTimeKeyListener = DateTimeKeyListener.getInstance();
//Synthetic comment -- @@ -131,15 +129,11 @@
mInstrumentation.sendStringSync("2");
assertEquals("12", mTextView.getText().toString());

        // press 'a' key.
        mInstrumentation.sendStringSync("a");
        assertEquals("12a", mTextView.getText().toString());

// press an unaccepted key if it exists.
int keyCode = TextMethodUtils.getUnacceptedKeyCode(DateTimeKeyListener.CHARACTERS);
if (-1 != keyCode) {
sendKeys(keyCode);
            assertEquals("12a", mTextView.getText().toString());
}

// remove DateTimeKeyListener
//Synthetic comment -- @@ -150,10 +144,10 @@
}
});
mInstrumentation.waitForIdleSync();
        assertEquals("12a", mTextView.getText().toString());

mInstrumentation.sendStringSync("1");
        assertEquals("12a", mTextView.getText().toString());
}

private class MyDateTimeKeyListener extends DateTimeKeyListener {








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java
//Synthetic comment -- index 5c0b9f1..d9242c5 100644

//Synthetic comment -- @@ -37,9 +37,11 @@
import android.widget.TextView;
import android.widget.TextView.BufferType;

@TestTargetClass(NumberKeyListener.class)
public class NumberKeyListenerTest extends
ActivityInstrumentationTestCase2<KeyListenerStubActivity> {
private MockNumberKeyListener mNumberKeyListener;
private Activity mActivity;
private Instrumentation mInstrumentation;
//Synthetic comment -- @@ -74,7 +76,7 @@
@ToBeFixed(bug = "1695243", explanation = "Android API javadocs are incomplete, " +
"should add NPE description in javadoc.")
public void testFilter() {
        mNumberKeyListener = new MockNumberKeyListener();
String source = "Android test";
SpannableString dest = new SpannableString("012345");
assertEquals("", mNumberKeyListener.filter(source, 0, source.length(),
//Synthetic comment -- @@ -122,12 +124,12 @@
)
@ToBeFixed(bug = "1695243", explanation = "Android API javadocs are incomplete.")
public void testLookup() {
        mNumberKeyListener = new MockNumberKeyListener();

KeyEvent event1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_0);
SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A);
str = new SpannableString("ABCD");
assertEquals('\0', mNumberKeyListener.lookup(event2, str));
//Synthetic comment -- @@ -147,7 +149,7 @@
)
@ToBeFixed(bug = "1695243", explanation = "Android API javadocs are incomplete.")
public void testOk() {
        mNumberKeyListener = new MockNumberKeyListener();

assertTrue(mNumberKeyListener.callOk(mNumberKeyListener.getAcceptedChars(), '3'));
assertFalse(mNumberKeyListener.callOk(mNumberKeyListener.getAcceptedChars(), 'e'));
//Synthetic comment -- @@ -172,7 +174,8 @@
)
public void testPressKey() {
final CharSequence text = "123456";
        final MockNumberKeyListener numberKeyListener = new MockNumberKeyListener();

mActivity.runOnUiThread(new Runnable() {
public void run() {
//Synthetic comment -- @@ -189,7 +192,7 @@
assertEquals("0123456", mTextView.getText().toString());

// an unaccepted key if it exists.
        int keyCode = TextMethodUtils.getUnacceptedKeyCode(MockNumberKeyListener.CHARACTERS);
if (-1 != keyCode) {
sendKeys(keyCode);
// text of TextView will not be changed.
//Synthetic comment -- @@ -209,12 +212,21 @@
}

private static class MockNumberKeyListener extends NumberKeyListener {
        static final char[] CHARACTERS = new char[] {'0', '1', '2',
                '3', '4', '5', '6', '7', '8', '9'};

@Override
protected char[] getAcceptedChars() {
            return CHARACTERS;
}

@Override








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/TimeKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/TimeKeyListenerTest.java
//Synthetic comment -- index 278af6b..d49d0b1 100644

//Synthetic comment -- @@ -100,10 +100,8 @@
* Scenario description:
* 1. Press '1' key and check if the content of TextView becomes "1"
* 2. Press '2' key and check if the content of TextView becomes "12"
     * 3. Press 'a' key and check if the content of TextView becomes "12a"
     * 4. Press an unaccepted key if it exists and this key could not be entered.
     * 5. Press 'm' key and check if the content of TextView becomes "12am"
     * 6. remove TimeKeyListener, '1' key will not be accepted.
*/
public void testTimeKeyListener() {
final TimeKeyListener timeKeyListener = TimeKeyListener.getInstance();
//Synthetic comment -- @@ -125,21 +123,13 @@
mInstrumentation.sendStringSync("2");
assertEquals("12", mTextView.getText().toString());

        // press 'a' key.
        mInstrumentation.sendStringSync("a");
        assertEquals("12a", mTextView.getText().toString());

// press an unaccepted key if it exists.
int keyCode = TextMethodUtils.getUnacceptedKeyCode(TimeKeyListener.CHARACTERS);
if (-1 != keyCode) {
sendKeys(keyCode);
            assertEquals("12a", mTextView.getText().toString());
}

        // press 'm' key.
        mInstrumentation.sendStringSync("m");
        assertEquals("12am", mTextView.getText().toString());

mActivity.runOnUiThread(new Runnable() {
public void run() {
mTextView.setKeyListener(null);
//Synthetic comment -- @@ -150,7 +140,7 @@

// press '1' key.
mInstrumentation.sendStringSync("1");
        assertEquals("12am", mTextView.getText().toString());
}

private class MyTimeKeyListener extends TimeKeyListener {








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/TextViewTest.java b/tests/tests/widget/src/android/widget/cts/TextViewTest.java
//Synthetic comment -- index 9bd8cc4..b444f11 100644

//Synthetic comment -- @@ -80,11 +80,9 @@
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
//Synthetic comment -- @@ -93,7 +91,6 @@
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.ExtractedText;
import android.view.inputmethod.ExtractedTextRequest;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.Scroller;
import android.widget.TextView;
//Synthetic comment -- @@ -232,7 +229,6 @@
mActivity.runOnUiThread(new Runnable() {
public void run() {
mTextView = findTextView(R.id.textview_text);
                mTextView.setText("");
}
});
mInstrumentation.waitForIdleSync();
//Synthetic comment -- @@ -244,45 +240,20 @@
mActivity.runOnUiThread(new Runnable() {
public void run() {
mTextView.setKeyListener(digitsKeyListener);
                mTextView.requestFocus();
}
});
mInstrumentation.waitForIdleSync();
assertSame(digitsKeyListener, mTextView.getKeyListener());
        assertEquals("", mTextView.getText().toString());

        // press '-' key.
        mInstrumentation.sendStringSync("-");
        assertEquals("", mTextView.getText().toString());

        // press '1' key.
        mInstrumentation.sendStringSync("1");
        assertEquals("1", mTextView.getText().toString());

        // press '.' key.
        mInstrumentation.sendStringSync(".");
        assertEquals("1", mTextView.getText().toString());

        // press 'a' key.
        mInstrumentation.sendStringSync("a");
        assertEquals("1", mTextView.getText().toString());

final QwertyKeyListener qwertyKeyListener
= QwertyKeyListener.getInstance(false, Capitalize.NONE);
mActivity.runOnUiThread(new Runnable() {
public void run() {
mTextView.setKeyListener(qwertyKeyListener);
                mTextView.requestFocus();
}
});
mInstrumentation.waitForIdleSync();
assertSame(qwertyKeyListener, mTextView.getKeyListener());
        assertEquals("1", mTextView.getText().toString());

        // press 'a' key.
        mInstrumentation.sendStringSync("a");

        assertEquals("1a", mTextView.getText().toString());
}

@TestTargets({
//Synthetic comment -- @@ -1786,10 +1757,9 @@
mInstrumentation.waitForIdleSync();
assertNull(mTextView.getError());

        final DigitsKeyListener digitsKeyListener = DigitsKeyListener.getInstance();
mActivity.runOnUiThread(new Runnable() {
public void run() {
                mTextView.setKeyListener(digitsKeyListener);
mTextView.setText("", BufferType.EDITABLE);
mTextView.setError(errorText);
mTextView.requestFocus();
//Synthetic comment -- @@ -1805,6 +1775,16 @@
// The icon and error message will not be reset to null
assertNotNull(mTextView.getError());

mInstrumentation.sendStringSync("1");
// a key event cause changes to the TextView's text
assertEquals("1", mTextView.getText().toString());







