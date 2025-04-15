/*Telephony: Initialize Menu.defaultItem to -1

Valid value for defaultItem is from 0...n-1.
Initializing it to 0 will result in 1st item
in the menu selected even when the CAT hasn't
supplied any default item.

Initialize the defaultItem to -1 to avoid
the 1st item getting selected.

Change-Id:Ibeacc30364b88d43bc4cdd3ef69cfc3b4b5691d2Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 63971*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/Menu.java b/src/java/com/android/internal/telephony/cat/Menu.java
//Synthetic comment -- index 7bbae01..134266d 100644

//Synthetic comment -- @@ -44,7 +44,7 @@
items = new ArrayList<Item>();
title = null;
titleAttrs = null;
        defaultItem = 0;
softKeyPreferred = false;
helpAvailable = false;
titleIconSelfExplanatory = false;







