/*Add Ubuntu patch to work around OpenJDK bug

Change-Id:Ide9497aeba2570c5151cd7f6e6e4cea6480d3885*/
//Synthetic comment -- diff --git a/guava/src/com/google/common/collect/Maps.java b/guava/src/com/google/common/collect/Maps.java
//Synthetic comment -- index 2aea6b7..703c447 100644

//Synthetic comment -- @@ -225,8 +225,8 @@
* @param comparator the comparator to sort the keys with
* @return a new, empty {@code TreeMap}
*/
  public static <C, K extends C, V> TreeMap<K, V> newTreeMap(
      @Nullable Comparator<C> comparator) {
// Ideally, the extra type parameter "C" shouldn't be necessary. It is a
// work-around of a compiler type inference quirk that prevents the
// following code from being compiled:







