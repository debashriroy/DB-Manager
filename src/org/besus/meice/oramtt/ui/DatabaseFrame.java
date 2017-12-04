package org.besus.meice.oramtt.ui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.TabExpander;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.besus.meice.oramtt.dao.*;
import org.besus.meice.oramtt.util.ConnectionManager;

public class DatabaseFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	private JToolBar toolBar;

	private JPanel contentPane;
	private JPanel rightPane;
	private JPanel leftPane;
	private JSplitPane splitPane;
	private DefaultTableModel model;
	private JTable databaseTable;
	private String selectedTable;
	private JTree tables;
	private JPopupMenu popup;
	private PopupHandler handler;

	private JButton saveButton;
	// private JButton addButton;
	private JButton refreshButton;
	private JButton deleteButton;
	private boolean insertMode = false;
	int oldRowCount;
	List<String> tableNames;

	public DatabaseFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("OraMTT-Database Table Manipulator");
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.15);

		rightPane = new JPanel();
		rightPane.setLayout(new BorderLayout(0, 0));
		toolBar = new JToolBar();
		toolBar.setSize(675, 30);
		rightPane.add(toolBar, BorderLayout.NORTH);
		customizeToolBar(toolBar);

		databaseTable = new JTable();
		databaseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		rightPane.add(databaseTable);
		splitPane.setRightComponent(rightPane);

		contentPane.add(splitPane, BorderLayout.CENTER);

		DefaultMutableTreeNode topNode = new DefaultMutableTreeNode(
				ConnectionManager.getDatabase());
		// tables.add
		tables = new JTree(topNode);
		leftPane = new JPanel();
		leftPane.setLayout(new BorderLayout(0, 0));
		leftPane.add(tables);
		JScrollPane scrollPane = new JScrollPane(tables,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		leftPane.add(scrollPane);

		// DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
		// renderer.setIcon(new ImageIcon("icons/database.png"));
		// tables.setCellRenderer(renderer);
				
		initializeTree();
		addListener();
		splitPane.setLeftComponent(leftPane);
		
		// setModal(true);
	}

	private void customizeToolBar(JToolBar toolBar) {
		saveButton = new JButton(new ImageIcon("icons/save.png"));
		// addButton = new JButton(new ImageIcon("icons/add.png"));
		refreshButton = new JButton(new ImageIcon("icons/refresh.png"));
		deleteButton = new JButton(new ImageIcon("icons/delete.png"));

		toolBar.add(saveButton);
		// toolBar.add(addButton);
		toolBar.add(deleteButton);
		toolBar.add(refreshButton);

		addActions();
	}

	private DefaultMutableTreeNode getRootNode() {
		return (DefaultMutableTreeNode) tables.getModel().getRoot();
	}

	private void initializeTree() {
		DefaultMutableTreeNode rootNode = getRootNode();
		tableNames = TableDetails.getTableNames();

		for (String table : tableNames) {
			List<String> columnDetails = TableDetails
					.retriveColumnDetails(table);
			List<String> indicesDetails = TableDetails
					.retriveConstraints(table);

			DefaultMutableTreeNode node = new DefaultMutableTreeNode(table);

			DefaultMutableTreeNode tbNodeCol = new DefaultMutableTreeNode(
					"Columns");
			for (String col : columnDetails) {
				DefaultMutableTreeNode tbNodeColDetails = new DefaultMutableTreeNode(
						col);
				tbNodeCol.add(tbNodeColDetails);
			}

			DefaultMutableTreeNode tbNodeInd = new DefaultMutableTreeNode(
					"Indices");
			for (String index : indicesDetails) {
				DefaultMutableTreeNode tbNodeIndexDetails = new DefaultMutableTreeNode(
						index);
				tbNodeInd.add(tbNodeIndexDetails);
			}

			node.add(tbNodeCol);
			node.add(tbNodeInd);

			rootNode.add(node);
		}

	}

	private void addListener() {
		tables.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent event) {
				Object selectedComponent = tables
						.getLastSelectedPathComponent();
				popup = null;
				if ((tableNames.contains(selectedComponent.toString()))
						&& (!selectedComponent.equals(getRootNode()))) {
					selectedTable = selectedComponent.toString();

					popup = new JPopupMenu();
					popup.setInvoker(tables);
					handler = new PopupHandler(tables, popup);
					popup.add(getMenuItem("Relationships/ Foreign keys..",
							handler));
					popup.add(getMenuItem("Insert / Update data", handler));
					PopupHandler.setSelectedTableName(selectedTable);
					retrieveTableDetails();
				} else
					popup = null;

			}
		});

	}

	public void retrieveTableDetails() {
		if (selectedTable != null && !selectedTable.equals("")) {
			List<String> columnNames = new ArrayList<String>();
			Object[][] data = TableDetails.getTableData(selectedTable,
					columnNames);
			model = new DefaultTableModel(data, columnNames.toArray());
			model.addRow(new Object[] {});
			databaseTable = new JTable(model);
			oldRowCount = model.getRowCount() - 1;
			TableCellListener tcl = new TableCellListener(databaseTable, action);
			JPanel rightPane = new JPanel();
			rightPane.setLayout(new BorderLayout(0, 0));
			rightPane.add(toolBar, BorderLayout.NORTH);

			JScrollPane scrollPane = new JScrollPane(databaseTable,
					ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
					ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			databaseTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
			rightPane.add(scrollPane);

			splitPane.setRightComponent(rightPane);
		}
	}

	// Refresh Button
	private void addActions() {
		refreshButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				retrieveTableDetails();
			}
		});

		// SAVE Button

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				int columnNumber = databaseTable.getColumnCount();
				for (int row = oldRowCount; row < model.getRowCount() - 1; row++) {
					Map<Integer, Object> data = new HashMap<Integer, Object>();
					for (int column = 0; column < columnNumber; column++) {

						data.put(column, databaseTable.getValueAt(row, column));

//						System.out.println("databaseTable get value at " + row
//								+ " , " + column + " :"
//								+ model.getValueAt(row, column));

					}
					int action = TableDetails.insertRow(data, selectedTable,
							columnNumber);
					if (action == -1) {
						JOptionPane.showMessageDialog(null,
								TableDetails.errorMessage,
								"Error in Insertion", 3);
						break;
					}
					// if (action == 1) {
					// JOptionPane.showMessageDialog(null,
					// "Duplicate Entry for Primary key",
					// "Error in Insertion", 3);
					// break;
					// }
				}
				// End of add new rows
				insertMode = false;
				retrieveTableDetails();

			}
		});
		// Delete Button
		deleteButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				int[] selectedRows = databaseTable.getSelectedRows();
				if (selectedRows.length > 0) {
					int act = JOptionPane
							.showConfirmDialog(
									null,
									"Do you surely want to delete the selected rows from the database ?",
									"Confirm Deletion",
									JOptionPane.OK_CANCEL_OPTION);

					if (act == 0) {
						int columnNumber = databaseTable.getColumnCount();

						for (int row = 0; row < selectedRows.length; row++) {
							Map<String, Object> data = new HashMap<String, Object>();
							for (int column = 0; column < columnNumber; column++) {

								data.put(databaseTable.getColumnName(column),
										databaseTable.getValueAt(
												selectedRows[row], column));

							}
							TableDetails.deleteRow(data, selectedTable);
						}
						retrieveTableDetails();
					}
				}
			}
		});
	}

	// For Updating each Cell
	Action action = new AbstractAction() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			TableCellListener tcl = (TableCellListener) e.getSource();
			boolean toBeUpdated = false;
