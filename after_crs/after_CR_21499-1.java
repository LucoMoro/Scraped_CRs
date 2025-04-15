/*Add check for null pointer to avoid crash.

This is added in order to prevent a crash. Also, updating the name
of a device to null, making it appear as 'Unknown', is probably not
intended.

Change-Id:Ibd3c93741489aae9118ba7b45223d58d5de6812f*/




//Synthetic comment -- diff --git a/src/com/android/bluetooth/opp/BluetoothOppPreference.java b/src/com/android/bluetooth/opp/BluetoothOppPreference.java
//Synthetic comment -- index 3ea207e..11a3184 100644

//Synthetic comment -- @@ -126,7 +126,7 @@

public void setName(BluetoothDevice remoteDevice, String name) {
if (V) Log.v(TAG, "Setname for " + remoteDevice + " to " + name);
        if (name != null && !name.equals(getName(remoteDevice))) {
Editor ed = mNamePreference.edit();
ed.putString(remoteDevice.getAddress(), name);
ed.apply();







