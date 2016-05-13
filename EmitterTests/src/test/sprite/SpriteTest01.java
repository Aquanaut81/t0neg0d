package test.sprite;

import com.jme3.animation.AnimChannel;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import emitter.Emitter;
import emitter.EmitterMesh;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.DestinationInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;
import emitter.particle.ParticleDataTriMesh;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Screen;

/**
 * test
 * @author normenhansen
 */
public class SpriteTest01 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Sphere s;
	Emitter effectring;
	Emitter eSprite;
	Node ring;
	AnimChannel c;
	
	public static void main(String[] args) {
		SpriteTest01 app = new SpriteTest01();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		this.setPauseOnLostFocus(true);
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		inputManager.setCursorVisible(true);
		
		Screen screen = new Screen(this);
		guiNode.addControl(screen);
		
		Node cyl = (Node)assetManager.loadModel("Models/Cylinder.j3o");
		Mesh cylmesh = ((Geometry)cyl.getChild(0)).getMesh();
		
		eSprite = new Emitter();
		eSprite.setName("eSprite");
		eSprite.setMaxParticles(25);
		eSprite.addInfluencers(
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new SpriteInfluencer()
		);
		eSprite.setShape(cylmesh);
		eSprite.setDirectionType(EmitterMesh.DirectionType.Normal);

		eSprite.setSprite("Textures/bolts.png", 3, 3);
		eSprite.setEmissionsPerSecond(7);
		eSprite.setParticlesPerEmission(3);
		eSprite.setForceMinMax(0.001f,0.001f);
		eSprite.setLifeMinMax(1f,1f);
		eSprite.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up_Y_Left);
		eSprite.setEmitterTestMode(false, false);
		eSprite.setUseVelocityStretching(false);
		eSprite.setVelocityStretchFactor(1.45f);
		
		eSprite.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.5f,0.5f,1f,1f));
		eSprite.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.5f,0.5f,1f,1f));
		
		eSprite.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(0.25f,1,0f));
		eSprite.getInfluencer(SizeInfluencer.class).setEnabled(false);
		
		eSprite.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp10Out);
		eSprite.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5In);
		eSprite.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.linear);
		
		eSprite.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		eSprite.getInfluencer(SpriteInfluencer.class).setAnimate(true);
		eSprite.getInfluencer(SpriteInfluencer.class).setFixedDuration(1f/9f);
		
		eSprite.getParticleNode().getLocalRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		
		eSprite.setLocalTranslation(0,0,0);
		eSprite.setLocalScale(.8f);
		
		eSprite.initialize(assetManager);
		
		ButtonAdapter b = new ButtonAdapter(screen, new Vector2f(0,0)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (eSprite.getSpatial() == null) {
					rootNode.addControl(eSprite);
					eSprite.setEnabled(true);
					setText("Remove");
				} else {
					eSprite.setEnabled(false);
					rootNode.removeControl(eSprite);
					eSprite.reset();
					setText("Add");
				}
			}
		};
		b.setText("Add");
		screen.addElement(b);
		
		ButtonAdapter b2 = new ButtonAdapter(screen, new Vector2f(0,b.getHeight())) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (!eSprite.isEnabled()) {
					eSprite.setEnabled(true);
					setText("Pause");
				} else {
					eSprite.setEnabled(false);
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
