/*Additional fixes to filter out library project lint warnings

Lint already has the concept of whether a project should report lint
warnings or not; a project can be included in analysis (e.g. for
unused resource checks), but can filter out any warnings local to that
project. This is useful when you are using a library, but don't want
to see errors from that library which may not be under your control.

The way this is handled from the command line is that lint will only
report errors for projects you've referenced; e.g. if you run "lint
/foo/bar" this will show errors in /foo/bar, but exclude errors found
in the library project /foo/bar/../library".

However, there were several lint checks which needed additional fixes
for this, because (like the unused error detector) they gather data
from multiple projects and process and report in the after-project
hook.

In addition, inside Eclipse, the UI would always automatically include
libraries. This is sometimes what you want, and sometimes not what you
want, so this CL adds a new toggle menu item to the lint action menu,
"Skip Library Project Dependencies", off by default, which you can
select to make lint runs from eclipse include or exclude library
projects in the report.

Change-Id:Idf3167e818931525e0dd7661f5cdf3a3e69b6522*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintRunner.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/EclipseLintRunner.java
//Synthetic comment -- index d69412b..43cd48d 100644

//Synthetic comment -- @@ -127,7 +127,9 @@
@Nullable IResource source,
boolean show) {
if (resources != null && !resources.isEmpty()) {
            if (!AdtPrefs.getPrefs().getSkipLibrariesFromLint()) {
                resources = addLibraries(resources);
            }

cancelCurrentJobs(false);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/RunLintAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/lint/RunLintAction.java
//Synthetic comment -- index a0d414e..e468a5a 100644

//Synthetic comment -- @@ -21,6 +21,7 @@
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.IconFactory;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.tools.lint.detector.api.LintUtils;

import org.eclipse.core.resources.IFile;
//Synthetic comment -- @@ -110,7 +111,8 @@

IconFactory iconFactory = IconFactory.getInstance();
ImageDescriptor allIcon = iconFactory.getImageDescriptor("lintrun"); //$NON-NLS-1$
        LintMenuAction allAction = new LintMenuAction("Check All Projects", allIcon,
                ACTION_RUN, null);

addAction(allAction);
addSeparator();
//Synthetic comment -- @@ -121,7 +123,7 @@
IProject p = project.getProject();
ImageDescriptor icon = ImageDescriptor.createFromImage(provider.getImage(p));
String label = String.format("Check %1$s", p.getName());
            LintMenuAction projectAction = new LintMenuAction(label, icon, ACTION_RUN, p);
addAction(projectAction);
}

//Synthetic comment -- @@ -131,7 +133,8 @@
// Currently only supported for XML files
if (file != null && LintUtils.endsWith(file.getName(), DOT_XML)) {
ImageDescriptor icon = ImageDescriptor.createFromImage(provider.getImage(file));
                IAction fileAction = new LintMenuAction("Check Current File", icon, ACTION_RUN,
                        file);

addSeparator();
addAction(fileAction);
//Synthetic comment -- @@ -140,10 +143,17 @@

ISharedImages images = PlatformUI.getWorkbench().getSharedImages();
ImageDescriptor clear = images.getImageDescriptor(ISharedImages.IMG_ELCL_REMOVEALL);
        LintMenuAction clearAction = new LintMenuAction("Clear Lint Warnings", clear, ACTION_CLEAR,
                null);
addSeparator();
addAction(clearAction);

        LintMenuAction excludeAction = new LintMenuAction("Skip Library Project Dependencies",
                allIcon, ACTION_TOGGLE_EXCLUDE, null);
        addSeparator();
        addAction(excludeAction);
        excludeAction.setChecked(AdtPrefs.getPrefs().getSkipLibrariesFromLint());

return mMenu;
}

//Synthetic comment -- @@ -161,32 +171,44 @@
return null;
}

    private static final int ACTION_RUN = 1;
    private static final int ACTION_CLEAR = 2;
    private static final int ACTION_TOGGLE_EXCLUDE = 3;

/**
* Actions in the pulldown context menu: run lint or clear lint markers on
* the given resource
*/
private static class LintMenuAction extends Action {
private final IResource mResource;
        private final int mAction;

/**
* Creates a new context menu action
*
* @param text the label
* @param descriptor the icon
         * @param action the action to run: run lint, clear, or toggle exclude libraries
* @param resource the resource to check or clear markers for, where
*            null means all projects
*/
        private LintMenuAction(String text, ImageDescriptor descriptor, int action,
IResource resource) {
            super(text, action == ACTION_TOGGLE_EXCLUDE ? AS_CHECK_BOX : AS_PUSH_BUTTON);
            if (descriptor != null) {
                setImageDescriptor(descriptor);
            }
            mAction = action;
mResource = resource;
}

@Override
public void run() {
            if (mAction == ACTION_TOGGLE_EXCLUDE) {
                AdtPrefs prefs = AdtPrefs.getPrefs();
                prefs.setSkipLibrariesFromLint(!prefs.getSkipLibrariesFromLint());
                return;
            }
List<IResource> resources = new ArrayList<IResource>();
if (mResource == null) {
// All projects
//Synthetic comment -- @@ -198,9 +220,10 @@
resources.add(mResource);
}
EclipseLintRunner.cancelCurrentJobs(false);
            if (mAction == ACTION_CLEAR) {
EclipseLintClient.clearMarkers(resources);
} else {
                assert mAction == ACTION_RUN;
EclipseLintRunner.startLint(resources, null, null, false /*fatalOnly*/,
true /*show*/);
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java
//Synthetic comment -- index 002dc63..89c19a0 100644

//Synthetic comment -- @@ -73,6 +73,7 @@
public final static String PREFS_FIX_LEGACY_EDITORS = AdtPlugin.PLUGIN_ID + ".fixLegacyEditors"; //$NON-NLS-1$
public final static String PREFS_SHARED_LAYOUT_EDITOR = AdtPlugin.PLUGIN_ID + ".sharedLayoutEditor"; //$NON-NLS-1$
public final static String PREFS_PREVIEWS = AdtPlugin.PLUGIN_ID + ".previews"; //$NON-NLS-1$
    public final static String PREFS_SKIP_LINT_LIBS = AdtPlugin.PLUGIN_ID + ".skipLintLibs"; //$NON-NLS-1$
public final static String PREFS_AUTO_PICK_TARGET = AdtPlugin.PLUGIN_ID + ".autoPickTarget"; //$NON-NLS-1$

/** singleton instance */
//Synthetic comment -- @@ -276,6 +277,10 @@
}
}
}

        if (property == null || PREFS_SKIP_LINT_LIBS.equals(property)) {
            mSkipLibrariesFromLint = mStore.getBoolean(PREFS_SKIP_LINT_LIBS);
        }
}

