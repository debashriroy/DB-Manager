package org.besus.meice.oramtt.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.besus.meice.oramtt.erd.model.Attribute;
import org.besus.meice.oramtt.erd.model.ERDiagram;
import org.besus.meice.oramtt.erd.model.Entity;
import org.besus.meice.oramtt.util.OracleDataMap;


public class AttributeDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private JButton okButton;
	private JButton cancelButton;
	private JLabel lblAttributeName;
	private JTextField textField;
	private JLabel lblAttributeType;
	private JComboBox cmbDataType;
	private JLabel lblAttributeSize;
	private JTextField sizeField;
	private JLabel lblEntity;
	private JComboBox cmbEntity;

	private Attribute attribute;
	private ERDiagram diagram;

	/**
	 * Create the dialog.
	 */
	public AttributeDialog(Attribute attribute, ERDiagram diagram) {
		setBounds(200, 200, 350, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			lblAttributeName = new JLabel("Attribute Name : ");
			contentPanel.add(lblAttributeName);
		}
		{
			textField = new JTextField();
			contentPanel.add(textField);
			textField.setColumns(20);
		}
		{
			lblAttributeType = new JLabel("Attribute Type : ");
			contentPanel.add(lblAttributeType);
		}
		{
			cmbDataType = new JComboBox();
			Set<String> userDataTypes = OracleDataMap.getUserDataTypes();
			for (String type : userDataTypes) {
				cmbDataType.addItem(type);
			}
			contentPanel.add(cmbDataType);
		}
		{
			lblAttributeSize = new JLabel(
					"           Maximum Attribute Size : ");
			contentPanel.add(lblAttributeSize);
		}
		{
			sizeField = new JTextField();
			contentPanel.add(sizeField);
			sizeField.setColumns(4);
			contentPanel.add(sizeField);
		}
		{
			lblEntity = new JLabel("                  Entity : ");
			contentPanel.add(lblEntity);
		}
		{
			cmbEntity = new JComboBox();
			List<Entity> entities = diagram.getEntities();
			for (Entity entity : entities) {
				cmbEntity.addItem(entity.getName());
			}
			contentPanel.add(cmbEntity);
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
		this.attribute = attribute;
		this.diagram = diagram;
		addActions();
	}

	private void addActions() {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String name = textField.getText();
				String type = (String) cmbDataType.getSelectedItem();
				String size = sizeField.getText();
				String entity = (String) cmbEntity.getSelectedItem();

				if (name != null && !name.equals("")) {
					attribute.setName(name);
				}

				if (size != null && !size.equals("")) {
					attribute.setSize(Integer.parseInt(size));
				}
				attribute.setDataType(OracleDataMap.getOracleDataType(type));
				attribute.setEntity(diagram.findEntity(entity));
				close();
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
