/*ProviderMap: Remove external process reference of ContentProviderRecord

ContentProviderRecords is removed from provider map by activity manager service
when the process hosting the content providers terminates and this causes GC
to free the content provider records but the reference to external process
using the content provider isn't cleared. Fix is for ProviderMap to clear the
reference and unlink for death notification.

Change-Id:I67fd10cee57b02257792d0c9b4569fe11dea7648*/




//Synthetic comment -- diff --git a/services/java/com/android/server/am/ContentProviderRecord.java b/services/java/com/android/server/am/ContentProviderRecord.java
//Synthetic comment -- index 8fb6a93..9fbbc6e 100644

//Synthetic comment -- @@ -32,6 +32,7 @@
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

class ContentProviderRecord {
final ActivityManagerService service;
//Synthetic comment -- @@ -219,6 +220,21 @@
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
//Synthetic comment -- index 9dbf5f5..ada5e7b 100644

//Synthetic comment -- @@ -120,10 +120,12 @@
}

void removeProviderByName(String name, int userId) {
        ContentProviderRecord removedRecord = null;

if (mSingletonByName.containsKey(name)) {
if (DBG)
Slog.i(TAG, "Removing from globalByName name=" + name);
            removedRecord = mSingletonByName.remove(name);
} else {
if (userId < 0) throw new IllegalArgumentException("Bad user " + userId);
if (DBG)
//Synthetic comment -- @@ -131,18 +133,25 @@
"Removing from providersByName name=" + name + " user=" + userId);
HashMap<String, ContentProviderRecord> map = getProvidersByName(userId);
// map returned by getProvidersByName wouldn't be null
            removedRecord = map.remove(name);
if (map.size() == 0) {
mProvidersByNamePerUser.remove(userId);
}
}

        // Clean up references to external process.
        if (removedRecord != null) {
            removedRecord.removeExternalProcessHandles();
        }
}

void removeProviderByClass(ComponentName name, int userId) {
        ContentProviderRecord removedRecord = null;

if (mSingletonByClass.containsKey(name)) {
if (DBG)
Slog.i(TAG, "Removing from globalByClass name=" + name);
            removedRecord = mSingletonByClass.remove(name);
} else {
if (userId < 0) throw new IllegalArgumentException("Bad user " + userId);
if (DBG)
//Synthetic comment -- @@ -150,11 +159,16 @@
"Removing from providersByClass name=" + name + " user=" + userId);
HashMap<ComponentName, ContentProviderRecord> map = getProvidersByClass(userId);
// map returned by getProvidersByClass wouldn't be null
            removedRecord = map.remove(name);
if (map.size() == 0) {
mProvidersByClassPerUser.remove(userId);
}
}

        // Clean up references to external process.
        if (removedRecord != null) {
            removedRecord.removeExternalProcessHandles();
        }
}

private HashMap<String, ContentProviderRecord> getProvidersByName(int userId) {







