/*Fix contact lookup with CLIR prefix

Allow phone numbers which are saved in the phonebook including a
CLIR prefix to be correctly resolved on an incoming call.

For a proper fix, all special cases detailed in calledPartyBCDToString()
should be taken into account; unfortunately that function cannot be
directly used as it currently is.*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/PhoneNumberUtils.java b/telephony/java/android/telephony/PhoneNumberUtils.java
//Synthetic comment -- index 07afe30..18bc951 100644

//Synthetic comment -- @@ -1480,6 +1480,13 @@
* @hide
*/
public static String normalizeNumber(String phoneNumber) {
StringBuilder sb = new StringBuilder();
int len = phoneNumber.length();
for (int i = 0; i < len; i++) {







