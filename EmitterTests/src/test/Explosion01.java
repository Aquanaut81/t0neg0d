package test;

import com.jme3.app.Application;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.filters.TranslucentBucketFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.system.AppSettings;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
import emitter.Emitter;
import emitter.Emitter.BillboardMode;
import emitter.EmitterMesh;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.ImpulseInfluencer;
import emitter.influencers.PhysicsInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Screen;

/**
 * test
 * @author normenhansen
 */
public class Explosion01 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Emitter hotDebris, hotDebrisSmoke, debris, blast1, blast2;
	Timer timer, timer2, timer3, timer4;
	Application app;
	BitmapFont font;
	Geometry floor, ob1, ob2, ob3, ob4;
	
    public static void main(String[] args) {
		AppSettings settings = new AppSettings(true);
		settings.setFullscreen(false);
		settings.setVSync(false);
		settings.setResolution(720,480);
		Explosion01 app = new Explosion01();
	//	app.setSettings(settings);
		app.start();
    }

    @Override
    public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		viewPort.setBackgroundColor(ColorRGBA.Gray);
		
		setupKeys();
		createGround();
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		inputManager.setCursorVisible(true);
		
		Screen screen = new Screen(this);
		guiNode.addControl(screen);
		
        AmbientLight al = new AmbientLight();
		al.setColor(new ColorRGBA(1f, 1f, 1f, 1f));
		rootNode.addLight(al);
		
		DirectionalLight sun = new DirectionalLight();
		sun.setDirection(new Vector3f(-.5f,-.5f,-.5f).normalizeLocal());
		sun.setColor(new ColorRGBA(1f,1f,1f,1f));
		rootNode.addLight(sun);

		Node campfire = (Node)assetManager.loadModel("Models/SphereEmitter.j3o");
		Mesh campfiremesh = ((Geometry)campfire.getChild(0)).getMesh();
		
		FilterPostProcessor fpp = new FilterPostProcessor(assetManager);        
        TranslucentBucketFilter tbf = new TranslucentBucketFilter(true);
        fpp.addFilter(tbf);
        viewPort.addProcessor(fpp);
		
		createSmokingProjectiles();
		createProjectiles();
		createBlast();
		
		
		ButtonAdapter b = new ButtonAdapter(screen, new Vector2f(0,0)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				explode();
			}
		};
		screen.addElement(b);
		
		app = this;
		
    }
	
	private void createGround() {
		Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat.setColor("Color", ColorRGBA.DarkGray);
		mat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		
		Box c = new Box(new Vector3f(35,0.015f,35),new Vector3f(1,1,1));
		floor = new Geometry("Floor Geom");
		floor.setMesh(c);
		floor.setMaterial(mat);
		Node n = new Node("Floor Node");
		n.attachChild(floor);;
		floor.center();
		rootNode.attachChild(n);
		
		n.setLocalTranslation(0,-0.65f,0);
		
		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", new ColorRGBA(.4f,.4f,.4f,1));
		mat2.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		
		Box box2 = new Box(new Vector3f(3f,5f,0.15f),new Vector3f(1,1,1));
		ob1 = new Geometry("Floor Geom");
		ob1.setMesh(box2);
		ob1.setMaterial(mat2);
		Node n2 = new Node("Floor Node");
		n2.attachChild(ob1);;
		ob1.center();
		rootNode.attachChild(n2);
		
		n2.setLocalTranslation(0,1,-3);
		
		Sphere s = new Sphere(12,12,1);
		ob2 = new Geometry("Floor Geom");
		ob2.setMesh(s);
		ob2.setMaterial(mat2);
		Node n3 = new Node("Floor Node");
		n3.attachChild(ob2);;
		ob2.center();
		rootNode.attachChild(n3);
		
		n3.setLocalTranslation(-3,1.75f,0);
		
	}
	
	private void createSmokingProjectiles() {
		hotDebris = new Emitter();
		hotDebris.setName("hotDebris");
		hotDebris.setMaxParticles(10);
		hotDebris.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new SizeInfluencer(),
			new ImpulseInfluencer(),
			new RotationInfluencer(),
			new PhysicsInfluencer(),
			new SpriteInfluencer()
		);
		hotDebris.setShapeSimpleEmitter();
	//	hotDebris.initParticles(ParticleDataTriMesh.class, null);
		hotDebris.setDirectionType(EmitterMesh.DirectionType.RandomNormalAligned);
		hotDebris.setSprite("Textures/Debris.png", 3, 3);
		
		hotDebris.setEmissionsPerSecond(0);
		hotDebris.setParticlesPerEmission(0);
		hotDebris.setForceMinMax(4,6f);
		hotDebris.setLife(4f);
		hotDebris.setBillboardMode(BillboardMode.Velocity);
		
		hotDebris.getInfluencer(GravityInfluencer.class).setGravity(0,4,0);
		
		hotDebris.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Yellow);
		hotDebris.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Orange);
		hotDebris.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.5f,0,0,1));
		hotDebris.getInfluencer(ColorInfluencer.class).setFixedDuration(1/12);
		
		hotDebris.getInfluencer(SizeInfluencer.class).addSize(.05f);
		hotDebris.getInfluencer(SizeInfluencer.class).addSize(.05f);
		
		hotDebris.getInfluencer(ImpulseInfluencer.class).setStrength(.05f);
		hotDebris.getInfluencer(ImpulseInfluencer.class).setMagnitude(.1f);
		
		hotDebris.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(true, true, true);
		hotDebris.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		hotDebris.getInfluencer(RotationInfluencer.class).setUseRandomSpeed(true);
		hotDebris.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(2,4,12));
		
		hotDebris.getInfluencer(PhysicsInfluencer.class).setCollisionReaction(PhysicsInfluencer.CollisionReaction.Bounce);
		hotDebris.getInfluencer(PhysicsInfluencer.class).setRestitution(1f);
		hotDebris.getInfluencer(PhysicsInfluencer.class).addCollidable(floor);
		hotDebris.getInfluencer(PhysicsInfluencer.class).addCollidable(ob1);
		hotDebris.getInfluencer(PhysicsInfluencer.class).addCollidable(ob2);
		hotDebris.getInfluencer(PhysicsInfluencer.class).setRestitution(0.485f);
		
		hotDebris.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		hotDebris.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		hotDebris.initialize(assetManager);
		rootNode.addControl(hotDebris);
		hotDebris.setEnabled(true);
		
		hotDebrisSmoke = new Emitter();
		hotDebrisSmoke.setName("fire");
		hotDebrisSmoke.setMaxParticles(450);
		hotDebrisSmoke.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new ImpulseInfluencer(),
			new RotationInfluencer()
		);
		hotDebrisSmoke.setShape(((Geometry)hotDebris.getParticleNode().getChild(0)).getMesh());
	//	hotDebrisSmoke.initParticles(ParticleDataTriMesh.class, null);
		hotDebrisSmoke.setDirectionType(EmitterMesh.DirectionType.NormalNegate);
		hotDebrisSmoke.setSprite("Textures/Explosion/SmokePuff3.png", 4, 4);
		hotDebrisSmoke.setUseSequentialEmissionFace(true);
		hotDebrisSmoke.setUseSequentialSkipPattern(true);
		
		hotDebrisSmoke.setEmissionsPerSecond(0);
		hotDebrisSmoke.setParticlesPerEmission(1);
		hotDebrisSmoke.setForce(0.1f);
		hotDebrisSmoke.setLifeMinMax(0.75f,1.2f);
		hotDebrisSmoke.setBillboardMode(BillboardMode.Camera);
		
		hotDebrisSmoke.getInfluencer(GravityInfluencer.class).setGravity(0,-.5f,0);
		
		hotDebrisSmoke.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.DarkGray);
		hotDebrisSmoke.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Black);
		
		hotDebrisSmoke.getInfluencer(AlphaInfluencer.class).addAlpha(0,Interpolation.exp10Out);
		hotDebrisSmoke.getInfluencer(AlphaInfluencer.class).addAlpha(1f,Interpolation.exp5In);
		hotDebrisSmoke.getInfluencer(AlphaInfluencer.class).addAlpha(.65f);
		
		hotDebrisSmoke.getInfluencer(SizeInfluencer.class).addSize(.2f);
		hotDebrisSmoke.getInfluencer(SizeInfluencer.class).addSize(.002f);
		hotDebrisSmoke.getInfluencer(SizeInfluencer.class).setUseRandomSize(true);
		
		hotDebrisSmoke.getInfluencer(ImpulseInfluencer.class).setStrength(.05f);
		hotDebrisSmoke.getInfluencer(ImpulseInfluencer.class).setMagnitude(.01f);
		
		hotDebrisSmoke.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		
		hotDebrisSmoke.initialize(assetManager);
		hotDebrisSmoke.getMaterial().setFloat("Softness", 13f);
		hotDebrisSmoke.getParticleNode().setQueueBucket(RenderQueue.Bucket.Translucent);
		
		rootNode.addControl(hotDebrisSmoke);
		hotDebrisSmoke.setEnabled(true);
	}
	
	private void createProjectiles() {
		debris = new Emitter();
		debris.setName("debris");
		debris.setMaxParticles(25);
		debris.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new SizeInfluencer(),
			new ImpulseInfluencer(),
			new RotationInfluencer(),
			new PhysicsInfluencer(),
			new SpriteInfluencer()
		);
		debris.setShapeSimpleEmitter();
	//	debris.initParticles(ParticleDataTriMesh.class, null);
		debris.setDirectionType(EmitterMesh.DirectionType.RandomNormalAligned);
		debris.setSprite("Textures/Debris.png", 3, 3);
		
		debris.setEmissionsPerSecond(0);
		debris.setParticlesPerEmission(0);
		debris.setForceMinMax(4,6f);
		debris.setLifeMinMax(3,4.5f);
		debris.setBillboardMode(BillboardMode.Velocity);
		
		debris.getInfluencer(GravityInfluencer.class).setGravity(0,4,0);
		
		debris.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.White);
		
		debris.getInfluencer(SizeInfluencer.class).addSize(.1f);
		debris.getInfluencer(SizeInfluencer.class).addSize(.1f);
		debris.getInfluencer(SizeInfluencer.class).setUseRandomSize(true);
		
		debris.getInfluencer(ImpulseInfluencer.class).setStrength(.05f);
		debris.getInfluencer(ImpulseInfluencer.class).setMagnitude(.1f);
		
		debris.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(true, true, true);
		debris.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
	//	debris.getInfluencer(RotationInfluencer.class).setUseRandomSpeed(true);
	//	debris.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(2,4,5), Interpolation.exp5Out);
		debris.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0,0,0));
		debris.getInfluencer(RotationInfluencer.class).setEnabled(false);
		
		debris.getInfluencer(PhysicsInfluencer.class).setCollisionReaction(PhysicsInfluencer.CollisionReaction.Bounce);
		debris.getInfluencer(PhysicsInfluencer.class).setRestitution(1f);
		debris.getInfluencer(PhysicsInfluencer.class).addCollidable(floor);
		debris.getInfluencer(PhysicsInfluencer.class).addCollidable(ob1);
		debris.getInfluencer(PhysicsInfluencer.class).addCollidable(ob2);
		debris.getInfluencer(PhysicsInfluencer.class).setRestitution(0.485f);
		
		debris.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		debris.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		
		debris.initialize(assetManager);
		rootNode.addControl(debris);
		debris.setEnabled(true);
	}
	
	private void createBlast() {
		Node n = (Node)assetManager.loadModel("Models/SphereEmitter.j3o");
		Mesh m = ((Geometry)n.getChild(0)).getMesh();
		
		blast1 = new Emitter();
		blast1.setName("blast1");
		blast1.setMaxParticles(200);
		blast1.addInfluencers(
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer()
		);
		blast1.setShape(m);
	//	blast1.initParticles(ParticleDataTriMesh.class, null);
		blast1.setDirectionType(EmitterMesh.DirectionType.Normal);
		blast1.setSprite("Textures/Explosion/exp03a.png", 8, 4);
		
		blast1.setEmissionsPerSecond(0);
		blast1.setParticlesPerEmission(3);
		blast1.setForceMinMax(.63f,.95f);
		blast1.setLifeMinMax(.45f,1.14f);
		blast1.setBillboardMode(BillboardMode.Camera);
		blast1.setUseRandomEmissionPoint(true);
		
		blast1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.25f,0.125f,0,1), Interpolation.exp10Out);
		blast1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.DarkGray);
		
		blast1.getInfluencer(AlphaInfluencer.class).addAlpha(.26f,Interpolation.exp10Out);
		blast1.getInfluencer(AlphaInfluencer.class).addAlpha(.5f,Interpolation.exp5In);
		blast1.getInfluencer(AlphaInfluencer.class).addAlpha(.15f);
		
	//	blast1.getInfluencer(SpriteInfluencer.class).setFrameSequence(12,13,14,13,14,13,14,15,14,13,14,15,16,17);
		
	//	blast1.getInfluencer(SizeInfluencer.class).addSize(.2f);
		blast1.getInfluencer(SizeInfluencer.class).addSize(.34f);
		blast1.getInfluencer(SizeInfluencer.class).addSize(0.05f);
		blast1.getInfluencer(SizeInfluencer.class).addSize(0.05f);
	//	blast1.getInfluencer(SizeInfluencer.class).setUseRandomSize(true);
		
		blast1.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		blast1.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		blast1.getInfluencer(RotationInfluencer.class).setUseRandomSpeed(true);
		blast1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0,0,2));
		blast1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0,0,0));
		
		blast1.initialize(assetManager);
		blast1.setLocalScale(0.25f);
		rootNode.addControl(blast1);
		blast1.setEnabled(true);
		
		blast2 = new Emitter();
		blast2.setName("blast2");
		blast2.setMaxParticles(200);
		blast2.addInfluencers(
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer(),
			new SpriteInfluencer()
		);
		blast2.setShape(m);
	//	blast2.initParticles(ParticleDataTriMesh.class, null);
		blast2.setDirectionType(EmitterMesh.DirectionType.Normal);
		blast2.setSprite("Textures/Explosion/exp03a.png", 8, 4);
		
		blast2.setEmissionsPerSecond(0);
		blast2.setParticlesPerEmission(3);
		blast2.setForceMinMax(.83f,1.35f);
		blast2.setLifeMinMax(.45f,.65f);
		blast2.setBillboardMode(BillboardMode.Normal);
		blast2.setUseRandomEmissionPoint(true);
		blast2.setUseVelocityStretching(true);
		blast2.setVelocityStretchFactor(0.5f);
		
	//	blast2.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.25f,0.125f,0,1), Interpolation.exp10In);
		blast2.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.DarkGray, Interpolation.linear);
		blast2.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Black);
		
	//	blast2.getInfluencer(AlphaInfluencer.class).addAlpha(.6f,Interpolation.exp10Out);
		blast2.getInfluencer(AlphaInfluencer.class).addAlpha(1f,Interpolation.exp10In);
	//	blast2.getInfluencer(AlphaInfluencer.class).addAlpha(.5f);
		
	//	blast2.getInfluencer(SpriteInfluencer.class).setFrameSequence(12,13,14,15,14,13,14,15,14,15);
		
		blast2.getInfluencer(SizeInfluencer.class).addSize(.54f, Interpolation.exp5Out);
		blast2.getInfluencer(SizeInfluencer.class).addSize(.85f);
		blast2.getInfluencer(SizeInfluencer.class).setUseRandomSize(true);
		
		blast2.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, false, true);
		blast2.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		blast2.getInfluencer(RotationInfluencer.class).setUseRandomSpeed(true);
		blast2.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0,0,3));
		blast2.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0,0,0));
		
		blast2.initialize(assetManager);
		blast2.setLocalScale(0.15f);
		rootNode.addControl(blast2);
		blast2.setEnabled(true);
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
	}

	Vector3f scale = new Vector3f();
	float scaleFactor = 0;
	boolean scaleDir = true;
    @Override
    public void simpleUpdate(float tpf) {
		if (scaleDir) {
			scaleFactor += tpf;
			if (scaleFactor >= 8) {
				scaleDir = false;
			}
		} else {
			scaleFactor -= tpf;
			if (scaleFactor <= -8) {
				scaleDir = true;
			}
		}
	//	scale.set(6,6,6);
	//	effectring.getNode().setLocalScale(16);
	//	effectring.getNode().setLocalTranslation(scaleFactor*12,0,0);
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
	
	public void explode() {
		try {
			timer.cancel();
			timer.purge();
		} catch (Exception e) {  }
		/*
		try {
			timer2.cancel();
			timer2.purge();
		} catch (Exception e) {  }
		try {
			timer3.cancel();
			timer3.purge();
		} catch (Exception e) {  }
		*/
		try {
			timer4.cancel();
			timer4.purge();
		} catch (Exception e) {  }
		timer = new Timer("Test", true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				app.enqueue(new Callable() {
					public Object call() throws Exception {
						hotDebrisSmoke.setEmissionsPerSecond(0);
						return null;
					}
					
				});	
			}
			
		},4000);
		/*
		timer2 = new Timer("Test", true);
		timer2.schedule(new TimerTask() {
			@Override
			public void run() {
				app.enqueue(new Callable() {
					public Object call() throws Exception {
						hotDebrisSmoke.setEmissionsPerSecond(300);
						return null;
					}
					
				});	
			}
			
		},500);
		timer3 = new Timer("Test", true);
		timer3.schedule(new TimerTask() {
			@Override
			public void run() {
				app.enqueue(new Callable() {
					public Object call() throws Exception {
						hotDebrisSmoke.setEmissionsPerSecond(200);
						return null;
					}
					
				});	
			}
			
		},1000);
		*/
		timer4 = new Timer("Test", true);
		timer4.schedule(new TimerTask() {
			@Override
			public void run() {
				app.enqueue(new Callable() {
					public Object call() throws Exception {
						hotDebrisSmoke.setEmissionsPerSecond(100);
						return null;
					}
					
				});	
			}
			
		},3500);
		debris.reset();
		debris.emitAllParticles();
		hotDebris.reset();
		hotDebris.emitAllParticles();
		hotDebrisSmoke.reset();
		hotDebrisSmoke.setEmissionsPerSecond(400);
		
		Timer blastTimer = new Timer("Test", true);
		blastTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				app.enqueue(new Callable() {
					public Object call() throws Exception {
						blast1.setEmissionsPerSecond(0);
						return null;
					}
					
				});	
			}
			
		},500);
		blast1.reset();
		blast1.setEmissionsPerSecond(120);
		
		
		Timer blastTimer2 = new Timer("Test", true);
		blastTimer2.schedule(new TimerTask() {
			@Override
			public void run() {
				app.enqueue(new Callable() {
					public Object call() throws Exception {
						blast2.setEmissionsPerSecond(0);
						return null;
					}
					
				});	
			}
			
		},250);
		blast2.reset();
		blast2.setEmissionsPerSecond(120);
		
	}
}
