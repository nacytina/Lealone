/*
 * Copyright 2004-2013 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package org.lealone.dbobject.table;

import org.lealone.command.dml.Select;
import org.lealone.expression.Expression;
import org.lealone.expression.ExpressionColumn;
import org.lealone.value.Value;

/**
 * The single column resolver is like a table with exactly one row.
 * It is used to parse a simple one-column check constraint.
 */
public class SingleColumnResolver implements ColumnResolver {

    private final Column column;
    private Value value;

    SingleColumnResolver(Column column) {
        this.column = column;
    }

    public String getTableAlias() {
        return null;
    }

    void setValue(Value value) {
        this.value = value;
    }

    public Value getValue(Column col) {
        return value;
    }

    public Column[] getColumns() {
        return new Column[] { column };
    }

    public String getSchemaName() {
        return null;
    }

    public TableFilter getTableFilter() {
        return null;
    }

    public Select getSelect() {
        return null;
    }

    public Column[] getSystemColumns() {
        return null;
    }

    public Column getRowIdColumn() {
        return null;
    }

    public Expression optimize(ExpressionColumn expressionColumn, Column col) {
        return expressionColumn;
    }

}
