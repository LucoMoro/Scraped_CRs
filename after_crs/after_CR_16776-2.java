/*Bug fixes and reducing timeout.

Change-Id:Ibfa23734679d0337aa7f6e250a621026b13a70f0*/




//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/HierarchyViewerApplication.java
//Synthetic comment -- index 7de97f4..d66f8e1 100644

//Synthetic comment -- @@ -1052,6 +1052,7 @@
int newValue = refreshSlider.getSelection();
if (oldValue != newValue) {
refreshInterval = newValue;
                refreshTimeLeft = Math.min(refreshTimeLeft, refreshInterval);
oldValue = newValue;
}
}








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceConnection.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceConnection.java
//Synthetic comment -- index 3080cc1..cda1a77 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
}

socketChannel.connect(new InetSocketAddress("127.0.0.1", port));
        socketChannel.socket().setSoTimeout(40000);
}

public BufferedReader getInputStream() throws IOException {








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/ui/TreeView.java
//Synthetic comment -- index b7d9303..2c3a13e 100644

//Synthetic comment -- @@ -96,6 +96,8 @@

private static Image filteredSelectedImage;

    private static Font systemFont;

private Color boxColor;

private Color textBackgroundColor;
//Synthetic comment -- @@ -178,6 +180,7 @@
if (scaledSelectedImage != null) {
scaledSelectedImage.dispose();
}
        systemFont = Display.getDefault().getSystemFont();
}

private DisposeListener disposeListener = new DisposeListener() {
//Synthetic comment -- @@ -941,7 +944,6 @@
}

private static Font getFont(int size, boolean bold) {
FontData[] fontData = systemFont.getFontData();
for (int i = 0; i < fontData.length; i++) {
fontData[i].setHeight(size);







