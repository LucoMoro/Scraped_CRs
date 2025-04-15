/*Use Reflection to Get Features for Report Header

Bug 2742459

Get the list of features to check by using reflection over the
PackageManager constants. These constants are guaranteed to be
there due to the SignatureTest that checks the API signature on
the device. Its possible that the actual value of the constants
could be different, but that is up to the operator to inspect
all values that appear in the report.

Change-Id:Ib492708bc95e5777a388aed1c4b468ba4723cf97*/




//Synthetic comment -- diff --git a/tools/device-setup/TestDeviceSetup/src/android/tests/getinfo/DeviceInfoInstrument.java b/tools/device-setup/TestDeviceSetup/src/android/tests/getinfo/DeviceInfoInstrument.java
//Synthetic comment -- index 1b8a83e..03fa59e 100644

//Synthetic comment -- @@ -31,25 +31,14 @@
import android.view.WindowManager;

import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeviceInfoInstrument extends Instrumentation {

private static final String PROCESSES = "processes";
private static final String FEATURES = "features";
private static final String PHONE_NUMBER = "phoneNumber";
//Synthetic comment -- @@ -193,7 +182,7 @@
Set<String> checkedFeatures = new HashSet<String>();

PackageManager packageManager = getContext().getPackageManager();
        for (String featureName : getPackageManagerFeatures()) {
checkedFeatures.add(featureName);
boolean hasFeature = packageManager.hasSystemFeature(featureName);
addFeature(features, featureName, "sdk", hasFeature);
//Synthetic comment -- @@ -215,6 +204,29 @@
}

/**
     * Use reflection to get the features defined by the SDK. If there are features that do not fit
     * the convention of starting with "FEATURE_" then they will still be shown under the
     * "Other Features" section.
     *
     * @return list of feature names from sdk
     */
    private List<String> getPackageManagerFeatures() {
        try {
            List<String> features = new ArrayList<String>();
            Field[] fields = PackageManager.class.getFields();
            for (Field field : fields) {
                if (field.getName().startsWith("FEATURE_")) {
                    String feature = (String) field.get(null);
                    features.add(feature);
                }
            }
            return features;
        } catch (IllegalAccessException illegalAccess) {
            throw new RuntimeException(illegalAccess);
        }
    }

    /**
* Return a semi-colon-delimited list of the root processes that were running on the phone
* or an error message.
*/







