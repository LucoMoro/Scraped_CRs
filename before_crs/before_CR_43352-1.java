/*Makes the add button for new APNs appear in the action bar.

The APN add button (a plus sign) now appears in the action bar if possible. The APN list interface is now more consistent with the WiFi list interface. Previously the user had to press the 'Menu' hardware button or the three dot button on the screen in order to reveal the actions.

Change-Id:Iae622054ec080ba4775c22223dd75a865c83fc27Signed-off-by: Manuel R. Ciosici <manuelrciosici@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/settings/ApnSettings.java b/src/com/android/settings/ApnSettings.java
//Synthetic comment -- index 56ee7a9..de1ce63 100644

//Synthetic comment -- @@ -209,7 +209,8 @@
super.onCreateOptionsMenu(menu);
menu.add(0, MENU_NEW, 0,
getResources().getString(R.string.menu_new))
                .setIcon(android.R.drawable.ic_menu_add);
menu.add(0, MENU_RESTORE, 0,
getResources().getString(R.string.menu_restore))
.setIcon(android.R.drawable.ic_menu_upload);







