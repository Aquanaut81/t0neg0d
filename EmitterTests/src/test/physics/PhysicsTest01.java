package test.physics;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
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
import emitter.influencers.GravityInfluencer;
import emitter.influencers.ImpulseInfluencer;
import emitter.influencers.PhysicsInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;
import emitter.particle.ParticleDataTriMesh;
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.RadioButtonGroup;
import tonegod.gui.controls.lists.Slider;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Screen;

/**
 * test
 * @author t0neg0d
 */
public class PhysicsTest01 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Screen screen;
	ButtonAdapter b, b2;
	Node emitterModelNode;
	Mesh emitterModelMesh;
	Emitter eVisual, ePhysics;
	Quaternion staticRot = new Quaternion(), updateRot = new Quaternion();
	float rot = 0;
	Geometry floor, sphere;
	
	public static void main(String[] args) {
		PhysicsTest01 app = new PhysicsTest01();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		this.setPauseOnLostFocus(true);
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		cam.setLocation(new Vector3f(13.94692f, 15.2493305f, 23.30932f));
		cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);
		
		inputManager.setCursorVisible(true);
		
		staticRot.fromAngleAxis(30*FastMath.DEG_TO_RAD, Vector3f.UNIT_X);
		
		setupKeys();
		createGround();
		addTestEmitters();
		createGUI();
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
		
		Material mat2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
		mat2.setColor("Color", ColorRGBA.Gray);
		mat2.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		
		Sphere s = new Sphere(12,12,2);
		sphere = new Geometry();
		sphere.setMesh(s);
		sphere.setMaterial(mat2);
		Node n2 = new Node("Sphere Node");
		n2.attachChild(sphere);;
		sphere.center();
		rootNode.attachChild(n2);
		n2.setLocalTranslation(0,0,8);
	}
	
	private void createGUI() {
		screen = new Screen(this);
		guiNode.addControl(screen);
		
		b = new ButtonAdapter(screen, new Vector2f(0,0)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (ePhysics.getSpatial() == null) {
					remove = false;
					rootNode.addControl(ePhysics);
					ePhysics.setEmissionsPerSecond(100);
					ePhysics.reset();
					ePhysics.setEnabled(true);
					setText("Remove");
				} else {
					setText("Waiting");
					this.setIsEnabled(false);
					ePhysics.setEmissionsPerSecond(0);
					remove = true;
				}
			}
		};
		b.setText("Add");
		screen.addElement(b);
		
		b2 = new ButtonAdapter(screen, new Vector2f(0,b.getHeight())) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (!ePhysics.isEnabled()) {
					ePhysics.setEnabled(true);
					setText("Pause");
				} else {
					ePhysics.setEnabled(false);
					setText("Resume");
				}
			}
		};
		b2.setText("Pause");
		screen.addElement(b2);
		
		RadioButtonGroup rbg = new RadioButtonGroup(screen) {
			@Override
			public void onSelect(int index, Button value) {
				switch (index) {
					case 0:
						ePhysics.getInfluencer(PhysicsInfluencer.class).setCollisionReaction(PhysicsInfluencer.CollisionReaction.Bounce);
						break;
					case 1:
						ePhysics.getInfluencer(PhysicsInfluencer.class).setCollisionReaction(PhysicsInfluencer.CollisionReaction.Stick);
						break;
					case 2:
						ePhysics.getInfluencer(PhysicsInfluencer.class).setCollisionReaction(PhysicsInfluencer.CollisionReaction.Destroy);
						break;
				}
			}
		};
		
		ButtonAdapter b3 = new ButtonAdapter(screen, new Vector2f(0,0));
		b3.setText("Bounce");
		rbg.addButton(b3);
		
		ButtonAdapter b4 = new ButtonAdapter(screen, new Vector2f(0,b.getHeight()));
		b4.setText("Stick");
		rbg.addButton(b4);
		
		ButtonAdapter b5 = new ButtonAdapter(screen, new Vector2f(0,b.getHeight()*2));
		b5.setText("Destroy");
		rbg.addButton(b5);
		
		Panel p = new Panel(screen, new Vector2f(0,b.getHeight()*2));
		p.setAsContainerOnly();
		
		rbg.setDisplayElement(p);
		screen.addElement(p);
		
		rbg.setSelected(0);
		
		Slider sl = new Slider(screen, new Vector2f(0,b.getHeight()*5), new Vector2f(b.getWidth(),b.getHeight()), Slider.Orientation.HORIZONTAL, true) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				ePhysics.getInfluencer(PhysicsInfluencer.class).setRestitution(Float.valueOf(value.toString()));
			}
		};
		sl.setStepFloatRange(0f, 1f, 0.01f);
		sl.setSelectedIndexWithCallback(75);
		
		screen.addElement(sl);
	}
	
	private void addTestEmitters() {
		emitterModelNode = (Node)assetManager.loadModel("Models/Circle.j3o");
		emitterModelMesh = ((Geometry)emitterModelNode.getChild(0)).getMesh();
		
		eVisual = new Emitter();
		eVisual.setName("eVisual");
		eVisual.setMaxParticles(100);
		eVisual.addInfluencers(
			new ColorInfluencer(),
			new SizeInfluencer(),
			new SpriteInfluencer(),
			new RotationInfluencer()
		);
		eVisual.setShape(emitterModelMesh);
		eVisual.setDirectionType(EmitterMesh.DirectionType.Random);
		eVisual.setSprite("Textures/flash.png", 2, 2);
		eVisual.setEmissionsPerSecond(100);
		eVisual.setParticlesPerEmission(1);
		eVisual.setLife(1);
		eVisual.setForce(0.01f);
		eVisual.setParticlesFollowEmitter(true);
		
		eVisual.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.65f,0.65f,1f,0.65f));
		eVisual.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,1f,0.015f));
		
		eVisual.getInfluencer(SizeInfluencer.class).addSize(0.15f);
		
		eVisual.getInfluencer(SpriteInfluencer.class).setAnimate(false);
		eVisual.getInfluencer(SpriteInfluencer.class).setUseRandomStartImage(true);
		
		eVisual.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(true, true, true);
		eVisual.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(10, 10, 10));
		
		eVisual.setLocalTranslation(0,8,0);
		
		eVisual.initialize(assetManager);
		rootNode.addControl(eVisual);
		eVisual.setEnabled(true);
		
		ePhysics = new Emitter();
		ePhysics.setName("ePhysics");
		ePhysics.setMaxParticles(800);
		ePhysics.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new PhysicsInfluencer(),
			new ImpulseInfluencer()
		);
		ePhysics.setShape(emitterModelMesh);
		ePhysics.setBillboardMode(Emitter.BillboardMode.Camera);
		ePhysics.setSprite("Textures/glow.png");
		ePhysics.setForceMinMax(8.0f, 12.0f);
		ePhysics.setLifeMinMax(4.0f,6.0f);
		ePhysics.setEmissionsPerSecond(100);
		ePhysics.setParticlesPerEmission(2);
		ePhysics.setEmitterTestMode(false, false);
		ePhysics.setUseRandomEmissionPoint(true);
		ePhysics.setParticlesFollowEmitter(false);
		ePhysics.setUseVelocityStretching(false);
		ePhysics.setDirectionType(EmitterMesh.DirectionType.Normal);
		
		ePhysics.getInfluencer(GravityInfluencer.class).setGravity(0,16,0);
		
		ePhysics.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(0.1f,0.1f,0.1f));
		ePhysics.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(0.1f,0.1f,0.1f));
		ePhysics.getInfluencer(SizeInfluencer.class).setEnabled(false);
		
		ePhysics.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Red, Interpolation.linear);
		
		ePhysics.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp10Out);
		ePhysics.getInfluencer(AlphaInfluencer.class).addAlpha(1f);
		ePhysics.getInfluencer(AlphaInfluencer.class).addAlpha(0f, Interpolation.exp5Out);
		
		ePhysics.getInfluencer(ImpulseInfluencer.class).setMagnitude(0.1f);
		
		ePhysics.getInfluencer(PhysicsInfluencer.class).setCollisionReaction(PhysicsInfluencer.CollisionReaction.Bounce);
		ePhysics.getInfluencer(PhysicsInfluencer.class).setRestitution(1f);
		ePhysics.getInfluencer(PhysicsInfluencer.class).addCollidable(floor);
		ePhysics.getInfluencer(PhysicsInfluencer.class).addCollidable(sphere);
		
		ePhysics.setLocalTranslation(0,8,0);
		
		ePhysics.initialize(assetManager);
		
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
	}

	boolean remove = false;
	@Override
	public void simpleUpdate(float tpf) {
		rot += tpf/2f;
		updateRot.fromAngleAxis(rot, Vector3f.UNIT_Y);
		updateRot.multLocal(staticRot);
		eVisual.setLocalRotation(updateRot);
		ePhysics.setLocalRotation(updateRot);
		if (remove) {
			if (ePhysics.getActiveParticleCount() == 0) {
				ePhysics.setEnabled(false);
				rootNode.removeControl(ePhysics);
				b.setIsEnabled(true);
				b.setText("Add");
			}
		}
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
