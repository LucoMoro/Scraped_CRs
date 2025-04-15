/*Make String-based hw prop not recognized.

Currently all those strings aren't set to be viewed by the user
in the AVD UI so we ignore them.
In the future we should use a specific flag to ignore some (like
avd.name) and allow others.

Change-Id:Icab59bcc0b97c14fa14b786c737457434b7f51ac*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/avd/HardwareProperties.java
//Synthetic comment -- index 7d29364..66cf0f4 100644

//Synthetic comment -- @@ -24,8 +24,8 @@
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -97,6 +97,10 @@
public String getDescription() {
return mDescription;
}

        public boolean isValid() {
            return mName != null && mType != null;
        }
}

/**
//Synthetic comment -- @@ -110,7 +114,7 @@
FileInputStream fis = new FileInputStream(file);
BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            Map<String, HardwareProperty> map = new TreeMap<String, HardwareProperty>();

String line = null;
HardwareProperty prop = null;
//Synthetic comment -- @@ -124,7 +128,6 @@
if (HW_PROP_NAME.equals(valueName)) {
prop = new HardwareProperty();
prop.mName = value;
}

if (prop == null) {
//Synthetic comment -- @@ -142,6 +145,11 @@
} else if (HW_PROP_DESC.equals(valueName)) {
prop.mDescription = value;
}

                        if (prop.isValid()) {
                            map.put(prop.mName, prop);
                        }

} else {
log.warning("Error parsing '%1$s': \"%2$s\" is not a valid syntax",
file.getAbsolutePath(), line);







