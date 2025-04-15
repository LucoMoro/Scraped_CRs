/*Fix updating and deleting FDN entries with an empty alpha identifier.

- Fix AdnRecord.buildAdnString() to generate the correct record when alpha
identifier is empty. This allows the user to update an FDN entry to remove
the alpha identifier. Previously the entire entry would be deleted because
an empty record was generated here when the alpha identifier was empty,
rather than a record containing the phone number with an empty alpha tag.
Also, return null if the number or alpha tag are too long.

- Fix bug in IccProvider.delete() where efType was compared against local
FDN constant rather than IccConstants.EF_FDN. This would always return
false. Comparing with IccConstants.EF_FDN gives the intended behavior.

Change-Id:I0ea75d7e107c7318c9a48ae6e0a15845a718f4c0*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecord.java b/telephony/java/com/android/internal/telephony/AdnRecord.java
//Synthetic comment -- index 0896ba6..1bf2d3c 100644

//Synthetic comment -- @@ -19,10 +19,9 @@
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;
import android.util.Log;

import com.android.internal.telephony.GsmAlphabet;

import java.util.Arrays;


//Synthetic comment -- @@ -38,8 +37,8 @@

//***** Instance Variables

    String alphaTag = "";
    String number = "";
String[] emails;
int extRecord = 0xff;
int efid;                   // or 0 if none
//Synthetic comment -- @@ -63,8 +62,8 @@
// ADN offset
static final int ADN_BCD_NUMBER_LENGTH = 0;
static final int ADN_TON_AND_NPI = 1;
    static final int ADN_DAILING_NUMBER_START = 2;
    static final int ADN_DAILING_NUMBER_END = 11;
static final int ADN_CAPABILITY_ID = 12;
static final int ADN_EXTENSION_ID = 13;

//Synthetic comment -- @@ -152,17 +151,31 @@
}

public boolean isEmpty() {
        return alphaTag.equals("") && number.equals("") && emails == null;
}

public boolean hasExtendedRecord() {
return extRecord != 0 && extRecord != 0xff;
}

public boolean isEqual(AdnRecord adn) {
        return ( alphaTag.equals(adn.getAlphaTag()) &&
                number.equals(adn.getNumber()) &&
                Arrays.equals(emails, adn.getEmails()));
}
//***** Parcelable Implementation

//Synthetic comment -- @@ -184,36 +197,33 @@
*
* @param recordSize is the size X of EF record
* @return hex byte[recordSize] to be written to EF record
     *          return nulll for wrong format of dialing nubmer or tag
*/
public byte[] buildAdnString(int recordSize) {
byte[] bcdNumber;
byte[] byteTag;
        byte[] adnString = null;
int footerOffset = recordSize - FOOTER_SIZE_BYTES;

        if (number == null || number.equals("") ||
                alphaTag == null || alphaTag.equals("")) {

            Log.w(LOG_TAG, "[buildAdnString] Empty alpha tag or number");
            adnString = new byte[recordSize];
            for (int i = 0; i < recordSize; i++) {
                adnString[i] = (byte) 0xFF;
            }
} else if (number.length()
                > (ADN_DAILING_NUMBER_END - ADN_DAILING_NUMBER_START + 1) * 2) {
Log.w(LOG_TAG,
                    "[buildAdnString] Max length of dailing number is 20");
        } else if (alphaTag.length() > footerOffset) {
Log.w(LOG_TAG,
"[buildAdnString] Max length of tag is " + footerOffset);
} else {

            adnString = new byte[recordSize];
            for (int i = 0; i < recordSize; i++) {
                adnString[i] = (byte) 0xFF;
            }

bcdNumber = PhoneNumberUtils.numberToCalledPartyBCD(number);

System.arraycopy(bcdNumber, 0, adnString,
//Synthetic comment -- @@ -222,16 +232,17 @@
adnString[footerOffset + ADN_BCD_NUMBER_LENGTH]
= (byte) (bcdNumber.length);
adnString[footerOffset + ADN_CAPABILITY_ID]
                    = (byte) 0xFF; // Capacility Id
adnString[footerOffset + ADN_EXTENSION_ID]
= (byte) 0xFF; // Extension Record Id

            byteTag = GsmAlphabet.stringToGsm8BitPacked(alphaTag);
            System.arraycopy(byteTag, 0, adnString, 0, byteTag.length);

}

        return adnString;
}

/**








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/AdnRecordLoader.java b/telephony/java/com/android/internal/telephony/AdnRecordLoader.java
//Synthetic comment -- index cfb5aaa..55bdc06 100644

//Synthetic comment -- @@ -106,7 +106,7 @@
* It will get the record size of EF record and compose hex adn array
* then write the hex array to EF record
*
     * @param adn is set with alphaTag and phoneNubmer
* @param ef EF fileid
* @param extensionEF extension EF fileid
* @param recordNumber 1-based record index
//Synthetic comment -- @@ -159,7 +159,7 @@
data = adn.buildAdnString(recordSize[0]);

if(data == null) {
                        throw new RuntimeException("worong ADN format",
ar.exception);
}

//Synthetic comment -- @@ -218,7 +218,7 @@
throw new RuntimeException("load failed", ar.exception);
}

                    Log.d(LOG_TAG,"ADN extention EF: 0x"
+ Integer.toHexString(extensionEF)
+ ":" + adn.extRecord
+ "\n" + IccUtils.bytesToHexString(data));








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccProvider.java b/telephony/java/com/android/internal/telephony/IccProvider.java
//Synthetic comment -- index ff0eca9..5955263 100644

//Synthetic comment -- @@ -277,7 +277,7 @@
return 0;
}

        if (efType == FDN && TextUtils.isEmpty(pin2)) {
return 0;
}








