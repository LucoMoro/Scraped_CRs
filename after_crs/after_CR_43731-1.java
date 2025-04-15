/*logcat: Support auto scroll lock with mousewheel actions

Another go at adding support for automatically enabling/disabling
scroll lock. Past attempts have been brittle because of differences
in how Windows, Linux and Mac handle the scrollbar.

This CL attempts to work around those differences by relying more
on the mousewheel events and less on the scroll bar events/locations.
Specifically, scrolling up using the mouse wheel indicates a desire
to view earlier items and this automatically enables scroll lock.
Scrolling down using the mouse wheel disables scroll lock only when
the scrollbar has reached the bottom. And since we can't reliably
detect when the scrollbar has reached the bottom, we just rely on
whether any scroll events are generated if the user attempts to
scroll down further using the mouse.

Change-Id:Ic3259cc4e2e8a20f3a87ce4bf234217cea792f88*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogCatPanel.java
//Synthetic comment -- index e7dcec9..9a9e226 100644

//Synthetic comment -- @@ -19,13 +19,13 @@
import com.android.ddmlib.DdmConstants;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.Log.LogLevel;
import com.android.ddmuilib.AbstractBufferFindTarget;
import com.android.ddmuilib.FindDialog;
import com.android.ddmuilib.ITableFocusListener;
import com.android.ddmuilib.ITableFocusListener.IFocusedTableActivator;
import com.android.ddmuilib.ImageLoader;
import com.android.ddmuilib.SelectionDependentPanel;
import com.android.ddmuilib.TableHelper;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
//Synthetic comment -- @@ -49,6 +49,8 @@
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
//Synthetic comment -- @@ -171,6 +173,8 @@

private boolean mShouldScrollToLatestLog = true;
private ToolItem mScrollLockCheckBox;
    private int mLastVerticalBarScrollTime;
    private int mLastMouseScrollTime;

private String mLogFileExportFolder;

//Synthetic comment -- @@ -682,13 +686,13 @@
mScrollLockCheckBox.setImage(
ImageLoader.getDdmUiLibLoader().loadImage(IMAGE_SCROLL_LOCK,
toolBar.getDisplay()));
        mScrollLockCheckBox.setSelection(true);
mScrollLockCheckBox.setToolTipText("Scroll Lock");
mScrollLockCheckBox.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent event) {
boolean scrollLock = mScrollLockCheckBox.getSelection();
                setScrollToLatestLog(scrollLock);
}
});
}
//Synthetic comment -- @@ -905,6 +909,43 @@
dispose();
}
});

        // The following mouse wheel listener and vertical scrollbar listener work in conjunction
        // to provide the following behavior:
        //      - When the mouse wheel is scrolled up => enable scroll lock
        //      - When the mouse wheel is scrolled down, and the scroll bar has reached the
        //        bottom, then disable scroll lock.
        // In the second case, identifying whether the scroll bar has reached the bottom is
        // platform specific - Mac, Linux and Windows all have different behaviors. So we use
        // a different heuristic: If the mouse is scrolled down, but there are no scroll events
        // generated, then that implies that the scroll bar has reached the bottom.
        mTable.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseScrolled(MouseEvent e) {
                if (mouseScrollUp(e)) {
                    setScrollToLatestLog(false);
                    mScrollLockCheckBox.setSelection(false);
                } else {
                    if (mLastVerticalBarScrollTime < mLastMouseScrollTime) {
                        setScrollToLatestLog(true);
                        mScrollLockCheckBox.setSelection(true);
                    }
                }

                mLastMouseScrollTime = e.time;
            }

            private boolean mouseScrollUp(MouseEvent e) {
                return e.count > 0;
            }
        });

        mTable.getVerticalBar().addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent arg0) {
                mLastVerticalBarScrollTime = arg0.time;
            }
        });
}

/** Setup menu to be displayed when right clicking a log message. */







