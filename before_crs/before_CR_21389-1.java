/*Adding a rule that Japanese IP-phone number start with 050.

Change-Id:I45a12833b36040bd5a4471f97c507f7fe0a4789e*/
//Synthetic comment -- diff --git a/telephony/java/android/telephony/JapanesePhoneNumberFormatter.java b/telephony/java/android/telephony/JapanesePhoneNumberFormatter.java
//Synthetic comment -- index 6390d8e..3cbf2df3 100644

//Synthetic comment -- @@ -24,6 +24,7 @@
*
* 022-229-1234 0223-23-1234 022-301-9876 015-482-7849 0154-91-3478
* 01547-5-4534 090-1234-1234 080-0123-6789
* 0800-000-9999 0570-000-000 0276-00-0000
*
* As you can see, there is no straight-forward rule here.
//Synthetic comment -- @@ -84,7 +85,7 @@
-35, -25, -25, -25, -25, -25, -25, -25, -25, -25,
-25, -25, -25, -35, -35, -35, -25, -25, -25, 520,
-100, -100, -45, -100, -45, -100, -45, -100, -45, -100,
    -25, -100, -25, 540, 580, 590, 600, 610, 630, 640,
-25, -35, -35, -35, -25, -25, -35, -35, -35, 550,
-35, -35, -25, -25, -25, -25, 560, 570, -25, -35,
-35, -35, -35, -35, -25, -25, -25, -25, -25, -25,







