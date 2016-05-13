package test.destination;

import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import emitter.Emitter;
import emitter.EmitterMesh;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.DestinationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;
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
public class DestinationTest01 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Screen screen;
	ButtonAdapter b1, b2;
	Node emitterModelNode;
	Mesh emitterModelMesh;
	Emitter eDest1;
	
	public static void main(String[] args) {
		DestinationTest01 app = new DestinationTest01();
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
		
		cam.setLocation(new Vector3f(0,0,87));
		
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
				if (eDest1.getSpatial() == null) {
					eDest1.reset();
					rootNode.addControl(eDest1);
					eDest1.setEnabled(true);
					setText("Remove");
				} else {
					rootNode.removeControl(eDest1);
					eDest1.setEnabled(false);
					b1.setText("Add");
				}
			}
		};
		b1.setText("Add");
		screen.addElement(b1);
		
		b2 = new ButtonAdapter(screen, new Vector2f(0,b1.getHeight())) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (!eDest1.isEnabled()) {
					eDest1.setEnabled(true);
					setText("Pause");
				} else {
					eDest1.setEnabled(false);
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
		eDest1 = new Emitter();
		eDest1.setName("eDest1");
		eDest1.setMaxParticles(200);
		eDest1.addInfluencers(
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new SpriteInfluencer(),
			new DestinationInfluencer()
		);
		eDest1.setShapeSimpleEmitter();
		eDest1.setDirectionType(EmitterMesh.DirectionType.RandomNormalAligned);
		eDest1.setEmitterTestMode(false, false);
		eDest1.setSprite("Textures/Core16.png",4,3);
		eDest1.setEmissionsPerSecond(200);
		eDest1.setParticlesPerEmission(1);
		eDest1.setForce(10f);
		eDest1.setLife(2f);
		eDest1.setBillboardMode(Emitter.BillboardMode.Camera);
		
		eDest1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Red, Interpolation.linear);
		eDest1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Orange, Interpolation.linear);
		eDest1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Yellow, Interpolation.linear);
		eDest1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Orange, Interpolation.linear);
		eDest1.getInfluencer(ColorInfluencer.class).addColor(ColorRGBA.Red, Interpolation.linear);
		
		eDest1.getInfluencer(AlphaInfluencer.class).addAlpha(1f, Interpolation.exp10In);
		
		eDest1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(1,1,1), Interpolation.exp10Out);
		
		eDest1.getInfluencer(SpriteInfluencer.class).setAnimate(true);
		eDest1.getInfluencer(SpriteInfluencer.class).setFixedDuration(1/96);
		
		eDest1.initialize(assetManager);
		
		setConfig1();
	}
	
	public void setConfig1() {
		eDest1.setEnabled(false);
		eDest1.killAllParticles();
		
		eDest1.setDirectionType(EmitterMesh.DirectionType.RandomNormalAligned);
		eDest1.setForce(10f);
		eDest1.setLife(2f);
		eDest1.getInfluencer(DestinationInfluencer.class).removeAll();
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(-5,-5,0), .15f, Interpolation.exp5Out);
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,-10,0), .25f, Interpolation.linear);
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(5,-5,0), .45f, Interpolation.linear);
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(10,-10,0), .65f, Interpolation.exp5Out);
		
		eDest1.setEnabled(true);
	}
	public void setConfig2() {
		eDest1.setEnabled(false);
		eDest1.killAllParticles();
		
		eDest1.setDirectionType(EmitterMesh.DirectionType.RandomNormalAligned);
		eDest1.setForce(10f);
		eDest1.setLife(2f);
		eDest1.getInfluencer(DestinationInfluencer.class).removeAll();
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,8,0), .175f, Interpolation.linear);
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,20,0), .65f, Interpolation.exp10Out);
		
		eDest1.setEnabled(true);
	}
	public void setConfig3() {
		eDest1.setEnabled(false);
		eDest1.killAllParticles();
		
		eDest1.setDirectionType(EmitterMesh.DirectionType.RandomNormalAligned);
		eDest1.setForce(10);
		eDest1.setLife(2f);
		eDest1.getInfluencer(DestinationInfluencer.class).removeAll();
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,-20,0), .05f, Interpolation.linear);
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,-5f,0), .1f, Interpolation.linear);
		
		eDest1.setEnabled(true);
	}
	public void setConfig4() {
		eDest1.setEnabled(false);
		eDest1.killAllParticles();
		
		eDest1.setDirectionType(EmitterMesh.DirectionType.RandomNormalNegate);
		eDest1.setForce(80f);
		eDest1.setLifeMinMax(2f,2f);
		eDest1.getInfluencer(DestinationInfluencer.class).removeAll();
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,10,0), .25f, Interpolation.linear);
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,0,0), .25f, Interpolation.linear);
		eDest1.getInfluencer(DestinationInfluencer.class).addDestination(new Vector3f(0,0,0), .25f, Interpolation.linear);
		
		eDest1.setEnabled(true);
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
