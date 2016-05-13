package test.radial;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import emitter.Emitter;
import emitter.EmitterMesh;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.RadialVelocityInfluencer;
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
public class RadialTest01 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Node emitterModelNode;
	Mesh emitterModelMesh;
	Emitter eRadialVel1, eRadialVel2, eRadialVel3, eRadialVel4, eRadialVel5;
	Screen screen;
	ButtonAdapter b, b2;
	
	public static void main(String[] args) {
		RadialTest01 app = new RadialTest01();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		this.setPauseOnLostFocus(true);
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		cam.setLocation(new Vector3f(4.4215417f, 2.3955333f, 15.1434145f));
		cam.setRotation(new Quaternion().set(-0.001677987f, 0.9923465f, -0.12272506f, -0.013568077f));
		inputManager.setCursorVisible(true);
		
		emitterModelNode = (Node)assetManager.loadModel("Models/Portal/Ring.j3o");
		emitterModelMesh = ((Geometry)emitterModelNode.getChild(0)).getMesh();
		
		createEmitter1();
		createEmitter2();
		createEmitter3();
		createEmitter4();
		createEmitter5();
		addGUI();
	}
	
	private void createEmitter1() {
		eRadialVel1 = new Emitter();
		eRadialVel1.setName("eRadialVel1");
		eRadialVel1.setMaxParticles(405);
		eRadialVel1.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer(),
			new RadialVelocityInfluencer()
		);
		eRadialVel1.setShape(emitterModelMesh);
		
		eRadialVel1.setSprite("Textures/Debris.png", 3, 3);
		eRadialVel1.setDirectionType(EmitterMesh.DirectionType.NormalNegate);
		eRadialVel1.setBillboardMode(Emitter.BillboardMode.Camera);
		eRadialVel1.setForceMinMax(0.31f, 2.31f);
		eRadialVel1.setLifeMinMax(2f, 2f);
		eRadialVel1.setEmissionsPerSecond(100);
		eRadialVel1.setParticlesPerEmission(2);
		eRadialVel1.setEmitterTestMode(false, false);
		
		eRadialVel1.getInfluencer(GravityInfluencer.class).setGravity(0,-24,0);
		eRadialVel1.getInfluencer(GravityInfluencer.class).setAlignment(GravityInfluencer.GravityAlignment.World);
		
		eRadialVel1.getInfluencer(RadialVelocityInfluencer.class).setRadialPullAlignment(RadialVelocityInfluencer.RadialPullAlignment.Emission_Point);
		eRadialVel1.getInfluencer(RadialVelocityInfluencer.class).setTangentForce(4);
		eRadialVel1.getInfluencer(RadialVelocityInfluencer.class).setRadialPull(3.5f);
		
		eRadialVel1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.075f,.075f,.075f));
		eRadialVel1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.04f,.04f,.04f));
		
		eRadialVel1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.LightGray, Interpolation.linear);
		
		eRadialVel1.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5Out);
		eRadialVel1.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5In);
		eRadialVel1.getInfluencer(AlphaInfluencer.class).addAlpha(0);
		
		eRadialVel1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 13));
		eRadialVel1.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		eRadialVel1.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		eRadialVel1.getInfluencer(RotationInfluencer.class).setEnabled(true);
		
		eRadialVel1.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		eRadialVel1.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		eRadialVel1.getEmitterNode().setLocalRotation(eRadialVel1.getEmitterNode().getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		eRadialVel1.setLocalScale(0.5f,0.4f,1);
		
		eRadialVel1.initialize(assetManager);
	}
	
	private void createEmitter2() {
		eRadialVel2 = new Emitter();
		eRadialVel2.setName("eRadialVel2");
		eRadialVel2.setMaxParticles(405);
		eRadialVel2.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer(),
			new RadialVelocityInfluencer()
		);
		eRadialVel2.setShape(emitterModelMesh);
		
		eRadialVel2.setSprite("Textures/Debris.png", 3, 3);
		eRadialVel2.setDirectionType(EmitterMesh.DirectionType.NormalNegate);
		eRadialVel2.setBillboardMode(Emitter.BillboardMode.Camera);
		eRadialVel2.setForceMinMax(0.31f, 2.31f);
		eRadialVel2.setLifeMinMax(2f, 2f);
		eRadialVel2.setEmissionsPerSecond(100);
		eRadialVel2.setParticlesPerEmission(2);
		eRadialVel2.setEmitterTestMode(false, false);
		
		eRadialVel2.getInfluencer(GravityInfluencer.class).setGravity(0,-12,0);
		
		eRadialVel2.getInfluencer(RadialVelocityInfluencer.class).setRadialPullAlignment(RadialVelocityInfluencer.RadialPullAlignment.Emitter_Center);
		eRadialVel2.getInfluencer(RadialVelocityInfluencer.class).setTangentForce(8);
		eRadialVel2.getInfluencer(RadialVelocityInfluencer.class).setRadialPull(1.5f);
		
		eRadialVel2.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.075f,.075f,.075f));
		eRadialVel2.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.04f,.04f,.04f));
		
		eRadialVel2.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.LightGray, Interpolation.linear);
		
		eRadialVel2.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5Out);
		eRadialVel2.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5In);
		eRadialVel2.getInfluencer(AlphaInfluencer.class).addAlpha(0);
		
		eRadialVel2.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 13));
		eRadialVel2.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		eRadialVel2.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		eRadialVel2.getInfluencer(RotationInfluencer.class).setEnabled(true);
		
		eRadialVel2.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		eRadialVel2.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		eRadialVel2.getEmitterNode().setLocalRotation(eRadialVel2.getEmitterNode().getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		eRadialVel2.setLocalTranslation(4f,0f,0f);
		
		eRadialVel2.initialize(assetManager);
	}
	
	private void createEmitter3() {
		eRadialVel3 = new Emitter();
		eRadialVel3.setName("eRadialVel3");
		eRadialVel3.setMaxParticles(405);
		eRadialVel3.addInfluencers(
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer(),
			new RadialVelocityInfluencer()
		);
		eRadialVel3.setShape(emitterModelMesh);
		
		eRadialVel3.setSprite("Textures/Debris.png", 3, 3);
		eRadialVel3.setDirectionType(EmitterMesh.DirectionType.NormalNegate);
		eRadialVel3.setBillboardMode(Emitter.BillboardMode.Camera);
		eRadialVel3.setForceMinMax(0.31f, 2.31f);
		eRadialVel3.setLifeMinMax(2f, 2f);
		eRadialVel3.setEmissionsPerSecond(100);
		eRadialVel3.setParticlesPerEmission(2);
		eRadialVel3.setEmitterTestMode(false, false);
		
		eRadialVel3.getInfluencer(RadialVelocityInfluencer.class).setRadialPullAlignment(RadialVelocityInfluencer.RadialPullAlignment.Emitter_Center);
		eRadialVel3.getInfluencer(RadialVelocityInfluencer.class).setTangentForce(3);
		eRadialVel3.getInfluencer(RadialVelocityInfluencer.class).setRadialPull(6.5f);
		
		eRadialVel3.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.075f,.075f,.075f));
		eRadialVel3.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.04f,.04f,.04f));
		
		eRadialVel3.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.LightGray, Interpolation.linear);
		
		eRadialVel3.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5Out);
		eRadialVel3.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5In);
		eRadialVel3.getInfluencer(AlphaInfluencer.class).addAlpha(0);
		
		eRadialVel3.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 13));
		eRadialVel3.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		eRadialVel3.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		eRadialVel3.getInfluencer(RotationInfluencer.class).setEnabled(true);
		
		eRadialVel3.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		eRadialVel3.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		eRadialVel3.getEmitterNode().setLocalRotation(eRadialVel3.getEmitterNode().getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		eRadialVel3.setLocalScale(0.5f,0.4f,1);
		eRadialVel3.setLocalTranslation(0f,-4f,1);
		
		eRadialVel3.initialize(assetManager);
	}
	
	private void createEmitter4() {
		eRadialVel4 = new Emitter();
		eRadialVel4.setName("eRadialVel4");
		eRadialVel4.setMaxParticles(405);
		eRadialVel4.addInfluencers(
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer(),
			new RadialVelocityInfluencer()
		);
		eRadialVel4.setShape(emitterModelMesh);
		
		eRadialVel4.setSprite("Textures/Debris.png", 3, 3);
		eRadialVel4.setDirectionType(EmitterMesh.DirectionType.NormalNegate);
		eRadialVel4.setBillboardMode(Emitter.BillboardMode.Camera);
		eRadialVel4.setForceMinMax(2.31f, 2.31f);
		eRadialVel4.setLifeMinMax(2f, 2f);
		eRadialVel4.setEmissionsPerSecond(100);
		eRadialVel4.setParticlesPerEmission(2);
		eRadialVel4.setEmitterTestMode(false, false);
		
		eRadialVel4.getInfluencer(RadialVelocityInfluencer.class).setRadialPullAlignment(RadialVelocityInfluencer.RadialPullAlignment.Emitter_Center);
		eRadialVel4.getInfluencer(RadialVelocityInfluencer.class).setRadialPullCenter(RadialVelocityInfluencer.RadialPullCenter.Absolute);
		eRadialVel4.getInfluencer(RadialVelocityInfluencer.class).setRadialUpAlignment(RadialVelocityInfluencer.RadialUpAlignment.UNIT_Z);
		eRadialVel4.getInfluencer(RadialVelocityInfluencer.class).setTangentForce(8);
		eRadialVel4.getInfluencer(RadialVelocityInfluencer.class).setRadialPull(1.85f);
		eRadialVel4.getInfluencer(RadialVelocityInfluencer.class).setUseRandomDirection(false);
		
		eRadialVel4.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.075f,.075f,.075f));
		eRadialVel4.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.04f,.04f,.04f));
		
		eRadialVel4.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.LightGray, Interpolation.linear);
		
		eRadialVel4.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5Out);
		eRadialVel4.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5In);
		eRadialVel4.getInfluencer(AlphaInfluencer.class).addAlpha(0);
		
		eRadialVel4.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 13));
		eRadialVel4.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		eRadialVel4.getInfluencer(RotationInfluencer.class).setUseRandomDirection(false);
		eRadialVel4.getInfluencer(RotationInfluencer.class).setEnabled(true);
		
		eRadialVel4.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		eRadialVel4.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
	//	eRadialVel4.getEmitterNode().setLocalRotation(eRadialVel1.getEmitterNode().getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		eRadialVel4.setLocalTranslation(4f,-4f,0f);
		eRadialVel4.setLocalRotation(eRadialVel1.getEmitterNode().getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		
		eRadialVel4.initialize(assetManager);
	}
	
	private void createEmitter5() {
		eRadialVel5 = new Emitter();
		eRadialVel5.setName("eRadialVel5");
		eRadialVel5.setMaxParticles(405);
		eRadialVel5.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer(),
			new RadialVelocityInfluencer()
		);
		eRadialVel5.setShape(emitterModelMesh);
		
		eRadialVel5.setSprite("Textures/Debris.png", 3, 3);
		eRadialVel5.setDirectionType(EmitterMesh.DirectionType.NormalNegate);
		eRadialVel5.setBillboardMode(Emitter.BillboardMode.Camera);
		eRadialVel5.setForceMinMax(0.31f, 2.31f);
		eRadialVel5.setLifeMinMax(2f, 2f);
		eRadialVel5.setEmissionsPerSecond(100);
		eRadialVel5.setParticlesPerEmission(2);
		eRadialVel5.setEmitterTestMode(false, false);
		
		eRadialVel5.getInfluencer(GravityInfluencer.class).setGravity(0,-6,0);
		
		eRadialVel5.getInfluencer(RadialVelocityInfluencer.class).setRadialPullAlignment(RadialVelocityInfluencer.RadialPullAlignment.Emitter_Center);
		eRadialVel5.getInfluencer(RadialVelocityInfluencer.class).setTangentForce(8);
		eRadialVel5.getInfluencer(RadialVelocityInfluencer.class).setRadialPull(6.5f);
		
		eRadialVel5.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.075f,.075f,.075f));
		eRadialVel5.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.04f,.04f,.04f));
		
		eRadialVel5.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.LightGray, Interpolation.linear);
		
		eRadialVel5.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5Out);
		eRadialVel5.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5In);
		eRadialVel5.getInfluencer(AlphaInfluencer.class).addAlpha(0);
		
		eRadialVel5.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 13));
		eRadialVel5.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		eRadialVel5.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		eRadialVel5.getInfluencer(RotationInfluencer.class).setEnabled(true);
		
		eRadialVel5.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		eRadialVel5.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		eRadialVel5.getEmitterNode().setLocalRotation(eRadialVel5.getEmitterNode().getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		eRadialVel5.setLocalScale(0.5f,0.4f,1);
		eRadialVel5.setLocalTranslation(8f,-4f,1);
		
		eRadialVel5.initialize(assetManager);
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
	}

	private void addGUI() {
		screen = new Screen(this);
		guiNode.addControl(screen);
		
		b = new ButtonAdapter(screen, new Vector2f(0,0)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (eRadialVel1.getSpatial() == null) {
					rootNode.addControl(eRadialVel1);
					rootNode.addControl(eRadialVel3);
					rootNode.addControl(eRadialVel2);
					rootNode.addControl(eRadialVel4);
					rootNode.addControl(eRadialVel5);
					eRadialVel1.setEnabled(true);
					eRadialVel3.setEnabled(true);
					eRadialVel2.setEnabled(true);
					eRadialVel4.setEnabled(true);
					eRadialVel5.setEnabled(true);
					setText("Remove");
				} else {
					eRadialVel1.setEnabled(false);
					eRadialVel3.setEnabled(false);
					eRadialVel2.setEnabled(false);
					eRadialVel4.setEnabled(false);
					eRadialVel5.setEnabled(false);
					rootNode.removeControl(eRadialVel1);
					rootNode.removeControl(eRadialVel3);
					rootNode.removeControl(eRadialVel2);
					rootNode.removeControl(eRadialVel4);
					rootNode.removeControl(eRadialVel5);
					eRadialVel1.killAllParticles();
					eRadialVel3.killAllParticles();
					eRadialVel2.killAllParticles();
					eRadialVel4.killAllParticles();
					eRadialVel5.killAllParticles();
					setText("Add");
				}
			}
		};
		b.setText("Add");
		screen.addElement(b);
		
		b2 = new ButtonAdapter(screen, new Vector2f(0,b.getHeight())) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (!eRadialVel1.isEnabled()) {
					eRadialVel1.setEnabled(true);
					eRadialVel3.setEnabled(true);
					eRadialVel2.setEnabled(true);
					eRadialVel4.setEnabled(true);
					eRadialVel5.setEnabled(true);
					setText("Pause");
				} else {
					eRadialVel1.setEnabled(false);
					eRadialVel3.setEnabled(false);
					eRadialVel2.setEnabled(false);
					eRadialVel4.setEnabled(false);
					eRadialVel5.setEnabled(false);
					setText("Resume");
				}
			}
		};
		b2.setText("Pause");
		screen.addElement(b2);
	}
	
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
