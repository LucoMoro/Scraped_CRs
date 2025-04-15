/*PBAP: Fix for Call History property parameter.

Use correct strings for Call History property parameter.
Values taken from PBAP v1.0. The same values are also in
PBAP v1.1.

Change-Id:Iacd2c23d472216c711193f622f3c4c293127d17cSigned-off-by: christian bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/src/com/android/bluetooth/pbap/BluetoothPbapCallLogComposer.java b/src/com/android/bluetooth/pbap/BluetoothPbapCallLogComposer.java
//Synthetic comment -- index f395d21..19855cb 100644

//Synthetic comment -- @@ -71,6 +71,7 @@
private static final int CALLER_NUMBERLABEL_COLUMN_INDEX = 5;

// Property for call log entry
private static final String VCARD_PROPERTY_X_TIMESTAMP = "X-IRMC-CALL-DATETIME";
private static final String VCARD_PROPERTY_CALLTYPE_INCOMING = "RECEIVED";
private static final String VCARD_PROPERTY_CALLTYPE_OUTGOING = "DIALED";







