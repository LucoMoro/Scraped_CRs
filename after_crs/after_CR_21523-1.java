/*Fix ContextTest and ContextWrapperTest Interaction

Bug 3188260

ContextWrapperTest#testAccessTheme passed when run on its own
but failed when executed in batch mode. ContextTest was
interfering by calling obtainedStyleAttributes which was
calling getTheme. getTheme sets the theme to the default
if there is no theme. You can only set the theme once in
the context, so future calls like those in testAccessTheme
no longer worked and broke the test.

These ContextWrapperTests are somewhat silly since the
implementation of ContextWrapper is to just call the
same methods in the context it wraps...so I moved the
test into ContextTest and made sure to set the theme
to the test theme at the beginning.

These ContextTests are also funny, because there are multiple
implementations of Context and this one just happens to work
with the Context given back by AndroidTestCase...

Change-Id:I710439ea09b8c048178faacb269157d4ba4341cb*/




//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContextTest.java b/tests/tests/content/src/android/content/cts/ContextTest.java
//Synthetic comment -- index 3eac36a..c2a27ae 100644

//Synthetic comment -- @@ -16,7 +16,14 @@

package android.content.cts;

import com.android.cts.stub.R;
import com.android.internal.util.XmlUtils;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import org.xmlpull.v1.XmlPullParserException;

//Synthetic comment -- @@ -24,18 +31,12 @@
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.content.res.Resources.NotFoundException;
import android.content.res.Resources.Theme;
import android.test.AndroidTestCase;
import android.util.AttributeSet;
import android.util.Xml;

import java.io.IOException;

@TestTargetClass(Context.class)
public class ContextTest extends AndroidTestCase {
//Synthetic comment -- @@ -45,6 +46,7 @@
protected void setUp() throws Exception {
super.setUp();
mContext = getContext();
        mContext.setTheme(R.style.Test_Theme);
}

@TestTargets({
//Synthetic comment -- @@ -109,6 +111,46 @@
@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,
            method = "getTheme",
            args = {}
        ),
        @TestTargetNew(
            level = TestLevel.COMPLETE,
            method = "setTheme",
            args = {int.class}
        )
    })
    public void testAccessTheme() {
        mContext.setTheme(R.style.Test_Theme);
        final Theme testTheme = mContext.getTheme();
        assertNotNull(testTheme);

        int[] attrs = {
            android.R.attr.windowNoTitle,
            android.R.attr.panelColorForeground,
            android.R.attr.panelColorBackground
        };
        TypedArray attrArray = null;
        try {
            attrArray = testTheme.obtainStyledAttributes(attrs);
            assertTrue(attrArray.getBoolean(0, false));
            assertEquals(0xff000000, attrArray.getColor(1, 0));
            assertEquals(0xffffffff, attrArray.getColor(2, 0));
        } finally {
            if (attrArray != null) {
                attrArray.recycle();
                attrArray = null;
            }
        }

        // setTheme only works for the first time
        mContext.setTheme(android.R.style.Theme_Black);
        assertSame(testTheme, mContext.getTheme());
    }

    @TestTargets({
        @TestTargetNew(
            level = TestLevel.COMPLETE,
notes = "",
method = "obtainStyledAttributes",
args = {int[].class}
//Synthetic comment -- @@ -136,7 +178,7 @@
public void testObtainStyledAttributes() {
// Test obtainStyledAttributes(int[])
TypedArray testTypedArray = mContext
                .obtainStyledAttributes(android.R.styleable.View);
assertNotNull(testTypedArray);
assertTrue(testTypedArray.length() > 2);
assertTrue(testTypedArray.length() > 0);
//Synthetic comment -- @@ -144,7 +186,7 @@

// Test obtainStyledAttributes(int, int[])
testTypedArray = mContext.obtainStyledAttributes(android.R.style.TextAppearance_Small,
                android.R.styleable.TextAppearance);
assertNotNull(testTypedArray);
assertTrue(testTypedArray.length() > 2);
testTypedArray.recycle();
//Synthetic comment -- @@ -166,14 +208,14 @@

// Test obtainStyledAttributes(AttributeSet, int[])
testTypedArray = mContext.obtainStyledAttributes(getAttributeSet(R.layout.context_layout),
                android.R.styleable.DatePicker);
assertNotNull(testTypedArray);
assertEquals(2, testTypedArray.length());
testTypedArray.recycle();

// Test obtainStyledAttributes(AttributeSet, int[], int, int)
testTypedArray = mContext.obtainStyledAttributes(getAttributeSet(R.layout.context_layout),
                android.R.styleable.DatePicker, 0, 0);
assertNotNull(testTypedArray);
assertEquals(2, testTypedArray.length());
testTypedArray.recycle();








//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContextWrapperTest.java b/tests/tests/content/src/android/content/cts/ContextWrapperTest.java
//Synthetic comment -- index 65bdc47..6ad14e0 100644

//Synthetic comment -- @@ -35,8 +35,6 @@
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursorDriver;
import android.database.sqlite.SQLiteDatabase;
//Synthetic comment -- @@ -237,46 +235,6 @@
@TestTargets({
@TestTargetNew(
level = TestLevel.COMPLETE,
method = "registerReceiver",
args = {BroadcastReceiver.class, IntentFilter.class}
),







