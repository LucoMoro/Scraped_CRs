/*traceview: use Matcher.find() instead of Matcher.matches()

Fixeshttp://code.google.com/p/android/issues/detail?id=37779Change-Id:I8c1671d24caed3156b98dcb36d345c0eb1f573d7*/
//Synthetic comment -- diff --git a/traceview/src/com/android/traceview/ProfileProvider.java b/traceview/src/com/android/traceview/ProfileProvider.java
//Synthetic comment -- index a38e61b..0492009 100644

//Synthetic comment -- @@ -90,7 +90,7 @@

private MethodData doMatchName(String name, int startIndex) {
// Check if the given "name" has any uppercase letters
        boolean hasUpper = mUppercase.matcher(name).matches();
for (int ii = startIndex; ii < mRoots.length; ++ii) {
MethodData md = mRoots[ii];
String fullName = md.getName();







