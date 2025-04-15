/*Removing spagetti debug code.

Yes, I tried to extract it, refactor and control the execution
by DEBUG_... constant. But in fact, that code simply does not have
sense.

Change-Id:I506549981c4a5a02ecb784f40f433efe31c8a4b7*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 0e38e10..342a854 100755

//Synthetic comment -- @@ -8118,37 +8118,6 @@

private final void killServicesLocked(ProcessRecord app,
boolean allowRestart) {
        // Report disconnected services.
        if (false) {
            // XXX we are letting the client link to the service for
            // death notifications.
            if (app.services.size() > 0) {
                Iterator<ServiceRecord> it = app.services.iterator();
                while (it.hasNext()) {
                    ServiceRecord r = it.next();
                    if (r.connections.size() > 0) {
                        Iterator<ArrayList<ConnectionRecord>> jt
                                = r.connections.values().iterator();
                        while (jt.hasNext()) {
                            ArrayList<ConnectionRecord> cl = jt.next();
                            for (int i=0; i<cl.size(); i++) {
                                ConnectionRecord c = cl.get(i);
                                if (c.binding.client != app) {
                                    try {
                                        //c.conn.connected(r.className, null);
                                    } catch (Exception e) {
                                        // todo: this should be asynchronous!
                                        Slog.w(TAG, "Exception thrown disconnected servce "
                                              + r.shortName
                                              + " from app " + app.processName, e);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

// Clean up any connections this application has to other services.
if (app.connections.size() > 0) {







