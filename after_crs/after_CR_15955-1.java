/*Fix issue #8626 - Froyo RTL regression

Change-Id:Id1a5b53e98c9e63f863ae50aa3b417f9f10543f9*/




//Synthetic comment -- diff --git a/core/java/android/text/Layout.java b/core/java/android/text/Layout.java
//Synthetic comment -- index 38ac9b7..e2150ae 100644

//Synthetic comment -- @@ -297,32 +297,25 @@
int left = 0;
int right = mWidth;

            // Draw all leading margin spans.  Adjust left always since programs that are not
            // bidi-aware treat the leading margin as the left and when it should semantically be
            // from the right, it destroys the screen layout in applications like the MMS app.
if (spannedText) {
final int length = spans.length;
for (int n = 0; n < length; n++) {
if (spans[n] instanceof LeadingMarginSpan) {
LeadingMarginSpan margin = (LeadingMarginSpan) spans[n];

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
//Synthetic comment -- @@ -1810,7 +1803,7 @@
public static class Directions {
private short[] mDirections;

        // The values in mDirections are the offsets from the last flip in direction
// in the line to the next flip in direction.  Runs at even indices
// are left-to-right, the others are right-to-left.  So, for example,
// a line that starts with a right-to-left run has 0 at mDirections[0],
//Synthetic comment -- @@ -1823,6 +1816,39 @@
/* package */ Directions(short[] dirs) {
mDirections = dirs;
}

        // This assumes the base direction is based on the direction mostly used in the text
        // this was written to fix the problem in froyo that nothing was aligned to the right,
        // and this enables the use of latin contacts with RTL text.
        public static int baseDirection(Directions dir,int length) {
            if (dir == DIRS_ALL_LEFT_TO_RIGHT) {
                return DIR_LEFT_TO_RIGHT;
            } else if (dir == DIRS_ALL_RIGHT_TO_LEFT) {
                return DIR_RIGHT_TO_LEFT;
            } 

            int sum=0;
            int lastSwitch=0;
            int i=0;
            while ((i+1) < dir.mDirections.length) {
                sum+=dir.mDirections[i];
                sum-=dir.mDirections[i+1];
                lastSwitch=dir.mDirections[i+1];
                i+=2;
            }

            if ((i+1)==dir.mDirections.length) {
                sum+=dir.mDirections[i];
            } else if (i==dir.mDirections.length) {
                sum-=length-lastSwitch;
            }

            if (sum>=0) {
                return DIR_LEFT_TO_RIGHT;
            } else {
                return DIR_RIGHT_TO_LEFT;
            }
        }
}

/**








//Synthetic comment -- diff --git a/core/java/android/text/StaticLayout.java b/core/java/android/text/StaticLayout.java
//Synthetic comment -- index f02ad2a..4d1f084 100644

//Synthetic comment -- @@ -1244,7 +1244,7 @@
}

public int getParagraphDirection(int line) {
        return Directions.baseDirection(mLineDirections[line],getLineEnd(line)-getLineStart(line));
}

public boolean getLineContainsTab(int line) {







