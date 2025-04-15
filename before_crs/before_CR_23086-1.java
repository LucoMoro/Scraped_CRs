/*Merge d4166974 from master. do not merge.
Fix issue with resource repository when using libraries

Change-Id:I45c8af34020f047f08a53ddd6ada3a8be4061a52*/
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







