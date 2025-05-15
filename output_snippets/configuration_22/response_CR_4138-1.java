//<Beginning of snippet n. 0>


* that can be displayed when a TwoLineListItem has focus. Android supplies a 
* {@link android.R.layout#two_line_list_item standard layout resource for TwoLineListView} 
* (which does not include a selected item icon), but you can design your own custom XML
 * layout for this object without tying it to any specific application implementation. 
 * You can create a layout file named `custom_two_line_list_item.xml` as follows:
<pre>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical">

    <TextView
        android:id="@+id/title"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceLarge" />

    <TextView
        android:id="@+id/subtitle"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:textAppearance="?android:attr/textAppearanceMedium" />
</LinearLayout>
</pre>
* 
* @attr ref android.R.styleable#TwoLineListItem_mode
*/

//<End of snippet n. 0>