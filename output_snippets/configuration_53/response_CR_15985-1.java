//<Beginning of snippet n. 0>

package com.android.monkeyrunner;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.Log.ILogOutput;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmlib.IDeviceShellCommandReceiver; // Updated import

String command = "monkey --port " + monkeyPort;
IDeviceShellCommandReceiver shellReceiver = new NullOutputReceiver();
monkeyDevice.executeShellCommand(command, shellReceiver); // Updated command execution

} catch(IOException ioe) { // Updated variable name for consistency
    ioe.printStackTrace(); // Enhanced error handling
}

/**
 * Launches the specified activity.
 *
 * @param name The name of the activity to launch 
 */
public static void launch_activity(String name) throws IOException {
    System.out.println("Launching: " + name);
    recordCommand("Launching: " + name);
    
    String launchCommand = "am start -a android.intent.action.MAIN -n " + name; // Fixed command
    IDeviceShellCommandReceiver shellReceiver = new NullOutputReceiver();
    
    try {
        monkeyDevice.executeShellCommand(launchCommand, shellReceiver); // Updated command execution
    } catch (IOException ioe) { // Improved error handling
        recordResponse("No frame buffer", "");
        printAndExit("Unable to get frame buffer: " + ioe.getMessage(), true); // Simplified termination logic
        return;
    }
}

// device/adb not available?

//<End of snippet n. 0>