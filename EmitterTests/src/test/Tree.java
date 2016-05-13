package test;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import emitter.Emitter;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.ImpulseInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;

/**
 * test
 * @author normenhansen
 */
public class Tree extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	
	public static void main(String[] args) {
		Tree app = new Tree();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		AmbientLight am = new AmbientLight();
		rootNode.addLight(am);
		
		setupKeys();
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		inputManager.setCursorVisible(true);
		
		Node hs = (Node)assetManager.loadModel("Models/Tree/treeemitter.j3o");
		Mesh hsmesh = ((Geometry)hs.getChild(0)).getMesh();
		
		SpriteInfluencer si = new SpriteInfluencer();
		
		Emitter e1 = new Emitter();
		e1.setName("e1");
		e1.setMaxParticles(6000);
		e1.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new ImpulseInfluencer(),
			new SpriteInfluencer()
		);
		e1.setShape(hsmesh);
	//	e1.initParticles(ParticleDataTriMesh.class, null);
		
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		e1.setSprite("Textures/leaves.png", "ColorMap", 2, 2);
		mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		mat.getAdditionalRenderState().setAlphaTest(true);
		mat.getAdditionalRenderState().setAlphaFallOff(.5f);
		e1.setMaterial(mat);
		
	//	e1.setSprite("Textures/leaves.png", 200, 200);
		e1.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up_Y_Left);
	//	e1.setParticleEmissionPoint(Emitter.ParticleEmissionPoint.Particle_Edge_Bottom);
		e1.setForceMinMax(0.001f, 0.001f);
		e1.setLifeMinMax(444.2f,444.2f);
		e1.setEmissionsPerSecond(0);
		e1.setParticlesPerEmission(0);
		e1.setEmitterTestMode(false, false);
	//	e1.setUseStaticParticles(true);
		e1.setUseRandomEmissionPoint(true);
		
		e1.initialize(assetManager);
		rootNode.addControl(e1);
		
		e1.getInfluencer(GravityInfluencer.class).setGravity(0,0,0);
		e1.getInfluencer(SizeInfluencer.class).addSize(1.5f);
		e1.getInfluencer(SizeInfluencer.class).addSize(1.5f);
		e1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.White);
		e1.getInfluencer(ColorInfluencer.class).setEnabled(false);
	//	e1.getInfluencer(AngleInfluencer.class).addRotationSpeed(90, 45, 0);
		e1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(.2f, .2f, 0));
		e1.getInfluencer(ImpulseInfluencer.class).setChance(.125f);
		e1.getInfluencer(ImpulseInfluencer.class).setMagnitude(.125f);
		e1.getInfluencer(ImpulseInfluencer.class).setStrength(.125f);
		e1.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		e1.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		
		e1.emitAllParticles();
		e1.setEnabled(false);
	//	e1.setEnabled(false);
	//	e1.getParticleNode().updateModelBound();
		
		rootNode.attachChild(hs);
		
		Node tree = (Node)assetManager.loadModel("Models/Tree/tree.j3o");
		
		rootNode.attachChild(tree);
		
	//	AnimControl ctrl = hs.getControl(AnimControl.class);
	//	AnimChannel chnl = ctrl.createChannel();
	//	chnl.setAnim("run");
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
