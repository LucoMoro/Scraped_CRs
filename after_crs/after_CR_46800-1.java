/*Fix infinite recursive loop

FindBugs description:

There is an apparent recursive loop at IntProperty.java
in method set(Object, Integer)

This method unconditionally invokes itself. This would seem
to indicate an infinite recursive loop that will result in a stack overflow.

Change-Id:I2f52dd3689198cb948925aa65dd9c95be7888fe7*/




//Synthetic comment -- diff --git a/core/java/android/util/IntProperty.java b/core/java/android/util/IntProperty.java
//Synthetic comment -- index 459d6b2..17977ca 100644

//Synthetic comment -- @@ -42,7 +42,7 @@

@Override
final public void set(T object, Integer value) {
        setValue(object, value.intValue());
}

}
\ No newline at end of file







