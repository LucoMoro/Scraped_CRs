/*Fix handling of @id/ resources

If you create a relative layout where one of the widgets create a new
id such as @+id/button1, and then a later widget references this
widget in a layout constraint using @id instead of @+id, such as
layout_below="@id/button1", then it's possible for the resource
repository to not update itself properly, and a subsequent layout
editor render will generate error messages (can't find @id/button1).

The problem is that the id parser was only looking up the resource
repository for the @id reference, it was not also looking up the
current resource file, to see if an @id reference is valid. This made
it erroneously mark an error, and therefore return false from the
parse method. This then had the cascaded effect of not updating the
repository information from the parse, so the newly added id didn't
get added to the maps.

Change-Id:Iae3d215897525582579faf1c8ba64260215fec9d*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/IdGeneratingResourceFile.java b/ide_common/src/com/android/ide/common/resources/IdGeneratingResourceFile.java
//Synthetic comment -- index 6c72dbf..9ff1748 100644

//Synthetic comment -- @@ -87,7 +87,9 @@
// need to parse the file and find the IDs.
if (!parseFileForIds(context)) {
context.requestFullAapt();
            // Continue through to updating the resource item here since it
            // will make for example layout rendering more accurate until
            // aapt is re-run
}

// We only need to update the repository if our IDs have changed
//Synthetic comment -- @@ -226,4 +228,13 @@
// IDs declared
mIdResources.put(value.getName(), value);
}

    @Override
    public boolean hasResourceValue(ResourceType type, String name) {
        if (type == ResourceType.ID) {
            return mIdResources.containsKey(name);
        }

        return false;
    }
}








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/IdResourceParser.java b/ide_common/src/com/android/ide/common/resources/IdResourceParser.java
//Synthetic comment -- index 324ad2b..1de664e 100644

//Synthetic comment -- @@ -130,7 +130,8 @@
// Validate resource references (unless we're scanning a framework
// resource or if we've already scheduled a full aapt run)
boolean exists = resources.hasResourceItem(value);
                            if (!exists && !mRepository.hasResourceValue(ResourceType.ID,
                                    value.substring(value.indexOf('/') + 1))) {
String error = String.format(
// Don't localize because the exact pattern matches AAPT's
// output which has hardcoded regexp matching in








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/MultiResourceFile.java b/ide_common/src/com/android/ide/common/resources/MultiResourceFile.java
//Synthetic comment -- index cff1869..c9a8bc7 100644

//Synthetic comment -- @@ -204,6 +204,12 @@
}

@Override
    public boolean hasResourceValue(ResourceType type, String name) {
        Map<String, ResourceValue> map = mResourceItems.get(type);
        return map != null && map.containsKey(name);
    }

    @Override
public ResourceValue getValue(ResourceType type, String name) {
// get the list for the given type
Map<String, ResourceValue> list = mResourceItems.get(type);








//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java b/ide_common/src/com/android/ide/common/resources/ValueResourceParser.java
//Synthetic comment -- index 7c11dd7..aabfd35 100644

//Synthetic comment -- @@ -44,6 +44,7 @@

public interface IValueResourceRepository {
void addResourceValue(ResourceValue value);
        boolean hasResourceValue(ResourceType type, String name);
}

private boolean inResources = false;







