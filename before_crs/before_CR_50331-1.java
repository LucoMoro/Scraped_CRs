/*issue:42906 sometimes CTS cannot detect the device connection after reboot
author:zheng xiaolei<zhengxiaolei201209@gmail.com>

CTS will try to reboot device every 200 case. And sometimes, CTS
cannot detect the device connection after reboot.
it should be a common issue. The CTS reboot the device as below:
1. CTS issue a command "adb shell reboot"to reboot device.
2. CTS will wait for around 5 seconds, then disconnect the
AndroidDebugBridge and exec the command "adb kill-server".
3. CTS start the adb server, and keep query whether the device's adb
usb device present in host Linux pc directory "/dev/bus/usb".
4. Once the adb server in host side detect the device's adb, it will
create a usb transport for this device and update CTS with transport.
At this time the usb transport's connection_state is "CS_OFFLINE"
5. Adb server will issue the command "SYNC"and "CNXN" to device
through adb usb connection. After adb server receive the reponse
"CNXN"from device's adb usb connection, it will set the usb
transport's connection_state to "CS_DEVICE" state.
6. CTS will send a msg "host:transport:serialnumber" to adb server.
And adb server will reponse this msg with "OK"if the usb transport's
connection_state in "CS_DEVICE" state, or with "FAIL" if the usb
transport's connection_state in "CS_OFFLINE" state.
7. If the adb server return "OK" in step 6, the CTS can go forward
the test case.
8. If the adb server return "FAIL" in step 6, It will cause a
AdbCommandRejectedException exception in DeviceServiceMonitor::run().
This will block the CTS to connect with device adb.
The root cause is once the step 4 has been finished,  the step 6(msg
"host:transport:serialnumber") may be occurred before the step
5(command&reponse "SYNC"&"CNXN") finished. So adb server will return
"FAIL"msg to the command "host:transport:serialnumber", which will
make CTS failed to finish the reboot function.
The fix should be in host side adb or CTS code to make sure the
command "host:transport:serialnumber"only issue when "SYNC"and
"CNXN"response has been proceed.*/
//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/DeviceManager.java b/tools/host/src/com/android/cts/DeviceManager.java
//Synthetic comment -- index c794332..47981f3 100644

//Synthetic comment -- @@ -197,6 +197,47 @@

/** {@inheritDoc} */
public void deviceConnected(IDevice device) {
new DeviceServiceMonitor(device).start();
}








