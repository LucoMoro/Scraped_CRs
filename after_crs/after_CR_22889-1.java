/*Update layoutlib api level to 6.

This will make earlier ADT versions force the user to update.

Change-Id:I5331da72306453647f8d48ddb3b5d4cd4e662550*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Bridge.java
//Synthetic comment -- index ad2dd38..e21d039 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
*/
public abstract class Bridge {

    public final static int API_CURRENT = 6;

/**
* Returns the API level of the layout library.







