/*SDK: change proguard.config for Windows.

Change the default to use / dir-separators even
for the Windows template. The / will be correctly
replaced by \\ by both Ant and our ExportHelper,
ensuring a relative path works on all platforms.

SDK bug: 28860

(This does not solve the path-separator issue with
; vs : on Windows. That will be addressed later.)

Change-Id:Iee3ae9c58aa606aee7943c4e20643164bbf7344b*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/project/ProjectProperties.java
//Synthetic comment -- index 2bb6b71..4de2042 100644

//Synthetic comment -- @@ -29,7 +29,6 @@
import com.android.sdklib.SdkConstants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
//Synthetic comment -- @@ -186,8 +185,10 @@
"#\n" +
"# To enable ProGuard to shrink and obfuscate your code, uncomment this "
+ "(available properties: sdk.dir, user.home):\n" +
           "#" + PROPERTY_PROGUARD_CONFIG + "=${" + PROPERTY_SDK +"}" + File.separator
               + FD_TOOLS + File.separator + FD_PROGUARD + File.separator
+ FN_ANDROID_PROGUARD_FILE + ':' + FN_PROJECT_PROGUARD_FILE +'\n' +
"\n";








