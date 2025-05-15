
//<Beginning of snippet n. 0>


//   same space
// * Add in screen resolution and density
String name = d.getName();
        if (name.equals("3.7 FWVGA slider")) {                        //$NON-NLS-1$
// Fix metadata: this one entry doesn't have "in" like the rest of them
            name = "3.7in FWVGA slider";                              //$NON-NLS-1$
}

Matcher matcher = PATTERN.matcher(name);

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
shell.setSize(600, 400);

TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
GridDataBuilder.create(tabFolder).fill().grab().hSpan(2);

TabItem sitesTabItem = new TabItem(tabFolder, SWT.NONE);

//<End of snippet n. 1>










//<Beginning of snippet n. 2>




import com.android.SdkConstants;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.internal.repository.ITaskFactory;
import com.android.sdkuilib.internal.repository.AboutDialog;
import com.android.sdkuilib.internal.repository.SettingsDialog;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.utils.ILogger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
* This is an intermediate version of the {@link AvdManagerPage}
});

GridLayout glShell = new GridLayout(2, false);
mShell.setLayout(glShell);

mShell.setMinimumSize(new Point(500, 300));

private void createContents() {

        TabFolder tabFolder = new TabFolder(mShell, SWT.NONE);
        GridDataBuilder.create(tabFolder).fill().grab().hSpan(2);

        // avd tab
        TabItem avdTabItem = new TabItem(tabFolder, SWT.NONE);
        avdTabItem.setText("Android Virtual Devices");
        createAvdTab(tabFolder, avdTabItem);

        // device tab
        TabItem devTabItem = new TabItem(tabFolder, SWT.NONE);
        devTabItem.setText("Device Definitions");
        createDeviceTab(tabFolder, devTabItem);
    }

    private void createAvdTab(TabFolder tabFolder, TabItem avdTabItem) {
        Composite root = new Composite(tabFolder, SWT.NONE);
        avdTabItem.setControl(root);
        GridLayoutBuilder.create(root).columns(1);

        mAvdPage = new AvdManagerPage(root, SWT.NONE, mUpdaterData, mDeviceManager);
        GridDataBuilder.create(mAvdPage).fill().grab();
    }

    private void createDeviceTab(TabFolder tabFolder, TabItem devTabItem) {
        Composite root = new Composite(tabFolder, SWT.NONE);
        devTabItem.setControl(root);
        GridLayoutBuilder.create(root).columns(1);

        DeviceManagerPage container = new DeviceManagerPage(root, SWT.NONE, mUpdaterData, mDeviceManager);
        GridDataBuilder.create(container).fill().grab();
}

@SuppressWarnings("unused")
e.printStackTrace();
}
}
}


}

/**
* Initializes settings.
* This must be called after addExtraPages(), which created a settings page.
* Iterate through all the pages to find the first (and supposedly unique) setting page,

//<End of snippet n. 2>










//<Beginning of snippet n. 3>

new file mode 100755

/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.sdkuilib.internal.repository.ui;

import com.android.sdklib.devices.Device;
import com.android.sdklib.devices.DeviceManager;
import com.android.sdklib.devices.DeviceManager.DevicesChangeListener;
import com.android.sdklib.devices.Hardware;
import com.android.sdklib.devices.Screen;
import com.android.sdklib.devices.Storage;
import com.android.sdklib.devices.Storage.Unit;
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.widgets.AvdCreationDialog;
import com.android.sdkuilib.internal.widgets.DeviceCreationDialog;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Resource;
import org.eclipse.swt.graphics.TextLayout;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A  page displaying Device Manager entries.
 */
