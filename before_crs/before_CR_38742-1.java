/*Plug a fd leak.

Change-Id:Ia7189e67e8a03eceaa81e13cac98f20a82a44276*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/VoiceMailConstants.java b/telephony/java/com/android/internal/telephony/gsm/VoiceMailConstants.java
//Synthetic comment -- index 0e49e35..d2665cb 100644

//Synthetic comment -- @@ -110,6 +110,12 @@
Log.w(LOG_TAG, "Exception in Voicemail parser " + e);
} catch (IOException e) {
Log.w(LOG_TAG, "Exception in Voicemail parser " + e);
}
}
}







