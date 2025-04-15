/*Check CtsVerifier Feature Count

Add a quick test that will warn us when the features go out of sync.

Change-Id:Ic4102614a8c4905af1ef2ba9098d7863fc7d2954*/




//Synthetic comment -- diff --git a/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java b/apps/CtsVerifier/src/com/android/cts/verifier/features/FeatureSummaryActivity.java
//Synthetic comment -- index d448616..5da8130 100644

//Synthetic comment -- @@ -63,7 +63,7 @@
/**
* Constructor does not include 'present' because that's a detected
* value, and not set during creation.
         *
* @param name value for this.name
* @param required value for this.required
*/








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







