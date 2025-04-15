/*for 4 line QWERTY (AZERTY) and some number key that are assigned with same hard key

Change-Id:I1276afd39c926c795afc7b39c7f6861ba722abab*/
//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/DateTimeKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/DateTimeKeyListenerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 225c4b4..dc0049c

//Synthetic comment -- @@ -107,7 +107,7 @@
* Scenario description:
* 1. Press '1' key and check if the content of TextView becomes "1"
* 2. Press '2' key and check if the content of TextView becomes "12"
     * 3. Press 'a' key and check if the content of TextView becomes "12a"
* 4. Press an unaccepted key if it exists. and this key will not be accepted.
* 5. remove DateKeyListener and Press '1' key, this key will not be accepted
*/
//Synthetic comment -- @@ -131,15 +131,15 @@
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
//Synthetic comment -- @@ -150,10 +150,10 @@
}
});
mInstrumentation.waitForIdleSync();
        assertEquals("12a", mTextView.getText().toString());

mInstrumentation.sendStringSync("1");
        assertEquals("12a", mTextView.getText().toString());
}

private class MyDateTimeKeyListener extends DateTimeKeyListener {








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/NumberKeyListenerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 5c0b9f1..3821816

//Synthetic comment -- @@ -128,9 +128,9 @@
SpannableString str = new SpannableString("012345");
assertEquals('0', mNumberKeyListener.lookup(event1, str));

        KeyEvent event2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_A);
        str = new SpannableString("ABCD");
        assertEquals('\0', mNumberKeyListener.lookup(event2, str));

try {
mNumberKeyListener.lookup(null, str);








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/TimeKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/TimeKeyListenerTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 278af6b..7dd6eb0

//Synthetic comment -- @@ -100,9 +100,9 @@
* Scenario description:
* 1. Press '1' key and check if the content of TextView becomes "1"
* 2. Press '2' key and check if the content of TextView becomes "12"
     * 3. Press 'a' key and check if the content of TextView becomes "12a"
* 4. Press an unaccepted key if it exists and this key could not be entered.
     * 5. Press 'm' key and check if the content of TextView becomes "12am"
* 6. remove TimeKeyListener, '1' key will not be accepted.
*/
public void testTimeKeyListener() {
//Synthetic comment -- @@ -125,20 +125,20 @@
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
//Synthetic comment -- @@ -150,7 +150,7 @@

// press '1' key.
mInstrumentation.sendStringSync("1");
        assertEquals("12am", mTextView.getText().toString());
}

private class MyTimeKeyListener extends TimeKeyListener {








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/TextViewTest.java b/tests/tests/widget/src/android/widget/cts/TextViewTest.java
old mode 100644
new mode 100755
//Synthetic comment -- index 9bd8cc4..45d649a

//Synthetic comment -- @@ -263,8 +263,8 @@
mInstrumentation.sendStringSync(".");
assertEquals("1", mTextView.getText().toString());

        // press 'a' key.
        mInstrumentation.sendStringSync("a");
assertEquals("1", mTextView.getText().toString());

final QwertyKeyListener qwertyKeyListener
//Synthetic comment -- @@ -1799,7 +1799,7 @@

assertEquals(errorText, mTextView.getError().toString());

        mInstrumentation.sendStringSync("a");
// a key event that will not change the TextView's text
assertEquals("", mTextView.getText().toString());
// The icon and error message will not be reset to null







