//<Beginning of snippet n. 0>
package com.android.monkeyrunner;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;

import java.io.IOException;
import java.util.regex.Pattern;

public static void launch_activity(String name) throws IOException {
    Log.info("Launching: " + name);
    recordCommand("Launching: " + name);
    String command = "am start -a android.intent.action.MAIN -n " + name;
    monkeyDevice.executeShellCommand(sanitizeCommand(command), new NullOutputReceiver());
    
    try {
        recordResponse("No frame buffer", "");
    } catch (IOException ioe) {
        Log.warn("Unable to get frame buffer. Please check the device status and try again."); 
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