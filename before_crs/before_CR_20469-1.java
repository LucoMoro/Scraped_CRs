/*Fix build by updating ide-common to the new LayoutLog.

Change-Id:If1a01daa87561f474b4c43d6c404a3c42e98d485*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java b/ide_common/src/com/android/ide/common/rendering/LayoutLibrary.java
//Synthetic comment -- index e1ce53b..a757c20 100644

//Synthetic comment -- @@ -346,15 +346,15 @@
ILayoutLog logWrapper = new ILayoutLog() {

public void warning(String message) {
                log.warning(null, message);
}

public void error(Throwable t) {
                log.error(null, "error!", t);
}

public void error(String message) {
                log.error(null, message);
}
};








