package org.besus.meice.oramtt.erd.model;

import java.util.ArrayList;
import java.util.List;

public class ERDiagram {

	private List<Model> models;
	
	public ERDiagram() {
		models = new ArrayList<Model>();
	}

	public List<Model> getModels() {
		return models;
	}
	
	public void addModel(Model model) {
		models.add(model);
	}

	public void repaint() {
		for (Model model : models) {
			model.redraw(model.getStartingPoint(), model.getReleasePoint());
		}
	}
	
	public List<Entity> getEntities() {
		List<Entity> entities = new ArrayList<Entity>();
		for(Model model : models) {
			if(model instanceof Entity) {
				entities.add((Entity)model);
			}
		}
		return entities;
	}
	
	public Entity findEntity(String entityName) {
		List<Entity> entities = getEntities();
		for(Entity entity : entities) {
			if(entityName.equals(entity.getName())) {
				return entity;
			}
		}
		return null;
	}
}
