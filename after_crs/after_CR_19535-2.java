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

Change-Id:I87c34a4c5e45f374f90eff187225a85a4f39f7af*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 51ff959..de74e78 100644

//Synthetic comment -- @@ -11387,6 +11387,14 @@
if (DEBUG_SERVICE) Slog.v(
TAG, "Removed service that is not running: " + r.shortName);
}

        if (r.bindings.size() > 0) {
            r.bindings.clear();
        }

        if (r.restarter instanceof ServiceRestarter) {
           ((ServiceRestarter)r.restarter).setService(null);
        }
}

ComponentName startServiceLocked(IApplicationThread caller,







