/*Added Type Arguments to Samples, to reduce Warnings and get a litle bit more Compiler Code checking for "new" Android Programmers

Change-Id:I25add019e99fec88c8422da14f3e18246ca0f0b5*/
//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/ApiDemos.java b/samples/ApiDemos/src/com/example/android/apis/ApiDemos.java
//Synthetic comment -- index 78b1fd7..42c588c 100644

//Synthetic comment -- @@ -52,8 +52,8 @@
getListView().setTextFilterEnabled(true);
}

    protected List getData(String prefix) {
        List<Map> myData = new ArrayList<Map>();

Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
mainIntent.addCategory(Intent.CATEGORY_SAMPLE_CODE);
//Synthetic comment -- @@ -107,10 +107,11 @@
return myData;
}

    private final static Comparator<Map> sDisplayNameComparator = new Comparator<Map>() {
private final Collator   collator = Collator.getInstance();

        public int compare(Map map1, Map map2) {
return collator.compare(map1.get("title"), map2.get("title"));
}
};
//Synthetic comment -- @@ -128,7 +129,7 @@
return result;
}

    protected void addItem(List<Map> data, String name, Intent intent) {
Map<String, Object> temp = new HashMap<String, Object>();
temp.put("title", name);
temp.put("intent", intent);
//Synthetic comment -- @@ -136,11 +137,11 @@
}

@Override
protected void onListItemClick(ListView l, View v, int position, long id) {
        Map map = (Map) l.getItemAtPosition(position);

Intent intent = (Intent) map.get("intent");
startActivity(intent);
}

}







