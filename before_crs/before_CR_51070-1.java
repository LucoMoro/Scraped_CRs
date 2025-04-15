/*Change default intent

Older JB releases have a bug handling persistence invocation. One work
around right now is to have the newer release (with persistence)
use  a lower intent value to allow older release to operate in group
owner mode.

Change-Id:I7ffb66081a352929a6f421e5d6141f592be7d472*/
//Synthetic comment -- diff --git a/wifi/java/android/net/wifi/WifiNative.java b/wifi/java/android/net/wifi/WifiNative.java
//Synthetic comment -- index 5e25623..7a9f106 100644

//Synthetic comment -- @@ -42,7 +42,7 @@

private static final boolean DBG = false;
private final String mTAG;
    private static final int DEFAULT_GROUP_OWNER_INTENT = 7;

static final int BLUETOOTH_COEXISTENCE_MODE_ENABLED = 0;
static final int BLUETOOTH_COEXISTENCE_MODE_DISABLED = 1;







