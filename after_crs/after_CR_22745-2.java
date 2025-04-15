/*Don't fail on netlink socket closing.

If the process listening on the netlink socket closes
the listener socket, don't consider that a security hole.

Change-Id:I27ed7a997877e2d618a0447e4ee2dba8f6aea2ac*/




//Synthetic comment -- diff --git a/tests/tests/security/src/android/security/cts/VoldExploitTest.java b/tests/tests/security/src/android/security/cts/VoldExploitTest.java
//Synthetic comment -- index 9030843..a72d6de 100644

//Synthetic comment -- @@ -91,8 +91,17 @@
confirmNetlinkMsgReceived();
}
} catch (IOException e) {
            // Ignore the exception.  The process either:
            //
            // 1) Crashed
            // 2) Closed the netlink socket and refused further messages
            //
            // If #1 occurs, our PID check in testTryToCrashVold() will
            // detect the process crashed and trigger an error.
            //
            // #2 is not a security bug.  It's perfectly acceptable to
            // refuse messages from someone trying to send you
            // malicious content.
}
}








