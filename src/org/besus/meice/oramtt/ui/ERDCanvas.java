package org.besus.meice.oramtt.ui;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Point;
import java.util.List;

import org.besus.meice.oramtt.erd.model.ERDiagram;
import org.besus.meice.oramtt.erd.model.Model;


public class ERDCanvas extends Canvas {

	private static final long serialVersionUID = 1L;

	private ERDiagram diagram;

	public ERDCanvas(ERDiagram diagram) {
		this.diagram = diagram;
	}
	
	public ERDiagram getDiagram() {
		return diagram;
	}

	public void paint(Graphics g) {
		List<Model> models = diagram.getModels();

		for (Model model : models) {
			Point definingLoc1 = model.getStartingPoint();
			Point definingLoc2 = model.getReleasePoint();
			model.paint(definingLoc1, definingLoc2);
		}
	}
}
