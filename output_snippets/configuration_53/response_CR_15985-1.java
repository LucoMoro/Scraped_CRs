//<Beginning of snippet n. 0>
package com.android.monkeyrunner;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;

import java.io.IOException;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

public static void launch_activity(String name) {
    Logger logger = Logger.getLogger(launch_activity.class.getName());
    
    try {
        logger.log(Level.INFO, "Launching: {0}", name);
        recordCommand("Launching: " + name);
        String command = "am start -a android.intent.action.MAIN -n " + name;
        monkeyDevice.executeShellCommand(sanitizeCommand(command), new NullOutputReceiver());
        
        recordResponse("No frame buffer", "");
    } catch (IOException ioe) {
        logger.log(Level.WARNING, "Unable to get frame buffer. Please check the device status and try again.", ioe);
        throw new RuntimeException("Failed to launch activity. Please ensure the device is connected and try again.", ioe);
    } catch (IllegalArgumentException iae) {
        logger.log(Level.SEVERE, "Error in command: {0}", iae.getMessage());
        throw new RuntimeException("Invalid command provided. Please check your input.", iae);
    }
}

private static String sanitizeCommand(String command) {
    if (command == null || command.isEmpty()) {
        throw new IllegalArgumentException("Command cannot be null or empty");
    }
    String sanitized = command.replaceAll("[\\\"'\\<\\>\\&;]", ""); 
    if (!isValidShellCommand(sanitized)) {
        throw new IllegalArgumentException("Command contains invalid characters.");
    }
    return sanitized;
}

private static boolean isValidShellCommand(String command) {
    String regex = "^[a-zA-Z0-9_\\-\\s]+$"; 
    return Pattern.matches(regex, command);
}
//<End of snippet n. 0>