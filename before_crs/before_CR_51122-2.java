/*Don't cache device name obtained when device is offline

When a device is connected, it goes from offline to online status
in a short time. However, the name for the device (obtained by
reading device's system properties) is cached the first time it is
requested. This defaults to just the serial number since the
device properties can't be read when the device is offline. This CL
just makes sure that the device name is cached only when the device
is online.

Change-Id:Iff11f46ed9e8b25508b72cc002cd57741f7b6c8d*/
//Synthetic comment -- diff --git a/ddmlib/src/main/java/com/android/ddmlib/Device.java b/ddmlib/src/main/java/com/android/ddmlib/Device.java
//Synthetic comment -- index 85d1d8d..ee59808 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -191,6 +193,10 @@

@Override
public String getName() {
if (mName == null) {
mName = constructName();
}
//Synthetic comment -- @@ -207,10 +213,18 @@
return getSerialNumber();
}
} else {
            String manufacturer = cleanupStringForDisplay(
                    getProperty(PROP_DEVICE_MANUFACTURER));
            String model = cleanupStringForDisplay(
                    getProperty(PROP_DEVICE_MODEL));

StringBuilder sb = new StringBuilder(20);

//Synthetic comment -- @@ -310,12 +324,20 @@
@Override
public String getPropertySync(String name) throws TimeoutException,
AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        CollectingOutputReceiver receiver = new CollectingOutputReceiver();
executeShellCommand(String.format("getprop '%s'", name), receiver, GETPROP_TIMEOUT);
String value = receiver.getOutput().trim();
if (value.isEmpty()) {
return null;
}
return value;
}








