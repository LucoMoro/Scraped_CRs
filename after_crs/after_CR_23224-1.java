/*cat : Icon display issue for STK proactive commands.

These changes ensure that the complete path for EF-IMG (4F20)
is sent to qcril UIM during SIM IO operation instead of only
sending only the fileid (4f20). These changes also take care of
sending right terminal response when Icon is loaded from
EF Image file instances (4fXX)

Change-Id:If71d39e69b2efa0785d7e6a2315d3bfe8a18ecf6*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccConstants.java b/telephony/java/com/android/internal/telephony/IccConstants.java
//Synthetic comment -- index b12d2d4..89e24d5 100644

//Synthetic comment -- @@ -58,7 +58,14 @@
static final int EF_CST = 0x6f32;
static final int EF_RUIM_SPN =0x6F41;

   // Image Instance Data Files (for EF_IMG) from TS 31.102
   static final int EF_IIDF1 = 0x4f01;
   static final int EF_IIDF2 = 0x4f02;
   static final int EF_IIDF3 = 0x4f03;
   static final int EF_IIDF4 = 0x4f04;
   static final int EF_IIDF5 = 0x4f05;

   // SMS record length from TS 51.011 10.5.3
static public final int SMS_RECORD_LENGTH = 176;

static final String MF_SIM = "3F00";








//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/IccFileHandler.java b/telephony/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 92ddd2c..71f7d2e 100644

//Synthetic comment -- @@ -164,9 +164,10 @@
onLoaded));

// TODO(): Verify when path changes are done.
        phone.mCM.iccIO(COMMAND_GET_RESPONSE, IccConstants.EF_IMG,
                getEFPath(IccConstants.EF_IMG), recordNum,
                READ_RECORD_MODE_ABSOLUTE, GET_RESPONSE_EF_IMG_SIZE_BYTES,
                 null, null, response);
}

/**
//Synthetic comment -- @@ -236,8 +237,8 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        phone.mCM.iccIO(COMMAND_READ_BINARY, fileid, getEFPath(fileid),
                highOffset, lowOffset, length, null, null, response);
}

/**
//Synthetic comment -- @@ -531,6 +532,11 @@
case EF_ICCID:
return MF_SIM;
case EF_IMG:
        case EF_IIDF1:
        case EF_IIDF2:
        case EF_IIDF3:
        case EF_IIDF4:
        case EF_IIDF5:
return MF_SIM + DF_TELECOM + DF_GRAPHICS;
}
return null;







