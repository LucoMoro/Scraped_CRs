/*Fix deserialization of transient fields.

We need to read field data in the stream, but if the field is (now)
transient, we should just ignore the request to set it.

Bug: 4471249
Change-Id:I5336fdeaaef73e912a48be53af75fd9d1b29fccf*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/io/ObjectInputStream.java b/luni/src/main/java/java/io/ObjectInputStream.java
//Synthetic comment -- index 4541f1b..acf00af 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.security.PrivilegedAction;
import java.util.ArrayList;
//Synthetic comment -- @@ -1089,8 +1090,11 @@

for (ObjectStreamField fieldDesc : fields) {
Field field = classDesc.getReflectionField(fieldDesc);
            if (Modifier.isTransient(field.getModifiers())) {
                field = null; // No setting transient fields! (http://b/4471249)
            }
            // We may not have been able to find the field, or it may be transient, but we still
            // need to read the value and do the other checking...
try {
Class<?> type = fieldDesc.getTypeInternal();
if (type == byte.class) {








//Synthetic comment -- diff --git a/luni/src/main/java/java/io/ObjectStreamClass.java b/luni/src/main/java/java/io/ObjectStreamClass.java
//Synthetic comment -- index e87fcd4..a28489a 100644

//Synthetic comment -- @@ -481,16 +481,14 @@
Field field = fields[i];
int modifiers = field.getModifiers() & FIELD_MODIFIERS_MASK;

                boolean skip = Modifier.isPrivate(modifiers) &&
                        (Modifier.isTransient(modifiers) || Modifier.isStatic(modifiers));
if (!skip) {
// write name, modifier & "descriptor" of all but private
// static and private transient
output.writeUTF(field.getName());
output.writeInt(modifiers);
                    output.writeUTF(descriptorForFieldSignature(getFieldSignature(field)));
}
}









//Synthetic comment -- diff --git a/luni/src/test/java/libcore/java/io/SerializationTest.java b/luni/src/test/java/libcore/java/io/SerializationTest.java
//Synthetic comment -- index 434dd56..d452c11 100644

//Synthetic comment -- @@ -18,6 +18,8 @@

import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectStreamClass;
import java.io.ObjectStreamField;
import java.io.Serializable;
import junit.framework.TestCase;
import libcore.util.SerializationTester;
//Synthetic comment -- @@ -26,6 +28,13 @@

// http://b/4471249
public void testSerializeFieldMadeTransient() throws Exception {
        // Does ObjectStreamClass have the right idea?
        ObjectStreamClass osc = ObjectStreamClass.lookup(FieldMadeTransient.class);
        ObjectStreamField[] fields = osc.getFields();
        assertEquals(1, fields.length);
        assertEquals("nonTransientInt", fields[0].getName());
        assertEquals(int.class, fields[0].getType());

// this was created by serializing a FieldMadeTransient with a non-0 transientInt
String s = "aced0005737200346c6962636f72652e6a6176612e696f2e53657269616c697a6174696f6e54657"
+ "374244669656c644d6164655472616e7369656e74000000000000000002000149000c7472616e736"
//Synthetic comment -- @@ -37,6 +46,7 @@
static class FieldMadeTransient implements Serializable {
private static final long serialVersionUID = 0L;
private transient int transientInt;
        private int nonTransientInt;
}

public void testSerialVersionUidChange() throws Exception {







