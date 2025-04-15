/*Refactoring and cleanup of PendingThumbnailRecord.

The most important part of this patch is marking 'finished' field
as 'volatile'. This is necessary, because the field is read out of
synchronized blocks in ActivityManagerService, contrary to other
fields.

Change-Id:Ie1677a13758eb06afbb0a594b1af058c5d775a5a*/
//Synthetic comment -- diff --git a/services/java/com/android/server/am/PendingThumbnailsRecord.java b/services/java/com/android/server/am/PendingThumbnailsRecord.java
//Synthetic comment -- index ed478c9..90eeffd 100644

//Synthetic comment -- @@ -21,19 +21,37 @@
import java.util.HashSet;

/**
 * This class keeps track of calls to getTasks() that are still
 * waiting for thumbnail images.
*/
class PendingThumbnailsRecord
{
    final IThumbnailReceiver receiver;   // who is waiting.
    HashSet pendingRecords; // HistoryRecord objects we still wait for.
    boolean finished;       // Is pendingRecords empty?

    PendingThumbnailsRecord(IThumbnailReceiver _receiver)
    {
        receiver = _receiver;
        pendingRecords = new HashSet();
        finished = false;
}
}







