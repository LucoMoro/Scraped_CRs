/*SDK: unit test for SdkSourceProperties.

Also fix a name changed in the SdkUiLib test.

Change-Id:I30fa83ac607fc4735af161604514f09c2aff6a93*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceProperties.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/internal/repository/SdkSourceProperties.java
//Synthetic comment -- index e9da67f..4edfd5e 100755

//Synthetic comment -- @@ -18,6 +18,8 @@

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.prefs.AndroidLocation;
import com.android.prefs.AndroidLocation.AndroidLocationException;

//Synthetic comment -- @@ -25,7 +27,9 @@
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
//Synthetic comment -- @@ -125,47 +129,40 @@
}
}

@Override
public String toString() {
        StringBuilder sb = new StringBuilder("<SdkSourceProperties ");      //$NON-NLS-1$
synchronized (sSourcesProperties) {
            for (Map.Entry<Object, Object> entry : sSourcesProperties.entrySet()) {
                sb.append('\n').append(entry.getKey())
                  .append(" = ").append(entry.getValue());                  //$NON-NLS-1$
}
}
        sb.append("\n>");                                                   //$NON-NLS-1$
return sb.toString();
}

private void loadLocked() {
// Load state from persistent file
        FileInputStream fis = null;
        try {
            String folder = AndroidLocation.getFolder();
            File f = new File(folder, SRC_FILENAME);
            if (f.exists()) {
                fis = new FileInputStream(f);

                sSourcesProperties.load(fis);

                // If it lacks our magic version key, don't use it
                if (sSourcesProperties.getProperty(KEY_VERSION) == null) {
                    sSourcesProperties.clear();
                }

                sModified = false;
}
        } catch (IOException ignore) {
            // nop
        } catch (AndroidLocationException ignore) {
            // nop
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ignore) {}
            }
}

if (sSourcesProperties.isEmpty()) {
//Synthetic comment -- @@ -177,7 +174,44 @@
}
}

    private void saveLocked() {
// Persist it to the file
FileOutputStream fos = null;
try {
//Synthetic comment -- @@ -199,6 +233,14 @@
} catch (IOException ignore) {}
}
}

}
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkSourcePropertiesTest.java b/sdkmanager/libs/sdklib/tests/src/com/android/sdklib/internal/repository/SdkSourcePropertiesTest.java
new file mode 100755
//Synthetic comment -- index 0000000..b4aa2e5

//Synthetic comment -- @@ -0,0 +1,138 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java b/sdkmanager/libs/sdkuilib/tests/com/android/sdkuilib/internal/repository/sdkman2/PackagesDiffLogicTest.java
//Synthetic comment -- index cf4b232..bc532a5 100755

//Synthetic comment -- @@ -1217,9 +1217,9 @@
"-- <NEW, pkg:The addon B from vendor 1, Android API 1, revision 7>\n" + // from src3+4
"PkgCategoryApi <API=EXTRAS, label=Extras, #items=0>\n",
getTree(m, true /*displaySortByApi*/));
        // When sorting by source, the src4 packages are not listed at all since
        // they are exactly the same as the ones from src2 or src3.
        // FIXME: in this sort mode, we should still list them explicitly.
assertEquals(
"PkgCategorySource <source=repo1 (example.com), #items=3>\n" +
"-- <INSTALLED, pkg:Android SDK Tools, revision 3>\n" +
//Synthetic comment -- @@ -1228,7 +1228,8 @@
"PkgCategorySource <source=repo2 (example.com), #items=1>\n" +
"-- <NEW, pkg:The addon A from vendor 1, Android API 1, revision 5>\n" + // from src2+3+4
"PkgCategorySource <source=repo3 (example.com), #items=1>\n" +
                "-- <NEW, pkg:The addon B from vendor 1, Android API 1, revision 7>\n",  // from src3+4
getTree(m, false /*displaySortByApi*/));
}








