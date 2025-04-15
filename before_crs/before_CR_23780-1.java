/*Phone: Fail to play CDMA_REORDER order tone

When network sends CDMA_REORDER order tone, it is wrongly
set to CDMA_ABBR_REORDER in eclair, which plays for short duration.
Ideally it needs to play CDMA_REORDER tone which plays continously.

Change-Id:I1aca2b3fec2a19d4c89d8c7fa333e4817c64a91d*/
//Synthetic comment -- diff --git a/src/com/android/phone/CallNotifier.java b/src/com/android/phone/CallNotifier.java
//Synthetic comment -- index 90c1a59..86cfd34 100755

//Synthetic comment -- @@ -1360,8 +1360,8 @@
toneLengthMillis = 5000;
break;
case TONE_REORDER:
                    toneType = ToneGenerator.TONE_CDMA_ABBR_REORDER;
                    toneVolume = TONE_RELATIVE_VOLUME_LOPRI;
toneLengthMillis = 4000;
break;
case TONE_INTERCEPT:
//Synthetic comment -- @@ -1442,6 +1442,7 @@
needToStopTone = false;
}
} else if ((toneType == ToneGenerator.TONE_CDMA_NETWORK_BUSY_ONE_SHOT) ||
(toneType == ToneGenerator.TONE_CDMA_ABBR_REORDER) ||
(toneType == ToneGenerator.TONE_CDMA_ABBR_INTERCEPT) ||
(toneType == ToneGenerator.TONE_CDMA_CALLDROP_LITE)) {







