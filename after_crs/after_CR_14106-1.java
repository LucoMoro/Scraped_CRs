/*Cleanup some whitespace.

Change-Id:I5b9dd9ca2f0493b7f69f225e2d75a9ce840babb2*/




//Synthetic comment -- diff --git a/graphics/java/android/graphics/Color.java b/graphics/java/android/graphics/Color.java
//Synthetic comment -- index a50693d..651bf17 100644

//Synthetic comment -- @@ -85,7 +85,7 @@
* These component values should be [0..255], but there is no
* range check performed, so if they are out of range, the
* returned color is undefined.
     * @param red   Red component [0..255] of the color
* @param green Green component [0..255] of the color
* @param blue  Blue component [0..255] of the color
*/
//Synthetic comment -- @@ -228,7 +228,7 @@
*     hsv[1] is Saturation [0...1]
*     hsv[2] is Value [0...1]
* If hsv values are out of range, they are pinned.
     * @param hsb 3 element array which holds the input HSB components.
* @return the resulting argb color
* 
* @hide Pending API council
//Synthetic comment -- @@ -308,10 +308,10 @@
*     hsv[0] is Hue [0 .. 360)
*     hsv[1] is Saturation [0...1]
*     hsv[2] is Value [0...1]
     * @param red   red component value [0..255]
     * @param green green component value [0..255]
* @param blue  blue component value [0..255]
     * @param hsv   3 element array which holds the resulting HSV components.
*/
public static void RGBToHSV(int red, int green, int blue, float hsv[]) {
if (hsv.length < 3) {
//Synthetic comment -- @@ -326,7 +326,7 @@
*     hsv[1] is Saturation [0...1]
*     hsv[2] is Value [0...1]
* @param color the argb color to convert. The alpha component is ignored.
     * @param hsv   3 element array which holds the resulting HSV components.
*/
public static void colorToHSV(int color, float hsv[]) {
RGBToHSV((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsv);
//Synthetic comment -- @@ -338,7 +338,7 @@
*     hsv[1] is Saturation [0...1]
*     hsv[2] is Value [0...1]
* If hsv values are out of range, they are pinned.
     * @param hsv 3 element array which holds the input HSV components.
* @return the resulting argb color
*/
public static int HSVToColor(float hsv[]) {
//Synthetic comment -- @@ -353,7 +353,7 @@
*     hsv[2] is Value [0...1]
* If hsv values are out of range, they are pinned.
* @param alpha the alpha component of the returned argb color.
     * @param hsv   3 element array which holds the input HSV components.
* @return the resulting argb color
*/
public static int HSVToColor(int alpha, float hsv[]) {







