package org.besus.meice.oramtt.dao;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPopupMenu;
import javax.swing.JTree;

import org.besus.meice.oramtt.ui.*;

public class PopupHandler implements ActionListener {
	JTree tree;
	JPopupMenu popup;
	Point loc;
	static String selectedTableName;

	public PopupHandler(JTree tree, JPopupMenu popup) {
		this.tree = tree;
		this.popup = popup;
		tree.addMouseListener(ma);
	}

	public static String getSelectedTableName() {
		return selectedTableName;
	}

	public static void setSelectedTableName(String selectedTableName) {
		PopupHandler.selectedTableName = selectedTableName;
	}

	public void actionPerformed(ActionEvent e) {
		String ac = e.getActionCommand();
//		System.out.println("ac." + ac);
		if (ac.equals("Insert / Update data")) {
			(new DatabaseFrame()).retrieveTableDetails();
		} else {
			ConstraintDailog.setSelectedTableName(selectedTableName);
			ConstraintDailog dialog = new ConstraintDailog();

			dialog.setVisible(true);
		}

	}

	private MouseListener ma = new MouseAdapter() {
		private void checkForPopup(MouseEvent e) {
			if (e.isPopupTrigger()) {
				loc = e.getPoint();
				popup.show(tree, loc.x, loc.y);
			}
		}

		public void mousePressed(MouseEvent e) {
			checkForPopup(e);
		}

		public void mouseReleased(MouseEvent e) {
			checkForPopup(e);
		}

		public void mouseClicked(MouseEvent e) {
			checkForPopup(e);
		}
	};
}