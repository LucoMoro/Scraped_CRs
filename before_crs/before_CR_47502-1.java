/*Synchronize calls to Matcher.closeImpl.

Bug: 7603088
Change-Id:I4ed42342438e4e74eecd2e4013be0c85e9b2ef29*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/util/regex/Matcher.java b/luni/src/main/java/java/util/regex/Matcher.java
//Synthetic comment -- index cfd4432..ce2dfe7 100644

//Synthetic comment -- @@ -229,11 +229,13 @@

this.pattern = pattern;

        if (address != 0) {
            closeImpl(address);
            address = 0;
}
        address = openImpl(pattern.address);

if (input != null) {
resetForInput();
//Synthetic comment -- @@ -634,7 +636,9 @@

@Override protected void finalize() throws Throwable {
try {
            closeImpl(address);
} finally {
super.finalize();
}







