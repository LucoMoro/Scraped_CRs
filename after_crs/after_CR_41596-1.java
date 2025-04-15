/*Phone: Fix bluetooth headset volume controls

The bluetooth headset profile (HSP) has optional
AT commands that the headset can use to control volume.
These commands are handled in the PhoneApp, even
if their scope is far wider.

For example, when playing Music, if a headset is
connected, the Bluetooth service spawned by PhoneApp
will take care of :
 - trigerring the audio routing
 - getting HS AT commands and modifying volume

The current implementation lacks the volume AT commands
hooks to change volume levels.
Add these controls.

Change-Id:I9c9baa65241e923365fffa2dfe0b2d8f31a8d34cAuthor: Robert Jarzmik <robert.jarzmik@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 33190*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 5845397..7a2a91e 100755

//Synthetic comment -- @@ -2017,6 +2017,32 @@
return headsetButtonPress();
}
});

        // Microphone Gain
        parser.register("+VGM", new AtCommandHandler() {
            @Override
            public AtCommandResult handleSetCommand(Object[] args) {
                // AT+VGM=<gain>    in range [0,15]
                // Headset/Handsfree is reporting its current gain setting
                return new AtCommandResult(AtCommandResult.OK);
            }
        });

        // Speaker Gain
        parser.register("+VGS", new AtCommandHandler() {
            @Override
            public AtCommandResult handleSetCommand(Object[] args) {
                // AT+VGS=<gain>    in range [0,15]
                if (args.length != 1 || !(args[0] instanceof Integer)) {
                    return new AtCommandResult(AtCommandResult.ERROR);
                }
                mScoGain = (Integer) args[0];
                int flag =  mAudioManager.isBluetoothScoOn() ? AudioManager.FLAG_SHOW_UI:0;

                mAudioManager.setStreamVolume(AudioManager.STREAM_BLUETOOTH_SCO, mScoGain, flag);
                return new AtCommandResult(AtCommandResult.OK);
            }
        });
}

/**







