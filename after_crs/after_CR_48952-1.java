/*Improve the Class documentation.

There were a few other instances of the same error reported in the bug,
plus bits that didn't make much sense, so I gave the whole file a quick
scrub.

Bug:http://code.google.com/p/android/issues/detail?id=42100Change-Id:I40669a614c92a80038253a4adf89f8c7f7f8e95c*/




//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Class.java b/luni/src/main/java/java/lang/Class.java
//Synthetic comment -- index b6183a5..f618b66 100644

//Synthetic comment -- @@ -148,18 +148,15 @@

/**
* Returns a {@code Class} object which represents the class with the
     * given name. The name should be the name of a non-primitive class, as described in
     * the {@link Class class definition}.
     * Primitive types can not be found using this method; use {@code int.class} or {@code Integer.TYPE} instead.
*
     * <p>If the class has not yet been loaded, it is loaded and initialized
     * first. This is done through either the class loader of the calling class
     * or one of its parent class loaders. It is possible that a static initializer is run as
     * a result of this call.
     *
* @throws ClassNotFoundException
*             if the requested class can not be found.
* @throws LinkageError
//Synthetic comment -- @@ -174,24 +171,14 @@

/**
* Returns a {@code Class} object which represents the class with the
     * given name. The name should be the name of a non-primitive class, as described in
     * the {@link Class class definition}.
     * Primitive types can not be found using this method; use {@code int.class} or {@code Integer.TYPE} instead.
*
     * <p>If the class has not yet been loaded, it is loaded first, using the given class loader.
     * If the class has not yet been initialized and {@code shouldInitialize} is true,
     * the class will be initialized.
     *
* @throws ClassNotFoundException
*             if the requested class can not be found.
* @throws LinkageError
//Synthetic comment -- @@ -200,7 +187,7 @@
*             if an exception occurs during static initialization of a
*             class.
*/
    public static Class<?> forName(String className, boolean shouldInitialize,
ClassLoader classLoader) throws ClassNotFoundException {

if (classLoader == null) {
//Synthetic comment -- @@ -209,12 +196,12 @@
// Catch an Exception thrown by the underlying native code. It wraps
// up everything inside a ClassNotFoundException, even if e.g. an
// Error occurred during initialization. This as a workaround for
        // an ExceptionInInitializerError that's also wrapped. It is actually
// expected to be thrown. Maybe the same goes for other errors.
// Not wrapping up all the errors will break android though.
Class<?> result;
try {
            result = classForName(className, shouldInitialize,
classLoader);
} catch (ClassNotFoundException e) {
Throwable cause = e.getCause();
//Synthetic comment -- @@ -226,17 +213,7 @@
return result;
}

    private static native Class<?> classForName(String className, boolean shouldInitialize,
ClassLoader classLoader) throws ClassNotFoundException;

/**
//Synthetic comment -- @@ -245,11 +222,17 @@
* members inherited from super classes and interfaces. If there are no such
* class members or if this object represents a primitive type then an array
* of length 0 is returned.
*/
public Class<?>[] getClasses() {
        Class<?>[] result = getDeclaredClasses(this, true);
        // Traverse all superclasses.
        for (Class<?> c = this.getSuperclass(); c != null; c = c.getSuperclass()) {
            Class<?>[] temp = getDeclaredClasses(c, true);
            if (temp.length != 0) {
                result = arraycopy(new Class[result.length + temp.length], result, temp);
            }
        }
        return result;
}

@Override public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
//Synthetic comment -- @@ -275,10 +258,9 @@
}

/**
     * Returns an array containing all the annotations of this class. If there are no annotations
* then an empty array is returned.
*
* @see #getDeclaredAnnotations()
*/
public Annotation[] getAnnotations() {
//Synthetic comment -- @@ -318,9 +300,6 @@
* Returns the canonical name of this class. If this class does not have a
* canonical name as defined in the Java Language Specification, then the
* method returns {@code null}.
*/
public String getCanonicalName() {
if (isLocalClass() || isAnonymousClass())
//Synthetic comment -- @@ -362,11 +341,8 @@
* Returns the class loader which was used to load the class represented by
* this {@code Class}. Implementations are free to return {@code null} for
* classes that were loaded by the bootstrap class loader. The Android
     * reference implementation, though, always returns a reference to an actual
     * class loader.
*/
public ClassLoader getClassLoader() {
if (this.isPrimitive()) {
//Synthetic comment -- @@ -388,8 +364,6 @@
* this Class without doing any security checks. The bootstrap ClassLoader
* is returned, unlike getClassLoader() which returns null in place of the
* bootstrap ClassLoader.
*/
ClassLoader getClassLoaderImpl() {
ClassLoader loader = getClassLoader(this);
//Synthetic comment -- @@ -398,30 +372,22 @@

/*
* Returns the defining class loader for the given class.
*/
    private static native ClassLoader getClassLoader(Class<?> c);

/**
* Returns a {@code Class} object which represents the component type if
* this class represents an array type. Returns {@code null} if this class
* does not represent an array type. The component type of an array type is
* the type of the elements of the array.
*/
public native Class<?> getComponentType();

/**
* Returns a {@code Constructor} object which represents the public
     * constructor matching the given parameter types.
     * {@code (Class[]) null} is equivalent to the empty array.
*
* @throws NoSuchMethodException
*             if the constructor can not be found.
* @see #getDeclaredConstructor(Class[])
//Synthetic comment -- @@ -432,14 +398,11 @@
}

/**
     * Returns a constructor or method with the given name. Use "<init>" to return a constructor.
*/
    private Member getConstructorOrMethod(String name, boolean searchSuperTypes,
boolean publicOnly, Class<?>[] parameterTypes) throws NoSuchMethodException {
        if (searchSuperTypes && !publicOnly) {
throw new AssertionError(); // can't lookup non-public members recursively
}
if (name == null) {
//Synthetic comment -- @@ -453,7 +416,7 @@
throw new NoSuchMethodException("parameter type is null");
}
}
        Member result = searchSuperTypes
? getPublicConstructorOrMethodRecursive(name, parameterTypes)
: Class.getDeclaredConstructorOrMethod(this, name, parameterTypes);
if (result == null || publicOnly && (result.getModifiers() & Modifier.PUBLIC) == 0) {
//Synthetic comment -- @@ -486,12 +449,10 @@

/**
* Returns an array containing {@code Constructor} objects for all public
     * constructors for this {@code Class}. If there
* are no public constructors or if this {@code Class} represents an array
* class, a primitive type or void then an empty array is returned.
*
* @see #getDeclaredConstructors()
*/
public Constructor<?>[] getConstructors() {
//Synthetic comment -- @@ -504,11 +465,9 @@
* included in the result. If there are no annotations at all, an empty
* array is returned.
*
* @see #getAnnotations()
*/
    public native Annotation[] getDeclaredAnnotations();

/**
* Returns the annotation if it exists.
//Synthetic comment -- @@ -526,58 +485,23 @@
* Class} represents. If there are no classes or interfaces declared or if
* this class represents an array class, a primitive type or void, then an
* empty array is returned.
*/
public Class<?>[] getDeclaredClasses() {
return getDeclaredClasses(this, false);
}

/*
     * Returns the list of member classes of the given class.
     * If no members exist, an empty array is returned.
*/
    private static native Class<?>[] getDeclaredClasses(Class<?> c, boolean publicOnly);

/**
* Returns a {@code Constructor} object which represents the constructor
     * matching the given parameter types that is declared by the class
* represented by this {@code Class}.
     * {@code (Class[]) null} is equivalent to the empty array.
*
* @throws NoSuchMethodException
*             if the requested constructor can not be found.
* @see #getConstructor(Class[])
//Synthetic comment -- @@ -592,10 +516,8 @@
* Returns an array containing {@code Constructor} objects for all
* constructors declared in the class represented by this {@code Class}. If
* there are no constructors or if this {@code Class} represents an array
     * class, a primitive type, or void then an empty array is returned.
*
* @see #getConstructors()
*/
public Constructor<?>[] getDeclaredConstructors() {
//Synthetic comment -- @@ -603,22 +525,15 @@
}

/*
     * Returns the list of constructors. If no constructors exist, an empty array is returned.
*/
    private static native <T> Constructor<T>[] getDeclaredConstructors(Class<T> c,
                                                                       boolean publicOnly);

/**
     * Returns a {@code Field} object for the field with the given name
* which is declared in the class represented by this {@code Class}.
*
* @throws NoSuchFieldException if the requested field can not be found.
* @see #getField(String)
*/
//Synthetic comment -- @@ -639,8 +554,6 @@
* if this {@code Class} represents an array class, a primitive type or void
* then an empty array is returned.
*
* @see #getFields()
*/
public Field[] getDeclaredFields() {
//Synthetic comment -- @@ -650,32 +563,23 @@
/*
* Returns the list of fields without performing any security checks
* first. If no fields exist at all, an empty array is returned.
*/
    static native Field[] getDeclaredFields(Class<?> c, boolean publicOnly);

/**
     * Returns the field if it is defined by {@code c}; null otherwise. This
* may return a non-public member.
*/
    static native Field getDeclaredField(Class<?> c, String name);

/**
* Returns a {@code Method} object which represents the method matching the
     * given name and parameter types that is declared by the class
* represented by this {@code Class}.
     * {@code (Class[]) null} is equivalent to the empty array.
*
* @throws NoSuchMethodException
     *             if the requested method can not be found.
* @throws NullPointerException
*             if {@code name} is {@code null}.
* @see #getMethod(String, Class[])
//Synthetic comment -- @@ -695,8 +599,6 @@
* methods or if this {@code Class} represents an array class, a primitive
* type or void then an empty array is returned.
*
* @see #getMethods()
*/
public Method[] getDeclaredMethods() {
//Synthetic comment -- @@ -704,58 +606,45 @@
}

/**
     * Returns the list of methods. If no methods exist, an empty array is returned.
*/
    static native Method[] getDeclaredMethods(Class<?> c, boolean publicOnly);

/**
     * Returns the constructor or method if it is defined by {@code c}; null
     * otherwise. This may return a non-public member. Use "<init>" to get a constructor.
*/
    static native Member getDeclaredConstructorOrMethod(Class c, String name, Class[] args);

/**
* Returns the declaring {@code Class} of this {@code Class}. Returns
* {@code null} if the class is not a member of another class or if this
     * {@code Class} represents an array class, a primitive type, or void.
*/
    public native Class<?> getDeclaringClass();

/**
* Returns the enclosing {@code Class} of this {@code Class}. If there is no
* enclosing class the method returns {@code null}.
*/
    public native Class<?> getEnclosingClass();

/**
     * Returns the enclosing {@code Constructor} of this {@code Class}, if it is an
* anonymous or local/automatic class; otherwise {@code null}.
*/
    public native Constructor<?> getEnclosingConstructor();

/**
     * Returns the enclosing {@code Method} of this {@code Class}, if it is an
* anonymous or local/automatic class; otherwise {@code null}.
*/
    public native Method getEnclosingMethod();

/**
     * Returns the {@code enum} constants associated with this {@code Class}.
* Returns {@code null} if this {@code Class} does not represent an {@code
* enum} type.
*/
@SuppressWarnings("unchecked") // we only cast after confirming that this class is an enum
public T[] getEnumConstants() {
//Synthetic comment -- @@ -767,13 +656,10 @@

/**
* Returns a {@code Field} object which represents the public field with the
     * given name. This method first searches the class C represented by
* this {@code Class}, then the interfaces implemented by C and finally the
* superclasses of C.
*
* @throws NoSuchFieldException
*             if the field can not be found.
* @see #getDeclaredField(String)
//Synthetic comment -- @@ -820,8 +706,6 @@
* <p>If there are no public fields or if this class represents an array class,
* a primitive type or {@code void} then an empty array is returned.
*
* @see #getDeclaredFields()
*/
public Field[] getFields() {
//Synthetic comment -- @@ -829,7 +713,7 @@
getPublicFieldsRecursive(fields);

/*
         * The result may include duplicates when this class implements an interface
* through multiple paths. Remove those duplicates.
*/
CollectionUtils.removeDuplicates(fields, Field.ORDER_BY_NAME_AND_DECLARING_CLASS);
//Synthetic comment -- @@ -857,12 +741,9 @@
}

/**
     * Returns the {@link Type}s of the interfaces that this {@code Class} directly
* implements. If the {@code Class} represents a primitive type or {@code
* void} then an empty array is returned.
*/
public Type[] getGenericInterfaces() {
GenericSignatureParser parser = new GenericSignatureParser(getClassLoader());
//Synthetic comment -- @@ -871,10 +752,8 @@
}

/**
     * Returns the {@code Type} that represents the superclass of this {@code
* class}.
*/
public Type getGenericSuperclass() {
GenericSignatureParser parser = new GenericSignatureParser(getClassLoader());
//Synthetic comment -- @@ -884,29 +763,22 @@

/**
* Returns an array of {@code Class} objects that match the interfaces
     * in the {@code implements} declaration of the class represented
* by this {@code Class}. The order of the elements in the array is
* identical to the order in the original class declaration. If the class
* does not implement any interfaces, an empty array is returned.
*/
public native Class<?>[] getInterfaces();

/**
* Returns a {@code Method} object which represents the public method with
     * the given name and parameter types.
     * {@code (Class[]) null} is equivalent to the empty array.
     * This method first searches the
* class C represented by this {@code Class}, then the superclasses of C and
* finally the interfaces implemented by C and finally the superclasses of C
* for a method with matching name.
*
* @throws NoSuchMethodException
*             if the method can not be found.
* @see #getDeclaredMethod(String, Class[])
//Synthetic comment -- @@ -924,13 +796,10 @@
* for the class C represented by this {@code Class}. Methods may be
* declared in C, the interfaces it implements or in the superclasses of C.
* The elements in the returned array are in no particular order.
*
     * <p>If there are no public methods or if this {@code Class} represents a
     * primitive type or {@code void} then an empty array is returned.
     *
* @see #getDeclaredMethods()
*/
public Method[] getMethods() {
//Synthetic comment -- @@ -946,7 +815,7 @@
}

/**
     * Populates {@code result} with public methods defined by this class, its
* superclasses, and all implemented interfaces, including overridden methods.
*/
private void getPublicMethodsRecursive(List<Method> result) {
//Synthetic comment -- @@ -969,18 +838,15 @@
* Returns an integer that represents the modifiers of the class represented
* by this {@code Class}. The returned value is a combination of bits
* defined by constants in the {@link Modifier} class.
*/
public int getModifiers() {
return getModifiers(this, false);
}

/*
     * Returns the modifiers for the given class.
*
     * {@code ignoreInnerClassesAttrib} determines whether we look for and use the
*     flags from an "inner class" attribute
*/
private static native int getModifiers(Class<?> clazz, boolean ignoreInnerClassesAttrib);
//Synthetic comment -- @@ -989,8 +855,6 @@
* Returns the name of the class represented by this {@code Class}. For a
* description of the format which is used, see the class definition of
* {@link Class}.
*/
public String getName() {
String result = name;
//Synthetic comment -- @@ -1033,8 +897,6 @@

/*
* Returns the simple name of a member or local class, or null otherwise.
*/
private native String getInnerClassName();

//Synthetic comment -- @@ -1046,20 +908,15 @@
}

/**
     * Returns the URL of the given resource, or null if the resource is not found.
     * The mapping between the resource name and the URL is managed by the class' class loader.
*
* @see ClassLoader
*/
    public URL getResource(String resourceName) {
// Get absolute resource name, but without the leading slash
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
} else {
String pkg = getName();
int dot = pkg.lastIndexOf('.');
//Synthetic comment -- @@ -1069,33 +926,29 @@
pkg = "";
}

            resourceName = pkg + "/" + resourceName;
}

// Delegate to proper class loader
ClassLoader loader = getClassLoader();
if (loader != null) {
            return loader.getResource(resourceName);
} else {
            return ClassLoader.getSystemResource(resourceName);
}
}

/**
     * Returns a read-only stream for the contents of the given resource, or null if the resource
     * is not found.
     * The mapping between the resource name and the stream is managed by the class' class loader.
*
* @see ClassLoader
*/
    public InputStream getResourceAsStream(String resourceName) {
// Get absolute resource name, but without the leading slash
        if (resourceName.startsWith("/")) {
            resourceName = resourceName.substring(1);
} else {
String pkg = getName();
int dot = pkg.lastIndexOf('.');
//Synthetic comment -- @@ -1105,15 +958,15 @@
pkg = "";
}

            resourceName = pkg + "/" + resourceName;
}

// Delegate to proper class loader
ClassLoader loader = getClassLoader();
if (loader != null) {
            return loader.getResourceAsStream(resourceName);
} else {
            return ClassLoader.getSystemResourceAsStream(resourceName);
}
}

//Synthetic comment -- @@ -1122,8 +975,6 @@
* All classes from any given dex file will have the same signers, but different dex
* files may have different signers. This does not fit well with the original
* {@code ClassLoader}-based model of {@code getSigners}.)
*/
public Object[] getSigners() {
// See http://code.google.com/p/android/issues/detail?id=1766.
//Synthetic comment -- @@ -1136,8 +987,6 @@
* the {@code Object} class, a primitive type, an interface or void then the
* method returns {@code null}. If this {@code Class} represents an array
* class then the {@code Object} class is returned.
*/
public native Class<? super T> getSuperclass();

//Synthetic comment -- @@ -1145,9 +994,6 @@
* Returns an array containing {@code TypeVariable} objects for type
* variables declared by the generic class represented by this {@code
* Class}. Returns an empty array if the class is not generic.
*/
@SuppressWarnings("unchecked")
public synchronized TypeVariable<Class<T>>[] getTypeParameters() {
//Synthetic comment -- @@ -1157,10 +1003,7 @@
}

/**
     * Tests whether this {@code Class} represents an annotation class.
*/
public boolean isAnnotation() {
final int ACC_ANNOTATION = 0x2000;  // not public in reflect.Modifiers
//Synthetic comment -- @@ -1189,59 +1032,43 @@
}

/**
     * Tests whether the class represented by this {@code Class} is
     * anonymous.
*/
native public boolean isAnonymousClass();

/**
     * Tests whether the class represented by this {@code Class} is an array class.
*/
public boolean isArray() {
return getComponentType() != null;
}

/**
     * Tests whether the given class type can be converted to the class
* represented by this {@code Class}. Conversion may be done via an identity
* conversion or a widening reference conversion (if either the receiver or
* the argument represent primitive types, only the identity conversion
* applies).
*
* @throws NullPointerException
     *             if {@code c} is {@code null}.
*/
    public native boolean isAssignableFrom(Class<?> c);

/**
     * Tests whether the class represented by this {@code Class} is an
* {@code enum}.
*/
public boolean isEnum() {
return ((getModifiers() & 0x4000) != 0) && (getSuperclass() == Enum.class);
}

/**
     * Tests whether the given object can be cast to the class
* represented by this {@code Class}. This is the runtime version of the
* {@code instanceof} operator.
*
* @return {@code true} if {@code object} can be cast to the type
*         represented by this {@code Class}; {@code false} if {@code
*         object} is {@code null} or cannot be cast.
//Synthetic comment -- @@ -1249,19 +1076,13 @@
public native boolean isInstance(Object object);

/**
     * Tests whether this {@code Class} represents an interface.
*/
public native boolean isInterface();

/**
     * Tests whether the class represented by this {@code Class} is defined
* locally.
*/
public boolean isLocalClass() {
boolean enclosed = (getEnclosingMethod() != null ||
//Synthetic comment -- @@ -1270,29 +1091,20 @@
}

/**
     * Tests whether the class represented by this {@code Class} is a member
* class.
*/
public boolean isMemberClass() {
return getDeclaringClass() != null;
}

/**
     * Tests whether this {@code Class} represents a primitive type.
*/
public native boolean isPrimitive();

/**
     * Tests whether this {@code Class} represents a synthetic type.
*/
public boolean isSynthetic() {
final int ACC_SYNTHETIC = 0x1000;   // not public in reflect.Modifiers
//Synthetic comment -- @@ -1309,7 +1121,6 @@
* constructor exists but is not accessible from the context where this
* method is invoked, an {@code IllegalAccessException} is thrown.
*
* @throws IllegalAccessException
*             if the default constructor is not visible.
* @throws InstantiationException
//Synthetic comment -- @@ -1334,9 +1145,6 @@
* Returns the {@code Package} of which the class represented by this
* {@code Class} is a member. Returns {@code null} if no {@code Package}
* object was created by the class loader of the class.
*/
public Package getPackage() {
// TODO This might be a hack, but the VM doesn't have the necessary info.
//Synthetic comment -- @@ -1353,42 +1161,33 @@
* Returns the assertion status for the class represented by this {@code
* Class}. Assertion is enabled / disabled based on the class loader,
* package or class default at runtime.
*/
public native boolean desiredAssertionStatus();

/**
     * Casts this {@code Class} to represent a subclass of the given class.
* If successful, this {@code Class} is returned; otherwise a {@code
* ClassCastException} is thrown.
*
* @throws ClassCastException
     *             if this {@code Class} cannot be cast to the given type.
*/
@SuppressWarnings("unchecked")
    public <U> Class<? extends U> asSubclass(Class<U> c) {
        if (c.isAssignableFrom(this)) {
return (Class<? extends U>)this;
}
String actualClassName = this.getName();
        String desiredClassName = c.getName();
throw new ClassCastException(actualClassName + " cannot be cast to " + desiredClassName);
}

/**
     * Casts the given object to the type represented by this {@code Class}.
* If the object is {@code null} then the result is also {@code null}.
*
* @throws ClassCastException
     *             if the object cannot be cast to the given type.
*/
@SuppressWarnings("unchecked")
public T cast(Object obj) {







