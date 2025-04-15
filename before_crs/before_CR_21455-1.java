/*Adding a rule that Japanese FMC number start with 060.

[FMC: Fixed Mobile Convergence]

Change-Id:I9ceaa37d5fcff81be9070cb153e62f9fc1b40f8c*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/JapanesePhoneNumberFormatter.java b/telephony/java/android/telephony/JapanesePhoneNumberFormatter.java
//Synthetic comment -- index 6390d8e..06a442a 100644

//Synthetic comment -- @@ -31,7 +31,7 @@
*/
/* package */ class JapanesePhoneNumberFormatter {
private static short FORMAT_MAP[] = {
    -100, 10, 220, -15, 410, 530, -15, 670, 780, 1060,
-100, -25, 20, 40, 70, 100, 150, 190, 200, 210,
-36, -100, -100, -35, -35, -35, 30, -100, -100, -100,
-35, -35, -35, -35, -35, -35, -35, -45, -35, -35,
//Synthetic comment -- @@ -150,7 +150,8 @@
-35, 1170, -25, -35, 1180, -35, 1190, -35, -25, -25,
-100, -100, -45, -45, -100, -100, -100, -100, -100, -100,
-25, -35, -35, -35, -35, -35, -35, -25, -25, -35,
    -35, -35, -35, -35, -35, -35, -35, -35, -35, -45};

public static void format(Editable text) {
// Here, "root" means the position of "'":







