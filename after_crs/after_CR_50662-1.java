/*Misc lint tweaks

Change-Id:I2fc3e6deb8266589a66f93138b2efa54825c7cab*/




//Synthetic comment -- diff --git a/common/src/main/java/com/android/SdkConstants.java b/common/src/main/java/com/android/SdkConstants.java
//Synthetic comment -- index d872c1c..16e05a1 100644

//Synthetic comment -- @@ -293,6 +293,20 @@
public static final String NS_DEVICES_XSD =
"http://schemas.android.com/sdk/devices/1";                     //$NON-NLS-1$

    /**
     * Namespace pattern for the custom resource XML, i.e. "http://schemas.android.com/apk/res/%s"
     * <p/>
     * This string contains a %s. It must be combined with the desired Java package, e.g.:
     * <pre>
     *    String.format(SdkConstants.NS_CUSTOM_RESOURCES_S, "android");
     *    String.format(SdkConstants.NS_CUSTOM_RESOURCES_S, "com.test.mycustomapp");
     * </pre>
     *
     * Note: if you need an URI specifically for the "android" namespace, consider using
     * {@link SdkConstants#NS_RESOURCES} instead.
     */
    public final static String NS_CUSTOM_RESOURCES_S = "http://schemas.android.com/apk/res/%1$s"; //$NON-NLS-1$


/** The name of the uses-library that provides "android.test.runner" */
public static final String ANDROID_TEST_RUNNER_LIB =








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RegistrationDetector.java b/lint/libs/lint_checks/src/main/java/com/android/tools/lint/checks/RegistrationDetector.java
//Synthetic comment -- index c4fd0a7..5b5a321 100644

//Synthetic comment -- @@ -74,7 +74,7 @@
EnumSet.of(Scope.MANIFEST, Scope.CLASS_FILE)).setMoreInfo(
"http://developer.android.com/guide/topics/manifest/manifest-intro.html"); //$NON-NLS-1$

    protected Multimap<String, String> mManifestRegistrations;

/** Constructs a new {@link RegistrationDetector} */
public RegistrationDetector() {
//Synthetic comment -- @@ -243,7 +243,7 @@
}

/** Looks up the tag a given framework class should be registered with */
    protected static String classToTag(String className) {
for (int i = 0, n = sClasses.length; i < n; i++) {
if (sClasses[i].equals(className)) {
return sTags[i];







