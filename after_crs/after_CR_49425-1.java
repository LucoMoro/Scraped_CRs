/*stk: fix "set up idle mode" + "Display" text icon

According to the 3GPP 31.111 chap 6.5.4: "if the terminal
receives an icon, and either an empty or no alpha text
string is given by the UICC, than the terminal shall reject
the command with general result "Command data not understood
by terminal".

This patch fix the code to respect this point for the set up
idle mode text and Display Text proactive command.

Change-Id:I42782af101914c23454c86dc765c1ac530636b73Author: Nizar Haouati <nizar.haouati@intel.com>
Signed-off-by: Nizar Haouati <nizar.haouati@intel.com>
Signed-off-by: Guillaume Lucas <guillaumex.lucas@intel.com>
Signed-off-by: Arun Ravindran <arun.ravindran@intel.com>
Signed-off-by: Jeevaka Badrappan <jeevaka.badrappan@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 15564*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java b/src/java/com/android/internal/telephony/cat/CommandParamsFactory.java
//Synthetic comment -- index ed30279..fa36b94 100644

//Synthetic comment -- @@ -314,6 +314,14 @@
textMsg.isHighPriority = (cmdDet.commandQualifier & 0x01) != 0;
textMsg.userClear = (cmdDet.commandQualifier & 0x80) != 0;

        // According to 3GPP 31.111 chap 6.5.4 (ETSI TS 102 223 clause 6.5.4):
        // If the terminal receives an icon and either an empty or no alpha
        // text string is given by UICC, than the terminal shall reject the
        // command with general result "Command not understood by terminal".
        if ((iconId != null) && (textMsg.text == null)) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }

mCmdParams = new DisplayTextParams(cmdDet, textMsg);

if (iconId != null) {
//Synthetic comment -- @@ -348,15 +356,20 @@
if (ctlv != null) {
textMsg.text = ValueParser.retrieveTextString(ctlv);
}

        ctlv = searchForTag(ComprehensionTlvTag.ICON_ID, ctlvs);
        if (ctlv != null) {
            iconId = ValueParser.retrieveIconId(ctlv);
            textMsg.iconSelfExplanatory = iconId.selfExplanatory;
}

        // According to 3GPP 31.111 chap 6.5.4 (ETSI TS 102 223 clause 6.5.4):
        // If the terminal receives an icon and either an empty or no alpha
        // text string is given by UICC, than the terminal shall reject the
        // command with general result "Command not understood by terminal".
        if ((iconId != null) && (textMsg.text == null)) {
            throw new ResultException(ResultCode.CMD_DATA_NOT_UNDERSTOOD);
        }
mCmdParams = new DisplayTextParams(cmdDet, textMsg);

if (iconId != null) {







