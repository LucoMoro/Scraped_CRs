/*39213: Refreshing resources fails with "Error: name expected". DO NOT MERGE

Change-Id:I7adb6b0f0435a37dbf6d63383c87642b1260be63*/
//Synthetic comment -- diff --git a/sdk_common/src/com/android/ide/common/resources/SingleResourceFile.java b/sdk_common/src/com/android/ide/common/resources/SingleResourceFile.java
//Synthetic comment -- index eb643f6..8e394d3 100644

//Synthetic comment -- @@ -16,12 +16,15 @@

package com.android.ide.common.resources;

import com.android.ide.common.rendering.api.DensityBasedResourceValue;
import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.common.resources.configuration.DensityQualifier;
import com.android.io.IAbstractFile;
import com.android.resources.FolderTypeRelationship;
import com.android.resources.ResourceType;

import java.util.Collection;
import java.util.List;
//Synthetic comment -- @@ -150,8 +153,8 @@
* @return true if parsing succeeds and false if it fails
*/
private boolean validateAttributes(ScanningContext context) {
        // We only need to check if it's a non-framework file
        if (!isFramework()) {
ValidatingResourceParser parser = new ValidatingResourceParser(context, false);
try {
IAbstractFile file = getFile();







