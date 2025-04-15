/*Set AuthType to NONE when username of APN is not set

When APN is edited, if authType and username are both not
set, authType should be set to NONE before setup of
data call.

The code today checks mApn.user for NULL, but if
user has edited APN the mApn.user field will be set
to an empty string.

Change the check of mApn.user to also take care of
this case.

Change-Id:I7bcbceb3c3ab40f713f4a87317377752e56352f4*/




//Synthetic comment -- diff --git a/telephony/java/com/android/internal/telephony/gsm/GsmDataConnection.java b/telephony/java/com/android/internal/telephony/gsm/GsmDataConnection.java
//Synthetic comment -- index 1f5b7ab..cfa056e 100644

//Synthetic comment -- @@ -89,8 +89,8 @@

int authType = mApn.authType;
if (authType == -1) {
            authType = TextUtils.isEmpty(mApn.user) ? RILConstants.SETUP_DATA_AUTH_NONE
                    : RILConstants.SETUP_DATA_AUTH_PAP_CHAP;
}

String protocol;







