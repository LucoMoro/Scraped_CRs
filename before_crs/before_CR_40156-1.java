/*Amortize the cost of Class.isEnum in Enum.valueOf.

(cherry-pick of 1f6a7be67187fa6553f6466c0ec311294149f754.)

Conflicts:

	luni/src/main/java/java/lang/Enum.java

Change-Id:Ic4905650dfd8c2b8359ab723955d228771ea27c7*/
//Synthetic comment -- diff --git a/luni/src/main/java/java/lang/Enum.java b/luni/src/main/java/java/lang/Enum.java
//Synthetic comment -- index 391670c..3d9e68b 100644

//Synthetic comment -- @@ -34,6 +34,9 @@
private static final BasicLruCache<Class<? extends Enum>, Object[]> sharedConstantsCache
= new BasicLruCache<Class<? extends Enum>, Object[]>(64) {
@Override protected Object[] create(Class<? extends Enum> enumType) {
Method method = (Method) Class.getDeclaredConstructorOrMethod(
enumType, "values", EmptyArray.CLASS);
try {
//Synthetic comment -- @@ -181,10 +184,11 @@
if (enumType == null || name == null) {
throw new NullPointerException("enumType == null || name == null");
}
        if (!enumType.isEnum()) {
throw new IllegalArgumentException(enumType + " is not an enum type");
}
        for (T value : getSharedConstants(enumType)) {
if (name.equals(value.name())) {
return value;
}







