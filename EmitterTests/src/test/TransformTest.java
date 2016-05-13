package test;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
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

/**
 * test
 * @author normenhansen
 */
public class TransformTest extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Sphere s;
	Quaternion q = new Quaternion();
	Node n;
	Geometry g;
	Mesh m;
	Vector3f scale = new Vector3f(1,1,1);
	boolean dir = true;
	Emitter e1;
	
	public static void main(String[] args) {
		TransformTest app = new TransformTest();
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
		
		//Node hs = (Node)assetManager.loadModel("Models/Character-HMN-FEM.j3o");
		//Mesh hsmesh = ((Geometry)hs.getChild(0)).getMesh();
		
		n = (Node)assetManager.loadModel("Models/Circle.j3o");
		m = ((Geometry)n.getChild(0)).getMesh();
		
		
		e1 = new Emitter();
		e1.setName("e1");
		e1.setMaxParticles(500);
		e1.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new DestinationInfluencer()
		);
		e1.setShape(m);
	//	e1.initParticles(ParticleDataTriMesh.class, null);
	//	e1.initParticles(ParticleTriMesh.class, hsmesh);
	//	e1.initParticleQuadMesh();
		
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
	//	mat.setTexture("ColorMap", e1.setSprite("Textures/grass01.png", 250, 250));
		mat.setColor("Color", ColorRGBA.Blue);
		mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		mat.getAdditionalRenderState().setWireframe(true);
	//	mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
	//	mat.getAdditionalRenderState().setAlphaTest(true);
	//	mat.getAdditionalRenderState().setAlphaFallOff(.5f);
		
	//	e1.setMaterial(mat);
		e1.setDirectionType(EmitterMesh.DirectionType.Normal);
		e1.setBillboardMode(Emitter.BillboardMode.Camera);
		e1.setSprite("Textures/glow.png");
		e1.setForceMinMax(4.0f, 4.0f);
		
		e1.setLifeMinMax(2.0f,2.0f);
		e1.setEmissionsPerSecond(250);
		e1.setParticlesPerEmission(1);
		e1.setEmitterTestMode(false, false);
	//	e1.setUseStaticParticles(true);
		e1.setUseRandomEmissionPoint(true);
		e1.setParticlesFollowEmitter(false);
		
		e1.getInfluencer(GravityInfluencer.class).setGravity(0,0,0);
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(0.1f,0.1f,1));
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(0.275f,0.075f,1));
	//	e1.getInfluencer(SizeInfluencer.class).setInterpolation(Interpolation.exp10Out);
	//	e1.getInfluencer(SizeInfluencer.class).setUseInterpolation(false);
		
	//	e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Blue);
	//	e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(.5f,.5f,1,0.15f));
		
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Red, Interpolation.linear);
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Red, Interpolation.linear);
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Orange, Interpolation.exp5Out);
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Yellow, Interpolation.linear);
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Orange, Interpolation.exp5In);
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Red, Interpolation.linear);
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Red, Interpolation.linear);
	//	e1.getInfluencer(ColorInfluencer.class).setUseInterpolation(true);
		e1.getInfluencer(ColorInfluencer.class).setFixedDuration(.0735f);
		/*
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.2f,0.2f,1,1f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.2f,0.2f,1,1f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.2f,0.2f,1,1f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.2f,0.2f,1,1f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.2f,0.2f,1,1f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1,1f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1,1f));
		e1.getInfluencer(ColorInfluencer.class).setFixedDuration(.225f);
		*/
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp10Out);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(1f);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp5Out);
	//	e1.getInfluencer(AlphaInfluencer.class).setUseInterpolation(true);
		
		
		e1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0, 0, 1.5f));
		e1.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		e1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,0f,0), .01f);
	//	e1.getInfluencer(DestinationInfluencer.class).setInterpolation(Interpolation.bounce);
	//	e1.getInfluencer(DestinationInfluencer.class).setUseInterpolation(true);
		e1.getInfluencer(DestinationInfluencer.class).setEnabled(true);
		
		e1.initialize(assetManager);
		rootNode.addControl(e1);
		e1.setEnabled(true);
		
	//	g = new Geometry();
	//	g.setMesh(m);
	//	n = new Node();
	//	n.attachChild(g);
	//	n = e1.getNode();
	//	rootNode.attachChild(n);
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
	}

	float rot = 0;
	Vector3f v = new Vector3f(5,1,1);
	Vector3f loc = new Vector3f();
	boolean lDir = true;
	
	@Override
	public void simpleUpdate(float tpf) {
		if (dir) {
			scale.addLocal(tpf, 0, 0);
			if (scale.x >= 4.15f)
				dir = false;
		} else {
			scale.subtractLocal(tpf, 0, 0);
			if (scale.x <= 1f)
				dir = true;
		}
		if (lDir) {
			loc.addLocal(tpf*2.5f, 0, 0);
			if (loc.x >= 8f)
				lDir = false;
		} else {
			loc.subtractLocal(tpf*2.5f, 0, 0);
			if (loc.x <= 1f)
				lDir = true;
		}
		rot += tpf;
		q.fromAngles(rot,rot*2,rot*0.5f);
		e1.setLocalRotation(q);
		e1.setLocalScale(scale);
		e1.setLocalTranslation(loc);
		
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
