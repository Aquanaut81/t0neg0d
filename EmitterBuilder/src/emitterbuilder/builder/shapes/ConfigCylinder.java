package emitterbuilder.builder.shapes;

import emitterbuilder.gui.ConfigShapeWindow;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.shape.Cylinder;
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
public class ConfigCylinder extends ConfigShapeWindow {
	TextField cylinderA, cylinderR, cylinderSR, cylinderH;
	ButtonAdapter setCylinder;
	int defA = 8, defR = 16;
	float defSR = 0.25f, defH = 0.5f;
	
	public ConfigCylinder(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lCylinderA = Layout.getNewLabel(screen, "Set Segments # Axis: ");
		ic.addChild(lCylinderA);
		
		Layout.incCol(lCylinderA);
		Layout.dim.set(Layout.floatW,Layout.h);
		cylinderA = new TextField(screen, "cylinderA", Layout.pos, Layout.dim);
		cylinderA.setType(TextField.Type.NUMERIC);
		cylinderA.setText(String.valueOf(defA));
		ic.addChild(cylinderA);
		form.addFormElement(cylinderA);
		
		Layout.incCol(cylinderA);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lCylinderR = Layout.getNewLabel(screen, "# Radial: ");
		ic.addChild(lCylinderR);
		
		Layout.incCol(lCylinderR);
		Layout.dim.set(Layout.floatW,Layout.h);
		cylinderR = new TextField(screen, "cylinderR", Layout.pos, Layout.dim);
		cylinderR.setType(TextField.Type.NUMERIC);
		cylinderR.setText(String.valueOf(defR));
		ic.addChild(cylinderR);
		form.addFormElement(cylinderR);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lCylinderSR = Layout.getNewLabel(screen, "Size Radius: ");
		ic.addChild(lCylinderSR);
		
		Layout.incCol(lCylinderSR);
		Layout.dim.set(Layout.floatW,Layout.h);
		cylinderSR = new TextField(screen, "cylinderSR", Layout.pos, Layout.dim);
		cylinderSR.setType(TextField.Type.NUMERIC);
		cylinderSR.setText(String.valueOf(defSR));
		ic.addChild(cylinderSR);
		form.addFormElement(cylinderSR);
		
		Layout.incCol(cylinderSR);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lCylinderH = Layout.getNewLabel(screen, "Height: ");
		ic.addChild(lCylinderH);
		
		Layout.incCol(lCylinderH);
		Layout.dim.set(Layout.floatW,Layout.h);
		cylinderH = new TextField(screen, "cylinderH", Layout.pos, Layout.dim);
		cylinderH.setType(TextField.Type.NUMERIC);
		cylinderH.setText(String.valueOf(defH));
		ic.addChild(cylinderH);
		form.addFormElement(cylinderH);
		
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		setCylinder = new ButtonAdapter(screen, "setCylinder", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				createPrimitive();
			}
		};
		setCylinder.setText("Create");
		ic.addChild(setCylinder);
		form.addFormElement(setCylinder);
		
		ic.sizeToContent();
		
		createWindow("JME Primitive: Cylinder");
	}
	
	@Override
	public void loadProperties(EmitterStruct struct) {
		if (builder.getShapeProperties().getFunction() == Function.Emitter_Shape) {
			if (struct.getShapeType().equals("JME Primitive Emitter")) {
				if (struct.getEmitter().getShape().getMesh().getClass() == Cylinder.class) {
					cylinderA.setText(String.valueOf(struct.getESI1()));
					cylinderR.setText(String.valueOf(struct.getESI2()));
					cylinderSR.setText(String.valueOf(struct.getESF1()));
					cylinderH.setText(String.valueOf(struct.getESF2()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		} else { 
			if (struct.getParticleType().equals("Primitive As Template")) {
				if (struct.getParticleShapeMesh() == Cylinder.class) {
					cylinderA.setText(String.valueOf(struct.getPMI1()));
					cylinderR.setText(String.valueOf(struct.getPMI2()));
					cylinderSR.setText(String.valueOf(struct.getPMF1()));
					cylinderH.setText(String.valueOf(struct.getPMF2()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		}
	}

	private void loadDefaults() {
		cylinderA.setText(String.valueOf(defA));
		cylinderR.setText(String.valueOf(defR));
		cylinderSR.setText(String.valueOf(defSR));
		cylinderH.setText(String.valueOf(defH));
	}
	
	@Override
	protected void createPrimitive() {
		if (builder.validateIntegerValue(cylinderA.getText()) &&
			builder.validateIntegerValue(cylinderR.getText()) &&
			builder.validateFloatValue(cylinderSR.getText()) &&
			builder.validateFloatValue(cylinderH.getText())) {
			switch (builder.getShapeProperties().getFunction()) {
				case Emitter_Shape:
					builder.emitter.setESI1(Integer.valueOf(cylinderA.getText()));
					builder.emitter.setESI2(Integer.valueOf(cylinderR.getText()));
					builder.emitter.setESF1(Float.valueOf(cylinderSR.getText()));
					builder.emitter.setESF2(Float.valueOf(cylinderH.getText()));
					Cylinder esc = new Cylinder(
						builder.emitter.getESI1(),
						builder.emitter.getESI2(),
						builder.emitter.getESF1(),
						builder.emitter.getESF2()
					);
					builder.getShapeProperties().applyNewEmitterShape(esc);
					break;
				case Particle_Mesh:
					builder.emitter.setPMI1(Integer.valueOf(cylinderA.getText()));
					builder.emitter.setPMI2(Integer.valueOf(cylinderR.getText()));
					builder.emitter.setPMF1(Float.valueOf(cylinderSR.getText()));
					builder.emitter.setPMF2(Float.valueOf(cylinderH.getText()));
					Cylinder pmc = new Cylinder(
						builder.emitter.getPMI1(),
						builder.emitter.getPMI2(),
						builder.emitter.getPMF1(),
						builder.emitter.getPMF2()
					);
					builder.getParticles().loadParticleMesh(ParticleDataTemplateMesh.class,pmc);
					builder.emitter.setParticleShapeMesh(Cylinder.class);
					break;
			}
			builder.getShapeProperties().closeAllShapeWindows();
		}
	}
}
