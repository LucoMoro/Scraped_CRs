/*Don't walk the inheritance heirarchy looking for deprecated superclasses.
It makes no sense that a class would become deprecated because its superclass
became deprecated - this resulted in nonsensical deprecation warnings for
classes that weren't actually deprecated, like WebView, simply because they
inherited from something that was.*/
//Synthetic comment -- diff --git a/tools/droiddoc/src/ClassInfo.java b/tools/droiddoc/src/ClassInfo.java
//Synthetic comment -- index 36edbf8..2a792c5 100644

//Synthetic comment -- @@ -378,16 +378,8 @@

public TagInfo[] deprecatedTags()
{
        TagInfo[] result = comment().deprecatedTags();
        if (result.length == 0) {
            if (comment().undeprecateTags().length == 0) {
                if (superclass() != null) {
                    result = superclass().deprecatedTags();
                }
            }
        }
        // should we also do the interfaces?
        return result;
}

public MethodInfo[] methods()







