package com.Model;

import java.util.List;

import org.jdesktop.swingx.treetable.AbstractTreeTableModel;

import com.Data.OExpiry;
import com.Data.OS;
import com.Data.TD;
import com.Utils.Formatter;

public class TDTreeModel extends AbstractTreeTableModel {
	
    private final static String[] COLUMN_NAMES = {"Option", "Strike", "Low", "Avg", "High", "Last", "Vol", "Analyze"};
    private List<OExpiry> optionStrikesExpiriesList;
    private TD cachedResults;

    public TDTreeModel(List<OExpiry> departmentList, TD res) {
        super(new Object());
        this.optionStrikesExpiriesList = departmentList;
        this.cachedResults = res;
    }

    public List<OExpiry> getOptionStrikesExpiriesList() {
		return optionStrikesExpiriesList;
	}

	public void setOptionStrikesExpiriesList(List<OExpiry> optionStrikesExpiriesList) {
		this.optionStrikesExpiriesList = optionStrikesExpiriesList;
	}

	public TD getCachedResults() {
		return cachedResults;
	}

	public void setCachedResults(TD cachedResults) {
		this.cachedResults = cachedResults;
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
        return true;
    }

    @Override
    public boolean isLeaf(Object node) {
        return node instanceof OS;
    }
    
    @Override
    public int getChildCount(Object parent) {
        if (parent instanceof OExpiry) {
        	OExpiry dept = (OExpiry) parent;
            return dept.getCallStrikes().size()+dept.getPutStrikes().size();
        }
        return optionStrikesExpiriesList.size();
    }

    @Override
    public Object getChild(Object parent, int index) {
        if (parent instanceof OExpiry) {
        	OExpiry dept = (OExpiry) parent;
        	if(index<dept.getCallStrikes().size())
        		return dept.getCallStrikes().get(index);
        	else return dept.getPutStrikes().get(index-dept.getCallStrikes().size());
        }
        return optionStrikesExpiriesList.get(index);
    }
    
    @Override
    public int getIndexOfChild(Object parent, Object child) {
    	OExpiry dept = (OExpiry) parent;
        OS emp = (OS) child;
        return dept.getCallStrikes().indexOf(emp);
    }
    
    @Override
    public Object getValueAt(Object node, int column) {
        if (node instanceof OExpiry) {
        	OExpiry dept = (OExpiry) node;
        	if(dept.isAnalysis()){
                switch (column) {
                    case 0:
                		return dept.getExpiry();                
                		}

        	}
            switch (column) {
                case 0:
                    return Formatter.convertDate(dept.getExpiry()) + "  C: "+ Formatter.formatInt(dept.getTotalCallVol()) +"  P: "+Formatter.formatInt(dept.getTotalPutVol());
            }
        } else if (node instanceof OS) {
        	OS emp = (OS) node;
            switch (column) {
                case 0:
                    return emp.getSym();
                case 1:
                    return ""+emp.getStrike();
                case 2:
                    return emp.getLow();
                case 3:{// cut off excess digits xx.xx
                	String s = emp.getAvg();
                	return Formatter.trimToDigit(s);
                }
                case 4:
                	return emp.getHigh();
                case 5:
                	return emp.getLast();
                case 6:
                    return emp.getVol();
                case 7:
                    return "View";
            }
        }
        return null;
    }
}
