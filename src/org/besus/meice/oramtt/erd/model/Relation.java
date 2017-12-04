package org.besus.meice.oramtt.erd.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.besus.meice.oramtt.ui.RelationDialog;
import org.besus.meice.oramtt.util.RelationType;


public class Relation extends Model {

	private RelationType type;
	private Entity fromEntity;
	private Entity toEntity;

	public RelationType getType() {
		return type;
	}

	public void setType(RelationType type) {
		this.type = type;
	}

	public Entity getFromEntity() {
		return fromEntity;
	}

	public void setFromEntity(Entity fromEntity) {
		this.fromEntity = fromEntity;
	}

	public Entity getToEntity() {
		return toEntity;
	}

	public void setToEntity(Entity toEntity) {
		this.toEntity = toEntity;
	}

	@Override
	Model draw(Point definingLoc1, Point definingLoc2) {
		getEditor().getGraphics().clearRect(0, 0,
				getEditor().getBounds().width, getEditor().getBounds().height);
		paint(definingLoc1, definingLoc2);

		Relation relation = new Relation();
		relation.setStartingPoint(definingLoc1);
		relation.setReleasePoint(definingLoc2);
		return relation;
	}

	public void paint(Point definingLoc1, Point definingLoc2) {
		Graphics g = getEditor().getGraphics();
		g.setColor(Color.BLACK);

		Point upperMidPoint = new Point((definingLoc1.x + definingLoc2.x) / 2,
				definingLoc1.y);
		Point lowerMidPoint = new Point((definingLoc1.x + definingLoc2.x) / 2,
				definingLoc2.y);
		Point leftMidPoint = new Point(definingLoc1.x,
				(definingLoc1.y + definingLoc2.y) / 2);
		Point rightMidPoint = new Point(definingLoc2.x,
				(definingLoc1.y + definingLoc2.y) / 2);

		g.drawLine(upperMidPoint.x, upperMidPoint.y, leftMidPoint.x,
				leftMidPoint.y);
		g.drawLine(upperMidPoint.x, upperMidPoint.y, rightMidPoint.x,
				rightMidPoint.y);
		g.drawLine(lowerMidPoint.x, lowerMidPoint.y, leftMidPoint.x,
				leftMidPoint.y);
		g.drawLine(lowerMidPoint.x, lowerMidPoint.y, rightMidPoint.x,
				rightMidPoint.y);

		if (getName() != null) {
			int nameLength = getName().length();
			g.drawString(getName(), (definingLoc1.x + definingLoc2.x) / 2 - 2
					* nameLength, (definingLoc1.y + definingLoc2.y) / 2);
		}

		if (getFromEntity() != null && getToEntity() != null) {
			int[] connectionPoints = new int[8];
			/*
			 * if (getFromEntity().equals(getToEntity())) { connectionPoints =
			 * getConnectionsOnSameSide(getFromEntity()); } else {
			 */
			//connectionPoints = getConnectionsOnOppositeSides(getFromEntity(),
					//getToEntity());
			// }
			getConnectionsOnOppositeSides(getFromEntity(), getToEntity(), g);
			
		}
	}

	@Override
	void redraw(Point definingLoc1, Point definingLoc2) {
		paint(definingLoc1, definingLoc2);
	}

	@Override
	void openDialog(Model drawnModel) {
		new RelationDialog((Relation) drawnModel, getEditor().getDiagram());
	}

	private int[] getConnectionsOnSameSide(Entity entity) {
		int[] connectionPoints = new int[8];
		return connectionPoints;
	}

	private void getConnectionsOnOppositeSides(Entity fromEntity,
			Entity toEntity, Graphics g) {
		//int[] connectionPoints = new int[8];
		Point midPoint = new Point(
				(getStartingPoint().x + getReleasePoint().x) / 2,
				(getStartingPoint().y + getReleasePoint().y) / 2);
		Point fromEntityMidpoint = new Point(
				(fromEntity.getStartingPoint().x + fromEntity.getReleasePoint().x) / 2,
				(fromEntity.getStartingPoint().y + fromEntity.getReleasePoint().y) / 2);
		Point toEntityMidpoint = new Point(
				(toEntity.getStartingPoint().x + toEntity.getReleasePoint().x) / 2,
				(toEntity.getStartingPoint().y + toEntity.getReleasePoint().y) / 2);

		int[] fromEntityConnections = getConnections(fromEntity, midPoint,
				fromEntityMidpoint);
		int[] toEntityConnections = getConnections(toEntity, midPoint,
				toEntityMidpoint);
		
		g.drawLine(fromEntityConnections[0], fromEntityConnections[1],
				fromEntityConnections[2], fromEntityConnections[3]);
		g.drawLine(toEntityConnections[0], toEntityConnections[1],
				toEntityConnections[2], toEntityConnections[3]);

		//return connectionPoints;
		switch(getType()) {
		case MANY_TO_MANY :
			break;
		case MANY_TO_ONE :
			drawArrow(toEntityConnections[0], toEntityConnections[1],
				toEntityConnections[2], toEntityConnections[3]);
			break;
		case ONE_TO_MANY :
			drawArrow(fromEntityConnections[0], fromEntityConnections[1],
				fromEntityConnections[2], fromEntityConnections[3]);
			break;
		case ONE_TO_ONE :
			drawArrow(fromEntityConnections[0], fromEntityConnections[1],
					fromEntityConnections[2], fromEntityConnections[3]);
			drawArrow(toEntityConnections[0], toEntityConnections[1],
					toEntityConnections[2], toEntityConnections[3]);
			break;
		default :
			break;
		}
	}

