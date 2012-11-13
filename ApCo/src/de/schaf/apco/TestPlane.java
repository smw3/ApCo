package de.schaf.apco;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.newdawn.slick.opengl.TextureLoader;

public class TestPlane extends Entity3D {
	public TestPlane() {
		try {
			setModel(OBJLoader.loadModel("de/schaf/apco/media/Poseidon.obj"));
			// TODO do this properly
            File F = new File("src/de/schaf/apco/media/Poseidon.png");
            setTexture(TextureLoader.getTexture("png", new FileInputStream(F)));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
