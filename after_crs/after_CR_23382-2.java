/*Indicate TmpFilter has been enabled by changing background color.

Change-Id:I8fff079680682f6ebdf6fc8025970e893d9bfcfe*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogColors.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogColors.java
//Synthetic comment -- index 9cff656..0fca1f7 100644

//Synthetic comment -- @@ -24,4 +24,6 @@
public Color errorColor;
public Color warningColor;
public Color verboseColor;
    public Color filteredBgColor;
    public Color unfilteredBgColor;
}








//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java
//Synthetic comment -- index 80ed6e9..4ba5f37 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
//Synthetic comment -- @@ -436,9 +437,18 @@

final Text filterText = new Text(bottom, SWT.SINGLE | SWT.BORDER);
filterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

filterText.addModifyListener(new ModifyListener() {
public void modifyText(ModifyEvent e) {

                String text = filterText.getText();
                updateFilteringWith(text);

                // change background color
                Color color = (text.length() > 0) ? mColors.filteredBgColor
                        : mColors.unfilteredBgColor;
                filterText.setBackground(color);
                setTablesBackground(color);
}
});

//Synthetic comment -- @@ -467,6 +477,23 @@
return top;
}

    private void setTablesBackground(Color color) {
        if (mDefaultFilter != null) {
            Table table = mDefaultFilter.getTable();
            if (table != null) {
                table.setBackground(color);
            }
        }
        if (mFilters != null) {
            for (LogFilter f : mFilters) {
                Table table = f.getTable();
                if (table != null) {
                    table.setBackground(color);
                }
            }
        }
    }

@Override
protected void postCreation() {
// pass








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java
//Synthetic comment -- index 37e265a..155bfec 100644

//Synthetic comment -- @@ -254,6 +254,8 @@
colors.errorColor = new Color(d, 255, 0, 0);
colors.warningColor = new Color(d, 255, 127, 0);
colors.verboseColor = new Color(d, 0, 0, 0);
        colors.filteredBgColor = new Color(d, 255, 255, 235);
        colors.unfilteredBgColor = new Color(d, 255, 255, 255);

mCreateFilterAction = new CommonAction(Messages.LogCatView_Create_Filter) {
@Override







