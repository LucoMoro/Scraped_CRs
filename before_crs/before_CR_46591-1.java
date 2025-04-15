/*Add setter classes to UiAutomator to allow customization.

Also fix compile error with StubDevice.*/
//Synthetic comment -- diff --git a/src/com/android/tradefed/device/StubDevice.java b/src/com/android/tradefed/device/StubDevice.java
//Synthetic comment -- index 4fbb0e6..98f6f81 100644

//Synthetic comment -- @@ -383,4 +383,12 @@
AdbCommandRejectedException, IOException {
// ignore
}
}








//Synthetic comment -- diff --git a/src/com/android/tradefed/testtype/UiAutomatorTest.java b/src/com/android/tradefed/testtype/UiAutomatorTest.java
//Synthetic comment -- index e317949..60e134d 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import com.android.tradefed.util.RunUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
//Synthetic comment -- @@ -70,6 +71,9 @@
@Option(name = "runner-path", description = "path to uiautomator runner; may be null and "
+ "default will be used in this case")
private String mRunnerPath = null;
/**
* {@inheritDoc}
*/
//Synthetic comment -- @@ -90,14 +94,19 @@
mCaptureLogs = captureLogs;
}

/**
* {@inheritDoc}
*/
@Override
public void run(ITestInvocationListener listener) throws DeviceNotAvailableException {
        setTestRunner(new UiAutomatorRunner(getDevice().getIDevice(),
getTestJarPaths().toArray(new String[]{}),
                mClasses.toArray(new String[]{}), mRunnerPath));
preTestSetup();
getRunUtil().sleep(getSyncTime());
mRunner.setMaxtimeToOutputResponse(mTestTimeout);
//Synthetic comment -- @@ -206,13 +215,6 @@
}

/**
     * @param runner {@link UiAutomatorRunner} to set.
     */
    public void setTestRunner(UiAutomatorRunner runner) {
        mRunner = runner;
    }

    /**
* @return the test jar path.
*/
public List<String> getTestJarPaths() {
//Synthetic comment -- @@ -239,4 +241,18 @@
public void setTestRunArgMap(Map<String, String> runArgMap) {
mArgMap = runArgMap;
}
}







