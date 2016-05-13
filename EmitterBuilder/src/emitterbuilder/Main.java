package emitterbuilder;

import com.jme3.app.SimpleApplication;
import com.jme3.math.Vector2f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.gui.FileBrowser;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d <cristofflanders@gmail.com>
 */
public class Main extends SimpleApplication {
	Screen screen;
	Node testScene;
	
    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    @Override
    public void simpleInitApp() {
		inputManager.deleteMapping(INPUT_MAPPING_EXIT);
		
		flyCam.setDragToRotate(true);
		getInputManager().setCursorVisible(true);
		flyCam.setMoveSpeed(30);
		
                screen = new Screen(this,"tonegod/gui/style/atlasdef/style_map.gui.xml");
		screen.setUseTextureAtlas(true, "tonegod/gui/style/atlasdef/atlas.png");
		screen.setUseCustomCursors(true);
		guiNode.addControl(screen);
		
		EmitterBuilder eb = new EmitterBuilder(this, screen);
		getStateManager().attach(eb);
		/*
		FileBrowser fb = new FileBrowser(screen, Vector2f.ZERO, new Vector2f(400,100), true);
		screen.addElement(fb);
		fb.centerToParent();
		fb.setValidExtensions(".j3o",".png",".jpg",".jpeg");
		fb.setRootFolder("Textures");
		*/
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
