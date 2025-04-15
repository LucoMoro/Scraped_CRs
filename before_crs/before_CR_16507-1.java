/*Telephony: Update the Mcc Table for India

The operator’s id (MNC) isn't displayed correctly because
it's cut-off to only 2 digits. This is a problem when creating
new APN's because this information is not known to the most users.
That means that after the creation of a new APN, the MNC code
will be wrong (only the first 2 digits are retrieved from the
table) and the user will not be able to access the internet.

Change-Id:Icc2aaa9717501d4d6cd2b9f829c342baa890695e*/
//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/MccTable.java b/telephony/java/com/android/internal/telephony/MccTable.java
//Synthetic comment -- index b73c2f7..e257705 100644

//Synthetic comment -- @@ -145,7 +145,7 @@
(401, 'kz', 2, 'Kazakhstan (Republic of)'),
(402, 'bt', 2, 'Bhutan (Kingdom of)'),
(404, 'in', 2, 'India (Republic of)'),
  (405, 'in', 2, 'India (Republic of)'),
(410, 'pk', 2, 'Pakistan (Islamic Republic of)'),
(412, 'af', 2, 'Afghanistan'),
(413, 'lk', 2, 'Sri Lanka (Democratic Socialist Republic of)'),
//Synthetic comment -- @@ -461,7 +461,7 @@
0x67640400, 0x6d730400, 0x6b6e0400, 0x6c630400, 0x76630400, 0x6e6c0400,
0x61770400, 0x62730400, 0x61690600, 0x646d0400, 0x63750400, 0x646f0400,
0x68740400, 0x74740400, 0x74630400, 0x617a0400, 0x6b7a0400, 0x62740400,
        0x696e0400, 0x696e0400, 0x706b0400, 0x61660400, 0x6c6b0400, 0x6d6d0400,
0x6c620400, 0x6a6f0400, 0x73790400, 0x69710400, 0x6b770400, 0x73610400,
0x79650400, 0x6f6d0400, 0x70730400, 0x61650400, 0x696c0400, 0x62680400,
0x71610400, 0x6d6e0400, 0x6e700400, 0x61650400, 0x61650400, 0x69720400,







