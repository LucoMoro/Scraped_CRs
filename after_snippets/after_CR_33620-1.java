
//<Beginning of snippet n. 0>


import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
}

@Override
    public IWizardPage getNextPage() {
// Sync working set data to the value object, since the WorkingSetGroup
// doesn't let us add listeners to do this lazily
mValues.workingSets = getWorkingSets();

        return super.getNextPage();
}
}

//<End of snippet n. 0>








