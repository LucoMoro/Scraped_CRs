/*Make the default skin a bit more dynamic based on the platform.

Change-Id:I37db0034cc106b573f0efb844cae074cb63ba588*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/PlatformTarget.java
//Synthetic comment -- index 2a22d0a..e14af0b 100644

//Synthetic comment -- @@ -178,8 +178,17 @@
}

public String getDefaultSkin() {
        if (mSkins.length == 1) {
            return mSkins[0];
        }

        if (mVersion.getApiLevel() >= 4) {
            // at this time, this is the default skin for all the platforms.
            // TODO: make it configurable using a file in the platform folder.
            return "WVGA800";
        }

        return "HVGA"; // this is for 1.5 and earlier.
}

/**







