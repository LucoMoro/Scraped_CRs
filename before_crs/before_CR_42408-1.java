/*Fix a bug where the max blowup of annotations was incorrect.

I'm not sure where the 1.34 number comes from but it's incorrect.
From the spec, the encoded_annotation is made up of a single byte
plus an unlimited number of uleb128 values. Each of these values
can double in width in the worst case. I received (personal) email
from one user who'd run into a case worse than the incorrect 1.34
limit.

Change-Id:I3b676e6d2b274aaa538ca61ce23945b3d49aff04*/
//Synthetic comment -- diff --git a/dx/src/com/android/dx/merge/DexMerger.java b/dx/src/com/android/dx/merge/DexMerger.java
//Synthetic comment -- index c48f436..8237bda 100644

//Synthetic comment -- @@ -1011,8 +1011,8 @@
classData += (int) Math.ceil(contents.classDatas.byteCount * 1.34);
// all of the bytes in an encoding arrays section may be uleb/sleb
encodedArray += contents.encodedArrays.byteCount * 2;
                // at most 1/3 of the bytes in an encoding arrays section are uleb/sleb
                annotation += (int) Math.ceil(contents.annotations.byteCount * 1.34);
// all of the bytes in a debug info section may be uleb/sleb
debugInfo += contents.debugInfos.byteCount * 2;
}







