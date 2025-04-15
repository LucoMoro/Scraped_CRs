/*Minor refactor, moving ManifestData out of the parser class.

Also started added new data parsing (screen support)

Change-Id:I783e973fa16598a777eec4536746e6e5b9cb3e74*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/RenamePackageAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/actions/RenamePackageAction.java
//Synthetic comment -- index 76ae3c3..7e5cc8f 100644

//Synthetic comment -- @@ -21,7 +21,7 @@
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerBuilder.java
//Synthetic comment -- index fd2b78f..50660d2 100644

//Synthetic comment -- @@ -33,7 +33,7 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerDeltaVisitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/PreCompilerDeltaVisitor.java
//Synthetic comment -- index aedbd6e..e151698 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.io.IFileWrapper;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 2e6f8b1..f5d452e 100644

//Synthetic comment -- @@ -22,7 +22,7 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectClassLoader;
import com.android.ide.eclipse.adt.internal.resources.manager.ProjectResources;
import com.android.layoutlib.api.IProjectCallback;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IProject;









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiManifestPkgAttrNode.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/model/UiManifestPkgAttrNode.java
//Synthetic comment -- index 2d3953b..fa06fb3 100755

//Synthetic comment -- @@ -28,7 +28,7 @@
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.wizards.actions.NewProjectAction;
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectWizard;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.IJavaProject;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 1054712..a901b8a 100644

//Synthetic comment -- @@ -42,7 +42,7 @@
import com.android.sdklib.NullSdkLog;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdManager.AvdInfo;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/LaunchConfigDelegate.java
//Synthetic comment -- index fe52e0b..a573e6f 100644

//Synthetic comment -- @@ -22,8 +22,8 @@
import com.android.ide.eclipse.adt.internal.launch.AndroidLaunchConfiguration.TargetMode;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Activity;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/MainLaunchConfigTab.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/MainLaunchConfigTab.java
//Synthetic comment -- index cc73ed5..6ef204c 100644

//Synthetic comment -- @@ -20,8 +20,8 @@
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper.NonLibraryProjectOnlyFilter;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Activity;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java
//Synthetic comment -- index 677b190..c3054c2 100755

//Synthetic comment -- @@ -27,8 +27,8 @@
import com.android.ide.eclipse.adt.internal.launch.junit.runtime.AndroidJUnitLaunchInfo;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Instrumentation;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/InstrumentationRunnerValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/InstrumentationRunnerValidator.java
//Synthetic comment -- index e3f1a93..63824e5 100644

//Synthetic comment -- @@ -20,8 +20,8 @@
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.project.BaseProjectHelper;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Instrumentation;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidManifestHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/AndroidManifestHelper.java
//Synthetic comment -- index f8a9b4b..cafd268 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.xml.AndroidManifestParser;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java
//Synthetic comment -- index 21c0d8e..b71e53e 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AndroidConstants;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/refactorings/extractstring/ExtractStringRefactoring.java
//Synthetic comment -- index 2c43854..cbffd34 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceFolderType;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/CompiledResourcesMonitor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/CompiledResourcesMonitor.java
//Synthetic comment -- index f05cf30..1fa6eed 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
import com.android.ide.eclipse.adt.internal.resources.ResourceType;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IFileListener;
import com.android.ide.eclipse.adt.internal.resources.manager.GlobalProjectMonitor.IProjectListener;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarkerDelta;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ProjectCheckPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/export/ProjectCheckPage.java
//Synthetic comment -- index 7155ceb..d43bdc4 100644

//Synthetic comment -- @@ -24,7 +24,7 @@
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.project.ProjectChooserHelper.NonLibraryProjectOnlyFilter;
import com.android.ide.eclipse.adt.internal.wizards.export.ExportWizard.ExportWizardPage;
import com.android.sdklib.xml.ManifestData;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index e74f679..b424c49 100644

//Synthetic comment -- @@ -33,8 +33,8 @@
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Activity;
import com.android.sdkuilib.internal.widgets.SdkTargetSelector;

import org.eclipse.core.filesystem.URIUtil;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewTestProjectCreationPage.java
//Synthetic comment -- index 608b002..49aceb5 100755

//Synthetic comment -- @@ -31,7 +31,7 @@
import com.android.ide.eclipse.adt.internal.wizards.newproject.NewProjectCreationPage.MainInfo;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;
import com.android.sdkuilib.internal.widgets.SdkTargetSelector;

import org.eclipse.core.filesystem.URIUtil;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/project/AndroidManifestParserTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/unittests/com/android/ide/eclipse/adt/internal/project/AndroidManifestParserTest.java
//Synthetic comment -- index e8571ee..c52fbb2 100644

//Synthetic comment -- @@ -18,8 +18,7 @@

import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.tests.AdtTestData;
import com.android.sdklib.xml.ManifestData;

import junit.framework.TestCase;

//Synthetic comment -- @@ -37,6 +36,7 @@
private static final String TESTAPP_XML = TESTDATA_PATH +
"AndroidManifest-testapp.xml";  //$NON-NLS-1$
private static final String PACKAGE_NAME =  "com.android.testapp"; //$NON-NLS-1$
    private static final Integer VERSION_CODE = 42;
private static final String ACTIVITY_NAME = "com.android.testapp.MainActivity"; //$NON-NLS-1$
private static final String LIBRARY_NAME = "android.test.runner"; //$NON-NLS-1$
private static final String INSTRUMENTATION_NAME = "android.test.InstrumentationTestRunner"; //$NON-NLS-1$
//Synthetic comment -- @@ -67,9 +67,18 @@
assertEquals(PACKAGE_NAME, mManifestTestApp.getPackage());
}

    public void testGetVersionCode() {
        assertEquals(VERSION_CODE, mManifestTestApp.getVersionCode());
        assertEquals(null, mManifestInstrumentation.getVersionCode());
    }

    public void testMinSdkVersion() {
        assertEquals("7", mManifestTestApp.getApiLevelRequirement());
    }

public void testGetActivities() {
assertEquals(1, mManifestTestApp.getActivities().length);
        ManifestData.Activity activity = mManifestTestApp.getActivities()[0];
assertEquals(ACTIVITY_NAME, activity.getName());
assertTrue(activity.hasAction());
assertTrue(activity.isHomeActivity());
//Synthetic comment -- @@ -78,13 +87,13 @@
}

public void testGetLauncherActivity() {
        ManifestData.Activity activity = mManifestTestApp.getLauncherActivity();
assertEquals(ACTIVITY_NAME, activity.getName());
assertTrue(activity.hasAction());
assertTrue(activity.isHomeActivity());
}

    private void assertEquals(ManifestData.Activity lhs, ManifestData.Activity rhs) {
assertTrue(lhs == rhs || (lhs != null && rhs != null));
if (lhs != null && rhs != null) {
assertEquals(lhs.getName(),        rhs.getName());








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifest.java
//Synthetic comment -- index 12d0ff6..47340eb 100644

//Synthetic comment -- @@ -34,27 +34,33 @@
*/
public final class AndroidManifest {

