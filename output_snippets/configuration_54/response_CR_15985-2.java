//<Beginning of snippet n. 0>

package com.android.monkeyrunner;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.Log.ILogOutput;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.IDevice.DeviceState;

import java.io.IOException;

String command = "monkey --port " + monkeyPort;
try {
    monkeyDevice.executeShellCommand(command, new NullOutputReceiver());
} catch(IOException e) {
    e.printStackTrace();
}

/**
 * Launches the specified activity.
 * 
 * @param name The name of the activity to launch 
 * @throws IOException If an I/O error occurs
 */
public static void launch_activity(String name) throws IOException {
    System.out.println("Launching: " + name);
    recordCommand("Launching: " + name);
    String cmd = "am start -a android.intent.action.MAIN -n " + name;
    try {
        monkeyDevice.executeShellCommand(cmd, new NullOutputReceiver());
    } catch (IOException ioe) {
        recordResponse("No frame buffer", "");
        printAndExit("Unable to get frame buffer: " + ioe.getMessage(), true /* terminate */);
    }
}

// device/adb not available?

//<End of snippet n. 0>