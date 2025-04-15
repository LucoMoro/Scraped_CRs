/*Typo fix in javadoc

spec -> otherSpec*/




//Synthetic comment -- diff --git a/dx/src/com/android/dx/dex/code/LocalList.java b/dx/src/com/android/dx/dex/code/LocalList.java
//Synthetic comment -- index 4614fc4..fb65997 100644

//Synthetic comment -- @@ -278,9 +278,9 @@
/**
* Returns whether or not this instance matches the given spec.
* 
         * @param otherSpec non-null; the spec in question
* @return <code>true</code> iff this instance matches
         * <code>otherSpec</code>
*/
public boolean matches(RegisterSpec otherSpec) {
return spec.equalsUsingSimpleType(otherSpec);







