/*Fixing services start order that impacts ICS - JB upgrade

JB has introduced LockSettingsService. When the phone is
upgrading from ICS, that used another way to store lock
settings, the LockSettingsService needs to import these
settings to store in its database. This happens when the
systemReady() method of this class is called by SystemServer.

The problem resides in the fact that the
DevicePolicyManagerService actually needs to access the
LockSettingsService during its systemReady() initialization,
causing invalid values to be read by it which propagates and
ends up causing a invalid return in the method
isActivePasswordSufficient.

If user had a Google corporate account that enforces password
related policies through Google Apps Device Policy (GADP) app
in ICS, when he upgrades to JB, the GADP will throw a
notification saying that the password doesn't meet the required
policies and needs to be changed, incorrectly, since it wasn't
touched during upgrade.

This fix initializes the LockSettingsService before the
DevicePolicyManagerService, which is the correct way since
the latter uses the first in its initialization. This prevents
this issue to happen, and probably future issues, depending
on the way that LockSettingsService evolves.

Change-Id:I3d4334a8b728f0ad9ae744cece430d15af25a0b7*/
//Synthetic comment -- diff --git a/services/java/com/android/server/SystemServer.java b/services/java/com/android/server/SystemServer.java
//Synthetic comment -- index f300d74..52572a0 100644

//Synthetic comment -- @@ -677,6 +677,12 @@
reportWtf("making Vibrator Service ready", e);
}

if (devicePolicy != null) {
try {
devicePolicy.systemReady();
//Synthetic comment -- @@ -718,11 +724,6 @@
} catch (Throwable e) {
reportWtf("making Package Manager Service ready", e);
}
        try {
            lockSettings.systemReady();
        } catch (Throwable e) {
            reportWtf("making Lock Settings Service ready", e);
        }

// These are needed to propagate to the runnable below.
final Context contextF = context;







