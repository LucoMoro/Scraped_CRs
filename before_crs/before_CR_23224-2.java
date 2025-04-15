/*Telephony/cat: Icon display issue for STK proactive commands.

These changes ensure that the complete path for EF-IMG (4F20)
is sent to vendor ril during SIM IO operation instead of only
sending the fileid (4f20) alone. These changes also take care of
sending complete path for EF Image file instances (4fXX)
during SIM IO operations.

Change-Id:If71d39e69b2efa0785d7e6a2315d3bfe8a18ecf6*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccConstants.java b/telephony/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index b12d2d4..89e24d5 100644

//Synthetic comment -- @@ -58,7 +58,14 @@
static final int EF_CST = 0x6f32;
static final int EF_RUIM_SPN =0x6F41;

    // SMS record length from TS 51.011 10.5.3
static public final int SMS_RECORD_LENGTH = 176;

static final String MF_SIM = "3F00";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccFileHandler.java b/telephony/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 92ddd2c..2e42d2a 100644

//Synthetic comment -- @@ -163,10 +163,10 @@
new LoadLinearFixedContext(IccConstants.EF_IMG, recordNum,
onLoaded));

        // TODO(): Verify when path changes are done.
        phone.mCM.iccIO(COMMAND_GET_RESPONSE, IccConstants.EF_IMG, "img",
                recordNum, READ_RECORD_MODE_ABSOLUTE,
                GET_RESPONSE_EF_IMG_SIZE_BYTES, null, null, response);
}

/**
//Synthetic comment -- @@ -236,8 +236,8 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        phone.mCM.iccIO(COMMAND_READ_BINARY, fileid, "img", highOffset, lowOffset,
                length, null, null, response);
}

/**
//Synthetic comment -- @@ -531,6 +531,11 @@
case EF_ICCID:
return MF_SIM;
case EF_IMG:
return MF_SIM + DF_TELECOM + DF_GRAPHICS;
}
return null;







