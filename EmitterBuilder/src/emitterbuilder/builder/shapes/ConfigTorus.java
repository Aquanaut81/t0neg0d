package emitterbuilder.builder.shapes;

import emitterbuilder.gui.ConfigShapeWindow;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.shape.Torus;
import emitter.particle.ParticleDataTemplateMesh;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
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
public class ConfigTorus extends ConfigShapeWindow {
	TextField torusC, torusR, torusI, torusO;
	ButtonAdapter setTorus;
	int defC = 16, defR = 16;
	float defI = 0.25f, defO = 0.5f;
	
	public ConfigTorus(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lTorusC = Layout.getNewLabel(screen, "Set Segments # Circle: ");
		ic.addChild(lTorusC);
		
		Layout.incCol(lTorusC);
		Layout.dim.set(Layout.floatW,Layout.h);
		torusC = new TextField(screen, "torusC", Layout.pos, Layout.dim);
		torusC.setType(TextField.Type.NUMERIC);
		torusC.setText(String.valueOf(defC));
		ic.addChild(torusC);
		form.addFormElement(torusC);
		
		Layout.incCol(torusC);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lTorusR = Layout.getNewLabel(screen, "# Radial: ");
		ic.addChild(lTorusR);
		
		Layout.incCol(lTorusR);
		Layout.dim.set(Layout.floatW,Layout.h);
		torusR = new TextField(screen, "torusR", Layout.pos, Layout.dim);
		torusR.setType(TextField.Type.NUMERIC);
		torusR.setText(String.valueOf(defR));
		ic.addChild(torusR);
		form.addFormElement(torusR);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lTorusI = Layout.getNewLabel(screen, "Radius Inner: ");
		ic.addChild(lTorusI);
		
		Layout.incCol(lTorusI);
		Layout.dim.set(Layout.floatW,Layout.h);
		torusI = new TextField(screen, "torusI", Layout.pos, Layout.dim);
		torusI.setType(TextField.Type.NUMERIC);
		torusI.setText(String.valueOf(defI));
		ic.addChild(torusI);
		form.addFormElement(torusI);
		
		Layout.incCol(torusI);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lTorusO = Layout.getNewLabel(screen, "Outter: ");
		ic.addChild(lTorusO);
		
		Layout.incCol(lTorusO);
		Layout.dim.set(Layout.floatW,Layout.h);
		torusO = new TextField(screen, "torusO", Layout.pos, Layout.dim);
		torusO.setType(TextField.Type.NUMERIC);
		torusO.setText(String.valueOf(defO));
		ic.addChild(torusO);
		form.addFormElement(torusO);
		
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		setTorus = new ButtonAdapter(screen, "setTorus", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				createPrimitive();
			}
		};
		setTorus.setText("Create");
		ic.addChild(setTorus);
		form.addFormElement(setTorus);
		
		ic.sizeToContent();
		
		createWindow("JME Primitive: Torus");
	}
	
	@Override
	public void loadProperties(EmitterStruct struct) {
		if (builder.getShapeProperties().getFunction() == Shapes.Function.Emitter_Shape) {
			if (struct.getShapeType().equals("JME Primitive Emitter")) {
				if (struct.getEmitter().getShape().getMesh().getClass() == Torus.class) {
					torusC.setText(String.valueOf(struct.getESI1()));
					torusR.setText(String.valueOf(struct.getESI2()));
					torusI.setText(String.valueOf(struct.getESF1()));
					torusO.setText(String.valueOf(struct.getESF2()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		} else {
			if (struct.getParticleType().equals("Primitive As Template")) {
				if (struct.getParticleShapeMesh() == Torus.class) {
					torusC.setText(String.valueOf(struct.getPMI1()));
					torusR.setText(String.valueOf(struct.getPMI2()));
					torusI.setText(String.valueOf(struct.getPMF1()));
					torusO.setText(String.valueOf(struct.getPMF2()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		}
	}

	private void loadDefaults() {
		torusC.setText(String.valueOf(defC));
		torusR.setText(String.valueOf(defR));
		torusI.setText(String.valueOf(defI));
		torusO.setText(String.valueOf(defO));
	}
	
	@Override
	protected void createPrimitive() {
		if (builder.validateIntegerValue(torusC.getText()) &&
			builder.validateIntegerValue(torusR.getText()) &&
			builder.validateFloatValue(torusI.getText()) &&
			builder.validateFloatValue(torusO.getText())) {
			switch (builder.getShapeProperties().getFunction()) {
				case Emitter_Shape:
					builder.emitter.setESI1(Integer.valueOf(torusC.getText()));
					builder.emitter.setESI2(Integer.valueOf(torusR.getText()));
					builder.emitter.setESF1(Float.valueOf(torusI.getText()));
					builder.emitter.setESF2(Float.valueOf(torusO.getText()));
					Torus est = new Torus(
						builder.emitter.getESI1(),
						builder.emitter.getESI2(),
						builder.emitter.getESF1(),
						builder.emitter.getESF2()
					);
					builder.getShapeProperties().applyNewEmitterShape(est);
					break;
				case Particle_Mesh:
					builder.emitter.setPMI1(Integer.valueOf(torusC.getText()));
					builder.emitter.setPMI2(Integer.valueOf(torusR.getText()));
					builder.emitter.setPMF1(Float.valueOf(torusI.getText()));
					builder.emitter.setPMF2(Float.valueOf(torusO.getText()));
					Torus pmt = new Torus(
						builder.emitter.getPMI1(),
						builder.emitter.getPMI2(),
						builder.emitter.getPMF1(),
						builder.emitter.getPMF2()
					);
					builder.getParticles().loadParticleMesh(ParticleDataTemplateMesh.class,pmt);
					builder.emitter.setParticleShapeMesh(Torus.class);
					break;
			}
			builder.getShapeProperties().closeAllShapeWindows();
		}
	}
}
