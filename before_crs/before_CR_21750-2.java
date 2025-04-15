/*Added support for handling priority of displaytext.

Requires that the CatApp has permission for GET_TASKS

Change-Id:I20db34705099aec8945351c5257ce1d9befa2d97Signed-off-by: Christian Bejram <christian.bejram@stericsson.com>*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/cat/CatService.java b/telephony/java/com/android/internal/telephony/cat/CatService.java
//Synthetic comment -- index 1e23e34..54719cd 100644

//Synthetic comment -- @@ -16,8 +16,13 @@

package com.android.internal.telephony.cat;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncResult;
import android.os.Handler;
import android.os.HandlerThread;
//Synthetic comment -- @@ -30,9 +35,11 @@
import com.android.internal.telephony.IccFileHandler;
import com.android.internal.telephony.IccRecords;

import android.util.Config;

import java.io.ByteArrayOutputStream;

/**
* Enumeration for representing the tag value of COMPREHENSION-TLV objects. If
//Synthetic comment -- @@ -255,7 +262,17 @@
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
break;
case DISPLAY_TEXT:
                // when application is not required to respond, send an immediate response.
if (!cmdMsg.geTextMessage().responseNeeded) {
sendTerminalResponse(cmdParams.cmdDet, ResultCode.OK, false, 0, null);
}
//Synthetic comment -- @@ -690,4 +707,78 @@
sendTerminalResponse(cmdDet, resMsg.resCode, false, 0, resp);
mCurrntCmd = null;
}
}







