/*ProviderMap: Remove external process reference of ContentProviderRecord

ContentProviderRecords is removed from provider map by activity manager service
when the process hosting the content providers terminates and this causes GC
to free the content provider records but the reference to external process
using the content provider isn't cleared. Fix is for ProviderMap to clear the
reference and unlink for death notification.

Change-Id:I67fd10cee57b02257792d0c9b4569fe11dea7648*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ContentProviderRecord.java b/services/java/com/android/server/am/ContentProviderRecord.java
//Synthetic comment -- index fb21b06..5b50001 100644

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

    /**
     * Force removes handles to external process, if any, should be used
     * only when the process hosting this content provider is terminated.
     */
    public void removeExternalProcessHandles() {
        synchronized (service) {
            if (externalProcessTokenToHandle != null) {
                Iterator iterator = externalProcessTokenToHandle.keySet().iterator();
                while (iterator.hasNext()) {
                     removeExternalProcessHandleInternalLocked((IBinder)iterator.next());
                }
            }
        }
    }

// This class represents a handle from an external process to a provider.
private class ExternalProcessHandle implements DeathRecipient {
private static final String LOG_TAG = "ExternalProcessHanldle";








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ProviderMap.java b/services/java/com/android/server/am/ProviderMap.java
//Synthetic comment -- index d148ec3..7c3d58e 100644

//Synthetic comment -- @@ -117,31 +117,45 @@
}

void removeProviderByName(String name, int optionalUserId) {
        ContentProviderRecord removedRecord = null;

if (mGlobalByName.containsKey(name)) {
if (DBG)
Slog.i(TAG, "Removing from globalByName name=" + name);
            removedRecord = mGlobalByName.remove(name);
} else {
// TODO: Verify this works, i.e., the caller happens to be from the correct user
if (DBG)
Slog.i(TAG,
"Removing from providersByName name=" + name + " user="
+ (optionalUserId == -1 ? Binder.getOrigCallingUser() : optionalUserId));
            removedRecord = getProvidersByName(optionalUserId).remove(name);
        }

        // Clean up references to external process.
        if (removedRecord != null) {
            removedRecord.removeExternalProcessHandles();
}
}

void removeProviderByClass(ComponentName name, int optionalUserId) {
        ContentProviderRecord removedRecord = null;

if (mGlobalByClass.containsKey(name)) {
if (DBG)
Slog.i(TAG, "Removing from globalByClass name=" + name);
            removedRecord = mGlobalByClass.remove(name);
} else {
if (DBG)
Slog.i(TAG,
"Removing from providersByClass name=" + name + " user="
+ (optionalUserId == -1 ? Binder.getOrigCallingUser() : optionalUserId));
            removedRecord = getProvidersByClass(optionalUserId).remove(name);
        }

         // Clean up references to external process.
        if (removedRecord != null) {
            removedRecord.removeExternalProcessHandles();
}
}








