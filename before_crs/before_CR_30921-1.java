/*Improved documentation

According to Dianne Hackborn documentation there is wrong.
Correct behaviour is described by her inhttp://code.google.com/p/android/issues/detail?id=8727Signed-off-by: Jacob Nordfalk <jacob.nordfalk@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/app/Application.java b/core/java/android/app/Application.java
//Synthetic comment -- index dd9ea26..3f30790 100644

//Synthetic comment -- @@ -64,8 +64,11 @@
}

/**
     * Called when the application is starting, before any other application
     * objects have been created.  Implementations should be as quick as
* possible (for example using lazy initialization of state) since the time
* spent in this function directly impacts the performance of starting the
* first activity, service, or receiver in a process.







