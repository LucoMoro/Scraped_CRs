/*Implement a recursive version of FileObserver*/
//Synthetic comment -- diff --git a/core/java/android/os/FileObserver.java b/core/java/android/os/FileObserver.java
//Synthetic comment -- index 667ce68..b08f277 100644

//Synthetic comment -- @@ -18,11 +18,12 @@

import android.util.Log;

import com.android.internal.os.RuntimeInit;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

/**
* Monitors files (using <a href="http://en.wikipedia.org/wiki/Inotify">inotify</a>)
//Synthetic comment -- @@ -77,6 +78,8 @@

private static class ObserverThread extends Thread {
private HashMap<Integer, WeakReference> m_observers = new HashMap<Integer, WeakReference>();
private int m_fd;

public ObserverThread() {
//Synthetic comment -- @@ -88,23 +91,52 @@
observe(m_fd);
}

        public int startWatching(String path, int mask, FileObserver observer) {
int wfd = startWatching(m_fd, path, mask);

Integer i = new Integer(wfd);
if (wfd >= 0) {
synchronized (m_observers) {
m_observers.put(i, new WeakReference(observer));
}
}

return i;
}

        public void stopWatching(int descriptor) {
            stopWatching(m_fd, descriptor);
}

public void onEvent(int wfd, int mask, String path) {
// look up our observer, fixing up the map if necessary...
FileObserver observer = null;
//Synthetic comment -- @@ -115,6 +147,7 @@
observer = (FileObserver) weak.get();
if (observer == null) {
m_observers.remove(wfd);
}
}
}
//Synthetic comment -- @@ -122,7 +155,15 @@
// ...then call out to the observer without the sync lock held
if (observer != null) {
try {
                    observer.onEvent(mask, path);
} catch (Throwable throwable) {
Log.wtf(LOG_TAG, "Unhandled exception in FileObserver " + observer, throwable);
}
//Synthetic comment -- @@ -179,7 +220,7 @@
*/
public void startWatching() {
if (m_descriptor < 0) {
            m_descriptor = s_observerThread.startWatching(m_path, m_mask, this);
}
}

//Synthetic comment -- @@ -190,7 +231,7 @@
*/
public void stopWatching() {
if (m_descriptor >= 0) {
            s_observerThread.stopWatching(m_descriptor);
m_descriptor = -1;
}
}
//Synthetic comment -- @@ -210,4 +251,12 @@
*     of the file or directory which triggered the event
*/
public abstract void onEvent(int event, String path);
}







