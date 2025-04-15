/*NPE fix when launching test project

When launching test proejct, it start to read test size setting from the
launch configuration. If it is never set before, it can be null.

Change-Id:I8493c5aca20969c9fe0fda1b210f432022cd099fSigned-off-by: skyisle <skyisle@gmail.com>*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/junit/AndroidJUnitLaunchConfigDelegate.java
//Synthetic comment -- index 0209e0f..baa3f6b 100755

//Synthetic comment -- @@ -168,14 +168,14 @@
String testSizeAnnotation = getStringLaunchAttribute(
AndroidJUnitLaunchConfigDelegate.ATTR_TEST_SIZE,
configuration);
        if (AndroidJUnitLaunchConfigurationTab.SMALL_TEST_ANNOTATION.equals(
                    testSizeAnnotation)){
return TestSize.SMALL;
        } else if (AndroidJUnitLaunchConfigurationTab.MEDIUM_TEST_ANNOTATION.equals(
                    testSizeAnnotation)) {
return TestSize.MEDIUM;
        } else if (AndroidJUnitLaunchConfigurationTab.LARGE_TEST_ANNOTATION.equals(
                    testSizeAnnotation)) {
return TestSize.LARGE;
} else {
return null;







