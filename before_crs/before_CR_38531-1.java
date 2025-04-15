/*Don't reset recipients when loading draft.

Previously, if you choose to 'reply','reply all' or 'forward' a message, then
saved a draft, when loading that draft, the 're:'/'fwd:' beginning the subject
title would cause the original source message to be re-analysed and the new
message's 'to:', 'cc:' & 'bcc:' fields to be wiped & repopulated. Definitely
not expected behaviour.

Reverts an edit from 626ad1929d63b7dd0017373939a7cf6f91c98de9

Change-Id:Id422c14d2d91d2b3326c934e4a92a3417592ddc7Signed-off-by: Joseph Price <pricechild@gmail.com>*/
//Synthetic comment -- diff --git a/src/com/android/email/activity/MessageCompose.java b/src/com/android/email/activity/MessageCompose.java
//Synthetic comment -- index 543932b..b931d6b 100644

//Synthetic comment -- @@ -940,7 +940,7 @@
// Use a best guess and infer the action here.
String inferredAction = inferAction();
if (inferredAction != null) {
                        setAction(inferredAction);
// No need to update the action selector as switching actions should do it.
return;
}