public class DeviceManagerPage extends Composite
    implements ISdkChangeListener, DevicesChangeListener, DisposeListener {

    private final UpdaterData mUpdaterData;
    private final DeviceManager mDeviceManager;
    private Table mTable;
    private Button mNewButton;
    private Button mEditButton;
    private Button mDeleteButton;
    private Button mNewAvdButton;
    private Button mRefreshButton;
    private ImageFactory mImageFactory;
    private Image mUserImage;
    private Image mGenericImage;
    private Image mOtherImage;
    private int mImageWidth;
    private boolean mDisableRefresh;
    /**
     * Create the composite.
     * @param parent The parent of the composite.
     * @param updaterData An instance of {@link UpdaterData}.
     */
    public DeviceManagerPage(Composite parent,
            int swtStyle,
            UpdaterData updaterData,
            DeviceManager deviceManager) {
        super(parent, swtStyle);

        mUpdaterData = updaterData;
        mUpdaterData.addListeners(this);

        mDeviceManager = deviceManager;
        mDeviceManager.registerListener(this);

        createContents(this);
        postCreate();  //$hide$
    }

    private void createContents(Composite parent) {

        // get some bitmaps.
        mImageFactory = new ImageFactory(parent.getDisplay());
        mUserImage = mImageFactory.getImageByName("accept_icon16.png"); // TODO change icons
        mGenericImage = mImageFactory.getImageByName("broken_16.png");
        mOtherImage = mImageFactory.getImageByName("reject_icon16.png");
        mImageWidth = Math.max(mGenericImage.getImageData().width,
                        Math.max(mUserImage.getImageData().width,
                                  mOtherImage.getImageData().width));

        // Layout has 2 columns
        GridLayoutBuilder.create(parent).columns(2);

        // Insert a top label explanation. This matches the design in AvdManagerPage so
        // that the table starts at the same height on both tabs.
        Label label = new Label(parent, SWT.NONE);
        label.setText("List of known device definitions. This can later be used to create Android Virtual Devices.");
        GridDataBuilder.create(label).hSpan(2);

        // Device table.
        mTable = new Table(parent, SWT.FULL_SELECTION | SWT.SINGLE | SWT.BORDER);
        mTable.setHeaderVisible(true);
        mTable.setLinesVisible(true);
        mTable.setFont(parent.getFont());
        setTableHeightHint(30);

        // Buttons on the side.
        Composite buttons = new Composite(parent, SWT.NONE);
        GridLayoutBuilder.create(buttons).columns(1).noMargins();
        GridDataBuilder.create(buttons).vFill();
        buttons.setFont(parent.getFont());

        mNewButton = new Button(buttons, SWT.PUSH | SWT.FLAT);
        mNewButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mNewButton.setText("New...");
        mNewButton.setToolTipText("Creates a new user device definition.");
        mNewButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                onNewDevice();
            }
        });

        mEditButton = new Button(buttons, SWT.PUSH | SWT.FLAT);
        mEditButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mEditButton.setText("Edit...");
        mEditButton.setToolTipText("Edit an existing device definition.");
        mEditButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                onEditDevice();
            }
        });

        mDeleteButton = new Button(buttons, SWT.PUSH | SWT.FLAT);
        mDeleteButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mDeleteButton.setText("Delete...");
        mDeleteButton.setToolTipText("Deletes the selected AVD.");
        mDeleteButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                onDeleteDevice();
            }
        });

        @SuppressWarnings("unused")
        Label spacing = new Label(buttons, SWT.NONE);

        mNewAvdButton = new Button(buttons, SWT.PUSH | SWT.FLAT);
        mNewAvdButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mNewAvdButton.setText("Create AVD...");
        mNewAvdButton.setToolTipText("Creates a new AVD based on this device definition.");
        mNewAvdButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                onNewAvd();
            }
        });

        Composite padding = new Composite(buttons, SWT.NONE);
        padding.setLayoutData(new GridData(GridData.FILL_VERTICAL));

        mRefreshButton = new Button(buttons, SWT.PUSH | SWT.FLAT);
        mRefreshButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        mRefreshButton.setText("Refresh");
        mRefreshButton.setToolTipText("Reloads the list of devices.");
        mRefreshButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                onRefresh();
            }
        });

        // Legend at the bottom.
        // This matches the one on AvdSelector so that the table height in the tab be similar.
        Composite legend = new Composite(parent, SWT.NONE);
        GridLayoutBuilder.create(legend).columns(4).noMargins();
        GridDataBuilder.create(legend).hFill().vTop().hGrab().hSpan(2);
        legend.setFont(parent.getFont());

        new Label(legend, SWT.NONE).setImage(mUserImage);
        new Label(legend, SWT.NONE).setText("A user-created device definition.");
        new Label(legend, SWT.NONE).setImage(mGenericImage);
        new Label(legend, SWT.NONE).setText("A generic device definition.");
        new Label(legend, SWT.NONE).setImage(mOtherImage);
        Label l = new Label(legend, SWT.NONE);
        l.setText("Some other manufacturer.");
        GridData gd;
        l.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
        gd.horizontalSpan = 3;


        // create the table columns
        final TableColumn column0 = new TableColumn(mTable, SWT.NONE);
        column0.setText("Device");

        adjustColumnsWidth(mTable, column0);
        setupSelectionListener(mTable);
        fillTable(mTable);
        setEnabled(true);

    }

    private void adjustColumnsWidth(final Table table, final TableColumn column0) {
        // Add a listener to resize the column to the full width of the table
        table.addControlListener(new ControlAdapter() {
            @Override
            public void controlResized(ControlEvent e) {
                Rectangle r = table.getClientArea();
                column0.setWidth(r.width * 100 / 100 - 1); // 100%
            }
        });
    }

    private void setupSelectionListener(Table table) {
        // TODO Auto-generated method stub

    }

    /**
     * Sets the table grid layout data.
     *
     * @param heightHint If > 0, the height hint is set to the requested value.
     */
    public void setTableHeightHint(int heightHint) {
        GridData data = new GridData();
        if (heightHint > 0) {
            data.heightHint = heightHint;
        }
        data.grabExcessVerticalSpace = true;
        data.grabExcessHorizontalSpace = true;
        data.horizontalAlignment = GridData.FILL;
        data.verticalAlignment = GridData.FILL;
        mTable.setLayoutData(data);
    }

    @Override
    public void widgetDisposed(DisposeEvent e) {
        dispose();
    }

    @Override
    public void dispose() {
        mUpdaterData.removeListener(this);
        mDeviceManager.unregisterListener(this);
        super.dispose();
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

    // -- Start of internal part ----------
    // Hide everything down-below from SWT designer
    //$hide>>$

    /**
     * Called by the constructor right after {@link #createContents(Composite)}.
     */
    private void postCreate() {
        // nothing to be done for now.
    }


    // -------

    private static class CellInfo {
        final boolean mIsUser;
        final Device  mDevice;
        final TextLayout mWidget;
        Rectangle mBounds;

        CellInfo(boolean isUser, Device device, TextLayout widget) {
            mIsUser = isUser;
            mDevice = device;
            mWidget = widget;
        }
    }

    private void fillTable(final Table table) {

        table.removeAll();
        disposeTableResources(table.getData("disposeResources"));

        final List<Resource> disposables = new ArrayList<Resource>();

        Font boldFont = getBoldFont(table);
        if (boldFont != null) {
            disposables.add(boldFont);
        } else {
            boldFont = table.getFont();
        }

        try {
            mDisableRefresh = true;
            disposables.addAll(fillDevices(table, boldFont, true,
                    mDeviceManager.getUserDevices(),
                    null));
            disposables.addAll(fillDevices(table, boldFont, false,
                    mDeviceManager.getDefaultDevices(),
                    mDeviceManager.getVendorDevices(mUpdaterData.getOsSdkRoot())));
        } finally {
            mDisableRefresh = false;
        }

        table.setData("disposeResources", disposables);

        if (!Boolean.TRUE.equals(table.getData("createdTableListeners"))) {
            table.addListener(SWT.PaintItem, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    if (event.item != null) {
                        Object info = event.item.getData();
                        if (info instanceof CellInfo) {
                            ((CellInfo) info).mWidget.draw(event.gc, event.x, event.y + 1);
                        }
                    }
                }
            });

            table.addListener(SWT.MeasureItem, new Listener() {
                @Override
                public void handleEvent(Event event) {
                    if (event.item != null) {
                        Object info = event.item.getData();
                        if (info instanceof CellInfo) {
                            CellInfo ci = (CellInfo) info;
                            Rectangle bounds = ci.mBounds;
                            if (bounds == null) {
                                // TextLayout.getBounds() seems expensive, so let's cache it.
                                ci.mBounds = bounds = ci.mWidget.getBounds();
                            }
                            event.width = bounds.width + 2;
                            event.height = bounds.height + 4;
                        }
                    }
                }
            });

            table.addDisposeListener(new DisposeListener() {
                @Override
                public void widgetDisposed(DisposeEvent event) {
                    disposeTableResources(table.getData("disposeResources"));
                }
            });

            table.addSelectionListener(new SelectionListener() {
                /** Handles single clicks on a row. */
                @Override
                public void widgetSelected(SelectionEvent event) {
                    updateButtonStates();
                }

                /** Handles double click on a row. */
                @Override
                public void widgetDefaultSelected(SelectionEvent event) {
                    // FIXME: should double-click be to edit a device or create a new AVD?
                    onEditDevice();
                }
            });
        }

        if (table.getItemCount() == 0) {
            table.setEnabled(true);
            TableItem item = new TableItem(table, SWT.NONE);
            item.setData(null);
            item.setText(0, "No devices available");
            return;
        }

        table.setData("createdTableListeners", Boolean.TRUE);
    }

    private void disposeTableResources(Object disposablesList) {
        if (disposablesList instanceof List<?>) {
            for (Object obj : (List<?>) disposablesList) {
                if (obj instanceof Resource) {
                    ((Resource) obj).dispose();
                }
            }
        }
    }

    private Font getBoldFont(Table table) {
        Display display = table.getDisplay();
        FontData[] fds = table.getFont().getFontData();
        if (fds != null && fds.length > 0) {
            fds[0].setStyle(SWT.BOLD);
            return new Font(display, fds[0]);
        }
        return null;
    }

    private List<Resource> fillDevices(
            Table table,
            Font boldFont,
            boolean isUser,
            List<Device> devices1,
            List<Device> devices2) {
        List<Resource> disposables = new ArrayList<Resource>();
        Display display = table.getDisplay();

        TextStyle boldStyle = new TextStyle();
        boldStyle.font = boldFont;


        List<Device> devices = new ArrayList<Device>(devices1);
        if (devices2 != null) {
            devices.addAll(devices2);
        }

        if (isUser) {
            // Just sort user devices by alphabetical name. They will show up at the top.
            Collections.sort(devices, new Comparator<Device>() {
                @Override
                public int compare(Device d1, Device d2) {
                    String s1 = d1 == null ? "" : d1.getName();
                    String s2 = d2 == null ? "" : d2.getName();
                    return s1.compareTo(s2);
                }});
        } else {
            // Sort non-user devices by descending "pretty name"
            // TODO revisit. Doesn't perform as well as expected.
            Collections.sort(devices, new Comparator<Device>() {
                @Override
                public int compare(Device d1, Device d2) {
                    String s1 = getPrettyName(d1, true /*leadZeroes*/);
                    String s2 = getPrettyName(d2, true /*leadZeroes*/);
                    return s2.compareTo(s1);
                }});
        }

        // Generate a list of the AVD names using these devices
        Map<Device, List<String>> device2avdMap = new HashMap<Device, List<String>>();
        for (AvdInfo avd : mUpdaterData.getAvdManager().getAllAvds()) {
            String n = avd.getDeviceName();
            String m = avd.getDeviceManufacturer();
            if (n == null || m == null || n.isEmpty() || m.isEmpty()) {
                continue;
            }
            for (Device device : devices) {
                if (m.equals(device.getManufacturer()) && n.equals(device.getName())) {
                    List<String> list = device2avdMap.get(device);
                    if (list == null) {
                        list = new LinkedList<String>();
                        device2avdMap.put(device, list);
                    }
                    list.add(avd.getName());
                }
            }
        }

        final String prefix = "\n    ";

        for (Device device : devices) {
            TableItem item = new TableItem(table, SWT.NONE);
            TextLayout widget = new TextLayout(display);
            CellInfo ci = new CellInfo(isUser, device, widget);
            item.setData(ci);

            widget.setIndent(mImageWidth * 2);
            widget.setFont(table.getFont());

            StringBuilder sb = new StringBuilder();
            String name = getPrettyName(device, false /*leadZeroes*/);
            sb.append(name);
            int pos1 = sb.length();

            String manu = device.getManufacturer();
            if (isUser) {
                item.setImage(mUserImage);
            } else if (GENERIC.equals(manu)) {
                item.setImage(mGenericImage);
            } else {
                item.setImage(mOtherImage);
                sb.append("  by ").append(device.getManufacturer());
            }

            Hardware hw = device.getDefaultHardware();
            Screen screen = hw.getScreen();
            sb.append(prefix);
            sb.append(String.format(java.util.Locale.US,
                        "Screen:   %1$.1f\", %2$d \u00D7 %3$d, %4$s %5$s", // U+00D7: Unicode multiplication sign
                        screen.getDiagonalLength(),
                        screen.getXDimension(),
                        screen.getYDimension(),
                        screen.getSize().getShortDisplayValue(),
                        screen.getPixelDensity().getResourceValue()
                        ));

            Storage sto = hw.getRam();
            Unit unit = sto.getSizeAsUnit(Unit.GiB) > 1 ? Unit.GiB : Unit.MiB;
            sb.append(prefix);
            sb.append(String.format(java.util.Locale.US,
                    "RAM:       %1$d %2$s",
                    sto.getSizeAsUnit(unit),
                    unit));

            List<String> avdList = device2avdMap.get(device);
            if (avdList != null && !avdList.isEmpty()) {
                sb.append(prefix);
                sb.append("Used by: ");
                boolean first = true;
                for (String avd : avdList) {
                    if (!first) {
                        sb.append(", ");
                    }
                    sb.append(avd);
                    first = false;
                }
            }

            widget.setText(sb.toString());
            widget.setStyle(boldStyle, 0, pos1);
        }

        return disposables;
    }

    // Constants extracted from DeviceMenuListerner -- TODO refactor somewhere else.
    @SuppressWarnings("unused")
    private static final String NEXUS = "Nexus";       //$NON-NLS-1$
    private static final String GENERIC = "Generic";   //$NON-NLS-1$
    private static Pattern PATTERN = Pattern.compile(
            "(\\d+\\.?\\d*)in (.+?)( \\(.*Nexus.*\\))?"); //$NON-NLS-1$
    /**
     * Returns a pretty name for the device.
     *
     * Extracted from DeviceMenuListerner.
     * Modified to remove the leading space insertion as it doesn't render
     * neatly in the avd manager. Instead added the option to add leading
     * zeroes to make the string names sort properly.
     *
     * Replace "'in'" with '"' (e.g. 2.7" QVGA instead of 2.7in QVGA)
     * Use the same precision for all devices (all but one specify decimals)
     * Add in screen resolution and density
     */
    private static String getPrettyName(Device d, boolean leadZeroes) {
        if (d == null) {
            return "";
        }
        String name = d.getName();
        if (name.equals("3.7 FWVGA slider")) {                        //$NON-NLS-1$
            // Fix metadata: this one entry doesn't have "in" like the rest of them
            name = "3.7in FWVGA slider";                              //$NON-NLS-1$
        }

        Matcher matcher = PATTERN.matcher(name);
        if (matcher.matches()) {
            String size = matcher.group(1);
            String n = matcher.group(2);
            int dot = size.indexOf('.');
            if (dot == -1) {
                size = size + ".0";
                dot = size.length() - 2;
            }
            if (leadZeroes && dot < 3) {
                // Pad to have at least 3 digits before the dot, for sorting purposes.
                // We can revisit this once we get devices that are more than 999 inches wide.
                size = "000".substring(dot) + size;
            }
            name = size + "\" " + n;
        }

        return name;
    }

    /**
     * Returns the currently selected cell info in the table or null
     */
    private CellInfo getTableSelection() {
        if (mTable.isDisposed()) {
            return null;
        }
        int selIndex = mTable.getSelectionIndex();
        if (selIndex >= 0) {
            return (CellInfo) mTable.getItem(selIndex).getData();
        }

        return null;
    }

    private void updateButtonStates() {
        CellInfo ci = getTableSelection();

        mNewButton.setEnabled(true);
        mEditButton.setEnabled(ci != null);
        mDeleteButton.setEnabled(ci != null && ci.mIsUser);
        mNewAvdButton.setEnabled(ci != null);
        mRefreshButton.setEnabled(true);
    }

    private void onNewDevice() {
        DeviceCreationDialog dlg = new DeviceCreationDialog(
                getShell(),
                mDeviceManager,
                mUpdaterData.getImageFactory(),
                null /*device*/);
        if (dlg.open() == Window.OK) {
            onRefresh();

            // Select the new device, if any.
            selectCellByDevice(dlg.getCreatedDevice());
        }
    }

    private void onEditDevice() {
        CellInfo ci = getTableSelection();
        if (ci == null || ci.mDevice == null) {
            return;
        }

        DeviceCreationDialog dlg = new DeviceCreationDialog(
                getShell(),
                mDeviceManager,
                mUpdaterData.getImageFactory(),
                ci.mDevice);
        if (dlg.open() == Window.OK) {
            onRefresh();

            // Select the new device, if any.
            selectCellByDevice(dlg.getCreatedDevice());
        }
    }

    private void onDeleteDevice() {
        CellInfo ci = getTableSelection();
        if (ci == null || ci.mDevice == null || !ci.mIsUser) {
            return;
        }

        final String name = getPrettyName(ci.mDevice, false /*leadZeroes*/);
        final AtomicBoolean result = new AtomicBoolean(false);
        getDisplay().syncExec(new Runnable() {
            @Override
            public void run() {
                Shell shell = getDisplay().getActiveShell();
                boolean ok = MessageDialog.openQuestion(shell,
                        "Delete Device Definition",
                        String.format(
                                "Please confirm that you want to delete the device definition named '%s'. This operation cannot be reverted.",
                                name));
                result.set(ok);
            }
        });

        if (result.get()) {
            mDeviceManager.removeUserDevice(ci.mDevice);
            onRefresh();
        }
    }

    private void onNewAvd() {
        CellInfo ci = getTableSelection();
        if (ci == null || ci.mDevice == null) {
            return;
        }

        AvdCreationDialog dlg = new AvdCreationDialog(mTable.getShell(),
                mUpdaterData.getAvdManager(),
                mImageFactory,
                mUpdaterData.getSdkLog(),
                null);
        dlg.setlectInitialDevice(ci.mDevice);

        if (dlg.open() == Window.OK) {
            onRefresh();
        }
    }

    private void onRefresh() {
        if (mDisableRefresh || mTable.isDisposed()) {
            return;
        }
        int selIndex = mTable.getSelectionIndex();
        CellInfo selected = getTableSelection();

        fillTable(mTable);

        if (selected != null) {
            if (selectCellByName(selected)) {
                return;
            }
        }
        // If not found by name, use the position if available.
        if (selIndex >= 0 && selIndex < mTable.getItemCount()) {
            mTable.select(selIndex);
        }
    }

    private boolean selectCellByName(CellInfo selected) {
        if (mTable.isDisposed() || selected == null || selected.mDevice == null) {
            return false;
        }
        String name = selected.mDevice.getName();
        for (int n = mTable.getItemCount() - 1; n >= 0; n--) {
            TableItem item = mTable.getItem(n);
            Object data = item.getData();
            if (data instanceof CellInfo) {
                CellInfo ci = (CellInfo) data;
                if (ci != null && ci.mDevice != null && name.equals(ci.mDevice.getName())) {
                    // Same cell object. Select it.
                    mTable.select(n);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean selectCellByDevice(Device selected) {
        if (mTable.isDisposed() || selected == null) {
            return false;
        }
        for (int n = mTable.getItemCount() - 1; n >= 0; n--) {
            TableItem item = mTable.getItem(n);
            Object data = item.getData();
            if (data instanceof CellInfo) {
                CellInfo ci = (CellInfo) data;
                if (ci != null && ci.mDevice == selected) {
                    // Same device object. Select it.
                    mTable.select(n);
                    return true;
                }
            }
        }
        return false;
    }

    // -------


    // --- Implementation of ISdkChangeListener ---

    @Override
    public void onSdkLoaded() {
        onSdkReload();
    }

    @Override
    public void onSdkReload() {
        onRefresh();
    }

    @Override
    public void preInstallHook() {
        // nothing to be done for now.
    }

    @Override
    public void postInstallHook() {
        // nothing to be done for now.
    }

    // --- Implementation of DevicesChangeListener

    @Override
    public void onDevicesChange() {
        onRefresh();
    }


    // End of hiding from SWT Designer
    //$hide<<$
}

//<End of snippet n. 3>










//<Beginning of snippet n. 4>


}
}
};
    private Device mInitWithDevice;

