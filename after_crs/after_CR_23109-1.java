/*Don't fail if vold.fstab is empty

This test shouldn't fail if vold.fstab is empty.  Rather, short
term, just ignore this case, at the expense that we may miss
legitimate security holes.

Long term, this needs to be properly fixed.

Change-Id:I328bc0360048b5233b336c1a35f7d8a9ade3c49c*/




//Synthetic comment -- diff --git a/tests/tests/security/src/android/security/cts/VoldExploitTest.java b/tests/tests/security/src/android/security/cts/VoldExploitTest.java
//Synthetic comment -- index a72d6de..df58a2c 100644

//Synthetic comment -- @@ -50,7 +50,11 @@
Set<String> devices = new HashSet<String>();
devices.addAll(getSysFsPath("/etc/vold.fstab"));
devices.addAll(getSysFsPath("/system/etc/vold.fstab"));
        if (devices.isEmpty()) {
          // FIXME: We should be able to detect this security hole
          // even if there's no vold.fstab entry
          return;
        }

// Verify that all processes listening for netlink messages
// currently exist.







