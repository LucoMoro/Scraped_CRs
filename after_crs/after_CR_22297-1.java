/*SDK Manager: Fix detection of emulator in tools local folder.

That's because the constant used did not have
the extension anymore.

Change-Id:I9ab254eec5c28c4d006ba4132d6b0c6d87c01e15*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/LocalSdkParser.java
//Synthetic comment -- index eb00562..c4b92b5 100755

//Synthetic comment -- @@ -303,8 +303,12 @@
names.add(file.getName());
}
}

        final String emulatorBinName =
            SdkConstants.FN_EMULATOR + SdkConstants.FN_EMULATOR_EXTENSION;

if (!names.contains(SdkConstants.androidCmdName()) ||
                !names.contains(emulatorBinName)) {
return null;
}








