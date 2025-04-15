/*fixes a bug that Working Set specified in "New Android Project" wizard is ignored in some cases.

In original code, specified Working Set data is set to mValues in ProjectNamePage#canFlipToNextPage(). This hack works in some cases, but not always.

canFlipToNextPage is invoked when user changes the name of project, but is not invoked when user changed the data of Working Set. So, if user changes WorkingSet as last action of this page, it is ignored.

I moved "mValues.workingSets = getWorkingSets();" from canFlipToNextPage() to getNextPage() because getNextPage() is alwasys invoked at the end of ProjectNamePage.http://code.google.com/p/android/issues/detail?id=21508Change-Id:I336fe449703144ac7a053cce6a946f931e97bb9fSigned-off-by: YAMAZAKI Makoto <makoto1975@gmail.com>*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/ProjectNamePage.java
//Synthetic comment -- index ff77b9f..d4c342c 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -625,11 +626,11 @@
}

@Override
    public IWizardPage getNextPage() {
// Sync working set data to the value object, since the WorkingSetGroup
// doesn't let us add listeners to do this lazily
mValues.workingSets = getWorkingSets();

        return super.getNextPage();
}
}