public AvdCreationDialog(Shell shell,
AvdManager avdManager,

if (mAvdInfo != null) {
fillExistingAvdInfo(mAvdInfo);
        } else if (mInitWithDevice != null) {
            fillInitialDeviceInfo(mInitWithDevice);
}

validatePage();
mStatusLabel = new Label(mStatusComposite, SWT.NONE);
mStatusLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
mStatusLabel.setText(""); //$NON-NLS-1$
    }

    /**
     * Can be called after the constructor to set the default device for this AVD.
     * Useful especially for new AVDs.
     * @param device
     */
    public void setlectInitialDevice(Device device) {
        mInitWithDevice = device;
}

/**
}
}

    private void fillInitialDeviceInfo(Device device) {
        String name = device.getManufacturer();
        if (!name.equals("Generic") && !name.equals("User")) { // TODO define & use constants
            name = " by " + name;
        } else {
            name = "";
        }
        name = "AVD for " + device.getName() + name;
        // sanitize the name
        name = name.replaceAll("[^0-9a-zA-Z_-]+", " ").trim().replaceAll("[ _]+", "_");
        mAvdName.setText(name);

        String manufacturer = device.getManufacturer();
        for (int i = 0; i < mDeviceManufacturer.getItemCount(); i++) {
            if (mDeviceManufacturer.getItem(i).equals(manufacturer)) {
                mDeviceManufacturer.select(i);
                break;
            }
        }
        reloadDeviceNameCombo();

        String deviceName = device.getName();
        for (int i = 0; i < mDeviceName.getItemCount(); i++) {
            if (mDeviceName.getItem(i).equals(deviceName)) {
                mDeviceName.select(i);
                break;
            }
        }
        toggleCameras();

    }

/**
* Returns the list of system images of a target.
* <p/>

return new ISystemImage[0];
}
}

//<End of snippet n. 4>










//<Beginning of snippet n. 5>


public void setSettingsController(SettingsController controller) {
mController = controller;
}

/**
* Sets the table grid layout data.
*

//<End of snippet n. 5>










//<Beginning of snippet n. 6>



private Button mOkButton;

    /** The hardware instance attached to each of the states of the created device. */
