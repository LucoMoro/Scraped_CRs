/*Add to layoutlib_api the ability to do layout only.

This lets the session do the measure/layout part and skip
the draw.

This returns the object location through the ViewInfo but
not the rendering.

Change-Id:I8875b4631b5fb00efa7096d804129612d46d9dc9*/
//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java b/layoutlib_api/src/com/android/ide/common/rendering/api/Capability.java
//Synthetic comment -- index ff6777b..ec4cf38 100644

//Synthetic comment -- @@ -28,6 +28,10 @@
CUSTOM_BACKGROUND_COLOR,
/** Ability to call {@link LayoutScene#render()} and {@link LayoutScene#render(long)}. */
RENDER,
/**
* Ability to control embedded layout parsers through {@link IXmlPullParser#getParser(String)}
*/
//Synthetic comment -- @@ -39,7 +43,6 @@
* {@link LayoutScene#setProperty(Object, String, String)}<br>
* The method that receives an animation listener can only use it if the
* ANIMATED_VIEW_MANIPULATION, or FULL_ANIMATED_VIEW_MANIPULATION is also supported.
     *
* */
VIEW_MANIPULATION,
/** Ability to play animations with<br>








//Synthetic comment -- diff --git a/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java b/layoutlib_api/src/com/android/ide/common/rendering/api/SessionParams.java
//Synthetic comment -- index 9446ff5..826e70d 100644

//Synthetic comment -- @@ -20,7 +20,6 @@

/**
* Rendering parameters for a {@link RenderSession}.
 *
*/
public class SessionParams extends RenderParams {

//Synthetic comment -- @@ -50,66 +49,71 @@

private final ILayoutPullParser mLayoutDescription;
private final RenderingMode mRenderingMode;

/**
    *
    * @param layoutDescription the {@link ILayoutPullParser} letting the LayoutLib Bridge visit the
    * layout file.
    * @param renderingMode The rendering mode.
    * @param projectKey An Object identifying the project. This is used for the cache mechanism.
    * @param screenWidth the screen width
    * @param screenHeight the screen height
    * @param density the density factor for the screen.
    * @param xdpi the screen actual dpi in X
    * @param ydpi the screen actual dpi in Y
    * @param themeName The name of the theme to use.
    * @param isProjectTheme true if the theme is a project theme, false if it is a framework theme.
    * @param projectResources the resources of the project. The map contains (String, map) pairs
    * where the string is the type of the resource reference used in the layout file, and the
    * map contains (String, {@link ResourceValue}) pairs where the key is the resource name,
    * and the value is the resource value.
    * @param frameworkResources the framework resources. The map contains (String, map) pairs
    * where the string is the type of the resource reference used in the layout file, and the map
    * contains (String, {@link ResourceValue}) pairs where the key is the resource name, and the
    * value is the resource value.
    * @param projectCallback The {@link IProjectCallback} object to get information from
    * the project.
    * @param minSdkVersion the minSdkVersion of the project
    * @param targetSdkVersion the targetSdkVersion of the project
    * @param log the object responsible for displaying warning/errors to the user.
    */
   public SessionParams(
           ILayoutPullParser layoutDescription,
           RenderingMode renderingMode,
           Object projectKey,
           int screenWidth, int screenHeight,
           Density density, float xdpi, float ydpi,
           RenderResources renderResources,
           IProjectCallback projectCallback,
           int minSdkVersion, int targetSdkVersion,
           LayoutLog log) {
       super(projectKey, screenWidth, screenHeight, density, xdpi, ydpi,
               renderResources, projectCallback, minSdkVersion, targetSdkVersion, log);

       mLayoutDescription = layoutDescription;
       mRenderingMode = renderingMode;

   }

   public SessionParams(SessionParams params) {
       super(params);
       mLayoutDescription = params.mLayoutDescription;
       mRenderingMode = params.mRenderingMode;
   }

   public ILayoutPullParser getLayoutDescription() {
       return mLayoutDescription;
   }

   public RenderingMode getRenderingMode() {
       return mRenderingMode;
   }



}







