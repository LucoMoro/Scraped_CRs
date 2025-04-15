/*SdkManager: don't user versionName for new platform packages.

Change-Id:I56f0a619a5b050c65843730a4fe86a50b3c127ae*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/PlatformPackage.java
//Synthetic comment -- index 4e0618d..4ca5f3f 100755

//Synthetic comment -- @@ -171,9 +171,7 @@

// First find if this platform is already installed. If so, reuse the same directory.
for (IAndroidTarget target : sdkManager.getTargets()) {
            if (target.isPlatform() && target.getVersion().equals(mVersion)) {
return new File(target.getLocation());
}
}







