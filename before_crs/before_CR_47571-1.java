/*email: "Exchange Services has stopped" - Rain of Exchange Javacrashes

Cleanup some nasty records containing NULL fields.

Change-Id:Iec5e7e1bebc4beee8fa010c3f9c9ee723fd2cd83Author: Emmanuel Berthier <emmanuel.berthier@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 60461*/
//Synthetic comment -- diff --git a/src/com/android/email/provider/DBHelper.java b/src/com/android/email/provider/DBHelper.java
//Synthetic comment -- index 6721569..974906a 100644

//Synthetic comment -- @@ -531,6 +531,16 @@

@Override
public void onOpen(SQLiteDatabase db) {
}
}








