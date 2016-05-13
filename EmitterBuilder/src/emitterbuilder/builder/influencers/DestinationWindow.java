/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package emitterbuilder.builder.influencers;

import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector3f;
import emitter.Interpolation;
import emitter.influencers.DestinationInfluencer;
import emitter.influencers.ParticleInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.DestStruct;
import emitterbuilder.gui.InfluencerWindow;
import emitterbuilder.gui.SelectListEditor;
import emitterbuilder.gui.utils.InterpolationUtil;
import emitterbuilder.gui.utils.Layout;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.lists.SelectList;
import tonegod.gui.controls.lists.SelectList.ListItem;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class DestinationWindow extends InfluencerWindow {
	TextField destX, destY, destZ, destWeight, destDur;
	SelectBox destIn;
	SelectListEditor destList;
	ButtonAdapter destUpdate, destEdit, destAdd, destCancel;
	CheckBox destRandom, destEnabled;
	
	public DestinationWindow(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lDestX = Layout.getNewLabel(screen, "Destination X: ");
		ic.addChild(lDestX);
		
		Layout.incCol(lDestX);
		Layout.dim.set(Layout.floatW,Layout.h);
		destX = new TextField(screen, "destX", Layout.pos, Layout.dim);
		destX.setType(TextField.Type.NUMERIC);
		ic.addChild(destX);
		form.addFormElement(destX);
		
		Layout.incCol(destX);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lDestY = Layout.getNewLabel(screen, "Y: ");
		ic.addChild(lDestY);
		
		Layout.incCol(lDestY);
		Layout.dim.set(Layout.floatW,Layout.h);
		destY = new TextField(screen, "destY", Layout.pos, Layout.dim);
		destY.setType(TextField.Type.NUMERIC);
		ic.addChild(destY);
		form.addFormElement(destY);
		
		Layout.incCol(destY);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lDestZ = Layout.getNewLabel(screen, "Z: ");
		ic.addChild(lDestZ);
		
		Layout.incCol(lDestZ);
		Layout.dim.set(Layout.floatW,Layout.h);
		destZ = new TextField(screen, "destZ", Layout.pos, Layout.dim);
		destZ.setType(TextField.Type.NUMERIC);
		ic.addChild(destZ);
		form.addFormElement(destZ);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lDestWeight = Layout.getNewLabel(screen, "Weight: ");
		ic.addChild(lDestWeight);
		
		Layout.incCol(lDestWeight);
		Layout.dim.set(Layout.floatW,Layout.h);
		destWeight = new TextField(screen, "destWeight", Layout.pos, Layout.dim);
		destWeight.setType(TextField.Type.NUMERIC);
		ic.addChild(destWeight);
		form.addFormElement(destWeight);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lDestIn = Layout.getNewLabel(screen, "Interpolation: ");
		ic.addChild(lDestIn);
		
		Layout.incCol(lDestIn);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		destIn = new SelectBox(screen, "destIn", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				
			}
		};
		InterpolationUtil.populateInterpolations(destIn);
		destIn.setSelectedByCaption("linear", false);
		ic.addChild(destIn);
		form.addFormElement(destIn);
		
		Layout.incCol(destIn);
		Layout.x += Layout.h;
		Layout.pos.setX(Layout.x);
		Layout.dim.set(Layout.bWidth,Layout.h);
		destCancel = new ButtonAdapter(screen, "destCancel", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				destAdd.show();
				destEdit.hide();
				destCancel.hide();
			}
		};
		destCancel.setText("Cancel");
		ic.addChild(destCancel);
		form.addFormElement(destCancel);
		
		Layout.incCol(destCancel);
	//	Layout.pos.set(colorA.getX()+colorA.getWidth()-Layout.bWidth,y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		destEdit = new ButtonAdapter(screen, "destEdit", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				updateDestination();
			}
		};
		destEdit.setText("Update");
		ic.addChild(destEdit);
		form.addFormElement(destEdit);
		
		Layout.pos.set(destEdit.getX(),Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		destAdd = new ButtonAdapter(screen, "destAdd", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				addDestination();
			}
		};
		destAdd.setText("Add");
		ic.addChild(destAdd);
		form.addFormElement(destAdd);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lDestList = Layout.getNewLabel(screen, "Current Destinations: ");
		ic.addChild(lDestList);
		
		Layout.incCol(lDestList);
		Layout.dim.set(400,Layout.h*4);
		destList = new SelectListEditor(screen, Layout.pos, Layout.dim) {
			@Override
			public void onEditSelectedItem(int index, ListItem updated) {
				if (!destList.getSelectList().getSelectedListItems().isEmpty()) {
					destAdd.hide();
					destEdit.show();
					destCancel.show();
					DestStruct struct = (DestStruct)destList.getSelectList().getSelectedListItems().get(0).getValue();
					destX.setText(String.valueOf(struct.getDestination().x));
					destY.setText(String.valueOf(struct.getDestination().y));
					destZ.setText(String.valueOf(struct.getDestination().z));
					destWeight.setText(String.valueOf(struct.getWeight()));
					destIn.setSelectedByCaption(InterpolationUtil.getInterpolationName(struct.getInterpolation()), false);
				}
			}
			@Override
			public void onRemoveSelectedItem(int index, ListItem removed) {
				
			}
			@Override
			public void onSelectListUpdate(List<SelectList.ListItem> items) {
				
			}
		};
		ic.addChild(destList);
		form.addFormElement(destList.items);
		
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		destUpdate = new ButtonAdapter(screen, "destUpdate", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				updateDestinationInfluencer();
			}
		};
		destUpdate.setText("Apply List");
		ic.addChild(destUpdate);
		form.addFormElement(destUpdate);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lDestRandom = Layout.getNewLabel(screen, "Random Start Destination: ");
		ic.addChild(lDestRandom);
		
		Layout.incCol(lDestRandom);
		Layout.dim.set(Layout.h,Layout.h);
		destRandom = new CheckBox(screen, "destRandom", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				DestinationInfluencer di = builder.emitter.getEmitter().getInfluencer(DestinationInfluencer.class);
				di.setUseRandomStartDestination(toggled);
			}
		};
		ic.addChild(destRandom);
		form.addFormElement(destRandom);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lDestEnabled = Layout.getNewLabel(screen, "Enabled: ");
		ic.addChild(lDestEnabled);
		
		Layout.incCol(lDestEnabled);
		Layout.dim.set(Layout.h,Layout.h);
		destEnabled = new CheckBox(screen, "destEnabled", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				DestinationInfluencer di = builder.emitter.getEmitter().getInfluencer(DestinationInfluencer.class);
				di.setEnabled(toggled);
			}
		};
		ic.addChild(destEnabled);
		form.addFormElement(destEnabled);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lDestDur = Layout.getNewLabel(screen, "Set Fixed Duration: ");
		ic.addChild(lDestDur);
		
		Layout.incCol(lDestDur);
		Layout.dim.set(Layout.floatW,Layout.h);
		destDur = new TextField(screen, "destDur", Layout.pos, Layout.dim);
		destDur.setType(TextField.Type.NUMERIC);
		ic.addChild(destDur);
		form.addFormElement(destDur);
		
		Layout.incCol(destDur);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyDestDur = new ButtonAdapter(screen, "applyDestDur", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(destDur.getText())) {
					DestinationInfluencer di = builder.emitter.getEmitter().getInfluencer(DestinationInfluencer.class);
					di.setFixedDuration(
						Float.valueOf(destDur.getText())
					);
				}
			}
		};
		applyDestDur.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyDestDur);
		form.addFormElement(applyDestDur);
		
		Layout.incCol(applyDestDur);
		Layout.dim.set(300,Layout.h);
		Label lDestDurExp = Layout.getNewLabel(screen, "NOTE: A duration of 0 = fixed duration disabled.");
		lDestDurExp.setTextAlign(BitmapFont.Align.Left);
		ic.addChild(lDestDurExp);
		
		ic.sizeToContent();
		
		createWindow("Destination Influencer");
		
		destUpdate.setX(ic.getWidth()-destUpdate.getWidth());
		destAdd.setX(ic.getWidth()-destAdd.getWidth());
		destEdit.setX(ic.getWidth()-destEdit.getWidth());
		destCancel.setX(ic.getWidth()-(destCancel.getWidth()*2+Layout.pad));
	}
	
	private void updateDestinationInfluencer() {
		if (builder.emitter != null) {
			builder.startChangeState();
			
			DestinationInfluencer si = builder.emitter.getEmitter().getInfluencer(DestinationInfluencer.class);
			si.removeAll();
			for (SelectList.ListItem li : destList.items.getListItems()) {
				DestStruct struct = (DestStruct)li.getValue();
				si.addDestination(
					struct.getDestination(),
					struct.getWeight(),
					struct.getInterpolation()
				);
			}
			
			builder.resetChangeState();
			builder.endChangeState();
		}
	}
	private void addDestination() {
		if (builder.validateFloatValue(destX.getText()) &&
			builder.validateFloatValue(destY.getText()) &&
			builder.validateFloatValue(destZ.getText())) {
			Interpolation interp = InterpolationUtil.getInterpolationByIndex(
				(Integer)destIn.getSelectedListItem().getValue()
			);
			DestStruct struct = new DestStruct(
				new Vector3f(
					Float.valueOf(destX.getText()),
					Float.valueOf(destY.getText()),
					Float.valueOf(destZ.getText())
				),
				Float.valueOf(destWeight.getText()),
				interp
			);
			destList.items.addListItem(
				struct.getDestination().toString() + " : " + struct.getWeight() + " : " + destIn.getText(),
				struct
			);
		}
	}
	private void updateDestination() {
		if (!destList.getSelectList().getSelectedListItems().isEmpty()) {
			if (builder.validateFloatValue(destX.getText()) &&
				builder.validateFloatValue(destY.getText()) &&
				builder.validateFloatValue(destZ.getText())) {
				SelectList.ListItem item = destList.getSelectList().getSelectedListItems().get(0);
				DestStruct struct = (DestStruct)item.getValue();
				struct.setDestination(
					Float.valueOf(destX.getText()),
					Float.valueOf(destY.getText()),
					Float.valueOf(destZ.getText()));
				struct.setWeight(Float.valueOf(destWeight.getText()));
				struct.setInterpolation(
					InterpolationUtil.getInterpolationByIndex(
						(Integer)destIn.getSelectedListItem().getValue()
					)
				);
				int index = destList.getSelectList().getSelectedIndex();
				if (index < destList.getSelectList().getListItems().size()-1) {
					destList.getSelectList().removeListItem(index);
					destList.getSelectList().insertListItem(
						index,
						struct.getDestination().toString() + " : " + struct.getWeight() + " : " + destIn.getText(),
						struct);
					destList.getSelectList().setSelectedIndex(index);
				} else if (index == destList.getSelectList().getListItems().size()-1) {
					destList.getSelectList().removeListItem(index);
					destList.getSelectList().addListItem(
						struct.getDestination().toString() + " : " + struct.getWeight() + " : " + destIn.getText(),
						struct);
					destList.getSelectList().setSelectedIndex(index);
				}
				destEdit.hide();
				destCancel.hide();
				destAdd.show();
			}
		}
	}
	
	@Override
	public void loadProperties(ParticleInfluencer influencer) {
		DestinationInfluencer inf = (DestinationInfluencer)influencer;
		
		destList.getSelectList().removeAllListItems();
		int index = 0;
		for (Vector3f dest : inf.getDestinations()) {
			Interpolation interp = inf.getInterpolations()[index];
			float weight = inf.getWeights()[index];
			DestStruct struct = new DestStruct(
				dest,
				weight,
				interp
			);
			destList.getSelectList().addListItem(
				struct.getDestination().toString() + " : " + struct.getWeight() + " : " + InterpolationUtil.getInterpolationName(struct.getInterpolation()),
				struct);
			index++;
		}
		destRandom.setIsChecked(inf.getUseRandomStartDestination());
		destEnabled.setIsChecked(inf.isEnabled());
		destDur.setText(String.valueOf(inf.getFixedDuration()));
	}

	@Override
	public void showGUIWindow() {
		builder.getApplication().getStateManager().attach(this);
		destEdit.hide();
		destCancel.hide();
	}
	
}
