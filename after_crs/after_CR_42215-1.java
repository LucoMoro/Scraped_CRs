/*Make SimpleSessionDescription locale safe

Explicitly use Locale.US in SimpleSessionDescription to avoid
unexpected results in some locales.

Change-Id:Idb4a36a9e332d302e1b9b940355917c0f738e076*/




//Synthetic comment -- diff --git a/voip/java/android/net/sip/SimpleSessionDescription.java b/voip/java/android/net/sip/SimpleSessionDescription.java
//Synthetic comment -- index 29166dc..9fcd21d 100644

//Synthetic comment -- @@ -18,6 +18,7 @@

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

/**
* An object used to manipulate messages of Session Description Protocol (SDP).
//Synthetic comment -- @@ -66,7 +67,7 @@
public SimpleSessionDescription(long sessionId, String address) {
address = (address.indexOf(':') < 0 ? "IN IP4 " : "IN IP6 ") + address;
mFields.parse("v=0");
        mFields.parse(String.format(Locale.US, "o=- %d %d %s", sessionId,
System.currentTimeMillis(), address));
mFields.parse("s=-");
mFields.parse("t=0 0");







