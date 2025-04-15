/*Fix for NPE in NdefRecord on empty record.

See Issue 36968:http://code.google.com/p/android/issues/detail?id=36968Added null check for RTD_URI and also added null check for TNF_ABSOLUTE_RECORD

Change-Id:I13e80facfb2561904710c5025292cce1a3a97c88Signed-off-by: TrevorBender <trevor.bender@gmail.com>*/
//Synthetic comment -- diff --git a/core/java/android/nfc/NdefRecord.java b/core/java/android/nfc/NdefRecord.java
//Synthetic comment -- index ed1c5b3..0055d56 100644

//Synthetic comment -- @@ -688,13 +688,14 @@
}
} catch (FormatException e) {  }
} else if (Arrays.equals(mType, RTD_URI)) {
                    return parseWktUri().normalizeScheme();
}
break;

case TNF_ABSOLUTE_URI:
Uri uri = Uri.parse(new String(mType, Charsets.UTF_8));
                return uri.normalizeScheme();

case TNF_EXTERNAL_TYPE:
if (inSmartPoster) {







