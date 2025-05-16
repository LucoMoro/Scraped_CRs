//<Beginning of snippet n. 0>

package com.android.monkeyrunner;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.Log.ILogOutput;
import com.android.ddmlib.Log.LogLevel;

private static final Logger logger = LoggerFactory.getLogger(MonkeyRunner.class);

String command = "monkey --port " + monkeyPort;
monkeyDevice.executeShellCommand(command, new NullOutputReceiver());

} catch(IOException ioe) {
    logger.error("An error occurred while executing shell command: {}", ioe.getMessage());
}
* 
* @param name The name of the activity to launch 
*/
public static void launch_activity(String name) throws IOException {
    logger.info("Launching: {}", name);
    recordCommand("Launching: " + name);
    monkeyDevice.executeShellCommand("am start -a android.intent.action.MAIN -n " + name);
    recordResponse("No frame buffer", "");
    printAndExit("Unable to get frame buffer: Unable to get frame buffer", true /* terminate */);
    return;
}

// device/adb not available?

//<End of snippet n. 0>