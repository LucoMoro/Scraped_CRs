/*Ensure ADT app templates' activity onCreate is protected.

It's considered a best practice for activity onCreate overrides to maintain the
original visibility of the method, which is protected. Implement this for all
activity templates.

Change-Id:Ia501628a3f330f671398935103fafc1834f8e78e*/
//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/DropdownActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/DropdownActivity.java.ftl
//Synthetic comment -- index fb19202..f2549ad 100644

//Synthetic comment -- @@ -26,7 +26,7 @@
private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

@Override
    public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.${layoutName});









//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/SimpleActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/SimpleActivity.java.ftl
//Synthetic comment -- index a409ad4..feae824 100644

//Synthetic comment -- @@ -11,7 +11,7 @@
public class ${activityClass} extends Activity {

@Override
    public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.${layoutName});
<#if parentActivityClass != "">








//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/TabsActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/TabsActivity.java.ftl
//Synthetic comment -- index b0a2f5d..0bf975e 100644

//Synthetic comment -- @@ -23,7 +23,7 @@
private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

@Override
    public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.${layoutName});









//Synthetic comment -- diff --git a/templates/activities/BlankActivity/root/src/app_package/TabsAndPagerActivity.java.ftl b/templates/activities/BlankActivity/root/src/app_package/TabsAndPagerActivity.java.ftl
//Synthetic comment -- index 9e82449..7aa79bd 100644

//Synthetic comment -- @@ -35,7 +35,7 @@
ViewPager mViewPager;

@Override
    public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.${layoutName});









//Synthetic comment -- diff --git a/templates/activities/FullscreenActivity/root/src/app_package/FullscreenActivity.java.ftl b/templates/activities/FullscreenActivity/root/src/app_package/FullscreenActivity.java.ftl
//Synthetic comment -- index 3040c67..729cbec 100755

//Synthetic comment -- @@ -49,7 +49,8 @@
*/
private SystemUiHider mSystemUiHider;

    public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);

setContentView(R.layout.${layoutName});








//Synthetic comment -- diff --git a/templates/activities/LoginActivity/root/src/app_package/LoginActivity.java.ftl b/templates/activities/LoginActivity/root/src/app_package/LoginActivity.java.ftl
//Synthetic comment -- index d766051..8defdc7 100644

//Synthetic comment -- @@ -55,8 +55,8 @@
private TextView mLoginStatusMessageView;

@Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

setContentView(R.layout.${layoutName});
<#if parentActivityClass != "">








//Synthetic comment -- diff --git a/templates/activities/MasterDetailFlow/root/src/app_package/ContentListActivity.java.ftl b/templates/activities/MasterDetailFlow/root/src/app_package/ContentListActivity.java.ftl
//Synthetic comment -- index cbd815e..ae73f7d 100644

//Synthetic comment -- @@ -32,7 +32,7 @@
private boolean mTwoPane;

@Override
    public void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);
setContentView(R.layout.activity_${collection_name});
<#if parentActivityClass != "">







