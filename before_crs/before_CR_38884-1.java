/*Improve HttpResponseCache performance.

Avoid writing to HttpResponseCache.CacheRequestImpl.cacheOut
one byte at a time via inefficient FilterOutputStream write.

(cherry-picked from 91cc423115fdfa682d9c4cd025dee06aaa145b3c.)

Bug: 6738383
Change-Id:Ia657d7417cc292746968809f6896a5e790f1394d*/
//Synthetic comment -- diff --git a/luni/src/main/java/libcore/net/http/HttpResponseCache.java b/luni/src/main/java/libcore/net/http/HttpResponseCache.java
//Synthetic comment -- index 56042b1..24aff87 100644

//Synthetic comment -- @@ -262,6 +262,13 @@
super.close();
editor.commit();
}
};
}








