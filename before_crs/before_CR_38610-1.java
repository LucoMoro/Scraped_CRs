/*ActivityManagerService: stay away from zombie content providers

When the lowmemorykiller kills a content provider in an unfortunate
moment, an innocent client app can become attached to a zombie provider
and be unconditionally killed moments after, when the dead provider is
being removed.
Prevent this race by addition of isAlive function to Process.java that
determines whether a process is still running and is not only a zombie,
add a check of the state of the provider process to getContentProviderImpl.

Change-Id:Id753b8dab103e4b447c4138468938ca9f5cb857d*/
//Synthetic comment -- diff --git a/core/java/android/os/Process.java b/core/java/android/os/Process.java
//Synthetic comment -- index 50567b2..f0e4b88 100644

//Synthetic comment -- @@ -828,4 +828,29 @@
*/
public boolean usingWrapper;
}
}








//Synthetic comment -- diff --git a/services/java/com/android/server/am/ActivityManagerService.java b/services/java/com/android/server/am/ActivityManagerService.java
//Synthetic comment -- index 0a4d19f..fdeb2ab 100644

//Synthetic comment -- @@ -5964,7 +5964,7 @@
// pending on the process even though we managed to update its
// adj level.  Not sure what to do about this, but at least
// the race is now smaller.
                    if (!success) {
// Uh oh...  it looks like the provider's process
// has been killed on us.  We need to wait for a new
// process to be started, and make sure its death
//Synthetic comment -- @@ -5973,7 +5973,9 @@
"Existing provider " + cpr.name.flattenToShortString()
+ " is crashing; detaching " + r);
boolean lastRef = decProviderCount(r, cpr);
                        appDiedLocked(cpr.proc, cpr.proc.pid, cpr.proc.thread);
if (!lastRef) {
// This wasn't the last ref our process had on
// the provider...  we have now been killed, bail.







