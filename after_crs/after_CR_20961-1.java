/*Change lbdef0309: two digit numbers are dialed normally not USSD if users dial 92~96 in croatia and serbia

Change-Id:I5573ea94a04187597e038b1798a7d44c70715701*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 12cd3c4..7bb5720 100755

//Synthetic comment -- @@ -148,7 +148,7 @@
static final int MATCH_GROUP_SIC = 9;
static final int MATCH_GROUP_PWD_CONFIRM = 11;
static final int MATCH_GROUP_DIALING_NUMBER = 12;
    


//***** Public Class methods
//Synthetic comment -- @@ -192,7 +192,7 @@

ret = new GsmMmiCode(phone);
ret.poundString = dialString;
        } else if (isTwoDigitShortCode(dialString, phone)) {
// this may be a short code, as defined in TS 22.030, 6.5.3.2
ret = null;
} else if (isShortCode(dialString, phone)) {
//Synthetic comment -- @@ -383,8 +383,6 @@
super(phone.getHandler().getLooper());
this.phone = phone;
this.context = phone.getContext();
}

//***** MmiCode implementation
//Synthetic comment -- @@ -453,9 +451,15 @@

	
static private boolean 
    isTwoDigitShortCode(String dialString, GSMPhone phone) {
        if (dialString.length() != 2) return false;

        //User dial normally using Two Digit number in case of special countries	
        String[] mTwoDigitNumberPattern;
        mTwoDigitNumberPattern = phone.getContext().getResources().getStringArray(com.android.internal.R.array.config_twoDigitNumberPattern);
        
if (mTwoDigitNumberPattern == null) return false;

for (String dialnumber : mTwoDigitNumberPattern) {
Log.d(LOG_TAG, "Two Digit Number Pattern");
if (dialString.equals(dialnumber)) {
//Synthetic comment -- @@ -463,7 +467,6 @@
return true;
}
}
return false;
}








