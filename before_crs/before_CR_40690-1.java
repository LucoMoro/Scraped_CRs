/*Fix Data stall issue

In the case of disabling apn and enabling immediately, as the state
of apn type may still be CONNECTED, it will return
Phone.APN_ALREADY_ACTIVE before setting Enabled of apn type as true,
and result in the data stall.

To avoid the issue, set enabled as true in enableApnType() even it
is already connected.

Change-Id:Ie2bad4557a6bd158424625a8bb590a18d1577fc2*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java b/src/java/com/android/internal/telephony/gsm/GsmDataConnectionTracker.java
//Synthetic comment -- index 0969ec8..2694204 100644

//Synthetic comment -- @@ -501,12 +501,12 @@

// If already active, return
if (DBG) log("enableApnType: " + apnType + " mState(" + apnContext.getState() + ")");

if (apnContext.getState() == DctConstants.State.CONNECTED) {
if (DBG) log("enableApnType: return APN_ALREADY_ACTIVE");
return PhoneConstants.APN_ALREADY_ACTIVE;
}
        setEnabled(apnTypeToId(apnType), true);
if (DBG) {
log("enableApnType: new apn request for type " + apnType +
" return APN_REQUEST_STARTED");







