/*Pick layout themes from manifest registrations and target SDK

This changeset changes the way the layout editor picks themes when a
layout is opened. It used to just pick the first available theme in
the theme chooser.

Now it performs the following checks, in order:

* Does this layout have a persisted theme setting from a previous run
  of the tool? If yes, use it.
* Can the activity for this layout be determined? If so, look at the
  manifest registration for the corresponding activity, and if it
  specifies a theme, use it.
* If not, does the manifest specify a default theme for the project?
  If so, use it.
* If not, is the target SDK version (or the minimum SDK version, if
  the target is not specified) at least API level 11 or higher? If so,
  the default theme is "Theme.Holo"
* If not, the default theme is "Theme".
* If the file to be opened is included from some other layout, use
  the no-decorations versions of the default layouts, e.g.
  Theme.NoTitleBar or Theme.Holo.NoActionBar.
* At the end of this resolution, the computed theme is stored as the
  persisted theme setting for this layout, so the above algorithm will
  only be computed once. We might want to tweak this such that it
  distinguishes between a default computation of a theme and a manual
  user choice of a theme.
* If the file is opened as "Show Included In" (e.g. rendered within an
  outer file) then the theme chosen is the one for the outer file.

During startup, this information will be asked for each and every
layout being reopened, so there is now a "ManifestInfo" class attached
to the project which keeps manifest information around. It checks the
timestamp of the AndroidManifest.xml file and refreshes its
information lazily if necessary.

All themes mentioned in the manifest are listed in a special section
at the top of the theme chooser (below the entry for the default
computed described above).

The code to look up the associated Activity of a layout is using a
simple heuristic: it looks for usages of the corresponding R.layout
field, and prefers references from methods called onCreate or in
classes whose superclass name ends with Activity. I tried a different
approach where I searched for usages of Activity.setContentView(int)
but this failed to identify a number of cases where the activity was
doing some simple logic and didn't pass the layout id directly as a
parameter in setContentView, so I went back to the basic approach.

Change-Id:Ibd3c0f089fefe38e6e6c607d65524990699c86d3*/
//Synthetic comment -- diff --git a/common/src/com/android/io/FileWrapper.java b/common/src/com/android/io/FileWrapper.java
//Synthetic comment -- index c1e8f81..2859c0d 100644

//Synthetic comment -- @@ -137,6 +137,10 @@
return isFile();
}

public IAbstractFolder getParentFolder() {
String p = this.getParent();
if (p == null) {








//Synthetic comment -- diff --git a/common/src/com/android/io/IAbstractFile.java b/common/src/com/android/io/IAbstractFile.java
//Synthetic comment -- index d8d794d..6dfc8d8 100644

//Synthetic comment -- @@ -50,4 +50,9 @@
* Returns the preferred mode to write into the file.
*/
PreferredWriteMode getPreferredWriteMode();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 77d74d8..f941ceb 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_NS_NAME_PREFIX;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;
//Synthetic comment -- @@ -32,9 +33,12 @@
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
//Synthetic comment -- @@ -73,9 +77,11 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SortedSet;

/**
//Synthetic comment -- @@ -137,7 +143,13 @@
private Combo mThemeCombo;
private Combo mTargetCombo;

    private int mPlatformThemeCount = 0;
/** updates are disabled if > 0 */
private int mDisableUpdates = 0;

//Synthetic comment -- @@ -219,6 +231,7 @@
ResourceRepository getFrameworkResources(IAndroidTarget target);
Map<ResourceType, Map<String, ResourceValue>> getConfiguredProjectResources();
Map<ResourceType, Map<String, ResourceValue>> getConfiguredFrameworkResources();
}

/**
//Synthetic comment -- @@ -681,8 +694,6 @@
loadedConfigData = mState.setData(data);
}

                    // update the themes and locales.
                    updateThemes();
updateLocales();

// If the current state was loaded from the persistent storage, we update the
//Synthetic comment -- @@ -712,6 +723,14 @@
}
}

// update the string showing the config value
updateConfigDisplay(mEditedConfig);

//Synthetic comment -- @@ -1344,11 +1363,109 @@
try {
// Reset the combo
mThemeCombo.removeAll();
            mPlatformThemeCount = 0;

ArrayList<String> themes = new ArrayList<String>();

// get the themes, and languages from the Framework.
if (frameworkRes != null) {
// get the configured resources for the framework
Map<ResourceType, Map<String, ResourceValue>> frameworResources =
//Synthetic comment -- @@ -1364,7 +1481,6 @@
String name = value.getName();
if (name.startsWith("Theme.") || name.equals("Theme")) {
themes.add(value.getName());
                            mPlatformThemeCount++;
}
}

//Synthetic comment -- @@ -1373,9 +1489,10 @@

for (String theme : themes) {
mThemeCombo.add(theme);
}

                    mPlatformThemeCount = themes.size();
themes.clear();
}
}
//Synthetic comment -- @@ -1403,14 +1520,16 @@
}

// sort them and add them the to the combo.
                        if (mPlatformThemeCount > 0 && themes.size() > 0) {
mThemeCombo.add(THEME_SEPARATOR);
}

Collections.sort(themes);

for (String theme : themes) {
mThemeCombo.add(theme);
}
}
}
//Synthetic comment -- @@ -1419,7 +1538,7 @@
// try to reselect the previous theme.
boolean needDefaultSelection = true;

            if (mState.theme != null) {
final int count = mThemeCombo.getItemCount();
for (int i = 0 ; i < count ; i++) {
if (mState.theme.equals(mThemeCombo.getItem(i))) {
//Synthetic comment -- @@ -1444,6 +1563,8 @@
} finally {
mDisableUpdates--;
}
}

// ---- getters for the config selection values ----
//Synthetic comment -- @@ -1573,7 +1694,7 @@
* @return true for project theme, false for framework theme
*/
public boolean isProjectTheme() {
        return mThemeCombo.getSelectionIndex() >= mPlatformThemeCount;
}

public IAndroidTarget getRenderingTarget() {
//Synthetic comment -- @@ -2323,4 +2444,3 @@
}
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CustomViewFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/CustomViewFinder.java
//Synthetic comment -- index 8cb0d26..02a674c 100644

//Synthetic comment -- @@ -20,6 +20,7 @@
import static com.android.sdklib.SdkConstants.FN_FRAMEWORK_LIBRARY;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.util.Pair;
//Synthetic comment -- @@ -38,7 +39,6 @@
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
//Synthetic comment -- @@ -225,7 +225,7 @@
}
};
try {
            IJavaProject javaProject = (IJavaProject) mProject.getNature(JavaCore.NATURE_ID);
if (javaProject != null) {
String className = layoutsOnly ? CLASS_VIEWGROUP : CLASS_VIEW;
IType activityType = javaProject.findType(className);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 47d4f22..5c912b1 100644

//Synthetic comment -- @@ -594,7 +594,6 @@
return null;
}


public ProjectResources getProjectResources() {
if (mEditedFile != null) {
ResourceManager manager = ResourceManager.getInstance();
//Synthetic comment -- @@ -732,6 +731,10 @@
getCanvasControl().setFitScale(true);
}
}
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageControl.java
//Synthetic comment -- index c33c4fe..7af89f8 100644

//Synthetic comment -- @@ -49,14 +49,16 @@
private float mScale = 1.0f;

/**
     * Creates an ImageControl rendering the given image, which will be dispose when this
     * control is disposed
*
* @param parent the parent to add the image control to
* @param style the SWT style to use
* @param image the image to be rendered, which must not be null and should be unique
*            for this image control since it will be disposed by this control when
     *            the control is disposed
*/
public ImageControl(Composite parent, int style, Image image) {
super(parent, style | SWT.NO_FOCUS | SWT.DOUBLE_BUFFERED);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index 86e33be..7832197 100644

//Synthetic comment -- @@ -186,6 +186,27 @@
}
}

