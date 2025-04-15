/*Added view introspection to ChimpChat and MonkeyRunner

Change-Id:I0e44f6d2c51c99cb0409087a77e2916b630051da*/




//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/ChimpManager.java b/chimpchat/src/com/android/chimpchat/ChimpManager.java
//Synthetic comment -- index c68b7df..73851b8 100644

//Synthetic comment -- @@ -19,6 +19,7 @@
import com.google.common.collect.Lists;

import com.android.chimpchat.core.PhysicalButton;
import com.android.chimpchat.core.ChimpRect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
//Synthetic comment -- @@ -28,6 +29,7 @@
import java.net.Socket;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
//Synthetic comment -- @@ -158,6 +160,7 @@
*
* @param command the monkey command to send to the device
* @return the (unparsed) response returned from the monkey.
     * @throws IOException on error communicating with the device
*/
private String sendMonkeyEventAndGetResponse(String command) throws IOException {
command = command.trim();
//Synthetic comment -- @@ -209,6 +212,7 @@
*
* @param command the monkey command to send to the device
* @return true on success.
     * @throws IOException on error communicating with the device
*/
private boolean sendMonkeyEvent(String command) throws IOException {
synchronized (this) {
//Synthetic comment -- @@ -243,6 +247,7 @@
*
* @param name name of static variable to get
* @return the value of the variable, or null if there was an error
     * @throws IOException on error communicating with the device
*/
public String getVariable(String name) throws IOException {
synchronized (this) {
//Synthetic comment -- @@ -255,7 +260,9 @@
}

/**
     * Function to get the list of variables from the device.
     * @return the list of variables as a collection of strings
     * @throws IOException on error communicating with the device
*/
public Collection<String> listVariable() throws IOException {
synchronized (this) {
//Synthetic comment -- @@ -270,7 +277,7 @@

/**
* Tells the monkey that we are done for this session.
     * @throws IOException on error communicating with the device
*/
public void done() throws IOException {
// this command just drops the connection, so handle it here
//Synthetic comment -- @@ -281,7 +288,7 @@

/**
* Tells the monkey that we are done forever.
     * @throws IOException on error communicating with the device
*/
public void quit() throws IOException {
// this command drops the connection, so handle it here
//Synthetic comment -- @@ -296,7 +303,6 @@
* @param x the x coordinate of where to click
* @param y the y coordinate of where to click
* @return success or not
* @throws IOException on error communicating with the device
*/
public boolean tap(int x, int y) throws IOException {
//Synthetic comment -- @@ -308,7 +314,7 @@
*
* @param text the string to type
* @return success
     * @throws IOException on error communicating with the device
*/
public boolean type(String text) throws IOException {
// The network protocol can't handle embedded line breaks, so we have to handle it
//Synthetic comment -- @@ -336,7 +342,7 @@
*
* @param keyChar the character to type.
* @return success
     * @throws IOException on error communicating with the device
*/
public boolean type(char keyChar) throws IOException {
return type(Character.toString(keyChar));
//Synthetic comment -- @@ -344,9 +350,104 @@

/**
* Wake the device up from sleep.
     * @throws IOException on error communicating with the device
*/
public void wake() throws IOException {
sendMonkeyEvent("wake");
}


    /**
     * Retrieves the list of view ids from the current application.
     * @return the list of view ids as a collection of strings
     * @throws IOException on error communicating with the device
     */
    public Collection<String> listViewIds() throws IOException {
        synchronized (this) {
            String response = sendMonkeyEventAndGetResponse("listviews");
            if (!parseResponseForSuccess(response)) {
                Collections.emptyList();
            }
            String extras = parseResponseForExtra(response);
            return Lists.newArrayList(extras.split(" "));
        }
    }

    /**
     * Retrieves the coordinates of the view with the given id.
     * @param id The string id of the view you want to query
     * @return A ChimpRect object with the coordinates of the view
     * @throws IOException on error communicating with the device
     */
    public ChimpRect getViewRectById(String id) throws IOException {
        synchronized(this) {
            String response = sendMonkeyEventAndGetResponse("findviewbyid "+ id);
            if (!parseResponseForSuccess(response)) {
                return new ChimpRect();
            }
            String extras = parseResponseForExtra(response);
            List<String> coords = Lists.newArrayList(extras.split(" "));
            ChimpRect rect = new ChimpRect();
            rect.left = Integer.parseInt(coords.get(0));
            rect.top = Integer.parseInt(coords.get(1));
            // The third param returned from Monkey is the width of the view
            rect.right = Integer.parseInt(coords.get(2))+rect.left;
            // The fourth param returned from Monkey is the height of the view
            rect.bottom = Integer.parseInt(coords.get(3))+rect.top;
            return rect;
        }
    }

    /**
     * Retrives the text contained by the view with the given id.
     * @param id The string id of the view you want to query
     * @return the views text
     * @throws IOException on error communicating with the device
     */
    public String getViewTextById(String id) throws IOException {
        synchronized(this) {
            String response = sendMonkeyEventAndGetResponse("gettextbyid "+id);
            if (!parseResponseForSuccess(response)) {
                return "";
            }
            return parseResponseForExtra(response);
        }
    }

    /**
     * Retrieves the class name of the view with the given id.
     * @param id The string id of the view you want to query
     * @return the class name of the of the view
     * @throws IOException on error communicating with the device
     */
    public String getViewClassById(String id) throws IOException {
        synchronized(this) {
            String response = sendMonkeyEventAndGetResponse("getclassbyid "+id);
            if (!parseResponseForSuccess(response)) {
                return "";
            }
            return parseResponseForExtra(response);
        }
    }


    /**
     * Retrieves the state of the view with the given id.
     * @param id The string id of the view you want to query
     * @return a boolean state for the view. For checkboxes it will return true if the checkbox
     * is checked, false otherwise. Similarly for RadioButtons and ToggleButtons, it will return
     * true if they are checked,, and false otherwise.
     * @throws IOException on error communicating with the device
     */
    public boolean getViewStateById(String id) throws IOException {
        synchronized(this) {
            String response = sendMonkeyEventAndGetResponse("getstatebyid "+id);
            if (!parseResponseForSuccess(response)) {
                // False seems like a reasonable default for items that I can't retrieve their
                // checked status.
                return false;
            }
            return Boolean.parseBoolean(parseResponseForExtra(response));
        }
    }
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java b/chimpchat/src/com/android/chimpchat/adb/AdbChimpDevice.java
//Synthetic comment -- index cfc0755..1878145 100644

//Synthetic comment -- @@ -29,7 +29,10 @@
import com.android.chimpchat.adb.LinearInterpolator.Point;
import com.android.chimpchat.core.IChimpImage;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.IChimpView;
import com.android.chimpchat.core.ISelector;
import com.android.chimpchat.core.TouchPressType;
import com.android.chimpchat.core.ChimpRect;
import com.android.chimpchat.hierarchyviewer.HierarchyViewer;

import java.io.IOException;
//Synthetic comment -- @@ -566,4 +569,24 @@
}
});
}


    @Override
    public Collection<String> getViewIdList() {
        try {
            return manager.listViewIds();
        } catch(IOException e) {
            LOG.log(Level.SEVERE, "Error retrieving view IDs", e);
            return null;
        }
    }

    @Override
    public IChimpView getView(ISelector selector) {
        IChimpView view = selector.getView();
        view.setManager(manager);
        return view;
    }


}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/By.java b/chimpchat/src/com/android/chimpchat/core/By.java
new file mode 100644
//Synthetic comment -- index 0000000..d732c29

//Synthetic comment -- @@ -0,0 +1,32 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.chimpchat.core;

/**
 * A class that lets you select objects based on different criteria.
 * It operates similar to WebDriver's By class.
 */
public class By {
    /**
     * A method to let you select items by id.
     * @param id The string id of the object you want
     * @return a selector that will select the appropriate item by id
     */
    public static ISelector id(String id) {
        return new SelectorId(id);
    }
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/ChimpRect.java b/chimpchat/src/com/android/chimpchat/core/ChimpRect.java
new file mode 100644
//Synthetic comment -- index 0000000..75ae300

//Synthetic comment -- @@ -0,0 +1,105 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.chimpchat.core;

/**
 * A class for holding information about view locations
 */
public class ChimpRect {
    public int left;
    public int top;
    public int right;
    public int bottom;

    /**
     * Creates an empty ChimpRect object. All coordinates are initialized to 0.
     */
    public ChimpRect() {}

    /**
     * Create a new ChimpRect with the given coordinates.
     * @param left   The X coordinate of the left side of the rectagle
     * @param top    The Y coordinate of the top of the rectangle
     * @param right  The X coordinate of the right side of the rectagle
     * @param bottom The Y coordinate of the bottom of the rectangle
     */
    public ChimpRect(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * A comparison method to determine if the object is equivalent to other ChimpRects.
     * @param obj The object to compare it to
     * @return True if the object is an equivalent rectangle, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ChimpRect){
            ChimpRect r = (ChimpRect) obj;
            if (r != null) {
                return left == r.left && top == r.top && right == r.right
                        && bottom == r.bottom;
            }
        }
        return false;
    }

    /**
     * The width of the ChimpRect
     * @return the width of the rectangle
     */
    public int getWidth() {
        return right-left;
    }

    /**
     * The height of the ChimpRect
     * @return the height of the rectangle
     */
    public int getHeight() {
        return bottom-top;
    }

    /**
     * Returns a 2 item int array with the x, y coordinates of the center of the ChimpRect.
     * @return a 2 item int array. The first item is the x value of the center of the ChimpRect and
     * the second item is the y value.
     */
    public int[] getCenter() {
        int[] center = new int[2];
        center[0] = left+getWidth()/2;
        center[1] = top+getHeight()/2;
        return center;
    }

    /**
     * Returns a representation of the rectangle in string form
     * @return a string representation of the rectangle
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("ChimpRect ");
        sb.append("top: ").append(top).append(" ");
        sb.append("right: ").append(right).append(" ");
        sb.append("bottom: ").append(bottom).append(" ");
        sb.append("left: ").append(left).append(" ");
        return sb.toString();
    }
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/ChimpViewId.java b/chimpchat/src/com/android/chimpchat/core/ChimpViewId.java
new file mode 100644
//Synthetic comment -- index 0000000..b3ccfda

//Synthetic comment -- @@ -0,0 +1,92 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.chimpchat.core;

import com.android.chimpchat.ChimpManager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* A class for querying a view object by its id */
public class ChimpViewId implements IChimpView {
    private static final Logger LOG = Logger.getLogger(ChimpViewId.class.getName());

    private String id;
    private ChimpManager manager;

    public ChimpViewId(String id) {
        this.id = id;
    }

    public void setManager(ChimpManager manager) {
        this.manager = manager;
    }
    /**
     * Get the coordinates for the view with the given id.
     * @return a ChimpRect object with the coordinates for the corners of the view
     */
    public ChimpRect getLocation() {
        try {
            return manager.getViewRectById(id);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, "Error retrieving view");
            return null;
        }
    }

    /**
     * Retrieve the text contained by the view
     * @return the text contained by the view
     */
    public String getText() {
        try {
            return manager.getViewTextById(id);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, "Error retrieving view text");
            return null;
        }
    }

    /**
     * Get the class of the view
     * @return the class name of the view
     */
    public String getViewClass(){
        try {
            return manager.getViewClassById(id);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, "Error retrieving view class");
            return null;
        }
    }

    /**
     * Get the state of the view. If the view is a CheckBox, it will return true
     * if its checked, false otherwise. Similarly for a RadioButton or a ToggleButton,
     * it will return true if the button is selected and false otherwise.
     * @return the state of the view
     */
    public boolean getState(){
        try {
            return manager.getViewStateById(id);
        } catch(IOException e) {
            LOG.log(Level.SEVERE, "Error retrieving view state");
            return false;
        }
    }
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/IChimpDevice.java b/chimpchat/src/com/android/chimpchat/core/IChimpDevice.java
//Synthetic comment -- index 03dc09d..82479ee 100644

//Synthetic comment -- @@ -201,4 +201,16 @@
* Wake up the screen on the device.
*/
void wake();

    /**
     * List the possible view ID strings from the current applications resource file
     * @return the list of view id strings
     */
    Collection<String> getViewIdList();

    /**
     * Retrieve the view object for the view with the given id.
     * @return a view object for the view with the given id
     */
    IChimpView getView(ISelector selector);
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/IChimpView.java b/chimpchat/src/com/android/chimpchat/core/IChimpView.java
new file mode 100644
//Synthetic comment -- index 0000000..aaec55d

//Synthetic comment -- @@ -0,0 +1,50 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.chimpchat.core;

import com.android.chimpchat.ChimpManager;

/**
 * An interface for view introspection.
 */
public interface IChimpView {

    /**
     * Set the manager for this view to communicate through.
     */
    void setManager(ChimpManager manager);

    /**
     * Obtain the state of this view.
     */
    boolean getState();

    /**
     * Obtain the class of the view as a string
     */
    String getViewClass();

    /**
     * Obtain the text contained in the view
     */
    String getText();

    /**
     * Obtain the location of the view on the device screen
     */
    ChimpRect getLocation();
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/ISelector.java b/chimpchat/src/com/android/chimpchat/core/ISelector.java
new file mode 100644
//Synthetic comment -- index 0000000..8dc2d33

//Synthetic comment -- @@ -0,0 +1,27 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.chimpchat.core;

/**
 * An interface for selectors
 */
public interface ISelector {
    /**
     * A method that allows you to get a view based on the give selector type
     */
    IChimpView getView();
}








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/PhysicalButton.java b/chimpchat/src/com/android/chimpchat/core/PhysicalButton.java
//Synthetic comment -- index 1da571e..8faabdd 100644

//Synthetic comment -- @@ -16,10 +16,10 @@
package com.android.chimpchat.core;

public enum PhysicalButton {
    HOME("KEYCODE_HOME"),
    SEARCH("KEYCODE_SEARCH"),
    MENU("KEYCODE_MENU"),
    BACK("KEYCODE_BACK"),
DPAD_UP("DPAD_UP"),
DPAD_DOWN("DPAD_DOWN"),
DPAD_LEFT("DPAD_LEFT"),








//Synthetic comment -- diff --git a/chimpchat/src/com/android/chimpchat/core/SelectorId.java b/chimpchat/src/com/android/chimpchat/core/SelectorId.java
new file mode 100644
//Synthetic comment -- index 0000000..87ec28a

//Synthetic comment -- @@ -0,0 +1,36 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.chimpchat.core;

/* A class for selecting objects by their id */
public class SelectorId implements ISelector {
    private String id;
    /**
     * @param id the id to select objects by
     */
    public SelectorId(String id){
        this.id = id;
    }

    /**
     * A method for selecting a view by the given id.
     * @return The view with the given id
     */
    public IChimpView getView() {
        return new ChimpViewId(id);
    }
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyDevice.java
//Synthetic comment -- index 61e92a0..d205b47 100644

//Synthetic comment -- @@ -20,6 +20,8 @@
import com.google.common.collect.Collections2;

import com.android.chimpchat.ChimpChat;
import com.android.chimpchat.core.By;
import com.android.chimpchat.core.IChimpView;
import com.android.chimpchat.core.IChimpDevice;
import com.android.chimpchat.core.IChimpImage;
import com.android.chimpchat.core.TouchPressType;
//Synthetic comment -- @@ -31,6 +33,7 @@
import org.python.core.ClassDictInit;
import org.python.core.Py;
import org.python.core.PyDictionary;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyTuple;

//Synthetic comment -- @@ -357,4 +360,36 @@

impl.wake();
}


    @MonkeyRunnerExported(doc = "Retrieve the properties that can be queried")
    public PyList getPropertyList(PyObject[] args, String[] kws) {
        ArgParser ap = JythonUtils.createArgParser(args, kws);
        Preconditions.checkNotNull(ap);
        Collection<String> properties = impl.getPropertyList();
        return new PyList(properties);
    }

    @MonkeyRunnerExported(doc = "Retrieve the view ids for the current application")
    public PyList getViewIdList(PyObject[] args, String[] kws) {
        ArgParser ap = JythonUtils.createArgParser(args, kws);
        Preconditions.checkNotNull(ap);
        Collection<String> viewIds = impl.getViewIdList();
        return new PyList(viewIds);
    }

     //Because the pythonic way is to have flatter hierarchies, rather than doing the
     //findView(By.id("foo")) style the java code uses, I'm going to expose them as individual
     //method calls. This is similar to WebDriver's python bindings.
    @MonkeyRunnerExported(doc = "Obtains the view with the specified id.",
                          args = {"id"},
                          argDocs = {"The id of the view to retrieve."},
                          returns = "The view object with the specified id.")
    public MonkeyView getViewById(PyObject[] args, String[] kws) {
        ArgParser ap = JythonUtils.createArgParser(args, kws);
        Preconditions.checkNotNull(ap);
        String id = ap.getString(0);
        IChimpView view = impl.getView(By.id(id));
        return new MonkeyView(view);
    }
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyRect.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyRect.java
new file mode 100644
//Synthetic comment -- index 0000000..98b2ecc

//Synthetic comment -- @@ -0,0 +1,85 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.monkeyrunner;

import com.android.chimpchat.core.ChimpRect;

import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.PyInteger;
import org.python.core.PyList;
import org.python.core.PyObject;

import java.util.List;
import java.util.LinkedList;
import java.util.logging.Logger;

/*
 * A Jython wrap for the ChimpRect class that stores coordinate information for views
 */
@MonkeyRunnerExported(doc = "Represents the coordinates of a rectangular object")
public class MonkeyRect extends PyObject implements ClassDictInit {
    private static final Logger LOG = Logger.getLogger(MonkeyRect.class.getName());

    private ChimpRect rect;

    @MonkeyRunnerExported(doc = "The x coordinate of the left side of the rectangle")
    public int left;
    @MonkeyRunnerExported(doc = "The y coordinate of the top side of the rectangle")
    public int top;
    @MonkeyRunnerExported(doc = "The x coordinate of the right side of the rectangle")
    public int right;
    @MonkeyRunnerExported(doc = "The y coordinate of the bottom side of the rectangle")
    public int bottom;

    public static void classDictInit(PyObject dict) {
        JythonUtils.convertDocAnnotationsForClass(MonkeyRect.class, dict);
    }

    public MonkeyRect(ChimpRect rect) {
        this.rect = rect;
        this.left = rect.left;
        this.right = rect.right;
        this.top = rect.top;
        this.bottom = rect.bottom;
    }

    @MonkeyRunnerExported(doc = "Returns the width of the rectangle",
                          returns = "The width of the rectangle as an integer")
    public PyInteger getWidth() {
        return new PyInteger(right-left);
    }

    @MonkeyRunnerExported(doc = "Returns the height of the rectangle",
                          returns = "The height of the rectangle as an integer")
    public PyInteger getHeight() {
        return new PyInteger(bottom-top);
    }

    @MonkeyRunnerExported(doc = "Returns a two item list that contains the x and y value of " +
                          "the center of the rectangle",
                          returns = "The center coordinates as a two item list of integers")
    public PyList getCenter(){
        List<PyInteger> center = new LinkedList<PyInteger>();
        /* Center x coordinate */
        center.add(new PyInteger(left+(right-left)/2));
        /* Center y coordinate */
        center.add(new PyInteger(top+(bottom-top)/2));
        return new PyList(center);
    }
}








//Synthetic comment -- diff --git a/monkeyrunner/src/com/android/monkeyrunner/MonkeyView.java b/monkeyrunner/src/com/android/monkeyrunner/MonkeyView.java
new file mode 100644
//Synthetic comment -- index 0000000..3960fa0

//Synthetic comment -- @@ -0,0 +1,82 @@
/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.monkeyrunner;

import com.google.common.base.Preconditions;

import com.android.chimpchat.core.IChimpView;

import com.android.monkeyrunner.doc.MonkeyRunnerExported;

import org.python.core.ArgParser;
import org.python.core.ClassDictInit;
import org.python.core.PyBoolean;
import org.python.core.PyObject;
import org.python.core.PyString;

import java.util.logging.Logger;

/*
 * Jython wrapper for the ChimpView class
 */
@MonkeyRunnerExported(doc = "Represents a view object.")
public class MonkeyView extends PyObject implements ClassDictInit {
    private static final Logger LOG = Logger.getLogger(MonkeyView.class.getName());

    private IChimpView impl;

    public static void classDictInit(PyObject dict) {
        JythonUtils.convertDocAnnotationsForClass(MonkeyView.class, dict);
    }

    public MonkeyView(IChimpView impl) {
        this.impl = impl;
    }

    @MonkeyRunnerExported(doc = "Returns the boolean state of this view. For CheckBoxes it " +
                          "returns true if the box is checked, and false otherwise. It operates " +
                          "similarly for RadioButtons, ToggleButtons, etc.",
                          returns = "A boolean value for whether the item is checked or not")
    public PyBoolean getState(PyObject[] args, String[] kws) {
        ArgParser ap = JythonUtils.createArgParser(args, kws);
        Preconditions.checkNotNull(ap);
        return new PyBoolean(impl.getState());
    }

    @MonkeyRunnerExported(doc = "Returns the class name of the view",
                          returns = "The class name of the view as a string")
    public PyString getViewClass(PyObject[] args, String[] kws) {
        ArgParser ap = JythonUtils.createArgParser(args, kws);
        Preconditions.checkNotNull(ap);
        return new PyString(impl.getViewClass());
    }

    @MonkeyRunnerExported(doc = "Returns the text contained by the view",
                          returns = "The text contained in the view")
    public PyString getText(PyObject[] args, String[] kws) {
        ArgParser ap = JythonUtils.createArgParser(args, kws);
        Preconditions.checkNotNull(ap);
        return new PyString(impl.getText());
    }

    @MonkeyRunnerExported(doc = "Returns the location of the view in the form of a MonkeyRect",
                          returns = "The location of the view as a MonkeyRect object")
    public MonkeyRect getLocation(PyObject[] args, String[] kws) {
        ArgParser ap = JythonUtils.createArgParser(args, kws);
        Preconditions.checkNotNull(ap);
        return new MonkeyRect(impl.getLocation());
    }
}







