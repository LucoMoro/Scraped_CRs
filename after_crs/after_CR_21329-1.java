/*Removing wrong equals method in comparator.

This implementation of equals method has been likely created by
template in an IDE. It is useless, possibly leading to poor performance
and completely violating the contract of equals method as defined in
Object base class.

Change-Id:I5e7fd759b7bd9370b44d9374b57390c4e9feba02*/




//Synthetic comment -- diff --git a/core/java/android/text/util/Linkify.java b/core/java/android/text/util/Linkify.java
//Synthetic comment -- index 98605888c..e2293e4 100644

//Synthetic comment -- @@ -498,10 +498,6 @@

return 0;
}
};

Collections.sort(links, c);