//			System.out.println("Row   : " + tcl.getRow());
//			System.out.println("Column: " + tcl.getColumn());
//			System.out.println("Old   : " + tcl.getOldValue());
//			System.out.println("New   : " + tcl.getNewValue());
//			System.out.println("oldRowCount:"+oldRowCount);
			// Insertion Part
			if (tcl.getRow() == databaseTable.getRowCount() - 1) {
//				System.out.println("IN INSERT PART");
				databaseTable.setValueAt(tcl.getNewValue(), tcl.getRow(),
						tcl.getColumn());
				insertMode = true;
				model.addRow(new Object[] {});
				databaseTable.setModel(model);
			} // Updation Part
			else if (!insertMode) {
				if ((tcl.getOldValue() == null)
						&& (!tcl.getNewValue().equals(null))) {
//					System.out.println("if part.." + tcl.getOldValue());
					toBeUpdated = true;
				} else if (!tcl.getOldValue().equals(tcl.getNewValue())) {
//					System.out.println("else if part....");
					toBeUpdated = true;
				}
				if (toBeUpdated) {
					Map<String, Object> data = new HashMap<String, Object>();
					for (int column = 0; column < databaseTable
							.getColumnCount(); column++) {
						data.put(databaseTable.getColumnName(column),
								databaseTable.getValueAt(tcl.getRow(), column));
					}

					int act = TableDetails
							.UpdateCell(data, databaseTable.getColumnName(tcl
									.getColumn()), tcl.getNewValue(), tcl
									.getOldValue(), selectedTable);
					if (act == -1) {
						JOptionPane
								.showMessageDialog(null,
										TableDetails.errorMessage,
										"Error in Update", 3);
						databaseTable.setValueAt(tcl.getOldValue(),
								tcl.getRow(), tcl.getColumn());
					}
//					if (act == 1) {
//						JOptionPane.showMessageDialog(null,
//								"Duplicate Entry for Primary Key",
//								"Error in Update", 3);
//						databaseTable.setValueAt(tcl.getOldValue(),
//								tcl.getRow(), tcl.getColumn());
//					}
				}
			}else if(insertMode && (oldRowCount-tcl.getRow()>=1)){
				JOptionPane.showMessageDialog(null,
						"Value cannot be changed",
						"Error in Update", 3);
				databaseTable.setValueAt(tcl.getOldValue(),tcl.getRow(), tcl.getColumn());
			}
		}
	};

	private JMenuItem getMenuItem(String s, ActionListener al) {
		JMenuItem menuItem = new JMenuItem(s);
		menuItem.addActionListener(al);
		return menuItem;
	}

}
