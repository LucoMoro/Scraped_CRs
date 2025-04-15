/*Added ISO-2022-JP as supported charset.

We sometimes receive iso-2022-jp message in Japan.
So, charset iso-2022-jp should be parsed.

Change-Id:I486fbb1517435b798b8cb35cf491bfc24ee89b7bKeep original blank line.*/
//Synthetic comment -- diff --git a/core/java/com/google/android/mms/pdu/CharacterSets.java b/core/java/com/google/android/mms/pdu/CharacterSets.java
//Synthetic comment -- index 4e22ca5..7df0f12 100644

//Synthetic comment -- @@ -40,6 +40,8 @@
public static final int ISO_8859_8  = 0x0B;
public static final int ISO_8859_9  = 0x0C;
public static final int SHIFT_JIS   = 0x11;
public static final int UTF_8       = 0x6A;
public static final int BIG5        = 0x07EA;
public static final int UCS2        = 0x03E8;
//Synthetic comment -- @@ -66,6 +68,8 @@
ISO_8859_8,
ISO_8859_9,
SHIFT_JIS,
UTF_8,
BIG5,
UCS2,
//Synthetic comment -- @@ -87,6 +91,8 @@
public static final String MIMENAME_ISO_8859_8  = "iso-8859-8";
public static final String MIMENAME_ISO_8859_9  = "iso-8859-9";
public static final String MIMENAME_SHIFT_JIS   = "shift_JIS";
public static final String MIMENAME_UTF_8       = "utf-8";
public static final String MIMENAME_BIG5        = "big5";
public static final String MIMENAME_UCS2        = "iso-10646-ucs-2";
//Synthetic comment -- @@ -110,6 +116,8 @@
MIMENAME_ISO_8859_8,
MIMENAME_ISO_8859_9,
MIMENAME_SHIFT_JIS,
MIMENAME_UTF_8,
MIMENAME_BIG5,
MIMENAME_UCS2,







