/*Disallow Java keywords in resource names

If you create a resource file that is a Java keyword, Bad Stuff
happens - it doesn't even make it into the R file.

This modifiers the resource name validator (used among other places in
the New XML File wizard) to disallow these names along with a suitable
error message.

Change-Id:Ic807bb9194d316f227bf3435509632374113563d*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ResourceNameValidator.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newxmlfile/ResourceNameValidator.java
//Synthetic comment -- index ee91f66..013e5e2 100644

//Synthetic comment -- @@ -26,6 +26,8 @@
import com.android.ide.eclipse.adt.internal.resources.manager.ResourceManager;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jface.dialogs.IInputValidator;

import java.util.HashSet;
//Synthetic comment -- @@ -73,6 +75,12 @@
}
}

            String level = "1.5"; //$NON-NLS-1$
            IStatus validIdentifier = JavaConventions.validateIdentifier(newText, level, level);
            if (!validIdentifier.isOK()) {
                return String.format("%1$s is not a valid name (reserved Java keyword)", newText);
            }

if (mExisting != null && mExisting.contains(newText)) {
return String.format("%1$s already exists", newText);
}







