/*ddms: Show user id only for non owners

- Makes it easier to scan
- Doesn't show user id at all for phones since there are no
  multiple users on a phone.

Change-Id:I108f220ccf0af7e1293dd6cab58705ea19014ae4*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/DevicePanel.java
//Synthetic comment -- index 5c649ba..a24b8a0 100644

//Synthetic comment -- @@ -245,7 +245,7 @@
case CLIENT_COL_NAME:
String name = cd.getClientDescription();
if (name != null) {
                            if (cd.isValidUserId() && cd.getUserId() != 0) {
return String.format(Locale.US, "%s (%d)", name, cd.getUserId());
} else {
return name;







