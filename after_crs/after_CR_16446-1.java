/*Making sure the old hierarchy viewer works with categorized properties.

Change-Id:Ie0406055c73881a1cc238689b05567bca09fc8b1*/




//Synthetic comment -- diff --git a/hierarchyviewer/src/com/android/hierarchyviewer/scene/ViewHierarchyLoader.java b/hierarchyviewer/src/com/android/hierarchyviewer/scene/ViewHierarchyLoader.java
//Synthetic comment -- index c1c1e6d..c9cb66d 100644

//Synthetic comment -- @@ -160,6 +160,11 @@
ViewNode.Property property = new ViewNode.Property();
property.name = data.substring(start, index);

            int colonIndex = property.name.indexOf(':');
            if (colonIndex != -1) {
                property.name = property.name.substring(colonIndex + 1);
            }

int index2 = data.indexOf(',', index + 1);
int length = Integer.parseInt(data.substring(index + 1, index2));
start = index2 + 1 + length;







