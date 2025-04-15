/*Add datetime postfix to default file name
when showing save screen image dialog.

Change-Id:I141124cddf211959b8f18846445663da0b9ad958*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java
//Synthetic comment -- index 590a8ab..689ca6f 100644

//Synthetic comment -- @@ -43,6 +43,7 @@

import java.io.File;
import java.io.IOException;
import java.util.Calendar;


/**
//Synthetic comment -- @@ -284,8 +285,17 @@
FileDialog dlg = new FileDialog(shell, SWT.SAVE);
String fileName;

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int sec = cal.get(Calendar.SECOND);

dlg.setText("Save image...");
        dlg.setFileName(String.format("device-%04d%02d%02d%02d%02d%02d.png",
                year, month, day, hour, min, sec));

String lastDir = DdmUiPreferences.getStore().getString("lastImageSaveDir");
if (lastDir.length() == 0) {







