/*Indicate TmpFilter has been enabled by changing background color.

Change-Id:I8fff079680682f6ebdf6fc8025970e893d9bfcfe*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/LogPanel.java
//Synthetic comment -- index 80ed6e9..2a6ef78 100644

//Synthetic comment -- @@ -44,6 +44,7 @@
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
//Synthetic comment -- @@ -436,9 +437,20 @@

final Text filterText = new Text(bottom, SWT.SINGLE | SWT.BORDER);
filterText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        final Color enableColor = new Color(filterText.getDisplay(), 255, 236, 236);
        final Color disableColor = new Color(filterText.getDisplay(), 255, 255, 255);

filterText.addModifyListener(new ModifyListener() {
public void modifyText(ModifyEvent e) {

                String text = filterText.getText();
                updateFilteringWith(text);

                // change background color
                Color color = (text.length() > 0) ? enableColor : disableColor;
                filterText.setBackground(color);
                setTablesBackground(color);
}
});

//Synthetic comment -- @@ -467,6 +479,23 @@
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







