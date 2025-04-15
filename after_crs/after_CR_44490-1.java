/*WIP -- AVD: display devices in a tab+list.

Change-Id:I70c6e0fa0b9622e8050e5d949674377e5ac0ffad*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/DeviceMenuListener.java
//Synthetic comment -- index 32f8e9d..95aeb4c 100644

//Synthetic comment -- @@ -205,9 +205,9 @@
//   same space
// * Add in screen resolution and density
String name = d.getName();
        if (name.equals("3.7 FWVGA slider")) {                        //$NON-NLS-1$
// Fix metadata: this one entry doesn't have "in" like the rest of them
            name = "3.7in FWVGA slider";                              //$NON-NLS-1$
}

Matcher matcher = PATTERN.matcher(name);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AddonSitesDialog.java
//Synthetic comment -- index 2b9f072..77f82b1 100755

//Synthetic comment -- @@ -54,7 +54,6 @@
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
//Synthetic comment -- @@ -110,7 +109,6 @@
shell.setSize(600, 400);

TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
GridDataBuilder.create(tabFolder).fill().grab().hSpan(2);

TabItem sitesTabItem = new TabItem(tabFolder, SWT.NONE);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/AvdManagerWindowImpl1.java
//Synthetic comment -- index ae6ba1c..048ba58 100755

//Synthetic comment -- @@ -31,6 +31,8 @@
import com.android.sdkuilib.repository.AvdManagerWindow.AvdInvocationContext;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.SdkUpdaterWindow;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.utils.ILogger;

import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -40,12 +42,14 @@
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

import java.util.ArrayList;
import java.util.HashMap;
//Synthetic comment -- @@ -169,10 +173,6 @@
});

GridLayout glShell = new GridLayout(2, false);
mShell.setLayout(glShell);

mShell.setMinimumSize(new Point(500, 300));
//Synthetic comment -- @@ -184,8 +184,36 @@

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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
new file mode 100755
//Synthetic comment -- index 0000000..447309e

//Synthetic comment -- @@ -0,0 +1,528 @@
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
import com.android.sdkuilib.internal.repository.UpdaterData;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.ArrayList;
import java.util.List;
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
                onRefresh(true);
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
        boolean mIsUser;
        Device  mDevice;
        TextLayout mWidget;
        Rectangle mBounds;

        CellInfo(boolean isUser, Device device, TextLayout widget) {
            mIsUser = isUser;
            mDevice = device;
            mWidget = widget;
        }
    }

    private void fillTable(Table table) {
        final List<Resource> disposables = new ArrayList<Resource>();

        final Font disposeFont = getBoldFont(table);
        Font boldFont = disposeFont != null ? disposeFont : table.getFont();

        disposables.addAll(fillDevices(table, boldFont, true,  mDeviceManager.getUserDevices()));
        disposables.addAll(fillDevices(table, boldFont, false, mDeviceManager.getDefaultDevices()));
        disposables.addAll(fillDevices(table, boldFont, false, mDeviceManager.getVendorDevices(mUpdaterData.getOsSdkRoot())));

        if (table.getItemCount() == 0) {
            table.setEnabled(true);
            TableItem item = new TableItem(table, SWT.NONE);
            item.setData(null);
            item.setText(0, "No devices available");
            return;
        }

        table.addListener(SWT.PaintItem, new Listener() {
            @Override
            public void handleEvent(Event event) {
                if (event.item != null) {
                    Object info = event.item.getData();
                    if (info instanceof CellInfo) {
                        ((CellInfo) info).mWidget.draw(event.gc, event.x, event.y + 3);
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
                        event.height = bounds.height + 2;
                    }
                }
            }
        });

        table.addDisposeListener(new DisposeListener() {
            @Override
            public void widgetDisposed(DisposeEvent event) {
                for (Resource d : disposables) {
                    d.dispose();
                }
                if (disposeFont != null) {
                    disposeFont.dispose();
                }
            }
        });
    }

    private Font getBoldFont(Table table) {
        Display display = table.getDisplay();
        FontData[] fds = table.getFont().getFontData();
        if (fds != null && fds.length > 0) {
            fds[0].setStyle(SWT.BOLD);
            //--ugly--fds[0].setHeight(fds[0].getHeight()+2);
            return new Font(display, fds[0]);
        }
        return null;
    }

    private List<Resource> fillDevices(Table table, Font boldFont, boolean isUser, List<Device> devices) {
        List<Resource> disposables = new ArrayList<Resource>();
        Display display = table.getDisplay();

        TextStyle boldStyle = new TextStyle();
        boldStyle.font = boldFont;

        for (Device device : devices) {
            TableItem item = new TableItem(table, SWT.NONE);
            TextLayout widget = new TextLayout(display);
            CellInfo ci = new CellInfo(isUser, device, widget);
            item.setData(ci);

            widget.setIndent(mImageWidth * 2);
            widget.setFont(table.getFont());

            StringBuilder sb = new StringBuilder();
            String name = getGenericLabel(device);
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
            sb.append("\n\n");

            Hardware hw = device.getDefaultHardware();
            Screen screen = hw.getScreen();
            sb.append(String.format(java.util.Locale.US,
                        "Screen: %1$.1f\", %2$d \u00D7 %3$d, %4$s %5$s\n", // U+00D7: Unicode multiplication sign
                        screen.getDiagonalLength(),
                        screen.getXDimension(),
                        screen.getYDimension(),
                        screen.getSize().getShortDisplayValue(),
                        screen.getPixelDensity().getResourceValue()
                        ));

            Storage sto = hw.getRam();
            Unit unit = sto.getSizeAsUnit(Unit.GiB) > 1 ? Unit.GiB : Unit.MiB;
            sb.append(String.format(java.util.Locale.US,
                    "RAM: %1$d %2$s\n",
                    sto.getSizeAsUnit(unit),
                    unit));


            widget.setText(sb.toString());
            widget.setStyle(boldStyle, 0, pos1);
        }

        return disposables;
    }

    // Extracted from DeviceMenuListerner -- TODO refactor somewhere else
    private static final String NEXUS = "Nexus";       //$NON-NLS-1$
    private static final String GENERIC = "Generic";   //$NON-NLS-1$
    private static Pattern PATTERN = Pattern.compile(
            "(\\d+\\.?\\d*)in (.+?)( \\(.*Nexus.*\\))?"); //$NON-NLS-1$
    private static String getGenericLabel(Device d) {
        // * Replace "'in'" with '"' (e.g. 2.7" QVGA instead of 2.7in QVGA)
        // * Use the same precision for all devices (all but one specify decimals)
        // * Add some leading space such that the dot ends up roughly in the
        //   same space
        // * Add in screen resolution and density
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
            for (int i = 0; i < 2 - dot; i++) {
                size = ' ' + size;
            }
            name = size + "\" " + n;
        }

        return name;
    }

    private void onNewDevice() {
        // TODO Auto-generated method stub

    }

    private void onEditDevice() {
        // TODO Auto-generated method stub

    }

    private void onDeleteDevice() {
        // TODO Auto-generated method stub

    }

    private void onNewAvd() {
        // TODO Auto-generated method stub

    }

    private void onRefresh(boolean b) {
        // TODO Auto-generated method stub

    }

    // -------


    // --- Implementation of ISdkChangeListener ---

    @Override
    public void onSdkLoaded() {
        onSdkReload();
    }

    @Override
    public void onSdkReload() {
        //--TODO FIXME --mAvdSelector.refresh(false /*reload*/);
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
        //--TODO FIXME --        mAvdSelector.refresh(false /*reload*/);
    }


    // End of hiding from SWT Designer
    //$hide<<$
}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index ab8e1c9..42d85eb 100644

//Synthetic comment -- @@ -438,6 +438,7 @@
public void setSettingsController(SettingsController controller) {
mController = controller;
}

/**
* Sets the table grid layout data.
*








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridLayoutBuilder.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/ui/GridLayoutBuilder.java
//Synthetic comment -- index fbb31ce..7e8c161 100755

//Synthetic comment -- @@ -30,7 +30,7 @@
*/
public final class GridLayoutBuilder {

    private GridLayout mGL;

private GridLayoutBuilder() {
mGL = new GridLayout();
//Synthetic comment -- @@ -41,7 +41,7 @@
*/
static public GridLayoutBuilder create(Composite parent) {
GridLayoutBuilder glh = new GridLayoutBuilder();
        parent.setLayout(glh.mGL);
return glh;
}








