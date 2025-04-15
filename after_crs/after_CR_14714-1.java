/*Command-line tool to authorise and deauthorise remote control applications

Add a module to the "svc" command-line tool which can add and remove
authorised keys to/from the RemoteControlService's database. This can
be used from "adb shell" (but not from other shells).

To add an authorised application:

$ svc remote add com.realvnc.androidsampleserver

To revoke an application's authorisation:

$ svc remote del com.realvnc.androidsampleserver

Change-Id:I7ded8ead26c8559db2b895f3e6d995dd6661e3e0*/




//Synthetic comment -- diff --git a/cmds/svc/src/com/android/commands/svc/RemoteControlCommand.java b/cmds/svc/src/com/android/commands/svc/RemoteControlCommand.java
new file mode 100644
//Synthetic comment -- index 0000000..f6a1509

//Synthetic comment -- @@ -0,0 +1,58 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.commands.svc;

import android.os.RemoteControl;

public class RemoteControlCommand extends Svc.Command {
    public RemoteControlCommand() {
        super("remote");
    }

    public String shortHelp() {
        return "Control the remote control service";
    }

    public String longHelp() {
        return shortHelp() + "\n"
                + "\n"
                + "usage: svc remote add <app>\n"
                + "         Allow <app> to use remote control\n\n"
                + "       svc remote del <app>\n"
                + "         Disallow <app> from using remote control\n";
    }

    public void run(String[] args) {

        if(args.length >= 2) {
            try {
                if("add".equals(args[1]) && (args.length == 3)) {
                    RemoteControl.addAuthorisedApplication(args[2]);
                    return;
                } else if("del".equals(args[1]) && (args.length == 3)) {
                    RemoteControl.removeAuthorisedApplication(args[2]);
                    return;
                }
            } catch(SecurityException e) {
                System.err.println("Permission denied");
                return;
            }
        }

        System.err.println(longHelp());
   }
}








//Synthetic comment -- diff --git a/cmds/svc/src/com/android/commands/svc/Svc.java b/cmds/svc/src/com/android/commands/svc/Svc.java
//Synthetic comment -- index 1cd4c0d..7182666 100644

//Synthetic comment -- @@ -94,6 +94,7 @@
COMMAND_HELP,
new PowerCommand(),
new DataCommand(),
            new WifiCommand(),
            new RemoteControlCommand()
};
}







