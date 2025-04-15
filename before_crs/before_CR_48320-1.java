/*Launcher2: JAVECRASH about ‘com.android.launcher’ happens when long pressing allAppsButton and Hotseat at the same time.

Can't get the right shortcut view When long press allAppsButton in Hotseat.
	   It will cause itemUnderLongClick.getTag() = null, so we need to check it.

Change-Id:I88b2cb9af61f4b2197f423b222a1ac031bfc188bAuthor: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: Jianchun Shen <jianchunx.shen@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 45586*/
//Synthetic comment -- diff --git a/src/com/android/launcher2/Launcher.java b/src/com/android/launcher2/Launcher.java
//Synthetic comment -- index c221815..a5dc600 100644

//Synthetic comment -- @@ -2313,7 +2313,9 @@
} else {
if (!(itemUnderLongClick instanceof Folder)) {
// User long pressed on an item
                    mWorkspace.startDrag(longClickCellInfo);
}
}
}







