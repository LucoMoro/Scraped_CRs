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
public class TableLayoutRule implements IViewRule {
    private List<TableRow> rows;

    public TableLayoutRule() {
        rows = new ArrayList<>();
        initializePredefinedRows();
    }

    @Override
    protected boolean isVertical(INode node) {
        // Tables are always vertical
        return true;
    }

    public void addRow(TableRow row) {
        if (row != null) {
            rows.add(row);
        }
    }

    public void removeRow(TableRow row) {
        rows.remove(row);
    }

    private void initializePredefinedRows() {
        for (int i = 0; i < 4; i++) {
            TableRow row = new TableRow(); // Assuming TableRow has a default constructor
            addRow(row);
        }
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
public class TableRowRule implements IViewRule {
    // Implementation for TableRow handling
}

//<End of snippet n. 1>