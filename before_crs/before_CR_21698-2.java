/*Fixing wrong Japanese phone formatter.http://code.google.com/p/android/issues/detail?id=15383Adding rules that Japanese number start with 050 and 060.

Change-Id:Id324d7ecfb18b348230a3903b356647045700d80*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/JapanesePhoneNumberFormatter.java b/telephony/java/android/telephony/JapanesePhoneNumberFormatter.java
//Synthetic comment -- index 6390d8e..f5e53ef 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
*
* 022-229-1234 0223-23-1234 022-301-9876 015-482-7849 0154-91-3478
* 01547-5-4534 090-1234-1234 080-0123-6789
* 0800-000-9999 0570-000-000 0276-00-0000
*
* As you can see, there is no straight-forward rule here.
//Synthetic comment -- @@ -31,7 +32,7 @@
*/
/* package */ class JapanesePhoneNumberFormatter {
private static short FORMAT_MAP[] = {
    -100, 10, 220, -15, 410, 530, -15, 670, 780, 1060,
-100, -25, 20, 40, 70, 100, 150, 190, 200, 210,
-36, -100, -100, -35, -35, -35, 30, -100, -100, -100,
-35, -35, -35, -35, -35, -35, -35, -45, -35, -35,
//Synthetic comment -- @@ -84,7 +85,7 @@
-35, -25, -25, -25, -25, -25, -25, -25, -25, -25,
-25, -25, -25, -35, -35, -35, -25, -25, -25, 520,
-100, -100, -45, -100, -45, -100, -45, -100, -45, -100,
    -25, -100, -25, 540, 580, 590, 600, 610, 630, 640,
-25, -35, -35, -35, -25, -25, -35, -35, -35, 550,
-35, -35, -25, -25, -25, -25, 560, 570, -25, -35,
-35, -35, -35, -35, -25, -25, -25, -25, -25, -25,
//Synthetic comment -- @@ -150,7 +151,8 @@
-35, 1170, -25, -35, 1180, -35, 1190, -35, -25, -25,
-100, -100, -45, -45, -100, -100, -100, -100, -100, -100,
-25, -35, -35, -35, -35, -35, -35, -25, -25, -35,
    -35, -35, -35, -35, -35, -35, -35, -35, -35, -45};

public static void format(Editable text) {
// Here, "root" means the position of "'":







