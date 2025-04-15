/*Remove Broken MediaControllerTests

Bug 3188260

These don't seem like they will be reliable even if they are fixed.

Change-Id:Ib2c077a0a98afd7247df7641d33781c2c32d0be6*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/MediaControllerTest.java b/tests/tests/widget/src/android/widget/cts/MediaControllerTest.java
//Synthetic comment -- index 40465ea..9f27138 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.cts.stub.R;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -31,11 +30,9 @@
import android.app.Instrumentation;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.AttributeSet;
import android.util.Xml;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
//Synthetic comment -- @@ -266,50 +263,6 @@

@TestTargetNew(
level = TestLevel.COMPLETE,
notes = "Test {@link MediaController#onTrackballEvent(MotionEvent)}, " +
"this function always returns false",
method = "onTrackballEvent",
//Synthetic comment -- @@ -351,46 +304,6 @@

@TestTargetNew(
level = TestLevel.COMPLETE,
notes = "Test {@link MediaController#setEnabled(boolean)}",
method = "setEnabled",
args = {boolean.class}







