/*Fix NPE

Change-Id:I7dbe5705b76ba54dc0dd9bc00051406e83ed7df6*/
//Synthetic comment -- diff --git a/src/com/android/contacts/ui/EditContactActivity.java b/src/com/android/contacts/ui/EditContactActivity.java
//Synthetic comment -- index c70cff6..3e248ea 100644

//Synthetic comment -- @@ -1370,6 +1370,9 @@

int value;
if (!skipAccountTypeCheck) {
value = oneSource.accountType.compareTo(twoSource.accountType);
if (value != 0) {
return value;







