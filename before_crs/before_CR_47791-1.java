/*monitor: Prefer using bundled SDK rather than lastSdk

Also fix the path to bundled SDK: monitor is in tools/lib/monitor,
not tools/monitor

Change-Id:Ia47112b8358076a8d93cc6492bc67490386459e7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorApplication.java b/eclipse/plugins/com.android.ide.eclipse.monitor/src/com/android/ide/eclipse/monitor/MonitorApplication.java
//Synthetic comment -- index ef8f186..425786f 100644

//Synthetic comment -- @@ -122,26 +122,30 @@
return sdkLocation;
}

// check for the last used SDK
sdkLocation = MonitorPlugin.getDdmsPreferenceStore().getLastSdkPath();
if (isValidSdkLocation(sdkLocation)) {
return sdkLocation;
}

        // The monitor app should be located in "<sdk>/tools/monitor/"
        // So see if the folder one level up from the install location is a valid SDK.
        Location install = Platform.getInstallLocation();
        if (install != null && install.getURL() != null) {
            String toolsFolder = new File(install.getURL().getFile()).getParent();
            if (toolsFolder != null) {
                sdkLocation = new File(toolsFolder).getParent();
                if (isValidSdkLocation(sdkLocation)) {
                    MonitorPlugin.getDdmsPreferenceStore().setLastSdkPath(sdkLocation);
                    return sdkLocation;
                }
            }
        }

// if nothing else works, prompt the user
sdkLocation = getSdkLocationFromUser(new Shell(display));
if (isValidSdkLocation(sdkLocation)) {







