/*fix when there are non-English character keymap

when devices use non-standard-english charmap set
to default, it causes the test to fail. So, we switch
the IME editor by sending the SYM key before the test
begins.

Change-Id:Ia6d3d81e2222f2b5245bfa8981a6677c04507799Signed-off-by: madan ankapura <mankapur@sta.samsung.com>*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/QwertyKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/QwertyKeyListenerTest.java
//Synthetic comment -- index c86dba3..533435b 100644

//Synthetic comment -- @@ -25,6 +25,7 @@

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemProperties;
import android.test.ActivityInstrumentationTestCase2;
import android.text.InputType;
import android.text.Selection;
//Synthetic comment -- @@ -74,6 +75,10 @@

prepareEmptyTextView();

        if(SystemProperties.getInt("ro.multilanguage",0)==1) {
           sendKeys(KeyEvent.KEYCODE_SYM);
        }

callOnKeyDown(keyListener, KeyEvent.KEYCODE_H);
assertEquals("h", mTextView.getText().toString());

//Synthetic comment -- @@ -96,6 +101,10 @@

prepareEmptyTextView();

        if(SystemProperties.getInt("ro.multilanguage",0)==1) {
           sendKeys(KeyEvent.KEYCODE_SYM);
        }

callOnKeyDown(keyListener, KeyEvent.KEYCODE_H);
assertEquals("H", mTextView.getText().toString());

//Synthetic comment -- @@ -118,6 +127,10 @@

prepareEmptyTextView();

        if(SystemProperties.getInt("ro.multilanguage",0)==1) {
           sendKeys(KeyEvent.KEYCODE_SYM);
        }

callOnKeyDown(keyListener, KeyEvent.KEYCODE_H);
assertEquals("H", mTextView.getText().toString());

//Synthetic comment -- @@ -146,6 +159,10 @@

prepareEmptyTextView();

        if(SystemProperties.getInt("ro.multilanguage",0)==1) {
           sendKeys(KeyEvent.KEYCODE_SYM);
        }

callOnKeyDown(keyListener, KeyEvent.KEYCODE_H);
assertEquals("H", mTextView.getText().toString());








