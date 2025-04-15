/*Fix Broken LinkifyTest Tests

Updated the assertions...not sure why these were marked as broken as the
code seems to work logically.

Change-Id:I3a9e1161e8a20d51d76d159e9f050329065398e4*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/util/cts/LinkifyTest.java b/tests/tests/text/src/android/text/util/cts/LinkifyTest.java
//Synthetic comment -- index 0733b9f..e31e842 100644

//Synthetic comment -- @@ -16,7 +16,6 @@

package android.text.util.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
//Synthetic comment -- @@ -27,7 +26,6 @@
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.widget.TextView;
//Synthetic comment -- @@ -217,7 +215,6 @@
android.text.util.Linkify.TransformFilter.class}
)
@ToBeFixed(bug = "1417734", explanation = "NullPointerException issue")
public void testAddLinks4() {
TextView tv = new TextView(mContext);

//Synthetic comment -- @@ -226,8 +223,9 @@
Linkify.addLinks(tv, LINKIFY_TEST_PATTERN, "Test:",
mMatchFilterStartWithDot, mTransformFilterUpperChar);
URLSpan[] spans = ((Spannable) tv.getText()).getSpans(0, text.length(), URLSpan.class);
        assertEquals(2, spans.length);
assertEquals("test:ilterpperase.pattern", spans[0].getURL());
        assertEquals("test:12", spans[1].getURL());

try {
Linkify.addLinks((TextView) null, LINKIFY_TEST_PATTERN, "Test:",
//Synthetic comment -- @@ -249,21 +247,24 @@
Linkify.addLinks(tv, LINKIFY_TEST_PATTERN, null,
mMatchFilterStartWithDot, mTransformFilterUpperChar);
spans = ((Spannable) tv.getText()).getSpans(0, text.length(), URLSpan.class);
        assertEquals(2, spans.length);
assertEquals("ilterpperase.pattern", spans[0].getURL());
        assertEquals("12", spans[1].getURL());

tv.setText(text);
Linkify.addLinks(tv, LINKIFY_TEST_PATTERN, "Test:", null, mTransformFilterUpperChar);
spans = ((Spannable) tv.getText()).getSpans(0, text.length(), URLSpan.class);
        assertEquals(3, spans.length);
assertEquals("test:ilterpperase.pattern", spans[0].getURL());
        assertEquals("test:12", spans[1].getURL());
        assertEquals("test:345.pattern", spans[2].getURL());

tv.setText(text);
Linkify.addLinks(tv, LINKIFY_TEST_PATTERN, "Test:", mMatchFilterStartWithDot, null);
spans = ((Spannable) tv.getText()).getSpans(0, text.length(), URLSpan.class);
        assertEquals(2, spans.length);
assertEquals("test:FilterUpperCase.pattern", spans[0].getURL());
        assertEquals("test:12", spans[1].getURL());
}

@TestTargetNew(
//Synthetic comment -- @@ -313,7 +314,6 @@
android.text.util.Linkify.TransformFilter.class}
)
@ToBeFixed(bug = "1417734", explanation = "NullPointerException issue")
public void testAddLinks6() {
String text = "FilterUpperCase.pattern, 12.345.pattern";

//Synthetic comment -- @@ -321,8 +321,9 @@
Linkify.addLinks(spannable, LINKIFY_TEST_PATTERN, "Test:",
mMatchFilterStartWithDot, mTransformFilterUpperChar);
URLSpan[] spans = (spannable.getSpans(0, spannable.length(), URLSpan.class));
        assertEquals(2, spans.length);
assertEquals("test:ilterpperase.pattern", spans[0].getURL());
        assertEquals("test:12", spans[1].getURL());

try {
Linkify.addLinks((Spannable)null, LINKIFY_TEST_PATTERN, "Test:",
//Synthetic comment -- @@ -344,20 +345,23 @@
Linkify.addLinks(spannable, LINKIFY_TEST_PATTERN, null, mMatchFilterStartWithDot,
mTransformFilterUpperChar);
spans = (spannable.getSpans(0, spannable.length(), URLSpan.class));
        assertEquals(2, spans.length);
assertEquals("ilterpperase.pattern", spans[0].getURL());
        assertEquals("12", spans[1].getURL());

spannable = new SpannableString(text);
Linkify.addLinks(spannable, LINKIFY_TEST_PATTERN, "Test:", null, mTransformFilterUpperChar);
spans = (spannable.getSpans(0, spannable.length(), URLSpan.class));
        assertEquals(3, spans.length);
assertEquals("test:ilterpperase.pattern", spans[0].getURL());
        assertEquals("test:12", spans[1].getURL());
        assertEquals("test:345.pattern", spans[2].getURL());

spannable = new SpannableString(text);
Linkify.addLinks(spannable, LINKIFY_TEST_PATTERN, "Test:", mMatchFilterStartWithDot, null);
spans = (spannable.getSpans(0, spannable.length(), URLSpan.class));
        assertEquals(2, spans.length);
assertEquals("test:FilterUpperCase.pattern", spans[0].getURL());
        assertEquals("test:12", spans[1].getURL());
}
}







