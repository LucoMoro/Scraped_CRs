/*SDK Manager: auto-select Support Library at startup.

When the SDK Manager starts, it auto-selects the
Windows driver on Windows. This change makes it
also auto-select the Support library extra package.
The rationale is that a lot of code now depends on it
(ADT templates, docs' sample code) so sooner or later
the user will need to install it anyway. We can help
newcomers by selecting it upfront.

Change-Id:I292c2d324bf68fe6b60f901fa5ecde0176ea5ec1*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/core/PackagesDiffLogic.java
//Synthetic comment -- index f5a2ed3..a95c8ac 100755

//Synthetic comment -- @@ -252,18 +252,30 @@
}
}

        if (selectTop && currentPlatform == SdkConstants.PLATFORM_WINDOWS) {
            // On Windows, we'll also auto-select the USB driver
for (PkgItem item : getAllPkgItems(true /*byApi*/, true /*bySource*/)) {
Package p = item.getMainPackage();
if (p instanceof ExtraPackage &&
item.getState() == PkgState.NEW &&
!item.getRevision().isPreview()) {
ExtraPackage ep = (ExtraPackage) p;
                    if (ep.getVendorId().equals("google") &&            //$NON-NLS-1$
                            ep.getPath().equals("usb_driver")) {        //$NON-NLS-1$
                        item.setChecked(true);
}
}
}
}







