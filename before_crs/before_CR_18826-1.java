/*Fixing a Monkey crash in Stk.

Monkey test has reports nullpointer exception in
the RES_ID_INPUT case. Added a null check to avoid
future exceptions.

Change-Id:I489e3f389d9f37aa8f48eee399ee32ed5967b33e*/
//Synthetic comment -- diff --git a/src/com/android/stk/StkAppService.java b/src/com/android/stk/StkAppService.java
//Synthetic comment -- index ec7fd50..a21b240 100644

//Synthetic comment -- @@ -40,6 +40,7 @@
import com.android.internal.telephony.cat.AppInterface;
import com.android.internal.telephony.cat.Menu;
import com.android.internal.telephony.cat.Item;
import com.android.internal.telephony.cat.ResultCode;
import com.android.internal.telephony.cat.CatCmdMessage;
import com.android.internal.telephony.cat.CatCmdMessage.BrowserSettings;
//Synthetic comment -- @@ -477,7 +478,8 @@
case RES_ID_INPUT:
CatLog.d(this, "RES_ID_INPUT");
String input = args.getString(INPUT);
            if (mCurrentCmd.geInput().yesNo) {
boolean yesNoSelection = input
.equals(StkInputActivity.YES_STR_RESPONSE);
resMsg.setYesNo(yesNoSelection);







