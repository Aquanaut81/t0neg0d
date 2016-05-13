package emitterbuilder.builder.influencers;

import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ParticleInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.AlphaStruct;
import emitterbuilder.gui.InfluencerWindow;
import emitterbuilder.gui.utils.Layout;
import emitterbuilder.gui.SelectListEditor;
import emitterbuilder.gui.utils.InterpolationUtil;
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
public class AlphaWindow extends InfluencerWindow {
	TextField alpha, alphaDur;
	SelectBox alphaIn;
	SelectListEditor alphaList;
	CheckBox alphaEnabled, sizeRandom;
	ButtonAdapter alphaCancel, alphaEdit, alphaAdd, alphaUpdate;
	
	public AlphaWindow(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lAlpha = Layout.getNewLabel(screen, "Alpha Value: ");
		ic.addChild(lAlpha);
		
		// Size X TextField
		Layout.incCol(lAlpha);
		Layout.dim.set(Layout.floatW,Layout.h);
		alpha = new TextField(screen, "alpha", Layout.pos, Layout.dim);
		alpha.setType(TextField.Type.NUMERIC);
		ic.addChild(alpha);
		form.addFormElement(alpha);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lAlphaIn = Layout.getNewLabel(screen, "Interpolation: ");
		ic.addChild(lAlphaIn);
		
		Layout.incCol(lAlphaIn);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		alphaIn = new SelectBox(screen, "alphaIn", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				
			}
		};
		InterpolationUtil.populateInterpolations(alphaIn);
		alphaIn.setSelectedByCaption("linear", false);
		ic.addChild(alphaIn);
		form.addFormElement(alphaIn);
		
