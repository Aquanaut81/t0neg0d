package emitterbuilder.builder.particles;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.Mesh;
import emitter.Emitter.BillboardMode;
import emitter.particle.ParticleDataImpostorMesh;
import emitter.particle.ParticleDataPointMesh;
import emitter.particle.ParticleDataTriMesh;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.shapes.Shapes;
import emitterbuilder.gui.CollapsableWindow;
import emitterbuilder.gui.utils.Layout;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class Particles extends CollapsableWindow {
	ButtonAdapter partConfig;
	CheckBox partTest, partFollow, partStatic, partStretch;
	SelectBox partType, partBBMode;
	TextField forceMin, forceMax, lifeMin, lifeMax, stretchMag, numParts;
	
	public Particles(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		loadParticleWindows();
	}
	
	@Override
	protected void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPartTest = Layout.getNewLabel(screen, "Enable Test Mode: ");
		ic.addChild(lPartTest);
		
		Layout.incCol(lPartTest);
		Layout.dim.set(Layout.h,Layout.h);
		partTest = new CheckBox(screen, "partTest", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setEmitterTestMode(builder.emitter.getEmitter().getEmitterTestModeShape(), isToggled);
				}
			}
		};
		ic.addChild(partTest);
		form.addFormElement(partTest);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPartType = Layout.getNewLabel(screen, "Particle Type: ");
		ic.addChild(lPartType);
		
		Layout.incCol(lPartType);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		partType = new SelectBox(screen, "partType", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				try {
			//	if (builder.emitter != null)
			//		builder.emitter.setShapeType(shapeType.getSelectedListItem().getCaption());
				if (partConfig != null) {
					switch (selectedIndex) {
						case 0:
							builder.emitter.setParticleType("Quad");
							loadParticleMesh(ParticleDataTriMesh.class,null);
							partConfig.setText("");
							partConfig.setIsEnabled(false);
							break;
						case  1:
							builder.emitter.setParticleType("Point");
							loadParticleMesh(ParticleDataPointMesh.class,null);
							partConfig.setText("");
							partConfig.setIsEnabled(false);
							break;
						case 2:
							builder.emitter.setParticleType("Impostor");
							loadParticleMesh(ParticleDataImpostorMesh.class,null);
							partConfig.setText("");
							partConfig.setIsEnabled(false);
							break;
						case 3:
							builder.emitter.setParticleType("Primitive As Template");
							partConfig.setText("Config");
							partConfig.setIsEnabled(true);
							break;
						case 4:
							builder.emitter.setParticleType("Mesh As Template");
							partConfig.setText("Load");
							partConfig.setIsEnabled(true);
							break;
					}
				}
				} catch (Exception ex) {  }
			}
		};
		partType.addListItem("Quad", 0);
		partType.addListItem("Point", 1);
		partType.addListItem("Impostor", 2);
		partType.addListItem("Primitive As Template", 3);
		partType.addListItem("Mesh As Template", 4);
		ic.addChild(partType);
		form.addFormElement(partType);
		
		Layout.incCol(partType);
		Layout.x += Layout.h;
		Layout.pos.set(Layout.x,Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		partConfig = new ButtonAdapter(screen, "partConfig", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				builder.getShapeProperties().setFunction(Shapes.Function.Particle_Mesh);
				switch (partType.getSelectIndex()) {
					case 3:
						builder.getShapeProperties().showSelectPrimitive();
						break;
				}
			}
		};
		ic.addChild(partConfig);
		form.addFormElement(partConfig);
		partConfig.setIsEnabled(false);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lBBMode = Layout.getNewLabel(screen, "Billboard Mode: ");
		ic.addChild(lBBMode);
		
		Layout.incCol(lBBMode);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		partBBMode = new SelectBox(screen, "partBBMode", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setBillboardMode(BillboardMode.valueOf(partBBMode.getSelectedListItem().getCaption()));
				}
			}
		};
		for (BillboardMode bbMode : BillboardMode.values()) {
			partBBMode.addListItem(bbMode.name(), bbMode);
		}
		ic.addChild(partBBMode);
		form.addFormElement(partBBMode);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lForceMin = Layout.getNewLabel(screen, "Initial Force Min: ");
		ic.addChild(lForceMin);
		
		Layout.incCol(lForceMin);
		Layout.dim.set(Layout.floatW,Layout.h);
		forceMin = new TextField(screen, "forceMin", Layout.pos, Layout.dim);
		forceMin.setType(TextField.Type.NUMERIC);
		forceMin.setText("1.0");
		ic.addChild(forceMin);
		form.addFormElement(forceMin);
		
		Layout.incCol(forceMin);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lForceMax = Layout.getNewLabel(screen, "Max: ");
		ic.addChild(lForceMax);
		
		Layout.incCol(lForceMax);
		Layout.dim.set(Layout.floatW,Layout.h);
		forceMax = new TextField(screen, "forceMax", Layout.pos, Layout.dim);
		forceMax.setType(TextField.Type.NUMERIC);
		forceMax.setText("1.0");
		ic.addChild(forceMax);
		form.addFormElement(forceMax);
		
		Layout.incCol(forceMax);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyForce = new ButtonAdapter(screen, "applyForce", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(forceMin.getText()) &&
					builder.validateFloatValue(forceMax.getText())) {
					builder.emitter.getEmitter().setForceMinMax(
						Float.valueOf(forceMin.getText()),
						Float.valueOf(forceMax.getText())
					);
				}
			}
		};
		applyForce.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyForce);
		form.addFormElement(applyForce);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lLifeMin = Layout.getNewLabel(screen, "Particle Life Min: ");
		ic.addChild(lLifeMin);
		
		Layout.incCol(lLifeMin);
		Layout.dim.set(Layout.floatW,Layout.h);
		lifeMin = new TextField(screen, "lifeMin", Layout.pos, Layout.dim);
		lifeMin.setType(TextField.Type.NUMERIC);
		lifeMin.setText("1.0");
		ic.addChild(lifeMin);
		form.addFormElement(lifeMin);
		
		Layout.incCol(lifeMin);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lLifeMax = Layout.getNewLabel(screen, "Max: ");
		ic.addChild(lLifeMax);
		
		Layout.incCol(lLifeMax);
		Layout.dim.set(Layout.floatW,Layout.h);
		lifeMax = new TextField(screen, "lifeMax", Layout.pos, Layout.dim);
		lifeMax.setType(TextField.Type.NUMERIC);
		lifeMax.setText("1.0");
		ic.addChild(lifeMax);
		form.addFormElement(lifeMax);
		
		Layout.incCol(lifeMax);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyLife = new ButtonAdapter(screen, "applyLife", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(lifeMin.getText()) &&
					builder.validateFloatValue(lifeMax.getText())) {
					builder.emitter.getEmitter().setLifeMinMax(
						Float.valueOf(lifeMin.getText()),
						Float.valueOf(lifeMax.getText())
					);
				}
			}
		};
		applyLife.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyLife);
		form.addFormElement(applyLife);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPartFollow = Layout.getNewLabel(screen, "Particles Follow Emitter: ");
		ic.addChild(lPartFollow);
		
		Layout.incCol(lPartFollow);
		Layout.dim.set(Layout.h,Layout.h);
		partFollow = new CheckBox(screen, "partFollow", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setParticlesFollowEmitter(isToggled);
				}
			}
		};
		ic.addChild(partFollow);
		form.addFormElement(partFollow);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPartStatic = Layout.getNewLabel(screen, "Static Particles: ");
		ic.addChild(lPartStatic);
		
		Layout.incCol(lPartStatic);
		Layout.dim.set(Layout.h,Layout.h);
		partStatic = new CheckBox(screen, "partStatic", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setUseStaticParticles(isToggled);
				}
			}
		};
		ic.addChild(partStatic);
		form.addFormElement(partStatic);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPartStretch = Layout.getNewLabel(screen, "Particle Stretching: ");
		ic.addChild(lPartStretch);
		
		Layout.incCol(lPartStretch);
		Layout.dim.set(Layout.h,Layout.h);
		partStretch = new CheckBox(screen, "partStretch", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setUseVelocityStretching(isToggled);
				}
			}
		};
		ic.addChild(partStretch);
		form.addFormElement(partStretch);
		
		Layout.incCol(partStretch);
		Layout.dim.set(Layout.lWidthM,Layout.h);
		Label lPartMag = Layout.getNewLabel(screen, "Magnitude: ");
		ic.addChild(lPartMag);
		
		Layout.incCol(lPartMag);
		Layout.dim.set(Layout.floatW,Layout.h);
		stretchMag = new TextField(screen, "stretchMag", Layout.pos, Layout.dim);
		stretchMag.setType(TextField.Type.NUMERIC);
		stretchMag.setText("1.0");
		ic.addChild(stretchMag);
		form.addFormElement(stretchMag);
		
		Layout.incCol(stretchMag);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyStretch = new ButtonAdapter(screen, "applyStretch", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(stretchMag.getText())) {
					builder.emitter.getEmitter().setVelocityStretchFactor(
						Float.valueOf(stretchMag.getText())
					);
				}
			}
		};
		applyStretch.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyStretch);
		form.addFormElement(applyStretch);
		
		Layout.incRow();
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lKillAllParts = Layout.getNewLabel(screen, "Kill All Particles: ");
		ic.addChild(lKillAllParts);
		
		Layout.incCol(lKillAllParts);
		Layout.dim.set(Layout.bWidth*2,Layout.h);
		ButtonAdapter killAllParts = new ButtonAdapter(screen, "killAllParts", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				builder.emitter.getEmitter().killAllParticles();
			}
		};
		killAllParts.setText("Reset");
		ic.addChild(killAllParts);
		form.addFormElement(killAllParts);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lEmitAllParts = Layout.getNewLabel(screen, "Emit All Particles: ");
		ic.addChild(lEmitAllParts);
		
		Layout.incCol(lEmitAllParts);
		Layout.dim.set(Layout.bWidth*2,Layout.h);
		ButtonAdapter emitAllParts = new ButtonAdapter(screen, "emitAllParts", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				builder.emitter.getEmitter().emitAllParticles();
			}
		};
		emitAllParts.setText("Emit All Particles");
		ic.addChild(emitAllParts);
		form.addFormElement(emitAllParts);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lEmitNumParts = Layout.getNewLabel(screen, "Emit # of Particles: ");
		ic.addChild(lEmitNumParts);
		
		Layout.incCol(lEmitNumParts);
		Layout.dim.set(Layout.floatW,Layout.h);
		numParts = new TextField(screen, "numParts", Layout.pos, Layout.dim);
		numParts.setType(TextField.Type.NUMERIC);
		numParts.setText("");
		ic.addChild(numParts);
		form.addFormElement(numParts);
		
		Layout.incCol(numParts);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter emitNumParts = new ButtonAdapter(screen, "emitNumParts", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateIntegerValue(numParts.getText())) {
					builder.emitter.getEmitter().emitNumParticles(
						Integer.valueOf(numParts.getText())
					);
				}
			}
		};
		emitNumParts.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(emitNumParts);
		form.addFormElement(emitNumParts);
		
		Layout.incRow();
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPartMat = Layout.getNewLabel(screen, "Particle Material: ");
		ic.addChild(lPartMat);
		
		Layout.incCol(lPartMat);
		Layout.dim.set(Layout.bWidth,Layout.h);
		ButtonAdapter configMat = new ButtonAdapter(screen, "configMat", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				builder.showMatWin();
			}
		};
		configMat.setText("Configure");
		ic.addChild(configMat);
		form.addFormElement(configMat);
		
		ic.sizeToContent();
		
		createWindow("");
	}
	public void loadParticleMesh(Class c, Mesh mesh) {
		builder.emitter.setParticleMeshType(c);
		builder.emitter.setParticleMesh(mesh);
		builder.rebuildEmitter();
	}

	@Override
	public void loadProperties() {
		win.setWindowTitle("Particle Properties (" + builder.emitter.getEmitter().getName() + ")");
		partTest.setIsChecked(builder.emitter.getEmitter().getEmitterTestModeParticles());
		partType.setSelectedByCaption(builder.emitter.getParticleType(), true);
		partBBMode.setSelectedByCaption(builder.emitter.getEmitter().getBillboardMode().name(), true);
		forceMin.setText(String.valueOf(builder.emitter.getEmitter().getForceMin()));
		forceMax.setText(String.valueOf(builder.emitter.getEmitter().getForceMax()));
		lifeMin.setText(String.valueOf(builder.emitter.getEmitter().getLifeMin()));
		lifeMax.setText(String.valueOf(builder.emitter.getEmitter().getLifeMax()));
		partFollow.setIsCheckedNoCallback(builder.emitter.getEmitter().getParticlesFollowEmitter());
		partStatic.setIsCheckedNoCallback(builder.emitter.getEmitter().getUseStaticParticles());
		partStretch.setIsCheckedNoCallback(builder.emitter.getEmitter().getUseVelocityStretching());
		stretchMag.setText(String.valueOf(builder.emitter.getEmitter().getVelocityStretchFactor()));
	}
	
	private void loadParticleWindows() {
		
	}
}
