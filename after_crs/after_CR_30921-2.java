/*Improved documentation (clarify on issue 8727)

Signed-off-by: Jacob Nordfalk <jacob.nordfalk@gmail.com>
Change-Id:I2bea866542c95eb0558004a64c9a2fd74aeff35b*/




//Synthetic comment -- diff --git a/core/java/android/app/Application.java b/core/java/android/app/Application.java
//Synthetic comment -- index dd9ea26..3a67cec 100644

//Synthetic comment -- @@ -64,11 +64,12 @@
}

/**
     * Called when the application is starting, before any activity, service,
     * or receiver objects (excluding content providers) have been created.
     * Implementations should be as quick as possible (for example using 
     * lazy initialization of state) since the time spent in this function
     * directly impacts the performance of starting the first activity,
     * service, or receiver in a process.
* If you override this method, be sure to call super.onCreate().
*/
public void onCreate() {







