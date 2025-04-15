/*Potential NPE in Account.

Also, avoid calling close() if the cursor is already closed.
Signed-off-by: Sylvain Becuwe <sylvain.becuwe@gmail.com>

Change-Id:I5e077a03e437721b8fc7ffb9af284eaa405d901e*/




//Synthetic comment -- diff --git a/emailcommon/src/com/android/emailcommon/provider/Account.java b/emailcommon/src/com/android/emailcommon/provider/Account.java
//Synthetic comment -- index 97a6f12..230e0ee 100755

//Synthetic comment -- @@ -574,7 +574,7 @@
return c.getLong(Account.ID_PROJECTION_COLUMN);
}
} finally {
            if (c != null && !c.isClosed()) c.close();
}
return Account.NO_ACCOUNT;
}







