/*Ignore Tests with Side Effects

It looks like a ContentHandlerFactoryTest marked with @SideEffect is
causing subsequent luni net tests to fail even though those tests pass
on their own.

Change-Id:I60a65d4debe2484624c83ea4709dc6231b0dcb10Conflicts:

	tools/utils/CollectAllTests.java*/




//Synthetic comment -- diff --git a/tools/utils/CollectAllTests.java b/tools/utils/CollectAllTests.java
//Synthetic comment -- index 7628ba2..539cf8d 100644

//Synthetic comment -- @@ -302,6 +302,11 @@
return getAnnotation(testClass, testName, SUPPRESSED_TEST) != null;
}

    private boolean hasSideEffects(final Class<? extends TestCase> testClass,
            final String testName) {
        return getAnnotation(testClass, testName, SIDE_EFFECT) != null;
    }

private String getAnnotation(final Class<? extends TestCase> testClass,
final String testName, final String annotationName) {
try {
//Synthetic comment -- @@ -349,6 +354,9 @@
} else if (isSuppressed(test.getClass(), testName)) {
System.out.println("ignoring suppressed test: " + test);
return;
        } else if (hasSideEffects(test.getClass(), testName)) {
            System.out.println("ignoring test with side effects: " + test);
            return;
}

if (!testName.startsWith("test")) {








//Synthetic comment -- diff --git a/tools/utils/DescriptionGenerator.java b/tools/utils/DescriptionGenerator.java
//Synthetic comment -- index 99be6dc..2d58543 100644

//Synthetic comment -- @@ -65,6 +65,7 @@
static final String HOST_CONTROLLER = "dalvik.annotation.HostController";
static final String KNOWN_FAILURE = "dalvik.annotation.KnownFailure";
static final String BROKEN_TEST = "dalvik.annotation.BrokenTest";
    static final String SIDE_EFFECT = "dalvik.annotation.SideEffect";
static final String SUPPRESSED_TEST = "android.test.suitebuilder.annotation.Suppress";

static final String JUNIT_TEST_CASE_CLASS_NAME = "junit.framework.testcase";







