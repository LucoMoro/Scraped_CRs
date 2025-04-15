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

        return GsmAlphabet.gsm8BitUnpackedToString(data, offset, length);
}

static int







