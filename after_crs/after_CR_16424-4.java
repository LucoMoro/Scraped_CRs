/*BluetoothHandsfree.java:
Added mechanism to broadcast vendor-specific headset events.

BluetoothHeadsetService.java:
Reformatted code.

Change-Id:I6c613d185f0ab96f9f040f54e8b8d7de9df4735bEvent broadcasts should include the Bluetooth company ID.

New class BluetoothAssignedNumbers defines all current Bluetooth company IDs.

Change-Id:I969bbb4d963c4f257f5d122436a55add3de02c89*/




//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothAssignedNumbers.java b/src/com/android/phone/BluetoothAssignedNumbers.java
new file mode 100644
//Synthetic comment -- index 0000000..e222b7a

//Synthetic comment -- @@ -0,0 +1,523 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.phone;

/**
 * Bluetooth Assigned Numbers.
 * <p>
 * For now we only include Company ID values.
 * @see <a href="https://www.bluetooth.org/technical/assignednumbers/identifiers.htm">
 * The Official Bluetooth SIG Member Website | Company Identifiers</a>
 *
 * @hide
 */
public class BluetoothAssignedNumbers {

    //// Bluetooth SIG Company ID values

    /*
     * Ericsson Technology Licensing.
     */
    public static final int ERICSSON_TECHNOLOGY = 0x0000;

    /*
     * Nokia Mobile Phones.
     */
    public static final int NOKIA_MOBILE_PHONES = 0x0001;

    /*
     * Intel Corp.
     */
    public static final int INTEL = 0x0002;

    /*
     * IBM Corp.
     */
    public static final int IBM = 0x0003;

    /*
     * Toshiba Corp.
     */
    public static final int TOSHIBA = 0x0004;

    /*
     * 3Com.
     */
    public static final int THREECOM = 0x0005;

    /*
     * Microsoft.
     */
    public static final int MICROSOFT = 0x0006;

    /*
     * Lucent.
     */
    public static final int LUCENT = 0x0007;

    /*
     * Motorola.
     */
    public static final int MOTOROLA = 0x0008;

    /*
     * Infineon Technologies AG.
     */
    public static final int INFINEON_TECHNOLOGIES = 0x0009;

    /*
     * Cambridge Silicon Radio.
     */
    public static final int CAMBRIDGE_SILICON_RADIO = 0x000A;

    /*
     * Silicon Wave.
     */
    public static final int SILICON_WAVE = 0x000B;

    /*
     * Digianswer A/S.
     */
    public static final int DIGIANSWER = 0x000C;

    /*
     * Texas Instruments Inc.
     */
    public static final int TEXAS_INSTRUMENTS = 0x000D;

    /*
     * Parthus Technologies Inc.
     */
    public static final int PARTHUS_TECHNOLOGIES = 0x000E;

    /*
     * Broadcom Corporation.
     */
    public static final int BROADCOM = 0x000F;

    /*
     * Mitel Semiconductor.
     */
    public static final int MITEL_SEMICONDUCTOR = 0x0010;

    /*
     * Widcomm, Inc.
     */
    public static final int WIDCOMM = 0x0011;

    /*
     * Zeevo, Inc.
     */
    public static final int ZEEVO = 0x0012;

    /*
     * Atmel Corporation.
     */
    public static final int ATMEL = 0x0013;

    /*
     * Mitsubishi Electric Corporation.
     */
    public static final int MITSUBISHI_ELECTRIC = 0x0014;

    /*
     * RTX Telecom A/S.
     */
    public static final int RTX_TELECOM = 0x0015;

    /*
     * KC Technology Inc.
     */
    public static final int KC_TECHNOLOGY = 0x0016;

    /*
     * Newlogic.
     */
    public static final int NEWLOGIC = 0x0017;

    /*
     * Transilica, Inc.
     */
    public static final int TRANSILICA = 0x0018;

    /*
     * Rohde & Schwarz GmbH & Co. KG.
     */
    public static final int ROHDE_AND_SCHWARZ = 0x0019;

    /*
     * TTPCom Limited.
     */
    public static final int TTPCOM = 0x001A;

    /*
     * Signia Technologies, Inc.
     */
    public static final int SIGNIA_TECHNOLOGIES = 0x001B;

    /*
     * Conexant Systems Inc.
     */
    public static final int CONEXANT_SYSTEMS = 0x001C;

    /*
     * Qualcomm.
     */
    public static final int QUALCOMM = 0x001D;

    /*
     * Inventel.
     */
    public static final int INVENTEL = 0x001E;

    /*
     * AVM Berlin.
     */
    public static final int AVM_BERLIN = 0x001F;

    /*
     * BandSpeed, Inc.
     */
    public static final int BANDSPEED = 0x0020;

