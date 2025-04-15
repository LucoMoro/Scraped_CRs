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
 * This class keeps track of calls to {@code getTasks()}
 * that are still waiting for thumbnail images.
*/
final class PendingThumbnailsRecord {

    /**
     * Who is waiting.
     */
    final IThumbnailReceiver receiver;

    /**
     * HistoryRecord objects we still wait for.
     */
    final HashSet pendingRecords = new HashSet();

    /**
     * A flag indicating that records were processed.
     */
    volatile boolean finished;

    /**
     * Creates a new pending thumbnail record for given receiver.
     *
     * @param receiver a thumbnail receiver.
     * @throws IllegalArgumentException if {@code receiver} is {@code null}.
     */
    PendingThumbnailsRecord(final IThumbnailReceiver receiver) {
        if (receiver == null) {
            throw new IllegalArgumentException();
        }
        this.receiver = receiver;
}

}







