/*Set a property when on a ipv6-only default pdp data context

This sets the property gsm.pdpprotocol.ipv6 when using an ipv6-only default pdp
data context.  This is used by init to start/stop the CLAT (464xlat) process.

This also sets the property gsm.defaultpdpcontext.interface to be the interface
name of the default pdp network interface.

Change-Id:I2dd8ed9c7f18c654cc20343120f1f4c23559138cSigned-off-by: Daniel Drown <dan-android@drown.org>*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 95ea107..a87c6a5 100644

//Synthetic comment -- @@ -1984,6 +1984,16 @@
setPreferredApn(mPreferredApn.id);
}
}

                    LinkProperties apnLinkProperties = dcac.getLinkPropertiesSync();
                    SystemProperties.set("gsm.defaultpdpcontext.interface",
			apnLinkProperties.getInterfaceName());

                    if(apn.protocol.equals(RILConstants.SETUP_DATA_PROTOCOL_IPV6)) {
                        SystemProperties.set("gsm.pdpprotocol.ipv6", "1");
                    } else {
                        SystemProperties.set("gsm.pdpprotocol.ipv6", "0");
                    }
} else {
SystemProperties.set("gsm.defaultpdpcontext.active", "false");
}
//Synthetic comment -- @@ -2076,6 +2086,11 @@

mPhone.notifyDataConnection(apnContext.getReason(), apnContext.getApnType());

        if(TextUtils.equals(apnContext.getApnType(),Phone.APN_TYPE_DEFAULT)) {
            SystemProperties.set("gsm.pdpprotocol.ipv6", "0");
            SystemProperties.set("gsm.defaultpdpcontext.interface", "");
        }

// if all data connection are gone, check whether Airplane mode request was
// pending.
if (isDisconnected()) {