    /*
     * Mansella Ltd.
     */
    public static final int MANSELLA = 0x0021;

    /*
     * NEC Corporation.
     */
    public static final int NEC = 0x0022;

    /*
     * WavePlus Technology Co., Ltd.
     */
    public static final int WAVEPLUS_TECHNOLOGY = 0x0023;

    /*
     * Alcatel.
     */
    public static final int ALCATEL = 0x0024;

    /*
     * Philips Semiconductors.
     */
    public static final int PHILIPS_SEMICONDUCTORS = 0x0025;

    /*
     * C Technologies.
     */
    public static final int C_TECHNOLOGIES = 0x0026;

    /*
     * Open Interface.
     */
    public static final int OPEN_INTERFACE = 0x0027;

    /*
     * R F Micro Devices.
     */
    public static final int RF_MICRO_DEVICES = 0x0028;

    /*
     * Hitachi Ltd.
     */
    public static final int HITACHI = 0x0029;

    /*
     * Symbol Technologies, Inc.
     */
    public static final int SYMBOL_TECHNOLOGIES = 0x002A;

    /*
     * Tenovis.
     */
    public static final int TENOVIS = 0x002B;

    /*
     * Macronix International Co. Ltd.
     */
    public static final int MACRONIX = 0x002C;

    /*
     * GCT Semiconductor.
     */
    public static final int GCT_SEMICONDUCTOR = 0x002D;

    /*
     * Norwood Systems.
     */
    public static final int NORWOOD_SYSTEMS = 0x002E;

    /*
     * MewTel Technology Inc.
     */
    public static final int MEWTEL_TECHNOLOGY = 0x002F;

    /*
     * ST Microelectronics.
     */
    public static final int ST_MICROELECTRONICS = 0x0030;

    /*
     * Synopsys.
     */
    public static final int SYNOPSYS = 0x0031;

    /*
     * Red-M (Communications) Ltd.
     */
    public static final int RED_M = 0x0032;

    /*
     * Commil Ltd.
     */
    public static final int COMMIL = 0x0033;

    /*
     * Computer Access Technology Corporation (CATC).
     */
    public static final int CATC = 0x0034;

    /*
     * Eclipse (HQ Espana) S.L.
     */
    public static final int ECLIPSE = 0x0035;

    /*
     * Renesas Technology Corp.
     */
    public static final int RENESAS_TECHNOLOGY = 0x0036;

    /*
     * Mobilian Corporation.
     */
    public static final int MOBILIAN_CORPORATION = 0x0037;

    /*
     * Terax.
     */
    public static final int TERAX = 0x0038;

    /*
     * Integrated System Solution Corp.
     */
    public static final int INTEGRATED_SYSTEM_SOLUTION = 0x0039;

    /*
     * Matsushita Electric Industrial Co., Ltd.
     */
    public static final int MATSUSHITA_ELECTRIC = 0x003A;

    /*
     * Gennum Corporation.
     */
    public static final int GENNUM = 0x003B;

    /*
     * Research In Motion.
     */
    public static final int RESEARCH_IN_MOTION = 0x003C;

    /*
     * IPextreme, Inc.
     */
    public static final int IPEXTREME = 0x003D;

    /*
     * Systems and Chips, Inc.
     */
    public static final int SYSTEMS_AND_CHIPS = 0x003E;

    /*
     * Bluetooth SIG, Inc.
     */
    public static final int BLUETOOTH_SIG = 0x003F;

    /*
     * Seiko Epson Corporation.
     */
    public static final int SEIKO_EPSON = 0x0040;

    /*
     * Integrated Silicon Solution Taiwan, Inc.
     */
    public static final int INTEGRATED_SILICON_SOLUTION = 0x0041;

    /*
     * CONWISE Technology Corporation Ltd.
     */
    public static final int CONWISE_TECHNOLOGY = 0x0042;

    /*
     * PARROT SA.
     */
    public static final int PARROT = 0x0043;

    /*
     * Socket Mobile.
     */
    public static final int SOCKET_MOBILE = 0x0044;

    /*
     * Atheros Communications, Inc.
     */
    public static final int ATHEROS_COMMUNICATIONS = 0x0045;

    /*
     * MediaTek, Inc.
     */
    public static final int MEDIATEK = 0x0046;

    /*
     * Bluegiga.
     */
    public static final int BLUEGIGA = 0x0047;

    /*
     * Marvell Technology Group Ltd.
     */
    public static final int MARVELL = 0x0048;

    /*
     * 3DSP Corporation.
     */
    public static final int THREE_DSP = 0x0049;

    /*
     * Accel Semiconductor Ltd.
     */
    public static final int ACCEL_SEMICONDUCTOR = 0x004A;

