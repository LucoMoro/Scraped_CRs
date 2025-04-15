/*Store plugin tests results in a tmp sub folder.

Change-Id:Ib52b2ae105225f3052cd46690d21a3f81be309d6*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/AdtProjectTest.java b/eclipse/plugins/com.android.ide.eclipse.tests/src/com/android/ide/eclipse/adt/internal/editors/layout/refactoring/AdtProjectTest.java
//Synthetic comment -- index db74295..85cca2b 100644

//Synthetic comment -- @@ -59,6 +59,7 @@
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

//Synthetic comment -- @@ -73,6 +74,7 @@
+ "internal/editors/layout/refactoring/testdata";
private static final String PROJECTNAME_PREFIX = "testproject-";
private static final long TESTS_START_TIME = System.currentTimeMillis();

/**
* We don't stash the project used by each test case as a field such that test cases
//Synthetic comment -- @@ -493,7 +495,24 @@
if (SdkConstants.CURRENT_PLATFORM == SdkConstants.PLATFORM_DARWIN) {
return new File("/tmp"); //$NON-NLS-1$
}
        return new File(System.getProperty("java.io.tmpdir")); //$NON-NLS-1$
}

/** Special editor context set on the model to be rendered */







