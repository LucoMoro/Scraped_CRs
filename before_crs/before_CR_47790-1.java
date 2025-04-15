/*Set audio mode after accepting the call

Set the audio mode to incall immediately after accepting the call and not wait
till call is reported as ACTIVE by RIL. This is done to speed up time taken for audio
to set up the voice path

Bug: 7612431
Change-Id:If1ca42e85546278e2873ba839f442dc7a4921f12*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/CallManager.java b/src/java/com/android/internal/telephony/CallManager.java
//Synthetic comment -- index b87ea50..5919115 100644

//Synthetic comment -- @@ -100,6 +100,8 @@
// default phone as the first phone registered, which is PhoneBase obj
private Phone mDefaultPhone;

// state registrants
protected final RegistrantList mPreciseCallStateRegistrants
= new RegistrantList();
//Synthetic comment -- @@ -384,6 +386,10 @@
audioManager.requestAudioFocusForCall(AudioManager.STREAM_RING,
AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
}
audioManager.setMode(AudioManager.MODE_RINGTONE);
}
break;
//Synthetic comment -- @@ -407,6 +413,7 @@
AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
audioManager.setMode(newAudioMode);
}
break;
case IDLE:
if (audioManager.getMode() != AudioManager.MODE_NORMAL) {
//Synthetic comment -- @@ -415,6 +422,7 @@
// abandon audio focus after the mode has been set back to normal
audioManager.abandonAudioFocusForCall();
}
break;
}
}
//Synthetic comment -- @@ -529,6 +537,24 @@
}
}

ringingPhone.acceptCall();

if (VDBG) {







