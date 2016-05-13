/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emitterbuilder.builder;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import emitter.Emitter.ParticleEmissionPoint;
import emitter.EmitterMesh.DirectionType;
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
public class Properties extends CollapsableWindow {

	TextField shapePosX, shapePosY, shapePosZ, 
			shapeRotAngleX, shapeRotAngleY, shapeRotAngleZ, 
			shapeScaleX, shapeScaleY, shapeScaleZ,
			numParticles, emPerSec, partPerEm;
	CheckBox shapeTest, updateLoop, randPoint, seqEmFace,
			skipPattern;
	SelectBox shapeType, directionType, partEP;
	ButtonAdapter shapeConfig;
	
	public Properties(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
	}
	
	@Override
	protected void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lShapeTest = Layout.getNewLabel(screen, "Test Mode: ");
		ic.addChild(lShapeTest);
		
		Layout.incCol(lShapeTest);
		Layout.dim.set(Layout.h,Layout.h);
		shapeTest = new CheckBox(screen, "shapeTest", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setEmitterTestMode(isToggled, builder.emitter.getEmitter().getEmitterTestModeParticles());
				}
			}
		};
		ic.addChild(shapeTest);
		form.addFormElement(shapeTest);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lUpdateLoop = Layout.getNewLabel(screen, "Update Loop: ");
		ic.addChild(lUpdateLoop);
		
		Layout.incCol(lUpdateLoop);
		Layout.dim.set(Layout.h,Layout.h);
		updateLoop = new CheckBox(screen, "updateLoop", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setEnabled(isToggled);
				}
			}
		};
		ic.addChild(updateLoop);
		form.addFormElement(updateLoop);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lShape = Layout.getNewLabel(screen, "Shape: ");
		ic.addChild(lShape);
		
		Layout.incCol(lShape);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		shapeType = new SelectBox(screen, "shapeType", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (builder.emitter != null)
					builder.emitter.setShapeType(shapeType.getSelectedListItem().getCaption());
				if (shapeConfig != null) {
					switch (selectedIndex) {
						case 0:
							shapeConfig.setText("");
							shapeConfig.setIsEnabled(false);
							if (builder.emitter != null) {
								builder.emitter.setShapeType("Simple Emitter");
								builder.startChangeState();
								builder.resetChangeState();
								builder.emitter.getEmitter().setShapeSimpleEmitter();
								builder.endChangeState();
							}
							break;
						case  1:
							shapeConfig.setText("Config");
							shapeConfig.setIsEnabled(true);
							break;
						case 2:
							shapeConfig.setText("Load");
							shapeConfig.setIsEnabled(true);
							break;
					}
				}
			}
		};
		shapeType.addListItem("Simple Emitter", 0);
		shapeType.addListItem("JME Primitive Emitter", 1);
		shapeType.addListItem("Mesh-base Emitter", 2);
		ic.addChild(shapeType);
		form.addFormElement(shapeType);
		
		Layout.incCol(shapeType);
		Layout.x += Layout.h;
		Layout.pos.set(Layout.x,Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		shapeConfig = new ButtonAdapter(screen, "shapeConfig", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				builder.getShapeProperties().setFunction(Shapes.Function.Emitter_Shape);
				switch (shapeType.getSelectIndex()) {
					case 1:
						builder.getShapeProperties().showSelectPrimitive();
						break;
					case 2:
						builder.getShapeProperties().showLoadMesh();
						break;
				}
			}
		};
		ic.addChild(shapeConfig);
		form.addFormElement(shapeConfig);
		shapeConfig.setIsEnabled(false);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lDirType = Layout.getNewLabel(screen, "Direction: ");
		ic.addChild(lDirType);
		
		Layout.incCol(lDirType);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		directionType = new SelectBox(screen, "directionType", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setDirectionType(DirectionType.valueOf(directionType.getSelectedListItem().getCaption()));
				}
			}
		};
		for (DirectionType dType : DirectionType.values()) {
			directionType.addListItem(dType.name(), dType);
		}
		ic.addChild(directionType);
		form.addFormElement(directionType);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPartEP = Layout.getNewLabel(screen, "Emission Anchor: ");
		ic.addChild(lPartEP);
		
		Layout.incCol(lPartEP);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		partEP = new SelectBox(screen, "partEP", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setParticleEmissionPoint(ParticleEmissionPoint.valueOf(partEP.getSelectedListItem().getCaption()));
				}
			}
		};
		for (ParticleEmissionPoint pPoint : ParticleEmissionPoint.values()) {
			partEP.addListItem(pPoint.name(), pPoint);
		}
		ic.addChild(partEP);
		form.addFormElement(partEP);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRandPoint = Layout.getNewLabel(screen, "Random Point: ");
		ic.addChild(lRandPoint);
		
		Layout.incCol(lShapeTest);
		Layout.dim.set(Layout.h,Layout.h);
		randPoint = new CheckBox(screen, "randPoint", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setUseRandomEmissionPoint(isToggled);
				}
			}
		};
		ic.addChild(randPoint);
		form.addFormElement(randPoint);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSeqEmFace = Layout.getNewLabel(screen, "Sequential Face: ");
		ic.addChild(lSeqEmFace);
		
		Layout.incCol(lSeqEmFace);
		Layout.dim.set(Layout.h,Layout.h);
		seqEmFace = new CheckBox(screen, "seqEmFace", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setUseSequentialEmissionFace(isToggled);
				}
			}
		};
		ic.addChild(seqEmFace);
		form.addFormElement(seqEmFace);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSkipPattern = Layout.getNewLabel(screen, "Skip Pattern: ");
		ic.addChild(lSkipPattern);
		
		Layout.incCol(lSkipPattern);
		Layout.dim.set(Layout.h,Layout.h);
		skipPattern = new CheckBox(screen, "skipPattern", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				if (builder.emitter != null) {
					builder.emitter.getEmitter().setUseSequentialSkipPattern(isToggled);
				}
			}
		};
		ic.addChild(skipPattern);
		form.addFormElement(skipPattern);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lNumParticles = Layout.getNewLabel(screen, "Max Particles: ");
		ic.addChild(lNumParticles);
		
		Layout.incCol(lNumParticles);
		Layout.dim.set(Layout.floatW,Layout.h);
		numParticles = new TextField(screen, "numParticles", Layout.pos, Layout.dim);
		numParticles.setType(TextField.Type.NUMERIC);
		numParticles.setText("100");
		ic.addChild(numParticles);
		form.addFormElement(numParticles);
		
		Layout.incCol(numParticles);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyNumPart = new ButtonAdapter(screen, "applyNumPart", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				builder.rebuildEmitter();
			}
		};
		applyNumPart.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyNumPart);
		form.addFormElement(applyNumPart);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lEPerSec = Layout.getNewLabel(screen, "Emissions Per Sec: ");
		ic.addChild(lEPerSec);
		
		Layout.incCol(lEPerSec);
		Layout.dim.set(Layout.floatW,Layout.h);
		emPerSec = new TextField(screen, "emPerSec", Layout.pos, Layout.dim);
		emPerSec.setType(TextField.Type.NUMERIC);
		emPerSec.setText("100");
		ic.addChild(emPerSec);
		form.addFormElement(emPerSec);
		
		Layout.incCol(emPerSec);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyEmPerSec = new ButtonAdapter(screen, "applyEmPerSec", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.emitter != null) {
					if (builder.validateIntegerValue(emPerSec.getText())) {
						builder.emitter.getEmitter().setEmissionsPerSecond(Integer.valueOf(emPerSec.getText()));
					}
				}
			}
		};
		applyEmPerSec.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyEmPerSec);
		form.addFormElement(applyEmPerSec);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lPPerE = Layout.getNewLabel(screen, "Particles Per Emission: ");
		ic.addChild(lPPerE);
		
		Layout.incCol(lPPerE);
		Layout.dim.set(Layout.floatW,Layout.h);
		partPerEm = new TextField(screen, "partPerEm", Layout.pos, Layout.dim);
		partPerEm.setType(TextField.Type.NUMERIC);
		partPerEm.setText("1");
		ic.addChild(partPerEm);
		form.addFormElement(partPerEm);
		
		Layout.incCol(partPerEm);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyPartPerEm = new ButtonAdapter(screen, "applyPartPerEm", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.emitter != null) {
					if (builder.validateIntegerValue(partPerEm.getText())) {
						builder.emitter.getEmitter().setParticlesPerEmission(Integer.valueOf(partPerEm.getText()));
					}
				}
			}
		};
		applyPartPerEm.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyPartPerEm);
		form.addFormElement(applyPartPerEm);
		
		// TODO: Continue...
		Layout.incRow();
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lShapePosX = Layout.getNewLabel(screen, "Position X: ");
		ic.addChild(lShapePosX);
		
		Layout.incCol(lShapePosX);
		Layout.dim.set(Layout.floatW,Layout.h);
		shapePosX = new TextField(screen, "shapePosX", Layout.pos, Layout.dim);
		shapePosX.setType(TextField.Type.NUMERIC);
		shapePosX.setText("0.0");
		ic.addChild(shapePosX);
		form.addFormElement(shapePosX);
		
		Layout.incCol(shapePosX);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lShapePosY = Layout.getNewLabel(screen, "Y: ");
		ic.addChild(lShapePosY);
		
		Layout.incCol(lShapePosY);
		Layout.dim.set(Layout.floatW,Layout.h);
		shapePosY = new TextField(screen, "shapePosY", Layout.pos, Layout.dim);
		shapePosY.setType(TextField.Type.NUMERIC);
		shapePosY.setText("0.0");
		ic.addChild(shapePosY);
		form.addFormElement(shapePosY);
		
		Layout.incCol(shapePosY);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lShapePosZ = Layout.getNewLabel(screen, "Z: ");
		ic.addChild(lShapePosZ);
		
		Layout.incCol(lShapePosZ);
		Layout.dim.set(Layout.floatW,Layout.h);
		shapePosZ = new TextField(screen, "shapePosZ", Layout.pos, Layout.dim);
		shapePosZ.setType(TextField.Type.NUMERIC);
		shapePosZ.setText("0.0");
		ic.addChild(shapePosZ);
		form.addFormElement(shapePosZ);
		
		Layout.incCol(shapePosZ);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyShapePos = new ButtonAdapter(screen, "applyShapePos", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.emitter != null) {
					if (builder.validateFloatValue(shapePosX.getText()) &&
						builder.validateFloatValue(shapePosY.getText()) &&
						builder.validateFloatValue(shapePosZ.getText())) {
						builder.emitter.getEmitter().setLocalTranslation(
							Float.valueOf(shapePosX.getText()),
							Float.valueOf(shapePosY.getText()),
							Float.valueOf(shapePosZ.getText())
						);
					}
				}
			}
		};
		applyShapePos.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyShapePos);
		form.addFormElement(applyShapePos);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lShapeScaleX = Layout.getNewLabel(screen, "Scale X: ");
		ic.addChild(lShapeScaleX);
		
		Layout.incCol(lShapeScaleX);
		Layout.dim.set(Layout.floatW,Layout.h);
		shapeScaleX = new TextField(screen, "shapeScaleX", Layout.pos, Layout.dim);
		shapeScaleX.setType(TextField.Type.NUMERIC);
		shapeScaleX.setText("1.0");
		ic.addChild(shapeScaleX);
		form.addFormElement(shapeScaleX);
		
		Layout.incCol(shapeScaleX);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lShapeScaleY = Layout.getNewLabel(screen, "Y: ");
		ic.addChild(lShapeScaleY);
		
		Layout.incCol(lShapeScaleY);
		Layout.dim.set(Layout.floatW,Layout.h);
		shapeScaleY = new TextField(screen, "shapeScaleY", Layout.pos, Layout.dim);
		shapeScaleY.setType(TextField.Type.NUMERIC);
		shapeScaleY.setText("1.0");
		ic.addChild(shapeScaleY);
		form.addFormElement(shapeScaleY);
		
		Layout.incCol(shapeScaleY);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lShapeScaleZ = Layout.getNewLabel(screen, "Z: ");
		ic.addChild(lShapeScaleZ);
		
		Layout.incCol(lShapeScaleZ);
		Layout.dim.set(Layout.floatW,Layout.h);
		shapeScaleZ = new TextField(screen, "shapeScaleZ", Layout.pos, Layout.dim);
		shapeScaleZ.setType(TextField.Type.NUMERIC);
		shapeScaleZ.setText("1.0");
		ic.addChild(shapeScaleZ);
		form.addFormElement(shapeScaleZ);
		
		Layout.incCol(shapeScaleZ);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyShapeScale = new ButtonAdapter(screen, "applyShapeScale", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.emitter != null) {
					if (builder.validateFloatValue(shapeScaleX.getText()) &&
						builder.validateFloatValue(shapeScaleY.getText()) &&
						builder.validateFloatValue(shapeScaleZ.getText())) {
						builder.emitter.getEmitter().setLocalScale(
							Float.valueOf(shapeScaleX.getText()),
							Float.valueOf(shapeScaleY.getText()),
							Float.valueOf(shapeScaleZ.getText())
						);
					}
				}
			}
		};
		applyShapeScale.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyShapeScale);
		form.addFormElement(applyShapeScale);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lShapeRotAxisX = Layout.getNewLabel(screen, "Rotation X: ");
		ic.addChild(lShapeRotAxisX);
		
		Layout.incCol(lShapeRotAxisX);
		Layout.dim.set(Layout.floatW,Layout.h);
		shapeRotAngleX = new TextField(screen, "shapeRotAngleX", Layout.pos, Layout.dim);
		shapeRotAngleX.setType(TextField.Type.NUMERIC);
		shapeRotAngleX.setText("0.0");
		ic.addChild(shapeRotAngleX);
		form.addFormElement(shapeRotAngleX);
		
		Layout.incCol(shapeRotAngleX);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lShapeRotAxisY = Layout.getNewLabel(screen, "Y: ");
		ic.addChild(lShapeRotAxisY);
		
		Layout.incCol(lShapeRotAxisY);
		Layout.dim.set(Layout.floatW,Layout.h);
		shapeRotAngleY = new TextField(screen, "shapeRotAngleY", Layout.pos, Layout.dim);
		shapeRotAngleY.setType(TextField.Type.NUMERIC);
		shapeRotAngleY.setText("0.0");
		ic.addChild(shapeRotAngleY);
		form.addFormElement(shapeRotAngleY);
		
		Layout.incCol(shapeRotAngleY);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lShapeRotAxisZ = Layout.getNewLabel(screen, "Z: ");
		ic.addChild(lShapeRotAxisZ);
		
		Layout.incCol(lShapeRotAxisZ);
		Layout.dim.set(Layout.floatW,Layout.h);
		shapeRotAngleZ = new TextField(screen, "shapeRotAngleZ", Layout.pos, Layout.dim);
		shapeRotAngleZ.setType(TextField.Type.NUMERIC);
		shapeRotAngleZ.setText("0.0");
		ic.addChild(shapeRotAngleZ);
		form.addFormElement(shapeRotAngleZ);
		
		Layout.incCol(shapeRotAngleZ);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyShapeRot = new ButtonAdapter(screen, "applyShapeRot", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.emitter != null) {
					if (builder.validateFloatValue(shapeRotAngleX.getText()) &&
						builder.validateFloatValue(shapeRotAngleY.getText()) &&
						builder.validateFloatValue(shapeRotAngleZ.getText())) {
						Vector3f axis = new Vector3f();
						Quaternion q = new Quaternion().fromAngles(
							Float.valueOf(shapeRotAngleX.getText())*FastMath.DEG_TO_RAD,
							Float.valueOf(shapeRotAngleY.getText())*FastMath.DEG_TO_RAD,
							Float.valueOf(shapeRotAngleZ.getText())*FastMath.DEG_TO_RAD
						);
						builder.emitter.getEmitter().setLocalRotation(q);
					}
				}
			}
		};
		applyShapeRot.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyShapeRot);
		form.addFormElement(applyShapeRot);
		
		ic.sizeToContent();
		
		createWindow("");
	}

	@Override
	public void loadProperties() {
		win.setWindowTitle("Emitter Shape Properties (" + builder.emitter.getEmitter().getName() + ")");
		updateLoop.setIsChecked(builder.emitter.getEmitter().isEnabled());
		shapeTest.setIsChecked(builder.emitter.getEmitter().getEmitterTestModeShape());
		shapeType.setSelectedByCaption(builder.emitter.getShapeType(), true);
		directionType.setSelectedByCaption(builder.emitter.getEmitter().getDirectionType().name(), true);
		randPoint.setIsChecked(builder.emitter.getEmitter().getUseRandomEmissionPoint());
		seqEmFace.setIsChecked(builder.emitter.getEmitter().getUseSequentialEmissionFace());
		skipPattern.setIsChecked(builder.emitter.getEmitter().getUseSequentialSkipPattern());
		numParticles.setText(String.valueOf(builder.emitter.getEmitter().getMaxParticles()));
		emPerSec.setText(String.valueOf(builder.emitter.getEmitter().getEmissionsPerSecond()));
		partPerEm.setText(String.valueOf(builder.emitter.getEmitter().getParticlesPerEmission()));
		Vector3f ePos = builder.emitter.getEmitter().getLocalTranslation();
		Vector3f eScale = builder.emitter.getEmitter().getLocalScale();
		Quaternion q = builder.emitter.getEmitter().getLocalRotation();
		float[] angles = new float[3];
		q.toAngles(angles);
		shapePosX.setText(String.valueOf(ePos.getX()));
		shapePosY.setText(String.valueOf(ePos.getY()));
		shapePosZ.setText(String.valueOf(ePos.getZ()));
		shapeScaleX.setText(String.valueOf(eScale.getX()));
		shapeScaleY.setText(String.valueOf(eScale.getY()));
		shapeScaleZ.setText(String.valueOf(eScale.getZ()));
		shapeRotAngleX.setText(String.valueOf(angles[0]*FastMath.RAD_TO_DEG));
		shapeRotAngleY.setText(String.valueOf(angles[1]*FastMath.RAD_TO_DEG));
		shapeRotAngleZ.setText(String.valueOf(angles[2]*FastMath.RAD_TO_DEG));
	}
	
}
