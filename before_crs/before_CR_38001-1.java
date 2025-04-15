/*SDK Manager: API to list extras and their revisions.

Change-Id:I4989af2e987cf2b58f6da6a6a88f11a9351289e0*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/SdkManager.java
//Synthetic comment -- index 5c570ce..0f55eeb 100644

//Synthetic comment -- @@ -45,6 +45,7 @@
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -332,6 +333,37 @@
return samples;
}


// -------- private methods ----------








