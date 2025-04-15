/*Add more theme query APIs to RenderResources

Change-Id:Iebde7536a0007898387dc7bb5d943e3767140a3c*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 4fb0d3d..ddfe2bc 100644

//Synthetic comment -- @@ -109,11 +109,46 @@
}

@Override
    public StyleResourceValue getTheme() {
return mTheme;
}

@Override
public ResourceValue getFrameworkResource(String resourceType, String resourceName) {
return getResource(resourceType, resourceName, mFrameworkResources);
}








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderResources.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderResources.java
//Synthetic comment -- index e371d5a..ce86c9a 100644

//Synthetic comment -- @@ -50,14 +50,35 @@
}

/**
     * Return the {@link StyleResourceValue} representing the current theme.
     * @return the theme or null if there is no theme.
*/
    public StyleResourceValue getTheme() {
return null;
}

/**
* Returns a framework resource by type and name. The returned resource is resolved.
* @param resourceType the type of the resource
* @param resourceName the name of the resource
//Synthetic comment -- @@ -83,8 +104,9 @@
* @return the {@link ResourceValue} object or <code>null</code>
*/
public ResourceValue findItemInTheme(String itemName) {
        if (getTheme() != null) {
            return findItemInStyle(getTheme(), itemName);
}

return null;







