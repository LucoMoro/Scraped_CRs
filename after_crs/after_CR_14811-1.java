/*Reduced warnings by removing unused imports, unused variables and adding type arguments

Change-Id:I551403a94de87a2fdf24ac0175ce69b4c8a48d54*/




//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Animation2.java b/samples/ApiDemos/src/com/example/android/apis/view/Animation2.java
//Synthetic comment -- index b2236aa..041794e 100644

//Synthetic comment -- @@ -49,7 +49,7 @@
s.setOnItemSelectedListener(this);
}

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
switch (position) {

case 0:
//Synthetic comment -- @@ -79,7 +79,7 @@
}
}

    public void onNothingSelected(AdapterView<?> parent) {
}

private String[] mStrings = {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Animation3.java b/samples/ApiDemos/src/com/example/android/apis/view/Animation3.java
//Synthetic comment -- index 11fc9ed..2cd7605 100644

//Synthetic comment -- @@ -50,7 +50,7 @@
s.setOnItemSelectedListener(this);
}

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
final View target = findViewById(R.id.target);
final View targetParent = (View) target.getParent();

//Synthetic comment -- @@ -96,6 +96,6 @@
target.startAnimation(a);
}

    public void onNothingSelected(AdapterView<?> parent) {
}
}
\ No newline at end of file








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete1.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete1.java
//Synthetic comment -- index f4274e5..bec4a5d 100644

//Synthetic comment -- @@ -19,12 +19,9 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.os.Bundle;

public class AutoComplete1 extends Activity {









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete6.java b/samples/ApiDemos/src/com/example/android/apis/view/AutoComplete6.java
//Synthetic comment -- index 3573bfb..2c28d65 100644

//Synthetic comment -- @@ -19,12 +19,9 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.os.Bundle;

public class AutoComplete6 extends Activity {









//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Buttons1.java b/samples/ApiDemos/src/com/example/android/apis/view/Buttons1.java
//Synthetic comment -- index e2f8cc8..a88ee30 100644

//Synthetic comment -- @@ -22,9 +22,6 @@

import android.app.Activity;
import android.os.Bundle;

/**
* A gallery of the different styles of buttons.








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/ExpandableList1.java b/samples/ApiDemos/src/com/example/android/apis/view/ExpandableList1.java
//Synthetic comment -- index 944db64..94d6c06 100644

//Synthetic comment -- @@ -24,7 +24,6 @@
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Focus1.java b/samples/ApiDemos/src/com/example/android/apis/view/Focus1.java
//Synthetic comment -- index 86f6ee7..c816b31 100644

//Synthetic comment -- @@ -20,12 +20,10 @@

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.ArrayAdapter;

/**
* Demonstrates the use of non-focusable views.
*/








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Gallery1.java b/samples/ApiDemos/src/com/example/android/apis/view/Gallery1.java
//Synthetic comment -- index a539a5b..7aaaaef 100644

//Synthetic comment -- @@ -49,7 +49,7 @@

// Set a item click listener, and just Toast the clicked position
g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
Toast.makeText(Gallery1.this, "" + position, Toast.LENGTH_SHORT).show();
}
});








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/ImageSwitcher1.java b/samples/ApiDemos/src/com/example/android/apis/view/ImageSwitcher1.java
//Synthetic comment -- index f72b623..0e74ea5 100644

//Synthetic comment -- @@ -56,11 +56,11 @@
g.setOnItemSelectedListener(this);
}

    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
mSwitcher.setImageResource(mImageIds[position]);
}

    public void onNothingSelected(AdapterView<?> parent) {
}

public View makeView() {








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List13.java b/samples/ApiDemos/src/com/example/android/apis/view/List13.java
//Synthetic comment -- index b3087be..68179ed 100644

//Synthetic comment -- @@ -52,7 +52,6 @@
private LayoutInflater mInflater;

public SlowAdapter(Context context) {
mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
}

//Synthetic comment -- @@ -114,11 +113,6 @@

return text;
}
}

@Override








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/List9.java b/samples/ApiDemos/src/com/example/android/apis/view/List9.java
//Synthetic comment -- index 15b3cc1..b2aea05 100644

//Synthetic comment -- @@ -111,8 +111,8 @@



    public void onScroll(AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount) {
if (mReady) {
char firstLetter = mStrings[firstVisibleItem].charAt(0);

//Synthetic comment -- @@ -120,8 +120,6 @@

mShowing = true;
mDialogText.setVisibility(View.VISIBLE);
}
mDialogText.setText(((Character)firstLetter).toString());
mHandler.removeCallbacks(mRemoveWindow);
//Synthetic comment -- @@ -316,5 +314,4 @@
"Woodside Cabecou", "Xanadu", "Xynotyro", "Yarg Cornish",
"Yarra Valley Pyramid", "Yorkshire Blue", "Zamorano",
"Zanetti Grana Padano", "Zanetti Parmigiano Reggiano"};
}








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/RatingBar1.java b/samples/ApiDemos/src/com/example/android/apis/view/RatingBar1.java
//Synthetic comment -- index 97416d4..5fbf6dd 100644

//Synthetic comment -- @@ -19,7 +19,6 @@
import android.app.Activity;
import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.android.apis.R;








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/ScrollView2.java b/samples/ApiDemos/src/com/example/android/apis/view/ScrollView2.java
//Synthetic comment -- index 89e4003..696937c 100644

//Synthetic comment -- @@ -20,12 +20,10 @@

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;

/**
* Demonstrates wrapping a layout in a ScrollView.
*








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/TableLayout10.java b/samples/ApiDemos/src/com/example/android/apis/view/TableLayout10.java
//Synthetic comment -- index f1f8f24..f904f84 100644

//Synthetic comment -- @@ -19,15 +19,9 @@
import com.example.android.apis.R;

import android.app.Activity;
import android.os.Bundle;

public class TableLayout10 extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/TableLayout11.java b/samples/ApiDemos/src/com/example/android/apis/view/TableLayout11.java
//Synthetic comment -- index 770238f..09b19a2 100644

//Synthetic comment -- @@ -25,8 +25,6 @@
* <p>This example shows how to use horizontal gravity in a table layout.</p>
*/
public class TableLayout11 extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/TableLayout12.java b/samples/ApiDemos/src/com/example/android/apis/view/TableLayout12.java
//Synthetic comment -- index 14cbd0d..f3fe850 100644

//Synthetic comment -- @@ -25,8 +25,6 @@
* <p>This example shows how to use cell spanning in a table layout.</p>
*/
public class TableLayout12 extends Activity {
@Override
protected void onCreate(Bundle savedInstanceState) {
super.onCreate(savedInstanceState);








//Synthetic comment -- diff --git a/samples/ApiDemos/src/com/example/android/apis/view/Tabs1.java b/samples/ApiDemos/src/com/example/android/apis/view/Tabs1.java
//Synthetic comment -- index 455969e..39f7e9b 100644

//Synthetic comment -- @@ -21,7 +21,6 @@
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.view.LayoutInflater;

import com.example.android.apis.R;








