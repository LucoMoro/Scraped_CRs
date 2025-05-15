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
    Log.e("MonkeyRunner", "Failed to execute command: " + e.getMessage(), e);
}

// ...

/**
 * @param name The name of the activity to launch 
 */
public static void launch_activity(String name) throws IOException {
    System.out.println("Launching: " + name);
    recordCommand("Launching: " + name);
    monkeyDevice.executeShellCommand("am start -a android.intent.action.MAIN -n " + name, new NullOutputReceiver());
    recordResponse("No frame buffer", "");
    printAndExit("Unable to get frame buffer: " + ioe.getMessage(), true); 
    return;
}

// ...

//<End of snippet n. 0>