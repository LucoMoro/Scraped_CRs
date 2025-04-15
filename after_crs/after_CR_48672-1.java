/*SystemServer: Don't call waitForAsecScan() during encrypting.

When preparing encryption, vold cannot receive any other cmds,
so the waitForAsecScan call in systemserver would be blocked
before timeout. That takes a lot of time to step into encryption
UI, this would confused end users.

This patch disabled waitForAsecScan calling when in encrytion procedure.

Change-Id:I77045ef2ab32fb33fdcb49d1f1fea0dc744db132Author: Liang Wang <liangx.wang@intel.com>
Signed-off-by: Liang Wang <liangx.wang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 57883*/




//Synthetic comment -- diff --git a/services/java/com/android/server/SystemServer.java b/services/java/com/android/server/SystemServer.java
//Synthetic comment -- index 894c4d0..4be5d96 100644

//Synthetic comment -- @@ -534,7 +534,8 @@
* AppWidget Provider. Make sure MountService is completely started
* first before continuing.
*/
            if (mountService != null &&
                   !ENCRYPTING_STATE.equals(SystemProperties.get("vold.decrypt"))) {
mountService.waitForAsecScan();
}








