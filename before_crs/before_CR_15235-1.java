/*Remove debug printfs

Change-Id:I34f2aa0d35c904a421654e320c9bdfc229583fab*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/export/ProjectConfig.java
//Synthetic comment -- index d883f5c..387e8ff 100644

//Synthetic comment -- @@ -28,7 +28,6 @@
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -311,8 +310,6 @@
return "Property split.abi is missing from config file";
}
String[] abis = tmp.split("\\|");
        System.out.println("ABIS: " + Arrays.toString(abis));
        System.out.println("current: " + mSplitByAbi);
if (mSplitByAbi != Boolean.valueOf(abis[0])) { // first value is always the split boolean
return "Property split.abi changed";
}







