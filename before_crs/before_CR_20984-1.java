/*Fix possible NPE in legacy callback support.

Change-Id:I5faa155393466e1bc4b2fc1e9417cde91a8be456*/
//Synthetic comment -- diff --git a/ide_common/src/com/android/ide/common/rendering/legacy/LegacyCallback.java b/ide_common/src/com/android/ide/common/rendering/legacy/LegacyCallback.java
//Synthetic comment -- index f4bc15a..144a196 100644

//Synthetic comment -- @@ -41,7 +41,11 @@

public final String[] resolveResourceValue(int id) {
Pair<ResourceType, String> info = resolveResourceId(id);
        return new String[] { info.getSecond(), info.getFirst().getName() };
}

public final String resolveResourceValue(int[] id) {







