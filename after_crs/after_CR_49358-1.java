/*Telephony: Fix Locale construction when language code is null

When the language code is null, Locale needs to be constructed
by calling the constructor with language code empty. Current
code calls the Locale with country code as language code.

Fix the issue by passing empty language code and the given
country code for Locale construction.

Change-Id:I528dbf26a4e31b759d25fa11c8e90bee9b872d85Author: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 34594*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/MccTable.java b/src/java/com/android/internal/telephony/MccTable.java
//Synthetic comment -- index 7d5a4e7..365773b 100644

//Synthetic comment -- @@ -101,7 +101,7 @@
} else {
Locale locale;
if (entry.language == null) {
                locale = new Locale("", entry.iso);
} else {
locale = new Locale(entry.language, entry.iso);
}







