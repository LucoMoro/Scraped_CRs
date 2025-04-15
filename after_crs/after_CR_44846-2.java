/*New API in layoutlib_api.

- Capability for fixed 9-patch scaling.
- software button param for rendering.

Change-Id:I2616dbd97dc413c2c5b5d52af6309967400d2456*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index ccf4068..622a4d4 100644

//Synthetic comment -- @@ -50,6 +50,7 @@
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.resources.Density;
import com.android.sdklib.devices.ButtonType;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
//Synthetic comment -- @@ -81,6 +82,7 @@
private final ResourceResolver mResourceResolver;
private final int mMinSdkVersion;
private final int mTargetSdkVersion;
    private final boolean mSoftwareButtons;
private final LayoutLibrary mLayoutLib;
private final IImageFactory mImageFactory;
private final Density mDensity;
//Synthetic comment -- @@ -120,6 +122,8 @@
mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
mMinSdkVersion = editor.getMinSdkVersion();
mTargetSdkVersion = editor.getTargetSdkVersion();
        mSoftwareButtons =
            config.getDevice().getDefaultHardware().getButtonType() == ButtonType.SOFT;
}

private RenderService(GraphicalEditorPart editor, FolderConfiguration configuration,
//Synthetic comment -- @@ -137,6 +141,8 @@
mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
mMinSdkVersion = editor.getMinSdkVersion();
mTargetSdkVersion = editor.getTargetSdkVersion();
        mSoftwareButtons =
            config.getDevice().getDefaultHardware().getButtonType() == ButtonType.SOFT;

// TODO: Look up device etc and offer additional configuration options here?
Density density = Density.MEDIUM;
//Synthetic comment -- @@ -398,6 +404,7 @@
mProjectCallback,
mMinSdkVersion,
mTargetSdkVersion,
                mSoftwareButtons,
mLogger);

// Request margin and baseline information.
//Synthetic comment -- @@ -533,6 +540,7 @@
mProjectCallback,
mMinSdkVersion,
mTargetSdkVersion,
                mSoftwareButtons,
mLogger);
params.setLayoutOnly();
params.setForceNoDecor();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/tests/functests/layoutRendering/ApiDemosRenderingTest.java
//Synthetic comment -- index 30f23de..7a6eef4 100644

//Synthetic comment -- @@ -263,6 +263,7 @@
projectCallBack,
1, // minSdkVersion
1, // targetSdkVersion
                    false, // softwareButtons
null //logger
));









//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index 06a01d5..a19b8d5 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
*/
public abstract class Bridge {

    public final static int API_CURRENT = 9;

/**
* Returns the API level of the layout library.








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java
//Synthetic comment -- index a7ab7ae..5ad438d 100644

//Synthetic comment -- @@ -61,5 +61,9 @@
*/
FULL_ANIMATED_VIEW_MANIPULATION,
ADAPTER_BINDING,
    EXTENDED_VIEWINFO,
    /**
     * Ability to properly resize nine-patch assets.
     */
    FIXED_SCALABLE_NINE_PATCH;
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index a620b05..e3edbd2 100644

//Synthetic comment -- @@ -52,6 +52,7 @@

private final ILayoutPullParser mLayoutDescription;
private final RenderingMode mRenderingMode;
    private final boolean mSoftwareButtons;
private boolean mLayoutOnly = false;
private Map<ResourceReference, AdapterBinding> mAdapterBindingMap;
private boolean mExtendedViewInfoMode = false;
//Synthetic comment -- @@ -72,6 +73,7 @@
* the project.
* @param minSdkVersion the minSdkVersion of the project
* @param targetSdkVersion the targetSdkVersion of the project
     * @param softwareButtons whether the device use software buttons
* @param log the object responsible for displaying warning/errors to the user.
*/
public SessionParams(
//Synthetic comment -- @@ -83,18 +85,21 @@
RenderResources renderResources,
IProjectCallback projectCallback,
int minSdkVersion, int targetSdkVersion,
            boolean softwareButtons,
LayoutLog log) {
super(projectKey, screenWidth, screenHeight, density, xdpi, ydpi,
renderResources, projectCallback, minSdkVersion, targetSdkVersion, log);

mLayoutDescription = layoutDescription;
mRenderingMode = renderingMode;
        mSoftwareButtons = softwareButtons;
}

public SessionParams(SessionParams params) {
super(params);
mLayoutDescription = params.mLayoutDescription;
mRenderingMode = params.mRenderingMode;
        mSoftwareButtons = params.mSoftwareButtons;
if (params.mAdapterBindingMap != null) {
mAdapterBindingMap = new HashMap<ResourceReference, AdapterBinding>(
params.mAdapterBindingMap);
//Synthetic comment -- @@ -110,6 +115,10 @@
return mRenderingMode;
}

    public boolean hasSoftwareButtons() {
        return mSoftwareButtons;
    }

public void setLayoutOnly() {
mLayoutOnly = true;
}







