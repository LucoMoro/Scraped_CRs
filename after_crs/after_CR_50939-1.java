/*Move SDK version name utilities out of lint and into sdk_common

This CL moves the getBuildCode(int api) utility method out of lint
and into sdk_common. It also moves the getAndroidName(int api) method
from ADT into sdk_common, and the associated HIGHEST_KNOWN_VERSION
constant out of SdkConstants and into the same new utility class,
SdkVersions.

Change-Id:Icc07cb8e827dbec8a14b16644b314fd8e9bd0605*/




//Synthetic comment -- diff --git a/common/src/main/java/com/android/SdkConstants.java b/common/src/main/java/com/android/SdkConstants.java
//Synthetic comment -- index 16e05a1..2ba5f0f 100644

//Synthetic comment -- @@ -1171,13 +1171,4 @@
public static final String ATTR_EMS = "ems"; //$NON-NLS-1$

public static final String VALUE_HORIZONTAL = "horizontal"; //$NON-NLS-1$
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/client/api/LintClient.java
//Synthetic comment -- index ea1aba1..e506fa4 100644

//Synthetic comment -- @@ -23,9 +23,9 @@
import static com.android.SdkConstants.RES_FOLDER;
import static com.android.SdkConstants.SRC_FOLDER;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.sdk.SdkVersionInfo;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkManager;
import com.android.tools.lint.detector.api.Context;
//Synthetic comment -- @@ -636,7 +636,7 @@
* @return the highest known API level
*/
public int getHighestKnownApiLevel() {
        int max = SdkVersionInfo.HIGHEST_KNOWN_API;

for (IAndroidTarget target : getTargets()) {
if (target.isPlatform()) {








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/LintUtils.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/LintUtils.java
//Synthetic comment -- index 9dec569..dfb3f38 100644

//Synthetic comment -- @@ -759,40 +759,4 @@

return imported;
}
}








//Synthetic comment -- diff --git a/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java b/lint/libs/lint_api/src/main/java/com/android/tools/lint/detector/api/Project.java
//Synthetic comment -- index 968b9b1..cc16c1d 100644

//Synthetic comment -- @@ -29,9 +29,9 @@
import static com.android.SdkConstants.TAG_USES_SDK;
import static com.android.SdkConstants.VALUE_TRUE;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.sdk.SdkVersionInfo;
import com.android.tools.lint.client.api.CircularDependencyException;
import com.android.tools.lint.client.api.Configuration;
import com.android.tools.lint.client.api.LintClient;
//Synthetic comment -- @@ -359,7 +359,7 @@

if (folders.size() == 1 && isAospFrameworksProject(mDir)) {
// No manifest file for this project: just init the manifest values here
            mMinSdk = mTargetSdk = SdkVersionInfo.HIGHEST_KNOWN_API;
File folder = new File(folders.get(0), RES_FOLDER);
if (!folder.exists()) {
folders = Collections.emptyList();








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/ApiDetector.java
//Synthetic comment -- index f9f2047..2546c02 100644

//Synthetic comment -- @@ -36,9 +36,9 @@
import static com.android.tools.lint.detector.api.Location.SearchDirection.FORWARD;
import static com.android.tools.lint.detector.api.Location.SearchDirection.NEAREST;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.ide.common.sdk.SdkVersionInfo;
import com.android.resources.ResourceFolderType;
import com.android.tools.lint.client.api.LintDriver;
import com.android.tools.lint.detector.api.Category;
//Synthetic comment -- @@ -966,8 +966,8 @@
}
}

                for (int api = 1; api <= SdkVersionInfo.HIGHEST_KNOWN_API; api++) {
                    String code = SdkVersionInfo.getBuildCode(api);
if (code != null && code.equalsIgnoreCase(targetApi)) {
return api;
}
//Synthetic comment -- @@ -1557,8 +1557,8 @@
}

private static int codeNameToApi(String codename) {
        for (int api = 1; api <= SdkVersionInfo.HIGHEST_KNOWN_API; api++) {
            String code = SdkVersionInfo.getBuildCode(api);
if (code != null && code.equalsIgnoreCase(codename)) {
return api;
}








//Synthetic comment -- diff --git a/sdk_common/src/main/java/com/android/ide/common/sdk/SdkVersionInfo.java b/sdk_common/src/main/java/com/android/ide/common/sdk/SdkVersionInfo.java
new file mode 100644
//Synthetic comment -- index 0000000..f62e1a2

//Synthetic comment -- @@ -0,0 +1,103 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
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
package com.android.ide.common.sdk;

import com.android.annotations.Nullable;

/** Information about available SDK Versions */
public class SdkVersionInfo {
    /**
     * The highest known API level. Note that the tools may also look at the
     * installed platforms to see if they can find more recently released
     * platforms, e.g. when the tools have not yet been updated for a new
     * release. This number is used as a baseline and any more recent platforms
     * found can be used to increase the highest known number.
     */
    public static final int HIGHEST_KNOWN_API = 17;

    /**
     * Returns the Android version and code name of the given API level, or null
     * if not known. The highest number (inclusive) that is supported
     * is {@link SdkVersionInfo#HIGHEST_KNOWN_API}.
     *
     * @param api the api level
     * @return a suitable version display name
     */
    @Nullable
    public static String getAndroidName(int api) {
        // See http://source.android.com/source/build-numbers.html
        switch (api) {
            case 1:  return "API 1: Android 1.0";
            case 2:  return "API 2: Android 1.1";
            case 3:  return "API 3: Android 1.5 (Cupcake)";
            case 4:  return "API 4: Android 1.6 (Donut)";
            case 5:  return "API 5: Android 2.0 (Eclair)";
            case 6:  return "API 6: Android 2.0.1 (Eclair)";
            case 7:  return "API 7: Android 2.1 (Eclair)";
            case 8:  return "API 8: Android 2.2 (Froyo)";
            case 9:  return "API 9: Android 2.3 (Gingerbread)";
            case 10: return "API 10: Android 2.3.3 (Gingerbread)";
            case 11: return "API 11: Android 3.0 (Honeycomb)";
            case 12: return "API 12: Android 3.1 (Honeycomb)";
            case 13: return "API 13: Android 3.2 (Honeycomb)";
            case 14: return "API 14: Android 4.0 (IceCreamSandwich)";
            case 15: return "API 15: Android 4.0.3 (IceCreamSandwich)";
            case 16: return "API 16: Android 4.1 (Jelly Bean)";
            case 17: return "API 17: Android 4.2 (Jelly Bean)";
            // If you add more versions here, also update LintUtils#getBuildCodes and
            // SdkConstants#HIGHEST_KNOWN_API

            default: return null;
        }
    }

    /**
     * Returns the applicable build code (for
     * {@code android.os.Build.VERSION_CODES}) for the corresponding API level,
     * or null if it's unknown. The highest number (inclusive) that is supported
     * is {@link SdkVersionInfo#HIGHEST_KNOWN_API}.
     *
     * @param api the API level to look up a version code for
     * @return the corresponding build code field name, or null
     */
    @Nullable
    public static String getBuildCode(int api) {
        // See http://developer.android.com/reference/android/os/Build.VERSION_CODES.html
        switch (api) {
            case 1:  return "BASE"; //$NON-NLS-1$
            case 2:  return "BASE_1_1"; //$NON-NLS-1$
            case 3:  return "CUPCAKE"; //$NON-NLS-1$
            case 4:  return "DONUT"; //$NON-NLS-1$
            case 5:  return "ECLAIR"; //$NON-NLS-1$
            case 6:  return "ECLAIR_0_1"; //$NON-NLS-1$
            case 7:  return "ECLAIR_MR1"; //$NON-NLS-1$
            case 8:  return "FROYO"; //$NON-NLS-1$
            case 9:  return "GINGERBREAD"; //$NON-NLS-1$
            case 10: return "GINGERBREAD_MR1"; //$NON-NLS-1$
            case 11: return "HONEYCOMB"; //$NON-NLS-1$
            case 12: return "HONEYCOMB_MR1"; //$NON-NLS-1$
            case 13: return "HONEYCOMB_MR2"; //$NON-NLS-1$
            case 14: return "ICE_CREAM_SANDWICH"; //$NON-NLS-1$
            case 15: return "ICE_CREAM_SANDWICH_MR1"; //$NON-NLS-1$
            case 16: return "JELLY_BEAN"; //$NON-NLS-1$
            case 17: return "JELLY_BEAN_MR1"; //$NON-NLS-1$
            // If you add more versions here, also update AdtUtils#getAndroidName and
            // SdkConstants#HIGHEST_KNOWN_API
        }

        return null;
    }
}







