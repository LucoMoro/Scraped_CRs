/*Keypress wakes up modem in power save mode

When key is pressed it checks if modem
is in power save mode.
If yes,then the ACTION_SCREEN_ON intent is broadcast.

Dependencies: frameworks/base, packages/apps/Phone
Change-Id:I118db954ac87721ab8997c1411f7db5d69ca08e0*/




//Synthetic comment -- diff --git a/phone/com/android/internal/policy/impl/PhoneWindowManager.java b/phone/com/android/internal/policy/impl/PhoneWindowManager.java
//Synthetic comment -- index dec495d..364e8d7 100755

//Synthetic comment -- @@ -1741,7 +1741,21 @@
}
return false;    
}

    void wakeUpModem() {
        try {
            final ITelephony phone = getPhoneInterface();
            if (phone != null && phone.isModemPowerSave()) {
                Intent mScreenOnIntent = new Intent(Intent.ACTION_SCREEN_ON);
                mContext.sendOrderedBroadcast(mScreenOnIntent, null);
                if (localLOGV)
                    Log.i(TAG, "Trying to wake up modem");
            }
        } catch (RemoteException ex) {
            Log.w(TAG, "ITelephony.isPowerSave() threw RemoteException" + ex);
        }
    }

/** {@inheritDoc} */
public int interceptKeyTq(RawInputEvent event, boolean screenIsOn) {
int result = ACTION_PASS_TO_USER;
//Synthetic comment -- @@ -1761,6 +1775,11 @@

if (keyguardActive) {
if (screenIsOn) {
               //This is the LOCKED state
               //Wake up the modem only when the menu key is pressed
               if ( event.keycode == KeyEvent.KEYCODE_MENU) {
                  wakeUpModem();
               }
// when the screen is on, always give the event to the keyguard
result |= ACTION_PASS_TO_USER;
} else {







