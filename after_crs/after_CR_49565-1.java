/*Makes the duplicate resource error friendlier

In the duplicate resource error, adds both offending files and lines.

Change-Id:I05d3e3b6ccc0e4d2027ea1f3ed1a65feb9103ced*/




//Synthetic comment -- diff --git a/builder/src/main/java/com/android/builder/resources/DuplicateResourceException.java b/builder/src/main/java/com/android/builder/resources/DuplicateResourceException.java
//Synthetic comment -- index 94593af..3bfdfdc 100644

//Synthetic comment -- @@ -25,6 +25,9 @@
private Resource mTwo;

DuplicateResourceException(Resource one, Resource two) {
        super(String.format("Duplicate resources: %1s:%2s, %3s:%4s",
                one.getSource().getFile().getAbsolutePath(), one.getKey(),
                two.getSource().getFile().getAbsolutePath(), two.getKey()));
mOne = one;
mTwo = two;
}







