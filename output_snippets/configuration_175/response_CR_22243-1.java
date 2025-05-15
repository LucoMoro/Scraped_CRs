//<Beginning of snippet n. 0>
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;

import java.util.List;

/**
 * An {@link IViewRule} for android.widget.TableLayout.
 * The default is vertical, not horizontal
 * The fill of all children should be wrap_content
 */

public class TableLayoutRule extends IViewRule {

    public TableLayoutRule() {
        // Initialize default TableRow instances
        for (int i = 0; i < 4; i++) {
            addRow(new TableRow());
        }
    }

    @Override
    protected boolean isVertical(INode node) {
        // Tables are always vertical
        return true;
    }

    public void addRow(TableRow row) {
        // Code to add a row to the TableLayout
    }

    public void removeRow(TableRow row) {
        // Code to remove a row from the TableLayout
    }
}

//<End of snippet n. 0>


//<Beginning of snippet n. 1>
package com.android.ide.common.layout;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

/**
 * An {@link IViewRule} for android.widget.TableRow.
 */
public class TableRowRule extends IViewRule {

    public TableRowRule() {
        // Constructor
    }

    public void setLayoutParams() {
        // Ensure layout parameters for TableRow are wrap_content
    }
}

//<End of snippet n. 1>