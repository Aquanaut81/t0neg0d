package test;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import emitter.Emitter;
import emitter.EmitterMesh;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.DestinationInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.ImpulseInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;

/**
 * test
 * @author normenhansen
 */
public class FlameSphere extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Node eNode;
	Geometry eGeom;
	Sphere eMesh;
	
	public static void main(String[] args) {
		FlameSphere app = new FlameSphere();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		setupKeys();
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		inputManager.setCursorVisible(true);
		
		createGround();
		
		eMesh = new Sphere (32,32,1);
		
		Node campfire = (Node)assetManager.loadModel("Models/Campfire.j3o");
		Mesh campfiremesh = ((Geometry)campfire.getChild(0)).getMesh();
		
		Emitter e1 = new Emitter();
		e1.setName("e1");
		e1.setMaxParticles(100);
		e1.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer(),
			new ImpulseInfluencer(),
			new DestinationInfluencer()
		);
		e1.setShape(eMesh);
	//	e1.initParticles(ParticleDataTriMesh.class, null);
		e1.setSprite("Textures/fire_4x.png",2,2);
		
		e1.setBillboardMode(Emitter.BillboardMode.Camera);
		e1.setForceMinMax(2.1f, 3.1f);
		e1.setLifeMinMax(0.45f,1f);
		e1.setEmissionsPerSecond(100);
		e1.setParticlesPerEmission(1);
		e1.setEmitterTestMode(false, false);
		e1.setUseRandomEmissionPoint(true);
		e1.setUseVelocityStretching(false);
	//	e1.setVelocityStretchFactor(0.2f);
		
		e1.getInfluencer(GravityInfluencer.class).setGravity(0,-2,0);
		
		e1.getInfluencer(DestinationInfluencer.class).removeAll();
		e1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,4,0), .52f, Interpolation.exp5Out);
		e1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,8,0), .42f, Interpolation.exp5Out);
	//	e1.getInfluencer(DestinationInfluencer.class).setUseInterpolation(true);
		
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(.15f, Interpolation.exp10Out);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(.6f);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(.3f, Interpolation.exp5In);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(0f);
		
		e1.getInfluencer(SizeInfluencer.class).addSize(.3f, Interpolation.exp10Out);
		e1.getInfluencer(SizeInfluencer.class).addSize(.5f);
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.1f,.1f,1f), Interpolation.exp5In);
		e1.getInfluencer(SizeInfluencer.class).addSize(0f);
		
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Orange);
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Yellow);
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Orange);
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.White);
		
		e1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 0));
		e1.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		e1.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		e1.getInfluencer(RotationInfluencer.class).setUseRandomSpeed(true);
		
		e1.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		e1.getInfluencer(SpriteInfluencer.class).setAnimate(true);
		e1.getInfluencer(SpriteInfluencer.class).setFixedDuration(1f/8f);
		
		e1.setLocalScale(.35f);
		e1.initialize(assetManager);
		rootNode.addControl(e1);
		e1.setEnabled(true);
		
		
		Emitter e1a = new Emitter();
		e1a.setName("e1a");
		e1a.setMaxParticles(75);
		e1a.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer(),
			new ImpulseInfluencer(),
			new DestinationInfluencer()
		);
		e1a.setShape(eMesh);
	//	e1a.initParticles(ParticleDataTriMesh.class, null);
		e1a.setSprite("Textures/Fire/Flame02.png",4,4);
		
		e1a.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up);
		e1a.setForceMinMax(3.1f, 4.1f);
		e1a.setLifeMinMax(0.45f,1f);
		e1a.setEmissionsPerSecond(75);
		e1a.setParticlesPerEmission(1);
		e1a.setEmitterTestMode(false, false);
		e1a.setUseRandomEmissionPoint(true);
		e1a.setUseVelocityStretching(true);
		e1a.setVelocityStretchFactor(0.175f);
		
		e1a.getInfluencer(GravityInfluencer.class).setGravity(0,-2,0);
		
		e1a.getInfluencer(DestinationInfluencer.class).removeAll();
		e1a.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,4,0), .42f, Interpolation.exp5Out);
		e1a.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,8,0), .32f, Interpolation.exp5Out);
	//	e1a.getInfluencer(DestinationInfluencer.class).setUseInterpolation(true);
		
		e1a.getInfluencer(AlphaInfluencer.class).addAlpha(.15f, Interpolation.exp10Out);
		e1a.getInfluencer(AlphaInfluencer.class).addAlpha(.4f);
		e1a.getInfluencer(AlphaInfluencer.class).addAlpha(.3f, Interpolation.exp5In);
		e1a.getInfluencer(AlphaInfluencer.class).addAlpha(0f);
		
		e1a.getInfluencer(SizeInfluencer.class).addSize(.0f, Interpolation.exp10Out);
		e1a.getInfluencer(SizeInfluencer.class).addSize(.5f);
		e1a.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.4f,.4f,1f), Interpolation.exp5In);
		e1a.getInfluencer(SizeInfluencer.class).addSize(0f);
		
		e1a.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Orange);
		e1a.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Yellow);
		e1a.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Orange);
		e1a.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.White);
		
		e1a.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 0));
		e1a.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		e1a.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		e1a.getInfluencer(RotationInfluencer.class).setUseRandomSpeed(true);
		
		e1a.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		e1a.getInfluencer(SpriteInfluencer.class).setAnimate(true);
		e1a.getInfluencer(SpriteInfluencer.class).setFixedDuration(1f/8f);
		
		e1a.initialize(assetManager);
		e1a.setLocalScale(.135f);
		rootNode.addControl(e1a);
		e1a.setEnabled(true);
		
		
		Emitter e2 = new Emitter();
		e2.setName("e2");
		e2.setMaxParticles(400);
		e2.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer(),
			new ImpulseInfluencer()
		);
		e2.setShapeSimpleEmitter();
		e2.setDirectionType(EmitterMesh.DirectionType.RandomNormalAligned);
	//	e2.initParticles(ParticleDataTriMesh.class, null);
		e2.setSprite("Textures/fire02.png",4,2);
		
		e2.setBillboardMode(Emitter.BillboardMode.Camera);
		e2.setForceMinMax(1.1f, 1.81f);
		e2.setLifeMinMax(2.45f,3f);
		e2.setEmissionsPerSecond(100);
		e2.setParticlesPerEmission(1);
		e2.setEmitterTestMode(false, false);
		e2.setUseRandomEmissionPoint(true);
		e2.setUseVelocityStretching(false);
	//	e2.setVelocityStretchFactor(0.02f);
		
		e2.getInfluencer(GravityInfluencer.class).setGravity(0,-22,0);
		
		e2.getInfluencer(AlphaInfluencer.class).addAlpha(.1f, Interpolation.exp10Out);
		e2.getInfluencer(AlphaInfluencer.class).addAlpha(.2f);
		e2.getInfluencer(AlphaInfluencer.class).addAlpha(.1f, Interpolation.exp5In);
		e2.getInfluencer(AlphaInfluencer.class).addAlpha(0f);
		
		e2.getInfluencer(SizeInfluencer.class).addSize(.6f, Interpolation.exp10Out);
		e2.getInfluencer(SizeInfluencer.class).addSize(1.8f);
		e2.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(2.7f,2.7f,1f), Interpolation.exp5In);
		e2.getInfluencer(SizeInfluencer.class).addSize(.2f);
		
		e2.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(.4f,.4f,.4f,0.2f));
		e2.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(.8f,.8f,.8f,0.15f));
		e2.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(.2f,.2f,.2f,0.1f));
		
		e2.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 0));
		e2.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		e2.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		e2.getInfluencer(RotationInfluencer.class).setUseRandomSpeed(true);
		
		e2.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		e2.getInfluencer(SpriteInfluencer.class).setAnimate(false);
	//	e2.getInfluencer(SpriteInfluencer.class).setFixedDuration(1/8);
		
		e2.initialize(assetManager);
		e2.setLocalScale(.35f);
		rootNode.addControl(e2);
		e2.setEnabled(true);
		
	}
	
	private void createGround() {
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.DarkGray);
		mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		
		Box c = new Box(new Vector3f(135,0.015f,135),new Vector3f(1,1,1));
		Geometry floor = new Geometry("Floor Geom");
		floor.setMesh(c);
		floor.setMaterial(mat);
		Node n = new Node("Floor Node");
		n.attachChild(floor);;
		floor.center();
		n.setLocalTranslation(0,-1,0);
		rootNode.attachChild(n);
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
	}

	@Override
	public void simpleUpdate(float tpf) {
		//TODO: add update code
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
