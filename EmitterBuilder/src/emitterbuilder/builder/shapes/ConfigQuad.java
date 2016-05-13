package emitterbuilder.builder.shapes;

import emitterbuilder.gui.ConfigShapeWindow;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.shape.Quad;
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
public class ConfigQuad extends ConfigShapeWindow {
	TextField quadW, quadH;
	ButtonAdapter setQuad;
	float defW = 0.5f, defH = 0.5f;
	
	public ConfigQuad(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lQuadW = Layout.getNewLabel(screen, "Quad Width: ");
		ic.addChild(lQuadW);
		
		Layout.incCol(lQuadW);
		Layout.dim.set(Layout.floatW,Layout.h);
		quadW = new TextField(screen, "quadW", Layout.pos, Layout.dim);
		quadW.setType(TextField.Type.NUMERIC);
		quadW.setText(String.valueOf(defW));
		ic.addChild(quadW);
		form.addFormElement(quadW);
		
		Layout.incCol(quadW);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lQuadH = Layout.getNewLabel(screen, "Height: ");
		ic.addChild(lQuadH);
		
		Layout.incCol(lQuadH);
		Layout.dim.set(Layout.floatW,Layout.h);
		quadH = new TextField(screen, "quadH", Layout.pos, Layout.dim);
		quadH.setType(TextField.Type.NUMERIC);
		quadH.setText(String.valueOf(defH));
		ic.addChild(quadH);
		form.addFormElement(quadH);
		
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		setQuad = new ButtonAdapter(screen, "setQuad", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				createPrimitive();
			}
		};
		setQuad.setText("Create");
		ic.addChild(setQuad);
		form.addFormElement(setQuad);
		
		ic.sizeToContent();
		
		createWindow("JME Primitive: Quad");
	}
	
	@Override
	public void loadProperties(EmitterStruct struct) {
		if (builder.getShapeProperties().getFunction() == Shapes.Function.Emitter_Shape) {
			if (struct.getShapeType().equals("JME Primitive Emitter")) {
				if (struct.getEmitter().getShape().getMesh().getClass() == Quad.class) {
					quadW.setText(String.valueOf(struct.getESF1()));
					quadH.setText(String.valueOf(struct.getESF2()));
				}else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		} else {
			if (struct.getParticleType().equals("Primitive As Template")) {
				if (struct.getParticleShapeMesh() == Quad.class) {
					quadW.setText(String.valueOf(struct.getPMF1()));
					quadH.setText(String.valueOf(struct.getPMF2()));
				} else {
					loadDefaults();
				}
			} else {
				loadDefaults();
			}
		}
	}
	
	private void loadDefaults() {
		quadW.setText(String.valueOf(defW));
		quadH.setText(String.valueOf(defH));
	}
	
	@Override
	protected void createPrimitive() {
		if (builder.validateFloatValue(quadW.getText()) &&
			builder.validateFloatValue(quadH.getText())) {
			switch (builder.getShapeProperties().getFunction()) {
				case Emitter_Shape:
					builder.emitter.setESF1(Float.valueOf(quadW.getText()));
					builder.emitter.setESF2(Float.valueOf(quadH.getText()));
					Quad esq = new Quad(
						builder.emitter.getESF1(),
						builder.emitter.getESF2()
					);
					builder.getShapeProperties().applyNewEmitterShape(esq);
					break;
				case Particle_Mesh:
					builder.emitter.setPMF1(Float.valueOf(quadW.getText()));
					builder.emitter.setPMF2(Float.valueOf(quadH.getText()));
					Quad pmq = new Quad(
						builder.emitter.getPMF1(),
						builder.emitter.getPMF2()
					);
					builder.getParticles().loadParticleMesh(ParticleDataTemplateMesh.class,pmq);
					builder.emitter.setParticleShapeMesh(Quad.class);
					break;
			}
			builder.getShapeProperties().closeAllShapeWindows();
		}
	}
}
