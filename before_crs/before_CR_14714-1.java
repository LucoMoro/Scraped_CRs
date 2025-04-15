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








//Synthetic comment -- diff --git a/cmds/svc/src/com/android/commands/svc/Svc.java b/cmds/svc/src/com/android/commands/svc/Svc.java
//Synthetic comment -- index 1cd4c0d..7182666 100644

//Synthetic comment -- @@ -94,6 +94,7 @@
COMMAND_HELP,
new PowerCommand(),
new DataCommand(),
            new WifiCommand()
};
}