    public final static String NODE_MANIFEST = "manifest";
    public final static String NODE_APPLICATION = "application";
    public final static String NODE_ACTIVITY = "activity";
    public final static String NODE_SERVICE = "service";
    public final static String NODE_RECEIVER = "receiver";
    public final static String NODE_PROVIDER = "provider";
    public final static String NODE_INTENT = "intent-filter";
    public final static String NODE_ACTION = "action";
    public final static String NODE_CATEGORY = "category";
    public final static String NODE_USES_SDK = "uses-sdk";
    public final static String NODE_INSTRUMENTATION = "instrumentation";
    public final static String NODE_USES_LIBRARY = "uses-library";
    public static final String NODE_SUPPORTS_SCREENS = "supports-screens";

    public final static String ATTRIBUTE_PACKAGE = "package";
    public final static String ATTRIBUTE_VERSIONCODE = "versionCode";
    public final static String ATTRIBUTE_NAME = "name";
    public final static String ATTRIBUTE_PROCESS = "process";
    public final static String ATTRIBUTE_DEBUGGABLE = "debuggable";
    public final static String ATTRIBUTE_MIN_SDK_VERSION = "minSdkVersion";
    public final static String ATTRIBUTE_TARGET_PACKAGE = "targetPackage";
    public final static String ATTRIBUTE_EXPORTED = "exported";
    public final static String ATTRIBUTE_RESIZABLE = "resizeable";
    public final static String ATTRIBUTE_ANYDENSITY = "anyDensity";
    public final static String ATTRIBUTE_SMALLSCREENS = "smallScreens";
    public final static String ATTRIBUTE_NORMALSCREENS = "normalScreens";
    public final static String ATTRIBUTE_LARGESCREENS = "largeScreens";

/**
* Returns the package for a given project.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java
//Synthetic comment -- index 5c89cc6..2330020 100644

//Synthetic comment -- @@ -20,6 +20,9 @@
import com.android.sdklib.io.IAbstractFile;
import com.android.sdklib.io.IAbstractFolder;
import com.android.sdklib.io.StreamException;
import com.android.sdklib.xml.ManifestData.Activity;
import com.android.sdklib.xml.ManifestData.Instrumentation;
import com.android.sdklib.xml.ManifestData.SupportsScreens;
import com.sun.rowset.internal.XmlErrorHandler;

import org.xml.sax.Attributes;
//Synthetic comment -- @@ -32,9 +35,6 @@

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
//Synthetic comment -- @@ -51,187 +51,6 @@
private final static String ACTION_MAIN = "android.intent.action.MAIN"; //$NON-NLS-1$
private final static String CATEGORY_LAUNCHER = "android.intent.category.LAUNCHER"; //$NON-NLS-1$

public interface ManifestErrorHandler extends ErrorHandler {
/**
* Handles a parsing error and an optional line number.
//Synthetic comment -- @@ -286,7 +105,6 @@
mErrorHandler = errorHandler;
}

/* (non-Javadoc)
* @see org.xml.sax.helpers.DefaultHandler#setDocumentLocator(org.xml.sax.Locator)
*/
//Synthetic comment -- @@ -318,6 +136,17 @@
mManifestData.mPackage = getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_PACKAGE,
false /* hasNamespace */);

                                // and the versionCode
                                String tmp = getAttributeValue(attributes,
                                        AndroidManifest.ATTRIBUTE_VERSIONCODE, true);
                                if (tmp != null) {
                                    try {
                                        mManifestData.mVersionCode = Integer.getInteger(tmp);
                                    } catch (NumberFormatException e) {
                                        // keep null in the field.
                                    }
                                }
