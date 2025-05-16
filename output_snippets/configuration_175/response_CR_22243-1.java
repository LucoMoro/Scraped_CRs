//<Beginning of snippet n. 0>
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;

import java.util.ArrayList;
import java.util.List;

/**
* An {@link IViewRule} for android.widget.TableLayout.
* The default is vertical, not horizontal
* The fill of all children should be wrap_content
*/
public class TableLayoutRule extends IViewRule {

    private List<TableRow> rows;

    public TableLayoutRule() {
        rows = new ArrayList<>();
        initializeRows();
    }

    private void initializeRows() {
        for (int i = 0; i < 4; i++) {
            addRow(new TableRow());
        }
    }

    public void addRow(TableRow row) {
        rows.add(row);
        // code to add the row to the layout
    }

    public void removeRow(TableRow row) {
        if (rows.contains(row)) {
            rows.remove(row);
            // code to remove the row from the layout
        }
    }

    @Override
    protected boolean isVertical(INode node) {
        // Tables are always vertical
        return true;
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
    // Implementation for TableRow
}
//<End of snippet n. 1>