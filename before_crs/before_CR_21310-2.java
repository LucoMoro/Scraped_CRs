/*Appends a space instead of using POUND key

A POUND key doesn't represent a space charactor in some countries.
So it appends a space to the text object instead of sending key event.

Change-Id:Ied72bd3b32a73d355916746e0b118a59969e6f92*/
//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/MultiTapKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/MultiTapKeyListenerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 175047e..5f55ddd

//Synthetic comment -- @@ -176,17 +176,19 @@
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
//Synthetic comment -- @@ -201,17 +203,19 @@
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







