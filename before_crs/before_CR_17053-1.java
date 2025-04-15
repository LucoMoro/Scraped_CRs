/*Fix interdepency between ADT and DDMS.

The issue was due to DDMS instantiating ADT classes
in start() when ADT was doing the same with DDMS classes.

This prevented the plug-ins from loading.

Change-Id:Ifea9e0fdcfb14581931d734cdd232c537537c30d*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AdtPlugin.java
//Synthetic comment -- index afe04ae..4f2e649 100644

//Synthetic comment -- @@ -212,6 +212,7 @@
}
});

// get the eclipse store
IPreferenceStore eclipseStore = getPreferenceStore();
AdtPrefs.init(eclipseStore);








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/DdmsPlugin.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/DdmsPlugin.java
//Synthetic comment -- index 26ccbe8..b79da99 100644

//Synthetic comment -- @@ -77,6 +77,7 @@
private static String sToolsFolder;
private static String sHprofConverter;

/** debugger connectors for already running apps.
* Initialized from an extension point.
*/
//Synthetic comment -- @@ -221,50 +222,84 @@
// set the preferences.
PreferenceInitializer.setupPreferences();

        // init the lib
        AndroidDebugBridge.init(true /* debugger support */);

        // get the available adb locators
        final IAdbLocator[] locators = findAdbLocators();

        // Find the location of ADB.
        // Because the running the locators will initialize their plug-in, we do this in a job
        // to make sure that the init of the DDMS plug-in is finished. Otherwise, if another
        // plug-in uses DDMS classes in their start() method, this would fail since DDMS would not
        // be loaded yet.
        // The job is scheduled at the end of the
        Job adbLocatorJob = new Job("ADB Location resolution") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                for (IAdbLocator locator : locators) {
                    String adbLocation = locator.getAdbLocation();
                    if (adbLocation != null) {
                        // checks if the location is valid.
                        if (setAdbLocation(adbLocation)) {
                            AndroidDebugBridge.createBridge(sAdbLocation,
                                    true /* forceNewBridge */);

                            // no need to look at the other locators.
                            break;
                        }
                    }
                }
                return Status.OK_STATUS;
            }
        };

        // get the available debugger connectors
        mDebuggerConnectors = findDebuggerConnectors();

        // get the available source revealer
        mSourceRevealers = findSourceRevealers();
// this class is set as the main source revealer and will look at all the implementations
// of the extension point. see #reveal(String, String, int)
StackTracePanel.setSourceRevealer(this);

        // finish the method with the schedule of the adbLocator job since it must run after
        // this method ends.
        adbLocatorJob.schedule();
}

/**
//Synthetic comment -- @@ -272,14 +307,10 @@
*
* @return an array of all locators found, or an empty array if none were found.
*/
    private IAdbLocator[] findAdbLocators() throws CoreException {
ArrayList<IAdbLocator> list = new ArrayList<IAdbLocator>();

        // get the adb location from an implementation of the ADB Locator extension point.
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(
                "com.android.ide.eclipse.ddms.adbLocator"); //$NON-NLS-1$
        IConfigurationElement[] configElements = extensionPoint.getConfigurationElements();
if (configElements.length > 0) {
// only use the first one, ignore the others.
IConfigurationElement configElement = configElements[0];
//Synthetic comment -- @@ -299,14 +330,10 @@
*
* @return an array of all locators found, or an empty array if none were found.
*/
    private IDebuggerConnector[] findDebuggerConnectors() throws CoreException {
ArrayList<IDebuggerConnector> list = new ArrayList<IDebuggerConnector>();

        // get the adb location from an implementation of the ADB Locator extension point.
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(
                "com.android.ide.eclipse.ddms.debuggerConnector"); //$NON-NLS-1$
        IConfigurationElement[] configElements = extensionPoint.getConfigurationElements();
if (configElements.length > 0) {
// only use the first one, ignore the others.
IConfigurationElement configElement = configElements[0];
//Synthetic comment -- @@ -326,14 +353,10 @@
*
* @return an array of all locators found, or an empty array if none were found.
*/
    private ISourceRevealer[] findSourceRevealers() throws CoreException {
ArrayList<ISourceRevealer> list = new ArrayList<ISourceRevealer>();

        // get the adb location from an implementation of the ADB Locator extension point.
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(
                "com.android.ide.eclipse.ddms.sourceRevealer"); //$NON-NLS-1$
        IConfigurationElement[] configElements = extensionPoint.getConfigurationElements();
if (configElements.length > 0) {
// only use the first one, ignore the others.
IConfigurationElement configElement = configElements[0];
//Synthetic comment -- @@ -441,6 +464,24 @@
}
}

public IDebuggerConnector[] getDebuggerConnectors() {
return mDebuggerConnectors;
}
//Synthetic comment -- @@ -668,9 +709,11 @@
*/
public void reveal(String applicationName, String className, int line) {
// loop on all source revealer till one succeeds
        for (ISourceRevealer revealer : mSourceRevealers) {
            if (revealer.reveal(applicationName, className, line)) {
                break;
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index bbaded7..47b164d 100644

//Synthetic comment -- @@ -91,7 +91,6 @@
private Action mDebugAction;
private Action mHprofAction;
private Action mTracingAction;
    private IDebuggerConnector[] mDebuggerConnectors;

private ImageDescriptor mTracingStartImage;
private ImageDescriptor mTracingStopImage;
//Synthetic comment -- @@ -386,13 +385,10 @@
mTracingStopImage = loader.loadDescriptor(DevicePanel.ICON_TRACING_STOP);
mTracingAction.setImageDescriptor(mTracingStartImage);

        // check if there's already a debug launcher set up in the plugin class
        mDebuggerConnectors = DdmsPlugin.getDefault().getDebuggerConnectors();

mDebugAction = new Action("Debug Process") {
@Override
public void run() {
                if (mDebuggerConnectors.length != 0) {
Client currentClient = mDeviceList.getSelectedClient();
if (currentClient != null) {
ClientData clientData = currentClient.getClientData();
//Synthetic comment -- @@ -420,7 +416,10 @@
if (packageName != null) {

// try all connectors till one returns true.
                            for (IDebuggerConnector connector : mDebuggerConnectors) {
if (connector.connectDebugger(packageName,
currentClient.getDebuggerListenPort())) {
return;
//Synthetic comment -- @@ -442,9 +441,7 @@
};
mDebugAction.setToolTipText("Debug the selected process, provided its source project is present and opened in the workspace.");
mDebugAction.setImageDescriptor(loader.loadDescriptor("debug-attach.png")); //$NON-NLS-1$
        if (mDebuggerConnectors.length == 0) {
            mDebugAction.setEnabled(false);
        }

placeActions();

//Synthetic comment -- @@ -477,7 +474,7 @@
selectedClient.setAsSelectedClient();
}

            mDebugAction.setEnabled(mDebuggerConnectors.length != 0);
mKillAppAction.setEnabled(true);
mGcAction.setEnabled(true);








