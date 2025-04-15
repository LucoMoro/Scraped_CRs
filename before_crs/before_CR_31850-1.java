/*Added check for mText property in getText()

Sometimes the node's property is not 'text:mText' but just 'mText'.
A new test was added to getText() to check for both properties and not
throwing unnecessarily the RuntimeException("No text property on node").

This has been tested by monkyrunner tests using EasyMonkeyDevice that runs
correctly after this patch is applied.

Change-Id:I5399169b906e28eeee0a17ab298a724cc5bb9b51Signed-off-by: Diego Torres Milano <dtmilano@gmail.com>*/
//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/hierarchyviewer/HierarchyViewer.java b/chimpchat/src/com/android/chimpchat/hierarchyviewer/HierarchyViewer.java
//Synthetic comment -- index 6ad98ad..6c34d71 100644

//Synthetic comment -- @@ -170,7 +170,11 @@
}
ViewNode.Property textProperty = node.namedProperties.get("text:mText");
if (textProperty == null) {
            throw new RuntimeException("No text property on node");
}
return textProperty.value;
}







