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
public class Z_SpellEffect2 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
        Emitter e1, e2, e3, e4, e5, e6;
	
	public static void main(String[] args) {
		Z_SpellEffect2 app = new Z_SpellEffect2();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		flyCam.setMoveSpeed(10);
                flyCam.setDragToRotate(true);
		
		e1 = new Emitter();
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
		e1.setBillboardMode(Emitter.BillboardMode.UNIT_Y);
		e1.setUseRandomEmissionPoint(false);
		e1.setUseVelocityStretching(false);
		
		e1.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,0f,0f));
		
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0f,0f,1f,1f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1f,1f));
		e1.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp5In);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(1, Interpolation.exp5Out);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp5In);
		
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.05f,.05f,.05f));
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(2f,2f,.5f));
		e1.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		
		e1.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		e1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 0.25f));
		e1.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		e1.setLocalScale(0.5f);
		
		e1.setLocalTranslation(0, 0.01f, 0);
		e1.initialize(assetManager);
		rootNode.addControl(e1);
		e1.setEnabled(true);
		
		e2 = new Emitter();
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
		
		e2.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.25f,0.25f,1f,1f));
		e2.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1f,1f));
		e2.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e2.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp5In);
		e2.getInfluencer(AlphaInfluencer.class).addAlpha(0.1f, Interpolation.exp5In);
		e2.getInfluencer(AlphaInfluencer.class).addAlpha(0, Interpolation.exp5In);
		
		e2.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.05f,.05f,.05f));
		e2.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(2f,2f,.5f));
		e2.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		e2.initialize(assetManager);
		rootNode.addControl(e2);
		e2.setEnabled(true);
		
		
		e3 = new Emitter();
		e3.setName("e3");
		e3.setMaxParticles(70);
		e3.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer()
		);
		e3.setShapeSimpleEmitter();
		e3.setDirectionType(DirectionType.RandomTangent);
	//	e3.initParticles(ParticleDataTriMesh.class, null);
		e3.setEmitterTestMode(false, false);
		e3.setSprite("Textures/glow.png");
		e3.setEmissionsPerSecond(60);
		e3.setParticlesPerEmission(1);
		e3.setForce(1.25f);
		e3.setLifeMinMax(1.15f,1.15f);
		e3.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up_Y_Left);
		e3.setUseRandomEmissionPoint(false);
		e3.setUseVelocityStretching(false);
		e3.setVelocityStretchFactor(3.65f);
		
		e3.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,0f,0f));
		e3.getInfluencer(GravityInfluencer.class).setAlignment(GravityAlignment.Emission_Point);
		e3.getInfluencer(GravityInfluencer.class).setMagnitude(1.5f);
		
		e3.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1f,1f));
		e3.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1f,1f));
		e3.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e3.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp5Out);
		e3.getInfluencer(AlphaInfluencer.class).addAlpha(0.35f, Interpolation.linear);
		e3.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp5In);
	//	e3.getInfluencer(AlphaInfluencer.class).addAlpha(.2f, Interpolation.exp5In);
		
		e3.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.015f,.015f,.015f));
		e3.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.015f,.015f,.015f));
		e3.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		e3.initialize(assetManager);
		rootNode.addControl(e3);
		e3.setEnabled(true);
		
		
		Node circle = (Node)assetManager.loadModel("Models/Circle.j3o");
		Mesh circleMesh = ((Geometry)circle.getChild(0)).getMesh();
		
		// Water spout
		e4 = new Emitter();
		e4.setName("e4");
		e4.setMaxParticles(110);
		e4.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new SpriteInfluencer(),
			new RotationInfluencer()
		);
		e4.setShape(circleMesh);
		e4.setDirectionType(DirectionType.Normal);
	//	e4.initParticles(ParticleDataTriMesh.class, null);
		e4.setEmitterTestMode(false, false);
		e4.setSprite("Textures/smokes.png", 2, 2);
		e4.setEmissionsPerSecond(100);
		e4.setParticlesPerEmission(1);
		e4.setForceMinMax(2.25f, 3.75f);
		e4.setLifeMinMax(1.15f,1.15f);
		e4.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up);
		e4.setUseRandomEmissionPoint(false);
		e4.setUseVelocityStretching(true);
		e4.setVelocityStretchFactor(3.65f);
		
		e4.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,0f,0f));
		e4.getInfluencer(GravityInfluencer.class).setAlignment(GravityAlignment.Emission_Point);
		e4.getInfluencer(GravityInfluencer.class).setMagnitude(1.5f);
		
		e4.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1f,1f));
		e4.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.45f,0.45f,1f,1f));
		e4.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e4.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp5Out);
		e4.getInfluencer(AlphaInfluencer.class).addAlpha(0.35f, Interpolation.linear);
		e4.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp5In);
	//	e4.getInfluencer(AlphaInfluencer.class).addAlpha(.2f, Interpolation.exp5In);
		
		e4.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.05f,.05f,.05f));
		e4.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.05f,.05f,.05f));
		e4.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		e4.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		e4.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		e4.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		
		e4.setLocalScale(0.015f);
		
		e4.initialize(assetManager);
		rootNode.addControl(e4);
		e4.setEnabled(true);
		
		Node hemi = (Node)assetManager.loadModel("Models/SphereEmitter.j3o");
		Mesh hemiMesh = ((Geometry)hemi.getChild(0)).getMesh();
		
		e5 = new Emitter();
		e5.setName("e5");
		e5.setMaxParticles(110);
		e5.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new SpriteInfluencer()
		);
		e5.setShape(hemiMesh);
		e5.setDirectionType(DirectionType.Normal);
	//	e5.initParticles(ParticleDataTriMesh.class, null);
		e5.setEmitterTestMode(false, false);
		e5.setSprite("Textures/smokes.png", 2, 2);
		e5.setEmissionsPerSecond(100);
		e5.setParticlesPerEmission(1);
		e5.setForceMinMax(0.55f, 1.25f);
		e5.setLifeMinMax(.5f,.5f);
		e5.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up);
		e5.setUseRandomEmissionPoint(true);
		e5.setUseVelocityStretching(true);
		e5.setVelocityStretchFactor(1.65f);
		
		e5.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,2f,0f));
		e5.getInfluencer(GravityInfluencer.class).setAlignment(GravityAlignment.World);
		e5.getInfluencer(GravityInfluencer.class).setMagnitude(1.5f);
		
		e5.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1f,1f));
		e5.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.45f,0.45f,1f,1f));
		e5.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e5.getInfluencer(AlphaInfluencer.class).addAlpha(0.05f, Interpolation.exp5Out);
		e5.getInfluencer(AlphaInfluencer.class).addAlpha(0.35f, Interpolation.linear);
		e5.getInfluencer(AlphaInfluencer.class).addAlpha(0.1f, Interpolation.exp5In);
	//	e4.getInfluencer(AlphaInfluencer.class).addAlpha(.2f, Interpolation.exp5In);
		
		e5.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.075f,.075f,.075f));
		e5.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.015f,.015f,.015f));
		e5.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		e5.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		e5.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		e5.initialize(assetManager);
		e5.setLocalScale(0.05f);
		rootNode.addControl(e5);
		e5.setEnabled(true);
		
		
		e6 = new Emitter();
		e6.setName("e6");
		e6.setMaxParticles(110);
		e6.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new SpriteInfluencer(),
			new RotationInfluencer()
		);
		e6.setShape(hemiMesh);
		e6.setDirectionType(DirectionType.Normal);
	//	e6.initParticles(ParticleDataTriMesh.class, null);
		e6.setEmitterTestMode(false, false);
		e6.setSprite("Textures/smokes.png", 2, 2);
		e6.setEmissionsPerSecond(100);
		e6.setParticlesPerEmission(1);
		e6.setForceMinMax(.55f, 0.75f);
		e6.setLifeMinMax(.25f,.45f);
		e6.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up);
		e6.setUseRandomEmissionPoint(true);
		e6.setUseVelocityStretching(false);
		e6.setVelocityStretchFactor(1.65f);
		
		e6.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,-7f,0f));
		e6.getInfluencer(GravityInfluencer.class).setAlignment(GravityAlignment.World);
		e6.getInfluencer(GravityInfluencer.class).setMagnitude(1.5f);
		
		e6.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1f,1f));
		e6.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.45f,0.45f,1f,1f));
		e6.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e6.getInfluencer(AlphaInfluencer.class).addAlpha(0.05f, Interpolation.exp5Out);
		e6.getInfluencer(AlphaInfluencer.class).addAlpha(0.35f, Interpolation.linear);
		e6.getInfluencer(AlphaInfluencer.class).addAlpha(0.1f, Interpolation.exp5In);
	//	e4.getInfluencer(AlphaInfluencer.class).addAlpha(.2f, Interpolation.exp5In);
		
		e6.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.075f,.075f,.075f));
		e6.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.115f,.115f,.015f));
		e6.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.075f,.075f,.075f));
		e6.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		e6.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		e6.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		e6.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, false);
		e6.getInfluencer(RotationInfluencer.class).setEnabled(false);
		
		e6.initialize(assetManager);
		e6.setLocalScale(0.05f);
		e6.setLocalTranslation(0,0f,0);
		rootNode.addControl(e6);
		e6.setEnabled(true);
                
                setupKeys();
	}
	
	private void setupKeys() {
		inputManager.addMapping("Key_1", new KeyTrigger(KeyInput.KEY_1));
		inputManager.addListener(this, "Key_1");
                inputManager.addMapping("Key_2", new KeyTrigger(KeyInput.KEY_2));
		inputManager.addListener(this, "Key_2");
                inputManager.addMapping("Key_3", new KeyTrigger(KeyInput.KEY_3));
		inputManager.addListener(this, "Key_3");
                inputManager.addMapping("Key_4", new KeyTrigger(KeyInput.KEY_4));
		inputManager.addListener(this, "Key_4");
                inputManager.addMapping("Key_5", new KeyTrigger(KeyInput.KEY_5));
		inputManager.addListener(this, "Key_5");
                inputManager.addMapping("Key_6", new KeyTrigger(KeyInput.KEY_6));
		inputManager.addListener(this, "Key_6");
	}

	float rot = 0;
	@Override
	public void simpleUpdate(float tpf) {
	
	}

	@Override
	public void simpleRender(RenderManager rm) {
		//TODO: add render code
	}

        @Override
	public void onAction(String name, boolean isPressed, float tpf) {
            if(!isPressed) {
                if (name.equals("Key_1")) e1.setEnabled(!e1.isEnabled());
                if (name.equals("Key_2")) e2.setEnabled(!e2.isEnabled());
                if (name.equals("Key_3")) e3.setEnabled(!e3.isEnabled());
                if (name.equals("Key_4")) e4.setEnabled(!e4.isEnabled());
                if (name.equals("Key_5")) e5.setEnabled(!e5.isEnabled());
                if (name.equals("Key_6")) e6.setEnabled(!e6.isEnabled());
            }
	}
}
