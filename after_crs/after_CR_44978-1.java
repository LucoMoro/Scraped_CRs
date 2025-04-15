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

    // Image Instance Data Files (for EF_IMG) from TS 31.102
    static final int EF_IIDF1 = 0x4f01;
    static final int EF_IIDF2 = 0x4f02;
    static final int EF_IIDF3 = 0x4f03;
    static final int EF_IIDF4 = 0x4f04;
    static final int EF_IIDF5 = 0x4f05;

// SMS record length from TS 51.011 10.5.3
static public final int SMS_RECORD_LENGTH = 176;









//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/IccFileHandler.java b/src/java/com/android/internal/telephony/IccFileHandler.java
//Synthetic comment -- index 98ab17b..fee7135 100644

//Synthetic comment -- @@ -166,10 +166,10 @@
new LoadLinearFixedContext(IccConstants.EF_IMG, recordNum,
onLoaded));

        mCi.iccIOForApp(COMMAND_GET_RESPONSE, IccConstants.EF_IMG,
                         getEFPath(IccConstants.EF_IMG), recordNum,
          READ_RECORD_MODE_ABSOLUTE, GET_RESPONSE_EF_IMG_SIZE_BYTES,
                                           null, null,mAid,response);
}

/**
//Synthetic comment -- @@ -257,8 +257,18 @@
Message response = obtainMessage(EVENT_READ_ICON_DONE, fileid, 0,
onLoaded);

        logd("IccFileHandler: loadEFImgTransparent fileid = "
               + fileid + " filePath = " + getEFPath(fileid) +
               " highOffset = " + highOffset + " lowOffset = "
               + lowOffset + " length = " + length);
        /*
         * Per TS 31.102, for displaying of Icon, under
         * DF Telecom and DF Graphics , EF instance(s) (4FXX,transparent files)
         * are present. The possible image file identifiers (EF instance) for
         * EF img ( 4F20, linear fixed file) are : 4F01 ... 4F05.
         */
        mCi.iccIOForApp(COMMAND_READ_BINARY, fileid, getEFPath(fileid),
                highOffset, lowOffset,length, null, null,mAid,response);
}

/**
//Synthetic comment -- @@ -556,6 +566,11 @@
// we only support global phonebook.
return MF_SIM + DF_TELECOM + DF_PHONEBOOK;
case EF_IMG:
        case EF_IIDF1:
        case EF_IIDF2:
        case EF_IIDF3:
        case EF_IIDF4:
        case EF_IIDF5:
return MF_SIM + DF_TELECOM + DF_GRAPHICS;
}
return null;







