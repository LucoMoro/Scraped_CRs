/*Fix issue #8626 - Froyo RTL regression

Change-Id:I1a64b9971c8a86d88d357c3cb41601e025d01c8d*/




//Synthetic comment -- diff --git a/core/java/android/text/Layout.java b/core/java/android/text/Layout.java
//Synthetic comment -- index 38ac9b7..3d79501 100644

//Synthetic comment -- @@ -305,13 +305,13 @@
if (spans[n] instanceof LeadingMarginSpan) {
LeadingMarginSpan margin = (LeadingMarginSpan) spans[n];

                       // if (dir == DIR_RIGHT_TO_LEFT) {
                       //     margin.drawLeadingMargin(c, paint, right, dir, ltop,
                         //                            lbaseline, lbottom, buf,
                          //                           start, end, isFirstParaLine, this);
                          //      
                         //   right -= margin.getLeadingMargin(isFirstParaLine);
                        //} else {
margin.drawLeadingMargin(c, paint, left, dir, ltop,
lbaseline, lbottom, buf,
start, end, isFirstParaLine, this);
//Synthetic comment -- @@ -320,7 +320,7 @@
if (margin instanceof LeadingMarginSpan.LeadingMarginSpan2) {
int count = ((LeadingMarginSpan.LeadingMarginSpan2)margin).getLeadingMarginLineCount();
useMargin = count > i;
                          //  }
left += margin.getLeadingMargin(useMargin);
}
}








//Synthetic comment -- diff --git a/core/java/android/text/StaticLayout.java b/core/java/android/text/StaticLayout.java
//Synthetic comment -- index f02ad2a..d6d5b98 100644

//Synthetic comment -- @@ -1244,7 +1244,7 @@
}

public int getParagraphDirection(int line) {
        return mLineDirections[line] == DIRS_ALL_LEFT_TO_RIGHT? DIR_LEFT_TO_RIGHT : DIR_RIGHT_TO_LEFT;
}

public boolean getLineContainsTab(int line) {







