/*Use a system property to determine mobile interfaces

Change-Id:I4fdf21d3e4b261c605a38477a22e2aa03edc8fb0*/




//Synthetic comment -- diff --git a/core/java/android/net/MobileDataStateTracker.java b/core/java/android/net/MobileDataStateTracker.java
//Synthetic comment -- index 98f32b3..a8f45cc 100644

//Synthetic comment -- @@ -79,18 +79,16 @@
mEnabled = false;
}

        String[] ifNames = SystemProperties.get(
            "mobiledata.interfaces",
            "rmnet0,eth0,gprs,ppp0"
        ).split(",");

        mDnsPropNames = new String[2 * ifNames.length];
        for (int i = 0; i < ifNames.length; ++i) {
            mDnsPropNames[2*i+0] = "net." + ifNames[i] + ".dns1";
            mDnsPropNames[2*i+1] = "net." + ifNames[i] + ".dns2";
        }
}

/**







