/*Add more theme query APIs to RenderResources

Change-Id:Iebde7536a0007898387dc7bb5d943e3767140a3c*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 4fb0d3d..bd63cb8 100644

//Synthetic comment -- @@ -109,11 +109,34 @@
}

@Override
    public StyleResourceValue getCurrentTheme() {
return mTheme;
}

@Override
    public StyleResourceValue getTheme(String name, boolean frameworkTheme) {
        ResourceValue theme = null;

        if (frameworkTheme) {
            Map<String, ResourceValue> frameworkStyleMap = mFrameworkResources.get(RES_STYLE);
            if (frameworkStyleMap != null) {
                theme = frameworkStyleMap.get(name);
            }
        } else {
            Map<String, ResourceValue> projectStyleMap = mProjectResources.get(RES_STYLE);
            if (projectStyleMap != null) {
                theme = projectStyleMap.get(name);
            }
        }

        if (theme instanceof StyleResourceValue) {
            return (StyleResourceValue) theme;
        }

        return null;
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
     * Returns the {@link StyleResourceValue} representing the current theme.
     * @return the theme or null if there is no current theme.
*/
    public StyleResourceValue getCurrentTheme() {
return null;
}

/**
     * Returns a theme by its name.
     *
     * @param name the name of the theme
     * @param frameworkTheme whether the theme is a framework theme.
     * @return the theme or null if there's no match
     */
    public StyleResourceValue getTheme(String name, boolean frameworkTheme) {
        return null;
    }

    /**
     * Returns whether a theme is a parent of a given theme.
     * @param parentTheme the parent theme
     * @param childTheme the child theme.
     * @return true if the parent theme is indeed a parent theme of the child theme.
     */
    public boolean themeIsParentOf(StyleResourceValue parentTheme, StyleResourceValue childTheme) {
        return false;
    }

    /**
* Returns a framework resource by type and name. The returned resource is resolved.
* @param resourceType the type of the resource
* @param resourceName the name of the resource
//Synthetic comment -- @@ -83,8 +104,9 @@
* @return the {@link ResourceValue} object or <code>null</code>
*/
public ResourceValue findItemInTheme(String itemName) {
        StyleResourceValue currentTheme = getCurrentTheme();
        if (currentTheme != null) {
            return findItemInStyle(currentTheme, itemName);
}

return null;







