/*Mms: Send Messaging with attachment,repeat 10-20 times crash

ConcurrentModificationException occurs when you add the elements
while traversing a list with Iterator.
The issue is resolved by using CopyOnWriteArrayList instead of
ArrayList.

Change-Id:Ic717ed047475eae9a810d5767ed0a3fbf41ae86fAuthor: Wenjie Qin <wenjie.qin@borqs.com>
Signed-off-by: Frederic Predon <frederic.predon@intel.com>
Signed-off-by: Xiaokang Qin <xiaokang.qin@intel.com>
Signed-off-by: Beare, Bruce J <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 7844*/
//Synthetic comment -- diff --git a/src/com/android/mms/model/Model.java b/src/com/android/mms/model/Model.java
//Synthetic comment -- index 55c6b48..bd6abe1 100644

//Synthetic comment -- @@ -17,11 +17,11 @@

package com.android.mms.model;

import java.util.ArrayList;

public class Model {
    protected ArrayList<IModelChangedObserver> mModelChangedObservers =
            new ArrayList<IModelChangedObserver>();

public void registerModelChangedObserver(IModelChangedObserver observer) {
if (!mModelChangedObservers.contains(observer)) {







