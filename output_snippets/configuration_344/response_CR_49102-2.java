//<Beginning of snippet n. 0>
final class Device implements IDevice {
    private static final String DEVICE_MODEL_PROPERTY = IDevice.DEVICE_MODEL_PROPERTY;
    private static final String DEVICE_MANUFACTURER_PROPERTY = IDevice.DEVICE_MANUFACTURER_PROPERTY;

    private static final int INSTALL_TIMEOUT = IDevice.INSTALL_TIMEOUT;
    private static final int BATTERY_TIMEOUT = IDevice.BATTERY_TIMEOUT;

    // Other methods and functionality...
    } else {
        String manufacturer = cleanupStringForDisplay(
                        getProperty(DEVICE_MANUFACTURER_PROPERTY));
        String model = cleanupStringForDisplay(
                        getProperty(DEVICE_MODEL_PROPERTY));

        StringBuilder sb = new StringBuilder(20);
    }
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public interface IDevice {
    public static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
    public static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$
    public static final int INSTALL_TIMEOUT = 2*60*1000; // 2 min
    public static final int BATTERY_TIMEOUT = 2*1000; // 2 seconds

    // Other methods and constants...
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
//<End of snippet n. 2>