private Hardware mHardware;
    /** The instance of the Device created by the dialog, if the user pressed {@code mOkButton}. */
    private Device mCreatedDevice;

    /**
     * This contains the Software for the device. Since it has no effect on the
     * emulator whatsoever, we just use a single instance with reasonable
     * defaults. */
private static final Software mSoftware;

static {
mUserDevices = mManager.getUserDevices();
}

    /**
     * Returns the instance of the Device created by the dialog,
     * if the user pressed the OK|create|edit|clone button.
     * Typically only non-null if the dialog returns OK.
     */
    public Device getCreatedDevice() {
        return mCreatedDevice;
    }

@Override
protected Control createContents(Composite parent) {
Control control = super.createContents(parent);
mManager.addUserDevice(d);
}
mManager.saveUserDevices();
            mCreatedDevice = d;
super.okPressed();
}
}

//<End of snippet n. 6>










//<Beginning of snippet n. 7>


*/
public final class GridLayoutBuilder {

    private GridLayout mGL;

private GridLayoutBuilder() {
mGL = new GridLayout();
*/
static public GridLayoutBuilder create(Composite parent) {
GridLayoutBuilder glh = new GridLayoutBuilder();
        parent.setLayout(glh.mGL);
return glh;
}


//<End of snippet n. 7>








