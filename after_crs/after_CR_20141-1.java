/*Fixed crash when joining two contacts

When joining contacts which do not have an associated account type
a NullPointerException will be thrown because of an illegal
comparison. This fix makes sure that the account type is not used as
a basis for comparison if it has an illegal value (null).

Change-Id:Ifd2bae4f8c5f14a569dc525772e24fb4ba3271ff*/




//Synthetic comment -- diff --git a/src/com/android/contacts/ui/EditContactActivity.java b/src/com/android/contacts/ui/EditContactActivity.java
//Synthetic comment -- index e37aade..7202da8 100644

//Synthetic comment -- @@ -1385,6 +1385,8 @@
return 1;
} else if (oneIsGoogle && twoIsGoogle){
skipAccountTypeCheck = true;
        } else if (oneSource.accountType == null || twoSource.accountType == null) {
            skipAccountTypeCheck = true;
}

int value;







