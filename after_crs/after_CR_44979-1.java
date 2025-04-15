/*Telephony: Support for 2 digit short codes.

Two digit short codes should be treated as USSD string except those that start
with "1". At present only 2 digit codes that end with "#" are treated as USSD
string. Added support for other strings as well.

Change-Id:I32b55795b28ac803f73116563e2356b533d82cad*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/src/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index fdc0606..5544cad 100644

//Synthetic comment -- @@ -548,6 +548,11 @@
return true;
}
}

            if ((dialString.length() <= MAX_LENGTH_SHORT_CODE) &&
                    (dialString.charAt(0) != '1')) {
                return true;
            }
}
return false;
}







