/*Change ROUTE_ALL from 15 (4-bit mask) to -1 (32-bit mask) to allow for more routes in the future.*/




//Synthetic comment -- diff --git a/media/java/android/media/AudioSystem.java b/media/java/android/media/AudioSystem.java
//Synthetic comment -- index 70b9f18..7526379 100644

//Synthetic comment -- @@ -114,8 +114,7 @@
public static final int ROUTE_SPEAKER           = (1 << 1);
public static final int ROUTE_BLUETOOTH         = (1 << 2);
public static final int ROUTE_HEADSET           = (1 << 3);
    public static final int ROUTE_ALL               = 0xFFFFFFFF;

/*
* Sets the audio routing for a specified mode







