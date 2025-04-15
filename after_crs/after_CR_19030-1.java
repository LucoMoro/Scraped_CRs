/*Use a system property to determine mobile interfaces

Change-Id:Ib933af08122ad218578aa4e05c6fe4cb85f5bc11*/




//Synthetic comment -- diff --git a/core/java/android/net/MobileDataStateTracker.java b/core/java/android/net/MobileDataStateTracker.java
//Synthetic comment -- index d790fc1..7266f4a 100644

//Synthetic comment -- @@ -80,18 +80,16 @@
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







