package com.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.TreeCellRenderer;

import org.jdesktop.swingx.JXTreeTable;

import com.Data.OExpiry;
import com.Data.OS;
import com.Data.TD;
import com.Model.TDHedgesModel;
import com.Model.TDTreeModel;
import com.Parser.ToSOptionScan;

public class OSMainPanel extends JPanel {
	
	private ToSOptionScan engine;
	private final JLabel statusLabel;
	
    public OSMainPanel() {
    	
    	engine = new ToSOptionScan();
        JFrame frame = new JFrame("HedgeHog");

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());
        
        /*JTable table = new JTable();

        JScrollPane tableContainer = new JScrollPane(table);

        panel.add(tableContainer, BorderLayout.CENTER);*/
        final Container contentPane = frame.getContentPane();
        BorderLayout layout = new BorderLayout();
        contentPane.setLayout(layout);
        
        String[] syms = engine.equity_symbols;
        String[] dates = engine.dates;
        final JComboBox chooseSymbolDropDown = new JComboBox(syms);
        topPanel.add(chooseSymbolDropDown);
        String[] sizes = {"100, 1000"};
        final JComboBox chooseSizeDropDown = new JComboBox(sizes);
        //topPanel.add(chooseSizeDropDown);
        String[] start = { "Start" };
        final JComboBox startBox = new JComboBox(dates);
        topPanel.add(startBox);
        
        String[] end = { "End" };
        final JComboBox endBox = new JComboBox(dates);
        topPanel.add(endBox);
        
