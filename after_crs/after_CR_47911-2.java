/*Make implementation of isEmpty consistent with implementation of getCount in HeaderListViewAdapter

Fix for Issue ID 18316. Make isEmpty() return false when getCount() returns a non-zero value. So that the list cannot be considered empty when a header or footer is present.https://code.google.com/p/android/issues/detail?id=18316Change-Id:Ib69201e9d5ef3efcbb68ea298b8cc8ca6e027246Signed-off-by: Emma Sajic <esajic@effectivelateralsolutions.co.uk>*/




//Synthetic comment -- diff --git a/core/java/android/widget/HeaderViewListAdapter.java b/core/java/android/widget/HeaderViewListAdapter.java
//Synthetic comment -- index e2a269e..8c3ec68 100644

//Synthetic comment -- @@ -78,9 +78,10 @@
return mFooterViewInfos.size();
}

	public boolean isEmpty() {
		return (mAdapter == null || mAdapter.isEmpty())
				&& getFootersCount() + getHeadersCount() == 0;
	}

private boolean areAllListInfosSelectable(ArrayList<ListView.FixedViewInfo> infos) {
if (infos != null) {







