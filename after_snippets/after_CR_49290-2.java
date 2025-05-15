
//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
}
});

        //--Label placeholder = new Label(mPackageRootComposite, SWT.NONE);
        //--placeholder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

        Link link = new Link(mPackageRootComposite, SWT.NONE);
        link.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
        final String printAction = "Print"; // extracted for NLS, to compare with below.
        link.setText(String.format("<a>Copy to clipboard</a> | <a>%1$s</a>", printAction));
        link.setToolTipText("Copies all text and license to clipboard | Print using system defaults.");
        link.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                super.widgetSelected(e);
                if (printAction.equals(e.text)) {
                    mPackageText.print();
                } else {
                    Point p = mPackageText.getSelection();
                    mPackageText.selectAll();
                    mPackageText.copy();
                    mPackageText.setSelection(p);
                }
            }
        });


mLicenseRadioAcceptAll = new Button(mPackageRootComposite, SWT.RADIO);
mLicenseRadioAcceptAll.setText("Accept All");
return sLastSize;
} else {
// Arbitrary values that look good on my screen and fit on 800x600
            return new Point(740, 470);
}
}


//<End of snippet n. 0>








