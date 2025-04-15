/*Fix issue #8626 - Froyo RTL regression

Change-Id:Id1a5b53e98c9e63f863ae50aa3b417f9f10543f9*/
//Synthetic comment -- diff --git a/core/java/android/text/Layout.java b/core/java/android/text/Layout.java
//Synthetic comment -- index 38ac9b7..e2150ae 100644

//Synthetic comment -- @@ -297,32 +297,25 @@
int left = 0;
int right = mWidth;

            // Draw all leading margin spans.  Adjust left or right according
            // to the paragraph direction of the line.
if (spannedText) {
final int length = spans.length;
for (int n = 0; n < length; n++) {
if (spans[n] instanceof LeadingMarginSpan) {
LeadingMarginSpan margin = (LeadingMarginSpan) spans[n];

                        if (dir == DIR_RIGHT_TO_LEFT) {
                            margin.drawLeadingMargin(c, paint, right, dir, ltop,
                                                     lbaseline, lbottom, buf,
                                                     start, end, isFirstParaLine, this);
                                
                            right -= margin.getLeadingMargin(isFirstParaLine);
                        } else {
                            margin.drawLeadingMargin(c, paint, left, dir, ltop,
                                                     lbaseline, lbottom, buf,
                                                     start, end, isFirstParaLine, this);

                            boolean useMargin = isFirstParaLine;
                            if (margin instanceof LeadingMarginSpan.LeadingMarginSpan2) {
                                int count = ((LeadingMarginSpan.LeadingMarginSpan2)margin).getLeadingMarginLineCount();
                                useMargin = count > i;
                            }
                            left += margin.getLeadingMargin(useMargin);
}
}
}
}
//Synthetic comment -- @@ -1810,7 +1803,7 @@
public static class Directions {
private short[] mDirections;

        // The values in mDirections are the offsets from the first character
// in the line to the next flip in direction.  Runs at even indices
// are left-to-right, the others are right-to-left.  So, for example,
// a line that starts with a right-to-left run has 0 at mDirections[0],
//Synthetic comment -- @@ -1823,6 +1816,39 @@
/* package */ Directions(short[] dirs) {
mDirections = dirs;
}
}

/**








//Synthetic comment -- diff --git a/core/java/android/text/StaticLayout.java b/core/java/android/text/StaticLayout.java
//Synthetic comment -- index f02ad2a..4d1f084 100644

//Synthetic comment -- @@ -1244,7 +1244,7 @@
}

public int getParagraphDirection(int line) {
        return mLines[mColumns * line + DIR] >> DIR_SHIFT;
}

public boolean getLineContainsTab(int line) {







