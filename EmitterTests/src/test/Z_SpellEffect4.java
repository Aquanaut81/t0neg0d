package test;

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
import com.jme3.scene.Node;
import com.jme3.scene.control.BillboardControl;
import com.jme3.scene.shape.Cylinder;
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
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Screen;

/**
 * test
 * @author normenhansen
 */
public class Z_SpellEffect4 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Emitter eLightPulse, eRadialGlow, eFlames, eBolts;
	Node testNode = new Node("testNode");
	
	public static void main(String[] args) {
		Z_SpellEffect4 app = new Z_SpellEffect4();
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
		
		// Light Pulse Emitter
		eLightPulse = new Emitter();
		eLightPulse.setName("eLightPulse");
		eLightPulse.setMaxParticles(6);
		eLightPulse.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer()
		);
		eLightPulse.setShapeSimpleEmitter();
		eLightPulse.setEmitterTestMode(false, false);
		eLightPulse.setSprite("Textures/halo.png");
		eLightPulse.setEmissionsPerSecond(2);
		eLightPulse.setParticlesPerEmission(1);
		eLightPulse.setForce(0.01f);
		eLightPulse.setLifeMinMax(1.25f,1.75f);
		eLightPulse.setBillboardMode(Emitter.BillboardMode.Velocity);
		eLightPulse.setDirectionType(EmitterMesh.DirectionType.Normal);
		eLightPulse.setUseRandomEmissionPoint(true);
		eLightPulse.setUseVelocityStretching(false);
	//	eLightPulse.setParticlesFollowEmitter(true);
		
		eLightPulse.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,0f,0f));
		
		eLightPulse.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,0.75f,0.25f,1f));
		eLightPulse.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,0.75f,1f));
		eLightPulse.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		eLightPulse.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp5Out);
		eLightPulse.getInfluencer(AlphaInfluencer.class).addAlpha(2.8f, Interpolation.exp5In);
		eLightPulse.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp5In);
		
		eLightPulse.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.05f,.05f,.05f));
		eLightPulse.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(3f,3f,.3f));
		eLightPulse.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		
		eLightPulse.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		eLightPulse.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 0.25f));
		eLightPulse.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		eLightPulse.setLocalScale(0.5f);
		
		eLightPulse.setLocalRotation(eLightPulse.getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		eLightPulse.setLocalTranslation(0, 0f, -0.52f);
		eLightPulse.setLocalScale(0.05f);
		
		eLightPulse.initialize(assetManager);
		
		// Emitter shape
		Cylinder cylmesh = new Cylinder(2,32,0.5f,0.15f);
		
		// Radial gloaw
		eRadialGlow = new Emitter();
		eRadialGlow.setName("fire");
		eRadialGlow.setMaxParticles(130);
		eRadialGlow.addInfluencers(
			new ColorInfluencer(), 
			new SizeInfluencer(),
			new DestinationInfluencer(),
			new SpriteInfluencer()
		);
		eRadialGlow.setShape(cylmesh);
		eRadialGlow.setDirectionType(EmitterMesh.DirectionType.Normal);

		eRadialGlow.setSprite("Textures/default.png");
		eRadialGlow.setEmissionsPerSecond(60);
		eRadialGlow.setParticlesPerEmission(1);
		eRadialGlow.setForceMinMax(1.2f,2.2f);
		eRadialGlow.setLifeMinMax(1.25f,2.25f);
		eRadialGlow.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up);
		eRadialGlow.setEmitterTestMode(false, false);
		eRadialGlow.setUseVelocityStretching(true);
		eRadialGlow.setVelocityStretchFactor(1.45f);
		eRadialGlow.setUseSequentialEmissionFace(true);
	//	eRadialGlow.setParticlesFollowEmitter(true);
		
		eRadialGlow.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,0f,0f,0.35f));
		eRadialGlow.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,0f,0.1f));
		
		eRadialGlow.getInfluencer(SizeInfluencer.class).addSize(0f, Interpolation.exp10Out);
		eRadialGlow.getInfluencer(SizeInfluencer.class).addSize(.4f, Interpolation.linear);
		eRadialGlow.getInfluencer(SizeInfluencer.class).addSize(.075f, Interpolation.exp5In);
		eRadialGlow.getInfluencer(SizeInfluencer.class).addSize(.025f);
		eRadialGlow.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		eRadialGlow.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,-1f,0), .125f, Interpolation.exp10In);
		
		eRadialGlow.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		eRadialGlow.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		eRadialGlow.setLocalTranslation(0, 0f, -0.51f);
		eRadialGlow.setLocalScale(0.25f);
		
		eRadialGlow.initialize(assetManager);
		
		// Flames Emitter
		eFlames = new Emitter();
		eFlames.setName("eFlames");
		eFlames.setMaxParticles(130);
		eFlames.addInfluencers(
			new ColorInfluencer(), 
			new SizeInfluencer(),
			new DestinationInfluencer(),
			new SpriteInfluencer()
		);
		eFlames.setShape(cylmesh);
		eFlames.setDirectionType(EmitterMesh.DirectionType.Normal);

		eFlames.setSprite("Textures/smokes.png", 2, 2);
		eFlames.setEmissionsPerSecond(60);
		eFlames.setParticlesPerEmission(1);
		eFlames.setForceMinMax(2.2f,2.2f);
		eFlames.setLifeMinMax(1.75f,1.75f);
		eFlames.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up_Y_Left);
		eFlames.setEmitterTestMode(false, false);
		eFlames.setUseVelocityStretching(true);
		eFlames.setVelocityStretchFactor(1.25f);
	//	eFlames.setParticlesFollowEmitter(true);
		
		eFlames.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,0.0f,0f,1f));
		eFlames.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,0f,1f));
			
		eFlames.getInfluencer(SizeInfluencer.class).addSize(0f, Interpolation.exp10Out);
		eFlames.getInfluencer(SizeInfluencer.class).addSize(.4f, Interpolation.linear);
		eFlames.getInfluencer(SizeInfluencer.class).addSize(.07f, Interpolation.exp5In);
		eFlames.getInfluencer(SizeInfluencer.class).addSize(.075f);
		eFlames.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		eFlames.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,-2f,0), .055f, Interpolation.linear);
		eFlames.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,4f,0), .01f, Interpolation.exp10Out);
		
		eFlames.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		eFlames.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		eFlames.getInfluencer(SpriteInfluencer.class).setEnabled(false);
		
		eFlames.getLocalRotation().fromAngleAxis(-FastMath.HALF_PI, Vector3f.UNIT_X);
		eFlames.getParticleNode().getLocalRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		eFlames.getParticleTestNode().getLocalRotation().fromAngleAxis(FastMath.HALF_PI, Vector3f.UNIT_X);
		
		eFlames.setLocalTranslation(0,0,0);
		eFlames.setLocalScale(0.15f);
		
		eFlames.initialize(assetManager);
		
		// Electrical Bolts Emitter
		eBolts = new Emitter();
		eBolts.setName("eBolts");
		eBolts.setMaxParticles(25);
		eBolts.addInfluencers(
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new SpriteInfluencer()
		);
		eBolts.setShape(cylmesh);
		eBolts.setDirectionType(EmitterMesh.DirectionType.Normal);

		eBolts.setSprite("Textures/bolts.png", 3, 3);
		eBolts.setEmissionsPerSecond(7);
		eBolts.setParticlesPerEmission(3);
		eBolts.setForceMinMax(0.001f,0.001f);
		eBolts.setLifeMinMax(1f,1f);
		eBolts.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up);
		eBolts.setParticleEmissionPoint(Emitter.ParticleEmissionPoint.Particle_Edge_Bottom);
		eBolts.setEmitterTestMode(false, false);
		eBolts.setUseVelocityStretching(false);
		eBolts.setVelocityStretchFactor(1.45f);
	//	eBolts.setParticlesFollowEmitter(true);
		
		eBolts.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.5f,0.5f,1f,1f));
		
		eBolts.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(0.25f,1.1f,0f));
		eBolts.getInfluencer(SizeInfluencer.class).setEnabled(false);
		
		eBolts.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp10Out);
		eBolts.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5In);
		eBolts.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.linear);
		
		eBolts.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		eBolts.getInfluencer(SpriteInfluencer.class).setAnimate(true);
		eBolts.getInfluencer(SpriteInfluencer.class).setFixedDuration(1f/9f);
		
		eBolts.setLocalTranslation(0, 0f, -0.5f);
		eBolts.setLocalScale(-0.275f);
		
		eBolts.initialize(assetManager);
		
		// GUI Components
		ButtonAdapter b = new ButtonAdapter(screen, new Vector2f(0,0)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (testNode.getControl(Emitter.class) == null) {
					testNode.addControl(eLightPulse);
					testNode.addControl(eRadialGlow);
					testNode.addControl(eFlames);
					testNode.addControl(eBolts);
					eLightPulse.setEnabled(true);
					eRadialGlow.setEnabled(true);
					eFlames.setEnabled(true);
					eBolts.setEnabled(true);
					setText("Remove");
				} else {
					eLightPulse.setEnabled(false);
					eRadialGlow.setEnabled(false);
					eFlames.setEnabled(false);
					eBolts.setEnabled(false);
					testNode.removeControl(eLightPulse);
					testNode.removeControl(eRadialGlow);
					testNode.removeControl(eFlames);
					testNode.removeControl(eBolts);
					eLightPulse.killAllParticles();
					eRadialGlow.killAllParticles();
					eFlames.killAllParticles();
					eBolts.killAllParticles();
					setText("Add");
				}
			}
		};
		b.setText("Add");
		screen.addElement(b);
		
		ButtonAdapter b2 = new ButtonAdapter(screen, new Vector2f(0,b.getHeight())) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (!eLightPulse.isEnabled()) {
					eLightPulse.setEnabled(true);
					eRadialGlow.setEnabled(true);
					eFlames.setEnabled(true);
					eBolts.setEnabled(true);
					setText("Pause");
				} else {
					eLightPulse.setEnabled(false);
					eRadialGlow.setEnabled(false);
					eFlames.setEnabled(false);
					eBolts.setEnabled(false);
					setText("Resume");
				}
			}
		};
		b2.setText("Pause");
		screen.addElement(b2);
		
		BillboardControl bbCtrl = new BillboardControl();
	//	testNode.addControl(bbCtrl);
		
	//	testNode.setLocalTranslation(5,0,0);
		rootNode.attachChild(testNode);
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
	}

	float rot = 0;
	boolean dir = true;
	@Override
	public void simpleUpdate(float tpf) {
		float[] angles = new float[3];
		testNode.getLocalRotation().toAngles(angles);
		angles[1] += tpf;
	//	testNode.setLocalRotation(testNode.getLocalRotation().fromAngles(angles));
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
