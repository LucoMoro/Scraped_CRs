/*SDK: cleanup all the Integer.parse(getProperty) calls.

Change-Id:Iee113eb3341f6aab65d65654505c5fedcfaff9a1*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ExtraPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ExtraPackage.java
//Synthetic comment -- index b728da9..32702b2 100755

//Synthetic comment -- @@ -253,10 +253,8 @@

mOldPaths = getProperty(props, PkgProps.EXTRA_OLD_PATHS, null);

        mMinApiLevel = getPropertyInt(props, PkgProps.EXTRA_MIN_API_LEVEL,
                MIN_API_LEVEL_NOT_SPECIFIED);

String projectFiles = getProperty(props, PkgProps.EXTRA_PROJECT_FILES, null);
ArrayList<String> filePaths = new ArrayList<String>();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/FullRevisionPackage.java
//Synthetic comment -- index 49674ce..5531b0c 100755

//Synthetic comment -- @@ -104,22 +104,10 @@
super(source, props, revision, license, description, descUrl,
archiveOs, archiveArch, archiveOsPath);

        int major = getPropertyInt(props, PkgProps.PKG_MAJOR_REV,revision);
        int minor = getPropertyInt(props, PkgProps.PKG_MINOR_REV, FullRevision.IMPLICIT_MINOR_REV);
        int micro = getPropertyInt(props, PkgProps.PKG_MICRO_REV, FullRevision.IMPLICIT_MINOR_REV);
        int preview = getPropertyInt(props, PkgProps.PKG_PREVIEW_REV, FullRevision.NOT_A_PREVIEW);

mPreviewVersion = new FullRevision(major, minor, micro, preview);
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/LayoutlibVersionMixin.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/LayoutlibVersionMixin.java
//Synthetic comment -- index 82512b9..7cb68e1 100755

//Synthetic comment -- @@ -64,12 +64,10 @@
* Parses the layoutlib version optionally available in the given {@link Properties}.
*/
public LayoutlibVersionMixin(Properties props) {
        int layoutlibApi = Package.getPropertyInt(props, PkgProps.LAYOUTLIB_API,
                                                         LAYOUTLIB_API_NOT_SPECIFIED);
        int layoutlibRev = Package.getPropertyInt(props, PkgProps.LAYOUTLIB_REV,
                                                         LAYOUTLIB_REV_NOT_SPECIFIED);
mLayoutlibVersion = Pair.of(layoutlibApi, layoutlibRev);
}









//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevisionPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MajorRevisionPackage.java
//Synthetic comment -- index cbd6706..7e61e5f 100755

//Synthetic comment -- @@ -77,8 +77,7 @@
super(source, props, revision, license, description, descUrl,
archiveOs, archiveArch, archiveOsPath);

        mRevision = new MajorRevision(getPropertyInt(props, PkgProps.PKG_MAJOR_REV, revision));
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MinToolsPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/MinToolsPackage.java
//Synthetic comment -- index 05ac96a..3de46b4 100755

//Synthetic comment -- @@ -78,10 +78,8 @@
super(source, props, revision, license, description, descUrl,
archiveOs, archiveArch, archiveOsPath);

        mMinToolsRevision = getPropertyInt(props, PkgProps.MIN_TOOLS_REV,
                                                  MIN_TOOLS_REV_NOT_SPECIFIED);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/Package.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/Package.java
//Synthetic comment -- index 3ad18d5..b36a111 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.internal.repository.packages;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.annotations.VisibleForTesting;
import com.android.annotations.VisibleForTesting.Visibility;
import com.android.sdklib.AndroidVersion;
//Synthetic comment -- @@ -205,7 +207,11 @@
* @return The string value of the given key in the properties, or null if the key
*   isn't found or if {@code props} is null.
*/
    @Nullable
    static String getProperty(
            @Nullable Properties props,
            @NonNull String propKey,
            @Nullable String defaultValue) {
if (props == null) {
return defaultValue;
}
//Synthetic comment -- @@ -213,6 +219,31 @@
}

/**
     * Utility method that returns an integer property from a {@link Properties} object.
     * Returns the default value if props is null or if the property is not defined or
     * cannot be parsed to an integer.
     *
     * @param props The {@link Properties} to search into.
     *   If null, the default value is returned.
     * @param propKey The name of the property. Must not be null.
     * @param defaultValue The default value to return if {@code props} is null or if the
     *   key is not found. Can be null.
     * @return The integer value of the given key in the properties, or the {@code defaultValue}.
     */
    static int getPropertyInt(
            @Nullable Properties props,
            @NonNull String propKey,
            int defaultValue) {
        String s = props != null ? props.getProperty(propKey, null) : null;
        if (s != null) {
            try {
                return Integer.parseInt(s);
            } catch (Exception ignore) {}
        }
        return defaultValue;
    }

    /**
* Save the properties of the current packages in the given {@link Properties} object.
* These properties will later be give the constructor that takes a {@link Properties} object.
*/








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SamplePackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/SamplePackage.java
//Synthetic comment -- index f615d34..9c9592e 100755

//Synthetic comment -- @@ -118,10 +118,8 @@

mVersion = target.getVersion();

        mMinApiLevel = getPropertyInt(props, PkgProps.SAMPLE_MIN_API_LEVEL,
                                             MIN_API_LEVEL_NOT_SPECIFIED);
}

/**
//Synthetic comment -- @@ -155,10 +153,8 @@

mVersion = new AndroidVersion(props);

        mMinApiLevel = getPropertyInt(props, PkgProps.SAMPLE_MIN_API_LEVEL,
                                             MIN_API_LEVEL_NOT_SPECIFIED);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ToolPackage.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/packages/ToolPackage.java
//Synthetic comment -- index 7ccd617..3fc7405 100755

//Synthetic comment -- @@ -144,11 +144,8 @@
archiveArch,
archiveOsPath);

        mMinPlatformToolsRevision = getPropertyInt(props, PkgProps.MIN_PLATFORM_TOOLS_REV,
                                                          MIN_PLATFORM_TOOLS_REV_INVALID);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/packages/PackageTest.java
//Synthetic comment -- index 4e8ee2e..6023b5a 100755

//Synthetic comment -- @@ -197,4 +197,20 @@
"Vendor Path, revision 5]",
Arrays.toString(list.toArray()));
}

    public void testGetPropertyInt() {
        Properties p = new Properties();

        assertEquals(42, Package.getPropertyInt(p, "key", 42));

        p.setProperty("key", "");
        assertEquals(43, Package.getPropertyInt(p, "key", 43));

        p.setProperty("key", "I am not a number");
        assertEquals(44, Package.getPropertyInt(p, "key", 44));

        p.setProperty("key", "6");
        assertEquals(6, Package.getPropertyInt(p, "key", 45));
    }

}







