/*add columns of Carrier about MVNO

To recognize MVNO, use new fields, SPN, IMSI, GID(Group Identifier Level 1),
and MVNO_TYPE.

Bug: 8143480

Change-Id:I1000b6a00a9ffd69777a349f8af0822393b8f96d*/




//Synthetic comment -- diff --git a/src/java/android/provider/Telephony.java b/src/java/android/provider/Telephony.java
//Synthetic comment -- index 50f3203..e9a598f 100644

//Synthetic comment -- @@ -1820,6 +1820,16 @@
* but currently only used for LTE(14) and EHRPD(13).
*/
public static final String BEARER = "bearer";

        public static final String SPN = "spn";
        public static final String IMSI = "imsi";
        public static final String GID = "gid";
        /**
          * MVNO type
          * spn(Service Provider Name), imsi, gid(Group Identifier Level 1)
          */
        public static final String MVNO_TYPE = "mvno_type";

}

/**







