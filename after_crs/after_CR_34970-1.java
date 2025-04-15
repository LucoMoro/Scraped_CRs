/*Ensure screenshots saved through DDMS end with .png.

This patch simply makes sure that the filename will have a .png extension when saving a screenshot through DDMS.

Change-Id:Idb00259ac456fcaf11ee99736663fc7ddbbc505bSigned-off-by: Sebastian Roth <sebastian.roth@gmail.com>*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/ScreenShotDialog.java
//Synthetic comment -- index d0c8a2f..0373fef 100644

//Synthetic comment -- @@ -321,7 +321,12 @@
// the value the dialog was initialized with. It does however return
// the full path as its return value, so just pick the path from
// there.
        	
            if(!fileName.endsWith(".png")) {
            	fileName = fileName + ".png";
            }

        	String saveDir = new File(fileName).getParent();
if (saveDir != null) {
DdmUiPreferences.getStore().setValue("lastImageSaveDir", saveDir);
}







