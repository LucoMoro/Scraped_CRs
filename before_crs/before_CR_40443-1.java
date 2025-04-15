/*ndk: Remove redundant implementation of IShellOutputReceiver

Change-Id:I43add9790edf1418cd821f3486035f495fe45ad2*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchDelegate.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/launch/NdkGdbLaunchDelegate.java
//Synthetic comment -- index 57d96d7..818a5c1 100644

//Synthetic comment -- @@ -19,9 +19,9 @@
import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.Client;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.IDevice.DeviceUnixSocketNamespace;
import com.android.ddmlib.IShellOutputReceiver;
import com.android.ddmlib.InstallException;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
//Synthetic comment -- @@ -233,7 +233,7 @@
activityName);
try {
CountDownLatch launchedLatch = new CountDownLatch(1);
            ShellOutputReceiver receiver = new ShellOutputReceiver(launchedLatch);
device.executeShellCommand(command, receiver);
launchedLatch.await(5, TimeUnit.SECONDS);
String shellOutput = receiver.getOutput();
//Synthetic comment -- @@ -479,39 +479,9 @@
String command = String.format("run-as %s /system/bin/sh -c pwd", app); //$NON-NLS-1$

CountDownLatch commandCompleteLatch = new CountDownLatch(1);
        ShellOutputReceiver receiver = new ShellOutputReceiver(commandCompleteLatch);
device.executeShellCommand(command, receiver);
commandCompleteLatch.await(timeout, timeoutUnit);
return receiver.getOutput().trim();
}

    private static class ShellOutputReceiver implements IShellOutputReceiver {
        private StringBuffer sb = new StringBuffer();
        private CountDownLatch mCompleteLatch;

        public ShellOutputReceiver(CountDownLatch commandCompleteLatch) {
            mCompleteLatch = commandCompleteLatch;
        }

        @Override
        public void addOutput(byte[] data, int offset, int length) {
            sb.append(new String(data, offset, length));
        }

        @Override
        public void flush() {
            if (mCompleteLatch != null) {
                mCompleteLatch.countDown();
            }
        }

        @Override
        public boolean isCancelled() {
            return false;
        }

        public String getOutput() {
            return sb.toString();
        }
    }
}







