/*Avoiding cyclic references when unbinding from a service

Cyclic references can occur between a Service object held by an
application and a ServiceRecord object held by the system server.
A part of the problem is that binders are leaked and since many binders
are implemented by inner classes of services these services are also leaked.
This causes low memory problems. The solution is: When a Service is beeing
destroyed, go through the ServiceRecord's all IntentBindRecord and set its
binder references to null. This allows the binder and the service object to
be garbage collected.

The call to System.gc() is needed because the service object cannot
be garbage collected until a gc has been run in the System server
process. Without this, we can theoretically run out of memory in the
process where the Service is running.

Change-Id:I5a257521964851f34c08ffb3908feaad96b1bafe*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 51ff959..43b1df2 100644

//Synthetic comment -- @@ -12022,6 +12022,15 @@
}
if (inStopping) {
mStoppingServices.remove(r);
                if (r.bindings.size() > 0) {
                    Iterator<IntentBindRecord> it = r.bindings.values().iterator();
                    while (it.hasNext()) {
                        IntentBindRecord record = it.next();
                        record.binder = null;
                        record.requested = record.received = record.hasBound = false;
                    }
                    System.gc();
                }
}
updateOomAdjLocked(r.app);
}







