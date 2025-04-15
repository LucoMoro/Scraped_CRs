/*Fix Broken ContextWrapperTest#testAccessTheme

Bug 3188260

The annotation said "needs investigation", but the test works fine.
There was a weird comment about setTheme only working for the first
time which is true...and looks like a bug in ContextThemeWrapper.

Change-Id:Ia228243d333dbced1568fd04dc39b8e441532c5a*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContextWrapperTest.java b/tests/tests/content/src/android/content/cts/ContextWrapperTest.java
//Synthetic comment -- index ac402ef..65bdc47 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.cts.stub.R;

import dalvik.annotation.BrokenTest;
import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -247,7 +246,6 @@
args = {int.class}
)
})
    @BrokenTest("needs investigation")
public void testAccessTheme() {
mContextWrapper.setTheme(R.style.Test_Theme);
final Theme testTheme = mContextWrapper.getTheme();
//Synthetic comment -- @@ -258,11 +256,18 @@
android.R.attr.panelColorForeground,
android.R.attr.panelColorBackground
};
        TypedArray attrArray = testTheme.obtainStyledAttributes(attrs);

        assertTrue(attrArray.getBoolean(0, false));
        assertEquals(0xff000000, attrArray.getColor(1, 0));
        assertEquals(0xffffffff, attrArray.getColor(2, 0));

// setTheme only works for the first time
mContextWrapper.setTheme(android.R.style.Theme_Black);