	private int[] getConnections(Entity fromEntity, Point midPoint,
			Point fromEntityMidpoint) {
		int connectionPoints[] = new int[4];

		double angleToFromEntity = ((Math
				.atan((double) (fromEntityMidpoint.y - midPoint.y)
						/ (double) (fromEntityMidpoint.x - midPoint.x))) * 180.0)
				/ Math.PI;

		if (angleToFromEntity >= 0 && angleToFromEntity <= 45) {
			int xDistance = fromEntityMidpoint.x - midPoint.x;
			// attribute is on lower right corner of entity within 45D
			if (xDistance > 0) {
				connectionPoints[0] = getReleasePoint().x;
				connectionPoints[1] = (getStartingPoint().y + getReleasePoint().y) / 2;
				connectionPoints[2] = (fromEntity.getStartingPoint().x + fromEntity
						.getReleasePoint().x) / 2;
				connectionPoints[3] = fromEntity.getStartingPoint().y;
			}
			// attribute is on upper left corner of entity within 45D
			else {
				connectionPoints[0] = fromEntity.getReleasePoint().x;
				connectionPoints[1] = (fromEntity.getStartingPoint().y + fromEntity
						.getReleasePoint().y) / 2;
				connectionPoints[2] = (getStartingPoint().x + getReleasePoint().x) / 2;
				connectionPoints[3] = getStartingPoint().y;
			}
		}

		if (angleToFromEntity > 45 && angleToFromEntity <= 90) {
			int xDistance = fromEntityMidpoint.x - midPoint.x;
			// attribute is on lower right corner of entity exceeding 45D
			if (xDistance > 0) {
				connectionPoints[0] = (getStartingPoint().x + getReleasePoint().x) / 2;
				connectionPoints[1] = getReleasePoint().y;
				connectionPoints[2] = (fromEntity.getStartingPoint().x + fromEntity
						.getReleasePoint().x) / 2;
				connectionPoints[3] = fromEntity.getStartingPoint().y;
			}
			// attribute is on upper left corner of entity exceeding 45D
			else {
				connectionPoints[0] = (fromEntity.getStartingPoint().x + fromEntity
						.getReleasePoint().x) / 2;
				connectionPoints[1] = fromEntity.getReleasePoint().y;
				connectionPoints[2] = getStartingPoint().x;
				connectionPoints[3] = (getStartingPoint().y + getReleasePoint().y) / 2;
			}
		}

		if (angleToFromEntity < 0 && angleToFromEntity >= -45) {
			int xDistance = fromEntityMidpoint.x - midPoint.x;
			// attribute is on upper right corner of entity within -45D
			if (xDistance > 0) {
				connectionPoints[0] = (fromEntity.getStartingPoint().x + fromEntity
						.getReleasePoint().x) / 2;
				connectionPoints[1] = fromEntity.getReleasePoint().y;
				connectionPoints[2] = getReleasePoint().x;
				connectionPoints[3] = (getStartingPoint().y + getReleasePoint().y) / 2;
			}
			// attribute is on lower left corner of entity within 45D
			else {
				connectionPoints[0] = fromEntity.getReleasePoint().x;
				connectionPoints[1] = (fromEntity.getStartingPoint().y + fromEntity
						.getReleasePoint().y) / 2;
				connectionPoints[2] = getStartingPoint().x;
				connectionPoints[3] = (getStartingPoint().y + getReleasePoint().y) / 2;
			}
		}

		if (angleToFromEntity < -45 && angleToFromEntity >= -90) {
			int xDistance = fromEntityMidpoint.x - midPoint.x;
			// attribute is on upper right corner of entity below -45D
			if (xDistance > 0) {
				connectionPoints[0] = (fromEntity.getStartingPoint().x + fromEntity
						.getReleasePoint().x) / 2;
				connectionPoints[1] = fromEntity.getReleasePoint().y;
				connectionPoints[2] = (getStartingPoint().x + getReleasePoint().x) / 2;
				connectionPoints[3] = getStartingPoint().y;
			}
			// attribute is on lower left corner of entity below -45D
			else {
				connectionPoints[0] = (fromEntity.getStartingPoint().x + fromEntity
						.getReleasePoint().x) / 2;
				connectionPoints[1] = fromEntity.getStartingPoint().y;
				connectionPoints[2] = (getStartingPoint().x + getReleasePoint().x) / 2;
				connectionPoints[3] = getReleasePoint().y;
			}
		}
		return connectionPoints;
	}
	
	private void drawArrow(int sourceX, int sourceY, int targetX, int targetY) {
		
	}
}
