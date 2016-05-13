package test;

import com.jme3.animation.LoopMode;
import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.font.BitmapFont;
import com.jme3.input.controls.ActionListener;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import java.util.Timer;
import emitter.Emitter;
import emitter.Emitter.BillboardMode;
import emitter.EmitterMesh.DirectionType;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;
import java.io.File;
import java.io.IOException;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 * test
 * @author normenhansen
 */
public class SaveTest extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Emitter hotDebris, hotDebrisSmoke, debris, blast1, blast2;
	Timer timer, timer2, timer3, timer4;
	Application app;
	BitmapFont font;
	Geometry floor, ob1, ob2, ob3, ob4;
	Emitter e1;
	
	Screen screen;
	TextField emitterName, emitterPCount;
	ButtonAdapter createEmitter;
	
    public static void main(String[] args) {
        SaveTest app = new SaveTest();
        app.start();
    }

    @Override
    public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		viewPort.setBackgroundColor(ColorRGBA.Black);
		
	//	setupKeys();
	//	createGround();
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		inputManager.setCursorVisible(true);
		
		screen = new Screen(this, "tonegod/gui/style/atlasdef/style_map.gui.xml");
		screen.setUseTextureAtlas(true, "tonegod/gui/style/atlasdef/atlas.png");
		guiNode.addControl(screen);
		
        AmbientLight al = new AmbientLight();
		al.setColor(new ColorRGBA(1f, 1f, 1f, 1f));
		rootNode.addLight(al);
		
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
		sun.setColor(new ColorRGBA(1f,1f,1f,1f));
		rootNode.addLight(sun);

		
		Node emitterNode = (Node)assetManager.loadModel("Models/Character-HMN-FEM.j3o");
	//	Mesh emitterMesh = ((Geometry)emitterNode.getChild(0)).getMesh();
		
		Node particleNode = (Node)assetManager.loadModel("Models/Character-HMN-FEM.j3o");
	//	Mesh particleMesh = ((Geometry)particleNode.getChild(0)).getMesh();
		
		
		
	//	form1.setSelectedTabIndex(emitterName);
	//	emitterName.setTabFocus();
		app = this;
		
		e1 = new Emitter();
		e1.setName("e1");
		e1.addInfluencers(
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new SpriteInfluencer(),
			new RotationInfluencer(),
			new GravityInfluencer()
		);
		e1.setMaxParticles(15);
		
		e1.setShapeSimpleEmitter();
	//	
		Sphere e1ES = new Sphere(16, 16, 0.5f);
		e1.setShape(e1ES);
	//	e1.setShape(emitterNode, false);
	//	e1.setParticleType(ParticleDataTemplateMesh.class, particleNode);
		
		e1.setDirectionType(DirectionType.Normal);
		e1.setEmissionsPerSecond(15);
		e1.setParticlesPerEmission(1);
		e1.setBillboardMode(BillboardMode.Camera);
		e1.setForceMinMax(2.25f,5.0f);
		e1.setLifeMinMax(0.999f,0.999f);
	//	e1.setParticlesFollowEmitter(true);
		e1.setUseSequentialEmissionFace(true);
		e1.setUseSequentialSkipPattern(true);
		
		e1.setSprite("Textures/Character-HMN-FEM.png", 1, 1);
		
		// Lighting test
		Material mat = new Material(assetManager, "Common/MatDefs/Light/Lighting.j3md");
		mat.setBoolean("UseVertexColor", true);
		mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		/*
		// Unshaded test
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setBoolean("VertexColor", true);
		*/
		mat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
		mat.getAdditionalRenderState().setAlphaTest(true);
		mat.getAdditionalRenderState().setAlphaFallOff(.15f);
		e1.setMaterial(mat, "DiffuseMap", true);
		
		e1.setEmitterTestMode(true, false);
	//	e1.setUseSequentialEmissionFace(true);
	//	e1.setUseSequentialSkipPattern(true);
		
		// Color Influencer
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1.0f,1.0f,1.0f,1.0f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1.0f,1.0f,1.0f,0.0f));

		// Alpha Influencer
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(1.0f, Interpolation.exp5In);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(0.0f);
		
		// Size Influencer
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(0.5f,0.5f,0.5f));
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(0.1f,0.1f,0.1f));
		
		// Sprite Influencer
		// Rotation Influencer
		e1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0.0f,0.0f,0.0f));
		e1.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false,false,false);
		e1.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		e1.getInfluencer(RotationInfluencer.class).setUseRandomSpeed(true);
		
		// RadialVelocity Influencer
	//	e1.getInfluencer(RadialVelocityInfluencer.class).setTangentForce(4.0f);
	//	e1.getInfluencer(RadialVelocityInfluencer.class).setRadialPull(3.0f);
	//	e1.getInfluencer(RadialVelocityInfluencer.class).setRadialPullAlignment(RadialPullAlignment.Emitter_Center);
	//	e1.getInfluencer(RadialVelocityInfluencer.class).setUseRandomDirection(true);

		// Gravity Influencer
		e1.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0.0f,4.0f,0.0f));

		// Set transforms and add to scene
		e1.setLocalTranslation(0.0f, 0.0f, 0.0f);
		e1.setLocalScale(1.0f, 1.0f, 1.0f);
		e1.setLocalRotation(new Quaternion().fromAngles(0.0f,0.0f,0.0f));
		
		e1.initialize(assetManager);
		rootNode.addControl(e1);
		e1.setEnabled(true);
		/*
		particleNode.setLocalScale(0.001f);
		rootNode.attachChild(particleNode);
		AnimControl animControl = particleNode.getControl(AnimControl.class);
		AnimChannel channel = animControl.createChannel();
		channel.setAnim("run", 1);
		channel.setSpeed(1);
		channel.setLoopMode(LoopMode.Loop);
	//	Emitter e2 = e1.clone();
	//	e2.setLocalTranslation(2,0,0);
		*/
		
		e1.setEmitterAnimation("run", 1, 1, LoopMode.Loop);
		e1.setParticleAnimation("kick", 1, 1, LoopMode.Loop);
	//	rootNode.addControl(e2);
		
		String userHome = System.getProperty("user.home");
		BinaryExporter exporter = BinaryExporter.getInstance();
		File file = new File(userHome+"/Models/"+"FX1.j3o");
		System.out.println(userHome+"/Models/"+"FX1.j3o");
		try {
			exporter.save(e1, file);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private void setupKeys() {
		
	}
	
	@Override
    public void simpleUpdate(float tpf) {
	//	e1.setLocalTranslation(e1.getLocalTranslation().add(tpf*15, 0, 0));
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

	public void onAction(String name, boolean isPressed, float tpf) {
		
	}
}