        JButton runButton = new JButton();
        runButton.setText("RUN");
        runButton.addActionListener(new ActionListener() { 
            public void actionPerformed(ActionEvent e) { 
                String chosen_symbol = (String) chooseSymbolDropDown.getSelectedItem();
                String chosen_start = (String) startBox.getSelectedItem();
                String chosen_end = (String) endBox.getSelectedItem();
                if(!chosen_symbol.equals("Select") && !chosen_start.equals("Select") && !chosen_end.equals("Select")){

            		TD results = null;
            		String stat = "";
            		String rng = "";
            		if(getDateNum(chosen_start) > getDateNum(chosen_end)){
                		stat = "Scan Start Date must be before End Date.";
                		statusLabel.setForeground(Color.RED);
                		statusLabel.setText(stat);
                		System.out.println(stat);
                	} else if (getDateNum(chosen_start) == getDateNum(chosen_end)){
                		String[] datesToScan = getDateStrings(chosen_start, chosen_end, engine.dates);
                		statusLabel.setForeground(Color.GREEN);
                		stat = "Scan "+chosen_symbol+" on "+chosen_start+" ("+datesToScan.length+").";
                		rng = ""+chosen_start+"-"+chosen_end+" ("+datesToScan.length+" days)";
                		statusLabel.setText(stat);
                		System.out.println(stat);
                		results = engine.scan(chosen_symbol, datesToScan, 100);
                	} else {
                		String[] datesToScan = getDateStrings(chosen_start, chosen_end, engine.dates);
                		statusLabel.setForeground(Color.GREEN);
                		stat = "Scan "+chosen_symbol+" from "+chosen_start+" to "+chosen_end+"  ("+datesToScan.length+").";
                		rng = ""+chosen_start+"-"+chosen_end+" ("+datesToScan.length+" days)";
                		statusLabel.setText(stat);
                		System.out.println(stat);
                		results = engine.scan(chosen_symbol, datesToScan, 100);
                	}
                	if(results!=null){
                		TDTreeModel model = results.generateModel();
                		/*String[][] data = new String[ (results.activeCallOptionStrikesCount.size() + results.activePutOptionStrikesCount.size())][];
                		int count = 0;
                		for(String crtSym : ( results.activeCallOptionStrikesCount.keySet() )){
                			String[] row = {crtSym, 
                							""+results.lowPricesActiveCallOptionStrikes.get(crtSym),
                							""+results.highPricesActiveCallOptionStrikes.get(crtSym),
                							""+results.activeCallOptionStrikesCount.get(crtSym),
                							""+results.avgPricesActiveCallOptionStrikes.get(crtSym)};
                			data[count++] = row;
                		}
                		for(String crtSym : ( results.activePutOptionStrikesCount.keySet() )){
                			String[] row = {crtSym, 
        							""+results.lowPricesActivePutOptionStrikes.get(crtSym),
        							""+results.highPricesActivePutOptionStrikes.get(crtSym),
        							""+results.activePutOptionStrikesCount.get(crtSym),
        							""+results.avgPricesActivePutOptionStrikes.get(crtSym)};
                			data[count++] = row;
                		}*/
                	    JXTreeTable xTable = new JXTreeTable(model);
                	    //xTable.addHighlighter(new ColorHighlighter(HighlightPredicate.EVEN, Color.GRAY, null, null, null));

                	    //xTable.setDefaultRenderer(Object.class, new TableCellRenderer());
                	    xTable.putClientProperty(JXTreeTable.DROP_HACK_FLAG_KEY, Boolean.TRUE);
                	    //xTable.expandAll();
                	    //xTable.setVisibleColumnCount(10);
                	    xTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);        
                	    xTable.setRootVisible(false); 
                	    xTable.setSortable(true);
                	    //JTextField textField = new JTextField(
                	            //"drag me over hierarchical column ..");
                	    //textField.setDragEnabled(true);
                	    xTable.getColumnModel().getColumn(0).setPreferredWidth(250);
                	    
                	    //sp.add(xTable);
                	    
                	    // hedges table

                		TDHedgesModel hedgesModel = results.generateHedgesModel();
                		String[][] hedgesData = new String[ (results.hedges.size())][];
                		/*count = 0;
                		for(String crtSym : ( results.hedges.keySet() )){
                			String[] row = {""+results.hedges.get(crtSym),
                							""+results.highPricesActiveCallOptionStrikes.get(crtSym),
                							""+results.activeCallOptionStrikesCount.get(crtSym),
                							""+results.avgPricesActiveCallOptionStrikes.get(crtSym)};
                			data[count++] = row;
                		}*/

                	    JXTreeTable xTable2 = new JXTreeTable(hedgesModel);
                	    //xTable.addHighlighter(new ColorHighlighter(HighlightPredicate.EVEN, Color.GRAY, null, null, null));

                	    //xTable2.setTreeCellRenderer(new OSTableCellRenderer());
                	    xTable2.putClientProperty(JXTreeTable.DROP_HACK_FLAG_KEY, Boolean.TRUE);
                	    //xTable.expandAll();
                	    //xTable.setVisibleColumnCount(10);     
                	    xTable2.setRootVisible(false); 
                	    xTable2.getColumnModel().getColumn(0).setPreferredWidth(250);
                	    xTable2.getColumnModel().getColumn(3).setPreferredWidth(120);
                	    xTable2.getColumnModel().getColumn(4).setPreferredWidth(50);
                	    xTable2.getColumnModel().getColumn(6).setPreferredWidth(100);
                	    xTable2.getColumnModel().getColumn(7).setPreferredWidth(100);
                	    xTable2.getColumnModel().getColumn(8).setPreferredWidth(100);
                	    xTable2.setSortable(true);
                	    
                	    Action clicked = new AbstractAction()
                	    {
                	        public void actionPerformed(ActionEvent e)
                	        {
                	        	JXTreeTable table = (JXTreeTable)e.getSource();
                	            int modelRow = Integer.valueOf( e.getActionCommand() );
                	            TDTreeModel model = (TDTreeModel)table.getTreeTableModel();
                	            String sym = (String)(table.getModel()).getValueAt(modelRow, 0);
                	            System.out.println("Analyzing "+ sym);
                	            showStrikeAnalysis(sym, model);
                	        }
                	    };
                	     
                	    ButtonColumn buttonColumn = new ButtonColumn(xTable, clicked, 7);
                	    buttonColumn.setMnemonic(KeyEvent.VK_D);
                	    xTable.setEditable(true);
                	    
                	    JScrollPane sp=new JScrollPane(xTable);  
                	    JScrollPane sp2=new JScrollPane(xTable2);  
                	    //sp.add(xTable2);
                	    //JTextField textField = new JTextField(
                	            //"drag me over hierarchical column ..");
                	    //textField.setDragEnabled(true);
                	    showScrollableResults(sp,sp2, results.symbol, rng);
                }
            } 
            }});
        topPanel.add(runButton);
        
        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout());
        
        statusLabel = new JLabel();
        statusLabel.setText("All Systems go.");
        bottomPanel.add(statusLabel);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);
        //engine.scanActive();
        //frame.getContentPane().add(contentPane);
        frame.pack();
        frame.setSize(600, 100);
        frame.setVisible(true);
    }

    public class OSTableCellRenderer extends JLabel implements TreeCellRenderer {

        @Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
        	TDHedgesModel model = (TDHedgesModel)tree.getModel();
            boolean isResultRootNode =
                            (value instanceof String);
     // render no icon space an empty icon of a callStackFrame
            if(isResultRootNode){
            	setBackground((row % 2 == 0) ? Color.gray : Color.black);
        	    return this;
            }else{
                setText(""+value);
            }
    	    return this;
		}
     }
    
    public void showStrikeAnalysis(String symbol, TDTreeModel parentModel){
    	if(symbol!=null&&!symbol.contains(":")){

        	Double strike = parentModel.getCachedResults().optionToStrike.get(symbol);
        	boolean analyzeCalls = symbol.endsWith("C");
        	List<OS> strikeResults = new ArrayList<OS>();
        	for(OExpiry expiry : parentModel.getOptionStrikesExpiriesList()){
        		if(analyzeCalls){
            		for(OS expiryStrike : expiry.getCallStrikes()){
            			if(strike.doubleValue() == parentModel.getCachedResults().optionToStrike.get(expiryStrike.getSym()).doubleValue()){
            				strikeResults.add(expiryStrike);
            			}
            		}
        		}else{
            		for(OS expiryStrike : expiry.getPutStrikes()){
            			if(strike.doubleValue() == parentModel.getCachedResults().optionToStrike.get(expiryStrike.getSym()).doubleValue()){
            				strikeResults.add(expiryStrike);
            			}
            		}
        		}
        	}
        	OExpiry oe = new OExpiry();
        	oe.setAnalysis(true);
        	oe.setExpiry("Analysis of $"+strike+" Strike "+(analyzeCalls?"CALL":"PUT")+" Contracts");
        	oe.setCallStrikes(strikeResults);
        	oe.setPutStrikes(new ArrayList<OS>());

        	List<OExpiry> ress = new ArrayList<OExpiry>();
        	ress.add(oe);
        	TDTreeModel newModel = new TDTreeModel(ress, parentModel.getCachedResults());
        	
        	JXTreeTable xTable = new JXTreeTable(newModel);
    	    //xTable.addHighlighter(new ColorHighlighter(HighlightPredicate.EVEN, Color.GRAY, null, null, null));

    	    //xTable.setDefaultRenderer(Object.class, new TableCellRenderer());
    	    xTable.putClientProperty(JXTreeTable.DROP_HACK_FLAG_KEY, Boolean.TRUE);
    	    //xTable.expandAll();
    	    //xTable.setVisibleColumnCount(10);
    	    xTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);        
    	    xTable.setRootVisible(false); 
    	    xTable.setSortable(true);
    	    //JTextField textField = new JTextField(
    	            //"drag me over hierarchical column ..");
    	    //textField.setDragEnabled(true);
    	    xTable.getColumnModel().getColumn(0).setPreferredWidth(250);
    	    xTable.expandAll();

    	    JScrollPane sp=new JScrollPane(xTable); 
    	    JFrame frame = new JFrame(symbol);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout());
           
            final Container contentPane = frame.getContentPane();
            BorderLayout layout = new BorderLayout();
            contentPane.setLayout(layout);  
    	    contentPane.add(sp, BorderLayout.CENTER);      
    	    contentPane.setSize(300,400);    
    	    contentPane.setVisible(true); 

            contentPane.add(topPanel, BorderLayout.NORTH);
            frame.setSize(600, 600);
            frame.setVisible(true);
    	}
    }
    
    public static void showScrollableResults(JScrollPane sp,JScrollPane sp2, String symbol, String timeString){
    	if(sp!=null){

            JFrame frame = new JFrame(symbol+" "+timeString);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout());
           
            final Container contentPane = frame.getContentPane();
            BorderLayout layout = new BorderLayout();
            contentPane.setLayout(layout);  
    	    contentPane.add(sp, BorderLayout.CENTER);      
    	    contentPane.setSize(300,400);    
    	    contentPane.setVisible(true); 

            contentPane.add(topPanel, BorderLayout.NORTH);
            frame.setSize(600, 600);
            frame.setVisible(true);
    	}
    	if(sp2!=null){
            JFrame frame = new JFrame(symbol+" "+timeString);

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout());
           
            final Container contentPane = frame.getContentPane();
            BorderLayout layout = new BorderLayout();
            contentPane.setLayout(layout);  
    	    contentPane.add(sp2, BorderLayout.CENTER);      
    	    contentPane.setSize(300,400);    
    	    contentPane.setVisible(true); 

            contentPane.add(topPanel, BorderLayout.NORTH);
            frame.setSize(800, 600);
            frame.setVisible(true);
    	}
    }
    
    public static void main(String[] args) {
    	try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        new OSMainPanel();
    }
    
    public static int getDateNum(String s){
    	if(s.equals("Select")){
    		return 0;
    	}
    	String ss = s.replace("_", "");
    	return Integer.parseInt(ss);
    }
    
    public static String[] getDateStrings(String s, String e, String[] dates){
    	int startNum = getDateNum(s);
    	int endNum = getDateNum(e);
    	ArrayList<String> dateslist = new ArrayList<String>();
    	for(String ss : dates){
    		int crtNum = getDateNum(ss);
    		if(crtNum>=startNum && crtNum <=endNum){
    			dateslist.add(ss);
    		}
    	}
    	Collections.reverse(dateslist);
    	String ret[] = new String[dateslist.size()];
    	for(int c = 0; c < dateslist.size(); c++){
    		ret[c] = dateslist.get(c);
    	}
    	return ret;
    }
    

}
