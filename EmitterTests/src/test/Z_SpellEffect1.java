package test;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import emitter.Emitter;
import emitter.EmitterMesh;
import emitter.EmitterMesh.DirectionType;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.GravityInfluencer.GravityAlignment;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.RadialVelocityInfluencer;
import emitter.influencers.RadialVelocityInfluencer.RadialPullAlignment;
import emitter.influencers.RadialVelocityInfluencer.RadialPullCenter;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SpriteInfluencer;

/**
 * test
 * @author normenhansen
 */
public class Z_SpellEffect1 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Sphere s;
	Emitter effectring;
	
	public static void main(String[] args) {
		Z_SpellEffect1 app = new Z_SpellEffect1();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		flyCam.setMoveSpeed(10);
		
		Node temp = (Node)assetManager.loadModel("Models/Circle.j3o");
		Mesh tempmesh = ((Geometry)temp.getChild(0)).getMesh();
		
		Emitter pillar = new Emitter();
		pillar.setName("effectringpe");
		pillar.setMaxParticles(72);
		pillar.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new SizeInfluencer(),
			new RadialVelocityInfluencer()
		);
		pillar.setShapeSimpleEmitter();
		pillar.setDirectionType(DirectionType.Random);
	//	pillar.initParticles(ParticleDataTriMesh.class, null);
		pillar.setEmitterTestMode(false, false);
		pillar.setSprite("Textures/fire_4x.png", 2,2);
		pillar.setEmissionsPerSecond(70);
		pillar.setParticlesPerEmission(1);
		pillar.setForceMinMax(1.35f,2.35f);
		pillar.setLifeMinMax(1f,1f);
		pillar.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up);
		pillar.setUseRandomEmissionPoint(true);
		pillar.setUseSequentialEmissionFace(true);
		pillar.setUseSequentialSkipPattern(true);
		pillar.setUseVelocityStretching(true);
		pillar.setVelocityStretchFactor(0.35f);
		
		pillar.getInfluencer(GravityInfluencer.class).setGravity(0,-20,0);
		
		pillar.getInfluencer(RadialVelocityInfluencer.class).setRadialPullAlignment(RadialPullAlignment.Emission_Point);
		pillar.getInfluencer(RadialVelocityInfluencer.class).setRadialPullCenter(RadialPullCenter.Variable_Y);
		pillar.getInfluencer(RadialVelocityInfluencer.class).setTangentForce(14);
		pillar.getInfluencer(RadialVelocityInfluencer.class).setRadialPull(3.15f);
		pillar.getInfluencer(RadialVelocityInfluencer.class).setUseRandomDirection(true);
		
		pillar.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,1,1f));
		pillar.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0f,0f,1,0.25f));
		pillar.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		pillar.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.3f,.3f,.3f));
		pillar.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.05f,.05f,.05f));
		pillar.getInfluencer(SizeInfluencer.class).setEnabled(false);
		
		pillar.setLocalTranslation(0,.2f,0);
		pillar.setLocalScale(0.5f);
		
		pillar.initialize(assetManager);
		rootNode.addControl(pillar);
		pillar.setEnabled(true);
		
		Emitter base = new Emitter();
		base.setName("base");
		base.setMaxParticles(50);
		base.addInfluencers(
			new ColorInfluencer(),
			new SizeInfluencer(),
			new RadialVelocityInfluencer()
		);
		base.setShapeSimpleEmitter();
		base.setDirectionType(DirectionType.RandomTangent);
	//	base.initParticles(ParticleDataTriMesh.class, null);
		base.setEmitterTestMode(false, false);
		base.setSprite("Textures/fire_4x.png", 2, 2);
		base.setEmissionsPerSecond(50);
		base.setParticlesPerEmission(1);
		base.setForceMinMax(0.75f,1.05f);
		base.setLifeMinMax(1f,1f);
		base.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up_Y_Left);
		base.setUseRandomEmissionPoint(true);
		base.setUseVelocityStretching(true);
		base.setVelocityStretchFactor(.35f);
		
		base.getInfluencer(RadialVelocityInfluencer.class).setRadialPullAlignment(RadialVelocityInfluencer.RadialPullAlignment.Emission_Point);
		base.getInfluencer(RadialVelocityInfluencer.class).setTangentForce(-24);
		base.getInfluencer(RadialVelocityInfluencer.class).setRadialPull(-3);
		
		base.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,1f,1f));
		base.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0f,0f,0,0.025f));
		
		base.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.3f,.3f,.3f));
		base.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(2.35f,2.35f,.05f));
		base.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		base.setLocalTranslation(0,0,0);
		base.getParticleNode().setLocalScale(0.5f);
		
		base.initialize(assetManager);
		rootNode.addControl(base);
		base.setEnabled(true);
		
		
		Node ring = (Node)assetManager.loadModel("Models/Circle.j3o");
		Mesh ringmesh = ((Geometry)ring.getChild(0)).getMesh();
		
		Emitter rocks = new Emitter();
		rocks.setName("rocks");
		rocks.setMaxParticles(200);
		rocks.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer(),
			new RadialVelocityInfluencer()
		);
		rocks.setShape(ringmesh);
	//	rocks.initParticles(ParticleDataTriMesh.class, null);
		rocks.setSprite("Textures/Debris.png", 3, 3);
		rocks.setDirectionType(EmitterMesh.DirectionType.RandomNormalAligned);
		rocks.setBillboardMode(Emitter.BillboardMode.Camera);
		rocks.setForceMinMax(2.31f, 4.31f);
		rocks.setLifeMinMax(1f, 1.25f);
		rocks.setEmissionsPerSecond(100);
		rocks.setParticlesPerEmission(1);
		rocks.setEmitterTestMode(false, false);
		
		rocks.getInfluencer(GravityInfluencer.class).setGravity(0,-0.25f,0);
		rocks.getInfluencer(GravityInfluencer.class).setAlignment(GravityAlignment.Emission_Point);
		rocks.getInfluencer(GravityInfluencer.class).setMagnitude(2);
		
		rocks.getInfluencer(RadialVelocityInfluencer.class).setRadialPullAlignment(RadialVelocityInfluencer.RadialPullAlignment.Emission_Point);
		rocks.getInfluencer(RadialVelocityInfluencer.class).setRadialPullCenter(RadialVelocityInfluencer.RadialPullCenter.Absolute);
		rocks.getInfluencer(RadialVelocityInfluencer.class).setTangentForce(3);
		rocks.getInfluencer(RadialVelocityInfluencer.class).setRadialPull(1);
		
		rocks.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.075f,.075f,.075f));
		rocks.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.04f,.04f,.04f));
		
		rocks.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.LightGray, Interpolation.linear);
		
		rocks.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5Out);
		rocks.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5In);
		rocks.getInfluencer(AlphaInfluencer.class).addAlpha(0);
		
		rocks.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 13));
		rocks.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		rocks.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		
		rocks.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		rocks.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
	//	rocks.getEmitterNode().getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X);
		rocks.setLocalScale(0.5f,0.5f,0.5f);
	//	rocks.setLocalRotation(rocks.getLocalRotation().fromAngles(90*FastMath.DEG_TO_RAD, 0, 0));
		rocks.initialize(assetManager);
		rootNode.addControl(rocks);
		rocks.setEnabled(true);
		
		
		Emitter flares = new Emitter();
		flares.setName("flares");
		flares.setMaxParticles(10);
		flares.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer()
		);
		flares.setShapeSimpleEmitter();
		flares.setDirectionType(DirectionType.RandomNormalAligned);
	//	flares.initParticles(ParticleDataTriMesh.class, null);
		flares.setEmitterTestMode(false, false);
		flares.setSprite("Textures/glow.png");
		flares.setEmissionsPerSecond(2);
		flares.setParticlesPerEmission(2);
		flares.setForceMinMax(6f,6f);
		flares.setLifeMinMax(1f,1f);
		flares.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up);
		flares.setUseVelocityStretching(true);
		
		flares.getInfluencer(GravityInfluencer.class).setGravity(0,6,0);
		flares.getInfluencer(GravityInfluencer.class).setAlignment(GravityAlignment.World);
		flares.getInfluencer(GravityInfluencer.class).setMagnitude(0.25f);
		
		flares.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,0,1f));
		flares.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,0f,0,0.25f));
		
		flares.getInfluencer(AlphaInfluencer.class).addAlpha(1f);
		
		flares.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.1f,.1f,.1f));
		flares.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.065f,.065f,.065f));
		
		flares.setLocalScale(0.5f);
		
		flares.initialize(assetManager);
		rootNode.addControl(flares);
		flares.setEnabled(true);
		
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
	}

	float rot = 0;
	@Override
	public void simpleUpdate(float tpf) {
	//	rot += tpf*50;
	//	effectring.setLocalRotation(effectring.getLocalRotation().fromAngleAxis(rot, Vector3f.UNIT_Y));
		
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
