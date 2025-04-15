/*Fix Broken LinkifyTest Tests

Updated the assertions...not sure why these were marked as broken as the
code seems to work logically.

Change-Id:I3a9e1161e8a20d51d76d159e9f050329065398e4*/




//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/util/cts/LinkifyTest.java b/tests/tests/text/src/android/text/util/cts/LinkifyTest.java
//Synthetic comment -- index 0733b9f..2c45c77 100644

//Synthetic comment -- @@ -16,18 +16,15 @@

package android.text.util.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;

import android.test.AndroidTestCase;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.widget.TextView;
//Synthetic comment -- @@ -90,7 +87,6 @@
method = "addLinks",
args = {android.text.Spannable.class, int.class}
)
public void testAddLinks1() {
SpannableString spannable = new SpannableString("name@gmail.com, "
+ "123456789, tel:(0812)1234567 "
//Synthetic comment -- @@ -129,7 +125,6 @@
method = "addLinks",
args = {android.widget.TextView.class, int.class}
)
public void testAddLinks2() {
String text = "www.google.com, name@gmail.com";
TextView tv = new TextView(mContext);
//Synthetic comment -- @@ -164,7 +159,6 @@
args = {android.widget.TextView.class, java.util.regex.Pattern.class,
java.lang.String.class}
)
public void testAddLinks3() {
String text = "Alan, Charlie";
TextView tv = new TextView(mContext);
//Synthetic comment -- @@ -216,8 +210,6 @@
java.lang.String.class, android.text.util.Linkify.MatchFilter.class,
android.text.util.Linkify.TransformFilter.class}
)
public void testAddLinks4() {
TextView tv = new TextView(mContext);

//Synthetic comment -- @@ -226,8 +218,9 @@
Linkify.addLinks(tv, LINKIFY_TEST_PATTERN, "Test:",
mMatchFilterStartWithDot, mTransformFilterUpperChar);
URLSpan[] spans = ((Spannable) tv.getText()).getSpans(0, text.length(), URLSpan.class);
        assertEquals(2, spans.length);
assertEquals("test:ilterpperase.pattern", spans[0].getURL());
        assertEquals("test:12", spans[1].getURL());

try {
Linkify.addLinks((TextView) null, LINKIFY_TEST_PATTERN, "Test:",
//Synthetic comment -- @@ -249,21 +242,24 @@
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
//Synthetic comment -- @@ -272,7 +268,6 @@
method = "addLinks",
args = {android.text.Spannable.class, java.util.regex.Pattern.class, java.lang.String.class}
)
public void testAddLinks5() {
String text = "google.pattern, test:AZ0101.pattern";

//Synthetic comment -- @@ -312,8 +307,6 @@
android.text.util.Linkify.MatchFilter.class,
android.text.util.Linkify.TransformFilter.class}
)
public void testAddLinks6() {
String text = "FilterUpperCase.pattern, 12.345.pattern";

//Synthetic comment -- @@ -321,8 +314,9 @@
Linkify.addLinks(spannable, LINKIFY_TEST_PATTERN, "Test:",
mMatchFilterStartWithDot, mTransformFilterUpperChar);
URLSpan[] spans = (spannable.getSpans(0, spannable.length(), URLSpan.class));
        assertEquals(2, spans.length);
assertEquals("test:ilterpperase.pattern", spans[0].getURL());
        assertEquals("test:12", spans[1].getURL());

try {
Linkify.addLinks((Spannable)null, LINKIFY_TEST_PATTERN, "Test:",
//Synthetic comment -- @@ -344,20 +338,23 @@
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







