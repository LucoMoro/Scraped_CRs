/*38700: wrong reporting of <requestFocus /> as missing size

(cherry picked from commit 2e851a6fcfda90bdf3b9e3059ff52b97a2be3522)

Change-Id:I70d513af429a6ca7d35cab5eb4e8d9639db87525*/
//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/RequiredAttributeDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/RequiredAttributeDetector.java
//Synthetic comment -- index a71f4b4..8cb2c42 100644

//Synthetic comment -- @@ -30,6 +30,7 @@
import static com.android.SdkConstants.FQCN_GRID_LAYOUT_V7;
import static com.android.SdkConstants.GRID_LAYOUT;
import static com.android.SdkConstants.LAYOUT_RESOURCE_PREFIX;
import static com.android.SdkConstants.STYLE_RESOURCE_PREFIX;
import static com.android.SdkConstants.TABLE_LAYOUT;
import static com.android.SdkConstants.TABLE_ROW;
//Synthetic comment -- @@ -311,12 +312,13 @@
return;
}

                if (VIEW_MERGE.equals(element.getNodeName())) {
return;
}

                boolean certain = true;

String parentTag = element.getParentNode() != null
?  element.getParentNode().getNodeName() : "";
if (TABLE_LAYOUT.equals(parentTag)
//Synthetic comment -- @@ -326,11 +328,7 @@
return;
}

                String tag = element.getTagName();
                if (tag.equals(VIEW_INCLUDE)) {
                    return;
                }

boolean isRoot = isRootElement(element);
if (isRoot || isRootElement(element.getParentNode())
&& VIEW_MERGE.equals(parentTag)) {








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/RequiredAttributeDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/RequiredAttributeDetectorTest.java
//Synthetic comment -- index abf3e04..3bae4d7 100644

//Synthetic comment -- @@ -82,4 +82,13 @@
));
}

}







