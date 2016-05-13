package test;

import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.font.BitmapFont;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import java.util.Timer;
import emitter.Emitter;
import emitter.Emitter.BillboardMode;
import emitter.EmitterMesh.DirectionType;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 * test
 * @author normenhansen
 */
public class CloneTest extends SimpleApplication implements ActionListener {
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
        CloneTest app = new CloneTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		viewPort.setBackgroundColor(ColorRGBA.Black);
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		inputManager.setCursorVisible(true);
		
	    AmbientLight al = new AmbientLight();
		al.setColor(new ColorRGBA(1f, 1f, 1f, 1f));
		rootNode.addLight(al);
		
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
		sun.setColor(new ColorRGBA(1f,1f,1f,1f));
		rootNode.addLight(sun);

		Node emitterNode = (Node)assetManager.loadModel("Models/Character-HMN-FEM.j3o");
		Mesh emitterMmesh = ((Geometry)emitterNode.getChild(0)).getMesh();
		
		app = this;
		
		Emitter e1 = new Emitter();
		e1.setName("e1");
		e1.setMaxParticles(100);
		e1.addInfluencers(
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new SpriteInfluencer(),
			new RotationInfluencer(),
			new GravityInfluencer()
		);
	//	Sphere e1ES = new Sphere(16, 16, 0.5f);
		e1.setShape(emitterNode, false);
		e1.setDirectionType(DirectionType.Normal);
		e1.setEmissionsPerSecond(100);
		e1.setParticlesPerEmission(1);
		e1.setBillboardMode(BillboardMode.Camera);
		e1.setForceMinMax(0.25f,0.3f);
		e1.setLifeMinMax(0.999f,0.999f);
		e1.setSprite("Textures/flash.png", 2, 2);
		e1.setEmitterTestMode(true, false);
		// Color Influencer
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1.0f,0.5f,0.0f,1.0f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1.0f,1.0f,0.0f,1.0f));

		// Alpha Influencer
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(1f);

		// Size Influencer
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.05f,.05f,.05f));
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(0.0125f,0.0125f,0.0125f));

		// Sprite Influencer
		// Rotation Influencer
		e1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0.0f,0.0f,3.0f));
		e1.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false,false,true);
		e1.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		e1.getInfluencer(RotationInfluencer.class).setUseRandomSpeed(true);

		// Gravity Influencer
		e1.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0.0f,-0.5f,0.0f));

		// Set transforms and add to scene
		e1.setLocalTranslation(0.0f, 0.0f, 0.0f);
		e1.setLocalScale(1.0f, 1.0f, 1.0f);
		e1.setLocalRotation(new Quaternion().fromAngles(0.0f,0.0f,0.0f));
		e1.initialize(assetManager);
		rootNode.addControl(e1);
		e1.setEnabled(true);
		
		e1.setEmitterAnimation("run", 1, 1, LoopMode.Loop);
		
		Emitter e2 = e1.clone();
		e2.setLocalTranslation(2,0,0);
		
		rootNode.addControl(e2);
	}
	
	private void setupKeys() {
		
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
