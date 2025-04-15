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
    public ResourceValue findItemInStyle(StyleResourceValue style, String itemName) {
        ResourceValue item = style.findValue(itemName, style.isFramework());

        // if we didn't find it, we look in the parent style (if applicable)
        if (item == null && mStyleInheritanceMap != null) {
            StyleResourceValue parentStyle = mStyleInheritanceMap.get(style);
            if (parentStyle != null) {
                return findItemInStyle(parentStyle, itemName);
            }
}

return item;








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/RenderResources.java b/layoutlib_api/src/com/android/ide/common/rendering/api/RenderResources.java
//Synthetic comment -- index 8ccbd69..f9e02d6 100644

//Synthetic comment -- @@ -127,14 +127,18 @@
* Returns the {@link ResourceValue} matching a given name in a given style. If the
* item is not directly available in the style, the method looks in its parent style.
*
* @param style the style to search in
     * @param itemName the name of the item to search for.
* @return the {@link ResourceValue} object or <code>null</code>
*
     * @Deprecated Use {@link #findItemInStyle(StyleResourceValue, String, boolean)}
*/
@Deprecated
    public ResourceValue findItemInStyle(StyleResourceValue style, String itemName) {
return null;
}








