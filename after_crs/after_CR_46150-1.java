/*Fix android reset problem caused by resolveractivity

when resolveractivity appears on some application,
after user disable all internal apps that can treat current intent
if user click disabled application,
resolveractivity occurs activiytnotfound exepcetion.

so phone occurs system_server crash and android reset.
this problem comes from mList update problem.
because mList can only be updated during 'mCurrentResolveList > 0'
in rebuildlist() function. in situation 'mCurrentResolveList = 0',
mList has wrong value and display wrong items.

Change-Id:Ifd6eb66687e2a7a4427bb61a562668eeb8291efd*/




//Synthetic comment -- diff --git a/core/java/com/android/internal/app/ResolverActivity.java b/core/java/com/android/internal/app/ResolverActivity.java
//Synthetic comment -- index 7334ac3..a552d04 100644

//Synthetic comment -- @@ -416,7 +416,7 @@
final int oldItemCount = getCount();
rebuildList();
notifyDataSetChanged();
            if (mCurrentResolveList != 0 && mCurrentResolveList.size() <= 0) {
// We no longer have any items...  just finish the activity.
finish();
}







