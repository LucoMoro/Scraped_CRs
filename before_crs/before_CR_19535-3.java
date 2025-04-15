/*frameworks/base: Fix to release references in ActivityManagerService

ServiceRecord's bindings is a hashmap to keep track of all active
bindings to the service. This is not cleared when the service is
brought down by activity manager. This adds up the references to
IntentBindRecords and its references to ServiceRecord. Fix is to
clear the bindings.

ServiceRecord's restarter is a reference to the service and is not
cleared when the service is brought down by activity manager. This
adds up the references to ServiceRecord. Fix is to set the reference
to null when the service is brought down by activity manager.

Change-Id:Ica448cd5f60192c8adb23209b5d0e2cf0c04e446*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 6a05d3c..16cd62b 100644

//Synthetic comment -- @@ -9120,6 +9120,14 @@
if (DEBUG_SERVICE) Slog.v(
TAG, "Removed service that is not running: " + r);
}
}

ComponentName startServiceLocked(IApplicationThread caller,







