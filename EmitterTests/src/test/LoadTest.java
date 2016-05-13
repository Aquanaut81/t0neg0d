package test;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.font.BitmapFont;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import java.util.Timer;
import emitter.Emitter;
import java.io.File;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 * test
 * @author normenhansen
 */
public class LoadTest extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Emitter hotDebris, hotDebrisSmoke, debris, blast1, blast2;
	Timer timer, timer2, timer3, timer4;
	Application app;
	BitmapFont font;
	Geometry floor, ob1, ob2, ob3, ob4;
	
	Screen screen;
	TextField emitterName, emitterPCount;
	ButtonAdapter createEmitter;
	
    public static void main(String[] args) {
        LoadTest app = new LoadTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		viewPort.setBackgroundColor(ColorRGBA.Black);
		
	//	setupKeys();
	//	createGround();
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		inputManager.setCursorVisible(true);
		
		screen = new Screen(this, "tonegod/gui/style/atlasdef/style_map.gui.xml");
		screen.setUseTextureAtlas(true, "tonegod/gui/style/atlasdef/atlas.png");
		guiNode.addControl(screen);
		
        AmbientLight al = new AmbientLight();
		al.setColor(new ColorRGBA(1f, 1f, 1f, 1f));
		rootNode.addLight(al);
		
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
		sun.setColor(new ColorRGBA(1f,1f,1f,1f));
		rootNode.addLight(sun);
		
		app = this;
		
		String userHome = System.getProperty("user.home");
		assetManager.registerLocator(userHome, FileLocator.class);
		File file = new File(userHome + "/Models/FX1.j3o");
		Emitter e1 = (Emitter)assetManager.loadAsset("Models/FX1.j3o");
		e1.initialize(assetManager);
		
		rootNode.addControl(e1);
		
		System.out.println(e1.getShape().getMesh().getClass().getSimpleName());
	}
	
	@Override
    public void simpleUpdate(float tpf) {
		
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

	public void onAction(String name, boolean isPressed, float tpf) {
		
	}
	
}
