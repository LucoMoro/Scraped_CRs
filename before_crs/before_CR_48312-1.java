/*Mms: Add initialization the mLayoutType parameter.

The mLayoutType value is always assigned to default value after
LayoutModel class re-construct.

Change-Id:Ib6085f21bff0de9ff735a0205b17431185bc4838Author: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: b416 <haixiong.zheng@borqs.com>
Signed-off-by: Bin Yang <bin.y.yang@intel.com>
Signed-off-by: Shuo Gao <shuo.gao@intel.com>
Signed-off-by: Bruce Beare <bruce.j.beare@intel.com>
Signed-off-by: Jack Ren <jack.ren@intel.com>
Author-tracking-BZ: 7957*/
//Synthetic comment -- diff --git a/src/com/android/mms/model/LayoutModel.java b/src/com/android/mms/model/LayoutModel.java
//Synthetic comment -- index 97b1637..0280534 100644

//Synthetic comment -- @@ -110,6 +110,11 @@
if (mTextRegion == null) {
createDefaultTextRegion();
}
}

public RegionModel getRootLayout() {







