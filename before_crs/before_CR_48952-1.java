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
     * specified name. The name should be the name of a class as described in
     * the {@link Class class definition}; however, {@code Class}es representing
     * primitive types can not be found using this method.
     * <p>
     * If the class has not been loaded so far, it is being loaded and linked
     * first. This is done through either the class loader of the calling class
     * or one of its parent class loaders. The class is also being initialized,
     * which means that a possible static initializer block is executed.
*
     * @param className
     *            the name of the non-primitive-type class to find.
     * @return the named {@code Class} instance.
* @throws ClassNotFoundException
*             if the requested class can not be found.
* @throws LinkageError
//Synthetic comment -- @@ -174,24 +171,14 @@

/**
* Returns a {@code Class} object which represents the class with the
     * specified name. The name should be the name of a class as described in
     * the {@link Class class definition}, however {@code Class}es representing
     * primitive types can not be found using this method. Security rules will
     * be obeyed.
     * <p>
     * If the class has not been loaded so far, it is being loaded and linked
     * first. This is done through either the specified class loader or one of
     * its parent class loaders. The caller can also request the class to be
     * initialized, which means that a possible static initializer block is
     * executed.
*
     * @param className
     *            the name of the non-primitive-type class to find.
     * @param initializeBoolean
     *            indicates whether the class should be initialized.
     * @param classLoader
     *            the class loader to use to load the class.
     * @return the named {@code Class} instance.
* @throws ClassNotFoundException
*             if the requested class can not be found.
* @throws LinkageError
//Synthetic comment -- @@ -200,7 +187,7 @@
*             if an exception occurs during static initialization of a
*             class.
*/
    public static Class<?> forName(String className, boolean initializeBoolean,
ClassLoader classLoader) throws ClassNotFoundException {

if (classLoader == null) {
//Synthetic comment -- @@ -209,12 +196,12 @@
// Catch an Exception thrown by the underlying native code. It wraps
// up everything inside a ClassNotFoundException, even if e.g. an
// Error occurred during initialization. This as a workaround for
        // an ExceptionInInitilaizerError that's also wrapped. It is actually
// expected to be thrown. Maybe the same goes for other errors.
// Not wrapping up all the errors will break android though.
Class<?> result;
try {
            result = classForName(className, initializeBoolean,
classLoader);
} catch (ClassNotFoundException e) {
Throwable cause = e.getCause();
//Synthetic comment -- @@ -226,17 +213,7 @@
return result;
}

    /*
     * Returns a class by name without any security checks.
     *
     * @param className The name of the non-primitive type class to find
     * @param initializeBoolean A boolean indicating whether the class should be
     *        initialized
     * @param classLoader The class loader to use to load the class
     * @return the named class.
     * @throws ClassNotFoundException If the class could not be found
     */
    static native Class<?> classForName(String className, boolean initializeBoolean,
ClassLoader classLoader) throws ClassNotFoundException;

/**
//Synthetic comment -- @@ -245,11 +222,17 @@
* members inherited from super classes and interfaces. If there are no such
* class members or if this object represents a primitive type then an array
* of length 0 is returned.
     *
     * @return the public class members of the class represented by this object.
*/
public Class<?>[] getClasses() {
        return getFullListOfClasses(true);
}

@Override public <A extends Annotation> A getAnnotation(Class<A> annotationType) {
//Synthetic comment -- @@ -275,10 +258,9 @@
}

/**
     * Returns all the annotations of this class. If there are no annotations
* then an empty array is returned.
*
     * @return a copy of the array containing this class' annotations.
* @see #getDeclaredAnnotations()
*/
public Annotation[] getAnnotations() {
//Synthetic comment -- @@ -318,9 +300,6 @@
* Returns the canonical name of this class. If this class does not have a
* canonical name as defined in the Java Language Specification, then the
* method returns {@code null}.
     *
     * @return this class' canonical name, or {@code null} if it does not have a
     *         canonical name.
*/
public String getCanonicalName() {
if (isLocalClass() || isAnonymousClass())
//Synthetic comment -- @@ -362,11 +341,8 @@
* Returns the class loader which was used to load the class represented by
* this {@code Class}. Implementations are free to return {@code null} for
* classes that were loaded by the bootstrap class loader. The Android
     * reference implementation, though, returns a reference to an actual
     * representation of the bootstrap class loader.
     *
     * @return the class loader for the represented class.
     * @see ClassLoader
*/
public ClassLoader getClassLoader() {
if (this.isPrimitive()) {
//Synthetic comment -- @@ -388,8 +364,6 @@
* this Class without doing any security checks. The bootstrap ClassLoader
* is returned, unlike getClassLoader() which returns null in place of the
* bootstrap ClassLoader.
     *
     * @return the ClassLoader
*/
ClassLoader getClassLoaderImpl() {
ClassLoader loader = getClassLoader(this);
//Synthetic comment -- @@ -398,30 +372,22 @@

/*
* Returns the defining class loader for the given class.
     *
     * @param clazz the class the class loader of which we want
     * @return the class loader
*/
    private static native ClassLoader getClassLoader(Class<?> clazz);

/**
* Returns a {@code Class} object which represents the component type if
* this class represents an array type. Returns {@code null} if this class
* does not represent an array type. The component type of an array type is
* the type of the elements of the array.
     *
     * @return the component type of this class.
*/
public native Class<?> getComponentType();

/**
* Returns a {@code Constructor} object which represents the public
     * constructor matching the specified parameter types.
*
     * @param parameterTypes
     *            the parameter types of the requested constructor.
     *            {@code (Class[]) null} is equivalent to the empty array.
     * @return the constructor described by {@code parameterTypes}.
* @throws NoSuchMethodException
*             if the constructor can not be found.
* @see #getDeclaredConstructor(Class[])
//Synthetic comment -- @@ -432,14 +398,11 @@
}

/**
     * Returns a constructor or method with the specified name.
     *
     * @param name the method name, or "<init>" to return a constructor.
     * @param recursive true to search supertypes.
*/
    private Member getConstructorOrMethod(String name, boolean recursive,
boolean publicOnly, Class<?>[] parameterTypes) throws NoSuchMethodException {
        if (recursive && !publicOnly) {
throw new AssertionError(); // can't lookup non-public members recursively
}
if (name == null) {
//Synthetic comment -- @@ -453,7 +416,7 @@
throw new NoSuchMethodException("parameter type is null");
}
}
        Member result = recursive
? getPublicConstructorOrMethodRecursive(name, parameterTypes)
: Class.getDeclaredConstructorOrMethod(this, name, parameterTypes);
if (result == null || publicOnly && (result.getModifiers() & Modifier.PUBLIC) == 0) {
//Synthetic comment -- @@ -486,12 +449,10 @@

/**
* Returns an array containing {@code Constructor} objects for all public
     * constructors for the class represented by this {@code Class}. If there
* are no public constructors or if this {@code Class} represents an array
* class, a primitive type or void then an empty array is returned.
*
     * @return an array with the public constructors of the class represented by
     *         this {@code Class}.
* @see #getDeclaredConstructors()
*/
public Constructor<?>[] getConstructors() {
//Synthetic comment -- @@ -504,11 +465,9 @@
* included in the result. If there are no annotations at all, an empty
* array is returned.
*
     * @return a copy of the array containing the annotations defined for the
     *         class that this {@code Class} represents.
* @see #getAnnotations()
*/
    native public Annotation[] getDeclaredAnnotations();

/**
* Returns the annotation if it exists.
//Synthetic comment -- @@ -526,58 +485,23 @@
* Class} represents. If there are no classes or interfaces declared or if
* this class represents an array class, a primitive type or void, then an
* empty array is returned.
     *
     * @return an array with {@code Class} objects for all the classes and
     *         interfaces that are used in member declarations.
*/
public Class<?>[] getDeclaredClasses() {
return getDeclaredClasses(this, false);
}

/*
     * Returns the list of member classes without performing any security checks
     * first. This includes the member classes inherited from superclasses. If no
     * member classes exist at all, an empty array is returned.
     *
     * @param publicOnly reflects whether we want only public members or all of them
     * @return the list of classes
*/
    private Class<?>[] getFullListOfClasses(boolean publicOnly) {
        Class<?>[] result = getDeclaredClasses(this, publicOnly);

        // Traverse all superclasses
        Class<?> clazz = this.getSuperclass();
        while (clazz != null) {
            Class<?>[] temp = getDeclaredClasses(clazz, publicOnly);
            if (temp.length != 0) {
                result = arraycopy(new Class[result.length + temp.length], result, temp);
            }

            clazz = clazz.getSuperclass();
        }

        return result;
    }

    /*
     * Returns the list of member classes of the given class. No security checks
     * are performed. If no members exist, an empty array is returned.
     *
     * @param clazz the class the members of which we want
     * @param publicOnly reflects whether we want only public member or all of them
     * @return the class' class members
     */
    private static native Class<?>[] getDeclaredClasses(Class<?> clazz, boolean publicOnly);

/**
* Returns a {@code Constructor} object which represents the constructor
     * matching the specified parameter types that is declared by the class
* represented by this {@code Class}.
*
     * @param parameterTypes
     *            the parameter types of the requested constructor.
     *            {@code (Class[]) null} is equivalent to the empty array.
     * @return the constructor described by {@code parameterTypes}.
* @throws NoSuchMethodException
*             if the requested constructor can not be found.
* @see #getConstructor(Class[])
//Synthetic comment -- @@ -592,10 +516,8 @@
* Returns an array containing {@code Constructor} objects for all
* constructors declared in the class represented by this {@code Class}. If
* there are no constructors or if this {@code Class} represents an array
     * class, a primitive type or void then an empty array is returned.
*
     * @return an array with the constructors declared in the class represented
     *         by this {@code Class}.
* @see #getConstructors()
*/
public Constructor<?>[] getDeclaredConstructors() {
//Synthetic comment -- @@ -603,22 +525,15 @@
}

/*
     * Returns the list of constructors without performing any security checks
     * first. If no constructors exist, an empty array is returned.
     *
     * @param clazz the class of interest
     * @param publicOnly reflects whether we want only public constructors or all of them
     * @return the list of constructors
*/
    private static native <T> Constructor<T>[] getDeclaredConstructors(
            Class<T> clazz, boolean publicOnly);

/**
     * Returns a {@code Field} object for the field with the specified name
* which is declared in the class represented by this {@code Class}.
*
     * @param name the name of the requested field.
     * @return the requested field in the class represented by this class.
* @throws NoSuchFieldException if the requested field can not be found.
* @see #getField(String)
*/
//Synthetic comment -- @@ -639,8 +554,6 @@
* if this {@code Class} represents an array class, a primitive type or void
* then an empty array is returned.
*
     * @return an array with the fields declared in the class represented by
     *         this class.
* @see #getFields()
*/
public Field[] getDeclaredFields() {
//Synthetic comment -- @@ -650,32 +563,23 @@
/*
* Returns the list of fields without performing any security checks
* first. If no fields exist at all, an empty array is returned.
     *
     * @param clazz the class of interest
     * @param publicOnly reflects whether we want only public fields or all of them
     * @return the list of fields
*/
    static native Field[] getDeclaredFields(Class<?> clazz, boolean publicOnly);

/**
     * Returns the field if it is defined by {@code clazz}; null otherwise. This
* may return a non-public member.
*/
    static native Field getDeclaredField(Class<?> clazz, String name);

/**
* Returns a {@code Method} object which represents the method matching the
     * specified name and parameter types that is declared by the class
* represented by this {@code Class}.
*
     * @param name
     *            the requested method's name.
     * @param parameterTypes
     *            the parameter types of the requested method.
     *            {@code (Class[]) null} is equivalent to the empty array.
     * @return the method described by {@code name} and {@code parameterTypes}.
* @throws NoSuchMethodException
     *             if the requested constructor can not be found.
* @throws NullPointerException
*             if {@code name} is {@code null}.
* @see #getMethod(String, Class[])
//Synthetic comment -- @@ -695,8 +599,6 @@
* methods or if this {@code Class} represents an array class, a primitive
* type or void then an empty array is returned.
*
     * @return an array with the methods declared in the class represented by
     *         this {@code Class}.
* @see #getMethods()
*/
public Method[] getDeclaredMethods() {
//Synthetic comment -- @@ -704,58 +606,45 @@
}

/**
     * Returns the list of methods without performing any security checks
     * first. If no methods exist, an empty array is returned.
*/
    static native Method[] getDeclaredMethods(Class<?> clazz, boolean publicOnly);

/**
     * Returns the constructor or method if it is defined by {@code clazz}; null
     * otherwise. This may return a non-public member.
     *
     * @param name the method name, or "<init>" to get a constructor.
*/
    static native Member getDeclaredConstructorOrMethod(Class clazz, String name, Class[] args);

/**
* Returns the declaring {@code Class} of this {@code Class}. Returns
* {@code null} if the class is not a member of another class or if this
     * {@code Class} represents an array class, a primitive type or void.
     *
     * @return the declaring {@code Class} or {@code null}.
*/
    native public Class<?> getDeclaringClass();

/**
* Returns the enclosing {@code Class} of this {@code Class}. If there is no
* enclosing class the method returns {@code null}.
     *
     * @return the enclosing {@code Class} or {@code null}.
*/
    native public Class<?> getEnclosingClass();

/**
     * Gets the enclosing {@code Constructor} of this {@code Class}, if it is an
* anonymous or local/automatic class; otherwise {@code null}.
     *
     * @return the enclosing {@code Constructor} instance or {@code null}.
*/
    native public Constructor<?> getEnclosingConstructor();

/**
     * Gets the enclosing {@code Method} of this {@code Class}, if it is an
* anonymous or local/automatic class; otherwise {@code null}.
     *
     * @return the enclosing {@code Method} instance or {@code null}.
*/
    native public Method getEnclosingMethod();

/**
     * Gets the {@code enum} constants associated with this {@code Class}.
* Returns {@code null} if this {@code Class} does not represent an {@code
* enum} type.
     *
     * @return an array with the {@code enum} constants or {@code null}.
*/
@SuppressWarnings("unchecked") // we only cast after confirming that this class is an enum
public T[] getEnumConstants() {
//Synthetic comment -- @@ -767,13 +656,10 @@

/**
* Returns a {@code Field} object which represents the public field with the
     * specified name. This method first searches the class C represented by
* this {@code Class}, then the interfaces implemented by C and finally the
* superclasses of C.
*
     * @param name
     *            the name of the requested field.
     * @return the public field specified by {@code name}.
* @throws NoSuchFieldException
*             if the field can not be found.
* @see #getDeclaredField(String)
//Synthetic comment -- @@ -820,8 +706,6 @@
* <p>If there are no public fields or if this class represents an array class,
* a primitive type or {@code void} then an empty array is returned.
*
     * @return an array with the public fields of the class represented by this
     *         {@code Class}.
* @see #getDeclaredFields()
*/
public Field[] getFields() {
//Synthetic comment -- @@ -829,7 +713,7 @@
getPublicFieldsRecursive(fields);

/*
         * The result may include duplicates when clazz implements an interface
* through multiple paths. Remove those duplicates.
*/
CollectionUtils.removeDuplicates(fields, Field.ORDER_BY_NAME_AND_DECLARING_CLASS);
//Synthetic comment -- @@ -857,12 +741,9 @@
}

/**
     * Gets the {@link Type}s of the interfaces that this {@code Class} directly
* implements. If the {@code Class} represents a primitive type or {@code
* void} then an empty array is returned.
     *
     * @return an array of {@link Type} instances directly implemented by the
     *         class represented by this {@code class}.
*/
public Type[] getGenericInterfaces() {
GenericSignatureParser parser = new GenericSignatureParser(getClassLoader());
//Synthetic comment -- @@ -871,10 +752,8 @@
}

/**
     * Gets the {@code Type} that represents the superclass of this {@code
* class}.
     *
     * @return an instance of {@code Type} representing the superclass.
*/
public Type getGenericSuperclass() {
GenericSignatureParser parser = new GenericSignatureParser(getClassLoader());
//Synthetic comment -- @@ -884,29 +763,22 @@

/**
* Returns an array of {@code Class} objects that match the interfaces
     * specified in the {@code implements} declaration of the class represented
* by this {@code Class}. The order of the elements in the array is
* identical to the order in the original class declaration. If the class
* does not implement any interfaces, an empty array is returned.
     *
     * @return an array with the interfaces of the class represented by this
     *         class.
*/
public native Class<?>[] getInterfaces();

/**
* Returns a {@code Method} object which represents the public method with
     * the specified name and parameter types. This method first searches the
* class C represented by this {@code Class}, then the superclasses of C and
* finally the interfaces implemented by C and finally the superclasses of C
* for a method with matching name.
*
     * @param name
     *            the requested method's name.
     * @param parameterTypes
     *            the parameter types of the requested method.
     *            {@code (Class[]) null} is equivalent to the empty array.
     * @return the public field specified by {@code name}.
* @throws NoSuchMethodException
*             if the method can not be found.
* @see #getDeclaredMethod(String, Class[])
//Synthetic comment -- @@ -924,13 +796,10 @@
* for the class C represented by this {@code Class}. Methods may be
* declared in C, the interfaces it implements or in the superclasses of C.
* The elements in the returned array are in no particular order.
     * <p>
     * If there are no public methods or if this {@code Class} represents a
     * primitive type or {@code void} then an empty array is returned.
     * </p>
*
     * @return an array with the methods of the class represented by this
     *         {@code Class}.
* @see #getDeclaredMethods()
*/
public Method[] getMethods() {
//Synthetic comment -- @@ -946,7 +815,7 @@
}

/**
     * Populates {@code result} with public methods defined by {@code clazz}, its
* superclasses, and all implemented interfaces, including overridden methods.
*/
private void getPublicMethodsRecursive(List<Method> result) {
//Synthetic comment -- @@ -969,18 +838,15 @@
* Returns an integer that represents the modifiers of the class represented
* by this {@code Class}. The returned value is a combination of bits
* defined by constants in the {@link Modifier} class.
     *
     * @return the modifiers of the class represented by this {@code Class}.
*/
public int getModifiers() {
return getModifiers(this, false);
}

/*
     * Return the modifiers for the given class.
*
     * @param clazz the class of interest
     * @ignoreInnerClassesAttrib determines whether we look for and use the
*     flags from an "inner class" attribute
*/
private static native int getModifiers(Class<?> clazz, boolean ignoreInnerClassesAttrib);
//Synthetic comment -- @@ -989,8 +855,6 @@
* Returns the name of the class represented by this {@code Class}. For a
* description of the format which is used, see the class definition of
* {@link Class}.
     *
     * @return the name of the class represented by this {@code Class}.
*/
public String getName() {
String result = name;
//Synthetic comment -- @@ -1033,8 +897,6 @@

/*
* Returns the simple name of a member or local class, or null otherwise.
     *
     * @return The name.
*/
private native String getInnerClassName();

//Synthetic comment -- @@ -1046,20 +908,15 @@
}

/**
     * Returns the URL of the resource specified by {@code resName}. The mapping
     * between the resource name and the URL is managed by the class' class
     * loader.
*
     * @param resName
     *            the name of the resource.
     * @return the requested resource's {@code URL} object or {@code null} if
     *         the resource can not be found.
* @see ClassLoader
*/
    public URL getResource(String resName) {
// Get absolute resource name, but without the leading slash
        if (resName.startsWith("/")) {
            resName = resName.substring(1);
} else {
String pkg = getName();
int dot = pkg.lastIndexOf('.');
//Synthetic comment -- @@ -1069,33 +926,29 @@
pkg = "";
}

            resName = pkg + "/" + resName;
}

// Delegate to proper class loader
ClassLoader loader = getClassLoader();
if (loader != null) {
            return loader.getResource(resName);
} else {
            return ClassLoader.getSystemResource(resName);
}
}

/**
     * Returns a read-only stream for the contents of the resource specified by
     * {@code resName}. The mapping between the resource name and the stream is
     * managed by the class' class loader.
*
     * @param resName
     *            the name of the resource.
     * @return a stream for the requested resource or {@code null} if no
     *         resource with the specified name can be found.
* @see ClassLoader
*/
    public InputStream getResourceAsStream(String resName) {
// Get absolute resource name, but without the leading slash
        if (resName.startsWith("/")) {
            resName = resName.substring(1);
} else {
String pkg = getName();
int dot = pkg.lastIndexOf('.');
//Synthetic comment -- @@ -1105,15 +958,15 @@
pkg = "";
}

            resName = pkg + "/" + resName;
}

// Delegate to proper class loader
ClassLoader loader = getClassLoader();
if (loader != null) {
            return loader.getResourceAsStream(resName);
} else {
            return ClassLoader.getSystemResourceAsStream(resName);
}
}

//Synthetic comment -- @@ -1122,8 +975,6 @@
* All classes from any given dex file will have the same signers, but different dex
* files may have different signers. This does not fit well with the original
* {@code ClassLoader}-based model of {@code getSigners}.)
     *
     * @return null.
*/
public Object[] getSigners() {
// See http://code.google.com/p/android/issues/detail?id=1766.
//Synthetic comment -- @@ -1136,8 +987,6 @@
* the {@code Object} class, a primitive type, an interface or void then the
* method returns {@code null}. If this {@code Class} represents an array
* class then the {@code Object} class is returned.
     *
     * @return the superclass of the class represented by this {@code Class}.
*/
public native Class<? super T> getSuperclass();

//Synthetic comment -- @@ -1145,9 +994,6 @@
* Returns an array containing {@code TypeVariable} objects for type
* variables declared by the generic class represented by this {@code
* Class}. Returns an empty array if the class is not generic.
     *
     * @return an array with the type variables of the class represented by this
     *         class.
*/
@SuppressWarnings("unchecked")
public synchronized TypeVariable<Class<T>>[] getTypeParameters() {
//Synthetic comment -- @@ -1157,10 +1003,7 @@
}

/**
     * Indicates whether this {@code Class} represents an annotation class.
     *
     * @return {@code true} if this {@code Class} represents an annotation
     *         class; {@code false} otherwise.
*/
public boolean isAnnotation() {
final int ACC_ANNOTATION = 0x2000;  // not public in reflect.Modifiers
//Synthetic comment -- @@ -1189,59 +1032,43 @@
}

/**
     * Indicates whether the class represented by this {@code Class} is
     * anonymously declared.
     *
     * @return {@code true} if the class represented by this {@code Class} is
     *         anonymous; {@code false} otherwise.
*/
native public boolean isAnonymousClass();

/**
     * Indicates whether the class represented by this {@code Class} is an array
     * class.
     *
     * @return {@code true} if the class represented by this {@code Class} is an
     *         array class; {@code false} otherwise.
*/
public boolean isArray() {
return getComponentType() != null;
}

/**
     * Indicates whether the specified class type can be converted to the class
* represented by this {@code Class}. Conversion may be done via an identity
* conversion or a widening reference conversion (if either the receiver or
* the argument represent primitive types, only the identity conversion
* applies).
*
     * @param cls
     *            the class to check.
     * @return {@code true} if {@code cls} can be converted to the class
     *         represented by this {@code Class}; {@code false} otherwise.
* @throws NullPointerException
     *             if {@code cls} is {@code null}.
*/
    public native boolean isAssignableFrom(Class<?> cls);

/**
     * Indicates whether the class represented by this {@code Class} is an
* {@code enum}.
     *
     * @return {@code true} if the class represented by this {@code Class} is an
     *         {@code enum}; {@code false} otherwise.
*/
public boolean isEnum() {
return ((getModifiers() & 0x4000) != 0) && (getSuperclass() == Enum.class);
}

/**
     * Indicates whether the specified object can be cast to the class
* represented by this {@code Class}. This is the runtime version of the
* {@code instanceof} operator.
*
     * @param object
     *            the object to check.
* @return {@code true} if {@code object} can be cast to the type
*         represented by this {@code Class}; {@code false} if {@code
*         object} is {@code null} or cannot be cast.
//Synthetic comment -- @@ -1249,19 +1076,13 @@
public native boolean isInstance(Object object);

/**
     * Indicates whether this {@code Class} represents an interface.
     *
     * @return {@code true} if this {@code Class} represents an interface;
     *         {@code false} otherwise.
*/
public native boolean isInterface();

/**
     * Indicates whether the class represented by this {@code Class} is defined
* locally.
     *
     * @return {@code true} if the class represented by this {@code Class} is
     *         defined locally; {@code false} otherwise.
*/
public boolean isLocalClass() {
boolean enclosed = (getEnclosingMethod() != null ||
//Synthetic comment -- @@ -1270,29 +1091,20 @@
}

/**
     * Indicates whether the class represented by this {@code Class} is a member
* class.
     *
     * @return {@code true} if the class represented by this {@code Class} is a
     *         member class; {@code false} otherwise.
*/
public boolean isMemberClass() {
return getDeclaringClass() != null;
}

/**
     * Indicates whether this {@code Class} represents a primitive type.
     *
     * @return {@code true} if this {@code Class} represents a primitive type;
     *         {@code false} otherwise.
*/
public native boolean isPrimitive();

/**
     * Indicates whether this {@code Class} represents a synthetic type.
     *
     * @return {@code true} if this {@code Class} represents a synthetic type;
     *         {@code false} otherwise.
*/
public boolean isSynthetic() {
final int ACC_SYNTHETIC = 0x1000;   // not public in reflect.Modifiers
//Synthetic comment -- @@ -1309,7 +1121,6 @@
* constructor exists but is not accessible from the context where this
* method is invoked, an {@code IllegalAccessException} is thrown.
*
     * @return a new instance of the class represented by this {@code Class}.
* @throws IllegalAccessException
*             if the default constructor is not visible.
* @throws InstantiationException
//Synthetic comment -- @@ -1334,9 +1145,6 @@
* Returns the {@code Package} of which the class represented by this
* {@code Class} is a member. Returns {@code null} if no {@code Package}
* object was created by the class loader of the class.
     *
     * @return Package the {@code Package} of which this {@code Class} is a
     *         member or {@code null}.
*/
public Package getPackage() {
// TODO This might be a hack, but the VM doesn't have the necessary info.
//Synthetic comment -- @@ -1353,42 +1161,33 @@
* Returns the assertion status for the class represented by this {@code
* Class}. Assertion is enabled / disabled based on the class loader,
* package or class default at runtime.
     *
     * @return the assertion status for the class represented by this {@code
     *         Class}.
*/
public native boolean desiredAssertionStatus();

/**
     * Casts this {@code Class} to represent a subclass of the specified class.
* If successful, this {@code Class} is returned; otherwise a {@code
* ClassCastException} is thrown.
*
     * @param clazz
     *            the required type.
     * @return this {@code Class} cast as a subclass of the given type.
* @throws ClassCastException
     *             if this {@code Class} cannot be cast to the specified type.
*/
@SuppressWarnings("unchecked")
    public <U> Class<? extends U> asSubclass(Class<U> clazz) {
        if (clazz.isAssignableFrom(this)) {
return (Class<? extends U>)this;
}
String actualClassName = this.getName();
        String desiredClassName = clazz.getName();
throw new ClassCastException(actualClassName + " cannot be cast to " + desiredClassName);
}

/**
     * Casts the specified object to the type represented by this {@code Class}.
* If the object is {@code null} then the result is also {@code null}.
*
     * @param obj
     *            the object to cast.
     * @return the object that has been cast.
* @throws ClassCastException
     *             if the object cannot be cast to the specified type.
*/
@SuppressWarnings("unchecked")
public T cast(Object obj) {







