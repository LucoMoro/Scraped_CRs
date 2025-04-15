/*Add a reboot command to IDevice to allow ddmlib users to reboot devices.

Change-Id:I8f8b792c68ec869980805c06aecf249558c6dc3f*/




//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java b/ddms/libs/ddmlib/src/com/android/ddmlib/AdbHelper.java
//Synthetic comment -- index ce8d366..7b39076 100644

//Synthetic comment -- @@ -728,4 +728,36 @@
}

}

    /**
     * Reboot the device.
     *
     * @param into what to reboot into (recovery, bootloader).  Or null to just reboot.
     */
    public static void reboot(String into, InetSocketAddress adbSockAddr,
            Device device) throws IOException {
        byte[] request;
        if (into == null) {
            request = formAdbRequest("reboot:"); //$NON-NLS-1$
        } else {
            request = formAdbRequest("reboot:" + into); //$NON-NLS-1$
        }

        SocketChannel adbChan = null;
        try {
            adbChan = SocketChannel.open(adbSockAddr);
            adbChan.configureBlocking(false);

            // if the device is not -1, then we first tell adb we're looking to talk
            // to a specific device
            setDevice(adbChan, device);

            if (write(adbChan, request) == false)
                throw new IOException("failed asking for reboot");
        } finally {
            if (adbChan != null) {
                adbChan.close();
            }
        }
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Device.java
//Synthetic comment -- index 4223248..a601c16 100644

//Synthetic comment -- @@ -516,4 +516,12 @@
executeShellCommand("pm uninstall " + packageName, receiver);
return receiver.getErrorMessage();
}

    /*
     * (non-Javadoc)
     * @see com.android.ddmlib.IDevice#reboot()
     */
    public void reboot(String into) throws IOException {
        AdbHelper.reboot(into, AndroidDebugBridge.getSocketAddress(), this);
    }
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java b/ddms/libs/ddmlib/src/com/android/ddmlib/IDevice.java
//Synthetic comment -- index 47db08a..bd6f436 100755

//Synthetic comment -- @@ -275,4 +275,11 @@
*/
public String uninstallPackage(String packageName) throws IOException;

    /**
     * Reboot the device.
     *
     * @param into the bootloader name to reboot into, or null to just reboot the device.
     * @throws IOException
     */
    public void reboot(String into) throws IOException;
}








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java b/ddms/libs/ddmlib/tests/src/com/android/ddmlib/testrunner/RemoteAndroidTestRunnerTest.java
//Synthetic comment -- index 4727153..d365248 100644

//Synthetic comment -- @@ -248,6 +248,9 @@
throw new UnsupportedOperationException();
}

        public void reboot(String into) throws IOException {
            throw new UnsupportedOperationException();
        }
}

/**







