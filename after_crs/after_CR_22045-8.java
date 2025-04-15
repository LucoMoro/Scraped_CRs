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

    public long getModificationStamp() {
        return lastModified();
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

    /**
     * Returns the last modification timestamp
     */
    long getModificationStamp();
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigurationComposite.java
//Synthetic comment -- index 77d74d8..f941ceb 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ide.eclipse.adt.internal.editors.layout.configuration;

import static com.android.ide.common.layout.LayoutConstants.ANDROID_NS_NAME_PREFIX;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.rendering.api.StyleResourceValue;
//Synthetic comment -- @@ -32,9 +33,12 @@
import com.android.ide.common.resources.configuration.ResourceQualifier;
import com.android.ide.common.resources.configuration.ScreenDimensionQualifier;
import com.android.ide.common.resources.configuration.ScreenOrientationQualifier;
import com.android.ide.common.resources.configuration.ScreenSizeQualifier;
import com.android.ide.common.resources.configuration.VersionQualifier;
import com.android.ide.common.sdk.LoadStatus;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestInfo;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
import com.android.ide.eclipse.adt.internal.sdk.AndroidTargetData;
//Synthetic comment -- @@ -73,9 +77,11 @@
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
//Synthetic comment -- @@ -137,7 +143,13 @@
private Combo mThemeCombo;
private Combo mTargetCombo;

    /**
     * List of booleans, matching item for item the theme names in the mThemeCombo
     * combobox, where each boolean represents whether the corresponding theme is a
     * project theme
     */
    private List<Boolean> mIsProjectTheme = new ArrayList<Boolean>(40);

/** updates are disabled if > 0 */
private int mDisableUpdates = 0;

//Synthetic comment -- @@ -219,6 +231,7 @@
ResourceRepository getFrameworkResources(IAndroidTarget target);
Map<ResourceType, Map<String, ResourceValue>> getConfiguredProjectResources();
Map<ResourceType, Map<String, ResourceValue>> getConfiguredFrameworkResources();
        String getIncludedWithin();
}

