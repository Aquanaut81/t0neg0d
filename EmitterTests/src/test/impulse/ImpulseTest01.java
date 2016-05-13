package test.impulse;

import com.jme3.animation.AnimChannel;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import emitter.Emitter;
import emitter.EmitterMesh;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.ImpulseInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Screen;

/**
 * test
 * @author normenhansen
 */
public class ImpulseTest01 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Sphere s;
	Emitter effectring;
	Emitter e1, e2, e3, e4;
	Node ring;
	AnimChannel c;
	
	public static void main(String[] args) {
		ImpulseTest01 app = new ImpulseTest01();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		this.setPauseOnLostFocus(true);
		
		cam.setLocation(new Vector3f(-0.0011532659f, 4.7550282f, 4.340925f));
		cam.setRotation(new Quaternion(-0.005735758f, 0.92004764f, -0.39153254f, -0.0134782195f));
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		inputManager.setCursorVisible(true);
		
		Screen screen = new Screen(this);
		guiNode.addControl(screen);
		
		Node cyl = (Node)assetManager.loadModel("Models/Cylinder.j3o");
		Mesh cylmesh = ((Geometry)cyl.getChild(0)).getMesh();
		
		e2 = new Emitter();
		e2.setName("e2");
		e2.setMaxParticles(130);
		e2.addInfluencers(
			new ColorInfluencer(), 
			new SizeInfluencer(),
			new ImpulseInfluencer(),
			new SpriteInfluencer()
		);
		e2.setShape(cylmesh);
		e2.setDirectionType(EmitterMesh.DirectionType.Normal);

		e2.setSprite("Textures/glow.png");
		e2.setEmissionsPerSecond(60);
		e2.setParticlesPerEmission(1);
		e2.setForceMinMax(1.2f,2.2f);
		e2.setLifeMinMax(1.75f,2.75f);
		e2.setBillboardMode(Emitter.BillboardMode.Camera);
		e2.setEmitterTestMode(false, false);
		e2.setUseVelocityStretching(false);
		e2.setVelocityStretchFactor(1.45f);
		e2.setUseSequentialEmissionFace(true);
		
		e2.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,0f,0f,0.35f));
		e2.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,0f,0.1f));
		
		e2.getInfluencer(SizeInfluencer.class).addSize(.125f);
		
	//	e2.getInfluencer(ImpulseInfluencer.class).setStrength(1);
	//	e2.getInfluencer(ImpulseInfluencer.class).setMagnitude(1);
	//	e2.getInfluencer(ImpulseInfluencer.class).setChance(1);
		
		e2.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		e2.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		e2.setLocalTranslation(0,0,0);
		e2.setLocalScale(.5f);
		
		e2.initialize(assetManager);
		
		
		ButtonAdapter b = new ButtonAdapter(screen, new Vector2f(0,0)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (rootNode.getControl(Emitter.class) == null) {
					rootNode.addControl(e2);
					e2.setEnabled(true);
					setText("Remove");
				} else {
					e2.setEnabled(false);
					rootNode.removeControl(e2);
					e2.killAllParticles();
					setText("Add");
				}
			}
		};
		b.setText("Add");
		screen.addElement(b);
		
		ButtonAdapter b2 = new ButtonAdapter(screen, new Vector2f(0,b.getHeight())) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (!e1.isEnabled()) {
					e2.setEnabled(true);
					setText("Pause");
				} else {
					e2.setEnabled(false);
					setText("Resume");
				}
			}
		};
		b2.setText("Pause");
		screen.addElement(b2);
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
	}

	float rot = 0;
	boolean dir = true;
	@Override
	public void simpleUpdate(float tpf) {
		
	}

	@Override
	public void simpleRender(RenderManager rm) {
		//TODO: add render code
	}

	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("F9")) {
			if (!isPressed) {
				if (stateManager.hasState(vrAppState))	{ 
					System.out.println("Stopping video recorder");
					stateManager.detach(vrAppState);
				} else {
					System.out.println("Starting video recorder");
					stateManager.attach(vrAppState);
				}
			}
		}
	}
}
