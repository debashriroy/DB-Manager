package org.besus.meice.oramtt.ui;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.besus.meice.oramtt.dao.TableDetails;
import org.besus.meice.oramtt.util.ConnectionManager;

public class ConstraintDailog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel dbLabel;
	private JLabel tbLabel;
	private JLabel refLabel;
	private JTable table;
	private DefaultTableModel model;

	static String selectedTableName;

	static String foreignKeyRefDesc;

	public static String getForeignKeyRefDesc() {
		return foreignKeyRefDesc;
	}

	public static void setForeignKeyRefDesc(String foreignKeyRefDesc) {
		ConstraintDailog.foreignKeyRefDesc = foreignKeyRefDesc;
	}

	public static String getSelectedTableName() {
		return selectedTableName;
	}

	public static void setSelectedTableName(String selectedTableName) {
		ConstraintDailog.selectedTableName = selectedTableName;
	}

	public ConstraintDailog() {
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//		setModal(true);
		setTitle("OraMTT-Show Ralationship");
		setBounds(300, 300, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0,0));
//		contentPane.setLayout(new GridBagLayout());
		
//		GroupLayout layout = new GroupLayout(getContentPane());
//		getContentPane().setLayout(layout);
//		layout.setAutoCreateGaps(true);
//		layout.setAutoCreateContainerGaps(true);


		setContentPane(contentPane);
		// TableDetails.retriveConstraints(selectedTableName);
		TableDetails.retriveForiegnKeyDetails(selectedTableName);
		table = new JTable();
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		contentPane.add(table);
		Object[][] data={
				{"Database:",ConnectionManager.getDatabase()},
				{"Table:",selectedTableName},
				{"References:",foreignKeyRefDesc}
		};
		Object[] columnNames={"",""};
		model=new DefaultTableModel(data,columnNames);
		table.setModel(model);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.getColumnModel().getColumn(0).setPreferredWidth(75);
		table.getColumnModel().getColumn(0).setMaxWidth(75);
		
		table.setEnabled(false);
		
			
//		dbLabel=new JLabel("Database : "+ConnectionManager.getDatabase());
//		contentPane.add(dbLabel);
//		JSeparator jsep1=new JSeparator(SwingConstants.HORIZONTAL);
//		jsep1.setSize(400, 1);
//		contentPane.add(jsep1);
//		tbLabel = new JLabel("Table : " + selectedTableName);
//		tbLabel.setHorizontalAlignment(SwingConstants.LEFT);
//		contentPane.add(tbLabel);
//		contentPane.add(new JSeparator(SwingConstants.HORIZONTAL));
//		contentPane.add(new JLabel("References:"));
//		refLabel = new JLabel(foreignKeyRefDesc);
//		contentPane.add(refLabel);
		setModal(true);
	}

}
