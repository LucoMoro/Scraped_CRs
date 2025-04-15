/*Test for Checking FeatureSummary FEATURE Count

Add a test that will compare the PackageManager FEATURE_* count
to the feature list in the FeatureSummaryActivity. Hopefully this
will remind us to update it for new releases.

Change-Id:I06080a76a714e7cfc70e00c3e1692f4970907198*/




//Synthetic comment -- diff --git a/apps/CtsVerifier/tests/src/com/android/cts/verifier/features/FeatureSummaryActivityTest.java b/apps/CtsVerifier/tests/src/com/android/cts/verifier/features/FeatureSummaryActivityTest.java
new file mode 100644
//Synthetic comment -- index 0000000..43f5da4

//Synthetic comment -- @@ -0,0 +1,55 @@
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

package com.android.cts.verifier.features;

import com.android.cts.verifier.features.FeatureSummaryActivity.Feature;

import android.content.pm.PackageManager;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class FeatureSummaryActivityTest extends TestCase {

    public void testAllFeatures() throws Exception {
        Set<String> expectedFeatures = getFeatureConstants();

        Set<String> actualFeatures = new HashSet<String>();
        for (Feature feature : FeatureSummaryActivity.ALL_FEATURES) {
            actualFeatures.add(feature.name);
        }

        assertEquals("Feature list needs to be updated.",
                expectedFeatures.size(), actualFeatures.size());
    }

    private static Set<String> getFeatureConstants()
            throws IllegalArgumentException, IllegalAccessException {
        Set<String> features = new HashSet<String>();
        Field[] fields = PackageManager.class.getFields();
        for (Field field : fields) {
            if (field.getName().startsWith("FEATURE_")) {
                String feature = (String) field.get(null);
                features.add(feature);
            }
        }
        return features;
    }
}







