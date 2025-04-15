/*Fixing possible race condition.

That return statement is effectively a reading of asynchronously modified
field. The reading and writing is elsewhere protected by locking on thread
instance, except this one occurence. Moving 'return' statement into
synchronized block ensures that it reads updated value.

Don't be afraid of deadlock - wait() releases the lock and acquires it
back when notified.

Change-Id:I940c24c04a1a2d4508d14d11b6174ff4fb2ab7d5*/




//Synthetic comment -- diff --git a/services/java/com/android/server/WindowManagerService.java b/services/java/com/android/server/WindowManagerService.java
//Synthetic comment -- index f605c11..5b236a7 100644

//Synthetic comment -- @@ -522,9 +522,8 @@
} catch (InterruptedException e) {
}
}
            return thr.mService;
}
}

static class WMThread extends Thread {