/**
//Synthetic comment -- @@ -515,6 +520,7 @@
//store.setDefault(PREVS_REMOVE_EMPTY_LINES, false);
//store.setDefault(PREFS_FORMAT_ON_SAVE, false);
//store.setDefault(PREFS_SHARED_LAYOUT_EDITOR, false);
        //store.setDefault(PREFS_SKIP_LINT_LIBS, false);

try {
store.setDefault(PREFS_DEFAULT_DEBUG_KEYSTORE,
//Synthetic comment -- @@ -610,4 +616,32 @@
store.setValue(PREFS_AUTO_PICK_TARGET, autoPick);
}
}

    private boolean mSkipLibrariesFromLint;

    /**
     * Sets whether libraries should be excluded when running lint on a project
     *
     * @param exclude if true, exclude library projects
     */
    public void setSkipLibrariesFromLint(boolean exclude) {
        if (exclude != mSkipLibrariesFromLint) {
            mSkipLibrariesFromLint = exclude;
            IPreferenceStore store = AdtPlugin.getDefault().getPreferenceStore();
            if (exclude) {
                store.setValue(PREFS_SKIP_LINT_LIBS, true);
            } else {
                store.setToDefault(PREFS_SKIP_LINT_LIBS);
            }
        }
    }

    /**
     * Returns whether libraries should be excluded when running lint on a project
     *
     * @return if true, exclude library projects
     */
    public boolean getSkipLibrariesFromLint() {
        return mSkipLibrariesFromLint;
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ArraySizeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ArraySizeDetector.java
//Synthetic comment -- index 8898507..4f63913 100644

//Synthetic comment -- @@ -80,8 +80,6 @@
ArraySizeDetector.class,
Scope.ALL_RESOURCES_SCOPE);

private Multimap<File, Pair<String, Integer>> mFileToArrayCount;

