/*SDK Manager fix display of update revisions.

A couple of occurences were displaying full revision
for updates (e.g "Update available: rev. 20.0.0")
instead of the short version (e.g. "rev. 20").

Change-Id:I593ba952df714b50e548dbb447aaf9ac4e4192ca*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/AddonPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/AddonPackage.java
//Synthetic comment -- index fff45b9..fd8545c 100755

//Synthetic comment -- @@ -488,7 +488,7 @@
return String.format("%1$s, Android API %2$s, revision %3$s%4$s",
getDisplayName(),
mVersion.getApiString(),
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "");
}

//Synthetic comment -- @@ -503,7 +503,7 @@
String s = String.format("%1$s, Android API %2$s, revision %3$s%4$s\nBy %5$s",
getDisplayName(),
mVersion.getApiString(),
                getRevision().toShortString(),
isObsolete() ? " (Obsolete)" : "",  //$NON-NLS-2$
getDisplayVendor());









//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/sdkman2/PackagesPage.java
//Synthetic comment -- index 109c061..0b6d725 100755

//Synthetic comment -- @@ -1376,7 +1376,7 @@
if (update != null) {
return String.format(
"Update available: rev. %1$s",
                                    update.getRevision().toShortString());
}
return "Installed";








