/*Phone: Control operator selection using EF_CSP data.

As per CPHS4_2.WW6, CPHS B.4.7.1, the most significant bit of
Value Added Services Group(0xC0) info, controls operator selection menu.
   -If this bit is set, display operator selection menu to user.
   -If this bit is not set do not display operator selection menu to user,
	 set Network Selection Mode to Automatic.

Change-Id:I6d0172831192de0cbe5d876fa25810caf0906b0a*/
//Synthetic comment -- diff --git a/src/com/android/phone/GsmUmtsOptions.java b/src/com/android/phone/GsmUmtsOptions.java
//Synthetic comment -- index 223f4f7..98fb5dc 100644

//Synthetic comment -- @@ -58,6 +58,16 @@
mButtonAPNExpand.setEnabled(false);
mButtonOperatorSelectionExpand.setEnabled(false);
mButtonPrefer2g.setEnabled(false);
}
}








