
//<Beginning of snippet n. 0>


* A Device. It can be a physical device or an emulator.
*/
final class Device implements IDevice {

private static final int INSTALL_TIMEOUT = 2*60*1000; //2min
private static final int BATTERY_TIMEOUT = 2*1000; //2 seconds
}
} else {
String manufacturer = cleanupStringForDisplay(
                    getProperty(PROP_DEVICE_MANUFACTURER));
String model = cleanupStringForDisplay(
                    getProperty(PROP_DEVICE_MODEL));

StringBuilder sb = new StringBuilder(20);


//<End of snippet n. 0>










//<Beginning of snippet n. 1>


public static final String PROP_BUILD_VERSION = "ro.build.version.release";
public static final String PROP_BUILD_API_LEVEL = "ro.build.version.sdk";
public static final String PROP_BUILD_CODENAME = "ro.build.version.codename";
    public static final String PROP_DEVICE_MODEL = "ro.product.model";
    public static final String PROP_DEVICE_MANUFACTURER = "ro.product.manufacturer";

public static final String PROP_DEBUGGABLE = "ro.debuggable";


//<End of snippet n. 1>










//<Beginning of snippet n. 2>


serializer.attribute(ns, ATTR_TIME, Long.toString(elapsedTime));
serializer.attribute(ns, TIMESTAMP, timestamp);
serializer.attribute(ns, HOSTNAME, mHostName);

serializer.startTag(ns, PROPERTIES);
        setPropertiesAttributes(serializer, ns);
serializer.endTag(ns, PROPERTIES);

Map<TestIdentifier, TestResult> testResults = mRunResult.getTestResults();
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

//<End of snippet n. 2>








