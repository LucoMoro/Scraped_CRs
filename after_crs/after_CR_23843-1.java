/*Clarify vold isn't exploitable when no sd card exists

Change-Id:I24903ca0f6267c0034df068abcece6091f25f38e*/




//Synthetic comment -- diff --git a/tests/tests/security/src/android/security/cts/VoldExploitTest.java b/tests/tests/security/src/android/security/cts/VoldExploitTest.java
//Synthetic comment -- index df5e58a..38eece7 100644

//Synthetic comment -- @@ -51,8 +51,8 @@
devices.addAll(getSysFsPath("/etc/vold.fstab"));
devices.addAll(getSysFsPath("/system/etc/vold.fstab"));
if (devices.isEmpty()) {
          // This vulnerability is not exploitable if there's
          // no entry in vold.fstab
return;
}








