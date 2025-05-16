//<Beginning of snippet n. 0>

package com.android.monkeyrunner;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.Log.ILogOutput;
import com.android.ddmlib.Log.LogLevel;

String command = "monkey --port " + monkeyPort;
monkeyDevice.executeShellCommand(command, new NullOutputReceiver());

} catch(IOException e) {
    Log.e("Error executing shell command", "IOException occurred", e);
}
* 
* @param name The name of the activity to launch 
*/
public static void launch_activity(String name) throws IOException {
    Log.i("ActivityLaunch", "Launching activity: " + name);
    recordCommand("Launching activity: " + name);
    monkeyDevice.executeShellCommand("am start -a android.intent.action.MAIN -n " + name);
    recordResponse("No frame buffer", "");
    printAndExit("Unable to get frame buffer: " + ioe.getMessage(), true /* terminate */);
    return;
}

// device/adb not available?

//<End of snippet n. 0>