		Layout.incCol(alphaIn);
		Layout.x += Layout.h;
		Layout.pos.setX(Layout.x);
		Layout.dim.set(Layout.bWidth,Layout.h);
		alphaCancel = new ButtonAdapter(screen, "alphaCancel", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				alphaAdd.show();
				alphaEdit.hide();
				alphaCancel.hide();
			}
		};
		alphaCancel.setText("Cancel");
		ic.addChild(alphaCancel);
		form.addFormElement(alphaCancel);
		
		Layout.incCol(alphaCancel);
		Layout.dim.set(Layout.bWidth,Layout.h );
		alphaEdit = new ButtonAdapter(screen, "alphaEdit", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				updateAlpha();
			}
		};
		alphaEdit.setText("Update");
		ic.addChild(alphaEdit);
		form.addFormElement(alphaEdit);
		
		Layout.pos.set(alphaEdit.getX(),Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h );
		alphaAdd = new ButtonAdapter(screen, "alphaAdd", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				addAlpha();
			}
		};
		alphaAdd.setText("Add");
		ic.addChild(alphaAdd);
		form.addFormElement(alphaAdd);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h );
		Label lAlphaList = Layout.getNewLabel(screen, "Current Alpha Values: ");
		ic.addChild(lAlphaList);
		
		Layout.incCol(lAlphaList);
		Layout.dim.set(400,Layout.h *4);
		alphaList = new SelectListEditor(screen, Layout.pos, Layout.dim) {
			@Override
			public void onEditSelectedItem(int index, ListItem updated) {
				if (!alphaList.getSelectList().getSelectedListItems().isEmpty()) {
					alphaAdd.hide();
					alphaEdit.show();
					alphaCancel.show();
					AlphaStruct struct = (AlphaStruct)alphaList.getSelectList().getSelectedListItems().get(0).getValue();
					alpha.setText(String.valueOf(struct.getAlpha()));
					alphaIn.setSelectedByCaption(
						InterpolationUtil.getInterpolationName(struct.getInterpolation()),
						false
					);
				}
			}
			@Override
			public void onRemoveSelectedItem(int index, ListItem removed) {  }
			@Override
			public void onSelectListUpdate(List<ListItem> items) {  }
		};
		ic.addChild(alphaList);
		form.addFormElement(alphaList.items);
		
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h );
		alphaUpdate = new ButtonAdapter(screen, "alphaUpdate", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				updateAlphaInfluencer();
			}
		};
		alphaUpdate.setText("Apply List");
		ic.addChild(alphaUpdate);
		form.addFormElement(alphaUpdate);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lAlphaEnabled = Layout.getNewLabel(screen, "Enabled: ");
		ic.addChild(lAlphaEnabled);
		
		Layout.incCol(lAlphaEnabled);
		Layout.dim.set(Layout.h,Layout.h );
		alphaEnabled = new CheckBox(screen, "alphaEnabled", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				AlphaInfluencer ai = builder.emitter.getEmitter().getInfluencer(AlphaInfluencer.class);
				ai.setEnabled(toggled);
			}
		};
		ic.addChild(alphaEnabled);
		form.addFormElement(alphaEnabled);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lAlphaDur = Layout.getNewLabel(screen, "Set Fixed Duration: ");
		ic.addChild(lAlphaDur);
		
		Layout.incCol(lAlphaDur);
		Layout.dim.set(Layout.floatW,Layout.h);
		alphaDur = new TextField(screen, "alphaDur", Layout.pos, Layout.dim);
		alphaDur.setType(TextField.Type.NUMERIC);
		ic.addChild(alphaDur);
		form.addFormElement(alphaDur);
		
		Layout.incCol(alphaDur);
		Layout.dim.set(Layout.h,Layout.h );
		ButtonAdapter applyAlphaDur = new ButtonAdapter(screen, "applyAlphaDur", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(alphaDur.getText())) {
					AlphaInfluencer ai = builder.emitter.getEmitter().getInfluencer(AlphaInfluencer.class);
					ai.setFixedDuration(
						Float.valueOf(alphaDur.getText())
					);
				}
			}
		};
		applyAlphaDur.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyAlphaDur);
		form.addFormElement(applyAlphaDur);
		
		Layout.incCol(applyAlphaDur);
		Layout.dim.set(300,Layout.h);
		Label lAlphaDurExp = Layout.getNewLabel(screen, "NOTE: A duration of 0 = fixed duration disabled.");
		lAlphaDurExp.setTextAlign(BitmapFont.Align.Left);
		ic.addChild(lAlphaDurExp);
		
		ic.sizeToContent();
		
		createWindow("Alpha Influencer");
		
		alphaUpdate.setX(ic.getWidth()-alphaUpdate.getWidth());
		alphaAdd.setX(ic.getWidth()-alphaAdd.getWidth());
		alphaEdit.setX(ic.getWidth()-alphaEdit.getWidth());
		alphaCancel.setX(ic.getWidth()-(alphaCancel.getWidth()*2+Layout.pad));
	}
	
	private void updateAlphaInfluencer() {
		if (builder.emitter != null) {
			builder.startChangeState();
			
			AlphaInfluencer ai = builder.emitter.getEmitter().getInfluencer(AlphaInfluencer.class);
			ai.removeAll();
			for (ListItem li : alphaList.items.getListItems()) {
				AlphaStruct cs = (AlphaStruct)li.getValue();
				ai.addAlpha(cs.getAlpha(),cs.getInterpolation());
			}
			
			builder.resetChangeState();
			builder.endChangeState();
		}
	}
	private void addAlpha() {
		if (builder.validateFloatValue(alpha.getText())) {
			Interpolation interp = InterpolationUtil.getInterpolationByIndex(
				(Integer)alphaIn.getSelectedListItem().getValue()
			);
			AlphaStruct a = new AlphaStruct(
				Float.valueOf(alpha.getText()),
				interp
			);
			alphaList.items.addListItem(
				String.valueOf(a.getAlpha()) + " : " + alphaIn.getText(),
				a
			);
			alphaList.items.setSelectedIndex(alphaList.items.getListItems().size()-1);
			alphaList.scrollToSelected();
		}
	}
	private void updateAlpha() {
		if (!alphaList.getSelectList().getSelectedListItems().isEmpty()) {
			if (builder.validateFloatValue(alpha.getText())) {
				ListItem item = alphaList.getSelectList().getSelectedListItems().get(0);
				AlphaStruct struct = (AlphaStruct)item.getValue();
				struct.setAlpha(Float.valueOf(alpha.getText()));
				struct.setInterpolation(
					InterpolationUtil.getInterpolationByIndex(
						(Integer)alphaIn.getSelectedListItem().getValue()
					)
				);
				int index = alphaList.getSelectList().getSelectedIndex();
				if (index < alphaList.getSelectList().getListItems().size()-1) {
					alphaList.getSelectList().removeListItem(index);
					alphaList.getSelectList().insertListItem(
						index,
						String.valueOf(struct.getAlpha()) + " : " + alphaIn.getText(),
						struct);
					alphaList.getSelectList().setSelectedIndex(index);
					alphaList.scrollToSelected();
				} else if (index == alphaList.getSelectList().getListItems().size()-1) {
					alphaList.getSelectList().removeListItem(index);
					alphaList.getSelectList().addListItem(
						String.valueOf(struct.getAlpha()) + " : " + alphaIn.getText(),
						struct);
					alphaList.getSelectList().setSelectedIndex(index);
					alphaList.scrollToSelected();
				}
				alphaEdit.hide();
				alphaCancel.hide();
				alphaAdd.show();
			}
		}
	}
	
	@Override
	public void loadProperties(ParticleInfluencer influencer) {
		AlphaInfluencer inf = (AlphaInfluencer)influencer;
		
		alphaList.getSelectList().removeAllListItems();
		int index = 0;
		for (Float alp : inf.getAlphas()) {
			Interpolation interp = inf.getInterpolations()[index];
			AlphaStruct struct = new AlphaStruct(
				alp,
				interp
			);
			alphaList.getSelectList().addListItem(
				String.valueOf(struct.getAlpha()) + " : " + InterpolationUtil.getInterpolationName(struct.getInterpolation()),
				struct);
			index++;
		}
		alphaEnabled.setIsChecked(inf.isEnabled());
		alphaDur.setText(String.valueOf(inf.getFixedDuration()));
	}

	@Override
	public void showGUIWindow() {
		builder.getApplication().getStateManager().attach(this);
		alphaEdit.hide();
		alphaCancel.hide();
	}
}
