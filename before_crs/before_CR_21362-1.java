/*Implementing more robust equality check.

Because of rounding in floating point operations, direct equality
check is not reliable. This implements robust method evaluating
distance of two numbers and comparing it to small enough constant.

Change-Id:Ie30684c472bfa38d0432f855f7075c34709958d5*/
//Synthetic comment -- diff --git a/core/java/android/text/Layout.java b/core/java/android/text/Layout.java
//Synthetic comment -- index 4e197cd..7a36e5c 100755

//Synthetic comment -- @@ -38,6 +38,8 @@
* For text that will not change, use a {@link StaticLayout}.
*/
public abstract class Layout {
private static final boolean DEBUG = false;
private static final ParagraphStyle[] NO_PARA_SPANS =
ArrayUtils.emptyArray(ParagraphStyle.class);
//Synthetic comment -- @@ -1154,7 +1156,7 @@
if (h2 < 0.5f)
h2 = 0.5f;

        if (h1 == h2) {
dest.moveTo(h1, top);
dest.lineTo(h1, bottom);
} else {







