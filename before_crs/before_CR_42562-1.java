/*Fixed Maps.java comment to reflect current code

Change-Id:I679123eada764a2b1d142f335d964ec653d99c07*/
//Synthetic comment -- diff --git a/guava/src/com/google/common/collect/Maps.java b/guava/src/com/google/common/collect/Maps.java
//Synthetic comment -- index 703c447..4215869 100644

//Synthetic comment -- @@ -227,7 +227,7 @@
*/
public static <K, V> TreeMap<K, V> newTreeMap(
@Nullable Comparator<? super K> comparator) {
    // Ideally, the extra type parameter "C" shouldn't be necessary. It is a
// work-around of a compiler type inference quirk that prevents the
// following code from being compiled:
// Comparator<Class<?>> comparator = null;







