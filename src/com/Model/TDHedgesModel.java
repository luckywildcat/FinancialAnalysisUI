package com.Model;

import java.util.List;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import com.Data.OExpiry;
import com.Data.OS;
import com.Data.TDTransactionSet;
import com.Data.Transaction;
import com.Utils.Formatter;

public class TDHedgesModel extends AbstractTreeTableModel{

    private final static String[] COLUMN_NAMES = {"Contract", "Strike", "Price", "Market", "Size", "Effective", "Last", "Cost", "Value", "UP", "EXCH", "Time"};
    private List<TDTransactionSet> transactionSets;

    public TDHedgesModel(List<TDTransactionSet> departmentList) {
        super(new Object());
        this.transactionSets = departmentList;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
    
    @Override
    public boolean isCellEditable(Object node, int column) {
        return false;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof Transaction;
    }
    
    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof TDTransactionSet) {
        	TDTransactionSet dept = (TDTransactionSet) parent;
        	return dept.getTransactions().size();
        }
        return transactionSets.size();
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof TDTransactionSet) {
        	TDTransactionSet dept = (TDTransactionSet) parent;
        	return dept.getTransactions().get(index);
        }
        return transactionSets.get(index);
    }
    
    @Override
    public int getIndexOfChild(Object parent, Object child) {
    	TDTransactionSet dept = (TDTransactionSet) parent;
    	Transaction emp = (Transaction) child;
        return dept.getTransactions().indexOf(emp);
    }
    
    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof TDTransactionSet) {
        	TDTransactionSet dept = (TDTransactionSet) node;
            switch (column) {
                case 0:
                    return dept.getName() ;
            }
        } else if (node instanceof Transaction) {
        	Transaction trans = (Transaction) node;
            switch (column) {
            	case 0:
            		return trans.getSymbol();
            	case 1:
            		return trans.getStrike();
            	case 2:
            		return trans.getPrice();
            	case 3:
            		return trans.getMarket();
            	case 4:
            		return Formatter.formatInt(trans.getSize());
            	case 5:
            		return trans.getEffectivePrice();
            	case 6:
            		return trans.getLastPrice();
            	case 7:
                	return Formatter.trimToDigit(""+trans.getTransactionCost());
            	case 8:
            		return Formatter.trimToDigit(""+trans.getLatestValue());
            	case 9:
            		return trans.getUnderlyingPrice();
            	case 10:
            		return trans.getExchange();
            	case 11:
            		return trans.getTimeStamp();
            }
        }
        return null;
    }

}
