/*Add ADT preference to disable skipping of PostComp

This change adds a preference option to ADT under
Android->Build to allow user control of whether
PostCompilation is skipped during a file save and
pushed back until launch or export.

Change-Id:Id877586bb11ae5af3fdb7fbc9c78c7f925115f07*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/build/builders/PostCompilerBuilder.java
//Synthetic comment -- index 2325293..1770df9 100644

//Synthetic comment -- @@ -255,7 +255,8 @@
// First thing we do is go through the resource delta to not
// lose it if we have to abort the build for any reason.
PostCompilerDeltaVisitor dv = null;
            if (args.containsKey(POST_C_REQUESTED)
                    && AdtPrefs.getPrefs().getBuildSkipPostCompileOnFileSave()) {
// Skip over flag setting
} else if (kind == FULL_BUILD) {
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project,
//Synthetic comment -- @@ -349,8 +350,9 @@
}

// Check to see if we're going to launch or export. If not, we can skip
            // the packaging and dexing process.
            if (!args.containsKey(POST_C_REQUESTED)
                    && AdtPrefs.getPrefs().getBuildSkipPostCompileOnFileSave()) {
AdtPlugin.printBuildToConsole(BuildVerbosity.VERBOSE, project,
Messages.Skip_Post_Compiler);
return allRefProjects;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/AdtPrefs.java
//Synthetic comment -- index ff39366..fc260d0 100644

//Synthetic comment -- @@ -34,6 +34,8 @@

public final static String PREFS_BUILD_FORCE_ERROR_ON_NATIVELIB_IN_JAR = AdtPlugin.PLUGIN_ID + ".forceErrorNativeLibInJar"; //$NON-NLS-1$

    public final static String PREFS_BUILD_SKIP_POST_COMPILE_ON_FILE_SAVE = AdtPlugin.PLUGIN_ID + ".skipPostCompileOnFileSave"; //$NON-NLS-1$

public final static String PREFS_BUILD_VERBOSITY = AdtPlugin.PLUGIN_ID + ".buildVerbosity"; //$NON-NLS-1$

public final static String PREFS_DEFAULT_DEBUG_KEYSTORE = AdtPlugin.PLUGIN_ID + ".defaultDebugKeyStore"; //$NON-NLS-1$
//Synthetic comment -- @@ -64,6 +66,7 @@

private boolean mBuildForceResResfresh = false;
private boolean mBuildForceErrorOnNativeLibInJar = true;
    private boolean mBuildSkipPostCompileOnFileSave = true;
private boolean mFormatXml = false;
private float mMonitorDensity = 0.f;
private String mPalette;
//Synthetic comment -- @@ -153,6 +156,10 @@
mBuildForceErrorOnNativeLibInJar = mStore.getBoolean(PREFS_BUILD_RES_AUTO_REFRESH);
}

        if (property == null || PREFS_BUILD_SKIP_POST_COMPILE_ON_FILE_SAVE.equals(property)) {
            mBuildSkipPostCompileOnFileSave = mStore.getBoolean(PREFS_BUILD_SKIP_POST_COMPILE_ON_FILE_SAVE);
        }

if (property == null || PREFS_MONITOR_DENSITY.equals(property)) {
mMonitorDensity = mStore.getFloat(PREFS_MONITOR_DENSITY);
}
//Synthetic comment -- @@ -190,6 +197,10 @@
return mBuildForceErrorOnNativeLibInJar;
}

    public boolean getBuildSkipPostCompileOnFileSave() {
        return mBuildSkipPostCompileOnFileSave;
    }

public String getPaletteModes() {
return mPalette;
}
//Synthetic comment -- @@ -220,6 +231,7 @@

store.setDefault(PREFS_BUILD_RES_AUTO_REFRESH, true);
store.setDefault(PREFS_BUILD_FORCE_ERROR_ON_NATIVELIB_IN_JAR, true);
        store.setDefault(PREFS_BUILD_SKIP_POST_COMPILE_ON_FILE_SAVE, true);

store.setDefault(PREFS_BUILD_VERBOSITY, BuildVerbosity.ALWAYS.name());









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/BuildPreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/preferences/BuildPreferencePage.java
//Synthetic comment -- index 2c538bd..3ad112a 100644

//Synthetic comment -- @@ -62,6 +62,10 @@
"Force error when external jars contain native libraries",
getFieldEditorParent()));

        addField(new BooleanFieldEditor(AdtPrefs.PREFS_BUILD_SKIP_POST_COMPILE_ON_FILE_SAVE,
                "Skip packaging and dexing until export or launch. (Speeds up automatic builds on file save)",
                getFieldEditorParent()));

RadioGroupFieldEditor rgfe = new RadioGroupFieldEditor(
AdtPrefs.PREFS_BUILD_VERBOSITY,
Messages.BuildPreferencePage_Build_Output, 1, new String[][] {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/project/ProjectHelper.java
//Synthetic comment -- index 9fce874..387dd5a 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.build.builders.PostCompilerBuilder;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.xml.ManifestData;
import com.android.util.Pair;
//Synthetic comment -- @@ -826,7 +827,9 @@
throws CoreException {
// Do an incremental build to pick up all the deltas
project.build(IncrementalProjectBuilder.INCREMENTAL_BUILD, monitor);
        // If the preferences indicate not to use post compiler optomization then the
        // incremental build will have done everything necessary
        if (fullBuild && AdtPrefs.getPrefs().getBuildSkipPostCompileOnFileSave()) {
// Create the map to pass to the PostC builder
Map<String, String> args = new TreeMap<String, String>();
args.put(PostCompilerBuilder.POST_C_REQUESTED, ""); //$NON-NLS-1$







