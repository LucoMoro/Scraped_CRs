/*Extend forkAndSpecialize to take further input arguments.

Extend forkAndSpecialize to take two further input arguments,
a seinfo string and the nice name for the process.  These can be used
as further selectors in determining the SELinux security context for
the process.

Change-Id:If3a654070025e699b2266425f6eb8ab7e6b8c562*/




//Synthetic comment -- diff --git a/dalvik/src/main/java/dalvik/system/Zygote.java b/dalvik/src/main/java/dalvik/system/Zygote.java
//Synthetic comment -- index 28c9912..9c6b5df 100644

//Synthetic comment -- @@ -107,20 +107,22 @@
* dimension having a length of 3 and representing
* (resource, rlim_cur, rlim_max). These are set via the posix
* setrlimit(2) call.
     * @param SEInfo null-ok a string specifying SEAndroid information for
     * the new process.
*
* @return 0 if this is the child, pid of the child
* if this is the parent, or -1 on error.
*/
public static int forkAndSpecialize(int uid, int gid, int[] gids,
            int debugFlags, int[][] rlimits, String SEInfo, String niceName) {
preFork();
        int pid = nativeForkAndSpecialize(uid, gid, gids, debugFlags, rlimits, SEInfo, niceName);
postFork();
return pid;
}

native public static int nativeForkAndSpecialize(int uid, int gid,
            int[] gids, int debugFlags, int[][] rlimits, String SEInfo, String niceName);

/**
* Forks a new VM instance.
//Synthetic comment -- @@ -130,7 +132,7 @@
public static int forkAndSpecialize(int uid, int gid, int[] gids,
boolean enableDebugger, int[][] rlimits) {
int debugFlags = enableDebugger ? DEBUG_ENABLE_DEBUGGER : 0;
        return forkAndSpecialize(uid, gid, gids, debugFlags, rlimits, null, null);
}

/**
//Synthetic comment -- @@ -175,7 +177,7 @@
public static int forkSystemServer(int uid, int gid, int[] gids,
boolean enableDebugger, int[][] rlimits) {
int debugFlags = enableDebugger ? DEBUG_ENABLE_DEBUGGER : 0;
        return forkAndSpecialize(uid, gid, gids, debugFlags, rlimits, null, null);
}

native public static int nativeForkSystemServer(int uid, int gid,







