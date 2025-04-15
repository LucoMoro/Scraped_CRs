/*fix Camera launch crash after tap power key to wake up the phone.

Change-Id:I11cf4d95e10afcd78c4f8dc3b4ffe94f808d3253Author: Huaqiang Chen<huaqiangx.chen@intel.com>
Singed-off-by: Yimin Xie <Mason.xie@borqs.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 25992*/
//Synthetic comment -- diff --git a/core/java/android/widget/FrameLayout.java b/core/java/android/widget/FrameLayout.java
//Synthetic comment -- index d019d8c7..cbe113e 100644

//Synthetic comment -- @@ -397,7 +397,7 @@

for (int i = 0; i < count; i++) {
final View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
final LayoutParams lp = (LayoutParams) child.getLayoutParams();

final int width = child.getMeasuredWidth();







