
//<Beginning of snippet n. 0>


import android.os.IBinder;
import android.os.SystemClock;
import android.test.InstrumentationTestCase;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

@TestTargetClass(Instrumentation.class)
public class InstrumentationTest extends InstrumentationTestCase {
assertEquals(text.length(), keyDownList.size());
assertEquals(text.length(), keyUpList.size());

        for (int i = 0; i < keyDownList.size(); i++) {
            assertEquals(KeyEvent.KEYCODE_A + i, keyDownList.get(i).getKeyCode());
        }

        for (int i = 0; i < keyUpList.size(); i++) {
            assertEquals(KeyEvent.KEYCODE_A + i, keyUpList.get(i).getKeyCode());
}
}


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


assertEquals("", mTextView.getText().toString());

// press '1' key.
        sendKeys(KeyEvent.KEYCODE_1);
assertEquals("1", mTextView.getText().toString());

// press '2' key.
        sendKeys(KeyEvent.KEYCODE_2);
assertEquals("12", mTextView.getText().toString());

// press 'a' key.
        sendKeys(KeyEvent.KEYCODE_A);
assertEquals("12a", mTextView.getText().toString());

// press an unaccepted key if it exists.
mInstrumentation.waitForIdleSync();
assertEquals("12a", mTextView.getText().toString());

        sendKeys(KeyEvent.KEYCODE_1);
assertEquals("12a", mTextView.getText().toString());
}


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


assertEquals("", mTextView.getText().toString());

// press '1' key.
        sendKeys(KeyEvent.KEYCODE_1);
assertEquals("1", mTextView.getText().toString());

// press '2' key.
        sendKeys(KeyEvent.KEYCODE_2);
assertEquals("12", mTextView.getText().toString());

// press 'a' key.
        sendKeys(KeyEvent.KEYCODE_A);
assertEquals("12a", mTextView.getText().toString());

// press an unaccepted key if it exists.
}

// press 'm' key.
        sendKeys(KeyEvent.KEYCODE_M);
assertEquals("12am", mTextView.getText().toString());

mActivity.runOnUiThread(new Runnable() {
mInstrumentation.waitForIdleSync();

// press '1' key.
        sendKeys(KeyEvent.KEYCODE_1);
assertEquals("12am", mTextView.getText().toString());
}


//<End of snippet n. 2>










//<Beginning of snippet n. 3>



package android.view.cts;

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
        assertEquals('\0', mKeyCharacterMap.getMatch(KeyEvent.KEYCODE_E, chars));
        assertEquals('A', mKeyCharacterMap.getMatch(KeyEvent.KEYCODE_A, chars));
        assertEquals('B', mKeyCharacterMap.getMatch(KeyEvent.KEYCODE_B, chars));
}

@TestTargetNew(
}
assertEquals('\0', mKeyCharacterMap.getMatch(1000, chars, 2));
assertEquals('\0', mKeyCharacterMap.getMatch(10000, chars, 2));
        assertEquals('\0', mKeyCharacterMap.getMatch(KeyEvent.KEYCODE_E, chars, 0));
        assertEquals('A', mKeyCharacterMap.getMatch(KeyEvent.KEYCODE_A, chars, 0));
        assertEquals('B', mKeyCharacterMap.getMatch(KeyEvent.KEYCODE_B, chars, 0));
}

@TestTargetNew(

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


});
mInstrumentation.waitForIdleSync();

        sendKeys(KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3);
assertEquals("", mDialerFilter.getLetters().toString());
assertEquals("123", mDialerFilter.getDigits().toString());

});
mInstrumentation.waitForIdleSync();

        sendKeys(KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_D, KeyEvent.KEYCODE_G);
assertEquals("ADG", mDialerFilter.getLetters().toString());
assertEquals("", mDialerFilter.getDigits().toString());

});
mInstrumentation.waitForIdleSync();

        sendKeys(KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_D, KeyEvent.KEYCODE_G);
assertEquals("ADG", mDialerFilter.getLetters().toString());
// A, D, K may map to numbers on some keyboards. Don't test.

});
mInstrumentation.waitForIdleSync();

        sendKeys(KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_3);
// 1, 2, 3 may map to letters on some keyboards. Don't test.
assertEquals("123", mDialerFilter.getDigits().toString());
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
assertEquals("", mTextView.getText().toString());

// press '-' key.
        sendKeys(KeyEvent.KEYCODE_MINUS);
assertEquals("", mTextView.getText().toString());

// press '1' key.
        sendKeys(KeyEvent.KEYCODE_1);
assertEquals("1", mTextView.getText().toString());

// press '.' key.
        sendKeys(KeyEvent.KEYCODE_PERIOD);
assertEquals("1", mTextView.getText().toString());

// press 'a' key.
        sendKeys(KeyEvent.KEYCODE_A);
assertEquals("1", mTextView.getText().toString());

final QwertyKeyListener qwertyKeyListener
assertEquals("1", mTextView.getText().toString());

// press 'a' key.
        sendKeys(KeyEvent.KEYCODE_A);
assertEquals("1a", mTextView.getText().toString());
}


assertEquals(errorText, mTextView.getError().toString());

        sendKeys(KeyEvent.KEYCODE_A);
// a key event that will not change the TextView's text
assertEquals("", mTextView.getText().toString());
// The icon and error message will not be reset to null
assertNotNull(mTextView.getError());

        sendKeys(KeyEvent.KEYCODE_1);
// a key event cause changes to the TextView's text
assertEquals("1", mTextView.getText().toString());
// the error message and icon will be cleared.

assertSame(expected, mTextView.getFilters());

        sendKeys(KeyEvent.KEYCODE_A);
// the text is capitalized by InputFilter.AllCaps
assertEquals("A", mTextView.getText().toString());
        sendKeys(KeyEvent.KEYCODE_B);
// the text is capitalized by InputFilter.AllCaps
assertEquals("AB", mTextView.getText().toString());
        sendKeys(KeyEvent.KEYCODE_C);
// 'C' could not be accepted, because there is a length filter.
assertEquals("AB", mTextView.getText().toString());

});
mInstrumentation.waitForIdleSync();

        sendKeys(KeyEvent.KEYCODE_A);
assertEquals("a", mTextView.getText().toString());
        sendKeys(KeyEvent.KEYCODE_B);
assertEquals("ab", mTextView.getText().toString());
sendKeys(KeyEvent.KEYCODE_DEL);
assertEquals("a", mTextView.getText().toString());

//<End of snippet n. 5>








