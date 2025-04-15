/*Support NDEF Intent filtering for NFC Forum External Type.

Android offers NDEF Intent filtering for common TNF types such as TNF_MIME_MEDIA, TNF_ABSOLUTE_URI and TNF_WELL_KNOWN.
However, before this commit it lacks Intent filtering support for another common TNF type: TNF_EXTERNAL_TYPE.
This commit adds support for the Android equivalent of the J2ME Push-Registry of the form:

  MIDlet-Push-1: ndef:external_rtd?name=urn:nfc:ext:company.com:demo

The solution is a simple mapping from the External RTD Record Type to a MIME type:

  x-ndef/x-external-rtd; name="company.com:demo"

According to the Technical Specification for the NFC Forum External Type Names,
this solution allows for every valid External Type Name to map to an RFC2045 valid MIME type.

Change-Id:I983f6022dd908bcca6d6d80accc047693aeb2a9aSigned-off-by: Matthijs Langenberg <mlangenberg@gmail.com>*/




//Synthetic comment -- diff --git a/src/com/android/nfc/NfcService.java b/src/com/android/nfc/NfcService.java
//Synthetic comment -- index 34a100d..a75bad3 100755

//Synthetic comment -- @@ -2595,6 +2595,12 @@
intent.setData(Uri.parse(new String(record.getPayload(), Charsets.UTF_8)));
return true;
}
                    case NdefRecord.TNF_EXTERNAL_TYPE: {
                        String name = new String(type, Charsets.US_ASCII);
                        String mimeType = "x-ndef/x-external-rtd; name=\"" + name + "\"";
                        intent.setType(mimeType);
                        return true;
                    }
case NdefRecord.TNF_WELL_KNOWN: {
byte[] payload = record.getPayload();
if (payload == null || payload.length == 0) return false;







