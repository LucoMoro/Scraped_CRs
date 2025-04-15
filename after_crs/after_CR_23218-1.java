/*SdkMan2: don't use yellow background anymore.

Change-Id:I9bcc54b58ab717d22d881bd36d38d7594a175214*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 5fc9881..04bf422 100755

//Synthetic comment -- @@ -36,7 +36,6 @@
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeColumnViewerLabelProvider;
//Synthetic comment -- @@ -47,7 +46,6 @@
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
//Synthetic comment -- @@ -146,7 +144,6 @@
private TreeViewerColumn mColumnApi;
private TreeViewerColumn mColumnRevision;
private TreeViewerColumn mColumnStatus;
private Font mTreeFontItalic;
private TreeColumn mTreeColumnName;

//Synthetic comment -- @@ -482,14 +479,10 @@
fontData.setStyle(SWT.ITALIC);
mTreeFontItalic = new Font(mTree.getDisplay(), fontData);

mTree.addDisposeListener(new DisposeListener() {
public void widgetDisposed(DisposeEvent e) {
mTreeFontItalic.dispose();
mTreeFontItalic = null;
}
});
}
//Synthetic comment -- @@ -1030,8 +1023,7 @@

// ----------------------

    public class PkgCellLabelProvider extends ColumnLabelProvider implements ITableFontProvider {

private final TreeViewerColumn mColumn;

//Synthetic comment -- @@ -1146,22 +1138,6 @@
}
return super.getFont(element);
}
}

private class PkgContentProvider implements ITreeContentProvider {







