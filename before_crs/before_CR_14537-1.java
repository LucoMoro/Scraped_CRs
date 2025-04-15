/*Fix a potential leak in java.util.Timer

In TimerImpl's run() method, the 'task' variable is used while finding
tasks to run. Because it is uninitialized for a portion of the loop,
it will still contain an invisible reference to its previous value,
pinning it in memory. If the Timer's queue becomes empty, it will then
attempt to wait until either more Tasks are added, or the Timer goes away.

However, if the pinned Task itself contains a reference to the Timer, this will
prevent the Timer's finalizer from ever running, meaning that this thread will
wait indefinitely. This causes both threads and memory to stack up.

Explicitly setting this variable to null alleviates the problem.

Seehttp://java.sun.com/docs/books/performance/1st_edition/html/JPAppGC.fm.html#997414for more information on invisible references.

Change-Id:Ibc1f973e072d8b5dbdd5fd48c72979b7f4d0c5fb*/
//Synthetic comment -- diff --git a/libcore/luni/src/main/java/java/util/Timer.java b/libcore/luni/src/main/java/java/util/Timer.java
//Synthetic comment -- index 6090031..670ee2a 100644

//Synthetic comment -- @@ -204,7 +204,7 @@
@Override
public void run() {
while (true) {
                TimerTask task;
synchronized (this) {
// need to check cancelled inside the synchronized block
if (cancelled) {







