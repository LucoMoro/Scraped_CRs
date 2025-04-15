/*Monkey: request am to show the ANR dialog

Default return value of 1 from appNotResponding was to show the
ANR dialog in eclair. However, recent version of android's am
interpret 1 as to wait. Fix is to change the default return
value from 1 to 0.

Change-Id:I6620ef54309f1d30ef18a2b60d73b346c2446f93*/




//Synthetic comment -- diff --git a/cmds/monkey/src/com/android/commands/monkey/Monkey.java b/cmds/monkey/src/com/android/commands/monkey/Monkey.java
//Synthetic comment -- index 68fb2a5..a1f3060 100644

//Synthetic comment -- @@ -337,9 +337,9 @@
synchronized (Monkey.this) {
mAbort = true;
}
                return (mKillProcessAfterError) ? -1 : 0;
}
            return 0;
}
}








