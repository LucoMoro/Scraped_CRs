/*Implement a recursive version of FileObserver*/




//Synthetic comment -- diff --git a/core/java/android/os/FileObserver.java b/core/java/android/os/FileObserver.java
//Synthetic comment -- index 667ce68..b08f277 100644

//Synthetic comment -- @@ -18,11 +18,12 @@

import android.util.Log;


import java.io.File;
import java.io.FileFilter;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;

/**
* Monitors files (using <a href="http://en.wikipedia.org/wiki/Inotify">inotify</a>)
//Synthetic comment -- @@ -77,6 +78,8 @@

private static class ObserverThread extends Thread {
private HashMap<Integer, WeakReference> m_observers = new HashMap<Integer, WeakReference>();
        private HashMap<Integer,String> m_listPath = new HashMap<Integer,String>();
        private FolderFilter filter = new FolderFilter();
private int m_fd;

public ObserverThread() {
//Synthetic comment -- @@ -88,23 +91,52 @@
observe(m_fd);
}

        public int startWatching(String observed, String path, int mask, FileObserver observer) {
int wfd = startWatching(m_fd, path, mask);

Integer i = new Integer(wfd);
if (wfd >= 0) {
synchronized (m_observers) {
m_observers.put(i, new WeakReference(observer));
                    m_listPath.put(i, path.replaceFirst(observed, ""));

                    File rootFolder = new File(path);
                    File[] childFolders = rootFolder.listFiles(filter);
                    if((childFolders != null))
                    {
                        for(int index = 0; index < childFolders.length; index++)
                            startWatching(observed, childFolders[index].getPath(), mask, observer);
                    }

}
}

return i;
}

        public void stopWatching(int descriptor, FileObserver observer) {
            synchronized(m_observers)
            {
                stopWatching(m_fd, descriptor);
                m_listPath.remove(descriptor);
                m_observers.remove(descriptor);

                Iterator <Integer> it = m_listPath.keySet().iterator();
                while(it.hasNext())
                {
                    Integer fd = it.next();
                    if(m_observers.get(fd).get() == observer)
                    {
                        stopWatching(m_fd, fd);
                        it.remove();
                        m_observers.remove(fd);
                    }
                }
            }
}



public void onEvent(int wfd, int mask, String path) {
// look up our observer, fixing up the map if necessary...
FileObserver observer = null;
//Synthetic comment -- @@ -115,6 +147,7 @@
observer = (FileObserver) weak.get();
if (observer == null) {
m_observers.remove(wfd);
                        m_listPath.remove(wfd);
}
}
}
//Synthetic comment -- @@ -122,7 +155,15 @@
// ...then call out to the observer without the sync lock held
if (observer != null) {
try {
                    String p = m_listPath.get(wfd);
                    if (path != null) {
                        if (p.length() > 0)
                            p += "/";
                        p += path;
                    }
                    if (p.length() == 0)
                        p = null;
                    observer.onEvent(mask, p);
} catch (Throwable throwable) {
Log.wtf(LOG_TAG, "Unhandled exception in FileObserver " + observer, throwable);
}
//Synthetic comment -- @@ -179,7 +220,7 @@
*/
public void startWatching() {
if (m_descriptor < 0) {
            m_descriptor = s_observerThread.startWatching(m_path, m_path, m_mask, this);
}
}

//Synthetic comment -- @@ -190,7 +231,7 @@
*/
public void stopWatching() {
if (m_descriptor >= 0) {
            s_observerThread.stopWatching(m_descriptor, this);
m_descriptor = -1;
}
}
//Synthetic comment -- @@ -210,4 +251,12 @@
*     of the file or directory which triggered the event
*/
public abstract void onEvent(int event, String path);

    private static class FolderFilter implements FileFilter
    {

        public boolean accept(File pathname) {
            return pathname.isDirectory();
        }
    }
}







