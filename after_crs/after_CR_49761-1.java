/*Faster ResourceFolderType.getFolderType(String folderName)

Change-Id:I7142846efabcb6aadeec2a609ebbc14964120301*/




//Synthetic comment -- diff --git a/layoutlib_api/src/main/java/com/android/resources/ResourceFolderType.java b/layoutlib_api/src/main/java/com/android/resources/ResourceFolderType.java
//Synthetic comment -- index 65c1cb3..5a271cf 100644

//Synthetic comment -- @@ -51,6 +51,7 @@
* @return the enum or null if not found.
*/
public static ResourceFolderType getTypeByName(String name) {
        assert name.indexOf('-') == -1 : name; // use #getFolderType instead
for (ResourceFolderType rType : values()) {
if (rType.mName.equals(name)) {
return rType;
//Synthetic comment -- @@ -67,10 +68,10 @@
* <code>null</code> if no matching type was found.
*/
public static ResourceFolderType getFolderType(String folderName) {
        int index = folderName.indexOf(ResourceConstants.RES_QUALIFIER_SEP);
        if (index != -1) {
            folderName = folderName.substring(0, index);
        }
        return getTypeByName(folderName);
}
}








//Synthetic comment -- diff --git a/layoutlib_api/src/test/java/com/android/resources/ResourceFolderTypeTest.java b/layoutlib_api/src/test/java/com/android/resources/ResourceFolderTypeTest.java
new file mode 100644
//Synthetic comment -- index 0000000..a3c2c51

//Synthetic comment -- @@ -0,0 +1,30 @@
/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.resources;

import junit.framework.TestCase;

public class ResourceFolderTypeTest extends TestCase {
  public void test() {
    assertEquals(ResourceFolderType.LAYOUT, ResourceFolderType.getFolderType("layout"));
    assertEquals(ResourceFolderType.LAYOUT, ResourceFolderType.getFolderType("layout-port"));
    assertEquals(ResourceFolderType.LAYOUT, ResourceFolderType.getFolderType("layout-port-sw600"));
    assertEquals(ResourceFolderType.VALUES, ResourceFolderType.getFolderType("values"));

    assertNull(ResourceFolderType.getFolderType(""));
    assertNull(ResourceFolderType.getFolderType("foo"));
  }
}







