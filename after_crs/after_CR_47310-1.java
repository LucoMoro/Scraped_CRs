/*ADDS ISO_PCD_A_B card emulation

This patch adds the API to specify an intent for ISO_PCD_A or
ISO_PCD_B tag in an app.  It uses previously patches to the
NFC Service

Change-Id:Ifd46db6d1d60c0e92cb6f04e26949114319a5486*/




//Synthetic comment -- diff --git a/core/java/android/nfc/Tag.java b/core/java/android/nfc/Tag.java
old mode 100644
new mode 100755
//Synthetic comment -- index f2cd232..d73951d

//Synthetic comment -- @@ -27,6 +27,8 @@
import android.nfc.tech.NfcBarcode;
import android.nfc.tech.NfcF;
import android.nfc.tech.NfcV;
import android.nfc.tech.IsoPcdA;
import android.nfc.tech.IsoPcdB;
import android.nfc.tech.TagTechnology;
import android.os.Bundle;
import android.os.Parcel;
//Synthetic comment -- @@ -188,6 +190,12 @@
case TagTechnology.NFC_BARCODE:
strings[i] = NfcBarcode.class.getName();
break;
                case TagTechnology.ISO_PCD_A:
                    strings[i] = IsoPcdA.class.getName();
                    break;
                case TagTechnology.ISO_PCD_B:
                    strings[i] = IsoPcdB.class.getName();
                    break;
default:
throw new IllegalArgumentException("Unknown tech type " + techList[i]);
}








//Synthetic comment -- diff --git a/core/java/android/nfc/tech/IsoPcdA.java b/core/java/android/nfc/tech/IsoPcdA.java
new file mode 100755
//Synthetic comment -- index 0000000..5d266de

//Synthetic comment -- @@ -0,0 +1,100 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 *   HOST CARD EMULATION PATCH 0.01
 *   Author:  doug yeager (doug@simplytapp.com)
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

package android.nfc.tech;

import android.nfc.ErrorCodes;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

/**
 * Provides access to ISO-PCD type A (ISO 14443-4) properties and I/O operations on a {@link Tag}.
 *
 * <p>Acquire an {@link IsoPcdA} object using {@link #get}.
 * <p>The primary ISO-PCD type A I/O operation is {@link #transceive}. Applications must
 * implement their own protocol stack on top of {@link #transceive}.
 *
 * <p class="note"><strong>Note:</strong> Methods that perform I/O operations
 * require the {@link android.Manifest.permission#NFC} permission.
 */
public final class IsoPcdA extends BasicTagTechnology {

    /**
     * Get an instance of {@link IsoPcdA} for the given tag.
     * <p>Does not cause any RF activity and does not block.
     * <p>Returns null if {@link IsoPcdA} was not enumerated in {@link Tag#getTechList}.
     * This indicates the tag does not support ISO-PCD type A.
     *
     * @param tag an ISO-PCD type A compatible PCD
     * @return ISO-PCD type A object
     */
    public static IsoPcdA get(Tag tag) {
        if (!tag.hasTech(TagTechnology.ISO_PCD_A)) return null;
        try {
            return new IsoPcdA(tag);
        } catch (RemoteException e) {
            return null;
        }
    }

    /** @hide */
    public IsoPcdA(Tag tag)
            throws RemoteException {
        super(tag, TagTechnology.ISO_PCD_A);
        Bundle extras = tag.getTechExtras(TagTechnology.ISO_PCD_A);
    }

    /**
     * Send raw ISO-PCD type A data to the PCD and receive the response.
     *
     * <p>Applications must only send the INF payload, and not the start of frame and
     * end of frame indicators. Applications do not need to fragment the payload, it
     * will be automatically fragmented and defragmented by {@link #transceive} if
     * it exceeds FSD/FSC limits.
     *
     * <p>Use {@link #getMaxTransceiveLength} to retrieve the maximum number of bytes
     * that can be sent with {@link #transceive}.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param data  - on the first call to transceive after PCD activation, the data sent to the method will be ignored
     * @return response bytes received, will not be null
     * @throws TagLostException if the tag leaves the field
     * @throws IOException if there is an I/O failure, or this operation is canceled
     */
    public byte[] transceive(byte[] data) throws IOException {
        return transceive(data, true);
    }

    /**
     * Return the maximum number of bytes that can be sent with {@link #transceive}.
     * @return the maximum number of bytes that can be sent with {@link #transceive}.
     */
    public int getMaxTransceiveLength() {
        return getMaxTransceiveLengthInternal();
    }
}








