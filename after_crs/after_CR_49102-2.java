/*Move some constants from Device to IDevice.

Also add another hook to XmlTestRunListener

Change-Id:I354b6bfb184c4168a8f3f81bb7184b40bfb2b220*/




//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/Device.java b/ddmlib/src/main/java/com/android/ddmlib/Device.java
//Synthetic comment -- index 84b7da3..85d1d8d 100644

//Synthetic comment -- @@ -34,8 +34,6 @@
* A Device. It can be a physical device or an emulator.
*/
final class Device implements IDevice {

private static final int INSTALL_TIMEOUT = 2*60*1000; //2min
private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds
//Synthetic comment -- @@ -210,9 +208,9 @@
}
} else {
String manufacturer = cleanupStringForDisplay(
                    getProperty(PROP_DEVICE_MANUFACTURER));
String model = cleanupStringForDisplay(
                    getProperty(PROP_DEVICE_MODEL));

StringBuilder sb = new StringBuilder(20);









//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/IDevice.java b/ddmlib/src/main/java/com/android/ddmlib/IDevice.java
//Synthetic comment -- index b8f2dc9..a9ebaad 100644

//Synthetic comment -- @@ -29,6 +29,8 @@
public static final String PROP_BUILD_VERSION = "ro.build.version.release";
public static final String PROP_BUILD_API_LEVEL = "ro.build.version.sdk";
public static final String PROP_BUILD_CODENAME = "ro.build.version.codename";
    public static final String PROP_DEVICE_MODEL = "ro.product.model";
    public static final String PROP_DEVICE_MANUFACTURER = "ro.product.manufacturer";

public static final String PROP_DEBUGGABLE = "ro.debuggable";









//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/testrunner/XmlTestRunListener.java b/ddmlib/src/main/java/com/android/ddmlib/testrunner/XmlTestRunListener.java
//Synthetic comment -- index d7496d0..3d160a6 100644

//Synthetic comment -- @@ -229,7 +229,9 @@
serializer.attribute(ns, ATTR_TIME, Long.toString(elapsedTime));
serializer.attribute(ns, TIMESTAMP, timestamp);
serializer.attribute(ns, HOSTNAME, mHostName);

serializer.startTag(ns, PROPERTIES);
        setPropertiesAttributes(serializer, ns);
serializer.endTag(ns, PROPERTIES);

Map<TestIdentifier, TestResult> testResults = mRunResult.getTestResults();
//Synthetic comment -- @@ -240,6 +242,16 @@
serializer.endTag(ns, TESTSUITE);
}

    /**
     * Sets the attributes on properties.
     * @param serializer the serializer
     * @param namespace the namespace
     * @throws IOException
     */
    protected void setPropertiesAttributes(KXmlSerializer serializer, String namespace)
            throws IOException {
    }

protected String getTestName(TestIdentifier testId) {
return testId.getTestName();
}







