/*Key Character Map Compatibility Fixes

Bug 2183699

Several CTS tests expect the keyboard character map to be QWERTY.
Fix the tests so they are keyboard character map agnostic by
replacing the sendKey calls to sendStringSync calls which load
the key character map and figure out what the real keys need to
be pressed. Avoid using literal A and try to figure out what the
real key would be needed to produce an A.

Change-Id:Ie2592a22e28e9cbad429403e276ef32f4799d203*/




//Synthetic comment -- diff --git a/tests/tests/app/src/android/app/cts/InstrumentationTest.java b/tests/tests/app/src/android/app/cts/InstrumentationTest.java
//Synthetic comment -- index a2c8eb8..5ce33a0 100644

//Synthetic comment -- @@ -36,6 +36,7 @@
import android.os.IBinder;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
//Synthetic comment -- @@ -49,7 +50,6 @@
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(Instrumentation.class)
public class InstrumentationTest extends InstrumentationTestCase {
//Synthetic comment -- @@ -491,12 +491,13 @@
assertEquals(text.length(), keyDownList.size());
assertEquals(text.length(), keyUpList.size());

        KeyCharacterMap kcm = KeyCharacterMap.load(KeyCharacterMap.BUILT_IN_KEYBOARD);
        KeyEvent[] keyEvents = kcm.getEvents(text.toCharArray());

        int i = 0;
        for (int j = 0; j < keyDownList.size(); j++) {
            assertEquals(keyEvents[i++].getKeyCode(), keyDownList.get(j).getKeyCode());
            assertEquals(keyEvents[i++].getKeyCode(), keyUpList.get(j).getKeyCode());
}
}









//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/DateTimeKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/DateTimeKeyListenerTest.java
//Synthetic comment -- index dd85b55..225c4b4 100644

//Synthetic comment -- @@ -124,15 +124,15 @@
assertEquals("", mTextView.getText().toString());

// press '1' key.
        mInstrumentation.sendStringSync("1");
assertEquals("1", mTextView.getText().toString());

// press '2' key.
        mInstrumentation.sendStringSync("2");
assertEquals("12", mTextView.getText().toString());

// press 'a' key.
        mInstrumentation.sendStringSync("a");
assertEquals("12a", mTextView.getText().toString());

// press an unaccepted key if it exists.
//Synthetic comment -- @@ -152,7 +152,7 @@
mInstrumentation.waitForIdleSync();
assertEquals("12a", mTextView.getText().toString());

        mInstrumentation.sendStringSync("1");
assertEquals("12a", mTextView.getText().toString());
}









//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/method/cts/TimeKeyListenerTest.java b/tests/tests/text/src/android/text/method/cts/TimeKeyListenerTest.java
//Synthetic comment -- index 7f2f6ef..278af6b 100644

//Synthetic comment -- @@ -118,15 +118,15 @@
assertEquals("", mTextView.getText().toString());

// press '1' key.
        mInstrumentation.sendStringSync("1");
assertEquals("1", mTextView.getText().toString());

// press '2' key.
        mInstrumentation.sendStringSync("2");
assertEquals("12", mTextView.getText().toString());

// press 'a' key.
        mInstrumentation.sendStringSync("a");
assertEquals("12a", mTextView.getText().toString());

// press an unaccepted key if it exists.
//Synthetic comment -- @@ -137,7 +137,7 @@
}

// press 'm' key.
        mInstrumentation.sendStringSync("m");
assertEquals("12am", mTextView.getText().toString());

mActivity.runOnUiThread(new Runnable() {
//Synthetic comment -- @@ -149,7 +149,7 @@
mInstrumentation.waitForIdleSync();

// press '1' key.
        mInstrumentation.sendStringSync("1");
assertEquals("12am", mTextView.getText().toString());
}









//Synthetic comment -- diff --git a/tests/tests/view/src/android/view/cts/KeyCharacterMapTest.java b/tests/tests/view/src/android/view/cts/KeyCharacterMapTest.java
//Synthetic comment -- index a96f384..3e6fb8e 100644

//Synthetic comment -- @@ -16,11 +16,13 @@

package android.view.cts;

import android.os.Debug;
import android.test.AndroidTestCase;
import android.text.TextUtils;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.KeyCharacterMap.KeyData;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -182,9 +184,17 @@
fail("should throw exception");
} catch (Exception e) {
}

        assertEquals('\0', mKeyCharacterMap.getMatch(getCharacterKeyCode('E'), chars));
        assertEquals('A', mKeyCharacterMap.getMatch(getCharacterKeyCode('A'), chars));
        assertEquals('B', mKeyCharacterMap.getMatch(getCharacterKeyCode('B'), chars));
    }

    private int getCharacterKeyCode(char oneChar) {
        // Lowercase the character to avoid getting modifiers in the KeyEvent array.
        char[] chars = new char[] {Character.toLowerCase(oneChar)};
        KeyEvent[] events = mKeyCharacterMap.getEvents(chars);
        return events[0].getKeyCode();
}