/**
//Synthetic comment -- @@ -681,8 +694,6 @@
loadedConfigData = mState.setData(data);
}

updateLocales();

// If the current state was loaded from the persistent storage, we update the
//Synthetic comment -- @@ -712,6 +723,14 @@
}
}

                    // Update themes. This is done after updating the devices above,
                    // since we want to look at the chosen device size to decide
                    // what the default theme (for example, with Honeycomb we choose
                    // Holo as the default theme but only if the screen size is XLARGE
                    // (and of course only if the manifest does not specify another
                    // default theme).
                    updateThemes();

// update the string showing the config value
updateConfigDisplay(mEditedConfig);

//Synthetic comment -- @@ -1344,11 +1363,109 @@
try {
// Reset the combo
mThemeCombo.removeAll();
            mIsProjectTheme.clear();

ArrayList<String> themes = new ArrayList<String>();
            String includedIn = mListener != null ? mListener.getIncludedWithin() : null;

            // First list any themes that are declared by the manifest
            if (mEditedFile != null) {
                IProject project = mEditedFile.getProject();
                ManifestInfo manifest = ManifestInfo.get(project);

                // Look up the screen size for the current configuration
                ScreenSize screenSize = null;
                if (mState.device != null) {
                    List<DeviceConfig> configs = mState.device.getConfigs();
                    for (DeviceConfig config : configs) {
                        ScreenSizeQualifier qualifier =
                            config.getConfig().getScreenSizeQualifier();
                        screenSize = qualifier.getValue();
                        break;
                    }
                }
                // Look up the default/fallback theme to use for this project (which
                // depends on the screen size when no particular theme is specified
                // in the manifest)
                String defaultTheme = manifest.getDefaultTheme(screenSize);

                Map<String, String> activityThemes = manifest.getActivityThemes();
                String pkg = manifest.getPackage();
                String preferred = null;
                boolean isIncluded = includedIn != null;
                if (mState.theme == null || isIncluded) {
                    String layoutName = ResourceHelper.getLayoutName(mEditedFile);

                    // If we are rendering a layout in included context, pick the theme
                    // from the outer layout instead
                    if (includedIn != null) {
                        layoutName = includedIn;
                    }

                    String activity = ManifestInfo.guessActivity(project, layoutName, pkg);
                    if (activity != null) {
                        preferred = activityThemes.get(activity);
                    }
                    if (preferred == null) {
                        preferred = defaultTheme;
                    }
                    String preferredTheme = ResourceHelper.styleToTheme(preferred);
                    if (includedIn == null) {
                        mState.theme = preferredTheme;
                    }
                    boolean isProjectTheme = !preferred.startsWith(PREFIX_ANDROID_STYLE);
                    mThemeCombo.add(preferredTheme);
                    mIsProjectTheme.add(Boolean.valueOf(isProjectTheme));

                    mThemeCombo.add(THEME_SEPARATOR);
                    mIsProjectTheme.add(Boolean.FALSE);
                }

                // Create a sorted list of unique themes referenced in the manifest
                // (sort alphabetically, but place the preferred theme at the
                // top of the list)
                Set<String> themeSet = new HashSet<String>(activityThemes.values());
                themeSet.add(defaultTheme);
                List<String> themeList = new ArrayList<String>(themeSet);
                final String first = preferred;
                Collections.sort(themeList, new Comparator<String>() {
                    public int compare(String s1, String s2) {
                        if (s1 == first) {
                            return -1;
                        } else if (s1 == first) {
                            return 1;
                        } else {
                            return s1.compareTo(s2);
                        }
                    }
                });

                if (themeList.size() > 1 ||
                        (themeList.size() == 1 && (preferred == null ||
                                !preferred.equals(themeList.get(0))))) {
                    for (String style : themeList) {
                        String theme = ResourceHelper.styleToTheme(style);

                        // Initialize the chosen theme to the first item
                        // in the used theme list (that's what would be chosen
                        // anyway) such that we stop attempting to look up
                        // the associated activity (during initialization,
                        // this method can be called repeatedly.)
                        if (mState.theme == null) {
                            mState.theme = theme;
                        }

                        boolean isProjectTheme = !style.startsWith(PREFIX_ANDROID_STYLE);
                        mThemeCombo.add(theme);
                        mIsProjectTheme.add(Boolean.valueOf(isProjectTheme));
                    }
                    mThemeCombo.add(THEME_SEPARATOR);
                    mIsProjectTheme.add(Boolean.FALSE);
                }
            }

// get the themes, and languages from the Framework.
            int platformThemeCount = 0;
if (frameworkRes != null) {
// get the configured resources for the framework
Map<ResourceType, Map<String, ResourceValue>> frameworResources =
//Synthetic comment -- @@ -1364,7 +1481,6 @@
String name = value.getName();
if (name.startsWith("Theme.") || name.equals("Theme")) {
themes.add(value.getName());
}
}

//Synthetic comment -- @@ -1373,9 +1489,10 @@

for (String theme : themes) {
mThemeCombo.add(theme);
                        mIsProjectTheme.add(Boolean.FALSE);
}

                    platformThemeCount = themes.size();
themes.clear();
}
}
//Synthetic comment -- @@ -1403,14 +1520,16 @@
}

// sort them and add them the to the combo.
                        if (platformThemeCount > 0 && themes.size() > 0) {
mThemeCombo.add(THEME_SEPARATOR);
                            mIsProjectTheme.add(Boolean.FALSE);
}

Collections.sort(themes);

for (String theme : themes) {
mThemeCombo.add(theme);
                            mIsProjectTheme.add(Boolean.TRUE);
}
}
}
//Synthetic comment -- @@ -1419,7 +1538,7 @@
// try to reselect the previous theme.
boolean needDefaultSelection = true;

            if (mState.theme != null && includedIn == null) {
final int count = mThemeCombo.getItemCount();
for (int i = 0 ; i < count ; i++) {
if (mState.theme.equals(mThemeCombo.getItem(i))) {
//Synthetic comment -- @@ -1444,6 +1563,8 @@
} finally {
mDisableUpdates--;
}

        assert mIsProjectTheme.size() == mThemeCombo.getItemCount();
}

// ---- getters for the config selection values ----
//Synthetic comment -- @@ -1573,7 +1694,7 @@
* @return true for project theme, false for framework theme
*/
public boolean isProjectTheme() {
        return mIsProjectTheme.get(mThemeCombo.getSelectionIndex()).booleanValue();
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
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.ProjectState;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.util.Pair;
//Synthetic comment -- @@ -38,7 +39,6 @@
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
//Synthetic comment -- @@ -225,7 +225,7 @@
}
};
try {
            IJavaProject javaProject = BaseProjectHelper.getJavaProject(mProject);
if (javaProject != null) {
String className = layoutsOnly ? CLASS_VIEWGROUP : CLASS_VIEW;
IType activityType = javaProject.findType(className);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/GraphicalEditorPart.java
//Synthetic comment -- index 0a65865..4700560 100644

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

        public String getIncludedWithin() {
            return mIncludedWithin != null ? mIncludedWithin.getName() : null;
        }
}

/**








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/ImageControl.java
//Synthetic comment -- index c33c4fe..7af89f8 100644

//Synthetic comment -- @@ -49,14 +49,16 @@
private float mScale = 1.0f;

/**
     * Creates an ImageControl rendering the given image, which will be disposed when this
     * control is disposed (unless the {@link #setDisposeImage} method is called to turn
     * off auto dispose).
*
* @param parent the parent to add the image control to
* @param style the SWT style to use
* @param image the image to be rendered, which must not be null and should be unique
*            for this image control since it will be disposed by this control when
     *            the control is disposed (unless the {@link #setDisposeImage} method is
     *            called to turn off auto dispose)
*/
public ImageControl(Composite parent, int style, Image image) {
super(parent, style | SWT.NO_FOCUS | SWT.DOUBLE_BUFFERED);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/IncludeFinder.java
//Synthetic comment -- index 86e33be..7832197 100644

//Synthetic comment -- @@ -186,6 +186,27 @@
}
}

    /**
     * Returns true if the given resource is included from some other layout in the
     * project
     *
     * @param included the resource to check
     * @return true if the file is included by some other layout
     */
    public boolean isIncluded(IResource included) {
        ensureInitialized();
        String mapKey = getMapKey(included);
        List<String> result = mIncludedBy.get(mapKey);
        if (result == null) {
            String name = getResourceName(included);
            if (!name.equals(mapKey)) {
                result = mIncludedBy.get(name);
            }
        }

        return result != null && result.size() > 0;
    }

