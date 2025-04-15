/*Fixed repo sample after changes to manifest merging.

Change-Id:I795b194d05025dd53e34973216fab36ee8b99c68*/
//Synthetic comment -- diff --git a/testapps/repo/app/src/main/java/com/example/android/multiproject/MainActivity.java b/testapps/repo/app/src/main/java/com/example/android/multiproject/MainActivity.java
//Synthetic comment -- index e984b9e..11d7c32 100644

//Synthetic comment -- @@ -5,6 +5,8 @@
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {
@Override
public void onCreate(Bundle savedInstanceState) {








//Synthetic comment -- diff --git a/testapps/repo/baseLibrary/src/main/java/com/sample/android/multiproject/PersonView.java b/testapps/repo/baseLibrary/src/main/java/com/sample/android/multiproject/library/PersonView.java
similarity index 85%
rename from testapps/repo/baseLibrary/src/main/java/com/sample/android/multiproject/PersonView.java
rename to testapps/repo/baseLibrary/src/main/java/com/sample/android/multiproject/library/PersonView.java
//Synthetic comment -- index a8f253f..b218532 100644

//Synthetic comment -- @@ -1,4 +1,4 @@
package com.example.android.multiproject;

import android.widget.TextView;
import android.content.Context;








//Synthetic comment -- diff --git a/testapps/repo/library/src/main/java/com/example/android/multiproject/ShowPeopleActivity.java b/testapps/repo/library/src/main/java/com/example/android/multiproject/library/ShowPeopleActivity.java
similarity index 93%
rename from testapps/repo/library/src/main/java/com/example/android/multiproject/ShowPeopleActivity.java
rename to testapps/repo/library/src/main/java/com/example/android/multiproject/library/ShowPeopleActivity.java
//Synthetic comment -- index b10f0d3..a3f2195 100644

//Synthetic comment -- @@ -1,4 +1,4 @@
package com.example.android.multiproject;

import android.app.Activity;
import android.os.Bundle;







