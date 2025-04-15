/*SDK Manager: make "choose package to install" dialog taller.

Also add an option to copy the package text to clipboard
or to print (using the system's default printer, whatever
that is.)

SDK Bug: 42445

Change-Id:I6969951b8bf1cc58f65fe05b2e5f16ecd2c2e64f*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/SdkUpdaterChooserDialog.java
//Synthetic comment -- index 49bebba..48f5553 100755

//Synthetic comment -- @@ -52,6 +52,7 @@
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
//Synthetic comment -- @@ -211,8 +212,26 @@
}
});

        Label placeholder = new Label(mPackageRootComposite, SWT.NONE);
        placeholder.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, 1, 1));

mLicenseRadioAcceptAll = new Button(mPackageRootComposite, SWT.RADIO);
mLicenseRadioAcceptAll.setText("Accept All");
//Synthetic comment -- @@ -366,7 +385,7 @@
return sLastSize;
} else {
// Arbitrary values that look good on my screen and fit on 800x600
            return new Point(740, 370);
}
}








