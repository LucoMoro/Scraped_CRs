/*Fix NPE in SdkManager: parsed props can be null.

Change-Id:Ia1b873fc48e87173148572b123d3c0346542f4d9*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/AvdManager.java
//Synthetic comment -- index 116fa4a..fefe891 100644

//Synthetic comment -- @@ -1402,7 +1402,7 @@
}

// get abi type
        String abiType = properties == null ? null : properties.get(AVD_INI_ABI_TYPE);
// for the avds created previously without enhancement, i.e. They are created based
// on previous API Levels. They are supposed to have ARM processor type
if (abiType == null) {







