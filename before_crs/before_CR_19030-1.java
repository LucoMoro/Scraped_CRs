/*Use a system property to determine mobile interfaces

Change-Id:Ib933af08122ad218578aa4e05c6fe4cb85f5bc11*/
//Synthetic comment -- diff --git a/core/java/android/net/MobileDataStateTracker.java b/core/java/android/net/MobileDataStateTracker.java
//Synthetic comment -- index d790fc1..7266f4a 100644

//Synthetic comment -- @@ -80,18 +80,16 @@
mEnabled = false;
}

        mDnsPropNames = new String[] {
                "net.rmnet0.dns1",
                "net.rmnet0.dns2",
                "net.eth0.dns1",
                "net.eth0.dns2",
                "net.eth0.dns3",
                "net.eth0.dns4",
                "net.gprs.dns1",
                "net.gprs.dns2",
                "net.ppp0.dns1",
                "net.ppp0.dns2"};

}

/**







