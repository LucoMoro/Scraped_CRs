/*Fix Hierarchy viewer plug-in again.

Change-Id:I15ef8c3050826c7f17dc1ee822a3c8e268c62c36*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPlugin.java b/eclipse/plugins/com.android.ide.eclipse.hierarchyviewer/src/com/android/ide/eclipse/hierarchyviewer/HierarchyViewerPlugin.java
//Synthetic comment -- index 959bf6c..978cfac 100644

//Synthetic comment -- @@ -115,10 +115,7 @@
new Thread() {
@Override
public void run() {
                if (director.acquireBridge()) {
                    director.startListenForDevices();
                    director.populateDeviceSelectionModel();
                }
}
}.start();
}
//Synthetic comment -- @@ -167,15 +164,23 @@
new Thread() {
@Override
public void run() {
                        HierarchyViewerDirector.getDirector().initDebugBridge();
                        HierarchyViewerDirector.getDirector().startListenForDevices();
                        HierarchyViewerDirector.getDirector().populateDeviceSelectionModel();
}
}.start();
}
}
}

/**
* Prints a message, associated with a project to the specified stream
*







