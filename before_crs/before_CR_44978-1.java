/*Telephony: Icon display issue for STK proactive commands.

These changes ensure that the complete path for EF-IMG (4F20)
is sent to qcril UIM during SIM IO operation instead of only
sending only the fileid (4f20). These changes also take care of
sending right terminal response when Icon is loaded from
EF Image file instances (4fXX).

Change-Id:I4690be11280a2fcc13598f636cf83bd34fa0425e*/
//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccConstants.java b/src/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index 847c883..b3db6c3 100644

//Synthetic comment -- @@ -76,6 +76,13 @@
static final int EF_IST = 0x6f07;
static final int EF_PCSCF = 0x6f09;

// SMS record length from TS 51.011 10.5.3
static public final int SMS_RECORD_LENGTH = 176;









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 98ab17b..fee7135 100644

//Synthetic comment -- @@ -166,10 +166,10 @@
new LoadLinearFixedContext(IccConstants.EF_IMG, recordNum,
onLoaded));

        // TODO(): Verify when path changes are done.
        mCi.iccIOForApp(COMMAND_GET_RESPONSE, IccConstants.EF_IMG, "img",
                recordNum, READ_RECORD_MODE_ABSOLUTE,
                GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null, mAid, response);
}

/**
//Synthetic comment -- @@ -257,8 +257,18 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        mCi.iccIOForApp(COMMAND_READ_BINARY, fileid, "img", highOffset, lowOffset,
                length, null, null, mAid, response);
}

/**
//Synthetic comment -- @@ -556,6 +566,11 @@
// we only support global phonebook.
return MF_SIM + DF_TELECOM + DF_PHONEBOOK;
case EF_IMG:
return MF_SIM + DF_TELECOM + DF_GRAPHICS;
}
return null;







