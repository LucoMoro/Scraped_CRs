/*Cat : Fix for sending correct TR for Idle mode , GCF TC : 27.22.4.22.2.4

When the proactive command SET_UP_IDLE_MODE_TEXT is issueds with
empty text and non-self explanatory icon, terminal response
'CMD_DATA_NOT_UNDERSTOOD' should be sent back to the UICC as
per the spec

Change-Id:I3cca4b460f05b27dc7275c5aa6bf84ddc1970350*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/telephony/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index 12204a0..ad4ce73 100644

//Synthetic comment -- @@ -338,13 +338,20 @@
if (ctlv != null) {
textMsg.text = ValueParser.retrieveTextString(ctlv);
}
        // load icons only when text exist.
        if (textMsg.text != null) {
            ctlv = searchForTag(ComprehensionTlvTag.ICON_ID, ctlvs);
            if (ctlv != null) {
                iconId = ValueParser.retrieveIconId(ctlv);
                textMsg.iconSelfExplanatory = iconId.selfExplanatory;
            }
}

mCmdParams = new DisplayTextParams(cmdDet, textMsg);







