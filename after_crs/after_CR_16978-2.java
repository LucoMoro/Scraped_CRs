/*GLE2: Let scripts display message dialog & input dialog.

Change-Id:I0d07d5f7e672d3ef6b077c5cf24ba5f20fe1dabb*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/Nullable.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/annotations/Nullable.java
new file mode 100755
//Synthetic comment -- index 0000000..6ea5b36

//Synthetic comment -- @@ -0,0 +1,35 @@
/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ide.eclipse.adt.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Denotes a parameter or field can be null.
 * <p/>
 * When decorating a method call parameter, this denotes the parameter can
 * legitimately be null and the method will gracefully deal with it. Typically used
 * on optional parameters.
 * <p/>
 * When decorating a method, this denotes the method might legitimately return null.
 * <p/>
 * This is a marker annotation and it has no specific attributes.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Nullable {
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/IClientRulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/IClientRulesEngine.java
//Synthetic comment -- index a9ff834..44372ef 100755

//Synthetic comment -- @@ -17,6 +17,10 @@

package com.android.ide.eclipse.adt.editors.layout.gscripts;

import com.android.ide.eclipse.adt.annotations.Nullable;

import groovy.lang.Closure;



/**
//Synthetic comment -- @@ -51,5 +55,24 @@
*   is fast and will return the same rule instance.
*/
IViewRule loadRule(String fqcn);

    /**
     * Displays the given message string in an alert dialog with an "OK" button.
     */
    void displayAlert(String message);

    /**
     * Display a simple input alert dialog with an OK and Cancel buttons.
     *
     * @param message The message to display in the alert dialog.
     * @param value The initial value to display in the input field. Can be null.
     * @param filter An optional closure acting as a filter. It receives the current
     *   string as input. It must return an error string (possibly empty) or false if the
     *   validation fails. Otherwise it should return true or null if validation succeeds.
     * @return Null if canceled by the user. Otherwise the possibly-empty input string.
     * @null Return value is null if dialog was canceled by the user.
     */
    @Nullable
    String displayInput(String message, @Nullable String value, @Nullable Closure filter);
}









//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/MenuAction.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/editors/layout/gscripts/MenuAction.java
//Synthetic comment -- index 3674b02..63454a4 100755

//Synthetic comment -- @@ -16,6 +16,8 @@

package com.android.ide.eclipse.adt.editors.layout.gscripts;

import com.android.ide.eclipse.adt.annotations.Nullable;

import groovy.lang.Closure;

import java.util.Map;
//Synthetic comment -- @@ -162,6 +164,7 @@
* An optional group id, to place the action in a given sub-menu.
* @null This value can be null.
*/
        @Nullable
private final String mGroupId;

/**
//Synthetic comment -- @@ -201,6 +204,7 @@
* Returns the optional id of an existing group or null
* @null This value can be null.
*/
        @Nullable
public String getGroupId() {
return mGroupId;
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/gre/RulesEngine.java
//Synthetic comment -- index 7790411..cbfe763 100755

//Synthetic comment -- @@ -49,6 +49,10 @@
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;

import groovy.lang.Closure;
import groovy.lang.ExpandoMetaClass;
//Synthetic comment -- @@ -69,11 +73,6 @@
import java.util.List;
import java.util.Map;

/**
* The rule engine manages the groovy rules files and interacts with them.
* There's one {@link RulesEngine} instance per layout editor.
//Synthetic comment -- @@ -814,6 +813,46 @@
public IViewRule loadRule(String fqcn) {
return RulesEngine.this.loadRule(fqcn, fqcn);
}

        public void displayAlert(String message) {
            MessageDialog.openInformation(
                    AdtPlugin.getDisplay().getActiveShell(),
                    mFqcn,  // title
                    message);
        }

        public String displayInput(String message, String value, final Closure filter) {
            IInputValidator validator = null;
            if (filter != null) {
                validator = new IInputValidator() {
                    public String isValid(String newText) {
                        Object result = RulesEngine.this.callClosure(filter, newText);

                        if (result instanceof String) {
                            return (String) result;

                        } else if (Boolean.FALSE.equals(result)) {
                            // Returns an empty string to indicate an undescribed error
                            return "";  //$NON-NLS-1$
                        }

                        // Return null to indicate success of validation
                        return null;
                    }
                };
            }

            InputDialog d = new InputDialog(
                        AdtPlugin.getDisplay().getActiveShell(),
                        mFqcn,  // title
                        message,
                        value == null ? "" : value, //$NON-NLS-1$
                        validator);
            if (d.open() == Window.OK) {
                return d.getValue();
            }
            return null;
        }
}

}







