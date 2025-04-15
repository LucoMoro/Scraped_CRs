/*Fix dividers for LinearLayout (TabWidget)

child.getTop()/child.getLeft - already contains offset for divider height/width,
so we need to subtract it, otherwise divider will be drawn behind the child.

Change-Id:Idd6e5aa4b20e84c64daaefdf393bc00fafb26c45Signed-off-by: Vladimir Baryshnikov <vovkab@gmail.com>*/




//Synthetic comment -- diff --git a/core/java/android/widget/LinearLayout.java b/core/java/android/widget/LinearLayout.java
//Synthetic comment -- index 427fd3e..e67e4bb 100644

//Synthetic comment -- @@ -307,7 +307,7 @@
if (child != null && child.getVisibility() != GONE) {
if (hasDividerBeforeChildAt(i)) {
final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    final int top = child.getTop() - lp.topMargin - mDividerHeight;
drawHorizontalDivider(canvas, top);
}
}
//Synthetic comment -- @@ -334,7 +334,7 @@
if (child != null && child.getVisibility() != GONE) {
if (hasDividerBeforeChildAt(i)) {
final LayoutParams lp = (LayoutParams) child.getLayoutParams();
                    final int left = child.getLeft() - lp.leftMargin - mDividerWidth;
drawVerticalDivider(canvas, left);
}
}







