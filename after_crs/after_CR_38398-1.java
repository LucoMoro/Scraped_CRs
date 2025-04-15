/*Telephony: Support for 2 digit short codes.

Two digit short codes should be treated as USSD string
except those that start with "1". At present only 2
digit codes that end with "#" are treated as USSD
string. Added support for other strings as well.

Change-Id:I8e06544889421ddbf80c6e1e0002d73aa296bb3c*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java b/telephony/java/com/android/internal/telephony/gsm/GsmMmiCode.java
//Synthetic comment -- index 16d3129..303a087 100644

//Synthetic comment -- @@ -539,6 +539,11 @@
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







