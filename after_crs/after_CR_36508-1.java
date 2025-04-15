/*Prevent excessive selection processing

Change-Id:I190faef19da0d9f17ce5b90ae4b178245ed0cbb1*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/OutlinePage.java
//Synthetic comment -- index a413ed1..1e77e4c 100755

//Synthetic comment -- @@ -76,6 +76,7 @@
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
//Synthetic comment -- @@ -560,6 +561,13 @@
}
}

    @Override
    public void selectionChanged(SelectionChangedEvent event) {
        if (!mIgnoreSelection) {
            super.selectionChanged(event);
        }
    }

// ----

/**







