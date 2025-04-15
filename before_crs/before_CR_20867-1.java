/*two digit numbers is dialed normally not USSD if users dial 92~96
in croatia and serbia

Change-Id:Ibdef0309d7494d489bcde531cc1bbbe33e003307Signed-off-by: Sang-Jun Park <sj2202.park@samsung.com>*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
old mode 100644
new mode 100755
//Synthetic comment -- index aa16fa3..65c5ad7

//Synthetic comment -- @@ -89,6 +89,10 @@
static final String SC_PUK          = "05";
static final String SC_PUK2         = "052";

//***** Event Constants

static final int EVENT_SET_COMPLETE         = 1;
//Synthetic comment -- @@ -191,6 +195,9 @@

ret = new GsmMmiCode(phone);
ret.poundString = dialString;
} else if (isShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
ret = new GsmMmiCode(phone);
//Synthetic comment -- @@ -445,6 +452,23 @@

}

/**
* Helper function for newFromDialString.  Returns true if dialString appears to be a short code
* AND conditions are correct for it to be treated as such.








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java b/telephony/java/com/android/internal/telephony/gsm/SIMRecords.java
old mode 100644
new mode 100755
//Synthetic comment -- index b14896a..6e161d3

//Synthetic comment -- @@ -453,6 +453,35 @@
return null;
}

// Length = length of MCC + length of MNC
// length of mcc = 3 (TS 23.003 Section 2.2)
return imsi.substring(0, 3 + mncLength);







