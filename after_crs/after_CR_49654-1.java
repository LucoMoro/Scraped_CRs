/*telephony: Fix typo

This patch fixes the typo in CatService.

Change-Id:Iff69df0ab0309e36f0a886c483f280febe5ad84eAuthor: Nizar Haouati <nizar.haouati@intel.com>
Signed-off-by: Nizar Haouati <nizar.haouati@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 71115*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CatService.java b/src/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 4eacae3..30c979d 100644

//Synthetic comment -- @@ -100,7 +100,7 @@
private static final int DEV_ID_TERMINAL    = 0x82;
private static final int DEV_ID_NETWORK     = 0x83;

    static final String STK_DEFAULT = "Default Message";

/* Intentionally private for singleton */
private CatService(CommandsInterface ci, UiccCardApplication ca, IccRecords ir,







