/*Bluetooth: Fixed memory leak and file handles leak

After next pairing operation with same Bluetooth device
previous BT state machine never stopped, but new one is created.

Change-Id:Ic707d0b72fd98dc1cd9b485f337b7b6fb5a4e826Signed-off-by: Anatolii Shuba <x0158321@ti.com>*/




//Synthetic comment -- diff --git a/core/java/android/server/BluetoothService.java b/core/java/android/server/BluetoothService.java
//Synthetic comment -- index fecc8f9..7e41b7d 100755

//Synthetic comment -- @@ -2392,7 +2392,11 @@
BluetoothDeviceProfileState addProfileState(String address, boolean setTrust) {
BluetoothDeviceProfileState state =
new BluetoothDeviceProfileState(mContext, address, this, mA2dpService, setTrust);
        BluetoothDeviceProfileState oldStateMachine  = mDeviceProfileState.put(address, state);
        if (oldStateMachine != null) {
            oldStateMachine.quit();
            oldStateMachine = null;
        }
state.start();
return state;
}