    /*
     * Continental Automotive Systems.
     */
    public static final int CONTINENTAL_AUTOMOTIVE = 0x004B;

    /*
     * Apple, Inc.
     */
    public static final int APPLE = 0x004C;

    /*
     * Staccato Communications, Inc.
     */
    public static final int STACCATO_COMMUNICATIONS = 0x004D;

    /*
     * Avago Technologies.
     */
    public static final int AVAGO = 0x004E;

    /*
     * APT Licensing Ltd.
     */
    public static final int APT_LICENSING = 0x004F;

    /*
     * SiRF Technology, Inc.
     */
    public static final int SIRF_TECHNOLOGY = 0x0050;

    /*
     * Tzero Technologies, Inc.
     */
    public static final int TZERO_TECHNOLOGIES = 0x0051;

    /*
     * J&M Corporation.
     */
    public static final int J_AND_M = 0x0052;

    /*
     * Free2move AB.
     */
    public static final int FREE2MOVE = 0x0053;

    /*
     * 3DiJoy Corporation.
     */
    public static final int THREE_DIJOY = 0x0054;

    /*
     * Plantronics, Inc.
     */
    public static final int PLANTRONICS = 0x0055;

    /*
     * Sony Ericsson Mobile Communications.
     */
    public static final int SONY_ERICSSON = 0x0056;

    /*
     * Harman International Industries, Inc.
     */
    public static final int HARMAN_INTERNATIONAL = 0x0057;

    /*
     * Vizio, Inc.
     */
    public static final int VIZIO = 0x0058;

    /*
     * Nordic Semiconductor ASA.
     */
    public static final int NORDIC_SEMICONDUCTOR = 0x0059;

    /*
     * EM Microelectronic-Marin SA.
     */
    public static final int EM_MICROELECTRONIC_MARIN = 0x005A;

    /*
     * Ralink Technology Corporation.
     */
    public static final int RALINK_TECHNOLOGY = 0x005B;

    /*
     * Belkin International, Inc.
     */
    public static final int BELKIN_INTERNATIONAL = 0x005C;

    /*
     * Realtek Semiconductor Corporation.
     */
    public static final int REALTEK_SEMICONDUCTOR = 0x005D;

    /*
     * Stonestreet One, LLC.
     */
    public static final int STONESTREET_ONE = 0x005E;

    /*
     * Wicentric, Inc.
     */
    public static final int WICENTRIC = 0x005F;

    /*
     * RivieraWaves S.A.S.
     */
    public static final int RIVIERAWAVES = 0x0060;

    /*
     * You can't instantiate one of these.
     */
    private BluetoothAssignedNumbers() {
    }

}








//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHandsfree.java b/src/com/android/phone/BluetoothHandsfree.java
//Synthetic comment -- index 9d205e3..5238da8 100644

//Synthetic comment -- @@ -249,6 +249,10 @@
} else {
initializeHandsfreeAtParser();
}

        // Headset vendor-specific commands
        registerAllVendorSpecificCommands();

headset.startEventThread();
configAudioParameters();

//Synthetic comment -- @@ -1031,8 +1035,28 @@
mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
}

    /*
     * Put the AT command, company ID, arguments, and device in an Intent and broadcast it.
     */
    private void broadcastVendorSpecificEventIntent(String command,
                                                    int companyId,
                                                    Object[] arguments,
                                                    BluetoothDevice device) {
        if (VDBG) log("broadcastVendorSpecificEventIntent(" + command + ")");
        Intent intent =
                new Intent(BluetoothHeadset.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT);
        intent.putExtra(BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD, command);
        intent.putExtra(BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID,
                        companyId);

        // assert: all elements of args are Serializable
        intent.putExtra(BluetoothHeadset.EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS, arguments);
        intent.putExtra(BluetoothDevice.EXTRA_DEVICE, device);
        mContext.sendBroadcast(intent, android.Manifest.permission.BLUETOOTH);
    }

void updateBtHandsfreeAfterRadioTechnologyChange() {
        if (VDBG) Log.d(TAG, "updateBtHandsfreeAfterRadioTechnologyChange...");

//Get the Call references from the new active phone again
mRingingCall = mPhone.getRingingCall();
//Synthetic comment -- @@ -1448,13 +1472,40 @@
return result;
}

    /*
     * Register a vendor-specific command.
     * @param commandName the name of the command.  For example, if the expected
     * incoming command is <code>AT+FOO=bar,baz</code>, the value of this should be
     * <code>"+FOO"</code>.
     * @param companyId the Bluetooth SIG Company Identifier
     * @param parser the AtParser on which to register the command
     */
    private void registerVendorSpecificCommand(String commandName,
                                               int companyId,
                                               AtParser parser) {
        parser.register(commandName,
                        new VendorSpecificCommandHandler(commandName, companyId));
    }

    /*
     * Register all vendor-specific commands here.
     */
    private void registerAllVendorSpecificCommands() {
        AtParser parser = mHeadset.getAtParser();

        // Plantronics-specific headset events go here
        registerVendorSpecificCommand("+XEVENT",
                                      BluetoothAssignedNumbers.PLANTRONICS,
                                      parser);
    }

