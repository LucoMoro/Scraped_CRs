
//<Beginning of snippet n. 0>



package com.android.monkeyrunner;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log;
import com.android.ddmlib.NullOutputReceiver;
import com.android.ddmlib.RawImage;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.TimeoutException;
import com.android.ddmlib.Log.ILogOutput;
import com.android.ddmlib.Log.LogLevel;

String command = "monkey --port " + monkeyPort;
monkeyDevice.executeShellCommand(command, new NullOutputReceiver());

    } catch(TimeoutException e) {
      e.printStackTrace();
    } catch (AdbCommandRejectedException e) {
      e.printStackTrace();
    } catch (ShellCommandUnresponsiveException e) {
      e.printStackTrace();
} catch(IOException e) {
e.printStackTrace();
}
* 
* @param name The name of the activity to launch 
*/
  public static void launch_activity(String name) throws IOException,
      TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException {
System.out.println("Launching: " + name);
recordCommand("Launching: " + name);
monkeyDevice.executeShellCommand("am start -a android.intent.action.MAIN -n " 
recordResponse("No frame buffer", "");
printAndExit("Unable to get frame buffer: " + ioe.getMessage(), true /* terminate */);
return;
    } catch (TimeoutException toe) {
      recordResponse("No frame buffer", "");
      printAndExit("Unable to get frame buffer: timeout", true /* terminate */);
      return;
    } catch (AdbCommandRejectedException acre) {
      recordResponse("No frame buffer", "");
      printAndExit("Unable to get frame buffer: " + acre.getMessage(), true /* terminate */);
      return;
}

// device/adb not available?

//<End of snippet n. 0>








