/*Fix (Qwerty|MultiTap)KeyListenerTest Tests

Replace the fragile setup code that required the target TextView to be
focused with code that directly call onKeyDown instead.

Change-Id:I09396d752a9ac1462b85f2f9fb9349701687abcc*/
//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/MultiTapKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/MultiTapKeyListenerTest.java
//Synthetic comment -- index 3bf059b..175047e 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
//Synthetic comment -- @@ -34,10 +33,11 @@
import android.text.method.MultiTapKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.BufferType;

@TestTargetClass(MultiTapKeyListener.class)
public class MultiTapKeyListenerTest extends
ActivityInstrumentationTestCase2<KeyListenerStubActivity> {
//Synthetic comment -- @@ -118,16 +118,126 @@
}
}

    @Override
    public void sendRepeatedKeys(int... keys) {
        super.sendRepeatedKeys(keys);
        waitForListenerTimeout();
}

    /**
     * Wait for TIME_OUT, or listener will accept key event as multi tap rather than a new key.
     */
    private void waitForListenerTimeout() {
try {
Thread.sleep(TIME_OUT);
} catch (InterruptedException e) {
//Synthetic comment -- @@ -135,312 +245,6 @@
}
}

    /**
     * Check point when Capitalize.NONE and autotext is false:
     * 1. press KEYCODE_4 twice, text is "h".
     * 2. press KEYCODE_3 twice, text is "he".
     * 3. press KEYCODE_5 three times, text is "hel".
     * 4. press KEYCODE_5 three times, text is "hell".
     * 5. press KEYCODE_8 twice, text is "hellu".
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    public void testPressKey1() {
        final MultiTapKeyListener multiTapKeyListener
                = MultiTapKeyListener.getInstance(false, Capitalize.NONE);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(multiTapKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_4);
        assertEquals("h", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_3);
        assertEquals("he", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("hel", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("hell", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_8);
        assertEquals("hellu", mTextView.getText().toString());
    }

    /**
     * Check point when Capitalize.NONE and autotext is true:
     * 1. press KEYCODE_4 twice, text is "h".
     * 2. press KEYCODE_3 twice, text is "he".
     * 3. press KEYCODE_5 three times, text is "hel".
     * 4. press KEYCODE_5 three times, text is "hell".
     * 5. press KEYCODE_8 twice, text should not be "hellu".
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    @ToBeFixed(bug = "1738241", explanation = "can not correct spelling automatically")
    public void testPressKey2() {
        final MultiTapKeyListener multiTapKeyListener
                = MultiTapKeyListener.getInstance(true, Capitalize.NONE);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(multiTapKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_4);
        assertEquals("h", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_3);
        assertEquals("he", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("hel", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("hell", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_8);
//        assertFalse("hellu".equals(mEditText.getText().toString())); issue 1738241
    }

    /**
     * Check point when Capitalize.SENTENCES and autotext is false:
     * 1. press KEYCODE_4 twice, text is "H".
     * 2. press KEYCODE_3 twice, text is "He".
     * 3. press KEYCODE_5 three times, text is "Hel".
     * 4. press KEYCODE_5 three times, text is "Hell".
     * 5. press KEYCODE_8 twice, text is "Hellu".
     * 6. press KEYCODE_1 once, text is "Hellu.".
     * 7. press KEYCODE_POUND once, text is "Hellu. ".
     * 8. press KEYCODE_6 once, text is "Hellu. M".
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    public void testPressKey3() {
        final MultiTapKeyListener multiTapKeyListener
                = MultiTapKeyListener.getInstance(false, Capitalize.SENTENCES);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(multiTapKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_4);
        assertEquals("H", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_3);
        assertEquals("He", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("Hel", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("Hell", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_8);
        assertEquals("Hellu", mTextView.getText().toString());

        sendRepeatedKeys(1, KeyEvent.KEYCODE_1);
        assertEquals("Hellu.", mTextView.getText().toString());

        sendRepeatedKeys(1, KeyEvent.KEYCODE_POUND);
        assertEquals("Hellu. ", mTextView.getText().toString());

        sendRepeatedKeys(1, KeyEvent.KEYCODE_6);
        assertEquals("Hellu. M", mTextView.getText().toString());
    }

    /**
     * Check point when Capitalize.SENTENCES and autotext is true:
     * 1. press KEYCODE_4 twice, text is "H".
     * 2. press KEYCODE_3 twice, text is "He".
     * 3. press KEYCODE_5 three times, text is "Hel".
     * 4. press KEYCODE_5 three times, text is "Hell".
     * 5. press KEYCODE_8 twice, text is "Hellu".
     * 6. press KEYCODE_1 once, text is "Hellu.".
     * 7. press KEYCODE_POUND once, text is "Hellu. ".
     * 8. press KEYCODE_6 once, text should not be "Hellu. M".
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    @ToBeFixed(bug = "1738241", explanation = "can not correct spelling automatically")
    public void testPressKey4() {
        final MultiTapKeyListener multiTapKeyListener
                = MultiTapKeyListener.getInstance(true, Capitalize.SENTENCES);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(multiTapKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_4);
        assertEquals("H", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_3);
        assertEquals("He", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("Hel", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("Hell", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_8);
        assertEquals("Hellu", mTextView.getText().toString());

        sendRepeatedKeys(1, KeyEvent.KEYCODE_1);
//        assertFalse("Hellu.".equals(mEditText.getText().toString())); issue 1738241
//
//        sendRepeatedKeys(1, KeyEvent.KEYCODE_POUND);
//        assertEquals("Hellu. ", mEditText.getText().toString());
//
//        sendRepeatedKeys(1, KeyEvent.KEYCODE_6);
//        assertEquals("Hellu. M", mEditText.getText().toString());
    }

    /**
     * Check point when Capitalize.WORDS and autotext is false:
     * 1. press KEYCODE_4 twice, text is "H".
     * 2. press KEYCODE_3 twice, text is "He".
     * 3. press KEYCODE_5 three times, text is "Hel".
     * 4. press KEYCODE_5 three times, text is "Hell".
     * 5. press KEYCODE_8 twice, text is "Hellu".
     * 6. press KEYCODE_POUND once, text is "Hellu ".
     * 7. press KEYCODE_6 once, text is "Hellu M".
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    public void testPressKey5() {
        final MultiTapKeyListener multiTapKeyListener
                = MultiTapKeyListener.getInstance(false, Capitalize.WORDS);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(multiTapKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_4);
        assertEquals("H", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_3);
        assertEquals("He", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("Hel", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("Hell", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_8);
        assertEquals("Hellu", mTextView.getText().toString());

        sendRepeatedKeys(1, KeyEvent.KEYCODE_POUND);
        assertEquals("Hellu ", mTextView.getText().toString());

        sendRepeatedKeys(1, KeyEvent.KEYCODE_6);
        assertEquals("Hellu M", mTextView.getText().toString());
    }

    /**
     * Check point when Capitalize.CHARACTERS and autotext is false:
     * 1. press KEYCODE_4 twice, text is "H".
     * 2. press KEYCODE_3 twice, text is "HE".
     * 3. press KEYCODE_5 three times, text is "HEL".
     * 4. press KEYCODE_5 three times, text is "HELL".
     * 5. press KEYCODE_8 twice, text is "HELLU".
     * 6. press KEYCODE_POUND once, text is "HELLU ".
     * 7. press KEYCODE_6 once, text is "HELLU M".
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    public void testPressKey6() {
        final MultiTapKeyListener multiTapKeyListener
                = MultiTapKeyListener.getInstance(false, Capitalize.CHARACTERS);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(multiTapKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_4);
        assertEquals("H", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_3);
        assertEquals("HE", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("HEL", mTextView.getText().toString());

        sendRepeatedKeys(3, KeyEvent.KEYCODE_5);
        assertEquals("HELL", mTextView.getText().toString());

        sendRepeatedKeys(2, KeyEvent.KEYCODE_8);
        assertEquals("HELLU", mTextView.getText().toString());

        sendRepeatedKeys(1, KeyEvent.KEYCODE_POUND);
        assertEquals("HELLU ", mTextView.getText().toString());

        sendRepeatedKeys(1, KeyEvent.KEYCODE_6);
        assertEquals("HELLU M", mTextView.getText().toString());
    }

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "getInstance",








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/QwertyKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/QwertyKeyListenerTest.java
//Synthetic comment -- index 5ae8cae..c86dba3 100644

//Synthetic comment -- @@ -26,7 +26,6 @@
import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
//Synthetic comment -- @@ -35,7 +34,6 @@
import android.text.method.TextKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.TextView.BufferType;

//Synthetic comment -- @@ -71,253 +69,121 @@
new QwertyKeyListener(null, true);
}

    /**
     * Check point when Capitalize.NONE and autotext is true:
     * 1. press KEYCODE_H, text is "h".
     * 2. press KEYCODE_E, text is "he".
     * 3. press KEYCODE_L, text is "hel".
     * 4. press KEYCODE_L, text is "hell".
     * 5. press KEYCODE_U, text should not be "hellu".
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    @ToBeFixed(bug = "1738241", explanation = "can not correct spelling automatically")
    public void testPressKey1() {
        final QwertyKeyListener qwertyKeyListener
                = QwertyKeyListener.getInstance(true, Capitalize.NONE);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(qwertyKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_H);
assertEquals("h", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_E);
assertEquals("he", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_L);
assertEquals("hel", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_L);
assertEquals("hell", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_U);
//        assertFalse("hellu".equals(mTextView.getText().toString())); issue 1738241
}

    /**
     * Check point when Capitalize.NONE and autotext is false:
     * 1. press KEYCODE_H, text is "h".
     * 2. press KEYCODE_E, text is "he".
     * 3. press KEYCODE_L, text is "hel".
     * 4. press KEYCODE_L, text is "hell".
     * 5. press KEYCODE_U, text is "hellu".
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    public void testPressKey2() {
        final QwertyKeyListener qwertyKeyListener
                = QwertyKeyListener.getInstance(false, Capitalize.NONE);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(qwertyKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_H);
        assertEquals("h", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_E);
        assertEquals("he", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_L);
        assertEquals("hel", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_L);
        assertEquals("hell", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_U);
        assertEquals("hellu", mTextView.getText().toString());
    }

    /**
     * Check point when Capitalize.SENTENCES and autotext is false:
     * 1. press KEYCODE_H, text is "H".
     * 2. press KEYCODE_E, text is "He".
     * 3. press KEYCODE_L, text is "Hel".
     * 4. press KEYCODE_L, text is "Hell".
     * 5. press KEYCODE_U, text is "Hellu".
     * 6. press KEYCODE_PERIOD, text is "Hellu."
     * 7. press KEYCODE_SPACE, text is "Hellu. "
     * 8. press KEYCODE_H, text is "Hellu. H"
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    public void testPressKey3() {
        final QwertyKeyListener qwertyKeyListener
                = QwertyKeyListener.getInstance(false, Capitalize.SENTENCES);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(qwertyKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_H);
assertEquals("H", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_E);
        assertEquals("He", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_L);
        assertEquals("Hel", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_L);
        assertEquals("Hell", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_U);
        assertEquals("Hellu", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_PERIOD);
        assertEquals("Hellu.", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_SPACE);
        assertEquals("Hellu. ", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_H);
        assertEquals("Hellu. H", mTextView.getText().toString());
    }

    /**
     * Check point when Capitalize.WORDS and autotext is false:
     * 1. press KEYCODE_H, text is "H".
     * 2. press KEYCODE_E, text is "He".
     * 3. press KEYCODE_L, text is "Hel".
     * 4. press KEYCODE_L, text is "Hell".
     * 5. press KEYCODE_U, text is "Hellu".
     * 7. press KEYCODE_SPACE, text is "Hellu "
     * 8. press KEYCODE_H, text is "Hellu H"
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    public void testPressKey4() {
        final QwertyKeyListener qwertyKeyListener
                = QwertyKeyListener.getInstance(false, Capitalize.WORDS);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(qwertyKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_H);
        assertEquals("H", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_E);
        assertEquals("He", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_L);
        assertEquals("Hel", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_L);
        assertEquals("Hell", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_U);
        assertEquals("Hellu", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_SPACE);
        assertEquals("Hellu ", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_H);
        assertEquals("Hellu H", mTextView.getText().toString());
    }

    /**
     * Check point when Capitalize.CHARACTERS and autotext is false:
     * 1. press KEYCODE_H, text is "H".
     * 2. press KEYCODE_E, text is "HE".
     * 3. press KEYCODE_L, text is "HEL".
     * 4. press KEYCODE_L, text is "HELL".
     * 5. press KEYCODE_U, text is "HELLU".
     * 7. press KEYCODE_SPACE, text is "HELLU "
     * 8. press KEYCODE_H, text is "HELLU H"
     */
    @TestTargetNew(
        level = TestLevel.COMPLETE,
        method = "onKeyDown",
        args = {View.class, Editable.class, int.class, KeyEvent.class}
    )
    public void testPressKey5() {
        final QwertyKeyListener qwertyKeyListener
                = QwertyKeyListener.getInstance(false, Capitalize.CHARACTERS);

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                mTextView.requestFocus();
                Selection.setSelection((Editable) mTextView.getText(), 0, 0);
                mTextView.setKeyListener(qwertyKeyListener);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_H);
        assertEquals("H", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_E);
assertEquals("HE", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_L);
assertEquals("HEL", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_L);
assertEquals("HELL", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_U);
        assertEquals("HELLU", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_SPACE);
        assertEquals("HELLU ", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_H);
        assertEquals("HELLU H", mTextView.getText().toString());
}

@TestTargetNew(







