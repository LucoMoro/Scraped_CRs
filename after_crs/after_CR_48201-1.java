/*Don't load abstract classes.

Bug 7475298

Change-Id:I5109675678fbc6b08df3759d014ca886d7bf9204*/




//Synthetic comment -- diff --git a/androidtestlib/src/com/android/test/runner/TestLoader.java b/androidtestlib/src/com/android/test/runner/TestLoader.java
//Synthetic comment -- index d5ad737..97c1083 100644

//Synthetic comment -- @@ -23,6 +23,7 @@

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

//Synthetic comment -- @@ -126,6 +127,11 @@
* @return <code>true</code> if loadedClass is a test
*/
private boolean isTestClass(Class<?> loadedClass) {
        if (Modifier.isAbstract(loadedClass.getModifiers())) {
            Log.v(LOG_TAG, String.format("Skipping abstract class %s: not a test",
                    loadedClass.getName()));
            return false;
        }
// TODO: try to find upstream junit calls to replace these checks
if (junit.framework.Test.class.isAssignableFrom(loadedClass)) {
return true;








//Synthetic comment -- diff --git a/androidtestlib/tests/src/com/android/test/runner/TestLoaderTest.java b/androidtestlib/tests/src/com/android/test/runner/TestLoaderTest.java
//Synthetic comment -- index 5ffff5c..34cadc1 100644

//Synthetic comment -- @@ -34,6 +34,9 @@
public static class JUnit3Test extends TestCase {
}

    public static abstract class AbstractTest extends TestCase {
    }

public static class JUnit4Test {
@Test
public void thisIsATest() {
//Synthetic comment -- @@ -93,4 +96,11 @@
Assert.assertEquals(0, mLoader.getLoadFailures().size());
Assert.assertTrue(mLoader.getLoadedClasses().contains(clazz));
}

    @Test
    public void testLoadTests_abstract() {
        Assert.assertNull(mLoader.loadIfTest(AbstractTest.class.getName()));
        Assert.assertEquals(0, mLoader.getLoadedClasses().size());
        Assert.assertEquals(0, mLoader.getLoadFailures().size());
    }
}







