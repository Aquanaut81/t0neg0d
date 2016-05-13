package emitterbuilder.builder.shapes;

import emitterbuilder.gui.ConfigShapeWindow;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.shape.Box;
import emitter.particle.ParticleDataTemplateMesh;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.builder.shapes.Shapes.Function;
import static emitterbuilder.builder.shapes.Shapes.Function.Emitter_Shape;
import static emitterbuilder.builder.shapes.Shapes.Function.Particle_Mesh;
import emitterbuilder.gui.utils.Layout;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class ConfigBox extends ConfigShapeWindow {
	private ButtonAdapter setBox;
	public TextField boxW, boxH, boxD;
	private float defW = 0.5f, defH = 0.5f, defD = 0.5f;
	
	public ConfigBox(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lBoxW = Layout.getNewLabel(screen, "Box Width: ");
		ic.addChild(lBoxW);
		
		Layout.incCol(lBoxW);
		Layout.dim.set(Layout.floatW,Layout.h);
		boxW = new TextField(screen, "boxW", Layout.pos, Layout.dim);
		boxW.setType(TextField.Type.NUMERIC);
		boxW.setText(String.valueOf(defW));
		ic.addChild(boxW);
		form.addFormElement(boxW);
		
		Layout.incCol(boxW);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lBoxH = Layout.getNewLabel(screen, "Height: ");
		ic.addChild(lBoxH);
		
		Layout.incCol(lBoxH);
		Layout.dim.set(Layout.floatW,Layout.h);
		boxH = new TextField(screen, "boxH", Layout.pos, Layout.dim);
		boxH.setType(TextField.Type.NUMERIC);
		boxH.setText(String.valueOf(defH));
		ic.addChild(boxH);
		form.addFormElement(boxH);
		
		Layout.incCol(boxH);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lBoxD = Layout.getNewLabel(screen, "Depth: ");
		ic.addChild(lBoxD);
		
		Layout.incCol(lBoxD);
		Layout.dim.set(Layout.floatW,Layout.h);
		boxD = new TextField(screen, "boxD", Layout.pos, Layout.dim);
		boxD.setType(TextField.Type.NUMERIC);
		boxD.setText(String.valueOf(defD));
		ic.addChild(boxD);
		form.addFormElement(boxD);
		
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		setBox = new ButtonAdapter(screen, "setBox", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				createPrimitive();
			}
		};
		setBox.setText("Create");
		ic.addChild(setBox);
		form.addFormElement(setBox);
		
		ic.sizeToContent();
		
		createWindow("JME Primitive: Box");
	}

	@Override
	public void loadProperties(EmitterStruct struct) {
		if (builder.getShapeProperties().getFunction() == Function.Emitter_Shape) {
			if (struct.getShapeType().equals("JME Primitive Emitter")) {
				if (struct.getEmitter().getShape().getMesh().getClass() == Box.class) {
					boxW.setText(String.valueOf(struct.getESF1()));
					boxH.setText(String.valueOf(struct.getESF2()));
					boxD.setText(String.valueOf(struct.getESF3()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		} else {
			if (struct.getParticleType().equals("Primitive As Template")) {
				if (struct.getParticleShapeMesh() == Box.class) {
					boxW.setText(String.valueOf(struct.getPMF1()));
					boxH.setText(String.valueOf(struct.getPMF2()));
					boxD.setText(String.valueOf(struct.getPMF3()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		}
	}
	
	private void loadDefaults() {
		boxW.setText(String.valueOf(defW));
		boxH.setText(String.valueOf(defH));
		boxD.setText(String.valueOf(defD));
	}
	
	@Override
	protected void createPrimitive() {
		if (builder.validateFloatValue(boxW.getText()) &&
			builder.validateFloatValue(boxH.getText()) &&
			builder.validateFloatValue(boxD.getText())) {
			switch (builder.getShapeProperties().getFunction()) {
				case Emitter_Shape:
					builder.emitter.setESF1(Float.valueOf(boxW.getText()));
					builder.emitter.setESF2(Float.valueOf(boxH.getText()));
					builder.emitter.setESF3(Float.valueOf(boxD.getText()));
					Box esb = new Box(
						builder.emitter.getESF1(),
						builder.emitter.getESF2(),
						builder.emitter.getESF3()
					);
					builder.getShapeProperties().applyNewEmitterShape(esb);
					break;
				case Particle_Mesh:
					builder.emitter.setPMF1(Float.valueOf(boxW.getText()));
					builder.emitter.setPMF2(Float.valueOf(boxH.getText()));
					builder.emitter.setPMF3(Float.valueOf(boxD.getText()));
					Box pmb = new Box(
						builder.emitter.getPMF1(),
						builder.emitter.getPMF2(),
						builder.emitter.getPMF3()
					);
					builder.getParticles().loadParticleMesh(ParticleDataTemplateMesh.class, pmb);
					builder.emitter.setParticleShapeMesh(Box.class);
					break;
			}
			builder.getShapeProperties().closeAllShapeWindows();
		}
	}
}
