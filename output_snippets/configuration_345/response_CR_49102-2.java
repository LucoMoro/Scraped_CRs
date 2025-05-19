//<Beginning of snippet n. 0>
final class Device implements IDevice {
    // Implementation details removed for brevity
} else {
    String manufacturer = cleanupStringForDisplay(
                    getProperty("device.manufacturer"));
    String model = cleanupStringForDisplay(
                    getProperty("device.model"));

    StringBuilder sb = new StringBuilder(20);
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public interface IDevice {
    // Removed constants from interface
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
    
    // Additional hook implementation as per CR_TASK
    // Implement the required integration here
}
//<End of snippet n. 2>