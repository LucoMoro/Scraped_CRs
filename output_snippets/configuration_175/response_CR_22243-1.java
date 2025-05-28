//<Beginning of snippet n. 0>
package com.android.ide.common.layout;

import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;

import java.util.List;
import java.util.ArrayList;

/**
* An {@link IViewRule} for android.widget.TableLayout.
* The default is vertical, not horizontal
* The fill of all children should be wrap_content
*/
public class TableLayoutRule implements IViewRule {
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
        if (row != null) {
            rows.add(row);
        }
    }

    public void removeRow(int index) {
        if (index >= 0 && index < rows.size()) {
            rows.remove(index);
        }
    }

    @Override
    protected boolean isVertical(INode node) {
        return true; // Tables are always vertical
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package com.android.ide.common.layout;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

import java.util.List;
import java.util.ArrayList;

/**
* An {@link IViewRule} for android.widget.TableRow.
*/
public class TableRowRule implements IViewRule {
    private List<TableRow> rows;

    public TableRowRule() {
        rows = new ArrayList<>();
    }

    public void addRow(TableRow row) {
        if (row != null) {
            rows.add(row);
            // Assuming TableLayoutRule maintains the state, notify it about the row addition
            // For cohesive behavior, it should call an appropriate method on the TableLayoutRule instance
        }
    }

    public void removeRow(int index) {
        if (index >= 0 && index < rows.size()) {
            rows.remove(index);
            // Assuming TableLayoutRule maintains the state, notify it about the row removal
            // For cohesive behavior, it should call an appropriate method on the TableLayoutRule instance
        }
    }
}

//<End of snippet n. 1>