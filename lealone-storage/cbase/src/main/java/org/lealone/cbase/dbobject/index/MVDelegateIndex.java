/*
 * Copyright 2004-2014 H2 Group. Multiple-Licensed under the MPL 2.0,
 * and the EPL 1.0 (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.lealone.cbase.dbobject.index;

import java.util.List;

import org.lealone.cbase.dbobject.table.MVTable;
import org.lealone.dbobject.index.BaseIndex;
import org.lealone.dbobject.index.Cursor;
import org.lealone.dbobject.index.IndexType;
import org.lealone.dbobject.table.Column;
import org.lealone.dbobject.table.IndexColumn;
import org.lealone.engine.Session;
import org.lealone.message.DbException;
import org.lealone.result.Row;
import org.lealone.result.SearchRow;
import org.lealone.result.SortOrder;
import org.lealone.value.ValueLong;

/**
 * An index that delegates indexing to another index.
 */
public class MVDelegateIndex extends BaseIndex implements CBaseIndex {

    private final MVPrimaryIndex mainIndex;

    public MVDelegateIndex(MVTable table, int id, String name, MVPrimaryIndex mainIndex, IndexType indexType) {
        IndexColumn[] cols = IndexColumn.wrap(new Column[] { table.getColumn(mainIndex.getMainIndexColumn()) });
        this.initBaseIndex(table, id, name, cols, indexType);
        this.mainIndex = mainIndex;
        if (id < 0) {
            throw DbException.throwInternalError("" + name);
        }
    }

    @Override
    public void addRowsToBuffer(List<Row> rows, String bufferName) {
        throw DbException.throwInternalError();
    }

    @Override
    public void addBufferedRows(List<String> bufferNames) {
        throw DbException.throwInternalError();
    }

    @Override
    public void add(Session session, Row row) {
        // nothing to do
    }

    @Override
    public boolean canGetFirstOrLast() {
        return true;
    }

    @Override
    public void close(Session session) {
        // nothing to do
    }

    @Override
    public Cursor find(Session session, SearchRow first, SearchRow last) {
        ValueLong min = mainIndex.getKey(first, MVPrimaryIndex.MIN, MVPrimaryIndex.MIN);
        // ifNull is MIN_VALUE as well, because the column is never NULL
        // so avoid returning all rows (returning one row is OK)
        ValueLong max = mainIndex.getKey(last, MVPrimaryIndex.MAX, MVPrimaryIndex.MIN);
        return mainIndex.find(session, min, max);
    }

    @Override
    public Cursor findFirstOrLast(Session session, boolean first) {
        return mainIndex.findFirstOrLast(session, first);
    }

    @Override
    public int getColumnIndex(Column col) {
        if (col.getColumnId() == mainIndex.getMainIndexColumn()) {
            return 0;
        }
        return -1;
    }

    //    @Override
    //    public double getCost(Session session, int[] masks, TableFilter filter, SortOrder sortOrder) {
    //        return 10 * getCostRangeIndex(masks, mainIndex.getRowCountApproximation(), filter, sortOrder);
    //    }

    @Override
    public double getCost(Session session, int[] masks, SortOrder sortOrder) {
        return 10 * getCostRangeIndex(masks, mainIndex.getRowCountApproximation(), sortOrder);
    }

    @Override
    public boolean needRebuild() {
        return false;
    }

    @Override
    public void remove(Session session, Row row) {
        // nothing to do
    }

    @Override
    public void remove(Session session) {
        mainIndex.setMainIndexColumn(-1);
    }

    @Override
    public void truncate(Session session) {
        // nothing to do
    }

    @Override
    public void checkRename() {
        // ok
    }

    @Override
    public long getRowCount(Session session) {
        return mainIndex.getRowCount(session);
    }

    @Override
    public long getRowCountApproximation() {
        return mainIndex.getRowCountApproximation();
    }

    @Override
    public long getDiskSpaceUsed() {
        return 0;
    }

}
