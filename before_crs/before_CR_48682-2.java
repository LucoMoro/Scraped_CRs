/*Phone: Fix NPE in PhoneUtils::toLogSafePhoneNumber

When PhoneGlobals.DEBUG_LEVEL is set to 2, there
will be NullPointerException when receiving an
incoming call from an unknown number.

Change-Id:I2a730b891420695f3a410164e611b66cf757edc8Signed-off-by: Bin Li <libin@marvell.com>*/
//Synthetic comment -- diff --git a/src/com/android/phone/PhoneUtils.java b/src/com/android/phone/PhoneUtils.java
//Synthetic comment -- index b71ae35..0d98907 100644

//Synthetic comment -- @@ -758,6 +758,11 @@
}

private static String toLogSafePhoneNumber(String number) {
if (VDBG) {
// When VDBG is true we emit PII.
return number;







