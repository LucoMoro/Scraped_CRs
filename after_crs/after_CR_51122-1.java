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
//Synthetic comment -- index 85d1d8d..5fc8db5 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Synthetic comment -- @@ -191,6 +193,10 @@

@Override
public String getName() {
        if (isOffline()) {
            return getSerialNumber();
        }

if (mName == null) {
mName = constructName();
}
//Synthetic comment -- @@ -207,10 +213,18 @@
return getSerialNumber();
}
} else {
            String manufacturer = null;
            String model = null;

            try {
                manufacturer = cleanupStringForDisplay(
                    getPropertyCacheOrSync(PROP_DEVICE_MANUFACTURER));
                model = cleanupStringForDisplay(
                    getPropertyCacheOrSync(PROP_DEVICE_MODEL));
            } catch (Exception e) {
                // If there are exceptions thrown while attempting to get these properties,
                // we can just use the serial number, so ignore these exceptions.
            }

StringBuilder sb = new StringBuilder(20);

//Synthetic comment -- @@ -310,12 +324,20 @@
@Override
public String getPropertySync(String name) throws TimeoutException,
AdbCommandRejectedException, ShellCommandUnresponsiveException, IOException {
        CountDownLatch latch = new CountDownLatch(1);
        CollectingOutputReceiver receiver = new CollectingOutputReceiver(latch);
executeShellCommand(String.format("getprop '%s'", name), receiver, GETPROP_TIMEOUT);
        try {
            latch.await(GETPROP_TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            return null;
        }

String value = receiver.getOutput().trim();
if (value.isEmpty()) {
return null;
}

return value;
}








