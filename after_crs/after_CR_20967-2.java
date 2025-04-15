/*Add datetime postfix to default file name
when showing save screen image dialog.

Change-Id:I141124cddf211959b8f18846445663da0b9ad958*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java
//Synthetic comment -- index 590a8ab..f98dd13 100644

//Synthetic comment -- @@ -43,6 +43,7 @@

import java.io.File;
import java.io.IOException;
import java.util.Calendar;


/**
//Synthetic comment -- @@ -282,10 +283,13 @@
*/
private void saveImage(Shell shell) {
FileDialog dlg = new FileDialog(shell, SWT.SAVE);

        Calendar now = Calendar.getInstance();
        String fileName = String.format("device-%tF-%tH%tM%tS.png",
                now, now, now, now);

dlg.setText("Save image...");
        dlg.setFileName(fileName);

String lastDir = DdmUiPreferences.getStore().getString("lastImageSaveDir");
if (lastDir.length() == 0) {







