/*New implementation for java.lang.Runtime's availableProcessors().

This patch adds a native implementation for availableProcessors(),
replacing the hardcoded "always return 1" implementation.

It uses sysconf(_SC_NPROCESSORS_ONLN) to get the number of online
processors.

Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/luni-kernel/src/main/java/java/lang/Runtime.java b/luni-kernel/src/main/java/java/lang/Runtime.java
//Synthetic comment -- index 28cc96f..e2439aa 100644

//Synthetic comment -- @@ -742,15 +742,13 @@
}

/**
     * Returns the number of processors available to the virtual machine. The
     * Android reference implementation (currently) always returns 1.
* 
* @return the number of available processors, at least 1.
* @since Android 1.0
*/
    public int availableProcessors() {
        return 1;
    }

/**
* Returns the maximum amount of memory that may be used by the virtual







