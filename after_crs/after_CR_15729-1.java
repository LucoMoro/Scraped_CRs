/*fix NPE

Change-Id:I0d6840557864fef7b493914ed5ddf9c0aca55486*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ui/EditContactActivity.java b/src/com/android/contacts/ui/EditContactActivity.java
//Synthetic comment -- index c70cff6..3e248ea 100644

//Synthetic comment -- @@ -1370,6 +1370,9 @@

int value;
if (!skipAccountTypeCheck) {
            if (oneSource.accountType == null) {
                return 1;
            }
value = oneSource.accountType.compareTo(twoSource.accountType);
if (value != 0) {
return value;







