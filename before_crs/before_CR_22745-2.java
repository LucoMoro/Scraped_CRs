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
            fail("Message send to PID=" + pid
                    + " failed.  It probably crashed due to CVE-2011-1823.");
}
}








