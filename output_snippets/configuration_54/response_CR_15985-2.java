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
import java.util.logging.Logger;

private static final Logger logger = Logger.getLogger("MonkeyRunnerLogger");

public static void launch_activity(String name) {
    logger.info("Launching: " + name);
    recordCommand("Launching: " + name);
    String command = "am start -a android.intent.action.MAIN -n " + sanitizeInput(name);

    try {
        monkeyDevice.executeShellCommand(command, new NullOutputReceiver());
    } catch (IOException ioe) {
        logger.severe("An error occurred while executing the command: " + ioe.getMessage());
        logger.info("Please ensure the activity name is correct and try again.");
    }
}

private static String sanitizeInput(String input) {
    // Sanitize input to prevent shell injection
    if (input == null || input.isEmpty()) {
        throw new IllegalArgumentException("Activity name cannot be null or empty");
    }
    return input.replaceAll("[^a-zA-Z0-9._-]", "");
}
//<End of snippet n. 0>