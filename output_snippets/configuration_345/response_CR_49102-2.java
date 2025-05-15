//<Beginning of snippet n. 0>
/**
 * A Device. It can be a physical device or an emulator.
 */
final class Device implements IDevice {
    public static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
    public static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$

    public static final int INSTALL_TIMEOUT = 2 * 60 * 1000; // 2min
    public static final int BATTERY_TIMEOUT = 2 * 1000; // 2 seconds

    // Existing methods and logic

} // Assuming the closing brace for the Device class here (this was missing).
// A location for the hook integration within XmlTestRunListener would be assumed to be here, but it's not provided in the snippet.
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