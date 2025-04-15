/*SDK Manager: new --clear-cache flag for command-line.

(cherry picked from commit b3862702e7dd316a9ec981bfeac1600f53b307d2)

Change-Id:I09f4678a226784623b5b606ca1b957d7ac7c9768*/




//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/Main.java b/sdkmanager/app/src/com/android/sdkmanager/Main.java
//Synthetic comment -- index 9cd0dd5..bbfbdcc 100644

//Synthetic comment -- @@ -36,6 +36,8 @@
import com.android.sdklib.internal.project.ProjectCreator.OutputLevel;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.internal.repository.DownloadCache;
import com.android.sdklib.internal.repository.DownloadCache.Strategy;
import com.android.sdklib.internal.repository.packages.PlatformToolPackage;
import com.android.sdklib.internal.repository.packages.ToolPackage;
import com.android.sdklib.repository.SdkAddonConstants;
//Synthetic comment -- @@ -229,6 +231,13 @@
* Actually do an action...
*/
private void doAction() {

        if (mSdkCommandLine.hasClearCache()) {
            DownloadCache d = new DownloadCache(Strategy.SERVE_CACHE);
            d.clearCache();
            mSdkLog.printf("SDK Manager repository: manifest cache cleared.\n");
        }

String verb = mSdkCommandLine.getVerb();
String directObject = mSdkCommandLine.getDirectObject();









//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java b/sdkmanager/app/src/com/android/sdkmanager/SdkCommandLine.java
//Synthetic comment -- index b1325ee..56de068 100644

//Synthetic comment -- @@ -91,6 +91,7 @@
public static final String KEY_ALIAS        = "alias";                          //$NON-NLS-1$
public static final String KEY_STOREPASS    = "storepass";                      //$NON-NLS-1$
public static final String KEY_KEYPASS      = "keypass";                        //$NON-NLS-1$
    public static final String KEY_CLEAR_CACHE   = "clear-cache";                   //$NON-NLS-1$

/**
* Action definitions for SdkManager command line.
//Synthetic comment -- @@ -165,6 +166,12 @@

// The following defines the parameters of the actions defined in mAction.

        // --- generic actions that can work on any verb ---

        define(Mode.BOOLEAN, false,
                GLOBAL_FLAG_VERB, NO_VERB_OBJECT, ""/*shortName*/, KEY_CLEAR_CACHE, //$NON-NLS-1$
                "Clear the SDK Manager repository manifest cache.", false);         //$NON-NLS-1$

// --- list avds ---

define(Mode.BOOLEAN, false,
//Synthetic comment -- @@ -438,6 +445,12 @@

// -- some helpers for generic action flags

    /** Helper that returns true if --verbose was requested. */
    public boolean hasClearCache() {
        return
            ((Boolean) getValue(GLOBAL_FLAG_VERB, NO_VERB_OBJECT, KEY_CLEAR_CACHE)).booleanValue();
    }

/** Helper to retrieve the --path value. */
public String getParamLocationPath() {
return (String) getValue(null, null, KEY_PATH);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/util/CommandLineParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/util/CommandLineParser.java
//Synthetic comment -- index 3b4c682..530569f 100644

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.sdklib.util;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.sdklib.ISdkLog;

import java.util.ArrayList;
//Synthetic comment -- @@ -801,20 +803,22 @@
*
* @param mode The {@link Mode} for the argument.
* @param mandatory True if this argument is mandatory for this action.
         * @param verb The verb name. Never null. Can be {@link CommandLineParser#GLOBAL_FLAG_VERB}.
         * @param directObject The action name. Can be {@link CommandLineParser#NO_VERB_OBJECT}.
* @param shortName The one-letter short argument name. Can be empty but not null.
* @param longName The long argument name. Can be empty but not null.
* @param description The description. Cannot be null.
         * @param defaultValue The default value (or values), which depends on the selected
         *          {@link Mode}. Can be null.
*/
public Arg(Mode mode,
boolean mandatory,
                   @NonNull String verb,
                   @NonNull String directObject,
                   @NonNull String shortName,
                   @NonNull String longName,
                   @NonNull String description,
                   @Nullable Object defaultValue) {
mMode = mode;
mMandatory = mandatory;
mVerb = verb;
//Synthetic comment -- @@ -897,19 +901,23 @@
*
* @param mode The {@link Mode} for the argument.
* @param mandatory The argument is required (never if {@link Mode#BOOLEAN})
     * @param verb The verb name. Never null. Can be {@link CommandLineParser#GLOBAL_FLAG_VERB}.
     * @param directObject The action name. Can be {@link CommandLineParser#NO_VERB_OBJECT}.
* @param shortName The one-letter short argument name. Can be empty but not null.
* @param longName The long argument name. Can be empty but not null.
* @param description The description. Cannot be null.
     * @param defaultValue The default value (or values), which depends on the selected
     *          {@link Mode}.
*/
protected void define(Mode mode,
boolean mandatory,
            @NonNull String verb,
            @NonNull String directObject,
            @NonNull String shortName,
            @NonNull String longName,
            @NonNull String description,
            @Nullable Object defaultValue) {
        assert verb != null;
assert(!(mandatory && mode == Mode.BOOLEAN)); // a boolean mode cannot be mandatory

// We should always have at least a short or long name, ideally both but never none.







