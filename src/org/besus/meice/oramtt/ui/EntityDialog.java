package org.besus.meice.oramtt.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.besus.meice.oramtt.erd.model.ERDiagram;
import org.besus.meice.oramtt.erd.model.Entity;


public class EntityDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	
	private JButton okButton;
	private JButton cancelButton;
	private ERDiagram diagram;
	
	private Entity model;
	/**
	 * Create the dialog.
	 */
	public EntityDialog(Entity drawnModel, ERDiagram diagram) {
		setBounds(200, 200, 550, 250);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		{
			JLabel lblEntityName = new JLabel("Entity Name");
			contentPanel.add(lblEntityName);
		}
		{
			textField = new JTextField();
			contentPanel.add(textField);
			textField.setColumns(20);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setVisible(true);
		this.model = drawnModel;
		this.diagram = diagram;
		addActions();
	}
	
	private void addActions() {
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = textField.getText();
				if(name != null && !name.equals("")) {
					model.setName(name);
					close();
				}
				
			}
		});
		
		cancelButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				close();
			}
		});
		
	}
	
	private void close() {
		dispose();
		diagram.repaint();
	}

}
