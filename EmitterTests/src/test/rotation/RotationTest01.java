package test.rotation;

import com.jme3.animation.AnimChannel;
import com.jme3.app.SimpleApplication;
import com.jme3.app.state.VideoRecorderAppState;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
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
import emitter.influencers.SpriteInfluencer;
import emitter.particle.ParticleDataTriMesh;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.core.Screen;

/**
 * test
 * @author normenhansen
 */
public class RotationTest01 extends SimpleApplication implements ActionListener {
	VideoRecorderAppState vrAppState;
	Sphere s;
	Emitter effectring;
	Emitter e1, e2, e3, e4;
	Node ring;
	AnimChannel c;
	
	public static void main(String[] args) {
		RotationTest01 app = new RotationTest01();
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
		
		Screen screen = new Screen(this);
		guiNode.addControl(screen);
		
		e1 = new Emitter();
		e1.setName("e1");
		e1.setMaxParticles(200);
		e1.addInfluencers(
			new GravityInfluencer(),
			new ColorInfluencer(),
			new AlphaInfluencer(),
			new SizeInfluencer(),
			new RotationInfluencer()
		);
		e1.setShapeSimpleEmitter();
		e1.setEmitterTestMode(false, false);
		e1.setSprite("Textures/Rune_Circle.png");
		e1.setEmissionsPerSecond(40);
		e1.setParticlesPerEmission(1);
		e1.setForceMinMax(2f, 4f);
		e1.setLifeMinMax(1f,1f);
		e1.setBillboardMode(Emitter.BillboardMode.Velocity);
		e1.setDirectionType(EmitterMesh.DirectionType.RandomTangent);
		e1.setUseRandomEmissionPoint(true);
		e1.setUseVelocityStretching(false);
		
		e1.getInfluencer(GravityInfluencer.class).setGravity(new Vector3f(0f,0f,0f));
		e1.getInfluencer(GravityInfluencer.class).setAlignment(GravityInfluencer.GravityAlignment.Emission_Point);
		e1.getInfluencer(GravityInfluencer.class).setMagnitude(0.5f);
		
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,0.75f,0.25f,1f));
		e1.getInfluencer(ColorInfluencer.class).addColor(new ColorRGBA(1f,1f,0.75f,1f));
		e1.getInfluencer(ColorInfluencer.class).setEnabled(true);
		
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(1f, Interpolation.exp10In);
		e1.getInfluencer(AlphaInfluencer.class).addAlpha(0.5f);
		
		e1.getInfluencer(SizeInfluencer.class).addSize(new Vector3f(.25f,.25f,.25f));
		e1.getInfluencer(SizeInfluencer.class).setEnabled(false);
		
		e1.getInfluencer(RotationInfluencer.class).setUseRandomStartRotation(false, true, false);
		e1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0f, 5f, 0f), Interpolation.exp5Out);
		e1.getInfluencer(RotationInfluencer.class).addRotationSpeed(new Vector3f(0f, 25f, 0f));
		e1.getInfluencer(RotationInfluencer.class).setUseRandomDirection(true);
		
	//	e1.setLocalRotation(e1.getLocalRotation().fromAngleAxis(90*FastMath.DEG_TO_RAD, Vector3f.UNIT_X));
	//	e1.setLocalTranslation(0, 0.01f, 0);
	//	e1.setLocalScale(0.05f);
		
		e1.initialize(assetManager);
		
		ButtonAdapter b = new ButtonAdapter(screen, new Vector2f(0,0)) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (rootNode.getControl(Emitter.class) == null) {
					rootNode.addControl(e1);
					e1.setEnabled(true);
					setText("Remove");
				} else {
					e1.setEnabled(false);
					rootNode.removeControl(e1);
					e1.killAllParticles();
					setText("Add");
				}
			}
		};
		b.setText("Add");
		screen.addElement(b);
		
		ButtonAdapter b2 = new ButtonAdapter(screen, new Vector2f(0,b.getHeight())) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (!e1.isEnabled()) {
					e1.setEnabled(true);
					setText("Pause");
				} else {
					e1.setEnabled(false);
					setText("Resume");
				}
			}
		};
		b2.setText("Pause");
		screen.addElement(b2);
	}
	
	private void setupKeys() {
		inputManager.addMapping("F9", new KeyTrigger(KeyInput.KEY_F9));
		inputManager.addListener(this, "F9");
	}

	float rot = 0;
	boolean dir = true;
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
