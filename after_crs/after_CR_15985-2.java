/*Update the monkeyrunner to use the new DDMlib API.

Change-Id:I0c1e17b450e264ca1e7d98523bde4ddfe1d97252*/




//Synthetic comment -- diff --git a/tools/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java b/tools/monkeyrunner/src/com/android/monkeyrunner/MonkeyRunner.java
//Synthetic comment -- index 4734ba1..582373a 100644

//Synthetic comment -- @@ -16,11 +16,14 @@

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

//Synthetic comment -- @@ -176,6 +179,12 @@
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
//Synthetic comment -- @@ -245,7 +254,8 @@
* 
* @param name The name of the activity to launch 
*/
  public static void launch_activity(String name) throws IOException,
      TimeoutException, AdbCommandRejectedException, ShellCommandUnresponsiveException {
System.out.println("Launching: " + name);
recordCommand("Launching: " + name);
monkeyDevice.executeShellCommand("am start -a android.intent.action.MAIN -n " 
//Synthetic comment -- @@ -565,6 +575,14 @@
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







