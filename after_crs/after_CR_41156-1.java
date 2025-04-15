/*ContentProviderRecord: Remove external process reference when finalized

ContentProviderRecords is removed from provider map by activity manager service
when the process hosting the content providers terminates and this causes GC
to free the content provider records but the reference to external process
using the content provider isn't cleared. Fix is to clear the reference and
unlink for death notification.

Change-Id:I67fd10cee57b02257792d0c9b4569fe11dea7648*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ContentProviderRecord.java b/services/java/com/android/server/am/ContentProviderRecord.java
//Synthetic comment -- index fb21b06..e9c6b88 100644

//Synthetic comment -- @@ -31,6 +31,7 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

class ContentProviderRecord {
final ActivityManagerService service;
//Synthetic comment -- @@ -210,6 +211,21 @@
return shortStringName = sb.toString();
}

    protected void finalize() throws Throwable {
        try {
            synchronized (service) {
                if (externalProcessTokenToHandle != null) {
                    Iterator iterator = externalProcessTokenToHandle.keySet().iterator();
                    while (iterator.hasNext()) {
                         removeExternalProcessHandleInternalLocked((IBinder)iterator.next());
                    }
                }
            }
        } finally {
             super.finalize();
        }
    }

// This class represents a handle from an external process to a provider.
private class ExternalProcessHandle implements DeathRecipient {
private static final String LOG_TAG = "ExternalProcessHanldle";







