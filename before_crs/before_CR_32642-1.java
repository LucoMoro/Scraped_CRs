/*Set a property when on a ipv6-only default pdp data context

This sets the property gsm.pdpprotocol.ipv6 when using an ipv6-only default pdp
data context.  This is used by init to start/stop the CLAT (464xlat) process.

Change-Id:I2dd8ed9c7f18c654cc20343120f1f4c23559138cSigned-off-by: Daniel Drown <dan-android@drown.org>*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 963db2c..e0c448e 100644

//Synthetic comment -- @@ -1901,6 +1901,12 @@
setPreferredApn(mPreferredApn.id);
}
}
} else {
SystemProperties.set("gsm.defaultpdpcontext.active", "false");
}
//Synthetic comment -- @@ -1993,6 +1999,10 @@

mPhone.notifyDataConnection(apnContext.getReason(), apnContext.getApnType());

// if all data connection are gone, check whether Airplane mode request was
// pending.
if (isDisconnected()) {







