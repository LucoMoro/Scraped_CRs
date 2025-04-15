/*NullPointerException invoking Field.getModifiers

Change-Id:I56c5908d16458d36098ffa7e2dc8c3518280f2e3*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/io/ObjectInputStream.java b/luni/src/main/java/java/io/ObjectInputStream.java
//Synthetic comment -- index acf00af..449204f 100644

//Synthetic comment -- @@ -1090,7 +1090,7 @@

for (ObjectStreamField fieldDesc : fields) {
Field field = classDesc.getReflectionField(fieldDesc);
            if (field != null && Modifier.isTransient(field.getModifiers())) {
field = null; // No setting transient fields! (http://b/4471249)
}
// We may not have been able to find the field, or it may be transient, but we still







