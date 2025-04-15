/*WIP: Use minSdk for device/avd compatibility checks

Change-Id:Iba31a45572dabe3895c4e51707ed515d407caae0*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/AndroidLaunchController.java
//Synthetic comment -- index 1f004f5..11f1971 100644

//Synthetic comment -- @@ -324,9 +324,18 @@
config.mTargetMode = TargetMode.MANUAL;
}

        // get the project target
IAndroidTarget projectTarget = currentSdk.getTarget(project);

// FIXME: check errors on missing sdk, AVD manager, or project target.

// device chooser response object.
//Synthetic comment -- @@ -370,17 +379,22 @@
AvdInfo preferredAvd = null;
if (config.mAvdName != null) {
preferredAvd = avdManager.getAvd(config.mAvdName, true /*validAvdOnly*/);
                if (projectTarget.canRunOn(preferredAvd.getTarget()) == false) {
preferredAvd = null;

AdtPlugin.printErrorToConsole(project, String.format(
                            "Preferred AVD '%1$s' is not compatible with the project target '%2$s'. Looking for a compatible AVD...",
                            config.mAvdName, projectTarget.getName()));
}
}

if (preferredAvd != null) {
                // look for a matching device

for (IDevice d : devices) {
String deviceAvd = d.getAvdName();
//Synthetic comment -- @@ -416,38 +430,31 @@
// a device which target is the same as the project's target) and use it as the
// new default.

            int reqApiLevel = 0;
            try {
                reqApiLevel = Integer.parseInt(requiredApiVersionNumber);

                if (reqApiLevel > 0 && reqApiLevel < projectTarget.getVersion().getApiLevel()) {
                    int maxDist = projectTarget.getVersion().getApiLevel() - reqApiLevel;
                    IAndroidTarget candidate = null;

                    for (IAndroidTarget target : currentSdk.getTargets()) {
                        if (target.canRunOn(projectTarget)) {
                            int currDist = target.getVersion().getApiLevel() - reqApiLevel;
                            if (currDist >= 0 && currDist < maxDist) {
                                maxDist = currDist;
                                candidate = target;
                                if (maxDist == 0) {
                                    // Found a perfect match
                                    break;
                                }
}
}
}

                    if (candidate != null) {
                        // We found a better SDK target candidate, that is closer to the
                        // API level from minSdkVersion than the one currently used by the
                        // project. Below (in the for...devices loop) we'll try to find
                        // a device/AVD for it.
                        projectTarget = candidate;
                    }
}
            } catch (NumberFormatException e) {
                // pass
}

HashMap<IDevice, AvdInfo> compatibleRunningAvds = new HashMap<IDevice, AvdInfo>();
//Synthetic comment -- @@ -578,7 +585,7 @@
boolean includeDevices = config.mTargetMode != TargetMode.ALL_EMULATORS;
boolean includeAvds = config.mTargetMode != TargetMode.ALL_DEVICES;
Collection<IDevice> compatibleDevices = findCompatibleDevices(devices,
                    requiredApiVersionNumber, includeDevices, includeAvds);
if (compatibleDevices.size() == 0) {
AdtPlugin.printErrorToConsole(project,
"No active compatible AVD's or devices found. "
//Synthetic comment -- @@ -601,7 +608,8 @@
// or the AVD to launch.
DeviceChooserDialog dialog = new DeviceChooserDialog(
AdtPlugin.getDisplay().getActiveShell(),
                            response, launchInfo.getPackageName(), desiredProjectTarget);
if (dialog.open() == Dialog.OK) {
DeviceChoiceCache.put(launch.getLaunchConfiguration().getName(), response);
continueLaunch.set(true);
//Synthetic comment -- @@ -634,22 +642,14 @@
/**
* Returns devices that can run a app of provided API level.
* @param devices list of devices to filter from
     * @param requiredApiVersionNumber minimum required API level that should be supported
* @param includeDevices include physical devices in the filtered list
* @param includeAvds include emulators in the filtered list
* @return set of compatible devices, may be an empty set
*/
private Collection<IDevice> findCompatibleDevices(IDevice[] devices,
            String requiredApiVersionNumber, boolean includeDevices, boolean includeAvds) {
Set<IDevice> compatibleDevices = new HashSet<IDevice>(devices.length);
        int minApi;
        try {
            minApi = Integer.parseInt(requiredApiVersionNumber);
        } catch (NumberFormatException e) {
            minApi = 1;
        }
        AndroidVersion requiredVersion = new AndroidVersion(minApi, null);

AvdManager avdManager = Sdk.getCurrent().getAvdManager();
for (IDevice d: devices) {
boolean isEmulator = d.isEmulator();








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChooserDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/launch/DeviceChooserDialog.java
//Synthetic comment -- index 781deaf..0f91461 100644

//Synthetic comment -- @@ -57,6 +57,9 @@
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;

/**
* A dialog that lets the user choose a device to deploy an application.
* The user can either choose an exiting running device (including running emulators)
//Synthetic comment -- @@ -80,6 +83,7 @@
private final DeviceChooserResponse mResponse;
private final String mPackageName;
private final IAndroidTarget mProjectTarget;
private final Sdk mSdk;

private Button mDeviceRadioButton;
//Synthetic comment -- @@ -92,16 +96,32 @@
* Basic Content Provider for a table full of {@link IDevice} objects. The input is
* a {@link AndroidDebugBridge}.
*/
    private static class ContentProvider implements IStructuredContentProvider {
@Override
public Object[] getElements(Object inputElement) {
if (inputElement instanceof AndroidDebugBridge) {
                return ((AndroidDebugBridge)inputElement).getDevices();
}

return new Object[0];
}

@Override
public void dispose() {
// pass
//Synthetic comment -- @@ -113,7 +133,6 @@
}
}


/**
* A Label Provider for the {@link TableViewer} in {@link DeviceChooserDialog}.
* It provides labels and images for {@link IDevice} objects.
//Synthetic comment -- @@ -136,7 +155,7 @@
if (deviceVersion == null) {
return mWarningImage;
} else {
                                if (deviceVersion.canRun(mProjectTarget.getVersion()) == false) {
return mNoMatchImage;
}

//Synthetic comment -- @@ -152,8 +171,22 @@
if (info == null) {
return mWarningImage;
}
                            return mProjectTarget.canRunOn(info.getTarget()) ?
                                    mMatchImage : mNoMatchImage;
}
}
}
//Synthetic comment -- @@ -259,12 +292,13 @@
}

public DeviceChooserDialog(Shell parent, DeviceChooserResponse response, String packageName,
            IAndroidTarget projectTarget) {
super(parent);

mResponse = response;
mPackageName = packageName;
mProjectTarget = projectTarget;
mSdk = Sdk.getCurrent();

AndroidDebugBridge.addDeviceChangeListener(this);
//Synthetic comment -- @@ -340,9 +374,16 @@
Composite top = new Composite(parent, SWT.NONE);
top.setLayout(new GridLayout(1, true));

Label label = new Label(top, SWT.NONE);
        label.setText(String.format("Select a device compatible with target %s.",
                mProjectTarget.getFullName()));

mDeviceRadioButton = new Button(top, SWT.RADIO);
mDeviceRadioButton.setText("Choose a running Android device");
//Synthetic comment -- @@ -736,12 +777,23 @@

@Override
public boolean accept(AvdInfo avd) {
if (mDevices != null) {
for (IDevice d : mDevices) {
                    if (mProjectTarget.canRunOn(avd.getTarget()) == false ||
                            avd.getName().equals(d.getAvdName())) {
return false;
}
}
}








