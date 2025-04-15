/*Telephony: Support for 2 digit short codes.

Two digit short codes should be treated as USSD string
except those that start with "1". At present only 2
digit codes that end with "#" are treated as USSD
string. Added support for other strings as well

Change-Id:Id8977e7b05df51a35c035b640fb7f958ebd9f8c5*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fdc0606..0718e86 100644

//Synthetic comment -- @@ -548,6 +548,11 @@
return true;
}
}
}
return false;
}







