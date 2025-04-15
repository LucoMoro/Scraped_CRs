/*Add cpu.model to AVD running armeabi-v7a system images.

Change-Id:I5b97b115ada432e284ad2dd3c69bcf7009677aec*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkConstants.java
//Synthetic comment -- index d82437f..b86cc18 100644

//Synthetic comment -- @@ -207,9 +207,11 @@
public final static String ABI_ARMEABI = "armeabi";
public final static String ABI_ARMEABI_V7A = "armeabi-v7a";
public final static String ABI_INTEL_ATOM = "x86";
    /** Name of the CPU to support. */
    public final static String CPU_ARM = "arm";
    public final static String CPU_INTEL_ATOM = "x86";

/** Name of the SDK skins folder. */
public final static String FD_SKINS = "skins";








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdInfo.java
//Synthetic comment -- index 33a62d0..0b9a261 100755

//Synthetic comment -- @@ -137,7 +137,7 @@
}

// legacy
        return SdkConstants.CPU_ARM;
}

/** Convenience function to return a more user friendly name of the abi type. */
//Synthetic comment -- @@ -145,11 +145,14 @@
String s = null;
if (raw.equalsIgnoreCase(SdkConstants.ABI_ARMEABI)) {
s = "ARM (" + SdkConstants.ABI_ARMEABI + ")";
        }
        else if (raw.equalsIgnoreCase(SdkConstants.ABI_INTEL_ATOM)) {
s = "Intel Atom (" + SdkConstants.ABI_INTEL_ATOM + ")";
        }
        else {
s = raw + " (" + raw + ")";
}
return s;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 709b7f4..5c6fd05 100644

//Synthetic comment -- @@ -69,13 +69,19 @@
* AVD/config.ini key name representing the abi type of the specific avd
*
*/
     public final static String AVD_INI_ABI_TYPE = "abi.type"; //$NON-NLS-1$

     /**
      * AVD/config.ini key name representing the CPU architecture of the specific avd
      *
      */
      public final static String AVD_INI_CPU_ARCH = "hw.cpu.arch"; //$NON-NLS-1$


/**
//Synthetic comment -- @@ -580,11 +586,13 @@
values.put(AVD_INI_ABI_TYPE, abiType);

// and the cpu arch.
            if (SdkConstants.ABI_ARMEABI.equals(abiType) ||
                    SdkConstants.ABI_ARMEABI_V7A.equals(abiType)) {
                values.put(AVD_INI_CPU_ARCH, SdkConstants.CPU_ARM);
} else if (SdkConstants.ABI_INTEL_ATOM.equals(abiType)) {
                values.put(AVD_INI_CPU_ARCH, SdkConstants.CPU_INTEL_ATOM);
} else {
log.error(null,
"ABI %1$s is not supported by this version of the SDK Tools", abiType);







