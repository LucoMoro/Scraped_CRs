/*Use nine patch capability to drive tvdpi masking

Change-Id:Ibe9ca90540113157d4fdb5554965d626c22581d8*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ComplementingConfiguration.java
//Synthetic comment -- index 151301a..d7b4a5b 100644

//Synthetic comment -- @@ -17,7 +17,7 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.RenderPreviewManager;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.resources.Density;
import com.android.resources.NightMode;
//Synthetic comment -- @@ -284,11 +284,11 @@
to = biggest + 0.1;
}

for (Device d : devices) {
double size = getScreenSize(d);
if (size >= from && size < to) {
                    if (RenderPreviewManager.HIDE_TVDPI &&
                            getDensity(d) == Density.TV) {
continue;
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/Configuration.java
//Synthetic comment -- index 3476964..c0b57ac 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.api.Rect;
import com.android.ide.common.resources.ResourceRepository;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.ide.common.resources.configuration.DeviceConfigHelper;
//Synthetic comment -- @@ -36,6 +37,7 @@
import com.android.ide.common.resources.configuration.UiModeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
//Synthetic comment -- @@ -1063,6 +1065,22 @@
return null;
}

@Override
public String toString() {
return Objects.toStringHelper(this.getClass())








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderPreviewManager.java
//Synthetic comment -- index a7c3e88..821abac 100644

//Synthetic comment -- @@ -74,9 +74,6 @@
* managing the image buffer cache, etc
*/
public class RenderPreviewManager {
    /** TODO: Tie this to a {@link Capability} instead */
    public static boolean HIDE_TVDPI = true;

private static double sScale = 1.0;
private static final int RENDER_DELAY = 150;
private static final int PREVIEW_VGAP = 18;
//Synthetic comment -- @@ -898,6 +895,7 @@
ConfigurationChooser chooser = getChooser();
List<Device> devices = chooser.getDeviceList();
Configuration configuration = chooser.getConfiguration();

// Rearrange the devices a bit such that the most interesting devices bubble
// to the front
//Synthetic comment -- @@ -943,7 +941,7 @@
interesting = false;
}

                    if (HIDE_TVDPI && d == Density.TV) {
interesting = false;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/RenderService.java
//Synthetic comment -- index 622a4d4..2a1f3b7 100644

//Synthetic comment -- @@ -17,10 +17,12 @@

import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;

import com.android.ide.common.api.IClientRulesEngine;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.Rect;
import com.android.ide.common.rendering.LayoutLibrary;
import com.android.ide.common.rendering.api.DrawableParams;
import com.android.ide.common.rendering.api.IImageFactory;
import com.android.ide.common.rendering.api.ILayoutPullParser;
//Synthetic comment -- @@ -49,8 +51,13 @@
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
import com.android.resources.Density;
import com.android.sdklib.devices.ButtonType;

import org.eclipse.core.resources.IProject;
import org.xmlpull.v1.XmlPullParser;
//Synthetic comment -- @@ -122,8 +129,7 @@
mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
mMinSdkVersion = editor.getMinSdkVersion();
mTargetSdkVersion = editor.getTargetSdkVersion();
        mSoftwareButtons =
            config.getDevice().getDefaultHardware().getButtonType() == ButtonType.SOFT;
}

private RenderService(GraphicalEditorPart editor, FolderConfiguration configuration,
//Synthetic comment -- @@ -141,8 +147,7 @@
mProjectCallback = editor.getProjectCallback(true /*reset*/, mLayoutLib);
mMinSdkVersion = editor.getMinSdkVersion();
mTargetSdkVersion = editor.getTargetSdkVersion();
        mSoftwareButtons =
            config.getDevice().getDefaultHardware().getButtonType() == ButtonType.SOFT;

// TODO: Look up device etc and offer additional configuration options here?
Density density = Density.MEDIUM;
//Synthetic comment -- @@ -158,6 +163,44 @@
mScreenSize = configuration.getScreenSizeQualifier();
}

/**
* Sets the screen size and density to use for rendering
*







