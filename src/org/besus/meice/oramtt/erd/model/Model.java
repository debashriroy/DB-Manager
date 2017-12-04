package org.besus.meice.oramtt.erd.model;

import java.awt.DisplayMode;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.besus.meice.oramtt.ui.ERDCanvas;
import org.besus.meice.oramtt.ui.ERDFrame;


public abstract class Model {

	protected static Model selectedModel;
	private static ERDCanvas editor;
	private static ERDFrame frame;

	private String name = null;
	private Point startingPoint = null;
	private Point releasePoint = null;

	public abstract void paint(Point definingLoc1, Point definingLoc2);

	abstract Model draw(Point definingLoc1, Point definingLoc2);

	abstract void redraw(Point definingLoc1, Point definingLoc2);

	abstract void openDialog(Model drawnModel);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point getStartingPoint() {
		return startingPoint;
	}

	public void setStartingPoint(Point startingPoint) {
		this.startingPoint = startingPoint;
	}

	public Point getReleasePoint() {
		return releasePoint;
	}

	public void setReleasePoint(Point releasePoint) {
		this.releasePoint = releasePoint;
	}

	public static void initialize(ModelType modelType, ERDFrame frame) {
		Model.frame = frame;
		Model.editor = frame.getEditor();
		switch (modelType) {
		case ATTRIBUTE:
			selectedModel = new Attribute();
			break;
		case ENTITY:
			selectedModel = new Entity();
			break;
		case RELATION:
			selectedModel = new Relation();
			break;
		default:
			throw new IllegalArgumentException("Unknown Model");
		}
		selectedModel.addListener();
	}

	protected ERDCanvas getEditor() {
		return editor;
	}

	private void addListener() {
		getEditor().addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent releaseEvent) {
				releasePoint = releaseEvent.getPoint();
				if (releasePoint.distance(startingPoint) == 0) {
					releasePoint = null;
					startingPoint = null;
				} else {
					Model drawnModel = draw(startingPoint, releasePoint);
					frame.getDiagram().addModel(drawnModel);
					openDialog(drawnModel);
				}
				frame.getDiagram().repaint();
			}

			@Override
			public void mousePressed(MouseEvent pressEvent) {
				frame.getDiagram().repaint();
				startingPoint = pressEvent.getPoint();
			}

			@Override
			public void mouseExited(MouseEvent exitEvent) {
				// NOTHING TO DO
			}

			@Override
			public void mouseEntered(MouseEvent enterEvent) {
				// NOTHING TO DO
			}

			@Override
			public void mouseClicked(MouseEvent clickEvent) {
				// NOTHING TO DO
			}
		});

		getEditor().addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent moveEvent) {
				// NOTHING TO DO
			}

			@Override
			public void mouseDragged(MouseEvent dragEvent) {
				Point currentPosition = dragEvent.getPoint();
				draw(startingPoint, currentPosition);
				frame.getDiagram().repaint();
			}
		});

	}
}
