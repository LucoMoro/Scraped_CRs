/*Command line tool to enable and disable device administrators

For automated testing purposes it is useful to be able to enable and
disable device administrator applications (android.app.admin) without
interacting with the user.

This patch adds a command:

$ svc admin [enable|disable] <name>

This takes a component name as a parameter and attempts to enable or
disable the corresponding administrator.

The command will only succeed when run from a root shell (because root
implicitly has the BIND_DEVICE_ADMIN permission), so it is available
to tests running on a host PC, via 'adb shell su -c [...]', but not to
normal applications.

Change-Id:Iae06fcd98e7277c898ba3a01183326879a5b78c4*/




//Synthetic comment -- diff --git a/cmds/svc/src/com/android/commands/svc/AdminCommand.java b/cmds/svc/src/com/android/commands/svc/AdminCommand.java
new file mode 100644
//Synthetic comment -- index 0000000..7396c3b

//Synthetic comment -- @@ -0,0 +1,63 @@
/*
 * Copyright (C) 2008 The Android Open Source Project
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

import android.app.admin.IDevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.RemoteException;
import android.os.ServiceManager;

public class AdminCommand extends Svc.Command {
    public AdminCommand() {
        super("admin");
    }

    public String shortHelp() {
        return "Control the device policy manager";
    }

    public String longHelp() {
        return shortHelp() + "\n"
                + "\n"
                + "usage: svc admin [enable|disable] <name>\n"
                + "         Enable or disable a device policy manager.\n\n";
    }

    public void run(String[] args) {
        if (args.length >= 3) {

            ComponentName name = ComponentName.unflattenFromString(args[2]);

            IDevicePolicyManager dpm = IDevicePolicyManager.Stub.asInterface(
                ServiceManager.getService(Context.DEVICE_POLICY_SERVICE));

            try {
                if ("enable".equals(args[1])) {
                    dpm.setActiveAdmin(name);
                    return;
                } else if ("disable".equals(args[1])) {
                    dpm.removeActiveAdmin(name);
                    return;
                }
            } catch(RemoteException e) {
                e.printStackTrace();
            }
        }
        System.err.println(longHelp());
    }
}








//Synthetic comment -- diff --git a/cmds/svc/src/com/android/commands/svc/Svc.java b/cmds/svc/src/com/android/commands/svc/Svc.java
//Synthetic comment -- index 1cd4c0d..1f95384 100644

//Synthetic comment -- @@ -94,6 +94,7 @@
COMMAND_HELP,
new PowerCommand(),
new DataCommand(),
            new WifiCommand(),
            new AdminCommand()
};
}







