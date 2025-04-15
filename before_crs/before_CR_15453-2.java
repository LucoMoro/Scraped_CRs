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
import android.tests.getinfo.RootProcessScanner.MalformedStatMException;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class DeviceInfoInstrument extends Instrumentation {

    // Should use XML files in frameworks/base/data/etc to generate dynamically.
    private static final String[] FEATURES_TO_CHECK = {
        PackageManager.FEATURE_CAMERA,
        PackageManager.FEATURE_CAMERA_AUTOFOCUS,
        PackageManager.FEATURE_CAMERA_FLASH,
        PackageManager.FEATURE_SENSOR_LIGHT,
        PackageManager.FEATURE_SENSOR_PROXIMITY,
        PackageManager.FEATURE_TELEPHONY,
        PackageManager.FEATURE_TELEPHONY_CDMA,
        PackageManager.FEATURE_TELEPHONY_GSM,
        PackageManager.FEATURE_TOUCHSCREEN_MULTITOUCH,
        PackageManager.FEATURE_LIVE_WALLPAPER,
    };

private static final String PROCESSES = "processes";
private static final String FEATURES = "features";
//Synthetic comment -- @@ -190,20 +180,26 @@
private String getFeatures() {
StringBuilder features = new StringBuilder();

        Set<String> checkedFeatures = new HashSet<String>();

        PackageManager packageManager = getContext().getPackageManager();
        for (String featureName : FEATURES_TO_CHECK) {
            checkedFeatures.add(featureName);
            boolean hasFeature = packageManager.hasSystemFeature(featureName);
            addFeature(features, featureName, "sdk", hasFeature);
        }

        FeatureInfo[] featureInfos = packageManager.getSystemAvailableFeatures();
        for (FeatureInfo featureInfo : featureInfos) {
            if (featureInfo.name != null && !checkedFeatures.contains(featureInfo.name)) {
                addFeature(features, featureInfo.name, "other", true);
}
}

return features.toString();
//Synthetic comment -- @@ -215,6 +211,29 @@
}

/**
* Return a semi-colon-delimited list of the root processes that were running on the phone
* or an error message.
*/
//Synthetic comment -- @@ -226,10 +245,9 @@
for (String rootProcess : rootProcesses) {
builder.append(rootProcess).append(';');
}
        } catch (FileNotFoundException notFound) {
            builder.append(notFound.getMessage());
        } catch (MalformedStatMException malformedStatM) {
            builder.append(malformedStatM.getMessage());
}

return builder.toString();








//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/TestSessionLog.java b/tools/host/src/com/android/cts/TestSessionLog.java
//Synthetic comment -- index ae0d88c..a8c9f8e 100644

//Synthetic comment -- @@ -420,15 +420,21 @@
parentNode.appendChild(featureInfo);

String features = deviceInfo.getFeatures();
String[] featurePairs = features.split(";");
for (String featurePair : featurePairs) {
            Node feature = document.createElement(TAG_FEATURE);
            featureInfo.appendChild(feature);

String[] nameTypeAvailability = featurePair.split(":");
            setAttribute(document, feature, ATTRIBUTE_NAME, nameTypeAvailability[0]);
            setAttribute(document, feature, ATTRIBUTE_TYPE, nameTypeAvailability[1]);
            setAttribute(document, feature, ATTRIBUTE_AVAILABLE, nameTypeAvailability[2]);
}
}








