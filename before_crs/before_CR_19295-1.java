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








//Synthetic comment -- diff --git a/cmds/svc/src/com/android/commands/svc/Svc.java b/cmds/svc/src/com/android/commands/svc/Svc.java
//Synthetic comment -- index 1cd4c0d..1f95384 100644

//Synthetic comment -- @@ -94,6 +94,7 @@
COMMAND_HELP,
new PowerCommand(),
new DataCommand(),
            new WifiCommand()
};
}







