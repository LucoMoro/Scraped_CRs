/*PinyinIME: hide candidates view during input mode switch

Candidates view was not properly hidden when switching input mode
between Chinese text mode and other modes. This commit fixes the issue.

Change-Id:I835d2ea80a147b587784a1c957e22d29fcb5f7c1Author: Wang Zhifeng <zhifeng.wang@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 44092*/
//Synthetic comment -- diff --git a/src/com/android/inputmethod/pinyin/PinyinIME.java b/src/com/android/inputmethod/pinyin/PinyinIME.java
//Synthetic comment -- index 9ac2c2d..c3bb783 100644

//Synthetic comment -- @@ -819,6 +819,7 @@
if (null != mComposingView) mComposingView.reset();
if (resetInlineText) commitResultText("");
resetCandidateWindow();
}

private void chooseAndUpdate(int candId) {







