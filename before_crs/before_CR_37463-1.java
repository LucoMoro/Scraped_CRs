/*Add window docking support for applying flyout prefs

Change-Id:Ib8c6cdd2222289aea67e3f190d6b4417137a68b5*/
//Synthetic comment -- diff --git a/propertysheet/src/org/eclipse/wb/core/controls/flyout/FlyoutControlComposite.java b/propertysheet/src/org/eclipse/wb/core/controls/flyout/FlyoutControlComposite.java
//Synthetic comment -- index 23d2f83..0deb101 100644

//Synthetic comment -- @@ -31,7 +31,6 @@
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Tracker;
import org.eclipse.wb.core.controls.Messages;
import org.eclipse.wb.draw2d.IColorConstants;
//Synthetic comment -- @@ -273,17 +272,30 @@
}

// BEGIN ADT MODIFICATIONS
/** If the flyout hover is showing, dismiss it */
public void dismissHover() {
      if (m_flyoutContainer != null) {
          m_flyoutContainer.dismissHover();
      }
}

/** Sets a listener to be modified when windows are opened, collapsed and expanded */
public void setListener(IFlyoutListener listener) {
      assert m_listener == null; // Only one listener supported
      m_listener = listener;
}
private IFlyoutListener m_listener;
// END ADT MODIFICATIONS







