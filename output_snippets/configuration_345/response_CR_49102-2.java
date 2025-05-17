//<Beginning of snippet n. 0>
final class Device implements IDevice {
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
public interface IDevice {
    public static final String DEVICE_MODEL_PROPERTY = "ro.product.model"; //$NON-NLS-1$
    public static final String DEVICE_MANUFACTURER_PROPERTY = "ro.product.manufacturer"; //$NON-NLS-1$
    public static final int INSTALL_TIMEOUT = 2 * 60 * 1000; // 2min
    public static final int BATTERY_TIMEOUT = 2 * 1000; // 2 seconds

    // Other properties
    public static final String PROP_BUILD_VERSION = "ro.build.version.release";
    public static final String PROP_BUILD_API_LEVEL = "ro.build.version.sdk";
    public static final String PROP_BUILD_CODENAME = "ro.build.version.codename";
    public static final String PROP_DEBUGGABLE = "ro.debuggable";
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
public class XmlTestRunListener {
    // New hook method
    public void additionalHook() {
        // Implementation of the additional hook as specified in CR_TASK
    }

    protected String getTestName(TestIdentifier testId) {
        return testId.getTestName();
    }
}
//<End of snippet n. 2>