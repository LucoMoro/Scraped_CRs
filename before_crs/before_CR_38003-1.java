/*SDK Manager: API to list extras and their revisions.

(cherry picked from commit ce952c83dcab748cb51dd87e23fdfdab26c04f23)

Change-Id:I8b879d869ba3f320c5dffbba71fdccb31245d2be*/
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








