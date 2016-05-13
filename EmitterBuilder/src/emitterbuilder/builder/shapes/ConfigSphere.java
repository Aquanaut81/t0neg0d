package emitterbuilder.builder.shapes;

import emitterbuilder.gui.ConfigShapeWindow;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.shape.Sphere;
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
public class ConfigSphere extends ConfigShapeWindow {
	TextField sphereP, sphereR, sphereS;
	ButtonAdapter setSphere;
	int defP = 16, defR = 16;
	float defS = 0.5f;
	
	public ConfigSphere(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSphereP = Layout.getNewLabel(screen, "Set Segments # Planes: ");
		ic.addChild(lSphereP);
		
		Layout.incCol(lSphereP);
		Layout.dim.set(Layout.floatW,Layout.h);
		sphereP = new TextField(screen, "sphereP", Layout.pos, Layout.dim);
		sphereP.setType(TextField.Type.NUMERIC);
		sphereP.setText(String.valueOf(defP));
		ic.addChild(sphereP);
		form.addFormElement(sphereP);
		
		Layout.incCol(sphereP);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lSphereR = Layout.getNewLabel(screen, "# Radial: ");
		ic.addChild(lSphereR);
		
		Layout.incCol(lSphereR);
		Layout.dim.set(Layout.floatW,Layout.h);
		sphereR = new TextField(screen, "sphereR", Layout.pos, Layout.dim);
		sphereR.setType(TextField.Type.NUMERIC);
		sphereR.setText(String.valueOf(defR));
		ic.addChild(sphereR);
		form.addFormElement(sphereR);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSphereS = Layout.getNewLabel(screen, "Sphere Size: ");
		ic.addChild(lSphereS);
		
		Layout.incCol(lSphereS);
		Layout.dim.set(Layout.floatW,Layout.h);
		sphereS = new TextField(screen, "sphereS", Layout.pos, Layout.dim);
		sphereS.setType(TextField.Type.NUMERIC);
		sphereS.setText(String.valueOf(defS));
		ic.addChild(sphereS);
		form.addFormElement(sphereS);
		
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		setSphere = new ButtonAdapter(screen, "setSphere", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				createPrimitive();
			}
		};
		setSphere.setText("Create");
		ic.addChild(setSphere);
		form.addFormElement(setSphere);
		
		ic.sizeToContent();
		
		createWindow("JME Primitive: Sphere");
	}
	
	@Override
	public void loadProperties(EmitterStruct struct) {
		if (builder.getShapeProperties().getFunction() == Shapes.Function.Emitter_Shape) {
			if (struct.getShapeType().equals("JME Primitive Emitter")) {
				if (struct.getEmitter().getShape().getMesh().getClass() == Sphere.class) {
					sphereP.setText(String.valueOf(struct.getESI1()));
					sphereR.setText(String.valueOf(struct.getESI2()));
					sphereS.setText(String.valueOf(struct.getESF1()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		} else {
			if (struct.getParticleType().equals("Primitive As Template")) {
				if (struct.getParticleShapeMesh() == Sphere.class) {
					sphereP.setText(String.valueOf(struct.getPMI1()));
					sphereR.setText(String.valueOf(struct.getPMI2()));
					sphereS.setText(String.valueOf(struct.getPMF1()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		}
	}
	
	private void loadDefaults() {
		sphereP.setText(String.valueOf(defP));
		sphereR.setText(String.valueOf(defR));
		sphereS.setText(String.valueOf(defS));
	}
	
	@Override
	protected void createPrimitive() {
		if (builder.validateIntegerValue(sphereP.getText()) &&
			builder.validateIntegerValue(sphereR.getText()) &&
			builder.validateFloatValue(sphereS.getText())) {
			switch (builder.getShapeProperties().getFunction()) {
				case Emitter_Shape:
					builder.emitter.setESI1(Integer.valueOf(sphereP.getText()));
					builder.emitter.setESI2(Integer.valueOf(sphereR.getText()));
					builder.emitter.setESF1(Float.valueOf(sphereS.getText()));
					Sphere ess = new Sphere(
						builder.emitter.getESI1(),
						builder.emitter.getESI2(),
						builder.emitter.getESF1()
					);
					builder.getShapeProperties().applyNewEmitterShape(ess);
					break;
				case Particle_Mesh:
					builder.emitter.setPMI1(Integer.valueOf(sphereP.getText()));
					builder.emitter.setPMI2(Integer.valueOf(sphereR.getText()));
					builder.emitter.setPMF1(Float.valueOf(sphereS.getText()));
					Sphere pms = new Sphere(
						builder.emitter.getPMI1(),
						builder.emitter.getPMI2(),
						builder.emitter.getPMF1()
					);
					builder.getParticles().loadParticleMesh(ParticleDataTemplateMesh.class,pms);
					builder.emitter.setParticleShapeMesh(Sphere.class);
					break;
			}
			builder.getShapeProperties().closeAllShapeWindows();
		}
	}
}