/**
* Register AT Command handlers to implement the Headset profile
*/
private void initializeHeadsetAtParser() {
if (VDBG) log("Registering Headset AT commands");
AtParser parser = mHeadset.getAtParser();
        // Headsets usually only have one button, which is meant to cause the
// HS to send us AT+CKPD=200 or AT+CKPD.
parser.register("+CKPD", new AtCommandHandler() {
private AtCommandResult headsetButtonPress() {
//Synthetic comment -- @@ -2131,6 +2182,7 @@
return new AtCommandResult("+CPAS: " + status);
}
});

mPhonebook.register(parser);
}

//Synthetic comment -- @@ -2205,6 +2257,30 @@
return true;
}

    /*
     * This class broadcasts vendor-specific commands + arguments to interested receivers.
     */
    private class VendorSpecificCommandHandler extends AtCommandHandler {

        private String mCommandName;

        private int mCompanyID;

        private VendorSpecificCommandHandler(String commandName, int companyId) {
            mCommandName = commandName;
            mCompanyID = companyId;
        }

        @Override
        public AtCommandResult handleSetCommand(Object[] arguments) {
            broadcastVendorSpecificEventIntent(mCommandName,
                                               mCompanyID,
                                               arguments,
                                               mHeadset.getRemoteDevice());
            return new AtCommandResult(AtCommandResult.OK);
        }
    }

private boolean inDebug() {
return DBG && SystemProperties.getBoolean(DebugThread.DEBUG_HANDSFREE, false);
}








//Synthetic comment -- diff --git a/src/com/android/phone/BluetoothHeadsetService.java b/src/com/android/phone/BluetoothHeadsetService.java
old mode 100644
new mode 100755
//Synthetic comment -- index 513caad..94ab632

//Synthetic comment -- @@ -157,8 +157,10 @@
if (priority <= BluetoothHeadset.PRIORITY_OFF) {
Log.i(TAG, "Rejecting incoming connection because priority = " + priority);

                headset = new HeadsetBase(mPowerManager, mAdapter,
                                          info.mRemoteDevice,
                                          info.mSocketFd, info.mRfcommChan,
                                          null);
headset.disconnect();
return;
}
//Synthetic comment -- @@ -167,8 +169,10 @@
// headset connecting us, lets join
mRemoteDevice = info.mRemoteDevice;
setState(BluetoothHeadset.STATE_CONNECTING);
                headset = new HeadsetBase(mPowerManager, mAdapter,
                                          mRemoteDevice, info.mSocketFd,
                                          info.mRfcommChan,
                                          mConnectedStatusHandler);
mHeadsetType = type;

mConnectingStatusHandler.obtainMessage(RFCOMM_CONNECTED, headset).sendToTarget();
//Synthetic comment -- @@ -180,8 +184,10 @@
Log.i(TAG, "Already attempting connect to " + mRemoteDevice +
", disconnecting " + info.mRemoteDevice);

                    headset = new HeadsetBase(mPowerManager, mAdapter,
                                              info.mRemoteDevice,
                                              info.mSocketFd, info.mRfcommChan,
                                              null);
headset.disconnect();
}
// If we are here, we are in danger of a race condition
//Synthetic comment -- @@ -196,8 +202,10 @@
}

// Now continue with new connection, including calling callback
                mHeadset = new HeadsetBase(mPowerManager, mAdapter,
                                           mRemoteDevice,
                                           info.mSocketFd, info.mRfcommChan,
                                           mConnectedStatusHandler);
mHeadsetType = type;

setState(BluetoothHeadset.STATE_CONNECTED, BluetoothHeadset.RESULT_SUCCESS);
//Synthetic comment -- @@ -209,8 +217,10 @@
Log.i(TAG, "Already connected to " + mRemoteDevice + ", disconnecting " +
info.mRemoteDevice);

                headset = new HeadsetBase(mPowerManager, mAdapter,
                                          info.mRemoteDevice,
                                          info.mSocketFd, info.mRfcommChan,
                                          null);
headset.disconnect();
break;
}
//Synthetic comment -- @@ -356,7 +366,8 @@
long timestamp;

timestamp = System.currentTimeMillis();
            HeadsetBase headset = new HeadsetBase(mPowerManager, mAdapter,
                                                  device, channel);

int result = waitForConnect(headset);








