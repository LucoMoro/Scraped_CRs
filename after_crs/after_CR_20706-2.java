/*LayoutLib API update: build properties and multi ViewInfo results.

The build properties are needed to populate android.os.Build

The multi ViewInfo results are needed to access all the top
level children of a merge layout.

Change-Id:I49638ae76aaf9e83dc4a0a73c3e7966d7b0a14a3*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index 1349c23..1d38b99 100755

//Synthetic comment -- @@ -788,7 +788,14 @@
if (image != null) {
BufferedImage cropped;
Rect initialCrop = null;
                        ViewInfo viewInfo = null;

                        List<ViewInfo> viewInfoList = session.getRootViews();

                        if (viewInfoList != null && viewInfoList.size() > 0) {
                            viewInfo = viewInfoList.get(0);
                        }

if (viewInfo != null) {
int x1 = viewInfo.getLeft();
int x2 = viewInfo.getRight();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ViewHierarchy.java
//Synthetic comment -- index ac60d80..738da30 100644

//Synthetic comment -- @@ -140,7 +140,14 @@
mIncludedBounds = null;

if (mIsResultValid && session != null) {
            ViewInfo root = null;

            List<ViewInfo> rootList = session.getRootViews();

            if (rootList != null && rootList.size() > 0) {
                root = rootList.get(0);
            }

if (root == null) {
mLastValidViewInfoRoot = null;
} else {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/sdk/AndroidTargetData.java
//Synthetic comment -- index 6c5db6c..31475ee 100644

//Synthetic comment -- @@ -253,6 +253,7 @@
public synchronized LayoutLibrary getLayoutLibrary() {
if (mLayoutBridgeInit == false && mLayoutLibrary.getStatus() == LoadStatus.LOADED) {
mLayoutLibrary.init(
                    mTarget.getProperties(),
new File(mTarget.getPath(IAndroidTarget.FONTS)),
getEnumValueMap(),
new LayoutLog() {








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index 815ee19..bd72632 100644

//Synthetic comment -- @@ -225,6 +225,7 @@
* Initializes the Layout Library object. This must be called before any other action is taken
* on the instance.
*
     * @param platformProperties The build properties for the platform.
* @param fontLocation the location of the fonts in the SDK target.
* @param enumValueMap map attrName => { map enumFlagName => Integer value }. This is typically
*          read from attrs.xml in the SDK target.
//Synthetic comment -- @@ -233,10 +234,12 @@
*
* @see Bridge#init(String, Map)
*/
    public boolean init(Map<String, String> platformProperties,
            File fontLocation, Map<String,
            Map<String, Integer>> enumValueMap,
LayoutLog log) {
if (mBridge != null) {
            return mBridge.init(platformProperties, fontLocation, enumValueMap, log);
} else if (mLegacyBridge != null) {
return mLegacyBridge.init(fontLocation.getAbsolutePath(), enumValueMap);
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/StaticRenderSession.java b/ide_common/src/com/android/ide/common/rendering/StaticRenderSession.java
//Synthetic comment -- index a33a497..c122c1c 100644

//Synthetic comment -- @@ -21,6 +21,8 @@
import com.android.ide.common.rendering.api.ViewInfo;

import java.awt.image.BufferedImage;
import java.util.Collections;
import java.util.List;

/**
* Static {@link RenderSession} returning a given {@link Result}, {@link ViewInfo} and
//Synthetic comment -- @@ -35,12 +37,12 @@
public class StaticRenderSession extends RenderSession {

private final Result mResult;
    private final List<ViewInfo> mRootViewInfo;
private final BufferedImage mImage;

public StaticRenderSession(Result result, ViewInfo rootViewInfo, BufferedImage image) {
mResult = result;
        mRootViewInfo = Collections.singletonList(rootViewInfo);
mImage = image;
}

//Synthetic comment -- @@ -50,7 +52,7 @@
}

@Override
    public List<ViewInfo> getRootViews() {
return mRootViewInfo;
}









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index 79741ea..d014b6c 100644

//Synthetic comment -- @@ -50,13 +50,16 @@
/**
* Initializes the Bridge object.
*
     * @param platformProperties The build properties for the platform.
* @param fontLocation the location of the fonts.
* @param enumValueMap map attrName => { map enumFlagName => Integer value }. This is typically
*          read from attrs.xml in the SDK target.
* @param log a {@link LayoutLog} object. Can be null.
* @return true if success.
*/
    public boolean init(Map<String, String> platformProperties,
            File fontLocation,
            Map<String, Map<String, Integer>> enumValueMap,
LayoutLog log) {
return false;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderSession.java
//Synthetic comment -- index 4db766d..0adf6f5 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.common.rendering.api.Result.Status;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Map;

/**
//Synthetic comment -- @@ -42,16 +43,20 @@
}

/**
     * Returns the {@link ViewInfo} objects for the top level views.
     * <p/>
     * In most case the list will only contain one item. If the top level node is {@code merge}
     * though then it will contain all the items under the {@code merge} tag.
     * <p/>
* This is reset to a new instance every time {@link #render()} is called and can be
* <code>null</code> if the call failed (and the method returned a {@link Result} with
* {@link Status#ERROR_UNKNOWN} or {@link Status#NOT_IMPLEMENTED}.
* <p/>
* This can be safely modified by the caller.
     *
     * @return the list of {@link ViewInfo} or null if there aren't any.
*/
    public List<ViewInfo> getRootViews() {
return null;
}








