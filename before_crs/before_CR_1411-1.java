/*Use a typed comparison of floating point field values rather than just a string comparison.
This addresses toolchain issues; seehttp://code.google.com/p/android/issues/detail?id=994*/
//Synthetic comment -- diff --git a/tools/apicheck/src/com/android/apicheck/FieldInfo.java b/tools/apicheck/src/com/android/apicheck/FieldInfo.java
//Synthetic comment -- index 9b467af..a962af5 100644

//Synthetic comment -- @@ -66,6 +66,40 @@
return parentQName + name();
}

public boolean isConsistent(FieldInfo fInfo) {
fInfo.mExistsInBoth = true;
mExistsInBoth = true;
//Synthetic comment -- @@ -75,8 +109,8 @@
"Field " + fInfo.qualifiedName() + " has changed type");
consistent = false;
}
      if ((mValue != null && !mValue.equals(fInfo.mValue)) || 
          (mValue == null && fInfo.mValue != null)) {
Errors.error(Errors.CHANGED_VALUE, fInfo.position(),
"Field " + fInfo.qualifiedName() + " has changed value from "
+ mValue + " to " + fInfo.mValue);







