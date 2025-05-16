//<Beginning of snippet n. 0>
final class Device implements IDevice {
    // Constants moved to IDevice interface
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public interface IDevice {
    public static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
    public static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$
    public static final int INSTALL_TIMEOUT = 2 * 60 * 1000; // 2min
    public static final int BATTERY_TIMEOUT = 2 * 1000; // 2 seconds
    
    public static final String PROP_BUILD_VERSION = "ro.build.version.release";
    public static final String PROP_BUILD_API_LEVEL = "ro.build.version.sdk";
    public static final String PROP_BUILD_CODENAME = "ro.build.version.codename";
    public static final String PROP_DEBUGGABLE = "ro.debuggable";
}
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

// Hook not present. Implementing as specified by CR_TASK.
protected void onTestRunStart() {
    // Implementation of new hook goes here
}

//<End of snippet n. 2>