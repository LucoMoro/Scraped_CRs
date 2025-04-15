/*ddms: report user id if available

The HELO and APNM packets may append the user id at the end of the
packet. If that data is available, display it as part of the
application name.

Change-Id:Ie353c2cc2895db649fc6ab6054f1c88f5d2f247f*/
//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/ClientData.java b/ddms/libs/ddmlib/src/com/android/ddmlib/ClientData.java
//Synthetic comment -- index d1156a4..ff83c37 100644

//Synthetic comment -- @@ -156,6 +156,12 @@
// client's self-description
private String mClientDescription;

// how interested are we in a debugger?
private DebuggerStatus mDebuggerInterest;

//Synthetic comment -- @@ -430,6 +436,23 @@
}

/**
* Sets client description.
*
* There may be a race between HELO and APNM.  Rather than try
//Synthetic comment -- @@ -451,6 +474,11 @@
}
}

/**
* Returns the debugger connection status.
*/








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/HandleAppName.java b/ddms/libs/ddmlib/src/com/android/ddmlib/HandleAppName.java
//Synthetic comment -- index c821dfc..da4ade3 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ddmlib;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
//Synthetic comment -- @@ -77,11 +78,32 @@
appNameLen = data.getInt();
appName = getString(data, appNameLen);

Log.d("ddm-appname", "APNM: app='" + appName + "'");

ClientData cd = client.getClientData();
synchronized (cd) {
cd.setClientDescription(appName);
}

client = checkDebuggerPortForAppName(client, appName);








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/HandleHello.java b/ddms/libs/ddmlib/src/com/android/ddmlib/HandleHello.java
//Synthetic comment -- index 4e62bca..b5c2968 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ddmlib;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
//Synthetic comment -- @@ -100,6 +101,23 @@
vmIdent = getString(data, vmIdentLen);
appName = getString(data, appNameLen);

Log.d("ddm-hello", "HELO: v=" + version + ", pid=" + pid
+ ", vm='" + vmIdent + "', app='" + appName + "'");

//Synthetic comment -- @@ -110,6 +128,10 @@
cd.setVmIdentifier(vmIdent);
cd.setClientDescription(appName);
cd.isDdmAware(true);
} else {
Log.e("ddm-hello", "Received pid (" + pid + ") does not match client pid ("
+ cd.getPid() + ")");








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java
//Synthetic comment -- index 27c844d..5c649ba 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
import org.eclipse.swt.widgets.TreeItem;

import java.util.ArrayList;

/**
* A display of both the devices and their clients.
//Synthetic comment -- @@ -244,7 +245,11 @@
case CLIENT_COL_NAME:
String name = cd.getClientDescription();
if (name != null) {
                            return name;
}
return "?";
case CLIENT_COL_PID:







