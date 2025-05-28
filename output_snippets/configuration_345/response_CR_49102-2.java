//<Beginning of snippet n. 0>
final class Device implements IDevice {
    // Removed constants from Device class
    // Implementation details removed for brevity
} else {
    String manufacturer = cleanupStringForDisplay(
                    getProperty(IDevice.DEVICE_MANUFACTURER_PROPERTY));
    String model = cleanupStringForDisplay(
                    getProperty(IDevice.DEVICE_MODEL_PROPERTY));

    StringBuilder sb = new StringBuilder(20);
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public interface IDevice {
    public static final String DEVICE_MODEL_PROPERTY = "device.model";
    public static final String DEVICE_MANUFACTURER_PROPERTY = "device.manufacturer";
    public static final int INSTALL_TIMEOUT = 60000; // 60 seconds
    public static final int BATTERY_TIMEOUT = 10000; // 10 seconds
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
public class XmlTestRunListener {
    private List<XmlTestRunListener> hooks = new ArrayList<>();

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

public void addHook(XmlTestRunListener listener) {
    if (listener != null) {
        if (!this.hooks.contains(listener)) {
            this.hooks.add(listener);
        } else {
            throw new IllegalArgumentException("Listener already added.");
        }
    }
}
//<End of snippet n. 2>