/** Locations for each array name. Populated during phase 2, if necessary */
//Synthetic comment -- @@ -238,8 +236,10 @@
} else {
String name = attribute.getValue();
if (phase == 1) {
                if (context.getProject().getReportIssues()) {
                    int childCount = LintUtils.getChildCount(element);
                    mFileToArrayCount.put(context.file, Pair.of(name, childCount));
                }
} else {
assert phase == 2;
if (mLocations.containsKey(name)) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ButtonDetector.java
//Synthetic comment -- index da274f6..6ac4cad 100644

//Synthetic comment -- @@ -366,6 +366,11 @@
return;
}

        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

if (mApplicableResources == null) {
mApplicableResources = new HashSet<String>();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateIdDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateIdDetector.java
//Synthetic comment -- index 720845d..48e8661 100644

//Synthetic comment -- @@ -20,9 +20,9 @@
import static com.android.SdkConstants.ATTR_ID;
import static com.android.SdkConstants.ATTR_LAYOUT;
import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.NEW_ID_PREFIX;
import static com.android.SdkConstants.VIEW_INCLUDE;

import com.android.annotations.NonNull;
import com.android.resources.ResourceFolderType;
//Synthetic comment -- @@ -211,6 +211,11 @@
layout = layout.substring(LAYOUT_RESOURCE_PREFIX.length());

if (context.getPhase() == 1) {
                if (!context.getProject().getReportIssues()) {
                    // If this is a library project not being analyzed, ignore it
                    return;
                }

List<String> to = mIncludes.get(context.file);
if (to == null) {
to = new ArrayList<String>();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/IconDetector.java
//Synthetic comment -- index 8ea2e3e..3e7e2e8 100644

//Synthetic comment -- @@ -335,6 +335,11 @@

@Override
public void afterCheckLibraryProject(@NonNull Context context) {
        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

checkResourceFolder(context, context.getProject());
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MergeRootFrameLayoutDetector.java
//Synthetic comment -- index 8769893..1b76c03 100644

//Synthetic comment -- @@ -23,9 +23,9 @@
import static com.android.SdkConstants.ATTR_LAYOUT_GRAVITY;
import static com.android.SdkConstants.DOT_JAVA;
import static com.android.SdkConstants.FRAME_LAYOUT;
import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.R_LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.VIEW_INCLUDE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
//Synthetic comment -- @@ -167,6 +167,11 @@
Handle handle = context.parser.createLocationHandle(context, element);
handle.setClientData(element);

                if (!context.getProject().getReportIssues()) {
                    // If this is a library project not being analyzed, ignore it
                    return;
                }

if (mPending == null) {
mPending = new ArrayList<Pair<String,Handle>>();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/MissingClassDetector.java
//Synthetic comment -- index 336f820..fc9449a 100644

//Synthetic comment -- @@ -170,6 +170,11 @@
return;
}

        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

if (mReferencedClasses == null) {
mReferencedClasses = Maps.newHashMapWithExpectedSize(16);
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/ObsoleteLayoutParamsDetector.java
//Synthetic comment -- index 08d53a3..24eb7ba 100644

//Synthetic comment -- @@ -55,12 +55,12 @@
import static com.android.SdkConstants.ATTR_LAYOUT_Y;
import static com.android.SdkConstants.DOT_XML;
import static com.android.SdkConstants.GRID_LAYOUT;
import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.LINEAR_LAYOUT;
import static com.android.SdkConstants.RELATIVE_LAYOUT;
import static com.android.SdkConstants.TABLE_ROW;
import static com.android.SdkConstants.VIEW_INCLUDE;
import static com.android.SdkConstants.VIEW_MERGE;
import static com.android.SdkConstants.VIEW_TAG;

import com.android.annotations.NonNull;
//Synthetic comment -- @@ -306,6 +306,11 @@
if (parent.getNodeType() == Node.ELEMENT_NODE) {
String tag = parent.getNodeName();
if (tag.indexOf('.') == -1 && !tag.equals(VIEW_MERGE)) {
                    if (!context.getProject().getReportIssues()) {
                        // If this is a library project not being analyzed, ignore it
                        return;
                    }

if (mIncludes == null) {
mIncludes = new HashMap<String, List<Pair<File, String>>>();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OnClickDetector.java
//Synthetic comment -- index 24dcf02..5b2f3f7 100644

//Synthetic comment -- @@ -134,6 +134,11 @@
context.report(ISSUE, attribute, context.getLocation(attribute),
"There should be no whitespace around attribute values", null);
} else if (!value.startsWith(PREFIX_RESOURCE_REF)) { // Not resolved
            if (!context.getProject().getReportIssues()) {
                // If this is a library project not being analyzed, ignore it
                return;
            }

if (mNames == null) {
mNames = new HashMap<String, Location.Handle>();
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverdrawDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverdrawDetector.java
//Synthetic comment -- index 2c3999d..4994271 100644

//Synthetic comment -- @@ -289,6 +289,11 @@
return;
}

            if (!context.getProject().getReportIssues()) {
                // If this is a library project not being analyzed, ignore it
                return;
            }

Location location = context.getLocation(attribute);
location.setClientData(attribute);
if (mRootAttributes == null) {
//Synthetic comment -- @@ -472,6 +477,9 @@

@Override
public AstVisitor createJavaVisitor(@NonNull JavaContext context) {
        if (!context.getProject().getReportIssues()) {
            return null;
        }
return new OverdrawVisitor();
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverrideDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/OverrideDetector.java
//Synthetic comment -- index 041536f..26f8d38 100644

//Synthetic comment -- @@ -215,6 +215,11 @@
@SuppressWarnings("rawtypes") // ASM4 API
@Override
public void checkClass(@NonNull ClassContext context, @NonNull ClassNode classNode) {
        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

List methodList = classNode.methods;
if (context.getPhase() == 1) {
for (Object m : methodList) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/PrivateKeyDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/PrivateKeyDetector.java
//Synthetic comment -- index b1c0bd7..ba7c29f 100644

//Synthetic comment -- @@ -16,9 +16,6 @@

package com.android.tools.lint.checks;

import com.android.annotations.NonNull;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -30,6 +27,8 @@
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;
//Synthetic comment -- @@ -98,6 +97,11 @@

@Override
public void afterCheckProject(@NonNull Context context) {
        if (!context.getProject().getReportIssues()) {
            // If this is a library project not being analyzed, ignore it
            return;
        }

Project project = context.getProject();
File projectFolder = project.getDir();

//Synthetic comment -- @@ -105,7 +109,7 @@
checkFolder(context, new File(projectFolder, "assets"));

for (File srcFolder : project.getJavaSourceFolders()) {
            checkFolder(context, srcFolder);
}
}









//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/RequiredAttributeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/RequiredAttributeDetector.java
//Synthetic comment -- index 9340d3a..af49d6c 100644

//Synthetic comment -- @@ -353,6 +353,11 @@
return;
}

                if (!context.getProject().getReportIssues()) {
                    // If this is a library project not being analyzed, ignore it
                    return;
                }

boolean certain = true;
boolean isRoot = isRootElement(element);
if (isRoot || isRootElement(element.getParentNode())








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/StringFormatDetector.java
//Synthetic comment -- index f0c499c..90ae0cf 100644

//Synthetic comment -- @@ -291,6 +291,11 @@
}

if (found && name != null) {
            if (!context.getProject().getReportIssues()) {
                // If this is a library project not being analyzed, ignore it
                return;
            }

// Record it for analysis when seen in Java code
if (mFormatStrings == null) {
mFormatStrings = new HashMap<String, List<Pair<Handle,String>>>();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WakelockDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WakelockDetector.java
//Synthetic comment -- index bddee0f..1642d55 100644

//Synthetic comment -- @@ -111,6 +111,11 @@
public void checkCall(@NonNull ClassContext context, @NonNull ClassNode classNode,
@NonNull MethodNode method, @NonNull MethodInsnNode call) {
if (call.owner.equals(WAKELOCK_OWNER)) {
            if (!context.getProject().getReportIssues()) {
                // If this is a library project not being analyzed, ignore it
                return;
            }

String name = call.name;
if (name.equals(ACQUIRE_METHOD)) {
mHasAcquire = true;








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongIdDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/WrongIdDetector.java
//Synthetic comment -- index 7efbfbf..e8a1bbf 100644

//Synthetic comment -- @@ -156,6 +156,11 @@
@Override
public void afterCheckFile(@NonNull Context context) {
if (mRelativeLayouts != null) {
            if (!context.getProject().getReportIssues()) {
                // If this is a library project not being analyzed, ignore it
                return;
            }

for (Element layout : mRelativeLayouts) {
NodeList children = layout.getChildNodes();
for (int j = 0, childCount = children.getLength(); j < childCount; j++) {







