/*Add an API to update the screen EasyMonkeyDevice.java.

Before this change, there was a need to do (device) easy_device every time every time activity at the test code changes. API that was added, which is a method of updating the screen. That so they do not have to be done every time the (device) easy_device when the activity is changed by running this API.

Change-Id:I57b90266a7fa84fb023ee2e48dae572c1d599c6f*/




//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/easy/EasyMonkeyDevice.java
//Synthetic comment -- index fb778b7..e3c2b55 100644

//Synthetic comment -- @@ -61,7 +61,7 @@
argDocs = { "MonkeyDevice to extend." })
public EasyMonkeyDevice(MonkeyDevice device) {
this.mDevice = device;
        updateHierarchyViewer();
}

@MonkeyRunnerExported(doc = "Sends a touch event to the selected object.",
//Synthetic comment -- @@ -187,6 +187,18 @@
return mHierarchyViewer.getFocusedWindowName();
}

    @MonkeyRunnerExported(doc = "To update the current screen. Use this method when the screen has been changed. For example, when the screen changes when Activity has changed.")
    public void updateView(PyObject[] args, String[] kws) {
        updateHierarchyViewer();
    }

    /**
     * Update HierarchyViewer object.
     */
    private void updateHierarchyViewer() {
        this.mHierarchyViewer = mDevice.getImpl().getHierarchyViewer();
    }

/**
* Forwards unknown methods to the original MonkeyDevice object.
*/







