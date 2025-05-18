//<Beginning of snippet n. 0>

package com.android.monkeyrunner;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.Log.ILogOutput;
import com.android.ddmlib.Log.LogLevel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

private static final Logger logger = Logger.getLogger("MonkeyRunnerLogger");

public static void launch_activity(String name) {
    try {
        logger.log(Level.INFO, "Launching: {0}", name);
        recordCommand("Launching: " + name);
        String command = "am start -a android.intent.action.MAIN -n " + name;
        AndroidDebugBridge.getBridge().getDevice().executeShellCommand(command, new NullOutputReceiver());
    } catch (IOException ioe) {
        logger.log(Level.WARNING, "An error occurred while launching the activity. Please check your parameters and try again.");
        // Suggest recovery options: Ensure the device is connected and the activity name is correct.
    }
}

//<End of snippet n. 0>