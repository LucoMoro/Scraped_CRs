/*Add optional data to LayoutLog API.

Change-Id:Iaa82c3647996a9ce7d7d348cdc19dce34b941238*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index ee4f59a..e1c6584 100644

//Synthetic comment -- @@ -321,7 +321,7 @@
mLogger.warning("wrongconstructor", //$NON-NLS-1$
String.format("Custom view %1$s is not using the 2- or 3-argument "
+ "View constructors; XML attributes will not work",
                                    clazz.getSimpleName()), null /*data*/);
}
break;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index e432a5a..9090baf 100644

//Synthetic comment -- @@ -1483,9 +1483,10 @@
// An error was generated. Print it (and any other accumulated warnings)
String errorMessage = session.getResult().getErrorMessage();
if (errorMessage != null && errorMessage.length() > 0) {
                logger.error(null, session.getResult().getErrorMessage(), null /*data*/);
} else if (!logger.hasProblems()) {
                logger.error(null, "Unexpected error in rendering, no details given",
                        null /*data*/);
}
displayLoggerProblems(iProject, logger);
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
//Synthetic comment -- index 040796e..e44c81b 100644

//Synthetic comment -- @@ -89,7 +89,7 @@
// ---- extends LayoutLog ----

@Override
    public void error(String tag, String message, Object data) {
String description = describe(message);
AdtPlugin.log(IStatus.ERROR, "%1$s: %2$s", mName, description);

//Synthetic comment -- @@ -97,7 +97,7 @@
}

@Override
    public void error(String tag, String message, Throwable throwable, Object data) {
String description = describe(message);
AdtPlugin.log(throwable, "%1$s: %2$s", mName, description);
if (throwable != null) {
//Synthetic comment -- @@ -108,14 +108,14 @@
}

@Override
    public void warning(String tag, String message, Object data) {
String description = describe(message);
AdtPlugin.log(IStatus.WARNING, "%1$s: %2$s", mName, description);
addWarning(tag, description);
}

@Override
    public void fidelityWarning(String tag, String message, Throwable throwable, Object data) {
String description = describe(message);
AdtPlugin.log(throwable, "%1$s: %2$s", mName, description);
if (throwable != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 216502c..6c5db6c 100644

//Synthetic comment -- @@ -258,17 +258,18 @@
new LayoutLog() {

@Override
                        public void error(String tag, String message, Throwable throwable,
                                Object data) {
AdtPlugin.log(throwable, message);
}

@Override
                        public void error(String tag, String message, Object data) {
AdtPlugin.log(IStatus.ERROR, message);
}

@Override
                        public void warning(String tag, String message, Object data) {
AdtPlugin.log(IStatus.WARNING, message);
}
});








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java b/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java
//Synthetic comment -- index 3a0ab06..5ce8974 100644

//Synthetic comment -- @@ -16,42 +16,142 @@

package com.android.ide.common.rendering.api;

/**
 * Log class for actions executed through {@link Bridge} and {@link RenderSession}.
 */
public class LayoutLog {
    /**
     * Prefix for resource warnings/errors. This is not meant to be used as-is by the Layout
     * Library, but is there to help test against a wider type of warning/error.
     * <p/>
     * {@code tag.startsWith(LayoutLog.TAG_RESOURCE_PREFIX} will test if the tag is any type
     * of resource warning/error
     */
public final static String TAG_RESOURCES_PREFIX = "resources.";

    /**
     * Prefix for matrix warnings/errors. This is not meant to be used as-is by the Layout
     * Library, but is there to help test against a wider type of warning/error.
     * <p/>
     * {@code tag.startsWith(LayoutLog.TAG_MATRIX_PREFIX} will test if the tag is any type
     * of matrix warning/error
     */
public final static String TAG_MATRIX_PREFIX = "matrix.";

    /**
     * Tag for unsupported feature that can have a big impact on the rendering. For instance, aild
     * access.
     */
public final static String TAG_UNSUPPORTED = "unsupported";

    /**
     * Tag for error when something really unexpected happens.
     */
public final static String TAG_BROKEN = "broken";

    /**
     * Tag for resource resolution failure.
     * In this case the warning/error data object will be a ResourceValue containing the type
     * and name of the resource that failed to resolve
     */
public final static String TAG_RESOURCES_RESOLVE = TAG_RESOURCES_PREFIX + "resolve";

    /**
     * Tag for failure when reading the content of a resource file.
     */
public final static String TAG_RESOURCES_READ = TAG_RESOURCES_PREFIX + "read";

    /**
     * Tag for wrong format in a resource value.
     */
public final static String TAG_RESOURCES_FORMAT = TAG_RESOURCES_PREFIX + "format";

    /**
     * Fidelity Tag used when a non affine transformation matrix is used in a Java API.
     */
public final static String TAG_MATRIX_AFFINE = TAG_MATRIX_PREFIX + "affine";

    /**
     * Tag used when a matrix cannot be inverted.
     */
public final static String TAG_MATRIX_INVERSE = TAG_MATRIX_PREFIX + "inverse";

    /**
     * Fidelity Tag used when a mask filter type is used but is not supported.
     */
public final static String TAG_MASKFILTER = "maskfilter";

    /**
     * Fidelity Tag used when a draw filter type is used but is not supported.
     */
public final static String TAG_DRAWFILTER = "drawfilter";

    /**
     * Fidelity Tag used when a path effect type is used but is not supported.
     */
public final static String TAG_PATHEFFECT = "patheffect";

    /**
     * Fidelity Tag used when a color filter type is used but is not supported.
     */
public final static String TAG_COLORFILTER = "colorfilter";

    /**
     * Fidelity Tag used when a rasterize type is used but is not supported.
     */
public final static String TAG_RASTERIZER = "rasterizer";

    /**
     * Fidelity Tag used when a shader type is used but is not supported.
     */
public final static String TAG_SHADER = "shader";

    /**
     * Fidelity Tag used when a xfermode type is used but is not supported.
     */
public final static String TAG_XFERMODE = "xfermode";

    /**
     * Logs a warning.
     * @param tag an tag describing the type of the warning
     * @param message the message of the warning
     * @param data an optional data bundle that the client can use to improve the warning display.
     */
    public void warning(String tag, String message, Object data) {
}

/**
     * Logs a fidelity warning.
     *
     * This type of warning indicates that the render will not be
     * the same as the rendering on a device due to limitation of the Java rendering API.
     *
     * @param tag an tag describing the type of the warning
     * @param message the message of the warning
     * @param throwable an optional Throwable that triggered the warning
     * @param data an optional data bundle that the client can use to improve the warning display.
*/
    public void fidelityWarning(String tag, String message, Throwable throwable, Object data) {
}

    /**
     * Logs an error.
     *
     * @param tag an tag describing the type of the error
     * @param message the message of the error
     * @param data an optional data bundle that the client can use to improve the error display.
     */
    public void error(String tag, String message, Object data) {
    }

    /**
     * Logs an error, and the {@link Throwable} that triggered it.
     *
     * @param tag an tag describing the type of the error
     * @param message the message of the error
     * @param throwable the Throwable that triggered the error
     * @param data an optional data bundle that the client can use to improve the error display.
     */
    public void error(String tag, String message, Throwable throwable, Object data) {
    }
}