@VisibleForTesting
/* package */ List<String> getIncludedBy(String included) {
ensureInitialized();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..05c4a5e

//Synthetic comment -- @@ -0,0 +1,514 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.internal.editors.manifest;

import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.sdklib.SdkConstants.NS_RESOURCES;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_MIN_SDK_VERSION;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_NAME;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_PACKAGE;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_TARGET_SDK_VERSION;
import static com.android.sdklib.xml.AndroidManifest.ATTRIBUTE_THEME;
import static com.android.sdklib.xml.AndroidManifest.NODE_ACTIVITY;
import static com.android.sdklib.xml.AndroidManifest.NODE_USES_SDK;
import static org.eclipse.jdt.core.search.IJavaSearchConstants.REFERENCES;

import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.io.IAbstractFile;
import com.android.resources.ScreenSize;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Retrieves and caches manifest information such as the themes to be used for
 * a given activity.
 *
 * @see AndroidManifest
 */
public class ManifestInfo {
    /**
     * The maximum number of milliseconds to search for an activity in the codebase when
     * attempting to associate layouts with activities in
     * {@link #guessActivity(IFile, String)}
     */
    private static final int SEARCH_TIMEOUT_MS = 3000;

    private final IProject mProject;
    private String mPackage;
    private String mDefaultTheme;
    private String mLargeDefaultTheme;
    private Map<String, String> mActivityThemes;
    private IAbstractFile mManifestFile;
    private long mLastModified;

    /**
     * Qualified name for the per-project non-persistent property storing the
     * {@link ManifestInfo} for this project
     */
    final static QualifiedName MANIFEST_FINDER = new QualifiedName(AdtPlugin.PLUGIN_ID,
            "manifest"); //$NON-NLS-1$

    /**
     * Constructs an {@link ManifestInfo} for the given project. Don't use this method;
     * use the {@link #get} factory method instead.
     *
     * @param project project to create an {@link ManifestInfo} for
     */
    private ManifestInfo(IProject project) {
        mProject = project;
    }

    /**
     * Returns the {@link ManifestInfo} for the given project
     *
     * @param project the project the finder is associated with
     * @return a {@ManifestInfo} for the given project, never null
     */
    public static ManifestInfo get(IProject project) {
        ManifestInfo finder = null;
        try {
            finder = (ManifestInfo) project.getSessionProperty(MANIFEST_FINDER);
        } catch (CoreException e) {
            // Not a problem; we will just create a new one
        }

        if (finder == null) {
            finder = new ManifestInfo(project);
            try {
                project.setSessionProperty(MANIFEST_FINDER, finder);
            } catch (CoreException e) {
                AdtPlugin.log(e, "Can't store ManifestInfo");
            }
        }

        return finder;
    }

    /**
     * Ensure that the package, theme and activity maps are initialized and up to date
     * with respect to the manifest file
     */
    private void sync() {
        if (mManifestFile == null) {
            IFolderWrapper projectFolder = new IFolderWrapper(mProject);
            mManifestFile = AndroidManifest.getManifest(projectFolder);
            if (mManifestFile == null) {
                return;
            }
        }

        // Check to see if our data is up to date
        long fileModified = mManifestFile.getModificationStamp();
        if (fileModified == mLastModified) {
            // Already have up to date data
            return;
        }
        mLastModified = fileModified;

        mActivityThemes = new HashMap<String, String>();
        mDefaultTheme = PREFIX_ANDROID_STYLE + "Theme"; //$NON-NLS-1$
        mLargeDefaultTheme = mDefaultTheme;

        mPackage = ""; //$NON-NLS-1$
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            InputSource is = new InputSource(mManifestFile.getContents());

            factory.setNamespaceAware(true);
            factory.setValidating(false);
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(is);

            Element root = document.getDocumentElement();
            mPackage = root.getAttribute(ATTRIBUTE_PACKAGE);
            NodeList activities = document.getElementsByTagName(NODE_ACTIVITY);
            for (int i = 0, n = activities.getLength(); i < n; i++) {
                Element activity = (Element) activities.item(i);
                String theme = activity.getAttributeNS(NS_RESOURCES, ATTRIBUTE_THEME);
                if (theme != null && theme.length() > 0) {
                    String name = activity.getAttributeNS(NS_RESOURCES, ATTRIBUTE_NAME);
                    if (name.startsWith(".")  //$NON-NLS-1$
                            && mPackage != null && mPackage.length() > 0) {
                        name = mPackage + name;
                    }
                    mActivityThemes.put(name, theme);
                }
            }

            // Look up target SDK
            String defaultTheme = root.getAttributeNS(NS_RESOURCES, ATTRIBUTE_THEME);
            if (defaultTheme == null || defaultTheme.length() == 0) {
                // From manifest theme documentation:
                // "If that attribute is also not set, the default system theme is used."

                NodeList usesSdks = root.getElementsByTagName(NODE_USES_SDK);
                if (usesSdks.getLength() > 0) {
                    Element usesSdk = (Element) usesSdks.item(0);
                    String targetSdk = null;
                    if (usesSdk.hasAttributeNS(NS_RESOURCES, ATTRIBUTE_TARGET_SDK_VERSION)) {
                        targetSdk = usesSdk.getAttributeNS(NS_RESOURCES,
                                ATTRIBUTE_TARGET_SDK_VERSION);
                    } else if (usesSdk.hasAttributeNS(NS_RESOURCES, ATTRIBUTE_MIN_SDK_VERSION)) {
                        targetSdk = usesSdk.getAttributeNS(NS_RESOURCES,
                                ATTRIBUTE_MIN_SDK_VERSION);
                    }
                    if (targetSdk != null) {
                        int apiLevel = -1;
                        try {
                            apiLevel = Integer.valueOf(targetSdk);
                        } catch (NumberFormatException e) {
                            // Handle codename
                            if (Sdk.getCurrent() != null) {
                                IAndroidTarget target = Sdk.getCurrent().getTargetFromHashString(
                                        "android-" + targetSdk); //$NON-NLS-1$
                                if (target != null) {
                                    // codename future API level is current api + 1
                                    apiLevel = target.getVersion().getApiLevel() + 1;
                                }
                            }
                        }

                        if (apiLevel >= 11) {
                            mLargeDefaultTheme = PREFIX_ANDROID_STYLE + "Theme.Holo"; //$NON-NLS-1$
                        }
                        // When Holo works everywhere:
                        // if (apiLevel >= N) {
                        //    mDefaultTheme = PREFIX_ANDROID_STYLE + "Theme.Holo"; //$NON-NLS-1$
                        // }
                    }
                }
            } else {
                mDefaultTheme = mLargeDefaultTheme = defaultTheme;
            }
        } catch (SAXException e) {
            AdtPlugin.log(e, "Malformed manifest");
        } catch (Exception e) {
            AdtPlugin.log(e, "Could not read Manifest data");
        }
    }

    /**
     * Returns the default package registered in the Android manifest
     *
     * @return the default package registered in the manifest
     */
    public String getPackage() {
        sync();
        return mPackage;
    }

    /**
     * Returns a map from activity full class names to the corresponding theme style to be
     * used
     *
     * @return a map from activity fqcn to theme style
     */
    public Map<String, String> getActivityThemes() {
        sync();
        return mActivityThemes;
    }

    /**
     * Returns the default theme for this project, by looking at the manifest default
     * theme registration, target SDK, etc.
     *
     * @param screenSize the screen size to obtain a default theme for, or null if unknown
     * @return the theme to use for this project, never null
     */
    public String getDefaultTheme(ScreenSize screenSize) {
        sync();

        if (screenSize == ScreenSize.XLARGE) {
            return mLargeDefaultTheme;
        } else {
            return mDefaultTheme;
        }
    }

    /**
     * Returns the activity associated with the given layout file. Makes an educated guess
     * by peeking at the usages of the R.layout.name field corresponding to the layout and
     * if it finds a usage.
     *
     * @param project the project containing the layout
     * @param layoutName the layout whose activity we want to look up
     * @param pkg the package containing activities
     * @return the activity name
     */
    public static String guessActivity(IProject project, String layoutName, String pkg) {
        final AtomicReference<String> activity = new AtomicReference<String>();
        SearchRequestor requestor = new SearchRequestor() {
            @Override
            public void acceptSearchMatch(SearchMatch match) throws CoreException {
                Object element = match.getElement();
                if (element instanceof IMethod) {
                    IMethod method = (IMethod) element;
                    IType declaringType = method.getDeclaringType();
                    String fqcn = declaringType.getFullyQualifiedName();
                    if (activity.get() == null
                            || declaringType.getSuperclassName().endsWith("Activity") //$NON-NLS-1$
                            || method.getElementName().equals("onCreate")) { //$NON-NLS-1$
                        activity.set(fqcn);
                    }
                }
            }
        };
        try {
            IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
            if (javaProject == null) {
                return null;
            }
            // TODO - look around a bit more and see if we can figure out whether the
            // call if from within a setContentView call!

            // Search for which java classes call setContentView(R.layout.layoutname);
            String typeFqcn = "R.layout"; //$NON-NLS-1$
            if (pkg != null) {
                typeFqcn = pkg + '.' + typeFqcn;
            }

            IType type = javaProject.findType(typeFqcn);
            if (type != null) {
                IField field = type.getField(layoutName);
                if (field.exists()) {
                    SearchPattern pattern = SearchPattern.createPattern(field, REFERENCES);
                    search(requestor, javaProject, pattern);
                }
            }
        } catch (CoreException e) {
            AdtPlugin.log(e, null);
        }

        return activity.get();
    }

    /**
     * Returns the activity associated with the given layout file.
     * <p>
     * This is an alternative to {@link #guessActivity(IFile, String)}. Whereas
     * guessActivity simply looks for references to "R.layout.foo", this method searches
     * for all usages of Activity#setContentView(int), and for each match it looks up the
     * corresponding call text (such as "setContentView(R.layout.foo)"). From this it uses
     * a regexp to pull out "foo" from this, and stores the association that layout "foo"
     * is associated with the activity class that contained the setContentView call.
     * <p>
     * This has two potential advantages:
     * <ol>
     * <li>It can be faster. We do the reference search -once-, and we've built a map of
     * all the layout-to-activity mappings which we can then immediately look up other
     * layouts for, which is particularly useful at startup when we have to compute the
     * layout activity associations to populate the theme choosers.
     * <li>It can be more accurate. Just because an activity references an "R.layout.foo"
     * field doesn't mean it's setting it as a content view.
     * </ol>
     * However, this second advantage is also its chief problem. There are some common
     * code constructs which means that the associated layout is not explicitly referenced
     * in a direct setContentView call; on a couple of sample projects I tested I found
     * patterns like for example "setContentView(v)" where "v" had been computed earlier.
     * Therefore, for now we're going to stick with the more general approach of just
     * looking up each field when needed. We're keeping the code around, though statically
     * compiled out with the "if (false)" construct below in case we revisit this.
     *
     * @param layoutFile the layout whose activity we want to look up
     * @return the activity name
     */
    @SuppressWarnings("all")
    public String guessActivityBySetContentView(String layoutName) {
        if (false) {
            // These should be fields
            final Pattern LAYOUT_FIELD_PATTERN =
                Pattern.compile("R\\.layout\\.([a-z0-9_]+)"); //$NON-NLS-1$
            Map<String, String> mUsages = null;

            sync();
            if (mUsages == null) {
                final Map<String, String> usages = new HashMap<String, String>();
                mUsages = usages;
                SearchRequestor requestor = new SearchRequestor() {
                    @Override
                    public void acceptSearchMatch(SearchMatch match) throws CoreException {
                        Object element = match.getElement();
                        if (element instanceof IMethod) {
                            IMethod method = (IMethod) element;
                            IType declaringType = method.getDeclaringType();
                            String fqcn = declaringType.getFullyQualifiedName();
                            IDocumentProvider provider = new TextFileDocumentProvider();
                            IResource resource = match.getResource();
                            try {
                                provider.connect(resource);
                                IDocument document = provider.getDocument(resource);
                                if (document != null) {
                                    String matchText = document.get(match.getOffset(),
                                            match.getLength());
                                    Matcher matcher = LAYOUT_FIELD_PATTERN.matcher(matchText);
                                    if (matcher.find()) {
                                        usages.put(matcher.group(1), fqcn);
                                    }
                                }
                            } catch (Exception e) {
                                AdtPlugin.log(e, "Can't find range information for %1$s",
                                        resource.getName());
                            } finally {
                                provider.disconnect(resource);
                            }
                        }
                    }
                };
                try {
                    IJavaProject javaProject = BaseProjectHelper.getJavaProject(mProject);
                    if (javaProject == null) {
                        return null;
                    }

                    // Search for which java classes call setContentView(R.layout.layoutname);
                    String typeFqcn = "R.layout"; //$NON-NLS-1$
                    if (mPackage != null) {
                        typeFqcn = mPackage + '.' + typeFqcn;
                    }

                    IType activityType = javaProject.findType(SdkConstants.CLASS_ACTIVITY);
                    if (activityType != null) {
                        IMethod method = activityType.getMethod(
                                "setContentView", new String[] {"I"}); //$NON-NLS-1$ //$NON-NLS-2$
                        if (method.exists()) {
                            SearchPattern pattern = SearchPattern.createPattern(method,
                                    REFERENCES);
                            search(requestor, javaProject, pattern);
                        }
                    }
                } catch (CoreException e) {
                    AdtPlugin.log(e, null);
                }
            }

            return mUsages.get(layoutName);
        }

        return null;
    }

    /**
     * Performs a search using the given pattern, scope and handler. The search will abort
     * if it takes longer than {@link #SEARCH_TIMEOUT_MS} milliseconds.
     */
    private static void search(SearchRequestor requestor, IJavaProject javaProject,
            SearchPattern pattern) throws CoreException {
        // Find the package fragment specified in the manifest; the activities should
        // live there.
        IJavaSearchScope scope = createPackageScope(javaProject);

        SearchParticipant[] participants = new SearchParticipant[] {
            SearchEngine.getDefaultSearchParticipant()
        };
        SearchEngine engine = new SearchEngine();

        final long searchStart = System.currentTimeMillis();
        NullProgressMonitor monitor = new NullProgressMonitor() {
            private boolean mCancelled;
            @Override
            public void internalWorked(double work) {
                long searchEnd = System.currentTimeMillis();
                if (searchEnd - searchStart > SEARCH_TIMEOUT_MS) {
                    mCancelled = true;
                }
            }

            @Override
            public boolean isCanceled() {
                return mCancelled;
            }
        };
        engine.search(pattern, participants, scope, requestor, monitor);
    }

    /** Creates a package search scope for the first package root in the given java project */
    private static IJavaSearchScope createPackageScope(IJavaProject javaProject) {
        IPackageFragmentRoot packageRoot = getSourcePackageRoot(javaProject);

        IJavaSearchScope scope;
        if (packageRoot != null) {
            IJavaElement[] scopeElements = new IJavaElement[] { packageRoot };
            scope = SearchEngine.createJavaSearchScope(scopeElements);
        } else {
            scope = SearchEngine.createWorkspaceScope();;
        }
        return scope;
    }

    /** Returns the first package root for the given java project */
    private static IPackageFragmentRoot getSourcePackageRoot(IJavaProject javaProject) {
        IPackageFragmentRoot packageRoot = null;
        List<IPath> sources = BaseProjectHelper.getSourceClasspaths(javaProject);

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        for (IPath path : sources) {
            IResource firstSource = workspace.getRoot().findMember(path);
            if (firstSource != null) {
                packageRoot = javaProject.getPackageFragmentRoot(firstSource);
                if (packageRoot != null) {
                    break;
                }
            }
        }
        return packageRoot;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/xml/Hyperlinks.java
//Synthetic comment -- index b7533be..b315cf5 100644

//Synthetic comment -- @@ -47,6 +47,7 @@
import com.android.ide.eclipse.adt.internal.editors.layout.gle2.GraphicalEditorPart;
import com.android.ide.eclipse.adt.internal.editors.manifest.ManifestEditor;
import com.android.ide.eclipse.adt.internal.editors.resources.descriptors.ResourcesDescriptors;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;
//Synthetic comment -- @@ -89,7 +90,6 @@
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
//Synthetic comment -- @@ -617,7 +617,7 @@
try {
IJavaSearchScope scope = null;
IType activityType = null;
            IJavaProject javaProject = BaseProjectHelper.getJavaProject(project);
if (javaProject != null) {
activityType = javaProject.findType(SdkConstants.CLASS_ACTIVITY);
if (activityType != null) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/ResourceHelper.java
//Synthetic comment -- index 428dbbf..467ae49 100644

//Synthetic comment -- @@ -17,6 +17,8 @@
package com.android.ide.eclipse.adt.internal.resources;

import static com.android.AndroidConstants.FD_RES_VALUES;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_ANDROID_STYLE;
import static com.android.ide.common.resources.ResourceResolver.PREFIX_STYLE;
import static com.android.ide.eclipse.adt.AdtConstants.ANDROID_PKG;
import static com.android.ide.eclipse.adt.AdtConstants.EXT_XML;
import static com.android.ide.eclipse.adt.AdtConstants.WS_SEP;
//Synthetic comment -- @@ -409,4 +411,36 @@
}
return null;
}

    /**
     * Returns the theme name to be shown for theme styles, e.g. for "@style/Theme" it
     * returns "Theme"
     *
     * @param style a theme style string
     * @return the user visible theme name
     */
    public static String styleToTheme(String style) {
        if (style.startsWith(PREFIX_STYLE)) {
            style = style.substring(PREFIX_STYLE.length());
        } else if (style.startsWith(PREFIX_ANDROID_STYLE)) {
            style = style.substring(PREFIX_ANDROID_STYLE.length());
        }
        return style;
    }

    /**
     * Returns the layout resource name for the given layout file, e.g. for
     * /res/layout/foo.xml returns foo.
     *
     * @param layoutFile the layout file whose name we want to look up
     * @return the layout name
     */
    public static String getLayoutName(IFile layoutFile) {
        String layoutName = layoutFile.getName();
        int dotIndex = layoutName.indexOf('.');
        if (dotIndex != -1) {
            layoutName = layoutName.substring(0, dotIndex);
        }
        return layoutName;
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFileWrapper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/io/IFileWrapper.java
//Synthetic comment -- index a7c895b..b4e7a3f 100644

//Synthetic comment -- @@ -97,6 +97,10 @@
return mFile;
}

    public long getModificationStamp() {
        return mFile.getModificationStamp();
    }

@Override
public boolean equals(Object obj) {
if (obj instanceof IFileWrapper) {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/manifest/ManifestInfoTest.java
new file mode 100644
//Synthetic comment -- index 0000000..590c611

//Synthetic comment -- @@ -0,0 +1,156 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.ide.eclipse.adt.internal.editors.manifest;

import static com.android.resources.ScreenSize.LARGE;
import static com.android.resources.ScreenSize.NORMAL;
import static com.android.resources.ScreenSize.XLARGE;

import com.android.ide.eclipse.adt.internal.editors.layout.refactoring.AdtProjectTest;
import com.android.ide.eclipse.adt.internal.resources.ResourceHelper;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.NullProgressMonitor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Map;

public class ManifestInfoTest extends AdtProjectTest {
    @Override
    protected boolean testCaseNeedsUniqueProject() {
        return true;
    }

    public void testGetActivityThemes1() throws Exception {
        ManifestInfo info = getManifestInfo(
                "<manifest xmlns:android='http://schemas.android.com/apk/res/android'\n" +
                "    package='com.android.unittest'>\n" +
                "    <uses-sdk android:minSdkVersion='3' android:targetSdkVersion='4'/>\n" +
                "</manifest>\n");
        Map<String, String> map = info.getActivityThemes();
        assertEquals(map.toString(), 0, map.size());
        assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(NORMAL)));
        assertEquals("@android:style/Theme", info.getDefaultTheme(null));
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(XLARGE)));
    }

    public void testGetActivityThemes2() throws Exception {
        ManifestInfo info = getManifestInfo(
                "<manifest xmlns:android='http://schemas.android.com/apk/res/android'\n" +
                "    package='com.android.unittest'>\n" +
                "    <uses-sdk android:minSdkVersion='3' android:targetSdkVersion='11'/>\n" +
                "</manifest>\n");
        Map<String, String> map = info.getActivityThemes();
        assertEquals(map.toString(), 0, map.size());
        assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme.Holo", ResourceHelper.styleToTheme(info.getDefaultTheme(XLARGE)));
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(LARGE)));
    }

    public void testGetActivityThemes3() throws Exception {
        ManifestInfo info = getManifestInfo(
                "<manifest xmlns:android='http://schemas.android.com/apk/res/android'\n" +
                "    package='com.android.unittest'>\n" +
                "    <uses-sdk android:minSdkVersion='11'/>\n" +
                "</manifest>\n");
        Map<String, String> map = info.getActivityThemes();
        assertEquals(map.toString(), 0, map.size());
        assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme.Holo", ResourceHelper.styleToTheme(info.getDefaultTheme(XLARGE)));
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(NORMAL)));
    }

    public void testGetActivityThemes4() throws Exception {
        ManifestInfo info = getManifestInfo(
                "<manifest xmlns:android='http://schemas.android.com/apk/res/android'\n" +
                "    package='com.android.unittest'>\n" +
                "    <application\n" +
                "        android:label='@string/app_name'\n" +
                "        android:name='.app.TestApp' android:icon='@drawable/app_icon'>\n" +
                "\n" +
                "        <activity\n" +
                "            android:name='.prefs.PrefsActivity'\n" +
                "            android:label='@string/prefs_title' />\n" +
                "\n" +
                "        <activity\n" +
                "            android:name='.app.IntroActivity'\n" +
                "            android:label='@string/intro_title'\n" +
                "            android:theme='@android:style/Theme.Dialog' />\n" +
                "    </application>\n" +
                "    <uses-sdk android:minSdkVersion='3' android:targetSdkVersion='4'/>\n" +
                "</manifest>\n" +
                ""
                );
        assertEquals("com.android.unittest", info.getPackage());
        assertEquals("Theme", ResourceHelper.styleToTheme(info.getDefaultTheme(XLARGE)));

        Map<String, String> map = info.getActivityThemes();
        assertEquals(map.toString(), 1, map.size());
        assertNull(map.get("com.android.unittest.prefs.PrefsActivity"));
        assertEquals("@android:style/Theme.Dialog",
                map.get("com.android.unittest.app.IntroActivity"));
    }

    public void testGetActivityThemes5() throws Exception {
        ManifestInfo info = getManifestInfo(
                "<manifest xmlns:android='http://schemas.android.com/apk/res/android'\n" +
                "    package='com.android.unittest'" +
                "    android:theme='@style/NoBackground'>\n" +
                "    <application\n" +
                "        android:label='@string/app_name'\n" +
                "        android:name='.app.TestApp' android:icon='@drawable/app_icon'>\n" +
                "\n" +
                "        <activity\n" +
                "            android:name='.prefs.PrefsActivity'\n" +
                "            android:label='@string/prefs_title' />\n" +
                "\n" +
                "        <activity\n" +
                "            android:name='.app.IntroActivity'\n" +
                "            android:label='@string/intro_title'\n" +
                "            android:theme='@android:style/Theme.Dialog' />\n" +
                "    </application>\n" +
                "    <uses-sdk android:minSdkVersion='3' android:targetSdkVersion='4'/>\n" +
                "</manifest>\n" +
                ""
                );

        assertEquals("@style/NoBackground", info.getDefaultTheme(XLARGE));
        assertEquals("@style/NoBackground", info.getDefaultTheme(NORMAL));
        assertEquals("NoBackground", ResourceHelper.styleToTheme(info.getDefaultTheme(NORMAL)));

        Map<String, String> map = info.getActivityThemes();
        assertEquals(map.toString(), 1, map.size());
        assertNull(map.get("com.android.unittest.prefs.PrefsActivity"));
        assertEquals("@android:style/Theme.Dialog",
                map.get("com.android.unittest.app.IntroActivity"));

    }

    private ManifestInfo getManifestInfo(String manifestContents) throws Exception {
        InputStream bstream = new ByteArrayInputStream(
                manifestContents.getBytes("UTF-8")); //$NON-NLS-1$

        IFile file = getProject().getFile("AndroidManifest.xml");
        if (file.exists()) {
            file.setContents(bstream, IFile.FORCE, new NullProgressMonitor());
        } else {
            file.create(bstream, false /* force */, new NullProgressMonitor());
        }
        return ManifestInfo.get(getProject());
    }
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceHelperTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/resources/ResourceHelperTest.java
//Synthetic comment -- index 1a69847..a653ae2 100644

