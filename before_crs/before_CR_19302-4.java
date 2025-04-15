/*LayoutLib API: move add/moveChild API back to using index.

Also add an optional data bundle to SceneResult.

Change-Id:I9f9d4ca1f1f05d536a87a005a7939a6d42d0d8a4*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 1e51994..a493b56 100755

//Synthetic comment -- @@ -53,8 +53,8 @@
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.SceneParams.RenderingMode;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.widgets.ResolutionChooserDialog;
//Synthetic comment -- @@ -1018,8 +1018,8 @@
// no root view yet indicates success and then update the canvas with it.

mCanvasViewer.getCanvas().setResult(
                        new BasicLayoutScene(SceneResult.SUCCESS, null /*rootViewInfo*/,
                                null /*image*/),
null /*explodeNodes*/);
return;
}
//Synthetic comment -- @@ -1274,7 +1274,7 @@
canvas.setResult(scene, explodeNodes);

// update the UiElementNode with the layout info.
        if (scene.getResult() != SceneResult.SUCCESS) {
// An error was generated. Print it.
displayError(scene.getResult().getErrorMessage());









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteComposite.java
//Synthetic comment -- index fc4d5d1..2bc9f34 100755

//Synthetic comment -- @@ -39,7 +39,6 @@
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
import com.android.layoutlib.api.LayoutBridge;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.ViewInfo;
import com.android.sdklib.SdkConstants;

//Synthetic comment -- @@ -751,7 +750,7 @@
}

