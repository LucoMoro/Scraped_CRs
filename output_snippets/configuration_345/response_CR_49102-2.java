//<Beginning of snippet n. 0>
final class Device implements IDevice {
    public static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
    public static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$

    public static final int INSTALL_TIMEOUT = 2*60*1000; //2min
    public static final int BATTERY_TIMEOUT = 2*1000; //2 seconds

    // Assuming there are methods that utilize the properties above
    private String cleanupStringForDisplay(String propertyValue) {
        // Implementation goes here
        return propertyValue; // Placeholder return
    }

    private String getProperty(String property) {
        // Implementation goes here
        return ""; // Placeholder return
    }

    public void someMethod() {
        String manufacturer = cleanupStringForDisplay(getProperty(DEVICE_MANUFACTURER_PROPERTY));
        String model = cleanupStringForDisplay(getProperty(DEVICE_MODEL_PROPERTY));

        StringBuilder sb = new StringBuilder(20);
        // Continue processing with sb
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public static final String PROP_BUILD_VERSION = "ro.build.version.release";
public static final String PROP_BUILD_API_LEVEL = "ro.build.version.sdk";
public static final String PROP_BUILD_CODENAME = "ro.build.version.codename";

public static final String PROP_DEBUGGABLE = "ro.debuggable";
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
serializer.attribute(ns, ATTR_TIME, Long.toString(elapsedTime));
serializer.attribute(ns, TIMESTAMP, timestamp);
serializer.attribute(ns, HOSTNAME, mHostName);
serializer.startTag(ns, PROPERTIES);
serializer.endTag(ns, PROPERTIES);

Map<TestIdentifier, TestResult> testResults = mRunResult.getTestResults();
serializer.endTag(ns, TESTSUITE);
}

protected String getTestName(TestIdentifier testId) {
    return testId.getTestName();
}
//<End of snippet n. 2>