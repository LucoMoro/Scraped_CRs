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
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
//Synthetic comment -- @@ -34,10 +33,11 @@
import android.text.method.MultiTapKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import java.util.concurrent.TimeUnit;

@TestTargetClass(MultiTapKeyListener.class)
public class MultiTapKeyListenerTest extends
ActivityInstrumentationTestCase2<KeyListenerStubActivity> {
//Synthetic comment -- @@ -118,16 +118,126 @@
}
}

    public void testOnKeyDown_capitalizeNone() {
        MultiTapKeyListener keyListener = MultiTapKeyListener.getInstance(false, Capitalize.NONE);

        prepareEmptyTextView();

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_4, 2);
        assertEquals("h", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_3, 2);
        assertEquals("he", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_5, 3);
        assertEquals("hel", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_5, 3);
        assertEquals("hell", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_6, 3);
        assertEquals("hello", mTextView.getText().toString());
}

    public void testOnKeyDown_capitalizeCharacters() {
        MultiTapKeyListener keyListener = MultiTapKeyListener.getInstance(false,
                Capitalize.CHARACTERS);

        prepareEmptyTextView();

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_4, 2);
        assertEquals("H", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_3, 2);
        assertEquals("HE", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_5, 3);
        assertEquals("HEL", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_5, 3);
        assertEquals("HELL", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_6, 3);
        assertEquals("HELLO", mTextView.getText().toString());
    }

    public void testOnKeyDown_capitalizeSentences() {
        MultiTapKeyListener keyListener = MultiTapKeyListener.getInstance(false,
                Capitalize.SENTENCES);

        prepareEmptyTextView();

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_4, 2);
        assertEquals("H", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_4, 3);
        assertEquals("Hi", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_1, 1);
        assertEquals("Hi.", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_POUND, 1);
        assertEquals("Hi. ", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_2, 2);
        assertEquals("Hi. B", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_9, 3);
        assertEquals("Hi. By", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_3, 2);
        assertEquals("Hi. Bye", mTextView.getText().toString());
    }

    public void testOnKeyDown_capitalizeWords() {
        MultiTapKeyListener keyListener = MultiTapKeyListener.getInstance(false,
                Capitalize.WORDS);

        prepareEmptyTextView();

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_4, 2);
        assertEquals("H", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_4, 3);
        assertEquals("Hi", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_POUND, 1);
        assertEquals("Hi ", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_2, 2);
        assertEquals("Hi B", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_9, 3);
        assertEquals("Hi By", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_3, 2);
        assertEquals("Hi Bye", mTextView.getText().toString());
    }

    private void prepareEmptyTextView() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                Selection.setSelection(mTextView.getEditableText(), 0, 0);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());
    }

    private void callOnKeyDown(final MultiTapKeyListener keyListener, final int keyCode,
            final int numTimes) {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                for (int i = 0; i < numTimes; i++) {
                    keyListener.onKeyDown(mTextView, mTextView.getEditableText(), keyCode,
                            new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
                }
            }
        });
        mInstrumentation.waitForIdleSync();

        // Wait a bit in order to distinguish this character and the next one.
try {
Thread.sleep(TIME_OUT);
} catch (InterruptedException e) {
//Synthetic comment -- @@ -135,312 +245,6 @@
}
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
import android.text.InputType;
import android.text.Selection;
import android.text.Spannable;
//Synthetic comment -- @@ -35,7 +34,6 @@
import android.text.method.TextKeyListener;
import android.text.method.TextKeyListener.Capitalize;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.TextView.BufferType;

//Synthetic comment -- @@ -71,253 +69,121 @@
new QwertyKeyListener(null, true);
}

    public void testOnKeyDown_capitalizeNone() {
        QwertyKeyListener keyListener = QwertyKeyListener.getInstance(false, Capitalize.NONE);

        prepareEmptyTextView();

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_H);
assertEquals("h", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_E);
assertEquals("he", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_L);
assertEquals("hel", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_L);
assertEquals("hell", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_O);
        assertEquals("hello", mTextView.getText().toString());
}

    public void testOnKeyDown_capitalizeCharacters() {
        QwertyKeyListener keyListener = QwertyKeyListener.getInstance(false,
                Capitalize.CHARACTERS);

        prepareEmptyTextView();

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_H);
assertEquals("H", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_E);
assertEquals("HE", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_L);
assertEquals("HEL", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_L);
assertEquals("HELL", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_O);
        assertEquals("HELLO", mTextView.getText().toString());
    }

    public void testOnKeyDown_capitalizeSentences() {
        QwertyKeyListener keyListener = QwertyKeyListener.getInstance(false,
                Capitalize.SENTENCES);

        prepareEmptyTextView();

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_H);
        assertEquals("H", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_I);
        assertEquals("Hi", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_PERIOD);
        assertEquals("Hi.", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_SPACE);
        assertEquals("Hi. ", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_B);
        assertEquals("Hi. B", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_Y);
        assertEquals("Hi. By", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_E);
        assertEquals("Hi. Bye", mTextView.getText().toString());
    }

    public void testOnKeyDown_capitalizeWords() {
        QwertyKeyListener keyListener = QwertyKeyListener.getInstance(false,
                Capitalize.WORDS);

        prepareEmptyTextView();

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_H);
        assertEquals("H", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_I);
        assertEquals("Hi", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_SPACE);
        assertEquals("Hi ", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_B);
        assertEquals("Hi B", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_Y);
        assertEquals("Hi By", mTextView.getText().toString());

        callOnKeyDown(keyListener, KeyEvent.KEYCODE_E);
        assertEquals("Hi Bye", mTextView.getText().toString());
    }

    private void prepareEmptyTextView() {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mTextView.setText("", BufferType.EDITABLE);
                Selection.setSelection(mTextView.getEditableText(), 0, 0);
            }
        });
        mInstrumentation.waitForIdleSync();
        assertEquals("", mTextView.getText().toString());
    }

    private void callOnKeyDown(final QwertyKeyListener keyListener, final int keyCode) {
        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                keyListener.onKeyDown(mTextView, mTextView.getEditableText(), keyCode,
                        new KeyEvent(KeyEvent.ACTION_DOWN, keyCode));
            }
        });
        mInstrumentation.waitForIdleSync();
}

@TestTargetNew(







