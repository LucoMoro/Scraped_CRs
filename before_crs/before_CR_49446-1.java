/*Fix incorrect error message in the commit transaction detection

Also, the unit test class had the wrong name after the tested
class was renamed.

Change-Id:I4398b112cd5e0d0fe99f15a5a66ffbd6436fec59*/
//Synthetic comment -- diff --git a/lint/cli/src/test/java/com/android/tools/lint/checks/RecycleDetectorTest.java b/lint/cli/src/test/java/com/android/tools/lint/checks/CleanupDetectorTest.java
similarity index 79%
rename from lint/cli/src/test/java/com/android/tools/lint/checks/RecycleDetectorTest.java
rename to lint/cli/src/test/java/com/android/tools/lint/checks/CleanupDetectorTest.java
//Synthetic comment -- index df6f451..0dff659 100644

//Synthetic comment -- @@ -19,7 +19,7 @@
import com.android.tools.lint.detector.api.Detector;

@SuppressWarnings("javadoc")
public class RecycleDetectorTest extends AbstractCheckTest {
@Override
protected Detector getDetector() {
return new CleanupDetector();
//Synthetic comment -- @@ -75,20 +75,20 @@
}

public void testCommit() throws Exception {
        assertEquals(""
                + "src/test/pkg/CommitTest.java:24: Warning: This FragmentTransaction should be recycled after use with #recycle() [CommitTransaction]\n"
                + "        getFragmentManager().beginTransaction(); // Missing commit\n"
                + "                             ~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/CommitTest.java:29: Warning: This FragmentTransaction should be recycled after use with #recycle() [CommitTransaction]\n"
                + "        FragmentTransaction transaction2 = getFragmentManager().beginTransaction(); // Missing commit\n"
                + "                                                                ~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/CommitTest.java:38: Warning: This FragmentTransaction should be recycled after use with #recycle() [CommitTransaction]\n"
                + "        getFragmentManager().beginTransaction(); // Missing commit\n"
                + "                             ~~~~~~~~~~~~~~~~\n"
                + "src/test/pkg/CommitTest.java:64: Warning: This FragmentTransaction should be recycled after use with #recycle() [Recycle]\n"
                + "        getSupportFragmentManager().beginTransaction();\n"
                + "                                    ~~~~~~~~~~~~~~~~\n"
                + "0 errors, 4 warnings\n",

lintProject(
"apicheck/classpath=>.classpath",








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CleanupDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/CleanupDetector.java
//Synthetic comment -- index 11365e2..4d5a4ac 100644

//Synthetic comment -- @@ -269,7 +269,7 @@

/** Computes an error message for a missing recycle of the given type */
private static String getErrorMessage(String owner) {
        if (FRAGMENT_MANAGER_CLS.equals(owner)) {
return "This transaction should be completed with a commit() call";
}
String className = owner.substring(owner.lastIndexOf('/') + 1);