//Synthetic comment -- diff --git a/core/java/android/nfc/tech/IsoPcdB.java b/core/java/android/nfc/tech/IsoPcdB.java
new file mode 100755
//Synthetic comment -- index 0000000..f8658a9

//Synthetic comment -- @@ -0,0 +1,100 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 *   HOST CARD EMULATION PATCH 0.01
 *   Author:  doug yeager (doug@simplytapp.com)
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

package android.nfc.tech;

import android.nfc.ErrorCodes;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import java.io.IOException;

/**
 * Provides access to ISO-PCD type B (ISO 14443-4) properties and I/O operations on a {@link Tag}.
 *
 * <p>Acquire an {@link IsoPcdB} object using {@link #get}.
 * <p>The primary ISO-PCD type B I/O operation is {@link #transceive}. Applications must
 * implement their own protocol stack on top of {@link #transceive}.
 *
 * <p class="note"><strong>Note:</strong> Methods that perform I/O operations
 * require the {@link android.Manifest.permission#NFC} permission.
 */
public final class IsoPcdB extends BasicTagTechnology {

    /**
     * Get an instance of {@link IsoPcdB} for the given tag.
     * <p>Does not cause any RF activity and does not block.
     * <p>Returns null if {@link IsoPcdB} was not enumerated in {@link Tag#getTechList}.
     * This indicates the tag does not support ISO-PCD type B.
     *
     * @param tag an ISO-PCD type B compatible PCD
     * @return ISO-PCD type B object
     */
    public static IsoPcdB get(Tag tag) {
        if (!tag.hasTech(TagTechnology.ISO_PCD_B)) return null;
        try {
            return new IsoPcdB(tag);
        } catch (RemoteException e) {
            return null;
        }
    }

    /** @hide */
    public IsoPcdB(Tag tag)
            throws RemoteException {
        super(tag, TagTechnology.ISO_PCD_B);
        Bundle extras = tag.getTechExtras(TagTechnology.ISO_PCD_B);
    }

    /**
     * Send raw ISO-PCD type B data to the PCD and receive the response.
     *
     * <p>Applications must only send the INF payload, and not the start of frame and
     * end of frame indicators. Applications do not need to fragment the payload, it
     * will be automatically fragmented and defragmented by {@link #transceive} if
     * it exceeds FSD/FSC limits.
     *
     * <p>Use {@link #getMaxTransceiveLength} to retrieve the maximum number of bytes
     * that can be sent with {@link #transceive}.
     *
     * <p>This is an I/O operation and will block until complete. It must
     * not be called from the main application thread. A blocked call will be canceled with
     * {@link IOException} if {@link #close} is called from another thread.
     *
     * <p class="note">Requires the {@link android.Manifest.permission#NFC} permission.
     *
     * @param data - on the first call to transceive after PCD activation, the data sent to the method will be ignored
     * @return response bytes received, will not be null
     * @throws TagLostException if the tag leaves the field
     * @throws IOException if there is an I/O failure, or this operation is canceled
     */
    public byte[] transceive(byte[] data) throws IOException {
        return transceive(data, true);
    }

    /**
     * Return the maximum number of bytes that can be sent with {@link #transceive}.
     * @return the maximum number of bytes that can be sent with {@link #transceive}.
     */
    public int getMaxTransceiveLength() {
        return getMaxTransceiveLengthInternal();
    }
}








//Synthetic comment -- diff --git a/core/java/android/nfc/tech/TagTechnology.java b/core/java/android/nfc/tech/TagTechnology.java
old mode 100644
new mode 100755
//Synthetic comment -- index 3493ea71..07b81c8

//Synthetic comment -- @@ -157,6 +157,24 @@
public static final int NFC_BARCODE = 10;

/**
     * This technology is an instance of {@link IsoPcdA}.
     * <p>Support for this technology type is optional. If a stack doesn't support this technology
     * type tags using it must still be discovered and present the lower level radio interface
     * technologies in use.
     * @hide
     */
    public static final int ISO_PCD_A = 11;

    /**
     * This technology is an instance of {@link IsoPcdB}.
     * <p>Support for this technology type is optional. If a stack doesn't support this technology
     * type tags using it must still be discovered and present the lower level radio interface
     * technologies in use.
     * @hide
     */
    public static final int ISO_PCD_B = 12;

    /**
* Get the {@link Tag} object backing this {@link TagTechnology} object.
* @return the {@link Tag} backing this {@link TagTechnology} object.
*/







