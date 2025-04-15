/*Add optional data to LayoutLog API.

Change-Id:Iaa82c3647996a9ce7d7d348cdc19dce34b941238*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index ee4f59a..e1c6584 100644

//Synthetic comment -- @@ -321,7 +321,7 @@
mLogger.warning("wrongconstructor", //$NON-NLS-1$
String.format("Custom view %1$s is not using the 2- or 3-argument "
+ "View constructors; XML attributes will not work",
                                    clazz.getSimpleName()));
}
break;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index e432a5a..9090baf 100644

//Synthetic comment -- @@ -1483,9 +1483,10 @@
// An error was generated. Print it (and any other accumulated warnings)
String errorMessage = session.getResult().getErrorMessage();
if (errorMessage != null && errorMessage.length() > 0) {
                logger.error(null, session.getResult().getErrorMessage());
} else if (!logger.hasProblems()) {
                logger.error(null, "Unexpected error in rendering, no details given");
}
displayLoggerProblems(iProject, logger);
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderLogger.java
//Synthetic comment -- index 040796e..e44c81b 100644

//Synthetic comment -- @@ -89,7 +89,7 @@
// ---- extends LayoutLog ----

@Override
    public void error(String tag, String message) {
String description = describe(message);
AdtPlugin.log(IStatus.ERROR, "%1$s: %2$s", mName, description);

//Synthetic comment -- @@ -97,7 +97,7 @@
}

@Override
    public void error(String tag, String message, Throwable throwable) {
String description = describe(message);
AdtPlugin.log(throwable, "%1$s: %2$s", mName, description);
if (throwable != null) {
//Synthetic comment -- @@ -108,14 +108,14 @@
}

@Override
    public void warning(String tag, String message) {
String description = describe(message);
AdtPlugin.log(IStatus.WARNING, "%1$s: %2$s", mName, description);
addWarning(tag, description);
}

@Override
    public void fidelityWarning(String tag, String message, Throwable throwable) {
String description = describe(message);
AdtPlugin.log(throwable, "%1$s: %2$s", mName, description);
if (throwable != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 216502c..6c5db6c 100644

//Synthetic comment -- @@ -258,17 +258,18 @@
new LayoutLog() {

@Override
                        public void error(String tag, String message, Throwable throwable) {
AdtPlugin.log(throwable, message);
}

@Override
                        public void error(String tag, String message) {
AdtPlugin.log(IStatus.ERROR, message);
}

@Override
                        public void warning(String tag, String message) {
AdtPlugin.log(IStatus.WARNING, message);
}
});








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java b/layoutlib_api/src/com/android/ide/common/rendering/api/LayoutLog.java
//Synthetic comment -- index 3a0ab06..5ce8974 100644

//Synthetic comment -- @@ -16,42 +16,142 @@

package com.android.ide.common.rendering.api;

public class LayoutLog {
public final static String TAG_RESOURCES_PREFIX = "resources.";
public final static String TAG_MATRIX_PREFIX = "matrix.";

public final static String TAG_UNSUPPORTED = "unsupported";
public final static String TAG_BROKEN = "broken";
public final static String TAG_RESOURCES_RESOLVE = TAG_RESOURCES_PREFIX + "resolve";
public final static String TAG_RESOURCES_READ = TAG_RESOURCES_PREFIX + "read";
public final static String TAG_RESOURCES_FORMAT = TAG_RESOURCES_PREFIX + "format";
public final static String TAG_MATRIX_AFFINE = TAG_MATRIX_PREFIX + "affine";
public final static String TAG_MATRIX_INVERSE = TAG_MATRIX_PREFIX + "inverse";
public final static String TAG_MASKFILTER = "maskfilter";
public final static String TAG_DRAWFILTER = "drawfilter";
public final static String TAG_PATHEFFECT = "patheffect";
public final static String TAG_COLORFILTER = "colorfilter";
public final static String TAG_RASTERIZER = "rasterizer";
public final static String TAG_SHADER = "shader";
public final static String TAG_XFERMODE = "xfermode";


    public void warning(String tag, String message) {
    }

    public void fidelityWarning(String tag, String message, Throwable throwable) {
    }

    public void error(String tag, String message) {
}

/**
     * Logs an error message and a {@link Throwable}.
     * @param message the message to log.
     * @param throwable the {@link Throwable} to log.
*/
    public void error(String tag, String message, Throwable throwable) {

}

}







