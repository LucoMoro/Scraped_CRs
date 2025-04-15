/*test

Change-Id:I7b2e1b730fabd9b433454c4a47b2453b2b5a3e65*/
//Synthetic comment -- diff --git a/tests/tests/content/src/android/content/cts/ContentQueryMapTest.java b/tests/tests/content/src/android/content/cts/ContentQueryMapTest.java
//Synthetic comment -- index d5d9666..086a27e 100644

//Synthetic comment -- @@ -266,6 +266,15 @@
Looper.loop();
}
}).start();

// insert NAME3 and VALUE3
values = new ContentValues();
//Synthetic comment -- @@ -288,6 +297,16 @@
Looper.loop();
}
}).start();
// update NAME3 and VALUE3
values = new ContentValues();
values.put(DummyProvider.NAME, NAME0);







