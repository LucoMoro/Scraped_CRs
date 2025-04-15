/*Drop-Down list for example projects looks strange on Linux (Project Wizard)

Seehttp://code.google.com/p/android/issues/detail?id=15529Change-Id:If69ae23c3949a6c871c4d6dd451011e7c0ba1656*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index 195c41f..94d56a1 100644

//Synthetic comment -- @@ -77,6 +77,9 @@
import java.io.FileFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -532,7 +535,11 @@

new Label(samples_group, SWT.NONE).setText("Samples:");

        mSamplesCombo = new Combo(samples_group, SWT.DROP_DOWN | SWT.READ_ONLY);
mSamplesCombo.setEnabled(false);
mSamplesCombo.select(0);
mSamplesCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -1208,6 +1215,7 @@
int selIndex = 0;
int i = 0;
int n = samplesRootPath.length();
for (String path : mSamplesPaths) {
if (path.length() > n) {
path = path.substring(n);
//Synthetic comment -- @@ -1224,10 +1232,10 @@
selIndex = i;
}

                mSamplesCombo.add(path);
i++;
}

mSamplesCombo.select(selIndex);

} else {







