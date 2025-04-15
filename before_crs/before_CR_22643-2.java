/*Add "Reset" to palette context menu

This CL adds a "Reset" item to the context menu in the palette, which
resets the palette back to the default mode -- alphabetical sort off,
categories on, scaled previews.

Change-Id:I2d6c65a905f3b2f7c164475ddb7bcaeff7f5e6e3*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gle2/PaletteControl.java
//Synthetic comment -- index e4b1890..fa79b27 100755

//Synthetic comment -- @@ -1118,9 +1118,11 @@
final static int TOGGLE_ALPHABETICAL = 2;
final static int TOGGLE_AUTO_CLOSE = 3;
final static int REFRESH = 4;

ToggleViewOptionAction(String title, int action, boolean checked) {
            super(title, action == REFRESH ? IAction.AS_PUSH_BUTTON : IAction.AS_CHECK_BOX);
mAction = action;
if (checked) {
setChecked(checked);
//Synthetic comment -- @@ -1146,6 +1148,13 @@
mPreviewIconFactory.refresh();
refreshPalette();
break;
}
savePaletteMode();
}
//Synthetic comment -- @@ -1190,6 +1199,11 @@
manager.add(new ToggleViewOptionAction("Auto Close Previous",
ToggleViewOptionAction.TOGGLE_AUTO_CLOSE,
mAutoClose));
Menu menu = manager.createContextMenu(PaletteControl.this);
menu.setLocation(x, y);
menu.setVisible(true);







