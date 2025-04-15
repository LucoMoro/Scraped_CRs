/*Mms: Correction to display the dial number of SMS

This patch fixes the display of the dial number of SMS, if the SMS
text contains the dial number.

Change-Id:Ia9f690106dc6ca084f2d04e33b2fdb4e1a004f36Author: Emmanuel Delaude <emmanuelx.delaude@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 26712*/
//Synthetic comment -- diff --git a/src/com/android/mms/ui/MessageListItem.java b/src/com/android/mms/ui/MessageListItem.java
//Synthetic comment -- index aeb1473..4d4b403 100644

//Synthetic comment -- @@ -618,8 +618,13 @@
}
final String telPrefix = "tel:";
if (url.startsWith(telPrefix)) {
                            url = PhoneNumberUtils.formatNumber(
                                            url.substring(telPrefix.length()), mDefaultCountryIso);
}
tv.setText(url);
} catch (android.content.pm.PackageManager.NameNotFoundException ex) {







