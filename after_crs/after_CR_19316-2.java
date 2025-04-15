/*LayoutLib API: SceneResult API clean up.

Change-Id:I1cb80da55f9ff16d0422eb6c4767448a4691b74b*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java
//Synthetic comment -- index f773a5a..6f4fea1 100644

//Synthetic comment -- @@ -150,7 +150,7 @@
sceneResult = SceneStatus.SUCCESS.getResult();
rootViewInfo = convertToViewInfo(result.getRootView());
} else {
            sceneResult = SceneStatus.ERROR_UNKNOWN.getResult(result.getErrorMessage());
rootViewInfo = null;
}









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java b/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java
//Synthetic comment -- index 20081a1..c009517 100644

//Synthetic comment -- @@ -18,6 +18,12 @@

/**
* Scene result class.
 * <p/>
 * This cannot be allocated directly, instead use
 * {@link SceneStatus#getResult()},
 * {@link SceneStatus#getResult(String, Throwable)},
 * {@link SceneStatus#getResult(String)}
 * {@link SceneStatus#getResult(Object)}
*/
public class SceneResult {

//Synthetic comment -- @@ -26,6 +32,10 @@
private final Throwable mThrowable;
private Object mData;

    /**
     * Scene Status enum.
     * <p/>This indicates the status of all scene actions.
     */
public enum SceneStatus {
SUCCESS,
NOT_IMPLEMENTED,
//Synthetic comment -- @@ -38,6 +48,8 @@
ERROR_ANIM_NOT_FOUND,
ERROR_UNKNOWN;

        private SceneResult mResult;

/**
* Returns a {@link SceneResult} object with this status.
* @return an instance of SceneResult;
//Synthetic comment -- @@ -46,7 +58,47 @@
// don't want to get generic error that way.
assert this != ERROR_UNKNOWN;

            if (mResult == null) {
                mResult = new SceneResult(this);
            }

            return mResult;
        }

        /**
         * Returns a {@link SceneResult} object with this status, and the given data.
         * @return an instance of SceneResult;
         *
         * @see SceneResult#getData()
         */
        public SceneResult getResult(Object data) {
            if (data == null) {
                return getResult();
            }

            // don't want to get generic error that way.
            assert this != ERROR_UNKNOWN;
            SceneResult r = new SceneResult(this);
            return r.setData(data);
        }

        /**
         * Returns a {@link #ERROR_UNKNOWN} result with the given message and throwable
         * @param errorMessage the error message
         * @param throwable the throwable
         * @return an instance of SceneResult.
         */
        public SceneResult getResult(String errorMessage, Throwable throwable) {
            return new SceneResult(this, errorMessage, throwable);
        }

        /**
         * Returns a {@link #ERROR_UNKNOWN} result with the given message
         * @param errorMessage the error message
         * @return an instance of SceneResult.
         */
        public SceneResult getResult(String errorMessage) {
            return new SceneResult(this, errorMessage, null /*throwable*/);
}
}

//Synthetic comment -- @@ -55,30 +107,19 @@
*
* @param status the status. Must not be null.
*/
    private SceneResult(SceneStatus status) {
this(status, null, null);
}

/**
* Creates a {@link SceneResult} object with the given SceneStatus, and the given message
* and {@link Throwable}
*
* @param status the status. Must not be null.
* @param errorMessage an optional error message.
* @param t an optional exception.
*/
    private SceneResult(SceneStatus status, String errorMessage, Throwable t) {
assert status != null;
mStatus = status;
mErrorMessage = errorMessage;
//Synthetic comment -- @@ -121,9 +162,12 @@
/**
* Sets an optional data bundle in the result object.
* @param data the data bundle
     *
     * @return returns <code>this</code>.
*/
    private SceneResult setData(Object data) {
mData = data;
        return this;
}

/**







