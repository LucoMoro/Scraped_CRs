/*Add lint detector finding duplicate resource definitions

Change-Id:I03d90622f0eedf8121c1a8c32600a5be6bfae1a4*/




//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/BuiltinIssueRegistry.java
//Synthetic comment -- index 147100d..9f0c064 100644

//Synthetic comment -- @@ -55,7 +55,7 @@
private static final List<Issue> sIssues;

static {
        final int initialCapacity = 123;
List<Issue> issues = new ArrayList<Issue>(initialCapacity);

issues.add(AccessibilityDetector.ISSUE);
//Synthetic comment -- @@ -66,6 +66,7 @@
issues.add(ApiDetector.UNSUPPORTED);
issues.add(DuplicateIdDetector.CROSS_LAYOUT);
issues.add(DuplicateIdDetector.WITHIN_LAYOUT);
        issues.add(DuplicateResourceDetector.ISSUE);
issues.add(WrongIdDetector.UNKNOWN_ID);
issues.add(WrongIdDetector.UNKNOWN_ID_LAYOUT);
issues.add(StateListDetector.ISSUE);








//Synthetic comment -- diff --git a/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateResourceDetector.java b/lint/libs/lint_checks/src/com/android/tools/lint/checks/DuplicateResourceDetector.java
new file mode 100644
//Synthetic comment -- index 0000000..fce4633

//Synthetic comment -- @@ -0,0 +1,166 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.tools.lint.checks;


import static com.android.SdkConstants.ATTR_NAME;
import static com.android.SdkConstants.ATTR_TYPE;
import static com.android.SdkConstants.TAG_ITEM;

import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import com.android.resources.ResourceFolderType;
import com.android.resources.ResourceType;
import com.android.tools.lint.detector.api.Category;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.Location;
import com.android.tools.lint.detector.api.Location.Handle;
import com.android.tools.lint.detector.api.ResourceXmlDetector;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.Severity;
import com.android.tools.lint.detector.api.Speed;
import com.android.tools.lint.detector.api.XmlContext;
import com.android.utils.Pair;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This detector identifies cases where a resource is defined multiple times in the
 * same resource folder
 */
public class DuplicateResourceDetector extends ResourceXmlDetector {

    /** The main issue discovered by this detector */
    public static final Issue ISSUE = Issue.create(
            "DuplicateDefinition", //$NON-NLS-1$
            "Discovers duplicate definitions of resources",

            "You can define a resource multiple times in different resource folders; that's how " +
            "string translations are done, for example. However, defining the same resource " +
            "more than once in the same resource folder is likely an error, for example " +
            "attempting to add a new resource without realizing that the name is already used, " +
            "and so on.",

            Category.CORRECTNESS,
            6,
            Severity.ERROR,
            DuplicateResourceDetector.class,
            Scope.ALL_RESOURCES_SCOPE).addAnalysisScope(Scope.RESOURCE_FILE_SCOPE);

    private static final String PRODUCT = "product";   //$NON-NLS-1$
    private Map<ResourceType, Set<String>> mTypeMap;
    private Map<ResourceType, List<Pair<String, Location.Handle>>> mLocations;
    private File mParent;

    /** Constructs a new {@link DuplicateResourceDetector} */
    public DuplicateResourceDetector() {
    }

    @Override
    public @NonNull Speed getSpeed() {
        return Speed.NORMAL;
    }

    @Override
    @Nullable
    public Collection<String> getApplicableAttributes() {
        return Collections.singletonList(ATTR_NAME);
    }

    @Override
    public boolean appliesTo(@NonNull ResourceFolderType folderType) {
        return folderType == ResourceFolderType.VALUES;
    }

    @Override
    public void beforeCheckFile(@NonNull Context context) {
        File parent = context.file.getParentFile();
        if (!parent.equals(mParent)) {
            mParent = parent;
            mTypeMap = Maps.newEnumMap(ResourceType.class);
            mLocations = Maps.newEnumMap(ResourceType.class);
        }
    }

    @Override
    public void visitAttribute(@NonNull XmlContext context, @NonNull Attr attribute) {
        Element element = attribute.getOwnerElement();

        if (element.hasAttribute(PRODUCT)) {
            return;
        }

        String tag = element.getTagName();
        String typeString = tag;
        if (tag.equals(TAG_ITEM)) {
            typeString = element.getAttribute(ATTR_TYPE);
        }
        ResourceType type = ResourceType.getEnum(typeString);
        if (type == null) {
            return;
        }

        if (type == ResourceType.ATTR
                && element.getParentNode().getNodeName().equals(
                        ResourceType.DECLARE_STYLEABLE.getName())) {
            return;
        }

        Set<String> names = mTypeMap.get(type);
        if (names == null) {
            names = Sets.newHashSetWithExpectedSize(40);
            mTypeMap.put(type, names);
        }

        String name = attribute.getValue();
        if (names.contains(name)) {
            String message = String.format("%1$s has already been defined in this folder",
                    name);
            Location location = context.getLocation(attribute);
            List<Pair<String, Handle>> list = mLocations.get(type);
            for (Pair<String, Handle> pair : list) {
                if (name.equals(pair.getFirst())) {
                    Location secondary = pair.getSecond().resolve();
                    secondary.setMessage("Previously defined here");
                    location.setSecondary(secondary);
                }
            }
            context.report(ISSUE, attribute, location, message, null);
        } else {
            names.add(name);
            List<Pair<String, Handle>> list = mLocations.get(type);
            if (list == null) {
                list = Lists.newArrayList();
                mLocations.put(type, list);
            }
            Location.Handle handle = context.parser.createLocationHandle(context, attribute);
            list.add(Pair.of(name, handle));
        }
    }
}








//Synthetic comment -- diff --git a/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/DuplicateResourceDetectorTest.java b/lint/libs/lint_checks/tests/src/com/android/tools/lint/checks/DuplicateResourceDetectorTest.java
new file mode 100644
//Synthetic comment -- index 0000000..a4a5a68

//Synthetic comment -- @@ -0,0 +1,63 @@
/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.android.tools.lint.checks;

import com.android.tools.lint.detector.api.Detector;

@SuppressWarnings("javadoc")
public class DuplicateResourceDetectorTest extends AbstractCheckTest {
    @Override
    protected Detector getDetector() {
        return new DuplicateResourceDetector();
    }

    public void test() throws Exception {
        assertEquals(
        "res/values/customattr2.xml:2: Error: ContentFrame has already been defined in this folder [DuplicateDefinition]\n" +
        "    <declare-styleable name=\"ContentFrame\">\n" +
        "                       ~~~~~~~~~~~~~~~~~~~\n" +
        "    res/values/customattr.xml:2: Previously defined here\n" +
        "res/values/strings2.xml:19: Error: wallpaper_instructions has already been defined in this folder [DuplicateDefinition]\n" +
        "    <string name=\"wallpaper_instructions\">Tap image to set landscape wallpaper</string>\n" +
        "            ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n" +
        "    res/values/strings.xml:29: Previously defined here\n" +
        "2 errors, 0 warnings\n",

        lintProject(
                "res/values/strings.xml",
                "res/values-land/strings.xml=>res/values/strings2.xml",
                "res/values-cs/strings.xml",
                "res/values/customattr.xml",
                "res/values/customattr.xml=>res/values/customattr2.xml"));
    }

    public void testOk() throws Exception {
        assertEquals(
        "No warnings.",

        lintProject(
                "res/values/strings.xml",
                "res/values-cs/strings.xml",
                "res/values-de-rDE/strings.xml",
                "res/values-es/strings.xml",
                "res/values-es-rUS/strings.xml",
                "res/values-land/strings.xml",
                "res/values-cs/arrays.xml",
                "res/values-es/donottranslate.xml",
                "res/values-nl-rNL/strings.xml"));
    }
}







