/*Improve search of items into style for legacy layoutlibs.

Legacy versions uses the old API that doesn't specify the
namespace of the attribute being queried. The implementation
used the namespace of the style as the namespace of the attribute
but this make little sense.

At better implementation searches in the project's namespace
and, if the attribute is not found, then searches in the
framework namespace.

Change-Id:Ief43ecd45f108162de2b1512027d4eedf2c132db*/




//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/resources/ResourceResolver.java b/ide_common/src/com/android/ide/common/resources/ResourceResolver.java
//Synthetic comment -- index 43416e7..772d75e 100644

//Synthetic comment -- @@ -167,15 +167,13 @@

@Override
@Deprecated
    public ResourceValue findItemInStyle(StyleResourceValue style, String attrName) {
        // this method is deprecated because it doesn't know about the namespace of the
        // attribute so we search for the project namespace first and then in the
        // android namespace if needed.
        ResourceValue item = findItemInStyle(style, attrName, false /*isFrameworkAttr*/);
        if (item == null) {
            item = findItemInStyle(style, attrName, true /*isFrameworkAttr*/);
}

return item;








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderResources.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderResources.java
//Synthetic comment -- index 8ccbd69..f9e02d6 100644

//Synthetic comment -- @@ -127,14 +127,18 @@
* Returns the {@link ResourceValue} matching a given name in a given style. If the
* item is not directly available in the style, the method looks in its parent style.
*
     * This version of doesn't support providing the namespace of the attribute so it'll search
     * in both the project's namespace and then in the android namespace.
     *
* @param style the style to search in
     * @param attrName the name of the attribute to search for.
* @return the {@link ResourceValue} object or <code>null</code>
*
     * @Deprecated Use {@link #findItemInStyle(StyleResourceValue, String, boolean)} since this
     * method doesn't know the item namespace.
*/
@Deprecated
    public ResourceValue findItemInStyle(StyleResourceValue style, String attrName) {
return null;
}








