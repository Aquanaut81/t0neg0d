package test.gravity;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import emitter.Emitter;
import emitter.EmitterMesh;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.particle.ParticleDataImpostorMesh;
import emitter.particle.ParticleDataTriMesh;
import tonegod.gui.controls.buttons.Button;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.RadioButtonGroup;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.core.Screen;

/**
 * test
 * @author t0neg0d
 */
public class GravityTest01 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Screen screen;
	ButtonAdapter b1, b2;
	Node emitterModelNode;
	Mesh emitterModelMesh;
	Emitter eGravity1;
	
	public static void main(String[] args) {
		GravityTest01 app = new GravityTest01();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		this.setPauseOnLostFocus(true);
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(25f);
		inputManager.setCursorVisible(true);
		
		cam.setLocation(new Vector3f(-0.070769615f, 8.845519f, 10.591199f));
		cam.setRotation(new Quaternion().set(-0.0011188702f, 0.9448984f, -0.32734585f, -0.0032296795f));
		
		setupKeys();
		addTestEmitters();
		createGUI();
	}
	
	private void createGUI() {
		screen = new Screen(this);
		guiNode.addControl(screen);
		
		b1 = new ButtonAdapter(screen, new Vector2f(0,0)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (eGravity1.getSpatial() == null) {
					eGravity1.reset();
					rootNode.addControl(eGravity1);
					eGravity1.setEnabled(true);
					setText("Remove");
				} else {
					rootNode.removeControl(eGravity1);
					eGravity1.setEnabled(false);
					b1.setText("Add");
				}
			}
		};
		b1.setText("Add");
		screen.addElement(b1);
		
		b2 = new ButtonAdapter(screen, new Vector2f(0,b1.getHeight())) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (!eGravity1.isEnabled()) {
					eGravity1.setEnabled(true);
					setText("Pause");
				} else {
					eGravity1.setEnabled(false);
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
						setConfig1();
						break;
					case 1:
						setConfig2();
						break;
					case 2:
						setConfig3();
						break;
					case 3:
						setConfig4();
						break;
				}
			}
		};
		
		ButtonAdapter b3 = new ButtonAdapter(screen, new Vector2f(0,0));
		b3.setText("Config 1");
		rbg.addButton(b3);
		
		ButtonAdapter b4 = new ButtonAdapter(screen, new Vector2f(0,b1.getHeight()));
		b4.setText("Config 2");
		rbg.addButton(b4);
		
		ButtonAdapter b5 = new ButtonAdapter(screen, new Vector2f(0,b1.getHeight()*2));
		b5.setText("Config 3");
		rbg.addButton(b5);
		
		ButtonAdapter b6 = new ButtonAdapter(screen, new Vector2f(0,b1.getHeight()*3));
		b6.setText("Config 4");
		rbg.addButton(b6);
		
		Panel p = new Panel(screen, new Vector2f(0,b1.getHeight()*2));
		p.setAsContainerOnly();
		
		rbg.setDisplayElement(p);
		screen.addElement(p);
		
		rbg.setSelected(0);
	}
	
	private void addTestEmitters() {
		eGravity1 = new Emitter();
		eGravity1.setName("eGravity1");
		eGravity1.setMaxParticles(1000);
		eGravity1.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer()
		);
		eGravity1.setShapeSimpleEmitter();
		eGravity1.setParticleType(ParticleDataImpostorMesh.class);
		eGravity1.setSprite("Textures/glow.png");
		eGravity1.setEmissionsPerSecond(100);
		eGravity1.setParticlesPerEmission(2);
		eGravity1.setForce(5.2f);
		eGravity1.setLife(.5f);
		eGravity1.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up);
		eGravity1.setEmitterTestMode(false, false);
		eGravity1.setUseVelocityStretching(true);
		eGravity1.setVelocityStretchFactor(1.45f);
		eGravity1.setUseRandomEmissionPoint(true);
		
		eGravity1.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0,0,0));
		eGravity1.getInfluencer(GravityInfluencer.class).setAlignment(GravityInfluencer.GravityAlignment.Reverse_Velocity);
		eGravity1.getInfluencer(GravityInfluencer.class).setMagnitude(6.5f);
		
		eGravity1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,0.75f,0f,0.45f));
		eGravity1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,9f,0.45f));
		
		eGravity1.getInfluencer(SizeInfluencer.class).addSize(.05f);
		
		eGravity1.setLocalTranslation(0,0,0);
		
		eGravity1.initialize(assetManager);
		
		setConfig1();
	}
	
	public void setConfig1() {
		eGravity1.setEnabled(false);
		eGravity1.killAllParticles();
		
		eGravity1.setShapeSimpleEmitter();
		eGravity1.setDirectionType(EmitterMesh.DirectionType.RandomNormalAligned);
		eGravity1.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up_Y_Left);
		eGravity1.setForce(5.2f);
		eGravity1.setEmissionsPerSecond(300);
		eGravity1.setParticlesPerEmission(2);
		eGravity1.setUseRandomEmissionPoint(false);
		
		eGravity1.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0,12,0));
		eGravity1.getInfluencer(GravityInfluencer.class).setAlignment(GravityInfluencer.GravityAlignment.World);
		
		eGravity1.setEnabled(true);
	}
	public void setConfig2() {
		eGravity1.setEnabled(false);
		eGravity1.killAllParticles();
		
		eGravity1.setShapeSimpleEmitter();
		eGravity1.setDirectionType(EmitterMesh.DirectionType.Random);
		eGravity1.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up_Y_Left);
		eGravity1.setForce(5.2f);
		eGravity1.setEmissionsPerSecond(300);
		eGravity1.setParticlesPerEmission(2);
		eGravity1.setUseRandomEmissionPoint(false);
		
		eGravity1.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0,-48,0));
		eGravity1.getInfluencer(GravityInfluencer.class).setAlignment(GravityInfluencer.GravityAlignment.World);
		
		eGravity1.setEnabled(true);
	}
	public void setConfig3() {
		eGravity1.setEnabled(false);
		eGravity1.killAllParticles();
		
		eGravity1.setShapeSimpleEmitter();
		eGravity1.setDirectionType(EmitterMesh.DirectionType.Random);
		eGravity1.setBillboardMode(Emitter.BillboardMode.Velocity_Z_Up);
		eGravity1.setForce(16f);
		eGravity1.setEmissionsPerSecond(300);
		eGravity1.setParticlesPerEmission(2);
		eGravity1.setUseRandomEmissionPoint(false);
		
		eGravity1.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0,0,0));
		eGravity1.getInfluencer(GravityInfluencer.class).setAlignment(GravityInfluencer.GravityAlignment.Reverse_Velocity);
		eGravity1.getInfluencer(GravityInfluencer.class).setMagnitude(5f);
		
		eGravity1.setEnabled(true);
	}
	public void setConfig4() {
		eGravity1.setEnabled(false);
		eGravity1.killAllParticles();
		
		Node node = (Node)assetManager.loadModel("Models/SphereEmitter.j3o");
		Mesh mesh = ((Geometry)node.getChild(0)).getMesh();
		
		eGravity1.setShape(mesh);
		eGravity1.setDirectionType(EmitterMesh.DirectionType.Normal);
		eGravity1.setForceMinMax(4.2f, 5f);
		eGravity1.setEmissionsPerSecond(1000);
		eGravity1.setParticlesPerEmission(2);
		eGravity1.setUseRandomEmissionPoint(true);
		
		eGravity1.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0,-4,0));
		eGravity1.getInfluencer(GravityInfluencer.class).setAlignment(GravityInfluencer.GravityAlignment.Emission_Point);
		eGravity1.getInfluencer(GravityInfluencer.class).setMagnitude(122.5f);
		
		eGravity1.setLocalScale(2);
		
		eGravity1.setEnabled(true);
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
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