//Synthetic comment -- @@ -170,4 +170,10 @@
assertFalse(ResourceHelper.canCreateResource("@android:dimen/foo"));
assertFalse(ResourceHelper.canCreateResource("@android:color/foo"));
}

    public void testStyleToTheme() throws Exception {
        assertEquals("Foo", ResourceHelper.styleToTheme("Foo"));
        assertEquals("Theme", ResourceHelper.styleToTheme("@android:style/Theme"));
        assertEquals("LocalTheme", ResourceHelper.styleToTheme("@style/LocalTheme"));
    }
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 216d694..89a4cba 100644

//Synthetic comment -- @@ -28,13 +28,23 @@

public class ResourceResolver extends RenderResources {

    /** The constant {@code style/} */
public final static String REFERENCE_STYLE = ResourceType.STYLE.getName() + "/";
    /** The constant {@code @android:} */
public final static String PREFIX_ANDROID_RESOURCE_REF = "@android:";
    /** The constant {@code @} */
public final static String PREFIX_RESOURCE_REF = "@";
    /** The constant {@code ?android:} */
public final static String PREFIX_ANDROID_THEME_REF = "?android:";
    /** The constant {@code ?} */
public final static String PREFIX_THEME_REF = "?";
    /** The constant {@code android:} */
public final static String PREFIX_ANDROID = "android:";
    /** The constant {@code @style/} */
    public static final String PREFIX_STYLE = PREFIX_RESOURCE_REF + REFERENCE_STYLE;
    /** The constant {@code @android:style/} */
    public static final String PREFIX_ANDROID_STYLE = PREFIX_ANDROID_RESOURCE_REF
            + REFERENCE_STYLE;

private final Map<ResourceType, Map<String, ResourceValue>> mProjectResources;
private final Map<ResourceType, Map<String, ResourceValue>> mFrameworkResources;








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java
//Synthetic comment -- index f06b773..26fcd7b 100644

//Synthetic comment -- @@ -76,6 +76,7 @@
public final static String ATTRIBUTE_REQ_HARDKEYBOARD = "reqHardKeyboard";
public final static String ATTRIBUTE_REQ_KEYBOARDTYPE = "reqKeyboardType";
public final static String ATTRIBUTE_REQ_TOUCHSCREEN = "reqTouchScreen";
    public static final String ATTRIBUTE_THEME = "theme";

/**
* Returns an {@link IAbstractFile} object representing the manifest for the given project.