@VisibleForTesting
/* package */ List<String> getIncludedBy(String included) {
ensureInitialized();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..05c4a5e

//Synthetic comment -- @@ -0,0 +1,514 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index b7533be..b315cf5 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestEditor;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
//Synthetic comment -- @@ -89,7 +90,6 @@
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
//Synthetic comment -- @@ -617,7 +617,7 @@
try {
IJavaSearchScope scope = null;
IType activityType = null;
            IJavaProject javaProject = (IJavaProject) project.getNature(JavaCore.NATURE_ID);
if (javaProject != null) {
activityType = javaProject.findType(SdkConstants.CLASS_ACTIVITY);
if (activityType != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 428dbbf..467ae49 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.eclipse.adt.internal.resources;

import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
//Synthetic comment -- @@ -409,4 +411,36 @@
}
return null;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFileWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFileWrapper.java
//Synthetic comment -- index a7c895b..b4e7a3f 100644

//Synthetic comment -- @@ -97,6 +97,10 @@
return mFile;
}

@Override
public boolean equals(Object obj) {
if (obj instanceof IFileWrapper) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java
new file mode 100644
//Synthetic comment -- index 0000000..590c611

//Synthetic comment -- @@ -0,0 +1,156 @@








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceHelperTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceHelperTest.java
//Synthetic comment -- index 1a69847..a653ae2 100644

//Synthetic comment -- @@ -170,4 +170,10 @@
assertFalse(ResourceHelper.canCreateResource("@android:dimen/foo"));
assertFalse(ResourceHelper.canCreateResource("@android:color/foo"));
}
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index e3c93b7..89a4cba 100644

//Synthetic comment -- @@ -28,13 +28,23 @@

public class ResourceResolver extends RenderResources {

    private final static String REFERENCE_STYLE = ResourceType.STYLE.getName() + "/";
public final static String PREFIX_ANDROID_RESOURCE_REF = "@android:";
public final static String PREFIX_RESOURCE_REF = "@";
public final static String PREFIX_ANDROID_THEME_REF = "?android:";
public final static String PREFIX_THEME_REF = "?";
public final static String PREFIX_ANDROID = "android:";


private final Map<ResourceType, Map<String, ResourceValue>> mProjectResources;
private final Map<ResourceType, Map<String, ResourceValue>> mFrameworkResources;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java
//Synthetic comment -- index f06b773..26fcd7b 100644

//Synthetic comment -- @@ -76,6 +76,7 @@
public final static String ATTRIBUTE_REQ_HARDKEYBOARD = "reqHardKeyboard";
public final static String ATTRIBUTE_REQ_KEYBOARDTYPE = "reqKeyboardType";
public final static String ATTRIBUTE_REQ_TOUCHSCREEN = "reqTouchScreen";

/**
* Returns an {@link IAbstractFile} object representing the manifest for the given project.







