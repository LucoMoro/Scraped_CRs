/*hide ime as camera hard key pressed

Rootcause: hide ime as camera hard key pressed

Produce Step:
Go to Settings-> Wireless & networks-> WiFi-> Turned on WiFi;
Select 'Add network';
Active keyboard and input some words in Network SSID;
Long tap the inputted words and select 'Edit'.(The list with 'Select all, Cut and Copy' are show.);
Press Camera key to active Camera application;
Check the view: Camera main view is messy with keyboard.

This issue may only be reproduced on devices with a hardware camera key.

Change-Id:Ica287e5faed33f3bbbc09a122c6edaab2240c09cAuthor: Huaqiang Chen<huaqiangx.chen@intel.com>
Signed-off-by: Jun Zhang <jun.zhang@borqs.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 27714*/
//Synthetic comment -- diff --git a/services/java/com/android/server/InputMethodManagerService.java b/services/java/com/android/server/InputMethodManagerService.java
//Synthetic comment -- index fdb278d..d4d28b8 100644

//Synthetic comment -- @@ -406,8 +406,13 @@
} else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
mScreenOn = false;
setImeWindowVisibilityStatusHiddenLocked();
            } else if (intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
hideInputMethodMenu();
return;
} else {
Slog.w(TAG, "Unexpected intent " + intent);







