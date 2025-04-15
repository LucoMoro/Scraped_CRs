/*Fix issue with resource repository when using libraries

Change-Id:I1a3e5ba9ac6e930d4be2daedff9691862f975408*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceRepository.java b/ide_common/src/com/android/ide/common/resources/ResourceRepository.java
//Synthetic comment -- index 41e4f89..f72ebd2 100644

//Synthetic comment -- @@ -482,10 +482,11 @@
*/
private Map<String, ResourceValue> getConfiguredResource(ResourceType type,
FolderConfiguration referenceConfig) {
// get the resource item for the given type
List<ResourceItem> items = mResourceMap.get(type);
if (items == null) {
            return Collections.emptyMap();
}

// create the map







