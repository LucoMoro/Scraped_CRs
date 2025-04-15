/*Make inline ResourceItem able to generate ResourceValue

This change fix the issue that since the simplification of
the resource repository, the resource item for inline ID values
would not be called to generate a ResourceValue since their
list of source file was empty.

This moves the creation of the ResourceValue to the ResourceItem
so that InlineResourceItem can override it.

This required moving findMatchingConfigurable into FolderConfiguration
which is a much better place for it anyway.

Change-Id:I36d6b148528c593ea432c9fd0ac8d542cbe2a26e*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/ProjectCallback.java
//Synthetic comment -- index 6ce4d6b..d432d14 100644

//Synthetic comment -- @@ -224,7 +224,7 @@

public Integer getResourceId(ResourceType type, String name) {
if (mProjectRes != null) {
            return mProjectRes.getResourceId(type, name);
}

return null;








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/configurations/FolderConfiguration.java
//Synthetic comment -- index 259ed35..15d1ba3 100644

//Synthetic comment -- @@ -17,8 +17,12 @@
package com.android.ide.eclipse.adt.internal.resources.configurations;

import com.android.AndroidConstants;
import com.android.ide.eclipse.adt.internal.resources.manager.Configurable;
import com.android.resources.ResourceFolderType;

import java.util.ArrayList;
import java.util.List;


/**
* Represents the configuration for Resource Folders. All the properties have a default
//Synthetic comment -- @@ -519,6 +523,114 @@
}

/**
     * Returns the best matching {@link Configurable} for this configuration.
     *
     * @param configurables the list of {@link Configurable} to choose from.
     *
     * @return an item from the given list of {@link Configurable} or null.
     *
     * @see http://d.android.com/guide/topics/resources/resources-i18n.html#best-match
     */
    public Configurable findMatchingConfigurable(List<? extends Configurable> configurables) {
        //
        // 1: eliminate resources that contradict the reference configuration
        // 2: pick next qualifier type
        // 3: check if any resources use this qualifier, if no, back to 2, else move on to 4.
        // 4: eliminate resources that don't use this qualifier.
        // 5: if more than one resource left, go back to 2.
        //
        // The precedence of the qualifiers is more important than the number of qualifiers that
        // exactly match the device.

        // 1: eliminate resources that contradict
        ArrayList<Configurable> matchingConfigurables = new ArrayList<Configurable>();
        for (int i = 0 ; i < configurables.size(); i++) {
            Configurable res = configurables.get(i);

            if (res.getConfiguration().isMatchFor(this)) {
                matchingConfigurables.add(res);
            }
        }

        // if there is only one match, just take it
        if (matchingConfigurables.size() == 1) {
            return matchingConfigurables.get(0);
        } else if (matchingConfigurables.size() == 0) {
            return null;
        }

        // 2. Loop on the qualifiers, and eliminate matches
        final int count = FolderConfiguration.getQualifierCount();
        for (int q = 0 ; q < count ; q++) {
            // look to see if one configurable has this qualifier.
            // At the same time also record the best match value for the qualifier (if applicable).

            // The reference value, to find the best match.
            // Note that this qualifier could be null. In which case any qualifier found in the
            // possible match, will all be considered best match.
            ResourceQualifier referenceQualifier = getQualifier(q);

            boolean found = false;
            ResourceQualifier bestMatch = null; // this is to store the best match.
            for (Configurable configurable : matchingConfigurables) {
                ResourceQualifier qualifier = configurable.getConfiguration().getQualifier(q);
                if (qualifier != null) {
                    // set the flag.
                    found = true;

                    // Now check for a best match. If the reference qualifier is null ,
                    // any qualifier is a "best" match (we don't need to record all of them.
                    // Instead the non compatible ones are removed below)
                    if (referenceQualifier != null) {
                        if (qualifier.isBetterMatchThan(bestMatch, referenceQualifier)) {
                            bestMatch = qualifier;
                        }
                    }
                }
            }

            // 4. If a configurable has a qualifier at the current index, remove all the ones that
            // do not have one, or whose qualifier value does not equal the best match found above
            // unless there's no reference qualifier, in which case they are all considered
            // "best" match.
            if (found) {
                for (int i = 0 ; i < matchingConfigurables.size(); ) {
                    Configurable configurable = matchingConfigurables.get(i);
                    ResourceQualifier qualifier = configurable.getConfiguration().getQualifier(q);

                    if (qualifier == null) {
                        // this resources has no qualifier of this type: rejected.
                        matchingConfigurables.remove(configurable);
                    } else if (referenceQualifier != null && bestMatch != null &&
                            bestMatch.equals(qualifier) == false) {
                        // there's a reference qualifier and there is a better match for it than
                        // this resource, so we reject it.
                        matchingConfigurables.remove(configurable);
                    } else {
                        // looks like we keep this resource, move on to the next one.
                        i++;
                    }
                }

                // at this point we may have run out of matching resources before going
                // through all the qualifiers.
                if (matchingConfigurables.size() < 2) {
                    break;
                }
            }
        }

        // Because we accept resources whose configuration have qualifiers where the reference
        // configuration doesn't, we can end up with more than one match. In this case, we just
        // take the first one.
        if (matchingConfigurables.size() == 0) {
            return null;
        }
        return matchingConfigurables.get(0);
    }


    /**
* Returns whether the configuration is a match for the given reference config.
* <p/>A match means that, for each qualifier of this config
* <ul>








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/InlineResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/InlineResourceItem.java
//Synthetic comment -- index 6d1bd23..8b2b27d 100644

//Synthetic comment -- @@ -16,6 +16,10 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.resources.ResourceType;


/**
* Represents a resource item that has been declared inline in another resource file.
//Synthetic comment -- @@ -28,6 +32,8 @@
*/
class InlineResourceItem extends ResourceItem {

