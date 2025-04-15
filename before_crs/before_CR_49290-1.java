/*SDK Manager: make "choose package to install" dialog taller.

SDL Bug: 42445

Change-Id:I6969951b8bf1cc58f65fe05b2e5f16ecd2c2e64f*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java
//Synthetic comment -- index 49bebba..7e66f3a 100755

//Synthetic comment -- @@ -366,7 +366,7 @@
return sLastSize;
} else {
// Arbitrary values that look good on my screen and fit on 800x600
            return new Point(740, 370);
}
}








