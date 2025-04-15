/*Test infrastructure tweaks

Change-Id:I6034d4fd2e28f5386301981b031a1089872e91d6*/




//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/AbstractCheckTest.java
//Synthetic comment -- index f57fb9c..f270cf2 100644

//Synthetic comment -- @@ -39,7 +39,10 @@
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
//Synthetic comment -- @@ -203,6 +206,26 @@
String path = "data" + File.separator + relativePath; //$NON-NLS-1$
InputStream stream =
AbstractCheckTest.class.getResourceAsStream(path);
        if (stream == null) {
            File root = getRootDir();
            assertNotNull(root);
            String pkg = AbstractCheckTest.class.getName();
            pkg = pkg.substring(0, pkg.lastIndexOf('.'));
            File f = new File(root,
                    "tools/base/lint/cli/src/test/java/".replace('/', File.separatorChar)
                        + pkg.replace('.', File.separatorChar)
                        + File.separatorChar + path);
            if (f.exists()) {
                try {
                    return new BufferedInputStream(new FileInputStream(f));
                } catch (FileNotFoundException e) {
                    stream = null;
                    if (expectExists) {
                        fail("Could not find file " + relativePath);
                    }
                }
            }
        }
if (!expectExists && stream == null) {
return null;
}
//Synthetic comment -- @@ -379,7 +402,11 @@
while (dir != null) {
File settingsGradle = new File(dir, "settings.gradle"); //$NON-NLS-1$
if (settingsGradle.exists()) {
                        return dir.getParentFile().getParentFile();
                    }
                    File lint = new File(dir, "lint");  //$NON-NLS-1$
                    if (lint.exists() && new File(lint, "cli").exists()) { //$NON-NLS-1$
                        return dir.getParentFile().getParentFile();
}
dir = dir.getParentFile();
}








//Synthetic comment -- diff --git a/testutils/src/main/java/com/android/testutils/SdkTestCase.java b/testutils/src/main/java/com/android/testutils/SdkTestCase.java
//Synthetic comment -- index 6a4d54a..402d7a9 100644

//Synthetic comment -- @@ -41,9 +41,7 @@
* files, computing string diffs, etc.
*/
@SuppressWarnings("javadoc")
public abstract class SdkTestCase extends TestCase {
/** Update golden files if different from the actual results */
private static final boolean UPDATE_DIFFERENT_FILES = false;
/** Create golden files if missing */
//Synthetic comment -- @@ -52,8 +50,8 @@
protected static Set<File> sCleanDirs = Sets.newHashSet();

protected String getTestDataRelPath() {
        fail("Must be overridden");
        return null;
}

public static int getCaretOffset(String fileContent, String caretLocation) {