mValidLevel++;
}
break;
//Synthetic comment -- @@ -344,6 +173,8 @@
true /* hasNamespace */);
} else if (AndroidManifest.NODE_INSTRUMENTATION.equals(localName)) {
processInstrumentationNode(attributes);
                            } else if (AndroidManifest.NODE_SUPPORTS_SCREENS.equals(localName)) {
                                processSupportsScreensNode(attributes);
}
break;
case LEVEL_ACTIVITY:
//Synthetic comment -- @@ -549,9 +380,8 @@
}

/**
         * Processes the instrumentation node.
         * @param attributes the attributes for the instrumentation node.
*/
private void processInstrumentationNode(Attributes attributes) {
// lets get the class name, and check it if required.
//Synthetic comment -- @@ -574,6 +404,31 @@
}

/**
         * Processes the supports-screens node.
         * @param attributes the attributes for the supports-screens node.
         */
        private void processSupportsScreensNode(Attributes attributes) {
            mManifestData.mSupportsScreens = new SupportsScreens();

            mManifestData.mSupportsScreens.mResizeable = Boolean.valueOf(
                    getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_RESIZABLE, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mAnyDensity = Boolean.valueOf(
                    getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_ANYDENSITY, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mSmallScreens = Boolean.valueOf(
                    getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_SMALLSCREENS, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mNormalScreens = Boolean.valueOf(
                    getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_NORMALSCREENS, true /*hasNamespace*/));
            mManifestData.mSupportsScreens.mLargeScreens = Boolean.valueOf(
                    getAttributeValue(attributes,
                            AndroidManifest.ATTRIBUTE_LARGESCREENS, true /*hasNamespace*/));
        }


        /**
* Searches through the attributes list for a particular one and returns its value.
* @param attributes the attribute list to search through
* @param attributeName the name of the attribute to look for.








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/ManifestData.java
new file mode 100644
//Synthetic comment -- index 0000000..3a9d034

//Synthetic comment -- @@ -0,0 +1,221 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdklib.xml;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

/**
 * Class containing the manifest info obtained during the parsing.
 */
public class ManifestData {
    /** Application package */
    String mPackage;
    /** Application version Code, null if the attribute is not present. */
    Integer mVersionCode = null;
    /** List of all activities */
    final ArrayList<Activity> mActivities = new ArrayList<Activity>();
    /** Launcher activity */
    Activity mLauncherActivity = null;
    /** list of process names declared by the manifest */
    Set<String> mProcesses = null;
    /** debuggable attribute value. If null, the attribute is not present. */
    Boolean mDebuggable = null;
    /** API level requirement. if null the attribute was not present. */
    String mApiLevelRequirement = null;
    /** List of all instrumentations declared by the manifest */
    final ArrayList<Instrumentation> mInstrumentations =
        new ArrayList<Instrumentation>();
    /** List of all libraries in use declared by the manifest */
    final ArrayList<String> mLibraries = new ArrayList<String>();

    SupportsScreens mSupportsScreens;

    /**
     * Instrumentation info obtained from manifest
     */
    public static class Instrumentation {
        private final String mName;
        private final String mTargetPackage;

        Instrumentation(String name, String targetPackage) {
            mName = name;
            mTargetPackage = targetPackage;
        }

        /**
         * Returns the fully qualified instrumentation class name
         */
        public String getName() {
            return mName;
        }

        /**
         * Returns the Android app package that is the target of this instrumentation
         */
        public String getTargetPackage() {
            return mTargetPackage;
        }
    }

    /**
     * Activity info obtained from the manifest.
     */
    public static class Activity {
        private final String mName;
        private final boolean mIsExported;
        private boolean mHasAction = false;
        private boolean mHasMainAction = false;
        private boolean mHasLauncherCategory = false;

        public Activity(String name, boolean exported) {
            mName = name;
            mIsExported = exported;
        }

        public String getName() {
            return mName;
        }

        public boolean isExported() {
            return mIsExported;
        }

        public boolean hasAction() {
            return mHasAction;
        }

        public boolean isHomeActivity() {
            return mHasMainAction && mHasLauncherCategory;
        }

        void setHasAction(boolean hasAction) {
            mHasAction = hasAction;
        }

        /** If the activity doesn't yet have a filter set for the launcher, this resets both
         * flags. This is to handle multiple intent-filters where one could have the valid
         * action, and another one of the valid category.
         */
        void resetIntentFilter() {
            if (isHomeActivity() == false) {
                mHasMainAction = mHasLauncherCategory = false;
            }
        }

        void setHasMainAction(boolean hasMainAction) {
            mHasMainAction = hasMainAction;
        }

        void setHasLauncherCategory(boolean hasLauncherCategory) {
            mHasLauncherCategory = hasLauncherCategory;
        }
    }

    public static class SupportsScreens {
        Boolean mResizeable;
        Boolean mSmallScreens;
        Boolean mAnyDensity;
        Boolean mLargeScreens;
        Boolean mNormalScreens;
    }

    /**
     * Returns the package defined in the manifest, if found.
     * @return The package name or null if not found.
     */
    public String getPackage() {
        return mPackage;
    }

    /**
     * Returns the versionCode value defined in the manifest, if found, null otherwise.
     * @return the versionCode or null if not found.
     */
    public Integer getVersionCode() {
        return mVersionCode;
    }

    /**
     * Returns the list of activities found in the manifest.
     * @return An array of fully qualified class names, or empty if no activity were found.
     */
    public Activity[] getActivities() {
        return mActivities.toArray(new Activity[mActivities.size()]);
    }

    /**
     * Returns the name of one activity found in the manifest, that is configured to show
     * up in the HOME screen.
     * @return the fully qualified name of a HOME activity or null if none were found.
     */
    public Activity getLauncherActivity() {
        return mLauncherActivity;
    }

    /**
     * Returns the list of process names declared by the manifest.
     */
    public String[] getProcesses() {
        if (mProcesses != null) {
            return mProcesses.toArray(new String[mProcesses.size()]);
        }

        return new String[0];
    }

    /**
     * Returns the <code>debuggable</code> attribute value or null if it is not set.
     */
    public Boolean getDebuggable() {
        return mDebuggable;
    }

    /**
     * Returns the <code>minSdkVersion</code> attribute, or null if it's not set.
     */
    public String getApiLevelRequirement() {
        return mApiLevelRequirement;
    }

    /**
     * Returns the list of instrumentations found in the manifest.
     * @return An array of {@link Instrumentation}, or empty if no instrumentations were
     * found.
     */
    public Instrumentation[] getInstrumentations() {
        return mInstrumentations.toArray(new Instrumentation[mInstrumentations.size()]);
    }

    /**
     * Returns the list of libraries in use found in the manifest.
     * @return An array of library names, or empty if no libraries were found.
     */
    public String[] getUsesLibraries() {
        return mLibraries.toArray(new String[mLibraries.size()]);
    }

    void addProcessName(String processName) {
        if (mProcesses == null) {
            mProcesses = new TreeSet<String>();
        }

        mProcesses.add(processName);
    }

}







