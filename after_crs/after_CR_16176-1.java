/*Cherk for ro.opengles.version System Property
Bug 2845550

Follow up to the report header including the Open GL ES version
number. This test makes sure the value is actually set, and it
does some light checking on the major and minor version numbers.

Change-Id:Ic91dd3fa3fbac97a6334a77fde2d875f3776adcd*/




//Synthetic comment -- diff --git a/tests/tests/graphics/src/android/opengl/cts/OpenGlEsVersionTest.java b/tests/tests/graphics/src/android/opengl/cts/OpenGlEsVersionTest.java
new file mode 100644
//Synthetic comment -- index 0000000..24cf314

//Synthetic comment -- @@ -0,0 +1,76 @@
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

package android.opengl.cts;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.test.AndroidTestCase;

/**
 * Test to check that the "ro.opengles.version" property is properly set by using the
 * {@link PackageManager#getSystemAvailableFeatures()} method.
 */
public class OpenGlEsVersionTest extends AndroidTestCase {

    private static final int MINIMUM_MAJOR_VERSION = 1;

    private static final int MINIMUM_MINOR_VERSION = 0;

    public void testOpenGlEsVersionFeature() {
        // Test that the "ro.opengles.version" property is set by checking the OpenGL ES version
        // feature set by the PackageManager.
        FeatureInfo versionInfo = getVersionFeatureInfo(mContext);
        assertNotNull(versionInfo);

        assertTrue("OpenGL ES major version feature present but set to undefined version.",
                FeatureInfo.GL_ES_VERSION_UNDEFINED != versionInfo.reqGlEsVersion);

        int majorVersion = getMajorVersion(versionInfo.reqGlEsVersion);
        assertTrue("OpenGL ES major version should be greater than " + MINIMUM_MAJOR_VERSION,
                majorVersion >= MINIMUM_MAJOR_VERSION);

        int minorVersion = getMinorVersion(versionInfo.reqGlEsVersion);
        assertTrue("OpenGL ES minor version should be greater than " + MINIMUM_MINOR_VERSION,
                minorVersion >= MINIMUM_MINOR_VERSION);
    }

    /** @see FeatureInfo#name */
    private static FeatureInfo getVersionFeatureInfo(Context context) {
        PackageManager packageManager = context.getPackageManager();
        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
        if (featureInfos != null && featureInfos.length > 0) {
            for (FeatureInfo featureInfo : featureInfos) {
                // Null feature name means this feature is the open gl es version feature.
                if (featureInfo.name == null) {
                    return featureInfo;
                }
            }
        }
        return null;
    }

    /** @see FeatureInfo#getGlEsVersion() */
    private static int getMajorVersion(int glEsVersion) {
        return ((glEsVersion & 0xffff0000) >> 16);
    }

    /** @see FeatureInfo#getGlEsVersion() */
    private static int getMinorVersion(int glEsVersion) {
        return glEsVersion & 0x0000ffff;
    }
}







