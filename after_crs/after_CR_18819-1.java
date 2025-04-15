/*fix signature checking

This fix adds some flexibility to the platform signature checking
capability and also ensures that the behavior matches the comment
inline.

Currently, the platform documentation indicates that signatures must
match exactly for applications to be upgraded. This limits scenarios
like transitioning the key used to sign a particular application. Error
codes when upgrading applications published to the market indicate that
applications should have at least one signature in common, not an exact
match, although the behavior of the market is different and matches the
published documentation. A common signature should be sufficient to
support upgrade scenarios.

More importantly, this change adds flexibility to signature-based
permissions. Currently, signatures must match exactly between the
declaring and requesting applications for the successful granting of
permissions. This limits how the declaring
application's signature-based permissions can be used. With this change,
a common subset is required to ensure permissions are properly granted
which should be sufficiently secure.

Change-Id:Ib25c6ad69a02d3a1b121f4a7c5ef3b64eee3cb7e*/




//Synthetic comment -- diff --git a/services/java/com/android/server/PackageManagerService.java b/services/java/com/android/server/PackageManagerService.java
//Synthetic comment -- index b70d69b..67a4865 100644

//Synthetic comment -- @@ -1868,7 +1868,7 @@
set2.add(sig);
}
// Make sure s2 contains all signatures in s1.
        if (set2.containsAll(set1)) {
return PackageManager.SIGNATURE_MATCH;
}
return PackageManager.SIGNATURE_NO_MATCH;







