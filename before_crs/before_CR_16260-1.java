/*Monkey: Set the name of the process to distinguish from generic name (app_process)

Change-Id:If58ff437bfbea421dc00a2d85547267c010871e4*/
//Synthetic comment -- diff --git a/cmds/monkey/src/com/android/commands/monkey/Monkey.java b/cmds/monkey/src/com/android/commands/monkey/Monkey.java
//Synthetic comment -- index 2f8d874..7c75776 100644

//Synthetic comment -- @@ -357,6 +357,8 @@
// Set ro.monkey if it's not set yet.
SystemProperties.set("ro.monkey", "true");

int resultCode = (new Monkey()).run(args);
System.exit(resultCode);
}







