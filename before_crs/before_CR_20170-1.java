/*Fix NON-NLS tokens

There were a number of // $NON-NLS-1$ references in the codebase.
Eclipse's "Externalize Strings" functionality will not handle these
correctly; there must not be a space between the "//" and the "$NON"
tokens.

(I left AndroidXmlEditor.xml alone; it is the file I discovered
the problem in but I fixed those references as part of another
pending checkin.)

Change-Id:If185c88a667273af614f0bee5959fd2618756c05*/
//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/AboutDialog.java b/ddms/app/src/com/android/ddms/AboutDialog.java
//Synthetic comment -- index d33c2dc..d9387c9 100644

//Synthetic comment -- @@ -64,7 +64,7 @@
Shell shell = new Shell(parent, getStyle());
shell.setText("About...");

        logoImage = loadImage(shell, "ddms-logo.png"); // $NON-NLS-1$
createContents(shell);
shell.pack();

//Synthetic comment -- @@ -86,7 +86,7 @@
*/
private Image loadImage(Shell shell, String fileName) {
InputStream imageStream;
        String pathName = "/images/" + fileName;  // $NON-NLS-1$

imageStream = this.getClass().getResourceAsStream(pathName);
if (imageStream == null) {








//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/StaticPortEditDialog.java b/ddms/app/src/com/android/ddms/StaticPortEditDialog.java
//Synthetic comment -- index b224967..9191d9f 100644

//Synthetic comment -- @@ -249,7 +249,7 @@
private void validate() {
// first we reset the warning dialog. This allows us to latter
// display warnings.
        mWarning.setText(""); // $NON-NLS-1$

// check the device name field is not empty
if (mDeviceSn == null || mDeviceSn.length() == 0) {
//Synthetic comment -- @@ -269,9 +269,9 @@

// validate the package name as well. It must be a fully qualified
// java package.
        String[] packageSegments = mAppName.split("\\."); // $NON-NLS-1$
for (String p : packageSegments) {
            if (p.matches("^[a-zA-Z][a-zA-Z0-9]*") == false) { // $NON-NLS-1$
mWarning.setText(packageError);
mOkButton.setEnabled(false);
return;
//Synthetic comment -- @@ -279,7 +279,7 @@

// lets also display a warning if the package contains upper case
// letters.
            if (p.matches("^[a-z][a-z0-9]*") == false) { // $NON-NLS-1$
mWarning.setText("Lower case is recommended for Java packages.");
}
}
//Synthetic comment -- @@ -300,7 +300,7 @@
}

// then we check it only contains digits.
        if (mPortNumber.matches("[0-9]*") == false) { // $NON-NLS-1$
mWarning.setText("Port Number invalid.");
mOkButton.setEnabled(false);
return;








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/ClientData.java b/ddms/libs/ddmlib/src/com/android/ddmlib/ClientData.java
//Synthetic comment -- index 7f4b5dd..9b96f64 100644

//Synthetic comment -- @@ -100,46 +100,46 @@
* Name of the value representing the max size of the heap, in the {@link Map} returned by
* {@link #getVmHeapInfo(int)}
*/
    public final static String HEAP_MAX_SIZE_BYTES = "maxSizeInBytes"; // $NON-NLS-1$
/**
* Name of the value representing the size of the heap, in the {@link Map} returned by
* {@link #getVmHeapInfo(int)}
*/
    public final static String HEAP_SIZE_BYTES = "sizeInBytes"; // $NON-NLS-1$
/**
* Name of the value representing the number of allocated bytes of the heap, in the
* {@link Map} returned by {@link #getVmHeapInfo(int)}
*/
    public final static String HEAP_BYTES_ALLOCATED = "bytesAllocated"; // $NON-NLS-1$
/**
* Name of the value representing the number of objects in the heap, in the {@link Map}
* returned by {@link #getVmHeapInfo(int)}
*/
    public final static String HEAP_OBJECTS_ALLOCATED = "objectsAllocated"; // $NON-NLS-1$

/**
* String for feature enabling starting/stopping method profiling
* @see #hasFeature(String)
*/
    public final static String FEATURE_PROFILING = "method-trace-profiling"; // $NON-NLS-1$

/**
* String for feature enabling direct streaming of method profiling data
* @see #hasFeature(String)
*/
    public final static String FEATURE_PROFILING_STREAMING = "method-trace-profiling-streaming"; // $NON-NLS-1$

/**
* String for feature allowing to dump hprof files
* @see #hasFeature(String)
*/
    public final static String FEATURE_HPROF = "hprof-heap-dump"; // $NON-NLS-1$

/**
* String for feature allowing direct streaming of hprof dumps
* @see #hasFeature(String)
*/
    public final static String FEATURE_HPROF_STREAMING = "hprof-heap-dump-streaming"; // $NON-NLS-1$

private static IHprofDumpHandler sHprofDumpHandler;
private static IMethodProfilingHandler sMethodProfilingHandler;








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/Debugger.java b/ddms/libs/ddmlib/src/com/android/ddmlib/Debugger.java
//Synthetic comment -- index cebbc32..9356c13 100644

//Synthetic comment -- @@ -71,7 +71,7 @@
mListenChannel.configureBlocking(false);        // required for Selector

InetSocketAddress addr = new InetSocketAddress(
                InetAddress.getByName("localhost"), // $NON-NLS-1$
listenPort);
mListenChannel.socket().setReuseAddress(true);  // enable SO_REUSEADDR
mListenChannel.socket().bind(addr);








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java b/ddms/libs/ddmlib/src/com/android/ddmlib/DeviceMonitor.java
//Synthetic comment -- index a975cd4..e039cda 100644

//Synthetic comment -- @@ -304,10 +304,10 @@
byte[] buffer = new byte[length];
String result = read(mMainAdbConnection, buffer);

            String[] devices = result.split("\n"); // $NON-NLS-1$

for (String d : devices) {
                String[] param = d.split("\t"); // $NON-NLS-1$
if (param.length == 2) {
// new adb uses only serial numbers to identify devices
Device device = new Device(this, param[0] /*serialnumber*/,








//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/HandleNativeHeap.java b/ddms/libs/ddmlib/src/com/android/ddmlib/HandleNativeHeap.java
//Synthetic comment -- index ca26590..2b91b1e 100644

//Synthetic comment -- @@ -28,10 +28,10 @@
*/
final class HandleNativeHeap extends ChunkHandler {

    public static final int CHUNK_NHGT = type("NHGT"); // $NON-NLS-1$
    public static final int CHUNK_NHSG = type("NHSG"); // $NON-NLS-1$
    public static final int CHUNK_NHST = type("NHST"); // $NON-NLS-1$
    public static final int CHUNK_NHEN = type("NHEN"); // $NON-NLS-1$

private static final HandleNativeHeap mInst = new HandleNativeHeap();









//Synthetic comment -- diff --git a/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java b/ddms/libs/ddmlib/src/com/android/ddmlib/SyncService.java
//Synthetic comment -- index 0303a03..f112a12 100644

//Synthetic comment -- @@ -163,7 +163,7 @@
// target a specific device
AdbHelper.setDevice(mChannel, mDevice);

            byte[] request = AdbHelper.formAdbRequest("sync:"); // $NON-NLS-1$
AdbHelper.write(mChannel, request, -1, DdmPreferences.getTimeOut());

AdbResponse resp = AdbHelper.readAdbResponse(mChannel, false /* readDiagString */);








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/EmulatorControlPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/EmulatorControlPanel.java
//Synthetic comment -- index cfd8986..4d36bc5 100644

//Synthetic comment -- @@ -535,8 +535,8 @@
createManualLocationControl(manualLocationComp);

ImageLoader loader = ImageLoader.getDdmUiLibLoader();
        mPlayImage = loader.loadImage("play.png", mParent.getDisplay()); // $NON-NLS-1$
        mPauseImage = loader.loadImage("pause.png", mParent.getDisplay()); // $NON-NLS-1$

Composite gpxLocationComp = new Composite(mLocationFolders, SWT.NONE);
item = new TabItem(mLocationFolders, SWT.NONE);
//Synthetic comment -- @@ -836,11 +836,11 @@

ImageLoader loader = ImageLoader.getDdmUiLibLoader();
mGpxBackwardButton = new Button(mGpxPlayControls, SWT.TOGGLE | SWT.FLAT);
        mGpxBackwardButton.setImage(loader.loadImage("backward.png", mParent.getDisplay())); // $NON-NLS-1$
mGpxBackwardButton.setSelection(false);
mGpxBackwardButton.addSelectionListener(mDirectionButtonAdapter);
mGpxForwardButton = new Button(mGpxPlayControls, SWT.TOGGLE | SWT.FLAT);
        mGpxForwardButton.setImage(loader.loadImage("forward.png", mParent.getDisplay())); // $NON-NLS-1$
mGpxForwardButton.setSelection(true);
mGpxForwardButton.addSelectionListener(mDirectionButtonAdapter);

//Synthetic comment -- @@ -974,11 +974,11 @@

ImageLoader loader = ImageLoader.getDdmUiLibLoader();
mKmlBackwardButton = new Button(mKmlPlayControls, SWT.TOGGLE | SWT.FLAT);
        mKmlBackwardButton.setImage(loader.loadImage("backward.png", mParent.getDisplay())); // $NON-NLS-1$
mKmlBackwardButton.setSelection(false);
mKmlBackwardButton.addSelectionListener(mDirectionButtonAdapter);
mKmlForwardButton = new Button(mKmlPlayControls, SWT.TOGGLE | SWT.FLAT);
        mKmlForwardButton.setImage(loader.loadImage("forward.png", mParent.getDisplay())); // $NON-NLS-1$
mKmlForwardButton.setSelection(true);
mKmlForwardButton.addSelectionListener(mDirectionButtonAdapter);









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/HeapPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/HeapPanel.java
//Synthetic comment -- index ce61682..a422e65 100644

//Synthetic comment -- @@ -430,7 +430,7 @@
l.setText("Zoom:");
mZoom = new Combo(zoomComposite, SWT.READ_ONLY);
for (int z : ZOOMS) {
                mZoom.add(String.format("%1$d%%", z)); // $NON-NLS-1$
}

mZoom.select(0);
//Synthetic comment -- @@ -503,27 +503,27 @@
col.pack();

col = new TableColumn(tab, SWT.RIGHT);
        col.setText("000.000WW"); // $NON-NLS-1$
col.pack();
col.setText("Heap Size");

col = new TableColumn(tab, SWT.RIGHT);
        col.setText("000.000WW"); // $NON-NLS-1$
col.pack();
col.setText("Allocated");

col = new TableColumn(tab, SWT.RIGHT);
        col.setText("000.000WW"); // $NON-NLS-1$
col.pack();
col.setText("Free");

col = new TableColumn(tab, SWT.RIGHT);
        col.setText("000.00%"); // $NON-NLS-1$
col.pack();
col.setText("% Used");

col = new TableColumn(tab, SWT.RIGHT);
        col.setText("000,000,000"); // $NON-NLS-1$
col.pack();
col.setText("# Objects");









//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/EventDisplayOptions.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/log/event/EventDisplayOptions.java
//Synthetic comment -- index c721a91..d746753 100644

//Synthetic comment -- @@ -273,7 +273,7 @@

ImageLoader loader = ImageLoader.getDdmUiLibLoader();
mEventDisplayNewButton = new Button(bottomControls, SWT.PUSH | SWT.FLAT);
        mEventDisplayNewButton.setImage(loader.loadImage("add.png", // $NON-NLS-1$
leftPanel.getDisplay()));
mEventDisplayNewButton.setToolTipText("Adds a new event display");
mEventDisplayNewButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
//Synthetic comment -- @@ -285,7 +285,7 @@
});

mEventDisplayDeleteButton = new Button(bottomControls, SWT.PUSH | SWT.FLAT);
        mEventDisplayDeleteButton.setImage(loader.loadImage("delete.png", // $NON-NLS-1$
leftPanel.getDisplay()));
mEventDisplayDeleteButton.setToolTipText("Deletes the selected event display");
mEventDisplayDeleteButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
//Synthetic comment -- @@ -297,7 +297,7 @@
});

mEventDisplayUpButton = new Button(bottomControls, SWT.PUSH | SWT.FLAT);
        mEventDisplayUpButton.setImage(loader.loadImage("up.png", // $NON-NLS-1$
leftPanel.getDisplay()));
mEventDisplayUpButton.setToolTipText("Moves the selected event display up");
mEventDisplayUpButton.addSelectionListener(new SelectionAdapter() {
//Synthetic comment -- @@ -325,7 +325,7 @@
});

mEventDisplayDownButton = new Button(bottomControls, SWT.PUSH | SWT.FLAT);
        mEventDisplayDownButton.setImage(loader.loadImage("down.png", // $NON-NLS-1$
leftPanel.getDisplay()));
mEventDisplayDownButton.setToolTipText("Moves the selected event display down");
mEventDisplayDownButton.addSelectionListener(new SelectionAdapter() {








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/EditFilterDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/EditFilterDialog.java
//Synthetic comment -- index 28a0fb0..8bbc7c5 100644

//Synthetic comment -- @@ -223,7 +223,7 @@
});

mPidWarning = new Label(main, SWT.NONE);
        mPidWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage("empty.png", // $NON-NLS-1$
mShell.getDisplay()));

l = new Label(main, SWT.NONE);
//Synthetic comment -- @@ -326,15 +326,15 @@

// then we check it only contains digits.
if (mPid != null) {
            if (mPid.matches("[0-9]*") == false) { // $NON-NLS-1$
mOkButton.setEnabled(false);
mPidWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(
                        "warning.png", // $NON-NLS-1$
mShell.getDisplay()));
return;
} else {
mPidWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(
                        "empty.png", // $NON-NLS-1$
mShell.getDisplay()));
}
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogFilter.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogFilter.java
//Synthetic comment -- index a32de2f..2f2cfef 100644

//Synthetic comment -- @@ -124,7 +124,7 @@
}

public boolean loadFromString(String string) {
        String[] segments = string.split(":"); // $NON-NLS-1$
int index = 0;

// get the name
//Synthetic comment -- @@ -460,11 +460,11 @@
mUnreadCount += mNewMessages.size();
totalCount = mTable.getItemCount();
if (mUnreadCount > 0) {
                mTabItem.setText(mName + " (" // $NON-NLS-1$
+ (mUnreadCount > totalCount ? totalCount : mUnreadCount)
                        + ")");  // $NON-NLS-1$
} else {
                mTabItem.setText(mName);  // $NON-NLS-1$
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/AndroidConstants.java
//Synthetic comment -- index 6e4b278..7d62acf 100644

//Synthetic comment -- @@ -48,7 +48,7 @@
* The old Editors Plugin ID. It is still used in some places for compatibility.
* Please do not use for new features.
*/
    public static final String EDITORS_NAMESPACE = "com.android.ide.eclipse.editors"; // $NON-NLS-1$

/** Nature of default Android projects */
public final static String NATURE_DEFAULT = "com.android.ide.eclipse.adt.AndroidNature"; //$NON-NLS-1$
//Synthetic comment -- @@ -232,5 +232,5 @@
/** The base URL where to find the Android class & manifest documentation */
public static final String CODESITE_BASE_URL = "http://code.google.com/android";  //$NON-NLS-1$

    public static final String LIBRARY_TEST_RUNNER = "android.test.runner"; // $NON-NLS-1$
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidTextEditor.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/AndroidTextEditor.java
old mode 100755
new mode 100644
//Synthetic comment -- index e524826..2b11671

//Synthetic comment -- @@ -78,7 +78,7 @@
private static final String PREF_CURRENT_PAGE = "_current_page";

/** Id string used to create the Android SDK browser */
    private static String BROWSER_ID = "android"; // $NON-NLS-1$

/** Page id of the XML source editor, used for switching tabs programmatically */
public final static String TEXT_EDITOR_ID = "editor_part"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/export/ExportLinksPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/export/ExportLinksPart.java
old mode 100755
new mode 100644
//Synthetic comment -- index 31ea988..d772897

//Synthetic comment -- @@ -47,7 +47,7 @@
final Composite table = createTableLayout(toolkit, 2 /* numColumns */);

StringBuffer buf = new StringBuffer();
        buf.append("<form>"); // $NON-NLS-1$

buf.append("<li style=\"image\" value=\"android_img\"><a href=\"action_dosomething\">");
buf.append("TODO Custom Action");
//Synthetic comment -- @@ -55,15 +55,15 @@
buf.append(": blah blah do something (like build/export).");
buf.append("</li>"); //$NON-NLS-1$

        buf.append(String.format("<li style=\"image\" value=\"android_img\"><a href=\"page:%1$s\">", // $NON-NLS-1$
ExportEditor.TEXT_EDITOR_ID));
buf.append("XML Source");
buf.append("</a>"); //$NON-NLS-1$
buf.append(": Directly edit the AndroidManifest.xml file.");
buf.append("</li>"); //$NON-NLS-1$

        buf.append("<li style=\"image\" value=\"android_img\">"); // $NON-NLS-1$
        buf.append("<a href=\"http://code.google.com/android/devel/bblocks-manifest.html\">Documentation</a>: Documentation from the Android SDK for AndroidManifest.xml."); // $NON-NLS-1$
buf.append("</li>"); //$NON-NLS-1$
buf.append("</form>"); //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index a7924c3..3656e4d 100644

//Synthetic comment -- @@ -140,12 +140,12 @@
private String getShortClassName(String fqcn) {
// The name is typically a fully-qualified class name. Let's make it a tad shorter.

        if (fqcn.startsWith("android.")) {                                      // $NON-NLS-1$
// For android classes, convert android.foo.Name to android...Name
int first = fqcn.indexOf('.');
int last = fqcn.lastIndexOf('.');
if (last > first) {
                return fqcn.substring(0, first) + ".." + fqcn.substring(last);   // $NON-NLS-1$
}
} else {
// For custom non-android classes, it's best to keep the 2 first segments of
//Synthetic comment -- @@ -154,7 +154,7 @@
first = fqcn.indexOf('.', first + 1);
int last = fqcn.lastIndexOf('.');
if (last > first) {
                return fqcn.substring(0, first) + ".." + fqcn.substring(last);   // $NON-NLS-1$
}
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/SelectionItem.java
old mode 100755
new mode 100644
//Synthetic comment -- index f830cb9..70a4098

//Synthetic comment -- @@ -134,12 +134,12 @@
if (name == null) {
// The name is typically a fully-qualified class name. Let's make it a tad shorter.

            if (fqcn.startsWith("android.")) {                                      // $NON-NLS-1$
// For android classes, convert android.foo.Name to android...Name
int first = fqcn.indexOf('.');
int last = fqcn.lastIndexOf('.');
if (last > first) {
                    name = fqcn.substring(0, first) + ".." + fqcn.substring(last);   // $NON-NLS-1$
}
} else {
// For custom non-android classes, it's best to keep the 2 first segments of
//Synthetic comment -- @@ -148,7 +148,7 @@
first = fqcn.indexOf('.', first + 1);
int last = fqcn.lastIndexOf('.');
if (last > first) {
                    name = fqcn.substring(0, first) + ".." + fqcn.substring(last);   // $NON-NLS-1$
}
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewLinksPart.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/manifest/pages/OverviewLinksPart.java
//Synthetic comment -- index daf0885..2386a8c 100644

//Synthetic comment -- @@ -46,36 +46,36 @@
Composite table = createTableLayout(toolkit, 2 /* numColumns */);

StringBuffer buf = new StringBuffer();
        buf.append(String.format("<form><li style=\"image\" value=\"app_img\"><a href=\"page:%1$s\">", // $NON-NLS-1$
ApplicationPage.PAGE_ID));
buf.append("Application");
buf.append("</a>");  //$NON-NLS-1$
buf.append(": Activities, intent filters, providers, services and receivers.");
buf.append("</li>"); //$NON-NLS-1$

        buf.append(String.format("<li style=\"image\" value=\"perm_img\"><a href=\"page:%1$s\">", // $NON-NLS-1$
PermissionPage.PAGE_ID));
buf.append("Permission");
buf.append("</a>"); //$NON-NLS-1$
buf.append(": Permissions defined and permissions used.");
buf.append("</li>"); //$NON-NLS-1$

        buf.append(String.format("<li style=\"image\" value=\"inst_img\"><a href=\"page:%1$s\">", // $NON-NLS-1$
InstrumentationPage.PAGE_ID));
buf.append("Instrumentation");
buf.append("</a>"); //$NON-NLS-1$
buf.append(": Instrumentation defined.");
buf.append("</li>"); //$NON-NLS-1$

        buf.append(String.format("<li style=\"image\" value=\"android_img\"><a href=\"page:%1$s\">", // $NON-NLS-1$
ManifestEditor.TEXT_EDITOR_ID));
buf.append("XML Source");
buf.append("</a>"); //$NON-NLS-1$
buf.append(": Directly edit the AndroidManifest.xml file.");
buf.append("</li>"); //$NON-NLS-1$

        buf.append("<li style=\"image\" value=\"android_img\">"); // $NON-NLS-1$
        buf.append("<a href=\"http://code.google.com/android/devel/bblocks-manifest.html\">Documentation</a>: Documentation from the Android SDK for AndroidManifest.xml."); // $NON-NLS-1$
buf.append("</li>"); //$NON-NLS-1$
buf.append("</form>"); //$NON-NLS-1$









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/descriptors/ResourcesDescriptors.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/resources/descriptors/ResourcesDescriptors.java
//Synthetic comment -- index b6ee15d..2bb7f0f 100644

//Synthetic comment -- @@ -200,7 +200,7 @@
null /* nsUri */,
"The mandatory name used in referring to this theme.",
nameAttrInfo),
                        new TextAttributeDescriptor("parent", // $NON-NLS-1$
"Parent",
null /* nsUri */,
"An optional parent theme. All values from the specified theme will be inherited into this theme. Any values with identical names that you specify will override inherited values.",








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/DdmsPlugin.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/DdmsPlugin.java
//Synthetic comment -- index 4ce8d00..ef7628d 100644

//Synthetic comment -- @@ -66,7 +66,7 @@


// The plug-in ID
    public static final String PLUGIN_ID = "com.android.ide.eclipse.ddms"; // $NON-NLS-1$

/** The singleton instance */
private static DdmsPlugin sPlugin;
//Synthetic comment -- @@ -143,7 +143,7 @@
//DdmUiPreferences.displayCharts();

// set the consoles.
        mDdmsConsole = new MessageConsole("DDMS", null); // $NON-NLS-1$
ConsolePlugin.getDefault().getConsoleManager().addConsoles(
new IConsole[] {
mDdmsConsole








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/EventLogView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/EventLogView.java
//Synthetic comment -- index 03752c4..8e170b7 100644

//Synthetic comment -- @@ -38,23 +38,23 @@
// create the external actions
CommonAction optionsAction = new CommonAction("Options...");
optionsAction.setToolTipText("Opens the options panel");
        optionsAction.setImageDescriptor(loader.loadDescriptor("edit.png")); // $NON-NLS-1$

CommonAction clearLogAction = new CommonAction("Clear Log");
clearLogAction.setToolTipText("Clears the event log");
        clearLogAction.setImageDescriptor(loader.loadDescriptor("clear.png")); // $NON-NLS-1$

CommonAction saveAction = new CommonAction("Save Log");
saveAction.setToolTipText("Saves the event log");
        saveAction.setImageDescriptor(loader.loadDescriptor("save.png")); // $NON-NLS-1$

CommonAction loadAction = new CommonAction("Load Log");
loadAction.setToolTipText("Loads an event log");
        loadAction.setImageDescriptor(loader.loadDescriptor("load.png")); // $NON-NLS-1$

CommonAction importBugAction = new CommonAction("Import Bug Report Log");
importBugAction.setToolTipText("Imports a bug report.");
        importBugAction.setImageDescriptor(loader.loadDescriptor("importBug.png")); // $NON-NLS-1$

placeActions(optionsAction, clearLogAction, saveAction, loadAction, importBugAction);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java
//Synthetic comment -- index a53a7c9..7cb9b3c 100644

//Synthetic comment -- @@ -71,26 +71,26 @@
public final class LogCatView extends SelectionDependentViewPart implements LogCatViewInterface {

public static final String ID =
        "com.android.ide.eclipse.ddms.views.LogCatView"; // $NON-NLS-1$

private static final String PREFS_COL_TIME =
        DdmsPlugin.PLUGIN_ID + ".logcat.time"; // $NON-NLS-1$
private static final String PREFS_COL_LEVEL =
        DdmsPlugin.PLUGIN_ID + ".logcat.level"; // $NON-NLS-1$
private static final String PREFS_COL_PID =
        DdmsPlugin.PLUGIN_ID + ".logcat.pid"; // $NON-NLS-1$
private static final String PREFS_COL_TAG =
        DdmsPlugin.PLUGIN_ID + ".logcat.tag"; // $NON-NLS-1$
private static final String PREFS_COL_MESSAGE =
        DdmsPlugin.PLUGIN_ID + ".logcat.message"; // $NON-NLS-1$

private static final String PREFS_FILTERS =
        DdmsPlugin.PLUGIN_ID + ".logcat.filters"; // $NON-NLS-1$

public static final String CHOICE_METHOD_DECLARATION =
        DdmsPlugin.PLUGIN_ID + ".logcat.MethodDeclaration"; // $NON-NLS-1$
public static final String CHOICE_ERROR_LINE =
        DdmsPlugin.PLUGIN_ID + ".logcat.ErrorLine"; // $NON-NLS-1$

private static LogCatView sThis;
private LogPanel mLogPanel;
//Synthetic comment -- @@ -126,7 +126,7 @@
PREFS_FILTERS);

// split in a string per filter
            String[] filters = filterPrefs.split("\\|"); // $NON-NLS-1$

ArrayList<LogFilter> list =
new ArrayList<LogFilter>(filters.length);
//Synthetic comment -- @@ -267,7 +267,7 @@
}
};
mEditFilterAction.setToolTipText("Edit Filter");
        mEditFilterAction.setImageDescriptor(loader.loadDescriptor("edit.png")); // $NON-NLS-1$

mDeleteFilterAction = new CommonAction("Delete Filter") {
@Override
//Synthetic comment -- @@ -276,7 +276,7 @@
}
};
mDeleteFilterAction.setToolTipText("Delete Filter");
        mDeleteFilterAction.setImageDescriptor(loader.loadDescriptor("delete.png")); // $NON-NLS-1$

mExportAction = new CommonAction("Export Selection As Text...") {
@Override
//Synthetic comment -- @@ -285,7 +285,7 @@
}
};
mExportAction.setToolTipText("Export Selection As Text...");
        mExportAction.setImageDescriptor(loader.loadDescriptor("save.png")); // $NON-NLS-1$

mGotoMethodDeclarationAction = new CommonAction("Go to Problem (method declaration)") {
@Override
//Synthetic comment -- @@ -333,7 +333,7 @@
mLogPanel.clear();
}
};
        mClearAction.setImageDescriptor(loader.loadDescriptor("clear.png")); // $NON-NLS-1$


// now create the log view








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/NativeHeapView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/NativeHeapView.java
//Synthetic comment -- index ed5aacb..91c8022 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
public class NativeHeapView extends TableView {

public static final String ID =
        "com.android.ide.eclipse.ddms.views.NativeHeapView"; // $NON-NLS-1$
private NativeHeapPanel mPanel;

public NativeHeapView() {








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/ThreadView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/ThreadView.java
//Synthetic comment -- index cd24458..9d4eeb7 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
public class ThreadView extends TableView {

public static final String ID =
        "com.android.ide.eclipse.ddms.views.ThreadView"; // $NON-NLS-1$
private ThreadPanel mPanel;

public ThreadView() {








//Synthetic comment -- diff --git a/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java b/sdkmanager/libs/sdklib/src/com/android/sdklib/xml/AndroidManifestParser.java
//Synthetic comment -- index ad0790e..d7ca8ec 100644

//Synthetic comment -- @@ -396,7 +396,7 @@
String exportedStr = getAttributeValue(attributes,
AndroidManifest.ATTRIBUTE_EXPORTED, true);
boolean exported = exportedStr == null ||
                        exportedStr.toLowerCase().equals("true"); // $NON-NLS-1$
mCurrentActivity = new Activity(activityName, exported);
mManifestData.mActivities.add(mCurrentActivity);









//Synthetic comment -- diff --git a/sdkstats/src/com/android/sdkstats/SdkStatsService.java b/sdkstats/src/com/android/sdkstats/SdkStatsService.java
//Synthetic comment -- index f5e6637..f750413 100644

//Synthetic comment -- @@ -88,12 +88,12 @@

/** List of Linux browser commands to try, in order (see openUrl). */
private static final String[] LINUX_BROWSERS = new String[] {
        "firefox -remote openurl(%URL%,new-window)",  // $NON-NLS-1$ running FF
        "mozilla -remote openurl(%URL%,new-window)",  // $NON-NLS-1$ running Moz
        "firefox %URL%",                              // $NON-NLS-1$ new FF
        "mozilla %URL%",                              // $NON-NLS-1$ new Moz
        "kfmclient openURL %URL%",                    // $NON-NLS-1$ Konqueror
        "opera -newwindow %URL%",                     // $NON-NLS-1$ Opera
};

public final static String PING_OPT_IN = "pingOptIn"; //$NON-NLS-1$
//Synthetic comment -- @@ -201,7 +201,7 @@
}

// If the last ping *for this app* was too recent, do nothing.
        String timePref = PING_TIME + "." + app;  // $NON-NLS-1$
long now = System.currentTimeMillis();
long then = prefs.getLong(timePref);
if (now - then < PING_INTERVAL_MSEC) {
//Synthetic comment -- @@ -245,21 +245,21 @@
private static void actuallySendPing(String app, String version, long id)
throws IOException {
// Detect and report the host OS.
        String os = System.getProperty("os.name");          // $NON-NLS-1$
        if (os.startsWith("Mac OS")) {                      // $NON-NLS-1$
            os = "mac";                                     // $NON-NLS-1$
String osVers = getVersion();
if (osVers != null) {
                os = os + "-" + osVers;                     // $NON-NLS-1$
}
        } else if (os.startsWith("Windows")) {              // $NON-NLS-1$
            os = "win";                                     // $NON-NLS-1$
String osVers = getVersion();
if (osVers != null) {
                os = os + "-" + osVers;                     // $NON-NLS-1$
}
        } else if (os.startsWith("Linux")) {                // $NON-NLS-1$
            os = "linux";                                   // $NON-NLS-1$
} else {
// Unknown -- surprising -- send it verbatim so we can see it.
os = URLEncoder.encode(os);
//Synthetic comment -- @@ -269,12 +269,12 @@
// Share the user ID for all apps, to allow unified activity reports.

URL url = new URL(
            "http",                                         // $NON-NLS-1$
            "tools.google.com",                             // $NON-NLS-1$
            "/service/update?as=androidsdk_" + app +        // $NON-NLS-1$
                "&id=" + Long.toHexString(id) +             // $NON-NLS-1$
                "&version=" + version +                     // $NON-NLS-1$
                "&os=" + os);                               // $NON-NLS-1$

// Discard the actual response, but make sure it reads OK
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//Synthetic comment -- @@ -284,7 +284,7 @@
if (conn.getResponseCode() != HttpURLConnection.HTTP_OK &&
conn.getResponseCode() != HttpURLConnection.HTTP_NOT_FOUND) {
throw new IOException(
                conn.getResponseMessage() + ": " + url);    // $NON-NLS-1$
}
}

//Synthetic comment -- @@ -296,11 +296,11 @@
* This method removes any exiting micro versions.
*/
private static String getVersion() {
        Pattern p = Pattern.compile("(\\d+)\\.(\\d+).*"); // $NON-NLS-1$
        String osVers = System.getProperty("os.version"); // $NON-NLS-1$
Matcher m = p.matcher(osVers);
if (m.matches()) {
            return m.group(1) + "." + m.group(2);         // $NON-NLS-1$
}

return null;
//Synthetic comment -- @@ -426,7 +426,7 @@
@Override
public void run() {
for (String cmd : LINUX_BROWSERS) {
                        cmd = cmd.replaceAll("%URL%", url);  // $NON-NLS-1$
try {
Process proc = Runtime.getRuntime().exec(cmd);
if (proc.waitFor() == 0) break;  // Success!








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/DmTraceReader.java b/traceview/src/com/android/traceview/DmTraceReader.java
//Synthetic comment -- index 5a19c19..fb881e2 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private byte[] mBytes = new byte[8];

// A regex for matching the thread "id name" lines in the .key file
    private static final Pattern mIdNamePattern = Pattern.compile("(\\d+)\t(.*)");  // $NON-NLS-1$

DmTraceReader(String traceFileName, boolean regression) {
mTraceFileName = traceFileName;
//Synthetic comment -- @@ -110,7 +110,7 @@
methodId = methodId & ~0x03;
MethodData methodData = mMethodMap.get(methodId);
if (methodData == null) {
            String name = String.format("(0x%1$x)", methodId);  // $NON-NLS-1$
methodData = new MethodData(methodId, name);
}

//Synthetic comment -- @@ -188,7 +188,7 @@
long prevCallTime = 0;
ThreadData threadData = mThreadMap.get(call.getThreadId());
if (threadData == null) {
            String name = String.format("[%1$d]", call.getThreadId());  // $NON-NLS-1$
threadData = new ThreadData(call.getThreadId(), name, mTopLevel);
mThreadMap.put(call.getThreadId(), threadData);
}








//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/TimeLineView.java b/traceview/src/com/android/traceview/TimeLineView.java
//Synthetic comment -- index 67dc97b..875becf 100644

//Synthetic comment -- @@ -153,23 +153,23 @@
mColorZoomSelection = new Color(display, 230, 230, 230);

mFontRegistry = new FontRegistry(display);
        mFontRegistry.put("small",  // $NON-NLS-1$
                new FontData[] { new FontData("Arial", 8, SWT.NORMAL) });  // $NON-NLS-1$
        mFontRegistry.put("courier8",  // $NON-NLS-1$
                new FontData[] { new FontData("Courier New", 8, SWT.BOLD) });  // $NON-NLS-1$
        mFontRegistry.put("medium",  // $NON-NLS-1$
                new FontData[] { new FontData("Courier New", 10, SWT.NORMAL) });  // $NON-NLS-1$

Image image = new Image(display, new Rectangle(100, 100, 100, 100));
GC gc = new GC(image);
if (mSetFonts) {
            gc.setFont(mFontRegistry.get("small"));  // $NON-NLS-1$
}
mSmallFontWidth = gc.getFontMetrics().getAverageCharWidth();
mSmallFontHeight = gc.getFontMetrics().getHeight();

if (mSetFonts) {
            gc.setFont(mFontRegistry.get("medium"));  // $NON-NLS-1$
}
mMediumFontWidth = gc.getFontMetrics().getAverageCharWidth();

//Synthetic comment -- @@ -318,7 +318,7 @@

public void update(Observable objservable, Object arg) {
// Ignore updates from myself
        if (arg == "TimeLineView")  // $NON-NLS-1$
return;
// System.out.printf("timeline update from %s\n", arg);
boolean foundHighlight = false;
//Synthetic comment -- @@ -330,14 +330,14 @@
continue;
String name = selection.getName();
// System.out.printf(" timeline highlight %s from %s\n", name, arg);
            if (name == "MethodData") {  // $NON-NLS-1$
foundHighlight = true;
mHighlightMethodData = (MethodData) selection.getValue();
// System.out.printf(" method %s\n",
// highlightMethodData.getName());
mHighlightCall = null;
startHighlighting();
            } else if (name == "Call") {  // $NON-NLS-1$
foundHighlight = true;
mHighlightCall = (Call) selection.getValue();
// System.out.printf(" call %s\n", highlightCall.getName());
//Synthetic comment -- @@ -354,13 +354,13 @@
records = new ArrayList<Record>();

if (false) {
            System.out.println("TimelineView() list of records:");  // $NON-NLS-1$
for (Record r : records) {
                System.out.printf("row '%s' block '%s' [%d, %d]\n", r.row  // $NON-NLS-1$
.getName(), r.block.getName(), r.block.getStartTime(),
r.block.getEndTime());
if (r.block.getStartTime() > r.block.getEndTime()) {
                    System.err.printf("Error: block startTime > endTime\n");  // $NON-NLS-1$
System.exit(1);
}
}
//Synthetic comment -- @@ -569,7 +569,7 @@
// Set up the off-screen gc
GC gcImage = new GC(image);
if (mSetFonts)
                gcImage.setFont(mFontRegistry.get("medium"));  // $NON-NLS-1$

if (mNumRows > 2) {
// Draw the row background stripes
//Synthetic comment -- @@ -702,7 +702,7 @@
// Set up the off-screen gc
GC gcImage = new GC(image);
if (mSetFonts)
                gcImage.setFont(mFontRegistry.get("medium"));  // $NON-NLS-1$

if (mSurface.drawingSelection()) {
drawSelection(display, gcImage);
//Synthetic comment -- @@ -756,7 +756,7 @@
// Display the maximum data value
double maxVal = mScaleInfo.getMaxVal();
info = mUnits.labelledString(maxVal);
            info = String.format(" max %s ", info);  // $NON-NLS-1$
Point extent = gc.stringExtent(info);
Point dim = getSize();
int x1 = dim.x - RightMargin - extent.x;
//Synthetic comment -- @@ -930,7 +930,7 @@
// Set up the off-screen gc
GC gcImage = new GC(image);
if (mSetFonts)
                gcImage.setFont(mFontRegistry.get("small"));  // $NON-NLS-1$

// Draw the background
// gcImage.setBackground(colorBackground);
//Synthetic comment -- @@ -1035,14 +1035,14 @@
ArrayList<Selection> selections = new ArrayList<Selection>();
// Get the row label
RowData rd = mRows[mMouseRow];
                selections.add(Selection.highlight("Thread", rd.mName));  // $NON-NLS-1$
                selections.add(Selection.highlight("Call", selectBlock));  // $NON-NLS-1$

int mouseX = mMouse.x - LeftMargin;
double mouseXval = mScaleInfo.pixelToValue(mouseX);
                selections.add(Selection.highlight("Time", mouseXval));  // $NON-NLS-1$

                mSelectionController.change(selections, "TimeLineView");  // $NON-NLS-1$
mHighlightMethodData = null;
mHighlightCall = (Call) selectBlock;
startHighlighting();
//Synthetic comment -- @@ -1078,7 +1078,7 @@
if (md == null && mHighlightCall != null)
md = mHighlightCall.getMethodData();
if (md == null)
                    System.out.printf("null highlight?\n");  // $NON-NLS-1$
if (md != null) {
mTimescale.setMethodName(md.getProfileName());
mTimescale.setMethodColor(md.getColor());







