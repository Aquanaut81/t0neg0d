package emitterbuilder.builder.influencers;

import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector3f;
import emitter.Interpolation;
import emitter.influencers.ParticleInfluencer;
import emitter.influencers.RotationInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.RotationStruct;
import emitterbuilder.gui.InfluencerWindow;
import emitterbuilder.gui.SelectListEditor;
import emitterbuilder.gui.utils.InterpolationUtil;
import emitterbuilder.gui.utils.Layout;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.lists.SelectList.ListItem;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class RotationWindow extends InfluencerWindow {
	TextField rotX, rotY, rotZ, rotDur;
	SelectBox rotIn;
	SelectListEditor rotList;
	ButtonAdapter rotCancel, rotEdit, rotAdd, rotUpdate;
	CheckBox rotRandomX, rotRandomY, rotRandomZ, rotEnabled, rotSpeedRandom, rotDirRandom;
	
	public RotationWindow(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRotX = Layout.getNewLabel(screen, "Rotation Speed X: ");
		ic.addChild(lRotX);
		
		// Size X TextField
		Layout.incCol(lRotX);
		Layout.dim.set(Layout.floatW,Layout.h);
		rotX = new TextField(screen, "rotX", Layout.pos, Layout.dim);
		rotX.setType(TextField.Type.NUMERIC);
		ic.addChild(rotX);
		form.addFormElement(rotX);
		
		Layout.incCol(rotX);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lRotY = Layout.getNewLabel(screen, "Y: ");
		ic.addChild(lRotY);
		
		Layout.incCol(lRotY);
		Layout.dim.set(Layout.floatW,Layout.h);
		rotY = new TextField(screen, "rotY", Layout.pos, Layout.dim);
		rotY.setType(TextField.Type.NUMERIC);
		ic.addChild(rotY);
		form.addFormElement(rotY);
		
		Layout.incCol(rotY);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lRotZ = Layout.getNewLabel(screen, "Z: ");
		ic.addChild(lRotZ);
		
		Layout.incCol(lRotZ);
		Layout.dim.set(Layout.floatW,Layout.h);
		rotZ = new TextField(screen, "rotZ", Layout.pos, Layout.dim);
		rotZ.setType(TextField.Type.NUMERIC);
		ic.addChild(rotZ);
		form.addFormElement(rotZ);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRotIn = Layout.getNewLabel(screen, "Interpolation: ");
		ic.addChild(lRotIn);
		
		Layout.incCol(lRotIn);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		rotIn = new SelectBox(screen, "rotIn", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				
			}
		};
		InterpolationUtil.populateInterpolations(rotIn);
		rotIn.setSelectedByCaption("linear", false);
		ic.addChild(rotIn);
		form.addFormElement(rotIn);
		
		Layout.incCol(rotIn);
		Layout.x += Layout.h;
		Layout.pos.setX(Layout.x);
	//	Layout.pos.set(colorA.getX()+colorA.getWidth()-(Layout.bWidth*2)-7,y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		rotCancel = new ButtonAdapter(screen, "rotCancel", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				rotAdd.show();
				rotEdit.hide();
				rotCancel.hide();
			}
		};
		rotCancel.setText("Cancel");
		ic.addChild(rotCancel);
		form.addFormElement(rotCancel);
		
		Layout.incCol(rotCancel);
	//	Layout.pos.set(colorA.getX()+colorA.getWidth()-Layout.bWidth,y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		rotEdit = new ButtonAdapter(screen, "rotEdit", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				updateRotation();
			}
		};
		rotEdit.setText("Update");
		ic.addChild(rotEdit);
		form.addFormElement(rotEdit);
		
		Layout.pos.set(rotEdit.getX(),Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		rotAdd = new ButtonAdapter(screen, "rotAdd", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				addRotation();
			}
		};
		rotAdd.setText("Add");
		ic.addChild(rotAdd);
		form.addFormElement(rotAdd);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRotList = Layout.getNewLabel(screen, "Current Speeds: ");
		ic.addChild(lRotList);
		
		Layout.incCol(lRotList);
		Layout.dim.set(400,Layout.h*4);
		rotList = new SelectListEditor(screen, Layout.pos, Layout.dim) {
			@Override
			public void onEditSelectedItem(int index, ListItem updated) {
				if (!rotList.getSelectList().getSelectedListItems().isEmpty()) {
					rotAdd.hide();
					rotEdit.show();
					rotCancel.show();
					RotationStruct struct = (RotationStruct)rotList.getSelectList().getSelectedListItems().get(0).getValue();
					rotX.setText(String.valueOf(struct.getRotation().x));
					rotY.setText(String.valueOf(struct.getRotation().y));
					rotZ.setText(String.valueOf(struct.getRotation().z));
					rotIn.setSelectedByCaption(InterpolationUtil.getInterpolationName(struct.getInterpolation()), false);
				}
			}
			@Override
			public void onRemoveSelectedItem(int index, ListItem removed) {
				
			}
			@Override
			public void onSelectListUpdate(List<ListItem> items) {
				
			}
		};
		ic.addChild(rotList);
		form.addFormElement(rotList.items);
		
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		rotUpdate = new ButtonAdapter(screen, "rotUpdate", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				updateRotationInfluencer();
			}
		};
		rotUpdate.setText("Apply List");
		ic.addChild(rotUpdate);
		form.addFormElement(rotUpdate);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRotRandomX = Layout.getNewLabel(screen, "Random Start Rot X: ");
		ic.addChild(lRotRandomX);
		
		Layout.incCol(lRotRandomX);
		Layout.dim.set(Layout.h,Layout.h);
		rotRandomX = new CheckBox(screen, "rotRandomX", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				RotationInfluencer ri = builder.emitter.getEmitter().getInfluencer(RotationInfluencer.class);
				ri.setUseRandomStartRotation(
					toggled,
					rotRandomY.getIsChecked(),
					rotRandomZ.getIsChecked()
				);
			}
		};
		ic.addChild(rotRandomX);
		form.addFormElement(rotRandomX);
		
		Layout.incCol(rotRandomX);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lRotRandomY = Layout.getNewLabel(screen, "Y: ");
		ic.addChild(lRotRandomY);
		
		Layout.incCol(lRotRandomY);
		Layout.dim.set(Layout.h,Layout.h);
		rotRandomY = new CheckBox(screen, "rotRandomY", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				RotationInfluencer ri = builder.emitter.getEmitter().getInfluencer(RotationInfluencer.class);
				ri.setUseRandomStartRotation(
					rotRandomX.getIsChecked(),
					toggled,
					rotRandomZ.getIsChecked()
				);
			}
		};
		ic.addChild(rotRandomY);
		form.addFormElement(rotRandomY);
		
		Layout.incCol(rotRandomY);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lRotRandomZ = Layout.getNewLabel(screen, "Z: ");
		ic.addChild(lRotRandomZ);
		
		Layout.incCol(lRotRandomZ);
		Layout.dim.set(Layout.h,Layout.h);
		rotRandomZ = new CheckBox(screen, "rotRandomZ", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				RotationInfluencer ri = builder.emitter.getEmitter().getInfluencer(RotationInfluencer.class);
				ri.setUseRandomStartRotation(
					rotRandomX.getIsChecked(),
					rotRandomY.getIsChecked(),
					toggled
				);
			}
		};
		ic.addChild(rotRandomZ);
		form.addFormElement(rotRandomZ);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRotSpeedRandom = Layout.getNewLabel(screen, "Random Speed: ");
		ic.addChild(lRotSpeedRandom);
		
		Layout.incCol(lRotSpeedRandom);
		Layout.dim.set(Layout.h,Layout.h);
		rotSpeedRandom = new CheckBox(screen, "rotSpeedRandom", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				RotationInfluencer ri = builder.emitter.getEmitter().getInfluencer(RotationInfluencer.class);
				ri.setUseRandomSpeed(toggled);
			}
		};
		ic.addChild(rotSpeedRandom);
		form.addFormElement(rotSpeedRandom);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRotDirRandom = Layout.getNewLabel(screen, "Random Directions: ");
		ic.addChild(lRotDirRandom);
		
		Layout.incCol(lRotDirRandom);
		Layout.dim.set(Layout.h,Layout.h);
		rotDirRandom = new CheckBox(screen, "rotDirRandom", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				RotationInfluencer ri = builder.emitter.getEmitter().getInfluencer(RotationInfluencer.class);
				ri.setUseRandomDirection(toggled);
			}
		};
		ic.addChild(rotDirRandom);
		form.addFormElement(rotDirRandom);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRotEnabled = Layout.getNewLabel(screen, "Enabled: ");
		ic.addChild(lRotEnabled);
		
		Layout.incCol(lRotEnabled);
		Layout.dim.set(Layout.h,Layout.h);
		rotEnabled = new CheckBox(screen, "rotEnabled", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				RotationInfluencer ri = builder.emitter.getEmitter().getInfluencer(RotationInfluencer.class);
				ri.setEnabled(toggled);
			}
		};
		ic.addChild(rotEnabled);
		form.addFormElement(rotEnabled);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lRotDur = Layout.getNewLabel(screen, "Set Fixed Duration: ");
		ic.addChild(lRotDur);
		
		Layout.incCol(lRotDur);
		Layout.dim.set(Layout.floatW,Layout.h);
		rotDur = new TextField(screen, "rotDur", Layout.pos, Layout.dim);
		rotDur.setType(TextField.Type.NUMERIC);
		ic.addChild(rotDur);
		form.addFormElement(rotDur);
		
		Layout.incCol(rotDur);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyRotDur = new ButtonAdapter(screen, "applyRotDur", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(rotDur.getText())) {
					RotationInfluencer ri = builder.emitter.getEmitter().getInfluencer(RotationInfluencer.class);
				//	inf.setFixedDuration(
				//		Float.valueOf(alphaDur.getText())
				//	);
				}
			}
		};
		applyRotDur.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyRotDur);
		form.addFormElement(applyRotDur);
		
		Layout.incCol(applyRotDur);
		Layout.dim.set(300,Layout.h);
		Label lRotDurExp = Layout.getNewLabel(screen, "NOTE: A duration of 0 = fixed duration disabled.");
		lRotDurExp.setTextAlign(BitmapFont.Align.Left);
		ic.addChild(lRotDurExp);
		
		ic.sizeToContent();
		
		createWindow("Rotation Influencer");
		
		rotUpdate.setX(ic.getWidth()-rotUpdate.getWidth());
		rotAdd.setX(ic.getWidth()-rotAdd.getWidth());
		rotEdit.setX(ic.getWidth()-rotEdit.getWidth());
		rotCancel.setX(ic.getWidth()-(rotCancel.getWidth()*2+Layout.pad));
	}
	
	private void updateRotationInfluencer() {
		if (builder.emitter != null) {
			builder.startChangeState();
			
			RotationInfluencer ri = builder.emitter.getEmitter().getInfluencer(RotationInfluencer.class);
			ri.removeAll();
			for (ListItem li : rotList.items.getListItems()) {
				RotationStruct cs = (RotationStruct)li.getValue();
				ri.addRotationSpeed(cs.getRotation(),cs.getInterpolation());
			}
			ri.setUseRandomStartRotation(
				rotRandomX.getIsChecked(),
				rotRandomY.getIsChecked(),
				rotRandomZ.getIsChecked()
			);
			ri.setUseRandomSpeed(rotSpeedRandom.getIsChecked());
			ri.setUseRandomDirection(rotDirRandom.getIsChecked());
			ri.setEnabled(rotEnabled.getIsChecked());
			
			builder.resetChangeState();
			builder.endChangeState();
		}
	}
	private void addRotation() {
		if (builder.validateFloatValue(rotX.getText()) &&
			builder.validateFloatValue(rotY.getText()) &&
			builder.validateFloatValue(rotZ.getText())) {
			Interpolation interp = InterpolationUtil.getInterpolationByIndex(
				(Integer)rotIn.getSelectedListItem().getValue()
			);
			RotationStruct rot = new RotationStruct(
				new Vector3f(
					Float.valueOf(rotX.getText()),
					Float.valueOf(rotY.getText()),
					Float.valueOf(rotZ.getText())),
				interp
			);
			rotList.items.addListItem(
				rot.getRotation().toString() + " : " + rotIn.getText(),
				rot
			);
		}
	}
	private void updateRotation() {
		if (!rotList.getSelectList().getSelectedListItems().isEmpty()) {
			if (builder.validateFloatValue(rotX.getText()) &&
				builder.validateFloatValue(rotY.getText()) &&
				builder.validateFloatValue(rotZ.getText())) {
				ListItem item = rotList.getSelectList().getSelectedListItems().get(0);
				RotationStruct struct = (RotationStruct)item.getValue();
				struct.setRotation(
					Float.valueOf(rotX.getText()),
					Float.valueOf(rotY.getText()),
					Float.valueOf(rotZ.getText()));
				struct.setInterpolation(
					InterpolationUtil.getInterpolationByIndex(
						(Integer)rotIn.getSelectedListItem().getValue()
					)
				);
				int index = rotList.getSelectList().getSelectedIndex();
				if (index < rotList.getSelectList().getListItems().size()-1) {
					rotList.getSelectList().removeListItem(index);
					rotList.getSelectList().insertListItem(
						index,
						struct.getRotation().toString() + " : " + rotIn.getText(),
						struct);
					rotList.getSelectList().setSelectedIndex(index);
				} else if (index < rotList.getSelectList().getListItems().size()-1) {
					rotList.getSelectList().removeListItem(index);
					rotList.getSelectList().addListItem(
						struct.getRotation().toString() + " : " + rotIn.getText(),
						struct);
					rotList.getSelectList().setSelectedIndex(index);
				}
				rotEdit.hide();
				rotCancel.hide();
				rotAdd.show();
			}
		}
	}
	
	@Override
	public void loadProperties(ParticleInfluencer influencer) {
		RotationInfluencer inf = (RotationInfluencer)influencer;
		
		rotList.getSelectList().removeAllListItems();
		int index = 0;
		for (Vector3f rot : inf.getRotations()) {
			Interpolation interp = inf.getInterpolations()[index];
			RotationStruct struct = new RotationStruct(
				rot,
				interp
			);
			rotList.getSelectList().addListItem(
				struct.getRotation().toString() + " : " + InterpolationUtil.getInterpolationName(struct.getInterpolation()),
				struct);
			index++;
		}
		rotRandomX.setIsChecked(inf.getUseRandomStartRotationX());
		rotRandomY.setIsChecked(inf.getUseRandomStartRotationY());
		rotRandomZ.setIsChecked(inf.getUseRandomStartRotationZ());
		rotSpeedRandom.setIsChecked(inf.getUseRandomSpeed());
		rotDirRandom.setIsChecked(inf.getUseRandomDirection());
		rotEnabled.setIsChecked(inf.isEnabled());
	}

	@Override
	public void showGUIWindow() {
		builder.getApplication().getStateManager().attach(this);
		rotEdit.hide();
		rotCancel.hide();
	}
}
