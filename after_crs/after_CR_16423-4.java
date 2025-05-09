/*BluetoothHeadset.java:
Added ACTION_VENDOR_SPECIFIC_HEADSET_EVENT,
EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD,  EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS.

BluetoothAssignedNumbers.java (new file):
A home for BT assigned numbers, beginning with the company IDs.

HeadsetBase.java:
Reformatted some code.

AtCommandHandler.java:
Fixed comment typos.

Change-Id:I34d6f248166305d72be66632779fc963b894379cAdded EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID.
Fixed a minor comment typo.

Change-Id:I8550692c13510a853c894b2d108bef4520d931c2*/




//Synthetic comment -- diff --git a/core/java/android/bluetooth/AtCommandHandler.java b/core/java/android/bluetooth/AtCommandHandler.java
//Synthetic comment -- index 8de2133..6deab34 100644

//Synthetic comment -- @@ -73,7 +73,7 @@
*             least one element in this array.
* @return     The result of this command.
*/
    // Typically used to set this parameter
public AtCommandResult handleSetCommand(Object[] args) {
return new AtCommandResult(AtCommandResult.ERROR);
}
//Synthetic comment -- @@ -83,11 +83,12 @@
* Test commands are part of the Extended command syntax, and are typically
* used to request an indication of the range of legal values that "FOO"
* can take.<p>
     * By default we return an OK result, to indicate that this command is at
* least recognized.<p>
* @return The result of this command.
*/
public AtCommandResult handleTestCommand() {
return new AtCommandResult(AtCommandResult.OK);
}

}








//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothAssignedNumbers.java b/core/java/android/bluetooth/BluetoothAssignedNumbers.java
new file mode 100644
//Synthetic comment -- index 0000000..55bc814

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

package android.bluetooth;

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








//Synthetic comment -- diff --git a/core/java/android/bluetooth/BluetoothHeadset.java b/core/java/android/bluetooth/BluetoothHeadset.java
//Synthetic comment -- index 95e61b6..e2d7369 100644

//Synthetic comment -- @@ -45,7 +45,7 @@
* This BluetoothHeadset object is not immediately bound to the
* BluetoothHeadset service. Use the ServiceListener interface to obtain a
* notification when it is bound, this is especially important if you wish to
 * immediately call methods on BluetoothHeadset after construction.
*
* Android only supports one connected Bluetooth Headset at a time.
*
//Synthetic comment -- @@ -85,6 +85,43 @@
"android.bluetooth.headset.extra.DISCONNECT_INITIATOR";

/**
     * Broadcast Action: Indicates a headset has posted a vendor-specific event.
     * <p>Always contains the extra fields {@link #EXTRA_DEVICE},
     * {@link #EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD}, and
     * {@link #EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS}.
     * <p>Requires {@link android.Manifest.permission#BLUETOOTH} to receive.
     */
    @SdkConstant(SdkConstantType.BROADCAST_INTENT_ACTION)
    public static final String ACTION_VENDOR_SPECIFIC_HEADSET_EVENT =
            "android.bluetooth.headset.action.VENDOR_SPECIFIC_HEADSET_EVENT";

    /**
     * A String extra field in {@link #ACTION_VENDOR_SPECIFIC_HEADSET_EVENT}
     * intents that contains the name of the vendor-specific command.
     */
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_CMD =
            "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_CMD";

    /**
     * An int extra field in {@link #ACTION_VENDOR_SPECIFIC_HEADSET_EVENT}
     * intents that contains the Company ID of the vendor defining the vendor-specific
     * command.
     * @see <a href="https://www.bluetooth.org/Technical/AssignedNumbers/identifiers.htm">
     * Bluetooth SIG Assigned Numbers - Company Identifiers</a>
     */
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID =
            "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID";

    /**
     * A Parcelable String array extra field in
     * {@link #ACTION_VENDOR_SPECIFIC_HEADSET_EVENT} intents that contains
     * the arguments to the vendor-specific command.
     */
    public static final String EXTRA_VENDOR_SPECIFIC_HEADSET_EVENT_ARGS =
            "android.bluetooth.headset.extra.VENDOR_SPECIFIC_HEADSET_EVENT_ARGS";


    /**
* TODO(API release): Consider incorporating as new state in
* HEADSET_STATE_CHANGED
*/
//Synthetic comment -- @@ -108,7 +145,7 @@

public static final int RESULT_FAILURE = 0;
public static final int RESULT_SUCCESS = 1;
    /** Connection canceled before completion. */
public static final int RESULT_CANCELED = 2;

/** Values for {@link #EXTRA_DISCONNECT_INITIATOR} */








//Synthetic comment -- diff --git a/core/java/android/bluetooth/HeadsetBase.java b/core/java/android/bluetooth/HeadsetBase.java
//Synthetic comment -- index e2935c9..9ef2eb5 100644

//Synthetic comment -- @@ -74,8 +74,8 @@

private native void cleanupNativeDataNative();

    public HeadsetBase(PowerManager pm, BluetoothAdapter adapter,
                       BluetoothDevice device, int rfcommChannel) {
mDirection = DIRECTION_OUTGOING;
mConnectTimestamp = System.currentTimeMillis();
mAdapter = adapter;
//Synthetic comment -- @@ -89,9 +89,10 @@
initializeNativeDataNative(-1);
}

    /* Create from an existing rfcomm connection */
    public HeadsetBase(PowerManager pm, BluetoothAdapter adapter,
                       BluetoothDevice device,
                       int socketFd, int rfcommChannel, Handler handler) {
mDirection = DIRECTION_INCOMING;
mConnectTimestamp = System.currentTimeMillis();
mAdapter = adapter;
//Synthetic comment -- @@ -128,7 +129,7 @@
(System.currentTimeMillis() - timestamp) + " ms");

if (result.getResultCode() == AtCommandResult.ERROR) {
            Log.i(TAG, "Error processing <" + input + ">");
}

sendURC(result.toString());
//Synthetic comment -- @@ -142,8 +143,9 @@
*/
protected void initializeAtParser() {
mAtParser = new AtParser();

        //TODO(): Get rid of this as there are no parsers registered. But because of dependencies
        // it needs to be done as part of refactoring HeadsetBase and BluetoothHandsfree
}

public AtParser getAtParser() {
//Synthetic comment -- @@ -159,8 +161,7 @@
String input = readNative(500);
if (input != null) {
handleInput(input);
                        } else {
last_read_error = getLastReadStatusNative();
if (last_read_error != 0) {
Log.i(TAG, "headset read error " + last_read_error);
//Synthetic comment -- @@ -179,8 +180,6 @@
mEventThread.start();
}

private native String readNative(int timeout_ms);
private native int getLastReadStatusNative();








