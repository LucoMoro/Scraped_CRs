/*cmds/am: Adds support for subintents.

In some situation we need to send broacast message with subintent
inside. So add this feature. Parameters:
 EXTRA_KEY - key for subintent
 EXTRA_SUBINTENT_ACTION - action for subintent
 SUBINTENT_URI_DATA - subintent data in URI format
Example:
 adb shell am broadcast -a com.android.launcher.action.INSTALL_SHORTCUT \
   --es android.intent.extra.shortcut.NAME Example_Name \
   --es android.intent.extra.shortcut.ICON_RESOURCE \
     'com.ui.testapp:drawable/ic_launcher' \
   --ent android.intent.extra.shortcut.INTENT \
     action=android.intent.action.MAIN \
	 'launchFlags=0x14000000;component=com.ui.testapp/.TestAppActivity;'
(This example create shortcut for app 'com.ui.testapp' on home screen)

Change-Id:I1b499e4ff4e1734ef604c8db06e84d9fe2abcbdc*/
//Synthetic comment -- diff --git a/cmds/am/src/com/android/commands/am/Am.java b/cmds/am/src/com/android/commands/am/Am.java
//Synthetic comment -- index fddb429..033bc7f 100644

//Synthetic comment -- @@ -182,6 +182,11 @@
} else if (opt.equals("--esn")) {
String key = nextArgRequired();
intent.putExtra(key, (String) null);
} else if (opt.equals("--ei")) {
String key = nextArgRequired();
String value = nextArgRequired();
//Synthetic comment -- @@ -1333,6 +1338,7 @@
"    [--eu <EXTRA_KEY> <EXTRA_URI_VALUE> ...]\n" +
"    [--eia <EXTRA_KEY> <EXTRA_INT_VALUE>[,<EXTRA_INT_VALUE...]]\n" +
"    [--ela <EXTRA_KEY> <EXTRA_LONG_VALUE>[,<EXTRA_LONG_VALUE...]]\n" +
"    [-n <COMPONENT>] [-f <FLAGS>]\n" +
"    [--grant-read-uri-permission] [--grant-write-uri-permission]\n" +
"    [--debug-log-resolution] [--exclude-stopped-packages]\n" +







