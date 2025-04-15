/*Adding right-click menu to LogCat DDMS.

Change-Id:Ib38a9ef4673f5f15b313aca4cba6f90f4a2e7dd0*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java
//Synthetic comment -- index 80ed6e9..6cd63fa 100644

//Synthetic comment -- @@ -42,9 +42,13 @@
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
//Synthetic comment -- @@ -54,6 +58,8 @@
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
//Synthetic comment -- @@ -323,6 +329,51 @@
}

/**
     * Menu class for displaying popup menu on TabFolder.
     */
    private class TabMenu {

        private TabFolder folder;

        private Menu menu;

        private MenuItem edit;

        TabMenu(TabFolder parent) {
            folder = parent;
            menu = new Menu(folder.getShell(), SWT.POP_UP);
            edit = new MenuItem(menu, SWT.POP_UP);
            edit.setText("Edit");
            edit.addSelectionListener(new SelectionListener() {
                @Override
                public void widgetSelected(SelectionEvent event) {
                    editFilter();
                }
                @Override
                public void widgetDefaultSelected(SelectionEvent event) {
                    // Do nothing
                }
            });
        }

        private void showTabMenu(Point point) {
            int selectionIndex = folder.getSelectionIndex();

            // ignore default logfilter
            if (selectionIndex != 0) {
                TabItem[] selection = folder.getSelection();
                if (selection[0].getBounds().contains(point)) {
                    Point pt = folder.toDisplay(point);

                    //draw menu
                    menu.setLocation (pt.x, pt.y);
                    menu.setVisible (true);
                }
            }
        }
    }

    /**
* Create the log view with some default parameters
* @param colors The display color object
* @param filterStorage the storage for user defined filters.
//Synthetic comment -- @@ -394,6 +445,9 @@
// pass
}

    private TabMenu mTabmenu = null;

    private static final int MOUSE_CLICK_RIGHT = 3;

/**
* Creates a control capable of displaying some information.  This is
//Synthetic comment -- @@ -410,6 +464,10 @@
// create the tab folder
mFolders = new TabFolder(top, SWT.NONE);
mFolders.setLayoutData(new GridData(GridData.FILL_BOTH));

        // create the tab menu
        mTabmenu = new TabMenu(mFolders);

mFolders.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent e) {
//Synthetic comment -- @@ -426,6 +484,28 @@
}
});

        mFolders.addMouseListener(new MouseListener() {
            private final Point point = new Point (0, 0);

            @Override
            public void mouseDown(MouseEvent event) {
                switch (event.button) {
                    case MOUSE_CLICK_RIGHT:
                        point.x = event.x;
                        point.y = event.y;
                        mTabmenu.showTabMenu(point);
                        break;
                }
            }
            @Override
            public void mouseUp(MouseEvent event) {
                // Do nothing
            }
            @Override
            public void mouseDoubleClick(MouseEvent event) {
                // Do nothing
            }
        });

Composite bottom = new Composite(top, SWT.NONE);
bottom.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));







