/*Fixed DeviceConfig to match change made to device schema

Change-Id:I5f7612ec4cdf45b2b93da759ab6ae218af0cd0aa*/




//Synthetic comment -- diff --git a/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java b/apps/DeviceConfig/src/com/example/android/deviceconfig/ConfigGenerator.java
//Synthetic comment -- index b2b08c6..0bfe5d0 100644

//Synthetic comment -- @@ -110,7 +110,7 @@
public static final String NODE_GPU = "gpu";
public static final String NODE_DOCK = "dock";
public static final String NODE_YDPI = "ydpi";
    public static final String NODE_POWER_TYPE = "power-type";
public static final String NODE_Y_DIMENSION = "y-dimension";
public static final String NODE_SCREEN_RATIO = "screen-ratio";
public static final String NODE_NAV_STATE = "nav-state";
//Synthetic comment -- @@ -521,7 +521,7 @@
hardware.appendChild(dock);
dock.appendChild(doc.createTextNode(" "));

            Element pluggedIn = doc.createElement(PREFIX + NODE_POWER_TYPE);
hardware.appendChild(pluggedIn);
pluggedIn.appendChild(doc.createTextNode(" "));








