/*hv: Add more logging for when things go wrong.

Change-Id:Idbb0a32507c19aadb11544342d73e1be7d1d4272*/




//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/DeviceBridge.java
//Synthetic comment -- index 07bb807..10308a3 100644

//Synthetic comment -- @@ -468,6 +468,7 @@
} catch (Exception e) {
Log.e(TAG, "Unable to load window data for window " + window.getTitle() + " on device "
+ window.getDevice());
            Log.e(TAG, e.getMessage());
} finally {
if (connection != null) {
connection.close();








//Synthetic comment -- diff --git a/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java b/hierarchyviewer2/libs/hierarchyviewerlib/src/com/android/hierarchyviewerlib/device/ViewNode.java
//Synthetic comment -- index 76eaa10..4ab4fc6 100644

//Synthetic comment -- @@ -125,6 +125,9 @@
this.parent.children.add(this);
}
int delimIndex = data.indexOf('@');
        if (delimIndex < 0) {
            throw new IllegalArgumentException("Invalid format for ViewNode, missing @: " + data);
        }
name = data.substring(0, delimIndex);
data = data.substring(delimIndex + 1);
delimIndex = data.indexOf(' ');







