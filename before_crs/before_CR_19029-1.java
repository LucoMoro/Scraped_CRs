/*Use a system property to determine mobile interfaces

Change-Id:I4fdf21d3e4b261c605a38477a22e2aa03edc8fb0*/
//Synthetic comment -- diff --git a/core/java/android/net/MobileDataStateTracker.java b/core/java/android/net/MobileDataStateTracker.java
//Synthetic comment -- index 98f32b3..a8f45cc 100644

//Synthetic comment -- @@ -79,18 +79,16 @@
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







