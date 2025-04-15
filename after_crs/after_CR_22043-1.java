/*Add repeat count config to layoutlib_api's AdapterBinding.

Change-Id:Idbb54de6dee6d6b466476a0e3acdd87dd394ee42*/




//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index a645098..e80378e 100644

//Synthetic comment -- @@ -82,10 +82,19 @@
* android.widget.AdapterView
*/
public static class AdapterBinding {
        private final int mRepeatCount;
private final List<ResourceReference> mHeaders = new ArrayList<ResourceReference>();
private final List<AdapterItemReference> mItems = new ArrayList<AdapterItemReference>();
private final List<ResourceReference> mFooters = new ArrayList<ResourceReference>();

        public AdapterBinding(int repeatCount) {
            mRepeatCount = repeatCount;
        }

        public int getRepeatCount() {
            return mRepeatCount;
        }

public void addHeader(ResourceReference layoutInfo) {
mHeaders.add(layoutInfo);
}







