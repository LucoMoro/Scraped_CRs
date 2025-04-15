/*Fixed resource leak in DeviceManager

Change-Id:I5eed8a5de878bd3c52452536baca0c56822263b8*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/devices/DeviceManager.java
//Synthetic comment -- index 7825215..e3985bd 100644

//Synthetic comment -- @@ -16,23 +16,6 @@

package com.android.sdklib.devices;

import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;
import com.android.resources.Keyboard;
//Synthetic comment -- @@ -45,6 +28,24 @@
import com.android.sdklib.internal.avd.HardwareProperties;
import com.android.sdklib.repository.PkgProps;

import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

/**
* Manager class for interacting with {@link Device}s within the SDK
*/
//Synthetic comment -- @@ -217,16 +218,18 @@
File properties = new File(item, SdkConstants.FN_SOURCE_PROP);
try {
BufferedReader propertiesReader = new BufferedReader(new FileReader(properties));
            try {
                String line;
                while ((line = propertiesReader.readLine()) != null) {
                    Matcher m = sPathPropertyPattern.matcher(line);
                    if (m.matches()) {
                        return true;
                    }
}
            } finally {
                propertiesReader.close();
}
        } catch (IOException ignore) { }
return false;
}
}







