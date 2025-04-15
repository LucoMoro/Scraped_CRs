/*Adding ksc5601 decoding for ksc5601 encoded Korean Stirng before gms8bit decoding.
Signed-off-by: Sang-Jun Park <sj2202.park@samsung.com>

Change-Id:I7a59aa694450f7df0bff964a9386c800b27619b9*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccUtils.java b/telephony/java/com/android/internal/telephony/IccUtils.java
old mode 100644
new mode 100755
//Synthetic comment -- index 71936f1..88cd87d

//Synthetic comment -- @@ -223,7 +223,39 @@
return ret.toString();
}

        boolean ksc5601flag = false;
        int c;

        for (int i = 0; i <length ;i = i+2){
           c = data[i] & 0xff;
            if (c>=0xB0 && c<=0xc8){
                ksc5601flag = true;
            }
        }
        if (!ksc5601flag)
            return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);

        //To read KSC5601 USIM data. written by pilkyoo sec

        int str_len;
        String ret;

        try {
               for ( str_len = 0; str_len < length ; str_len++){
                    c = data[str_len] & 0xff;
                    if (c == 0xff) {
                        data[str_len] = 0x00;
                        break;
                    }
                }
                ret = new String(data, offset, str_len, "EUC_KR");
                Log.d(LOG_TAG, "String(data, offset, str_len, EUC_KR)");
            } catch (UnsupportedEncodingException ex) {
                ret = "";
                Log.e(LOG_TAG, "implausible UnsupportedEncodingException", ex);
                return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);        
            }
        return ret;
}

static int







