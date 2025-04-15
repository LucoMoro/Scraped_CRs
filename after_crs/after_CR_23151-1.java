/*Fix flaky SdkRepository test.

It seems like class.getResourceAsStream throws a
NullPointerException on the test machines whereas
it returns null (as documented) other times when
the resource is not found.

This patch just refactor 2 duplicated getXsdStream
methods in the base RepoConstants and adjusts to
take both behaviors into account.

Change-Id:Ieefd92a006efda30828b8043e39f6b7e904bc53f*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/RepoConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/RepoConstants.java
//Synthetic comment -- index bd46779..d04f476 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.repository;

import java.io.InputStream;



/**
//Synthetic comment -- @@ -107,5 +109,42 @@
*/
public static final String FD_TEMP = "temp";     //$NON-NLS-1$

    /**
     * Returns a stream to the requested XML Schema.
     * This is an internal helper. Users of the library should call
     * {@link SdkRepoConstants#getXsdStream(String, int)} or
     * {@link SdkAddonConstants#getXsdStream(String, int)}.
     *
     * @param version An integer >= 1.
     * @return An {@link InputStream} object for the local XSD file or
     *         null if there is no schema for the requested version.
     * @see SdkRepoConstants#getXsdStream(int)
     * @see SdkAddonConstants#getXsdStream(int)
     */
    protected static InputStream getXsdStream(String rootElement, int version) {
        String filename = String.format("%1$s-%2$d.xsd", rootElement, version);      //$NON-NLS-1$

        InputStream stream = null;
        try {
            stream = RepoConstants.class.getResourceAsStream(filename);
        } catch (Exception e) {
            // Some implementations seem to return null on failure,
            // others throw an exception. We want to return null.
        }
        if (stream == null) {
            // Try the alternate schemas that are not published yet.
            // This allows us to internally test with new schemas before the
            // public repository uses it.
            filename = String.format("-%1$s-%2$d.xsd", rootElement, version);      //$NON-NLS-1$
            try {
                stream = RepoConstants.class.getResourceAsStream(filename);
            } catch (Exception e) {
                // Some implementations seem to return null on failure,
                // others throw an exception. We want to return null.
            }
        }

        return stream;
    }

}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkAddonConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkAddonConstants.java
//Synthetic comment -- index bd5577e..f75c046 100755

//Synthetic comment -- @@ -63,23 +63,14 @@
};

/**
     * Returns a stream to the requested {@code sdk-addon} XML Schema.
*
* @param version Between 1 and {@link #NS_LATEST_VERSION}, included.
* @return An {@link InputStream} object for the local XSD file or
*         null if there is no schema for the requested version.
*/
public static InputStream getXsdStream(int version) {
        return getXsdStream(NODE_SDK_ADDON, version);
}

/**








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepoConstants.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/repository/SdkRepoConstants.java
//Synthetic comment -- index b6e9097..7053e71 100755

//Synthetic comment -- @@ -86,23 +86,14 @@
};

/**
     * Returns a stream to the requested {@code sdk-repository} XML Schema.
*
* @param version Between 1 and {@link #NS_LATEST_VERSION}, included.
* @return An {@link InputStream} object for the local XSD file or
*         null if there is no schema for the requested version.
*/
public static InputStream getXsdStream(int version) {
        return getXsdStream(NODE_SDK_REPOSITORY, version);
}

/**







