/*AVD: Create New Device, experimental layout.

Try a 2-column layout for the Create New Device dialog.

Change-Id:I499e44e6207823f6a210bb06f731839c9513b559*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/DeviceCreationDialog.java
//Synthetic comment -- index 2b7025f..ed52999 100644

//Synthetic comment -- @@ -44,7 +44,9 @@
import com.android.sdklib.devices.State;
import com.android.sdklib.devices.Storage;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -134,7 +136,7 @@
DeviceManager manager,
ImageFactory imageFactory,
@Nullable Device device) {
        super(parentShell, 2, false);
mImageFactory = imageFactory;
mDevice = device;
mManager = manager;
//Synthetic comment -- @@ -171,40 +173,65 @@
public void createDialogContent(Composite parent) {

ValidationListener validator = new ValidationListener();
        SizeListener sizeListener = new SizeListener();
NavStateListener navListener = new NavStateListener();

String tooltip = "Name of the new device";
        generateLabel("Name:", tooltip, parent);
        mDeviceName = generateText(parent, tooltip, new CreateNameModifyListener());

tooltip = "Diagonal length of the screen in inches";
        generateLabel("Screen Size (in):", tooltip, parent);
        mDiagonalLength = generateText(parent, tooltip, sizeListener);

tooltip = "The resolution of the device in pixels";
        generateLabel("Resolution:", tooltip, parent);
        Group dimensionGroup = new Group(parent, SWT.NONE);
        dimensionGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        dimensionGroup.setLayout(new GridLayout(3, false));
mXDimension = generateText(dimensionGroup, tooltip, sizeListener);
new Label(dimensionGroup, SWT.NONE).setText("x");
mYDimension = generateText(dimensionGroup, tooltip, sizeListener);

tooltip = "The screen size bucket that the device falls into";
        generateLabel("Size:", tooltip, parent);
        mSize = generateCombo(parent, tooltip, ScreenSize.values(), 1, validator);

tooltip = "The aspect ratio bucket the screen falls into. A \"long\" screen is wider.";
        generateLabel("Screen Ratio:", tooltip, parent);
        mRatio = generateCombo(parent, tooltip, ScreenRatio.values(), 1, validator);

tooltip = "The pixel density bucket the device falls in";
        generateLabel("Density:", tooltip, parent);
        mDensity = generateCombo(parent, tooltip, Density.values(), 3, validator);

        generateLabel("Sensors:", "The sensors available on the device", parent);
        Group sensorGroup = new Group(parent, SWT.NONE);
sensorGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
sensorGroup.setLayout(new GridLayout(2, false));
mAccelerometer = generateButton(sensorGroup, "Accelerometer",
//Synthetic comment -- @@ -215,8 +242,8 @@
mProximitySensor = generateButton(sensorGroup, "Proximity Sensor",
"Presence of a proximity sensor", SWT.CHECK, true, validator);

        generateLabel("Cameras", "The cameras available on the device", parent);
        Group cameraGroup = new Group(parent, SWT.NONE);
cameraGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
cameraGroup.setLayout(new GridLayout(2, false));
mCameraFront = generateButton(cameraGroup, "Front", "Presence of a front camera",
//Synthetic comment -- @@ -224,8 +251,8 @@
mCameraRear = generateButton(cameraGroup, "Rear", "Presence of a rear camera",
SWT.CHECK, true, validator);

        generateLabel("Input:", "The input hardware on the given device", parent);
        Group inputGroup = new Group(parent, SWT.NONE);
inputGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
inputGroup.setLayout(new GridLayout(3, false));
mKeyboard = generateButton(inputGroup, "Keyboard", "Presence of a hardware keyboard",
//Synthetic comment -- @@ -242,8 +269,8 @@
"The device has a trackball navigation element", SWT.RADIO, false, navListener);

tooltip = "The amount of RAM on the device";
        generateLabel("RAM:", tooltip, parent);
        Group ramGroup = new Group(parent, SWT.NONE);
ramGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
ramGroup.setLayout(new GridLayout(2, false));
mRam = generateText(ramGroup, tooltip, validator);
//Synthetic comment -- @@ -254,11 +281,13 @@
mRamCombo.select(0);
mRamCombo.addModifyListener(validator);

tooltip = "Type of buttons (Home, Menu, etc.) on the device. "
+ "This can be software buttons like on the Galaxy Nexus, or hardware buttons like "
+ "the capacitive buttons on the Nexus S.";
        generateLabel("Buttons:", tooltip, parent);
        mButtons = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
mButtons.setToolTipText(tooltip);
mButtons.add("Software");
mButtons.add("Hardware");
//Synthetic comment -- @@ -266,9 +295,9 @@
mButtons.select(0);
mButtons.addModifyListener(validator);

        generateLabel("Device States:", "The available states for the given device", parent);

        mStateGroup = new Group(parent, SWT.NONE);
mStateGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
mStateGroup.setLayout(new GridLayout(2, true));

//Synthetic comment -- @@ -320,24 +349,27 @@
"Hardware navigation is available in this state", SWT.CHECK, true, validator);
mLandscapeKeysNav.setEnabled(false);

        mForceCreation = new Button(parent, SWT.CHECK);
mForceCreation.setText("Override the existing device with the same name");
        mForceCreation
                .setToolTipText("There's already an AVD with the same name. Check this to delete it and replace it by the new AVD.");
mForceCreation.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER,
true, false, 2, 1));
mForceCreation.setEnabled(false);
mForceCreation.addSelectionListener(validator);

// add a separator to separate from the ok/cancel button
        Label label = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
label.setLayoutData(new GridData(GridData.FILL, GridData.CENTER, true, false, 3, 1));

// add stuff for the error display
Composite statusComposite = new Composite(parent, SWT.NONE);
GridLayout gl;
        statusComposite.setLayoutData(new GridData(GridData.FILL, GridData.CENTER,
                true, false, 3, 1));
statusComposite.setLayout(gl = new GridLayout(2, false));
gl.marginHeight = gl.marginWidth = 0;








