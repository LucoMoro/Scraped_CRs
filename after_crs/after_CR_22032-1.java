/*Remove Broken Widget Tests

Bug 3188260

Change-Id:Icbc5700430d1575d601efa0013eabcc7969da267*/




//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/GalleryTest.java b/tests/tests/widget/src/android/widget/cts/GalleryTest.java
//Synthetic comment -- index e95deb7..aea178a 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import com.android.cts.stub.R;
import com.android.internal.view.menu.ContextMenuBuilder;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -34,7 +33,6 @@
import android.content.Context;
import android.os.SystemClock;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.util.AttributeSet;
//Synthetic comment -- @@ -133,70 +131,6 @@
}
}

@TestTargetNew(
level = TestLevel.NOT_FEASIBLE,
method = "setAnimationDuration",








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/TableLayoutTest.java b/tests/tests/widget/src/android/widget/cts/TableLayoutTest.java
//Synthetic comment -- index 258883d..8214102 100644

//Synthetic comment -- @@ -16,6 +16,14 @@

package android.widget.cts;

import com.android.cts.stub.R;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import dalvik.annotation.ToBeFixed;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.test.ActivityInstrumentationTestCase2;
//Synthetic comment -- @@ -33,15 +41,6 @@
import android.widget.TableRow;
import android.widget.TextView;

/**
* Test {@link TableLayout}.
*/
//Synthetic comment -- @@ -492,169 +491,6 @@
assertTrue(tableLayout.getChildAt(1).isLayoutRequested());
}

@TestTargetNew(
level = TestLevel.COMPLETE,
notes = "Test addView(View child)",








//Synthetic comment -- diff --git a/tests/tests/widget/src/android/widget/cts/VideoViewTest.java b/tests/tests/widget/src/android/widget/cts/VideoViewTest.java
//Synthetic comment -- index 8e1ffec..6b9aa84 100644

//Synthetic comment -- @@ -18,7 +18,6 @@

import com.android.cts.stub.R;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -31,7 +30,6 @@
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.view.View.MeasureSpec;
//Synthetic comment -- @@ -239,142 +237,6 @@
}.run();
}

@TestTargetNew(
level = TestLevel.COMPLETE,
method = "setOnErrorListener",







