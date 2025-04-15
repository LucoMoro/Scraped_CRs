/*Relocate emulator control panel in DDMS perspective.

Change-Id:If2b847d6937ea64b2727ff78f1c94850f91da81b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/Perspective.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/Perspective.java
//Synthetic comment -- index d86a526..4f40f4f 100644

//Synthetic comment -- @@ -53,11 +53,6 @@
folder.addPlaceholder(DeviceView.ID + ":*"); //$NON-NLS-1$
folder.addView(DeviceView.ID);

folder = layout.createFolder("ddms-detail", IPageLayout.RIGHT, 0.5f, //$NON-NLS-1$
editorArea);
folder.addPlaceholder(ThreadView.ID + ":*"); //$NON-NLS-1$
//Synthetic comment -- @@ -66,6 +61,7 @@
folder.addView(AllocTrackerView.ID);
folder.addView(NetworkStatisticsView.ID);
folder.addView(FileExplorerView.ID);
        folder.addView(EmulatorControlView.ID);

layout.addPerspectiveShortcut("org.eclipse.ui.resourcePerspective"); //$NON-NLS-1$
layout.addPerspectiveShortcut("org.eclipse.debug.ui.DebugPerspective"); //$NON-NLS-1$







