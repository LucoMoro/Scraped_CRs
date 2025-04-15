/*Dynamically generated resources id were not used in id resolution.

Dynamic IDs are generated when requesting an int value from a
(type,name) pair where type==ResourceType.ID and the name doesn't
reference any existing ID (This is possible when IDs are declared inline
and the layout file has not been saved, for instance, so the R class
has not been regenerated yet).

There is a reverse method querying for a (type,name) pair based on
an integer. This methods was not looking for generated id and would
return null instead of the name associated with the generated ID.

Change-Id:I0bc4bc76a5157d56d06c5f4538c4fcc38f1bded7*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/resources/manager/ProjectResources.java
//Synthetic comment -- index 7598da1..5b4bf38 100644

//Synthetic comment -- @@ -69,6 +69,7 @@
* layout files that are not saved yet).
*/
private final Map<String, Integer> mDynamicIds = new HashMap<String, Integer>();
private int mDynamicSeed = DYNAMIC_ID_SEED_START;

private final IProject mProject;
//Synthetic comment -- @@ -178,11 +179,19 @@
* @return a {@link Pair} of 2 strings { name, type } or null if the id could not be resolved
*/
public Pair<ResourceType, String> resolveResourceId(int id) {
if (mResIdValueToNameMap != null) {
            return mResIdValueToNameMap.get(id);
}

        return null;
}

/**
//Synthetic comment -- @@ -233,6 +242,7 @@
public void resetDynamicIds() {
synchronized (mDynamicIds) {
mDynamicIds.clear();
mDynamicSeed = DYNAMIC_ID_SEED_START;
}
}
//Synthetic comment -- @@ -257,6 +267,7 @@
if (value == null) {
value = new Integer(++mDynamicSeed);
mDynamicIds.put(name, value);
}

return value;







