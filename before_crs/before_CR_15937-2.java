/*--device option sometimes ignored on start command
when used with --test option

the value fetched from the option was overridden from
a value retrieved from a previous session.  this change
lets the --device option have the final say.

Change-Id:Ib4db83ae9500b25b522965eaba17df9521f2eba2*/
//Synthetic comment -- diff --git a/tools/host/src/com/android/cts/ConsoleUi.java b/tools/host/src/com/android/cts/ConsoleUi.java
//Synthetic comment -- index 679ecc0..951e3e9 100644

//Synthetic comment -- @@ -451,15 +451,6 @@
return;
}

            if (cp.containsKey(CTSCommand.OPTION_DEVICE)) {
                deviceId = cp.getValue(CTSCommand.OPTION_DEVICE);
                String[] deviceIdList = deviceId.trim().split(",");
                if (deviceIdList.length > 1) {
                    Log.e("Just allow choosing one device ID.", null);
                    return;
                }
            }

ActionType actionType = ActionType.START_NEW_SESSION;
if (cp.containsKey(CTSCommand.OPTION_TEST)) {
testName = cp.getValue(CTSCommand.OPTION_TEST);
//Synthetic comment -- @@ -493,6 +484,15 @@
}
}

if (deviceId == null) {
TestDevice td = mHost.getFirstAvailableDevice();
if (td == null) {







