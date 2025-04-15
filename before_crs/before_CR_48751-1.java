/*hide ime as camera hard key press

produce Step:
Go to Settings-> Wireless & networks-> WiFi-> Turned on WiFi;
Select 'Add network';
Active keyboard and input some words in Network SSID;
Long tap the inputted words and select 'Edit'.(The list with 'Select all, Cut and Copy' are show.);
Press Camera key to active Camera application;
Check the view: Camera main view is messy with keyboard.

This issue is not reproduced in Sansung Nexus S as Camera key is not available in Samsund Nexus S.

rootcause: hide ime as camera hard key press

Change-Id:I718cfb8f4f380ae99fa44a9590d91c939a9d64a5Author: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Zhang Jun <jun.zhang@borqs.com>
Signed-off-by: Bing Deng <bingx.deng@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 27714*/
//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index c9ff595..3b6aaae 100644

//Synthetic comment -- @@ -428,8 +428,12 @@
setImeWindowVisibilityStatusHiddenLocked();
updateActive();
return;
            } else if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
hideInputMethodMenu();
// No need to updateActive
return;
} else {







