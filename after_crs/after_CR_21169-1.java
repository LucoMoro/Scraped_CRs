/*Bypass testFormatMethods if device is not using en_US resource.

Change-Id:I915e80cbca27eef563a7242b71a164be9ed90401*/




//Synthetic comment -- diff --git a/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java b/tests/tests/permission/src/android/permission/cts/NoCallPermissionTest.java
deleted file mode 100644
//Synthetic comment -- index 88d5f1c..0000000

//Synthetic comment -- @@ -1,96 +0,0 @@








//Synthetic comment -- diff --git a/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java b/tests/tests/text/src/android/text/format/cts/DateUtilsTest.java
//Synthetic comment -- index a5dd335..c5ed7d5 100644

//Synthetic comment -- @@ -203,6 +203,13 @@
return;
}


	String usingCountryForResource = mContext.getResources().getConfiguration().locale.getCountry();
	if(!Locale.US.getCountry().equals(usingCountryForResource)) {
		//only test on en_US
		return;
	}

long elapsedTime = 2 * 60 * 60;
String expected = "2:00:00";
assertEquals(expected, DateUtils.formatElapsedTime(elapsedTime));







