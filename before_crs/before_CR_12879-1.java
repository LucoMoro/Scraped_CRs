/*ImageButton example doesn't work. Default state should be at the last.
see res/res/drawable/button_inset.xml*/
//Synthetic comment -- diff --git a/core/java/android/widget/ImageButton.java b/core/java/android/widget/ImageButton.java
//Synthetic comment -- index d417e40..2fc29bc 100644

//Synthetic comment -- @@ -44,11 +44,11 @@
* <pre>
* &lt;?xml version="1.0" encoding="utf-8"?&gt;
* &lt;selector xmlns:android="http://schemas.android.com/apk/res/android"&gt;
 *     &lt;item android:drawable="@drawable/button_normal" /&gt; &lt;!-- default --&gt;
*     &lt;item android:state_pressed="true"
*           android:drawable="@drawable/button_pressed" /&gt; &lt;!-- pressed --&gt;
*     &lt;item android:state_focused="true"
*           android:drawable="@drawable/button_focused" /&gt; &lt;!-- focused --&gt;
* &lt;/selector&gt;</pre>
*
* <p>Save the XML file in your project {@code res/drawable/} folder and then 







