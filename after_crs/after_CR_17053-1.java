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

    private boolean mHasDebuggerConnectors;
/** debugger connectors for already running apps.
* Initialized from an extension point.
*/
//Synthetic comment -- @@ -221,50 +222,84 @@
// set the preferences.
PreferenceInitializer.setupPreferences();

// this class is set as the main source revealer and will look at all the implementations
// of the extension point. see #reveal(String, String, int)
StackTracePanel.setSourceRevealer(this);

        /*
         * Load the extension point implementations.
         * The first step is to load the IConfigurationElement representing the implementations.
         * The 2nd step is to use these objects to instantiate the implementation classes.
         *
         * Because the 2nd step will trigger loading the plug-ins providing the implementations,
         * and those plug-ins could access DDMS classes (like ADT), this 2nd step should be done
         * in a Job to ensure that DDMS is loaded, so that the other plug-ins can load.
         *
         * Both steps could be done in the 2nd step but some of DDMS UI rely on knowing if there
         * is an implementation or not (DeviceView), so we do the first steps in start() and, in
         * some case, record it.
         *
         */

        // get the IConfigurationElement for the debuggerConnector right away.
        final IConfigurationElement[] dcce = findConfigElements(
                "com.android.ide.eclipse.ddms.debuggerConnector"); //$NON-NLS-1$
        mHasDebuggerConnectors = dcce.length > 0;

        // get the other configElements and instantiante them in a Job.
        new Job("DDMS post-create init") {
            @Override
            protected IStatus run(IProgressMonitor monitor) {
                try {
                    // init the lib
                    AndroidDebugBridge.init(true /* debugger support */);

                    // get the available adb locators
                    IConfigurationElement[] elements = findConfigElements(
                            "com.android.ide.eclipse.ddms.adbLocator"); //$NON-NLS-1$

                    IAdbLocator[] locators = instantiateAdbLocators(elements);

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

                    // get the available debugger connectors
                    mDebuggerConnectors = instantiateDebuggerConnectors(dcce);

                    // get the available source revealers
                    elements = findConfigElements("com.android.ide.eclipse.ddms.sourceRevealer"); //$NON-NLS-1$
                    mSourceRevealers = instantiateSourceRevealers(elements);

                    return Status.OK_STATUS;
                } catch (CoreException e) {
                    return e.getStatus();
                }
            }
        }.schedule();
    }

    private IConfigurationElement[] findConfigElements(String name) {

        // get the adb location from an implementation of the ADB Locator extension point.
        IExtensionRegistry extensionRegistry = Platform.getExtensionRegistry();
        IExtensionPoint extensionPoint = extensionRegistry.getExtensionPoint(name);
        if (extensionPoint != null) {
            return extensionPoint.getConfigurationElements();
        }

        // shouldn't happen or it means the plug-in is broken.
        return new IConfigurationElement[0];
}

/**
//Synthetic comment -- @@ -272,14 +307,10 @@
*
* @return an array of all locators found, or an empty array if none were found.
*/
    private IAdbLocator[] instantiateAdbLocators(IConfigurationElement[] configElements)
            throws CoreException {
ArrayList<IAdbLocator> list = new ArrayList<IAdbLocator>();

if (configElements.length > 0) {
// only use the first one, ignore the others.
IConfigurationElement configElement = configElements[0];
//Synthetic comment -- @@ -299,14 +330,10 @@
*
* @return an array of all locators found, or an empty array if none were found.
*/
    private IDebuggerConnector[] instantiateDebuggerConnectors(
            IConfigurationElement[] configElements) throws CoreException {
ArrayList<IDebuggerConnector> list = new ArrayList<IDebuggerConnector>();

if (configElements.length > 0) {
// only use the first one, ignore the others.
IConfigurationElement configElement = configElements[0];
//Synthetic comment -- @@ -326,14 +353,10 @@
*
* @return an array of all locators found, or an empty array if none were found.
*/
    private ISourceRevealer[] instantiateSourceRevealers(IConfigurationElement[] configElements)
            throws CoreException {
ArrayList<ISourceRevealer> list = new ArrayList<ISourceRevealer>();

if (configElements.length > 0) {
// only use the first one, ignore the others.
IConfigurationElement configElement = configElements[0];
//Synthetic comment -- @@ -441,6 +464,24 @@
}
}

    /**
     * Returns whether there are implementation of the debuggerConnectors extension point.
     * <p/>
     * This is guaranteed to return the correct value as soon as the plug-in is loaded.
     */
    public boolean hasDebuggerConnectors() {
        return mHasDebuggerConnectors;
    }

    /**
     * Returns the implementations of {@link IDebuggerConnector}.
     * <p/>
     * There may be a small amount of time right after the plug-in load where this can return
     * null even if there are implementation.
     * <p/>
     * Since the use of the implementation likely require user input, the UI can use
     * {@link #hasDebuggerConnectors()} to know if there are implementations before they are loaded.
     */
public IDebuggerConnector[] getDebuggerConnectors() {
return mDebuggerConnectors;
}
//Synthetic comment -- @@ -668,9 +709,11 @@
*/
public void reveal(String applicationName, String className, int line) {
// loop on all source revealer till one succeeds
        if (mSourceRevealers != null) {
            for (ISourceRevealer revealer : mSourceRevealers) {
                if (revealer.reveal(applicationName, className, line)) {
                    break;
                }
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/DeviceView.java
//Synthetic comment -- index bbaded7..47b164d 100644

//Synthetic comment -- @@ -91,7 +91,6 @@
private Action mDebugAction;
private Action mHprofAction;
private Action mTracingAction;

private ImageDescriptor mTracingStartImage;
private ImageDescriptor mTracingStopImage;
//Synthetic comment -- @@ -386,13 +385,10 @@
mTracingStopImage = loader.loadDescriptor(DevicePanel.ICON_TRACING_STOP);
mTracingAction.setImageDescriptor(mTracingStartImage);

mDebugAction = new Action("Debug Process") {
@Override
public void run() {
                if (DdmsPlugin.getDefault().hasDebuggerConnectors()) {
Client currentClient = mDeviceList.getSelectedClient();
if (currentClient != null) {
ClientData clientData = currentClient.getClientData();
//Synthetic comment -- @@ -420,7 +416,10 @@
if (packageName != null) {

// try all connectors till one returns true.
                            IDebuggerConnector[] connectors =
                                    DdmsPlugin.getDefault().getDebuggerConnectors();

                            for (IDebuggerConnector connector : connectors) {
if (connector.connectDebugger(packageName,
currentClient.getDebuggerListenPort())) {
return;
//Synthetic comment -- @@ -442,9 +441,7 @@
};
mDebugAction.setToolTipText("Debug the selected process, provided its source project is present and opened in the workspace.");
mDebugAction.setImageDescriptor(loader.loadDescriptor("debug-attach.png")); //$NON-NLS-1$
        mDebugAction.setEnabled(DdmsPlugin.getDefault().hasDebuggerConnectors());

placeActions();

//Synthetic comment -- @@ -477,7 +474,7 @@
selectedClient.setAsSelectedClient();
}

            mDebugAction.setEnabled(DdmsPlugin.getDefault().hasDebuggerConnectors());
mKillAppAction.setEnabled(true);
mGcAction.setEnabled(true);








