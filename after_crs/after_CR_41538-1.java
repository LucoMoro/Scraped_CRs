/*SimUnlockScreen: disable IME in SimUnlockScreen view

SimUnlockScreen is not dismissed and instead is hiden by default. This
will leave a dangling IME soft keyboard after unlock. This commit
disables the IME.

Change-Id:I7c8fd7d8ac728fa27ffe2ece758fdbfa7f28b5e2Author: Wang Zhifeng <zhifeng.wang@intel.com>
Signed-off-by: Wang Zhifeng <zhifeng.wang@intel.com>
Singed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 44294*/




//Synthetic comment -- diff --git a/policy/src/com/android/internal/policy/impl/SimUnlockScreen.java b/policy/src/com/android/internal/policy/impl/SimUnlockScreen.java
//Synthetic comment -- index 80407f5..890baf8 100644

//Synthetic comment -- @@ -107,7 +107,7 @@

/** {@inheritDoc} */
public boolean needsInput() {
        return false;
}

/** {@inheritDoc} */







