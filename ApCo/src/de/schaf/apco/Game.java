package de.schaf.apco;

import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glOrtho;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.TextureLoader;

public class Game {

	public static Game Instance; // for lazy bums
	
	public static final int RENDER_WIDTH = 1024;
	public static final int RENDER_HEIGHT = 768;

	private boolean vsync = false;

	private long lastFrame;
	private long lastFPS;
	private int fps;
	long delta;

	private ArrayList<Entity> Entities = new ArrayList<Entity>();
	private Entity BackgroundSprite = null;
	
	public TestPlane TPlane;

	public void start() throws LWJGLException {
		Instance = this;
		
		init();
		initGL();
		// initTextures();
		lastFPS = getTime(); // set lastFPS to current Time
		while (!Display.isCloseRequested()) {
			getDelta();
			logic(delta);
			cls();
			if (Display.isActive() || Display.isVisible() || Display.isDirty()) {
				render();
			}
			Display.update();
			// run at 60 FPS
			if (vsync)
				Display.sync(60);
			updateFPS();
		}
		cleanup();
	}

	public void init() throws LWJGLException {
		Display.setTitle("ApCo!!");
		try {
			Display.setDisplayMode(new DisplayMode(RENDER_WIDTH, RENDER_HEIGHT));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
		Mouse.create();
		
		// Initialize Backgroundsprite
		BackgroundSprite = new Entity();
		try {
			BackgroundSprite.setTexture(TextureLoader.getTexture(
					"PNG",
					this.getClass().getResourceAsStream(
							"/de/schaf/apco/media/grid.png")));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		//
		TPlane = new TestPlane();
		TPlane.setPosition(new Vector3f(400f,300f,0f));
		addObject(TPlane);
	}

	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0; // reset the FPS counter
			lastFPS += 1000; // add one second
		}
		fps++;
	}

	public long getTime() {
		return System.nanoTime() / 1000000;
	}

	public void getDelta() {
		long time = getTime();
		delta = (int) (time - lastFrame);
		lastFrame = time;
	}

	private void logic(long delta) {
		// float delta_mod = (float) delta / 1000;
		MouseControls.Do();
	}

	public void addObject(Entity E) {
		Entities.add(E);
	}

	/* Graphics stuff */

	private void initGL() {
		if (!GLContext.getCapabilities().GL_EXT_framebuffer_object) {
			System.err.println("FBO not supported!");
		}
	}

	private void render() {
		Camera.initISO();
		
		try {
			BackgroundSprite.Render();
		} catch (CorruptTextureException e1) {
			e1.printStackTrace();
		}
		
		Camera.initISO();		

		try {
			drawObjects();
		} catch (CorruptTextureException e) {
			e.printStackTrace();
		}
	}

	private void drawObjects() throws CorruptTextureException {
		for (Entity E : Entities) {
			E.Render();
		}

	}

	private void cleanup() {
		Display.destroy();
	}

	private void cls() {
		// clear the screen
		glClearColor(0.0f, 1.0f, 0.0f, 0.5f);
		// glClear(GL_COLOR_BUFFER_BIT | GL_STENCIL_BUFFER_BIT |
		// GL_DEPTH_BUFFER_BIT);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
	}

	public static void main(String[] args) throws Exception {
		(new Game()).start();
	}
}
