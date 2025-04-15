/*Merge "SDK Manager: auto-select Support Library at startup."

When the SDK Manager starts, it auto-selects the
Windows driver on Windows. This change makes it
also auto-select the Support library extra package.
The rationale is that a lot of code now depends on it
(ADT templates, docs' sample code) so sooner or later
the user will need to install it anyway. We can help
newcomers by selecting it upfront.

(cherry picked from commit 15832585a03db4e9ccb0b9732c7c9fd2677f8529)

Change-Id:I26c6277deb569b58ec904342dbd98dd347c11dde*/
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







