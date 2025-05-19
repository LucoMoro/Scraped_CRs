//<Beginning of snippet n. 0>
import com.android.ide.common.api.IMenuCallback;
import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;
import com.android.ide.common.api.MenuAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableLayout implements IViewRule {
    private final List<TableRow> rows;

    public TableLayout() {
        this.rows = Collections.synchronizedList(new ArrayList<>());
        initializeTable();
    }

    private void initializeTable() {
        for (int i = 0; i < 4; i++) {
            addRow();
        }
    }

    public synchronized void addRow() {
        TableRow newRow = new TableRow();
        newRow.notifyTableLayout(this);
        rows.add(newRow);
        notifyTableLayout();
    }

    public synchronized void removeRow(int index) {
        if (index < 0 || index >= rows.size()) {
            throw new IndexOutOfBoundsException("Invalid index for row removal.");
        }
        rows.remove(index);
        notifyTableLayout();
    }
    
    public synchronized List<TableRow> getRows() {
        return new ArrayList<>(rows);
    }

    private void notifyTableLayout() {
        // Notify other components or listeners if necessary (implementation not shown)
    }

    @Override
    protected boolean isVertical(INode node) {
        return true;
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>
package com.android.ide.common.layout;

import com.android.ide.common.api.INode;
import com.android.ide.common.api.IViewRule;
import com.android.ide.common.api.InsertType;

public class TableRow implements IViewRule {
    private TableLayout tableLayout;

    public void notifyTableLayout(TableLayout tableLayout) {
        this.tableLayout = tableLayout;
    }

    public void removeRow() {
        if (tableLayout != null) {
            int index = getRowIndex();
            if(index >= 0) {
                tableLayout.removeRow(index);
            } else {
                throw new IllegalStateException("Row is not associated with any TableLayout.");
            }
        } else {
            throw new IllegalStateException("TableLayout is not set.");
        }
    }

    private int getRowIndex() {
        return tableLayout.getRows().indexOf(this);
    }
    
    // Implement other necessary methods as needed
}

//<End of snippet n. 1>