    private ResourceValue mValue = null;

/**
* Constructs a new inline ResourceItem.
* @param name the name of the resource as it appears in the XML and R.java files.
//Synthetic comment -- @@ -47,6 +53,17 @@
}

@Override
    public ResourceValue getResourceValue(ResourceType type, FolderConfiguration referenceConfig,
            boolean isFramework) {
        assert type == ResourceType.ID;
        if (mValue == null) {
            mValue = new ResourceValue(type, getName(), isFramework);
        }

        return mValue;
    }

    @Override
public String toString() {
return "InlineResourceItem [mName=" + getName() + ", mFiles=" //$NON-NLS-1$ //$NON-NLS-2$
+ getSourceFileList() + "]"; //$NON-NLS-1$








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index a6ee95d..20f869a 100644

//Synthetic comment -- @@ -56,7 +56,7 @@
private Map<IntArrayWrapper, String> mStyleableValueToNameMap;

/**
     * This list is used by {@link #getResourceId(String, String)} when the resource
* query is an ID that doesn't exist (for example for ID automatically generated in
* layout files that are not saved yet).
*/
//Synthetic comment -- @@ -179,12 +179,12 @@
}

/**
     * Returns the integer id of a resource given its type and name.
* <p/>If the resource is of type {@link ResourceType#ID} and does not exist in the
* internal map, then new id values are dynamically generated (and stored so that queries
* with the same names will return the same value).
*/
    public Integer getResourceId(ResourceType type, String name) {
if (mResourceValueMap != null) {
Map<String, Integer> map = mResourceValueMap.get(type);
if (map != null) {
//Synthetic comment -- @@ -206,7 +206,7 @@

/**
* Resets the list of dynamic Ids. This list is used by
     * {@link #getResourceId(String, String)} when the resource query is an ID that doesn't
* exist (for example for ID automatically generated in layout files that are not saved yet.)
* <p/>This method resets those dynamic ID and must be called whenever the actual list of IDs
* change.
//Synthetic comment -- @@ -272,6 +272,10 @@
* coming from XML declaration into the cached list {@link #mIdResourceList}.
*/
void mergeIdResources() {
        if (mResourceValueMap == null) {
            return;
        }

// get the current ID values
List<ResourceItem> resources = mResourceMap.get(ResourceType.ID);









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceItem.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceItem.java
//Synthetic comment -- index f826ed7..bc121cc 100644

//Synthetic comment -- @@ -16,6 +16,7 @@

package com.android.ide.eclipse.adt.internal.resources.manager;

import com.android.ide.common.rendering.api.ResourceValue;
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.resources.ResourceType;

//Synthetic comment -- @@ -93,6 +94,20 @@
return false;
}

    public ResourceValue getResourceValue(ResourceType type, FolderConfiguration referenceConfig,
            boolean isFramework) {
        // look for the best match for the given configuration
        // the match has to be of type ResourceFile since that's what the input list contains
        ResourceFile match = (ResourceFile) referenceConfig.findMatchingConfigurable(mFiles);

        if (match != null) {
            // get the value of this configured resource.
            return match.getValue(type, mName);
        }

        return null;
    }

/**
* Adds a new source file.
* @param file the source file.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceManager.java
//Synthetic comment -- index 1d3c709..f3d872e 100644

//Synthetic comment -- @@ -528,6 +528,8 @@
}
}
}

                    projectResources.postUpdate();
} catch (CoreException e) {
// This happens if the project is closed or if the folder doesn't exist.
// Since we already test for that, we can ignore this exception.








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ResourceRepository.java
//Synthetic comment -- index 39de45c..93946cd 100644

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.ide.eclipse.adt.internal.resources.configurations.FolderConfiguration;
import com.android.ide.eclipse.adt.internal.resources.configurations.LanguageQualifier;
import com.android.ide.eclipse.adt.internal.resources.configurations.RegionQualifier;
import com.android.ide.eclipse.adt.io.IFolderWrapper;
import com.android.io.IAbstractFolder;
import com.android.resources.FolderTypeRelationship;
//Synthetic comment -- @@ -135,7 +134,7 @@
for (int i = 0 ; i < count ; i++) {
ResourceFolder resFolder = list.get(i);
// this is only used for Eclipse stuff so we know it's an IFolderWrapper
                IAbstractFolder folder = resFolder.getFolder();
if (removedFolder.equals(folder)) {
// we found the matching ResourceFolder. we need to remove it.
list.remove(i);
//Synthetic comment -- @@ -315,7 +314,7 @@
}

// from those, get the folder with a config matching the given reference configuration.
        Configurable match = config.findMatchingConfigurable(matchingFolders);

// do we have a matching folder?
if (match instanceof ResourceFolder) {
//Synthetic comment -- @@ -345,8 +344,9 @@
for (ResourceItem item : items) {
if (name.equals(item.getName())) {
if (referenceConfig != null) {
                    Configurable match = referenceConfig.findMatchingConfigurable(
                            item.getSourceFileList());

if (match instanceof ResourceFile) {
return Collections.singletonList((ResourceFile) match);
}
//Synthetic comment -- @@ -471,135 +471,16 @@
HashMap<String, ResourceValue> map = new HashMap<String, ResourceValue>(items.size());

for (ResourceItem item : items) {
            ResourceValue value = item.getResourceValue(type, referenceConfig,
                    isFrameworkRepository());
            if (value != null) {
                map.put(item.getName(), value);
}
}

return map;
}


/**
* Called after a resource change event, when the resource delta has been processed.








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceValue.java b/layoutlib_api/src/com/android/ide/common/rendering/api/ResourceValue.java
//Synthetic comment -- index f15d903..730d5c1 100644

//Synthetic comment -- @@ -27,19 +27,19 @@
private final ResourceType mType;
private final String mName;
private String mValue = null;
    private final boolean mIsFramework;

    public ResourceValue(ResourceType type, String name, boolean isFramework) {
mType = type;
mName = name;
        mIsFramework = isFramework;
}

public ResourceValue(ResourceType type, String name, String value, boolean isFramework) {
mType = type;
mName = name;
mValue = value;
        mIsFramework = isFramework;
}

public ResourceType getResourceType() {
//Synthetic comment -- @@ -50,6 +50,7 @@
* Returns the type of the resource. For instance "drawable", "color", etc...
* @deprecated use {@link #getResourceType()} instead.
*/
    @Deprecated
public String getType() {
return mType.getName();
}
//Synthetic comment -- @@ -73,7 +74,7 @@
* resource (<code>false</false>).
*/
public final boolean isFramework() {
        return mIsFramework;
}

/**
//Synthetic comment -- @@ -94,8 +95,8 @@

@Override
public String toString() {
        return "ResourceValue [" + mType + "/" + mName + " = " + mValue  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                + " (framework:" + mIsFramework + ")]"; //$NON-NLS-1$ //$NON-NLS-2$
}









