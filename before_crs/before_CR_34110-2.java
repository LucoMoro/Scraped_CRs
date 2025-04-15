/*Add an API to update the screen EasyMonkeyDevice.java.

Before this change, there was a need to do (device) easy_device every time every time activity at the test code changes.
API that was added, which is a method of updating the screen.
That so they do not have to be done every time the (device) easy_device when the activity is changed by running this API.

when you run the touch method, automatically update the HierarchyViewer

Change-Id:Ic2be87665daf62c598090f8d9ce86d36f7a0ff9b*/
//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java
//Synthetic comment -- index fb778b7..25099aa 100644

//Synthetic comment -- @@ -61,7 +61,7 @@
argDocs = { "MonkeyDevice to extend." })
public EasyMonkeyDevice(MonkeyDevice device) {
this.mDevice = device;
        this.mHierarchyViewer = device.getImpl().getHierarchyViewer();
}

@MonkeyRunnerExported(doc = "Sends a touch event to the selected object.",
//Synthetic comment -- @@ -84,6 +84,7 @@
public void touch(By selector, TouchPressType type) {
Point p = getElementCenter(selector);
mDevice.getImpl().touch(p.x, p.y, type);
}

@MonkeyRunnerExported(doc = "Types a string into the specified object.",
//Synthetic comment -- @@ -187,6 +188,18 @@
return mHierarchyViewer.getFocusedWindowName();
}

/**
* Forwards unknown methods to the original MonkeyDevice object.
*/







