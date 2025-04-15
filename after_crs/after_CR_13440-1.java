/*Adding definiton for WiMax network and iDen network*/




//Synthetic comment -- diff --git a/telephony/java/android/telephony/TelephonyManager.java b/telephony/java/android/telephony/TelephonyManager.java
//Synthetic comment -- index 6685c18..8428349 100644

//Synthetic comment -- @@ -386,6 +386,10 @@
public static final int NETWORK_TYPE_HSUPA = 9;
/** Current network is HSPA */
public static final int NETWORK_TYPE_HSPA = 10;
    /** Current network is WIMAX */
    public static final int NETWORK_TYPE_WIMAX = 11;
    /** Current network is iDen */
    public static final int NETWORK_TYPE_IDEN = 12;

/**
* Returns a constant indicating the radio technology (network type)
//Synthetic comment -- @@ -451,6 +455,10 @@
return "CDMA - EvDo rev. A";
case NETWORK_TYPE_1xRTT:
return "CDMA - 1xRTT";
            case NETWORK_TYPE_WIMAX:
                return "WiMax";
            case NETWORK_TYPE_IDEN:
                return "iDen";
default:
return "UNKNOWN";
}







