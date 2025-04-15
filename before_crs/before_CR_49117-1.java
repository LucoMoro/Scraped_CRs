/*permission: add xt_qtaguid file permession checks.

In the past, the xt_qtaguid netfilter would auto-init the groups allowed
to access and control its data based on AID_NET_BW_ACCT and
AID_NET_BW_STATS. But now, it relies on file group ownership to get those
group IDs. They should be setup in init.rc.

Change-Id:I5d1ddc07eb6d1c31510bea2875076e930bd0039d*/
//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java b/tests/tests/permission/src/android/permission/cts/FileSystemPermissionTest.java
//Synthetic comment -- index 38f6244..1b09fb2 100644

//Synthetic comment -- @@ -192,6 +192,39 @@
assertFileOwnedByGroup(f, "nfc");
}

/**
* Assert that a file is owned by a specific owner. This is a noop if the
* file does not exist.







