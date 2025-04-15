/*MIPS company name is all-caps

Change-Id:Ib2bb0b69e08a639b7469f1c9a67e97e8cdd6eb48*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java
//Synthetic comment -- index 9e5c61f..c4b6b18 100755

//Synthetic comment -- @@ -176,7 +176,7 @@
s = "Intel Atom (" + SdkConstants.ABI_INTEL_ATOM + ")";

} else if (raw.equalsIgnoreCase(SdkConstants.ABI_MIPS)) {
            s = "MIPS (" + SdkConstants.ABI_MIPS + ")";

} else {
s = raw + " (" + raw + ")";








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SystemImagePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SystemImagePackage.java
//Synthetic comment -- index d83e44c..69335a5 100755

//Synthetic comment -- @@ -210,7 +210,7 @@
private static String getAbiDisplayNameInternal(String abi) {
return abi.replace("armeabi", "ARM EABI")         //$NON-NLS-1$  //$NON-NLS-2$
.replace("x86",     "Intel x86 Atom")   //$NON-NLS-1$  //$NON-NLS-2$
                  .replace("mips",    "MIPS")             //$NON-NLS-1$  //$NON-NLS-2$
.replace("-", " ");                     //$NON-NLS-1$  //$NON-NLS-2$
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/sources/SdkRepoSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/sources/SdkRepoSourceTest.java
//Synthetic comment -- index 33f66e7..9df0bfd 100755

//Synthetic comment -- @@ -747,7 +747,7 @@
"Found Android SDK Platform-tools, revision 3\n" +
"Found Samples for SDK API 14, revision 24 (Obsolete)\n" +
"Found ARM EABI System Image, Android API 42, revision 12\n" +
                     "Found MIPS System Image, Android API 42, revision 12\n" +
"Found Sources for Android SDK, API 42, revision 12\n",
monitor.getCapturedVerboseLog());
assertEquals("", monitor.getCapturedLog());
//Synthetic comment -- @@ -891,7 +891,7 @@
"Found Samples for SDK API 14, revision 24 (Obsolete)\n" +
"Found Samples for SDK API 14, revision 25 (Obsolete)\n" +
"Found ARM EABI System Image, Android API 42, revision 12\n" +
                     "Found MIPS System Image, Android API 42, revision 12\n" +
"Found Sources for Android SDK, API 42, revision 12\n",
monitor.getCapturedVerboseLog());
assertEquals("", monitor.getCapturedLog());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/sources/SdkSysImgSourceTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/sources/SdkSysImgSourceTest.java
//Synthetic comment -- index db84755..c080d11 100755

//Synthetic comment -- @@ -131,7 +131,7 @@
"Found Intel x86 Atom System Image, Android API 2, revision 1\n" +
"Found ARM EABI v7a System Image, Android API 2, revision 2\n" +
"Found ARM EABI System Image, Android API 42, revision 12\n" +
                "Found MIPS System Image, Android API 42, revision 12\n",
monitor.getCapturedVerboseLog());
assertEquals("", monitor.getCapturedLog());
assertEquals("", monitor.getCapturedErrorLog());







