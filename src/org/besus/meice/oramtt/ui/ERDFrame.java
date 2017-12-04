package org.besus.meice.oramtt.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JToolBar.Separator;
import javax.swing.JTree;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;

import org.besus.meice.oramtt.erd.model.ERDiagram;
import org.besus.meice.oramtt.erd.model.Model;
import org.besus.meice.oramtt.erd.model.ModelType;
import org.besus.meice.oramtt.util.Constants;


public class ERDFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JToolBar toolBar;

	/*
	 * Toolbar Buttons
	 */
	private JButton newButton;
	private JButton openButton;
	private JButton saveButton;
	private JButton saveAsButton;
	private JButton undoButton;
	private JButton redoButton;
	private JButton deleteButton;
	private JButton runButton;
	private JButton entityButton;
	private JButton attributeButton;
	private JButton relationButton;

	private ERDCanvas editor;

	private ERDiagram diagram;

	/**
	 * Create the frame.
	 */
	public ERDFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("OraMTT - ER Diagram Designer");
		setBounds(100, 100, 900, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		toolBar = new JToolBar();
		toolBar.setSize(900, 30);
		contentPane.add(toolBar, BorderLayout.NORTH);
		customizeToolbar(toolBar);

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.15);

		initializeDiagram();
		editor = new ERDCanvas(diagram);
		splitPane.setRightComponent(editor);

		contentPane.add(splitPane, BorderLayout.CENTER);

		DefaultMutableTreeNode topNode = new DefaultMutableTreeNode("Tables");
		JTree tree = new JTree(topNode);
		splitPane.setLeftComponent(tree);

	}
	
	public ERDCanvas getEditor() {
		return editor;
	}

	private void customizeToolbar(JToolBar toolBar) {
		newButton = new JButton(new ImageIcon("icons/new.png"));
		openButton = new JButton(new ImageIcon("icons/open.png"));
		saveButton = new JButton(new ImageIcon("icons/save.png"));
		saveAsButton = new JButton(new ImageIcon("icons/saveas.png"));
		undoButton = new JButton(new ImageIcon("icons/undo.png"));
		redoButton = new JButton(new ImageIcon("icons/redo.png"));
		deleteButton = new JButton(new ImageIcon("icons/delete.png"));
		runButton = new JButton(new ImageIcon("icons/run.png"));

		toolBar.add(newButton);
		toolBar.add(openButton);
		toolBar.add(saveButton);
		toolBar.add(saveAsButton);
		toolBar.add(new Separator());
		toolBar.add(new Separator());
		toolBar.add(undoButton);
		toolBar.add(redoButton);
		toolBar.add(deleteButton);
		toolBar.add(new Separator());
		toolBar.add(new Separator());
		toolBar.add(runButton);
		toolBar.add(new Separator());
		toolBar.add(new Separator());
		toolBar.add(new Separator());
		toolBar.add(new Separator());

		entityButton = new JButton(new ImageIcon("icons/entity.png"));
		toolBar.add(entityButton);
		attributeButton = new JButton(new ImageIcon("icons/attribute.png"));
		toolBar.add(attributeButton);
		relationButton = new JButton(new ImageIcon("icons/relation.png"));
		toolBar.add(relationButton);

		addTooltipTexts();
		addActions();
	}

	private void addTooltipTexts() {
		newButton.setToolTipText(Constants.ERDFRAME_NEW_BUTTON_TOOLTIPTEXT);
		openButton.setToolTipText(Constants.ERDFRAME_OPEN_BUTTON_TOOLTIPTEXT);
		saveButton.setToolTipText(Constants.ERDFRAME_SAVE_BUTTON_TOOLTIPTEXT);
		saveAsButton
				.setToolTipText(Constants.ERDFRAME_SAVEAS_BUTTON_TOOLTIPTEXT);
		undoButton.setToolTipText(Constants.ERDFRAME_UNDO_BUTTON_TOOLTIPTEXT);
		redoButton.setToolTipText(Constants.ERDFRAME_REDO_BUTTON_TOOLTIPTEXT);
		deleteButton
				.setToolTipText(Constants.ERDFRAME_DELETE_BUTTON_TOOLTIPTEXT);
		runButton.setToolTipText(Constants.ERDFRAME_RUN_BUTTON_TOOLTIPTEXT);

		entityButton
				.setToolTipText(Constants.ERDFRAME_ENTITY_BUTTON_TOOLTIPTEXT);
		attributeButton
				.setToolTipText(Constants.ERDFRAME_ATTRIBUTE_BUTTON_TOOLTIPTEXT);
		relationButton
				.setToolTipText(Constants.ERDFRAME_RELATION_BUTTON_TOOLTIPTEXT);
	}

	private void addActions() {
		Component[] components = toolBar.getComponents();
		for (Component component : components) {
			if (component instanceof JButton) {
				((JButton) component)
						.addActionListener(new ButtonClickListeners(this));
			}
		}
	}

	public ERDiagram getDiagram() {
		return diagram;
	}

	public void initializeDiagram() {
		diagram = new ERDiagram();
	}

	private class ButtonClickListeners implements ActionListener {

		private ERDFrame frame;

		public ButtonClickListeners(ERDFrame frame) {
			this.frame = frame;
		}

		@Override
		public void actionPerformed(ActionEvent clickEvent) {
			removeEditorListeners();
			Object source = clickEvent.getSource();
			if (source instanceof JButton) {
				JButton clickedButton = (JButton) source;
				String toolTipText = clickedButton.getToolTipText();

				if (toolTipText
						.equals(Constants.ERDFRAME_NEW_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					// TODO
				}

				if (toolTipText
						.equals(Constants.ERDFRAME_OPEN_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					// TODO
				}

				if (toolTipText
						.equals(Constants.ERDFRAME_SAVE_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					// TODO
				}

				if (toolTipText
						.equals(Constants.ERDFRAME_SAVEAS_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					// TODO
				}

				if (toolTipText
						.equals(Constants.ERDFRAME_UNDO_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					// TODO
				}

				if (toolTipText
						.equals(Constants.ERDFRAME_REDO_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					// TODO
				}

				if (toolTipText
						.equals(Constants.ERDFRAME_DELETE_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					// TODO
				}

				if (toolTipText
						.equals(Constants.ERDFRAME_RUN_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					// TODO
				}

				if (toolTipText
						.equals(Constants.ERDFRAME_ENTITY_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					Model.initialize(ModelType.ENTITY, frame);
				}

				if (toolTipText
						.equals(Constants.ERDFRAME_ATTRIBUTE_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					Model.initialize(ModelType.ATTRIBUTE, frame);
				}

				if (toolTipText
						.equals(Constants.ERDFRAME_RELATION_BUTTON_TOOLTIPTEXT)) {
					editor.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					Model.initialize(ModelType.RELATION, frame);
				}
			}

		}

		private void removeEditorListeners() {
			MouseListener[] mouseListeners = editor.getMouseListeners();
			MouseMotionListener[] mouseMotionListeners = editor
					.getListeners(MouseMotionListener.class);

			for (MouseListener listener : mouseListeners) {
				editor.removeMouseListener(listener);
			}

			for (MouseMotionListener listener : mouseMotionListeners) {
				editor.removeMouseMotionListener(listener);
			}
		}

	}

}
