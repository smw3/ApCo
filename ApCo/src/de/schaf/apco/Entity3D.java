package de.schaf.apco;

import org.lwjgl.opengl.GL11;

public class Entity3D extends Entity {
	protected MeshObj Model = null;
	
	public void setModel(MeshObj MO) {
		Model = MO;
	}
	
	public void Render() throws CorruptTextureException {
		if (Texture == null)
			throw new CorruptTextureException("Nullpointer");
		GL11.glPushMatrix();
		{
			GL11.glTranslatef(getPosition().x,getPosition().y,0f);
			GL11.glScalef(1f, 1f, 1f);
			Model.setActiveTexture(Texture);
			Model.render();
		}
		GL11.glPopMatrix();
	}
}
