/*Add comment
Signed-off-by: Le Van Khanh <khanhlvbka@gmail.com>

Change-Id:I65464dbdb1aa824204d0892c01c9a985b33761fc*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContentQueryMapTest.java b/tests/tests/content/src/android/content/cts/ContentQueryMapTest.java
//Synthetic comment -- index 086a27e..604b0d2 100644

//Synthetic comment -- @@ -266,15 +266,12 @@
Looper.loop();
}
}).start();
<<<<<<< HEAD
=======
	/*Start PHNL Modification*/
	try {
		Thread.sleep(2000);
	} catch(Exception ex) {
	}
	/*End PHNL Modification*/
>>>>>>> Add comment

// insert NAME3 and VALUE3
values = new ContentValues();
//Synthetic comment -- @@ -297,16 +294,12 @@
Looper.loop();
}
}).start();
<<<<<<< HEAD
=======
	/*Start PHNL Modification*/
	try {
		Thread.sleep(2000);
	} catch(Exception ex) {
	}
	/*End PHNL Modification*/	

>>>>>>> Add comment
// update NAME3 and VALUE3
values = new ContentValues();
values.put(DummyProvider.NAME, NAME0);







