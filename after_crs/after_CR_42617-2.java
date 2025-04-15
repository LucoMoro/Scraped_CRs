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

    // client's user id (on device in a multi user environment)
    private int mUserId;

    // client's user id is valid
    private boolean mValidUserId;

// how interested are we in a debugger?
private DebuggerStatus mDebuggerInterest;

//Synthetic comment -- @@ -430,6 +436,23 @@
}

/**
     * Returns the client's user id.
     * @return user id if set, -1 otherwise
     */
    public int getUserId() {
        return mUserId;
    }

    /**
     * Returns true if the user id of this client was set. Only devices that support multiple
     * users will actually return the user id to ddms. For other/older devices, this will not
     * be set.
     */
    public boolean isValidUserId() {
        return mValidUserId;
    }

    /**
* Sets client description.
*
* There may be a race between HELO and APNM.  Rather than try
//Synthetic comment -- @@ -451,6 +474,11 @@
}
}

    void setUserId(int id) {
        mUserId = id;
        mValidUserId = true;
    }

/**
* Returns the debugger connection status.
*/








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/HandleAppName.java b/ddms/libs/ddmlib/src/com/android/ddmlib/HandleAppName.java
//Synthetic comment -- index c821dfc..da4ade3 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ddmlib;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
//Synthetic comment -- @@ -77,11 +78,32 @@
appNameLen = data.getInt();
appName = getString(data, appNameLen);

        // Newer devices send user id in the APNM packet.
        int userId = -1;
        boolean validUserId = false;
        if (data.hasRemaining()) {
            try {
                userId = data.getInt();
                validUserId = true;
            } catch (BufferUnderflowException e) {
                // two integers + utf-16 string
                int expectedPacketLength = 8 + appNameLen * 2;

                Log.e("ddm-appname", "Insufficient data in APNM chunk to retrieve user id.");
                Log.e("ddm-appname", "Actual chunk length: " + data.capacity());
                Log.e("ddm-appname", "Expected chunk length: " + expectedPacketLength);
            }
        }

Log.d("ddm-appname", "APNM: app='" + appName + "'");

ClientData cd = client.getClientData();
synchronized (cd) {
cd.setClientDescription(appName);

            if (validUserId) {
                cd.setUserId(userId);
            }
}

client = checkDebuggerPortForAppName(client, appName);








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/HandleHello.java b/ddms/libs/ddmlib/src/com/android/ddmlib/HandleHello.java
//Synthetic comment -- index 4e62bca..b5c2968 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.ddmlib;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
//Synthetic comment -- @@ -100,6 +101,23 @@
vmIdent = getString(data, vmIdentLen);
appName = getString(data, appNameLen);

        // Newer devices send user id in the APNM packet.
        int userId = -1;
        boolean validUserId = false;
        if (data.hasRemaining()) {
            try {
                userId = data.getInt();
                validUserId = true;
            } catch (BufferUnderflowException e) {
                // five integers + two utf-16 strings
                int expectedPacketLength = 20 + appNameLen * 2 + vmIdentLen * 2;

                Log.e("ddm-hello", "Insufficient data in HELO chunk to retrieve user id.");
                Log.e("ddm-hello", "Actual chunk length: " + data.capacity());
                Log.e("ddm-hello", "Expected chunk length: " + expectedPacketLength);
            }
        }

Log.d("ddm-hello", "HELO: v=" + version + ", pid=" + pid
+ ", vm='" + vmIdent + "', app='" + appName + "'");

//Synthetic comment -- @@ -110,6 +128,10 @@
cd.setVmIdentifier(vmIdent);
cd.setClientDescription(appName);
cd.isDdmAware(true);

                if (validUserId) {
                    cd.setUserId(userId);
                }
} else {
Log.e("ddm-hello", "Received pid (" + pid + ") does not match client pid ("
+ cd.getPid() + ")");








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java
//Synthetic comment -- index 27c844d..5c649ba 100644

//Synthetic comment -- @@ -49,6 +49,7 @@
import org.eclipse.swt.widgets.TreeItem;

import java.util.ArrayList;
import java.util.Locale;

/**
* A display of both the devices and their clients.
//Synthetic comment -- @@ -244,7 +245,11 @@
case CLIENT_COL_NAME:
String name = cd.getClientDescription();
if (name != null) {
                            if (cd.isValidUserId()) {
                                return String.format(Locale.US, "%s (%d)", name, cd.getUserId());
                            } else {
                                return name;
                            }
}
return "?";
case CLIENT_COL_PID:







