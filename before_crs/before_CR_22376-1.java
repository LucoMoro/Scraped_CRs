/*Cache resource resolver and manifest info

This changeset adds caching of the ResourceResolver in the layout
editor such that it can be reused for successive rendering requests as
long as the configuration does not change.

It also adds caching of the manifest icon and label; these are
invalidated whenever the manifest file is updated and saved.

Change-Id:Idbf77fd96fa60a4ac24e06bbecae882829a8b812*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 05a1264..f7b3e61 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout.gle2;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_STRING_PREFIX;
import static com.android.ide.common.layout.LayoutConstants.SCROLL_VIEW;
import static com.android.ide.common.layout.LayoutConstants.STRING_PREFIX;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
//Synthetic comment -- @@ -55,6 +56,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.configuration.ConfigurationComposite.IConfigListener;
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.IncludeFinder.Reference;
import com.android.ide.eclipse.adt.internal.editors.layout.gre.RulesEngine;
import com.android.ide.eclipse.adt.internal.editors.ui.DecorComposite;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiDocumentNode;
import com.android.ide.eclipse.adt.internal.editors.uimodel.UiElementNode;
//Synthetic comment -- @@ -236,6 +238,7 @@
private TargetListener mTargetListener;

private ConfigListener mConfigListener;

private ReloadListener mReloadListener;

//Synthetic comment -- @@ -436,6 +439,7 @@
*/
public void onConfigurationChange() {
mConfiguredFrameworkRes = mConfiguredProjectRes = null;

if (mEditedFile == null || mConfigComposite.getEditedConfig() == null) {
return;
//Synthetic comment -- @@ -511,6 +515,7 @@
public void onThemeChange() {
// Store the state in the current file
mConfigComposite.storeState();

recomputeLayout();

//Synthetic comment -- @@ -778,6 +783,7 @@

// because the target changed we must reset the configured resources.
mConfiguredFrameworkRes = mConfiguredProjectRes = null;

// make sure we remove the custom view loader, since its parent class loader is the
// bridge class loader.
//Synthetic comment -- @@ -796,6 +802,7 @@
/** Refresh the configured project resources associated with this editor */
public void refreshProjectResources() {
mConfiguredProjectRes = null;
mConfigListener.getConfiguredProjectResources();
}

//Synthetic comment -- @@ -1381,59 +1388,33 @@
model.refreshUi();
}

    private RenderSession renderWithBridge(IProject iProject, UiDocumentNode model,
LayoutLibrary layoutLib, int width, int height, Set<UiElementNode> explodeNodes,
Integer overrideBgColor, boolean noDecor, LayoutLog logger, Reference includeWithin,
RenderingMode renderingMode) {
ResourceManager resManager = ResourceManager.getInstance();

        ProjectResources projectRes = resManager.getProjectResources(iProject);
if (projectRes == null) {
displayError("Missing project resources.");
return null;
}

        // Get the resources of the file's project.
        Map<ResourceType, Map<String, ResourceValue>> configuredProjectRes =
            mConfigListener.getConfiguredProjectResources();

        // Get the framework resources
        Map<ResourceType, Map<String, ResourceValue>> frameworkResources =
            mConfigListener.getConfiguredFrameworkResources();

        // Abort the rendering if the resources are not found.
        if (configuredProjectRes == null) {
            displayError("Missing project resources for current configuration.");
            return null;
        }

        if (frameworkResources == null) {
            displayError("Missing framework resources.");
            return null;
        }

// Lazily create the project callback the first time we need it
if (mProjectCallback == null) {
            mProjectCallback = new ProjectCallback(layoutLib, projectRes, iProject);
} else {
// Also clears the set of missing/broken classes prior to rendering
mProjectCallback.getMissingClasses().clear();
mProjectCallback.getUninstantiatableClasses().clear();
}

        // get the selected theme
        String theme = mConfigComposite.getTheme();
        if (theme == null) {
            displayError("Missing theme.");
            return null;
        }

if (mUseExplodeMode) {
// compute how many padding in x and y will bump the screen size
List<UiElementNode> children = model.getUiChildren();
if (children.size() == 1) {
ExplodedRenderingHelper helper = new ExplodedRenderingHelper(
                        children.get(0).getXmlNode(), iProject);

// there are 2 paddings for each view
// left and right, or top and bottom.
//Synthetic comment -- @@ -1447,10 +1428,9 @@
Density density = mConfigComposite.getDensity();
float xdpi = mConfigComposite.getXDpi();
float ydpi = mConfigComposite.getYDpi();
        boolean isProjectTheme = mConfigComposite.isProjectTheme();

ILayoutPullParser modelParser = new UiElementPullParser(model,
                mUseExplodeMode, explodeNodes, density, xdpi, iProject);
ILayoutPullParser topParser = modelParser;

// Code to support editing included layout
//Synthetic comment -- @@ -1460,9 +1440,8 @@
String contextLayoutName = includeWithin.getName();

// Find the layout file.
            Map<String, ResourceValue> layouts = configuredProjectRes.get(
                    ResourceType.LAYOUT);
            ResourceValue contextLayout = layouts.get(contextLayoutName);
if (contextLayout != null) {
File layoutFile = new File(contextLayout.getValue());
if (layoutFile.isFile()) {
//Synthetic comment -- @@ -1482,15 +1461,16 @@
}
}

        // FIXME: make resource resolver persistent, and only update it when something changes.
        ResourceResolver resolver = ResourceResolver.create(
                configuredProjectRes, frameworkResources,
                theme, isProjectTheme);

SessionParams params = new SessionParams(
topParser,
renderingMode,
                iProject /* projectKey */,
width, height,
density, xdpi, ydpi,
resolver,
//Synthetic comment -- @@ -1500,19 +1480,11 @@
logger);
if (noDecor) {
params.setForceNoDecor();
        }

        // FIXME make persistent and only reload when the manifest (or at least resources) changes.
        IFolderWrapper projectFolder = new IFolderWrapper(getProject());
        IAbstractFile manifest = AndroidManifest.getManifest(projectFolder);
        if (manifest != null) {
try {
                params.setAppIcon(AndroidManifest.getApplicationIcon(manifest));
            } catch (Exception e) {
                // ignore.
            }
            try {
                params.setAppLabel(AndroidManifest.getApplicationLabel(manifest));
} catch (Exception e) {
// ignore.
}
//Synthetic comment -- @@ -1530,18 +1502,6 @@
// set the Image Overlay as the image factory.
params.setImageFactory(getCanvasControl().getImageOverlay());

        // ---------------------------------------
        // Data binding DEBUG
//        AdapterBinding binding = new AdapterBinding(3);
//        binding.addHeader(new ResourceReference("header", false));
//        binding.addFooter(new ResourceReference("footer", false));
//        DataBindingItem item = new DataBindingItem("groupitem", false, 3);
//        binding.addItem(item);
//        item.addChild(new DataBindingItem("separator", false, 1));
//        item.addChild(new DataBindingItem("listitem", false, 3));
//        params.addAdapterBinding(new ResourceReference("listview"), binding);
        // ---------------------------------------

try {
mProjectCallback.setLogger(logger);
return layoutLib.createSession(params);
//Synthetic comment -- @@ -1564,7 +1524,7 @@
* @return the image, or null if something went wrong
*/
BufferedImage renderThemeItem(String itemName, int width, int height) {
        ResourceResolver resources = createResolver();
LayoutLibrary layoutLibrary = getLayoutLibrary();
IProject project = getProject();
ResourceValue drawableResourceValue = resources.findItemInTheme(itemName);
//Synthetic comment -- @@ -1591,19 +1551,44 @@
return null;
}

    public ResourceResolver createResolver() {
        String theme = mConfigComposite.getTheme();
        boolean isProjectTheme = mConfigComposite.isProjectTheme();
        Map<ResourceType, Map<String, ResourceValue>> configuredProjectRes =
            mConfigListener.getConfiguredProjectResources();

        // Get the framework resources
        Map<ResourceType, Map<String, ResourceValue>> frameworkResources =
            mConfigListener.getConfiguredFrameworkResources();

        return ResourceResolver.create(
                configuredProjectRes, frameworkResources,
                theme, isProjectTheme);
}

/**
//Synthetic comment -- @@ -1702,6 +1687,7 @@

// force a reparse in case a value XML file changed.
mConfiguredProjectRes = null;

// clear the cache in the bridge in case a bitmap/9-patch changed.
LayoutLibrary layoutLib = getReadyLayoutLib(true /*displayError*/);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PreviewIconFactory.java
//Synthetic comment -- index 4d28df5..80cc87d 100644

//Synthetic comment -- @@ -338,7 +338,7 @@
RGB background = null;
RGB foreground = null;

        ResourceResolver resources = mPalette.getEditor().createResolver();
StyleResourceValue theme = resources.getCurrentTheme();
if (theme != null) {
background = resolveThemeColor(resources, "windowBackground"); //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractStyleRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/ExtractStyleRefactoring.java
//Synthetic comment -- index 3e18fca..49d0fef 100644

//Synthetic comment -- @@ -515,7 +515,7 @@
if (types.size() == 1) {
String view = DescriptorsUtils.getBasename(types.iterator().next());

            ResourceResolver resolver = mEditor.getGraphicalEditor().createResolver();
// Look up the theme item name, which for a Button would be "buttonStyle", and so on.
String n = Character.toLowerCase(view.charAt(0)) + view.substring(1)
+ "Style"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
//Synthetic comment -- index 5e71822..0feab55 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.sdklib.SdkConstants.NS_RESOURCES;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_NAME;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_PACKAGE;
//Synthetic comment -- @@ -98,6 +100,8 @@
private IAbstractFile mManifestFile;
private long mLastModified;
private int mTargetSdk;

/**
* Qualified name for the per-project non-persistent property storing the
//Synthetic comment -- @@ -167,6 +171,8 @@
mManifestTheme = null;
mTargetSdk = 1; // Default when not specified
mPackage = ""; //$NON-NLS-1$

Document document = null;
try {
//Synthetic comment -- @@ -194,6 +200,18 @@
}
}

// Look up target SDK
String defaultTheme = root.getAttributeNS(NS_RESOURCES, ATTRIBUTE_THEME);
if (defaultTheme == null || defaultTheme.length() == 0) {
//Synthetic comment -- @@ -292,6 +310,26 @@
}

/**
* Returns the activity associated with the given layout file. Makes an educated guess
* by peeking at the usages of the R.layout.name field corresponding to the layout and
* if it finds a usage.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/AdtProjectTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/AdtProjectTest.java
//Synthetic comment -- index b82d5ba..650cf04 100644

//Synthetic comment -- @@ -89,6 +89,9 @@
super.setUp();

// Prevent preview icon computation during plugin test to make test faster
AdtPrefs.getPrefs().setPaletteModes("ICON_TEXT"); //$NON-NLS-1$

getProject();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java
//Synthetic comment -- index 66a2482..5eba812 100644

//Synthetic comment -- @@ -166,7 +166,40 @@

}



private ManifestInfo getManifestInfo(String manifestContents) throws Exception {
InputStream bstream = new ByteArrayInputStream(