if (scene != null) {
                if (scene.getResult() == SceneResult.SUCCESS) {
BufferedImage image = scene.getImage();
if (image != null) {
BufferedImage cropped;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index 686b0ba..2278cfd 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import com.android.ide.eclipse.adt.internal.editors.layout.uimodel.UiViewElementNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.ViewInfo;

import org.eclipse.swt.graphics.Rectangle;
//Synthetic comment -- @@ -130,7 +129,7 @@
}

mScene = scene;
        mIsResultValid = (scene != null && scene.getResult() == SceneResult.SUCCESS);
mExplodedParents = false;

if (mIsResultValid && scene != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 024e9e3..53a36fb 100644

//Synthetic comment -- @@ -37,7 +37,6 @@
import com.android.layoutlib.api.IXmlPullParser;
import com.android.layoutlib.api.LayoutScene;
import com.android.layoutlib.api.SceneParams;
import com.android.layoutlib.api.SceneResult;
import com.android.layoutlib.api.SceneParams.RenderingMode;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
//Synthetic comment -- @@ -217,7 +216,7 @@
null //logger
));

            if (scene.getResult() != SceneResult.SUCCESS) {
if (projectCallBack.mCustomViewAttempt == false) {
System.out.println("FAILED");
fail(String.format("Rendering %1$s: %2$s", layout.getName(),








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java b/ide_common/src/com/android/ide/common/layoutlib/LayoutBridgeWrapper.java
//Synthetic comment -- index e1b8241..f773a5a 100644

//Synthetic comment -- @@ -25,6 +25,7 @@
import com.android.layoutlib.api.ViewInfo;
import com.android.layoutlib.api.ILayoutResult.ILayoutViewInfo;
import com.android.layoutlib.api.SceneParams.RenderingMode;

import java.lang.reflect.Field;
import java.util.ArrayList;
//Synthetic comment -- @@ -146,10 +147,10 @@
ViewInfo rootViewInfo;

if (result.getSuccess() == ILayoutResult.SUCCESS) {
            sceneResult = SceneResult.SUCCESS;
rootViewInfo = convertToViewInfo(result.getRootView());
} else {
            sceneResult = new SceneResult(result.getErrorMessage());
rootViewInfo = null;
}









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java b/layoutlib_api/src/com/android/layoutlib/api/LayoutScene.java
//Synthetic comment -- index 8c3d954..871661e 100644

//Synthetic comment -- @@ -158,8 +158,8 @@
/**
* Inserts a new child in a ViewGroup object, and renders the result.
* <p/>
     * The child is first inflated and then added to its parent, before the given sibling.
     * If the sibling is <code>null</code>, then it is added at the end of the ViewGroup.
* <p/>
* If an animation listener is passed then the rendering is done asynchronously and the
* result is sent to the listener.
//Synthetic comment -- @@ -168,15 +168,18 @@
* The child stays in the view hierarchy after the rendering is done. To remove it call
* {@link #removeChild(Object, int)}.
*
* @param parentView the parent View object to receive the new child.
* @param childXml an {@link IXmlPullParser} containing the content of the new child.
     * @param beforeSibling the object in <var>parentView</var> to insert before. If
     *             <code>null</code>, insert at the end.
* @param listener an optional {@link IAnimationListener}.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult insertChild(Object parentView, IXmlPullParser childXml, Object beforeSibling,
IAnimationListener listener) {
return NOT_IMPLEMENTED.getResult();
}
//Synthetic comment -- @@ -184,9 +187,11 @@
/**
* Move a new child to a different ViewGroup object.
* <p/>
     * The child is first removed from its current parent, and then added to its new parent, before
     * the given sibling. If the sibling is <code>null</code>, then it is added at the end
     * of the ViewGroup.
* <p/>
* If an animation listener is passed then the rendering is done asynchronously and the
* result is sent to the listener.
//Synthetic comment -- @@ -198,13 +203,13 @@
* @param parentView the parent View object to receive the child. Can be the current parent
*             already.
* @param childView the view to move.
     * @param beforeSibling the object in <var>parentView</var> to insert before. If
     *             <code>null</code>, insert at the end.
* @param listener an optional {@link IAnimationListener}.
*
* @return a {@link SceneResult} indicating the status of the action.
*/
    public SceneResult moveChild(Object parentView, Object childView, Object beforeSibling,
IAnimationListener listener) {
return NOT_IMPLEMENTED.getResult();
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java b/layoutlib_api/src/com/android/layoutlib/api/SceneResult.java
//Synthetic comment -- index f72c97f..8fada80 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
private final SceneStatus mStatus;
private final String mErrorMessage;
private final Throwable mThrowable;

public enum SceneStatus {
SUCCESS,
//Synthetic comment -- @@ -45,43 +46,25 @@
// don't want to get generic error that way.
assert this != ERROR_UNKNOWN;

            if (this == SUCCESS) {
                return SceneResult.SUCCESS;
            }

return new SceneResult(this);
}
}

/**
     * Singleton SUCCESS {@link SceneResult} object.
*/
    public static final SceneResult SUCCESS = new SceneResult(SceneStatus.SUCCESS);

    /**
     * Creates a {@link SceneResult} object, with {@link SceneStatus#ERROR_UNKNOWN} status, and
     * the given message.
     */
    public SceneResult(String errorMessage) {
        this(SceneStatus.ERROR_UNKNOWN, errorMessage, null);
    }

    /**
     * Creates a {@link SceneResult} object, with {@link SceneStatus#ERROR_UNKNOWN} status, and
     * the given message and {@link Throwable}
     */
    public SceneResult(String errorMessage, Throwable t) {
        this(SceneStatus.ERROR_UNKNOWN, errorMessage, t);
}

/**
* Creates a {@link SceneResult} object with the given SceneStatus, and the given message
* and {@link Throwable}.
     * <p>
     * This should not be used to create {@link SceneResult} object with
     * {@link SceneStatus#SUCCESS}. Use {@link SceneResult#SUCCESS} instead.
*
     * @param status the status
*/
public SceneResult(SceneStatus status, String errorMessage) {
this(status, errorMessage, null);
//Synthetic comment -- @@ -90,31 +73,26 @@
/**
* Creates a {@link SceneResult} object with the given SceneStatus, and the given message
* and {@link Throwable}
     * <p>
     * This should not be used to create {@link SceneResult} object with
     * {@link SceneStatus#SUCCESS}. Use {@link SceneResult#SUCCESS} instead.
*
     * @param status the status
*/
public SceneResult(SceneStatus status, String errorMessage, Throwable t) {
        assert status != SceneStatus.SUCCESS;
mStatus = status;
mErrorMessage = errorMessage;
mThrowable = t;
}

/**
     * Creates a {@link SceneResult} object with the given SceneStatus.
* <p>
     * This should not be used to create {@link SceneResult} object with
     * {@link SceneStatus#SUCCESS}. Use {@link SceneResult#SUCCESS} instead.
     *
     * @param status the status
*/
    public SceneResult(SceneStatus status) {
        mStatus = status;
        mErrorMessage = null;
        mThrowable = null;
}

/**
//Synthetic comment -- @@ -139,4 +117,20 @@
public Throwable getException() {
return mThrowable;
}
}







