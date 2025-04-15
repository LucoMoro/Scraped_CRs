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
//Synthetic comment -- index 1b8a83e..4e0b2f0 100644

//Synthetic comment -- @@ -25,30 +25,20 @@
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeviceInfoInstrument extends Instrumentation {

    private static final String TAG = "DeviceInfoInstrument";

private static final String PROCESSES = "processes";
private static final String FEATURES = "features";
//Synthetic comment -- @@ -190,20 +180,26 @@
private String getFeatures() {
StringBuilder features = new StringBuilder();

        try {
            Set<String> checkedFeatures = new HashSet<String>();

            PackageManager packageManager = getContext().getPackageManager();
            for (String featureName : getPackageManagerFeatures()) {
                checkedFeatures.add(featureName);
                boolean hasFeature = packageManager.hasSystemFeature(featureName);
                addFeature(features, featureName, "sdk", hasFeature);
}

            FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
            if (featureInfos != null) {
                for (FeatureInfo featureInfo : featureInfos) {
                    if (featureInfo.name != null && !checkedFeatures.contains(featureInfo.name)) {
                        addFeature(features, featureInfo.name, "other", true);
                    }
                }
            }
        } catch (Exception exception) {
            Log.e(TAG, "Error getting features: " + exception.getMessage(), exception);
}

return features.toString();
//Synthetic comment -- @@ -215,6 +211,29 @@
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
//Synthetic comment -- @@ -226,10 +245,9 @@
for (String rootProcess : rootProcesses) {
builder.append(rootProcess).append(';');
}
        } catch (Exception exception) {
            Log.e(TAG, "Error getting processes: " + exception.getMessage(), exception);
            builder.append(exception.getMessage());
}

return builder.toString();








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestSessionLog.java b/tools/host/src/com/android/cts/TestSessionLog.java
//Synthetic comment -- index ae0d88c..a8c9f8e 100644

//Synthetic comment -- @@ -420,15 +420,21 @@
parentNode.appendChild(featureInfo);

String features = deviceInfo.getFeatures();
        if (features == null) {
            features = "";
        }

String[] featurePairs = features.split(";");
for (String featurePair : featurePairs) {
String[] nameTypeAvailability = featurePair.split(":");
            if (nameTypeAvailability.length >= 3) {
                Node feature = document.createElement(TAG_FEATURE);
                featureInfo.appendChild(feature);

                setAttribute(document, feature, ATTRIBUTE_NAME, nameTypeAvailability[0]);
                setAttribute(document, feature, ATTRIBUTE_TYPE, nameTypeAvailability[1]);
                setAttribute(document, feature, ATTRIBUTE_AVAILABLE, nameTypeAvailability[2]);
            }
}
}








