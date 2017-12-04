package org.besus.meice.oramtt.erd.model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import org.besus.meice.oramtt.ui.EntityDialog;


public class Entity extends Model {

	private List<Attribute> attributes;

	public Entity() {
		attributes = new ArrayList<Attribute>();
	}

	public void addAttribute(Attribute attribute) {
		attributes.add(attribute);
	}

	@Override
	Model draw(Point definingLoc1, Point definingLoc2) {
		getEditor().getGraphics().clearRect(0, 0,
				getEditor().getBounds().width, getEditor().getBounds().height);
		paint(definingLoc1, definingLoc2);

		Entity entity = new Entity();
		entity.setStartingPoint(definingLoc1);
		entity.setReleasePoint(definingLoc2);
		return entity;
	}

	public void paint(Point definingLoc1, Point definingLoc2) {
		Graphics g = getEditor().getGraphics();

		Color color = Color.BLACK;
		g.setColor(color);
		g.drawRect(definingLoc1.x, definingLoc1.y, definingLoc2.x
				- definingLoc1.x, definingLoc2.y - definingLoc1.y);

		if (getName() != null) {
			int nameLength = getName().length();
			g.drawString(getName(), (definingLoc1.x + definingLoc2.x) / 2
					- 2*nameLength , (definingLoc1.y + definingLoc2.y) / 2);
		}
	}

	@Override
	void redraw(Point definingLoc1, Point definingLoc2) {
		paint(definingLoc1, definingLoc2);
	}

	@Override
	void openDialog(Model drawnModel) {
		if (drawnModel instanceof Entity) {
			new EntityDialog((Entity) drawnModel, getEditor().getDiagram());
		}
	}

}
