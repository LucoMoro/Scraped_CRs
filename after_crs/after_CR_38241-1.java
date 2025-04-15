/*Mms: Take Ellipsis also in to consideration when comparing width

For initial comparison of Message width and available text field width
Ellipsis is not taken in to consideration so Mms app is crashing
if message width is between 435 to 450.

Modify to take Ellipsis also in to consideration when ever
comparing the message width.

Change-Id:Id089b5f2707720d1038d4a348bc20389fccf6a9b*/




//Synthetic comment -- diff --git a/src/com/android/mms/ui/SearchActivity.java b/src/com/android/mms/ui/SearchActivity.java
//Synthetic comment -- index e7bfcb0..e584cb2 100644

//Synthetic comment -- @@ -117,12 +117,13 @@
float searchStringWidth = tp.measureText(mTargetString);
float textFieldWidth = getWidth();

            float ellipsisWidth = tp.measureText(sEllipsis);
            textFieldWidth -= (2F * ellipsisWidth); // assume we'll need one on both ends

String snippetString = null;
if (searchStringWidth > textFieldWidth) {
snippetString = mFullText.substring(startPos, startPos + searchStringLength);
} else {

int offset = -1;
int start = -1;







