/*Fix required attribute check for GridLayout children

Change-Id:I58e67eb1acb6b8e5a503f1aa7822c97b545eac10*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/RequiredAttributeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/RequiredAttributeDetector.java
//Synthetic comment -- index 81b0626..a71f4b4 100644

//Synthetic comment -- @@ -319,7 +319,15 @@

String parentTag = element.getParentNode() != null
?  element.getParentNode().getNodeName() : "";
                if (TABLE_LAYOUT.equals(parentTag)
                        || TABLE_ROW.equals(parentTag)
                        || GRID_LAYOUT.equals(parentTag)
                        || FQCN_GRID_LAYOUT_V7.equals(parentTag)) {
                    return;
                }

                String tag = element.getTagName();
                if (tag.equals(VIEW_INCLUDE)) {
return;
}

//Synthetic comment -- @@ -368,11 +376,6 @@
}
}

String message;
if (!(hasWidth || hasHeight)) {
if (certain) {







