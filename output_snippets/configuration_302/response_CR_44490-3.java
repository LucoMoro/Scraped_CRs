//<Beginning of snippet n. 0>
String name = d.getName();
if (name.equals("3.7 FWVGA slider")) {
    name = "3.7in FWVGA slider";
}

Matcher matcher = PATTERN.matcher(name);
// Add screen resolution and density
int screenWidth = d.getScreenWidth();
int screenHeight = d.getScreenHeight();
float screenDensity = d.getScreenDensity();
String deviceInfo = String.format("%s - Resolution: %dx%d, Density: %.1fdpi", name, screenWidth, screenHeight, screenDensity);

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

// ... existing imports

TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
GridDataBuilder.create(tabFolder).fill().grab().hSpan(2);

TabItem devicesTabItem = new TabItem(tabFolder, SWT.NONE);
devicesTabItem.setText("Devices");

// Create the table to display device details
Table devicesTable = new Table(tabFolder, SWT.BORDER | SWT.FULL_SELECTION);
devicesTable.setHeaderVisible(true);
devicesTable.setLinesVisible(true);
devicesTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

// Set up columns for device name, resolution, and density
TableColumn nameColumn = new TableColumn(devicesTable, SWT.NONE);
nameColumn.setText("Device Name");
nameColumn.setWidth(200);
TableColumn resolutionColumn = new TableColumn(devicesTable, SWT.NONE);
resolutionColumn.setText("Resolution");
resolutionColumn.setWidth(100);
TableColumn densityColumn = new TableColumn(devicesTable, SWT.NONE);
densityColumn.setText("Density (dpi)");
densityColumn.setWidth(100);

// Populate the table with device data
for (Device device : mDeviceManager.getDevices(mUpdaterData.getOsSdkRoot())) {
    String deviceName = device.getName();
    int width = device.getScreenWidth();
    int height = device.getScreenHeight();
    float density = device.getScreenDensity();
    
    TableItem item = new TableItem(devicesTable, SWT.NONE);
    item.setText(new String[]{deviceName, String.format("%dx%d", width, height), String.format("%.1f", density)});
}

//<End of snippet n. 1>

//<Beginning of snippet n. 2>
// ... existing imports and class definition

// Define the createContents method to build UI
private void createContents() {
    mAvdPage = new AvdManagerPage(mShell, SWT.NONE, mUpdaterData, mDeviceManager);
    mAvdPage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
    
    // Add the devices tab to the interface
    TabFolder tabFolder = new TabFolder(mShell, SWT.NONE);
    tabFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));

    TabItem devicesTab = new TabItem(tabFolder, SWT.NONE);
    devicesTab.setText("Devices");
}

// ... existing methods

//<End of snippet n. 2>

//<Beginning of snippet n. 3>
// No changes required
//<End of snippet n. 3>

//<Beginning of snippet n. 4>
// No changes required
//<End of snippet n. 4>

//<Beginning of snippet n. 5>
// No changes required
//<End of snippet n. 5>

//<Beginning of snippet n. 6>
// No changes required
//<End of snippet n. 6>

//<Beginning of snippet n. 7>
// No changes required
//<End of snippet n. 7>