/*Browser freezes when adding new incognito tab after removing it

When do layout, the position of new tab may be changed since other tab
closed, we need to get the position again at the time.

Change-Id:Ia5bcc15a0074678f401d6b3e93026e5ef5d2af7dAuthor: weiweix.ji <weiweix.ji@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 66643*/
//Synthetic comment -- diff --git a/src/com/android/browser/NavScreen.java b/src/com/android/browser/NavScreen.java
//Synthetic comment -- index 1d2114e..fda8bcb 100644

//Synthetic comment -- @@ -176,7 +176,8 @@

@Override
public void onLayout(int l, int t, int r, int b) {
                    mUi.hideNavScreen(tix, true);
switchToTab(tab);
}
});







