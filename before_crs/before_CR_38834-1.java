/*Pass SearchView suggestion cursor exception to Log.

When an error in fetching a column occurs in your suggestions cursor
adapter this will ensure the stacktrace is logged to provide more
context about what failed.

Change-Id:Iafe4918caebb9b0fb712b758b2d3ace329fd2512*/
//Synthetic comment -- diff --git a/core/java/android/widget/SearchView.java b/core/java/android/widget/SearchView.java
//Synthetic comment -- index 9d2ff2e..b3d0fdd 100644

//Synthetic comment -- @@ -1521,8 +1521,8 @@
} catch (RuntimeException e2 ) {
rowNum = -1;
}
            Log.w(LOG_TAG, "Search Suggestions cursor at row " + rowNum +
                            " returned exception" + e.toString());
return null;
}
}







