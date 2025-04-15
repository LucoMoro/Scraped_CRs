/*Fix for bug 1185.
The constructor IntentFilter(String action, String dataType) ignores the action parameter.
This change ensures that it is correctly added to the set of actions.*/
//Synthetic comment -- diff --git a/core/java/android/content/IntentFilter.java b/core/java/android/content/IntentFilter.java
//Synthetic comment -- index e81bc86..b151de5 100644

//Synthetic comment -- @@ -351,6 +351,7 @@
throws MalformedMimeTypeException {
mPriority = 0;
mActions = new ArrayList<String>();
addDataType(dataType);
}








