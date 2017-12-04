package org.besus.meice.oramtt.erd.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.besus.meice.oramtt.ui.AttributeDialog;


public class Attribute extends Model {

	private Entity entity;
	private String dataType;
	private int size;

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public Entity getEntity() {
		return entity;
	}

	@Override
	Model draw(Point definingLoc1, Point definingLoc2) {
		getEditor().getGraphics().clearRect(0, 0,
				getEditor().getBounds().width, getEditor().getBounds().height);
		paint(definingLoc1, definingLoc2);

		Attribute attribute = new Attribute();
		attribute.setStartingPoint(definingLoc1);
		attribute.setReleasePoint(definingLoc2);
		return attribute;
	}

	public void paint(Point definingLoc1, Point definingLoc2) {
		Graphics g = getEditor().getGraphics();
		Color color = Color.BLACK;
		g.setColor(color);
		g.drawOval(definingLoc1.x, definingLoc1.y, definingLoc2.x
				- definingLoc1.x, definingLoc2.y - definingLoc1.y);

		if (getName() != null) {
			int nameLength = getName().length();
			g.drawString(getName(), (definingLoc1.x + definingLoc2.x) / 2 - 2
					* nameLength, (definingLoc1.y + definingLoc2.y) / 2);
		}
		if (getEntity() != null) {
			drawConnection();
		}
	}

	@Override
	void redraw(Point definingLoc1, Point definingLoc2) {
		paint(definingLoc1, definingLoc2);
	}

	@Override
	void openDialog(Model drawnModel) {
		new AttributeDialog((Attribute) drawnModel, getEditor().getDiagram());
	}

	private void drawConnection() {
		Point centrePointOfAttribute = new Point(
				(getStartingPoint().x + getReleasePoint().x) / 2,
				(getStartingPoint().y + getReleasePoint().y) / 2);
		Point centrePointOfEntity = new Point(
				(getEntity().getStartingPoint().x + getEntity()
						.getReleasePoint().x) / 2,
				(getEntity().getStartingPoint().y + getEntity()
						.getReleasePoint().y) / 2);

		int[] connectionPoints = getConnectionPoints(centrePointOfEntity,
				centrePointOfAttribute);
		Graphics g = getEditor().getGraphics();
		g.drawLine(connectionPoints[0], connectionPoints[1],
				connectionPoints[2], connectionPoints[3]);

	}

	private int[] getConnectionPoints(Point p1, Point p2) {
		int[] connectionPoints = new int[4];
		double angle = ((Math.atan((double) (p2.y - p1.y)
				/ (double) (p2.x - p1.x))) * 180.0)
				/ Math.PI;
		if (angle >= 0 && angle <= 45) {
			int xDistance = p2.x - p1.x;
			// attribute is on lower right corner of entity within 45D
			if (xDistance > 0) {
				connectionPoints[0] = (getEntity().getStartingPoint().x + getEntity()
						.getReleasePoint().x) / 2;
				connectionPoints[1] = getEntity().getReleasePoint().y;
				connectionPoints[2] = getStartingPoint().x;
				connectionPoints[3] = (getStartingPoint().y + getReleasePoint().y) / 2;
			}
			// attribute is on upper left corner of entity within 45D
			else {
				connectionPoints[0] = (getEntity().getStartingPoint().x + getEntity()
						.getReleasePoint().x) / 2;
				connectionPoints[1] = getEntity().getStartingPoint().y;
				connectionPoints[2] = getReleasePoint().x;
				connectionPoints[3] = (getStartingPoint().y + getReleasePoint().y) / 2;
			}
		}

		if (angle > 45 && angle <= 90) {
			int xDistance = p2.x - p1.x;
			// attribute is on lower right corner of entity exceeding 45D
			if (xDistance > 0) {
				connectionPoints[0] = (getEntity().getStartingPoint().x + getEntity()
						.getReleasePoint().x) / 2;
				connectionPoints[1] = getEntity().getReleasePoint().y;
				connectionPoints[2] = (getStartingPoint().x + getReleasePoint().x) / 2;
				connectionPoints[3] = getStartingPoint().y;
			}
			// attribute is on upper left corner of entity exceeding 45D
			else {
				connectionPoints[0] = (getEntity().getStartingPoint().x + getEntity()
						.getReleasePoint().x) / 2;
				connectionPoints[1] = getEntity().getStartingPoint().y;
				connectionPoints[2] = (getStartingPoint().x + getReleasePoint().x) / 2;
				connectionPoints[3] = getReleasePoint().y;
			}
		}

		if (angle < 0 && angle >= -45) {
			int xDistance = p2.x - p1.x;
			// attribute is on upper right corner of entity within 45D
			if (xDistance > 0) {
				connectionPoints[0] = (getEntity().getStartingPoint().x + getEntity()
						.getReleasePoint().x) / 2;
				connectionPoints[1] = getEntity().getStartingPoint().y;
				connectionPoints[2] = getStartingPoint().x;
				connectionPoints[3] = (getStartingPoint().y + getReleasePoint().y) / 2;
			}
			// attribute is on lower left corner of entity within 45D
			else {
				connectionPoints[0] = (getEntity().getStartingPoint().x + getEntity()
						.getReleasePoint().x) / 2;
				connectionPoints[1] = getEntity().getReleasePoint().y;
				connectionPoints[2] = getReleasePoint().x;
				connectionPoints[3] = (getStartingPoint().y + getReleasePoint().y) / 2;
			}
		}
		
		if (angle < -45 && angle >= -90) {
			int xDistance = p2.x - p1.x;
			// attribute is on upper right corner of entity below -45D
			if (xDistance > 0) {
				connectionPoints[0] = (getEntity().getStartingPoint().x + getEntity()
						.getReleasePoint().x) / 2;
				connectionPoints[1] = getEntity().getStartingPoint().y;
				connectionPoints[2] = (getStartingPoint().x + getReleasePoint().x) / 2;
				connectionPoints[3] = getReleasePoint().y;
			}
			// attribute is on lower left corner of entity below -45D
			else {
				connectionPoints[0] = (getEntity().getStartingPoint().x + getEntity()
						.getReleasePoint().x) / 2;
				connectionPoints[1] = getEntity().getReleasePoint().y;
				connectionPoints[2] = (getStartingPoint().x + getReleasePoint().x) / 2;
				connectionPoints[3] = getStartingPoint().y;
			}
		}
		return connectionPoints;
	}

}
