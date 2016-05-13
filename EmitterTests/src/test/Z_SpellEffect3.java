package test;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import emitter.Emitter;
import emitter.EmitterMesh.DirectionType;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.GravityInfluencer.GravityAlignment;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;

/**
 * test
 * @author normenhansen
 */
public class Z_SpellEffect3 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Sphere s;
	Emitter effectring;
	Emitter e3, e4;
	
	public static void main(String[] args) {
		Z_SpellEffect3 app = new Z_SpellEffect3();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		flyCam.setMoveSpeed(10);
		
		Node ring = (Node)assetManager.loadModel("Models/Portal/Ring.j3o");
		Mesh ringMesh = ((Geometry)ring.getChild(0)).getMesh();
		
		Node circle = (Node)assetManager.loadModel("Models/Circle.j3o");
		Mesh circleMesh = ((Geometry)circle.getChild(0)).getMesh();
		
		
		Emitter e1 = new Emitter();
		e1.setName("e1");
		e1.setMaxParticles(6);
		e1.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer()
		);
		e1.setShapeSimpleEmitter();
	//	e1.setDirectionType(DirectionType.Normal);
	//	e1.initParticles(ParticleDataTriMesh.class, null);
		e1.setEmitterTestMode(false, false);
		e1.setSprite("Textures/halo.png");
		e1.setEmissionsPerSecond(2);
		e1.setParticlesPerEmission(1);
		e1.setForce(0.01f);
		e1.setLifeMinMax(1.25f,1.75f);
		e1.setBillboardMode(Emitter.BillboardMode.Velocity);
		e1.setDirectionType(DirectionType.Normal);
		e1.setUseRandomEmissionPoint(true);
		e1.setUseVelocityStretching(false);
		
		e1.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,0f,0f));
		
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,1f,1f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,1f,0.75f,1f));
		e1.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp5Out);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(0.5f, Interpolation.exp5In);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp5In);
		
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.05f,.05f,.05f));
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(2f,2f,.5f));
		e1.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		
		e1.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		e1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 0.25f));
		e1.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		e1.setLocalScale(0.5f);
		
		e1.initialize(assetManager);
		e1.setLocalRotation(e1.getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		e1.setLocalTranslation(0, 0.01f, 0);
		e1.setLocalScale(0.05f);
		rootNode.addControl(e1);
		e1.setEnabled(true);
		
		Emitter e2 = new Emitter();
		e2.setName("e2");
		e2.setMaxParticles(40);
		e2.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer()
		);
		e2.setShapeSimpleEmitter();
		e2.setDirectionType(DirectionType.RandomTangent);
	//	e2.initParticles(ParticleDataTriMesh.class, null);
		e2.setEmitterTestMode(false, false);
		e2.setSprite("Textures/smokes.png", 2, 2);
		e2.setEmissionsPerSecond(35);
		e2.setParticlesPerEmission(1);
		e2.setForce(.25f);
		e2.setLifeMinMax(.5f,1.3f);
		e2.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up_Y_Left);
		e2.setUseRandomEmissionPoint(false);
		e2.setUseVelocityStretching(false);
		
		e2.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,0f,0f));
		
		e2.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.25f,1,0.25f,1f));
		e2.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,1,0.75f,1f));
		e2.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e2.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp5In);
		e2.getInfluencer(AlphaInfluencer.class).addAlpha(0.1f, Interpolation.exp5In);
		e2.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp5In);
		
		e2.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.05f,.05f,.05f));
		e2.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(2f,2f,.5f));
		e2.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		e2.initialize(assetManager);
		e2.getParticleNode().setLocalRotation(e2.getParticleNode().getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		rootNode.addControl(e2);
		e2.setEnabled(true);
		
		e3 = new Emitter();
		e3.setName("e3");
		e3.setMaxParticles(10);
		e3.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer()
		);
		e3.setShapeSimpleEmitter();
		e3.setDirectionType(DirectionType.Normal);
	//	e3.initParticles(ParticleDataTriMesh.class, null);
		e3.setEmitterTestMode(false, false);
		e3.setSprite("Textures/flash.png", 2, 2);
		e3.setEmissionsPerSecond(8);
		e3.setParticlesPerEmission(1);
		e3.setForce(2.5f);
		e3.setLifeMinMax(1f,1f);
		e3.setBillboardMode(Emitter.BillboardMode.Camera);
		e3.setUseRandomEmissionPoint(false);
		e3.setUseVelocityStretching(false);
		
		e3.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,1f,0f));
		e3.getInfluencer(GravityInfluencer.class).setAlignment(GravityAlignment.Emission_Point);
		e3.getInfluencer(GravityInfluencer.class).setMagnitude(2);
		
		e3.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.25f,0.25f,0.25f,1f));
		e3.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,1f,1f));
		e3.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e3.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp5Out);
		e3.getInfluencer(AlphaInfluencer.class).addAlpha(0.65f, Interpolation.exp5In);
		e3.getInfluencer(AlphaInfluencer.class).addAlpha(0.25f, Interpolation.linear);
		e3.getInfluencer(AlphaInfluencer.class).setEnabled(true);
		
		e3.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.15f,.15f,.15f));
		e3.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.015f,.015f,.015f));
		e3.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		e3.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		e3.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		e3.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 10));
		
		e3.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		e3.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		
		e3.initialize(assetManager);
		rootNode.addControl(e3);
		e3.setEnabled(true);
		
		
		e4 = new Emitter();
		e4.setName("e4");
		e4.setMaxParticles(10);
		e4.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer()
		);
		e4.setShapeSimpleEmitter();
		e4.setDirectionType(DirectionType.NormalNegate);
	//	e4.initParticles(ParticleDataTriMesh.class, null);
		e4.setEmitterTestMode(false, false);
		e4.setSprite("Textures/flash.png", 2, 2);
		e4.setEmissionsPerSecond(8);
		e4.setParticlesPerEmission(1);
		e4.setForce(2.5f);
		e4.setLifeMinMax(1f,1f);
		e4.setBillboardMode(Emitter.BillboardMode.Camera);
		e4.setUseRandomEmissionPoint(false);
		e4.setUseVelocityStretching(false);
		
		e4.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,1f,0f));
		e4.getInfluencer(GravityInfluencer.class).setAlignment(GravityAlignment.Emission_Point);
		e4.getInfluencer(GravityInfluencer.class).setMagnitude(2);
		
		e4.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.25f,0.25f,0.25f,1f));
		e4.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,1f,1f));
		e4.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e4.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp5Out);
		e4.getInfluencer(AlphaInfluencer.class).addAlpha(0.65f, Interpolation.exp5In);
		e4.getInfluencer(AlphaInfluencer.class).addAlpha(0.25f, Interpolation.linear);
		e4.getInfluencer(AlphaInfluencer.class).setEnabled(true);
		
		e4.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.15f,.15f,.15f));
		e4.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.015f,.015f,.015f));
		e4.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		e4.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		e4.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		e4.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 10));
		
		e4.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		e4.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		
		e4.initialize(assetManager);
		rootNode.addControl(e4);
		e4.setEnabled(true);
		
		Emitter e5 = new Emitter();
		e5.setName("e5");
		e5.setMaxParticles(70);
		e5.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer()
		);
		e5.setShapeSimpleEmitter();
		e5.setDirectionType(DirectionType.RandomTangent);
	//	e5.initParticles(ParticleDataTriMesh.class, null);
		e5.setEmitterTestMode(false, false);
		e5.setSprite("Textures/glow.png");
		e5.setEmissionsPerSecond(60);
		e5.setParticlesPerEmission(1);
		e5.setForce(1.25f);
		e5.setLifeMinMax(1.15f,1.15f);
		e5.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up_Y_Left);
		e5.setUseRandomEmissionPoint(false);
		e5.setUseVelocityStretching(false);
		e5.setVelocityStretchFactor(3.65f);
		
		e5.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,0f,0f));
		e5.getInfluencer(GravityInfluencer.class).setAlignment(GravityAlignment.Emission_Point);
		e5.getInfluencer(GravityInfluencer.class).setMagnitude(1.5f);
		
		e5.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1f,1f));
		e5.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1f,1f));
		e5.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e5.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp5Out);
		e5.getInfluencer(AlphaInfluencer.class).addAlpha(0.35f, Interpolation.linear);
		e5.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp5In);
	//	e5.getInfluencer(AlphaInfluencer.class).addAlpha(.2f, Interpolation.exp5In);
		
		e5.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.015f,.015f,.015f));
		e5.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.015f,.015f,.015f));
		e5.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		e5.initialize(assetManager);
		e5.getParticleNode().setLocalRotation(e5.getParticleNode().getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
		rootNode.addControl(e5);
		e5.setEnabled(true);
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
	}

	float rot = 0;
	@Override
	public void simpleUpdate(float tpf) {
		rot += tpf*2;
		e3.setLocalRotation(e3.getLocalRotation().fromAngleAxis(rot, Vector3f.UNIT_Z));
		e4.setLocalRotation(e3.getLocalRotation().fromAngleAxis(rot, Vector3f.UNIT_Z));
		
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
