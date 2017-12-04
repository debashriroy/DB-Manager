package org.besus.meice.oramtt.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import org.besus.meice.oramtt.erd.model.ERDiagram;
import org.besus.meice.oramtt.erd.model.Entity;
import org.besus.meice.oramtt.erd.model.Relation;
import org.besus.meice.oramtt.util.RelationType;


public class RelationDialog extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	private JButton okButton;
	private JButton cancelButton;
	private JLabel lblRelationName;
	private JTextField txtName;
	private JLabel lblRelationType;
	private JComboBox cmbRelationType;
	private JLabel lblFromEntity;
	private JComboBox cmbFromEntity;
	private JLabel lblToEntity;
	private JComboBox cmbToEntity;

	private Relation relation;
	private ERDiagram diagram;

	/**
	 * Create the dialog.
	 */
	public RelationDialog(Relation relation, ERDiagram diagram) {
		setBounds(100, 100, 350, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				lblRelationName = new JLabel("Relation Name : ");
				contentPanel.add(lblRelationName);
			}
			{
				txtName = new JTextField(20);
				contentPanel.add(txtName);
			}
			{
				lblRelationType = new JLabel("Relation Type : ");
				contentPanel.add(lblRelationType);
			}
			{
				cmbRelationType = new JComboBox(new String[] { "One To One",
						"One To Many", "Many To One", "Many To Many" });
				contentPanel.add(cmbRelationType);
			}
			{
				lblFromEntity = new JLabel(
						"                          From Entity : ");
				contentPanel.add(lblFromEntity);
			}
			{
				cmbFromEntity = new JComboBox();
				List<Entity> entities = diagram.getEntities();
				for (Entity entity : entities) {
					cmbFromEntity.addItem(entity.getName());
				}
				contentPanel.add(cmbFromEntity);
			}
			{
				lblToEntity = new JLabel(
						"                             To Entity : ");
				contentPanel.add(lblToEntity);
			}
			{
				cmbToEntity = new JComboBox();
				List<Entity> entities = diagram.getEntities();
				for (Entity entity : entities) {
					cmbToEntity.addItem(entity.getName());
				}
				contentPanel.add(cmbToEntity);
			}
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

		this.relation = relation;
		this.diagram = diagram;
		addAction();
	}

	private void addAction() {
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent event) {
				String name = txtName.getText();
				if (name != null && !name.equals("")) {
					relation.setName(name);
				}
				String type = (String) cmbRelationType.getSelectedItem();
				if (type.equals("One To One")) {
					relation.setType(RelationType.ONE_TO_ONE);
				}
				if (type.equals("One To Many")) {
					relation.setType(RelationType.ONE_TO_MANY);
				}
				if (type.equals("Many To One")) {
					relation.setType(RelationType.MANY_TO_ONE);
				}
				if (type.equals("Many To Many")) {
					relation.setType(RelationType.MANY_TO_MANY);
				}

				String fromEntity = (String) cmbFromEntity.getSelectedItem();
				relation.setFromEntity(diagram.findEntity(fromEntity));
				
				String toEntity = (String) cmbToEntity.getSelectedItem();
				relation.setToEntity(diagram.findEntity(toEntity));
			
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
