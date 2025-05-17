//<Beginning of snippet n. 0>
package com.android.monkeyrunner;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.NullOutputReceiver;
import java.io.IOException;

public class MonkeyRunner {

    private static IDevice monkeyDevice;
    private static int monkeyPort;

    public static void launch_activity(String name) {
        Log.info("Launching: " + name);
        recordCommand("Launching: " + name);
        String command = "am start -a android.intent.action.MAIN -n " + name;
        
        try {
            monkeyDevice.executeShellCommand(command, new NullOutputReceiver());
            recordResponse("No frame buffer", "");
        } catch (IOException ioe) {
            Log.error("Unable to get frame buffer: An error occurred.");
            // Graceful error handling, no abrupt termination
        }
    }
}
//<End of snippet n. 0>