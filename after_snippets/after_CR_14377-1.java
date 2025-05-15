
//<Beginning of snippet n. 0>


import android.os.IBinder;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;

@TestTargetClass(Instrumentation.class)
public class InstrumentationTest extends InstrumentationTestCase {
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


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


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
mInstrumentation.waitForIdleSync();
assertEquals("12a", mTextView.getText().toString());

        mInstrumentation.sendStringSync("1");
assertEquals("12a", mTextView.getText().toString());
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


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
}

// press 'm' key.
        mInstrumentation.sendStringSync("m");
assertEquals("12am", mTextView.getText().toString());

mActivity.runOnUiThread(new Runnable() {
mInstrumentation.waitForIdleSync();

// press '1' key.
        mInstrumentation.sendStringSync("1");
assertEquals("12am", mTextView.getText().toString());
}


//<End of snippet n. 2>










//<Beginning of snippet n. 3>



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
}
assertEquals('\0', mKeyCharacterMap.getMatch(1000, chars, 2));
assertEquals('\0', mKeyCharacterMap.getMatch(10000, chars, 2));
        assertEquals('\0', mKeyCharacterMap.getMatch(getCharacterKeyCode('E'), chars));
        assertEquals('A', mKeyCharacterMap.getMatch(getCharacterKeyCode('A'), chars));
        assertEquals('B', mKeyCharacterMap.getMatch(getCharacterKeyCode('B'), chars));
}

@TestTargetNew(

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


});
mInstrumentation.waitForIdleSync();

        mInstrumentation.sendStringSync("123");
assertEquals("", mDialerFilter.getLetters().toString());
assertEquals("123", mDialerFilter.getDigits().toString());

});
mInstrumentation.waitForIdleSync();

        mInstrumentation.sendStringSync("adg");
assertEquals("ADG", mDialerFilter.getLetters().toString());
assertEquals("", mDialerFilter.getDigits().toString());

});
mInstrumentation.waitForIdleSync();

        mInstrumentation.sendStringSync("adg");
assertEquals("ADG", mDialerFilter.getLetters().toString());
// A, D, K may map to numbers on some keyboards. Don't test.

});
mInstrumentation.waitForIdleSync();

        mInstrumentation.sendStringSync("123");
// 1, 2, 3 may map to letters on some keyboards. Don't test.
assertEquals("123", mDialerFilter.getDigits().toString());
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
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
assertEquals("1", mTextView.getText().toString());

// press 'a' key.
        mInstrumentation.sendStringSync("a");

assertEquals("1a", mTextView.getText().toString());
}


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

});
mInstrumentation.waitForIdleSync();

        mInstrumentation.sendStringSync("a");
assertEquals("a", mTextView.getText().toString());
        mInstrumentation.sendStringSync("b");
assertEquals("ab", mTextView.getText().toString());
sendKeys(KeyEvent.KEYCODE_DEL);
assertEquals("a", mTextView.getText().toString());

//<End of snippet n. 5>








