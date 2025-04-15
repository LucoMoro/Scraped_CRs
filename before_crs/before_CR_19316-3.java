/*LayoutLib API: SceneResult API clean up.

Change-Id:I1cb80da55f9ff16d0422eb6c4767448a4691b74b*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java
//Synthetic comment -- index f773a5a..6f4fea1 100644

//Synthetic comment -- @@ -150,7 +150,7 @@
sceneResult = SceneStatus.SUCCESS.getResult();
rootViewInfo = convertToViewInfo(result.getRootView());
} else {
            sceneResult = new SceneResult(SceneStatus.ERROR_UNKNOWN, result.getErrorMessage());
rootViewInfo = null;
}









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java b/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java
//Synthetic comment -- index 20081a1..1a46167 100644

//Synthetic comment -- @@ -17,7 +17,13 @@
package com.android.layoutlib.api;

/**
 * Scene result class.
*/
public class SceneResult {

//Synthetic comment -- @@ -26,6 +32,10 @@
private final Throwable mThrowable;
private Object mData;

public enum SceneStatus {
SUCCESS,
NOT_IMPLEMENTED,
//Synthetic comment -- @@ -38,6 +48,8 @@
ERROR_ANIM_NOT_FOUND,
ERROR_UNKNOWN;

/**
* Returns a {@link SceneResult} object with this status.
* @return an instance of SceneResult;
//Synthetic comment -- @@ -46,7 +58,46 @@
// don't want to get generic error that way.
assert this != ERROR_UNKNOWN;

            return new SceneResult(this);
}
}

//Synthetic comment -- @@ -55,36 +106,44 @@
*
* @param status the status. Must not be null.
*/
    public SceneResult(SceneStatus status) {
this(status, null, null);
}

/**
* Creates a {@link SceneResult} object with the given SceneStatus, and the given message
     * and {@link Throwable}.
     *
     * @param status the status. Must not be null.
     * @param errorMessage an optional error message.
     */
    public SceneResult(SceneStatus status, String errorMessage) {
        this(status, errorMessage, null);
    }

    /**
     * Creates a {@link SceneResult} object with the given SceneStatus, and the given message
* and {@link Throwable}
*
* @param status the status. Must not be null.
* @param errorMessage an optional error message.
* @param t an optional exception.
*/
    public SceneResult(SceneStatus status, String errorMessage, Throwable t) {
assert status != null;
mStatus = status;
mErrorMessage = errorMessage;
mThrowable = t;
}

/**
* Returns whether the status is successful.
* <p>
//Synthetic comment -- @@ -119,14 +178,6 @@
}

/**
     * Sets an optional data bundle in the result object.
     * @param data the data bundle
     */
    public void setData(Object data) {
        mData = data;
    }

    /**
* Returns the optional data bundle stored in the result object.
* @return the data bundle or <code>null</code> if none have been set.
*/







