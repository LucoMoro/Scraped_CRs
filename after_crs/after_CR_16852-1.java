/*Adding a smaller timeout to capturing nodes. This is so that it doesn't get stuck.

Change-Id:Ie2fd89d74a2a044334b45ba050f2c3150f63a8e7*/




//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index d00c4dc..20feeec 100644

//Synthetic comment -- @@ -513,6 +513,7 @@
DeviceConnection connection = null;
try {
connection = new DeviceConnection(window.getDevice());
            connection.getSocket().setSoTimeout(5000);
connection.sendCommand("CAPTURE " + window.encode() + " " + viewNode.toString()); //$NON-NLS-1$
return new Image(Display.getDefault(), connection.getSocket().getInputStream());
} catch (Exception e) {







