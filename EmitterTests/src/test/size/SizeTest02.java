package test.size;

import test.color.*;
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
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import emitter.Emitter;
import emitter.EmitterMesh;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
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
public class SizeTest02 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Screen screen;
	ButtonAdapter b1, b2;
	Node emitterModelNode;
	Mesh emitterModelMesh;
	Emitter eSize;
	
	public static void main(String[] args) {
		SizeTest02 app = new SizeTest02();
		app.start();
	}

	@Override
	public void simpleInitApp() {
		vrAppState = new VideoRecorderAppState();
		vrAppState.setQuality(0.35f);
		
		this.setPauseOnLostFocus(true);
		
		flyCam.setDragToRotate(true);
		flyCam.setMoveSpeed(15f);
		inputManager.setCursorVisible(true);
		
		cam.setLocation(new Vector3f(0.0f, 9.230603f, 10.0f));
		cam.setRotation(new Quaternion(0.0014983257f, 0.96028787f, -0.27895933f, 0.0051578884f));

		
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
				if (eSize.getSpatial() == null) {
					eSize.reset();
					rootNode.addControl(eSize);
					eSize.setEnabled(true);
					setText("Remove");
				} else {
					rootNode.removeControl(eSize);
					eSize.setEnabled(false);
					b1.setText("Add");
				}
			}
		};
		b1.setText("Add");
		screen.addElement(b1);
		
		b2 = new ButtonAdapter(screen, new Vector2f(0,b1.getHeight())) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (!eSize.isEnabled()) {
					eSize.setEnabled(true);
					setText("Pause");
				} else {
					eSize.setEnabled(false);
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
		
		Panel p = new Panel(screen, new Vector2f(0,b1.getHeight()*2));
		p.setAsContainerOnly();
		
		rbg.setDisplayElement(p);
		screen.addElement(p);
		
		rbg.setSelected(0);
	}
	
	private void addTestEmitters() {
		eSize = new Emitter();
		eSize.setName("eSize");
		eSize.setMaxParticles(48);
		eSize.addInfluencers(
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer()
		);
		eSize.setShapeSimpleEmitter();
		
		eSize.setEmitterTestMode(false, false);
		eSize.setSprite("Textures/Rune_Circle.png");
		eSize.setEmissionsPerSecond(4);
		eSize.setParticlesPerEmission(1);
		eSize.setForceMinMax(1f,1f);
		eSize.setLifeMinMax(5.25f,5.25f);
		eSize.setBillboardMode(Emitter.BillboardMode.UNIT_Y);
		eSize.setDirectionType(EmitterMesh.DirectionType.Normal);
		
		eSize.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0f,0f,.25f));
		eSize.getInfluencer(RotationInfluencer.class).setUseRandomDirection(false);
		eSize.getInfluencer(RotationInfluencer.class).setUseRandomSpeed(false);
		
		eSize.getInfluencer(AlphaInfluencer.class).addAlpha(1f);
		
		eSize.getInfluencer(ColorInfluencer.class).removeAll();
		eSize.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.2f,0.2f,1,1f));
		eSize.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.2f,0.2f,1,1f));
		eSize.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.2f,0.2f,1,1f));
		eSize.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.2f,0.2f,1,1f));
		eSize.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(0.75f,0.75f,1,1f));
		eSize.getInfluencer(ColorInfluencer.class).setFixedDuration(.225f);
		
		eSize.initialize(assetManager);
		
		setConfig1();
	}
	
	public void setConfig1() {
		eSize.setEnabled(false);
		eSize.killAllParticles();
		
		eSize.setEmissionsPerSecond(1);
		
		eSize.getInfluencer(SizeInfluencer.class).removeAll();
		eSize.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(3f,3f,3f));
		eSize.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(0f,0f,0f));
		eSize.getInfluencer(SizeInfluencer.class).setUseRandomSize(false);
		eSize.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		eSize.setEnabled(true);
	}
	public void setConfig2() {
		eSize.setEnabled(false);
		eSize.killAllParticles();
		
		eSize.setEmissionsPerSecond(6);
		
		eSize.getInfluencer(SizeInfluencer.class).removeAll();
		eSize.getInfluencer(SizeInfluencer.class).addSize(0);
		eSize.getInfluencer(SizeInfluencer.class).addSize(2, Interpolation.exp5In);
		eSize.getInfluencer(SizeInfluencer.class).addSize(1.25f, Interpolation.exp5Out);
		eSize.getInfluencer(SizeInfluencer.class).addSize(0.5f, Interpolation.exp5In);
		eSize.getInfluencer(SizeInfluencer.class).addSize(1.25f, Interpolation.exp5Out);
		eSize.getInfluencer(SizeInfluencer.class).addSize(2);
		eSize.getInfluencer(SizeInfluencer.class).addSize(0);
	//	eSize.getInfluencer(SizeInfluencer.class).setUseRandomSize(true);
	//	eSize.getInfluencer(SizeInfluencer.class).setRandomSizeTolerance(0.25f);
		eSize.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		eSize.setEnabled(true);
	}
	public void setConfig3() {
		eSize.setEnabled(false);
		eSize.killAllParticles();
		
		eSize.setEmissionsPerSecond(1);
		
		eSize.getInfluencer(SizeInfluencer.class).removeAll();
		eSize.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(4.5f,3,0), Interpolation.bounceOut);
		eSize.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(4.5f,3,0), Interpolation.bounceOut);
		eSize.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(3,3,0), Interpolation.bounceOut);
		eSize.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(3,3,0), Interpolation.bounceOut);
		eSize.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(3,4.5f,0), Interpolation.bounceOut);
		eSize.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(3,4.5f,0), Interpolation.bounceOut);
		eSize.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(3,3,0), Interpolation.bounceOut);
		eSize.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(3,3,0), Interpolation.bounceOut);
		eSize.getInfluencer(SizeInfluencer.class).setEnabled(true);
		
		eSize.setEnabled(true);
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
