/*get group identifier level1 (Gid1)

To recognize MVNO(Mobile Virtual Network Operator), need some filters,
Service Provider Name(SPN), International Mobile Subscriber ID(IMSI),
and Group Identifier Level1(Gid1).
The GID1 elementary files on the SIM are specified in GSM 11.11 (ETS 300 977)

To get Gid1, implement it.

Bug: 8143480
Change-Id:I44f9d622240a73c42ea6418d2d55e1dae23f3b81*/




//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IccConstants.java b/src/java/com/android/internal/telephony/uicc/IccConstants.java
//Synthetic comment -- index c689539..86696401 100644

//Synthetic comment -- @@ -88,4 +88,7 @@

//UICC access
static final String DF_ADF = "7FFF";

    // USIM SIM file ids from GSM 11.11 and TS 31.102
    static final int EF_GID1 = 0x6F3E;
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/IccRecords.java b/src/java/com/android/internal/telephony/uicc/IccRecords.java
//Synthetic comment -- index 38da633..7f8dfb9 100644

//Synthetic comment -- @@ -214,6 +214,14 @@
}

/**
     * Get the Group Identifier Level 1 (GID1) on a SIM for GSM.
     * @return null if SIM is not yet ready
     */
    public String getGid1() {
        return null;
    }

    /**
* Set subscriber number to SIM record
*
* The subscriber number is stored in EF_MSISDN (TS 51.011)








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/SIMRecords.java b/src/java/com/android/internal/telephony/uicc/SIMRecords.java
old mode 100755
new mode 100644
//Synthetic comment -- index 66eaf6a..7f1d71e

//Synthetic comment -- @@ -92,6 +92,8 @@

UsimServiceTable mUsimServiceTable;

    String gid1;

// ***** Constants

// Bitmasks for SPN display rules.
//Synthetic comment -- @@ -147,6 +149,7 @@
private static final int EVENT_SIM_REFRESH = 31;
private static final int EVENT_GET_CFIS_DONE = 32;
private static final int EVENT_GET_CSP_CPHS_DONE = 33;
    private static final int EVENT_GET_GID1_DONE = 34;

// Lookup table for carriers known to produce SIMs which incorrectly indicate MNC length.

//Synthetic comment -- @@ -220,6 +223,7 @@
efCPHS_MWI = null;
spdiNetworks = null;
pnnHomeName = null;
        gid1 = null;

adnCache.reset();

//Synthetic comment -- @@ -250,6 +254,11 @@
}

@Override
    public String getGid1() {
        return gid1;
    }

    @Override
public UsimServiceTable getUsimServiceTable() {
return mUsimServiceTable;
}
//Synthetic comment -- @@ -1105,6 +1114,22 @@
handleEfCspData(data);
break;

            case EVENT_GET_GID1_DONE:
                isRecordLoadResponse = true;

                ar = (AsyncResult)msg.obj;
                data =(byte[])ar.result;

                if (ar.exception != null) {
                    loge("Exception in get GID1 " + ar.exception);
                    gid1 = null;
                    break;
                }
                gid1 = IccUtils.bytesToHexString(data);
                log("GID1: " + gid1);

                break;

default:
super.handleMessage(msg);   // IccRecords handles generic record load responses

//Synthetic comment -- @@ -1374,7 +1399,10 @@
mFh.loadEFTransparent(EF_INFO_CPHS, obtainMessage(EVENT_GET_INFO_CPHS_DONE));
recordsToLoad++;

        mFh.loadEFTransparent(EF_CSP_CPHS, obtainMessage(EVENT_GET_CSP_CPHS_DONE));
        recordsToLoad++;

        mFh.loadEFTransparent(EF_GID1, obtainMessage(EVENT_GET_GID1_DONE));
recordsToLoad++;

// XXX should seek instead of examining them all
//Synthetic comment -- @@ -1696,6 +1724,7 @@
pw.println(" spdiNetworks[]=" + spdiNetworks);
pw.println(" pnnHomeName=" + pnnHomeName);
pw.println(" mUsimServiceTable=" + mUsimServiceTable);
        pw.println(" gid1=" + gid1);
pw.flush();
}
\ No newline at end of file
}








//Synthetic comment -- diff --git a/src/java/com/android/internal/telephony/uicc/UsimFileHandler.java b/src/java/com/android/internal/telephony/uicc/UsimFileHandler.java
//Synthetic comment -- index f219153..4e7369d 100644

//Synthetic comment -- @@ -57,6 +57,7 @@
case EF_EXT2:
case EF_INFO_CPHS:
case EF_CSP_CPHS:
        case EF_GID1:
return MF_SIM + DF_ADF;

case EF_PBR:







