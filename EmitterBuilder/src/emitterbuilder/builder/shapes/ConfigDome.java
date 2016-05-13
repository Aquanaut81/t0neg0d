package emitterbuilder.builder.shapes;

import emitterbuilder.gui.ConfigShapeWindow;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Dome;
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
public class ConfigDome extends ConfigShapeWindow {
	TextField domeR, domeS, domeP;
	ButtonAdapter setDome;
	int defP = 8, defR = 16;
	float defS = 0.5f;
	
	public ConfigDome(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPlane = Layout.getNewLabel(screen, "Set Segments # Planes: ");
		ic.addChild(lPlane);
		
		Layout.incCol(lPlane);
		Layout.dim.set(Layout.floatW,Layout.h);
		domeP = new TextField(screen, "domeP", Layout.pos, Layout.dim);
		domeP.setType(TextField.Type.NUMERIC);
		domeP.setText(String.valueOf(defP));
		ic.addChild(domeP);
		form.addFormElement(domeP);
		
		Layout.incCol(domeP);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lRadial = Layout.getNewLabel(screen, "# Radial: ");
		ic.addChild(lRadial);
		
		Layout.incCol(lRadial);
		Layout.dim.set(Layout.floatW,Layout.h);
		domeR = new TextField(screen, "domeR", Layout.pos, Layout.dim);
		domeR.setType(TextField.Type.NUMERIC);
		domeR.setText(String.valueOf(defR));
		ic.addChild(domeR);
		form.addFormElement(domeR);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lDomeS = Layout.getNewLabel(screen, "Dome Size: ");
		ic.addChild(lDomeS);
		
		Layout.incCol(lDomeS);
		Layout.dim.set(Layout.floatW,Layout.h);
		domeS = new TextField(screen, "domeS", Layout.pos, Layout.dim);
		domeS.setType(TextField.Type.NUMERIC);
		domeS.setText(String.valueOf(defS));
		ic.addChild(domeS);
		form.addFormElement(domeS);
		
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		setDome = new ButtonAdapter(screen, "setDome", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				createPrimitive();
			}
		};
		setDome.setText("Create");
		ic.addChild(setDome);
		form.addFormElement(setDome);
		
		ic.sizeToContent();
		
		createWindow("JME Primitive: Dome");
	}
	
	@Override
	public void loadProperties(EmitterStruct struct) {
		if (builder.getShapeProperties().getFunction() == Shapes.Function.Emitter_Shape) {
			if (struct.getShapeType().equals("JME Primitive Emitter")) {
				if (struct.getEmitter().getShape().getMesh().getClass() == Dome.class) {
					domeP.setText(String.valueOf(struct.getESI1()));
					domeR.setText(String.valueOf(struct.getESI2()));
					domeS.setText(String.valueOf(struct.getESF1()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		} else {
			if (struct.getParticleType().equals("Primitive As Template")) {
				if (struct.getParticleShapeMesh() == Dome.class) {
					domeP.setText(String.valueOf(struct.getPMI1()));
					domeR.setText(String.valueOf(struct.getPMI2()));
					domeS.setText(String.valueOf(struct.getPMF1()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		}
	}
	
	private void loadDefaults() {
		domeP.setText(String.valueOf(defP));
		domeR.setText(String.valueOf(defR));
		domeS.setText(String.valueOf(defS));
	}
	
	@Override
	protected void createPrimitive() {
		if (builder.validateIntegerValue(domeP.getText()) &&
			builder.validateIntegerValue(domeR.getText()) &&
			builder.validateFloatValue(domeS.getText())) {
			switch (builder.getShapeProperties().getFunction()) {
				case Emitter_Shape:
					builder.emitter.setESI1(Integer.valueOf(domeP.getText()));
					builder.emitter.setESI2(Integer.valueOf(domeR.getText()));
					builder.emitter.setESF1(Float.valueOf(domeS.getText()));
					Dome esd = new Dome(
						new Vector3f(0,0,0),
						builder.emitter.getESI1(),
						builder.emitter.getESI2(),
						builder.emitter.getESF1(),
						false
					);
					builder.getShapeProperties().applyNewEmitterShape(esd);
					break;
				case Particle_Mesh:
					builder.emitter.setPMI1(Integer.valueOf(domeP.getText()));
					builder.emitter.setPMI2(Integer.valueOf(domeR.getText()));
					builder.emitter.setPMF1(Float.valueOf(domeS.getText()));
					Dome pmd = new Dome(
						new Vector3f(0,0,0),
						builder.emitter.getPMI1(),
						builder.emitter.getPMI2(),
						builder.emitter.getPMF1(),
						false
					);
					builder.getParticles().loadParticleMesh(ParticleDataTemplateMesh.class,pmd);
					builder.emitter.setParticleShapeMesh(Dome.class);
					break;
			}
			builder.getShapeProperties().closeAllShapeWindows();
		}
	}
}
