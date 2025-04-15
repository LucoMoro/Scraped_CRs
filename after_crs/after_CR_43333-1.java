/*Monkey not handling SIGHUP signal

This fix resolves a problem that has always been present but not
always visible. A fix in bionic made the problem visible (see
ff3129b0fe52fabec273077871cb35515465d44b )
adb daemon on the device side terminates connection with the host
and sends SIGHUP signal to all its children involved in this
connection and also to the shell process being monkey's parent.
This fix is creating a new session for the Monkey process which
causes it to be detached from the controlling terminal and allows
it to run as a stand-alone application. The new session leader is
not affected by the SIGHUP signal sent by the parent process.

Change-Id:I09246d73b70e5bb3578f79ba1f9419eaf120de6c*/




//Synthetic comment -- diff --git a/cmds/monkey/src/com/android/commands/monkey/Monkey.java b/cmds/monkey/src/com/android/commands/monkey/Monkey.java
//Synthetic comment -- index fcf0893..bbb535e 100644

//Synthetic comment -- @@ -54,6 +54,9 @@
import java.util.List;
import java.util.Random;

import libcore.io.ErrnoException;
import libcore.io.Libcore;

/**
* Application that injects random key events and other actions into the system.
*/
//Synthetic comment -- @@ -469,6 +472,17 @@
// Set the process name showing in "ps" or "top"
Process.setArgV0("com.android.commands.monkey");

        // Creates new process session and makes the Monkey its leader.
        // The Monkey continues running as stand-alone application
        // without controlling terminal. I.e. Monkey will not receive
        // SIGHUP signal after Monkey - MonkeyRunner connection termination.
        try {
            Libcore.os.setsid();
        } catch (ErrnoException errnoException) {
            System.err.println(errnoException);
            System.exit(1);
        }

int resultCode = (new Monkey()).run(args);
System.exit(resultCode);
}