@TestTargetNew(
//Synthetic comment -- @@ -200,9 +210,9 @@
}
assertEquals('\0', mKeyCharacterMap.getMatch(1000, chars, 2));
assertEquals('\0', mKeyCharacterMap.getMatch(10000, chars, 2));
        assertEquals('\0', mKeyCharacterMap.getMatch(getCharacterKeyCode('E'), chars));
        assertEquals('A', mKeyCharacterMap.getMatch(getCharacterKeyCode('A'), chars));
        assertEquals('B', mKeyCharacterMap.getMatch(getCharacterKeyCode('B'), chars));
}

@TestTargetNew(








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/DialerFilterTest.java b/tests/tests/widget/src/android/widget/cts/DialerFilterTest.java
//Synthetic comment -- index f95057a..5b3d578 100644

//Synthetic comment -- @@ -125,7 +125,7 @@
});
mInstrumentation.waitForIdleSync();

        mInstrumentation.sendStringSync("123");
assertEquals("", mDialerFilter.getLetters().toString());
assertEquals("123", mDialerFilter.getDigits().toString());

//Synthetic comment -- @@ -137,7 +137,7 @@
});
mInstrumentation.waitForIdleSync();

        mInstrumentation.sendStringSync("adg");
assertEquals("ADG", mDialerFilter.getLetters().toString());
assertEquals("", mDialerFilter.getDigits().toString());

//Synthetic comment -- @@ -149,7 +149,7 @@
});
mInstrumentation.waitForIdleSync();

        mInstrumentation.sendStringSync("adg");
assertEquals("ADG", mDialerFilter.getLetters().toString());
// A, D, K may map to numbers on some keyboards. Don't test.

//Synthetic comment -- @@ -161,7 +161,7 @@
});
mInstrumentation.waitForIdleSync();

        mInstrumentation.sendStringSync("123");
// 1, 2, 3 may map to letters on some keyboards. Don't test.
assertEquals("123", mDialerFilter.getDigits().toString());
}








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/TextViewTest.java b/tests/tests/widget/src/android/widget/cts/TextViewTest.java
//Synthetic comment -- index dcd2b05..eac5801 100644

//Synthetic comment -- @@ -80,6 +80,7 @@
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
//Synthetic comment -- @@ -247,19 +248,19 @@
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
//Synthetic comment -- @@ -275,7 +276,8 @@
assertEquals("1", mTextView.getText().toString());

// press 'a' key.
        mInstrumentation.sendStringSync("a");

assertEquals("1a", mTextView.getText().toString());
}

//Synthetic comment -- @@ -1864,13 +1866,13 @@

assertEquals(errorText, mTextView.getError().toString());

        mInstrumentation.sendStringSync("a");
// a key event that will not change the TextView's text
assertEquals("", mTextView.getText().toString());
// The icon and error message will not be reset to null
assertNotNull(mTextView.getError());

        mInstrumentation.sendStringSync("1");
// a key event cause changes to the TextView's text
assertEquals("1", mTextView.getText().toString());
// the error message and icon will be cleared.
//Synthetic comment -- @@ -1911,13 +1913,13 @@

assertSame(expected, mTextView.getFilters());

        mInstrumentation.sendStringSync("a");
// the text is capitalized by InputFilter.AllCaps
assertEquals("A", mTextView.getText().toString());
        mInstrumentation.sendStringSync("b");
// the text is capitalized by InputFilter.AllCaps
assertEquals("AB", mTextView.getText().toString());
        mInstrumentation.sendStringSync("c");
// 'C' could not be accepted, because there is a length filter.
assertEquals("AB", mTextView.getText().toString());

//Synthetic comment -- @@ -2095,9 +2097,9 @@
});
mInstrumentation.waitForIdleSync();

        mInstrumentation.sendStringSync("a");
assertEquals("a", mTextView.getText().toString());
        mInstrumentation.sendStringSync("b");
assertEquals("ab", mTextView.getText().toString());
sendKeys(KeyEvent.KEYCODE_DEL);
assertEquals("a", mTextView.getText().toString());







