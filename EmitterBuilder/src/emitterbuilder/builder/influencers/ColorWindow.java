package emitterbuilder.builder.influencers;

import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.ColorRGBA;
import emitter.Interpolation;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.ParticleInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.ColorStruct;
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
public class ColorWindow extends InfluencerWindow {
	TextField colorR, colorG, colorB, colorA, colorDur;
	SelectBox colorIn;
	ButtonAdapter colorCancel, colorEdit, colorAdd, colorUpdate;
	SelectListEditor colorList;
	CheckBox colorRandom, colorEnabled;
	
	public ColorWindow(EmitterBuilder builder, Screen screen) {
		super(builder,screen);
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lColorR = Layout.getNewLabel(screen, "Color R: ");
		ic.addChild(lColorR);
		
		// GravityWindow X TextField
		Layout.incCol(lColorR);
		Layout.dim.set(Layout.floatW,Layout.h);
		colorR = new TextField(screen, "colorR", Layout.pos, Layout.dim);
		colorR.setType(TextField.Type.NUMERIC);
		ic.addChild(colorR);
		form.addFormElement(colorR);
		
		Layout.incCol(colorR);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lColorG = Layout.getNewLabel(screen, "G: ");
		ic.addChild(lColorG);
		
		Layout.incCol(lColorG);
		Layout.dim.set(Layout.floatW,Layout.h);
		colorG = new TextField(screen, "colorG", Layout.pos, Layout.dim);
		colorG.setType(TextField.Type.NUMERIC);
		ic.addChild(colorG);
		form.addFormElement(colorG);
		
		Layout.incCol(colorG);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lColorB = Layout.getNewLabel(screen, "B: ");
		ic.addChild(lColorB);
		
		Layout.incCol(lColorB);
		Layout.dim.set(Layout.floatW,Layout.h);
		colorB = new TextField(screen, "colorB", Layout.pos, Layout.dim);
		colorB.setType(TextField.Type.NUMERIC);
		ic.addChild(colorB);
		form.addFormElement(colorB);
		
		Layout.incCol(colorB);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lColorA = Layout.getNewLabel(screen, "A: ");
		ic.addChild(lColorA);
		
		Layout.incCol(lColorA);
		Layout.dim.set(Layout.floatW,Layout.h);
		colorA = new TextField(screen, "colorA", Layout.pos, Layout.dim);
		colorA.setType(TextField.Type.NUMERIC);
		ic.addChild(colorA);
		form.addFormElement(colorA);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lColorIn = Layout.getNewLabel(screen, "Interpolation: ");
		ic.addChild(lColorIn);
		
		Layout.incCol(lColorIn);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		colorIn = new SelectBox(screen, "colorIn", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				
			}
		};
		InterpolationUtil.populateInterpolations(colorIn);
		colorIn.setSelectedByCaption("linear", false);
		ic.addChild(colorIn);
		form.addFormElement(colorIn);
		
		Layout.pos.set(colorA.getX()+colorA.getWidth()-(Layout.bWidth*2)-7,Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		colorCancel = new ButtonAdapter(screen, "colorCancel", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				colorAdd.show();
				colorEdit.hide();
				colorCancel.hide();
			}
		};
		colorCancel.setText("Cancel");
		ic.addChild(colorCancel);
		form.addFormElement(colorCancel);
		
		Layout.pos.set(colorA.getX()+colorA.getWidth()-Layout.bWidth,Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		colorEdit = new ButtonAdapter(screen, "colorEdit", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				updateColor();
			}
		};
		colorEdit.setText("Update");
		ic.addChild(colorEdit);
		form.addFormElement(colorEdit);
	//	colorEdit.hide();
		
		Layout.pos.set(colorA.getX()+colorA.getWidth()-Layout.bWidth,Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		colorAdd = new ButtonAdapter(screen, "colorAdd", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				addColor();
			}
		};
		colorAdd.setText("Add");
		ic.addChild(colorAdd);
		form.addFormElement(colorAdd);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lColorList = Layout.getNewLabel(screen, "Current Colors: ");
		ic.addChild(lColorList);
		
		Layout.incCol(lColorList);
		Layout.dim.set(400,Layout.h*4);
		colorList = new SelectListEditor(screen, Layout.pos, Layout.dim) {
			@Override
			public void onEditSelectedItem(int index, ListItem updated) {
				if (!colorList.getSelectList().getSelectedListItems().isEmpty()) {
					colorAdd.hide();
					colorEdit.show();
					colorCancel.show();
					ColorStruct struct = (ColorStruct)colorList.getSelectList().getSelectedListItems().get(0).getValue();
					colorR.setText(String.valueOf(struct.getColor().r));
					colorG.setText(String.valueOf(struct.getColor().g));
					colorB.setText(String.valueOf(struct.getColor().b));
					colorA.setText(String.valueOf(struct.getColor().a));
					colorIn.setSelectedByCaption(InterpolationUtil.getInterpolationName(struct.getInterpolation()), false);
				}
			}
			@Override
			public void onRemoveSelectedItem(int index, ListItem removed) {
				
			}
			@Override
			public void onSelectListUpdate(List<ListItem> items) {
				
			}
		};
		ic.addChild(colorList);
		form.addFormElement(colorList.items);
		
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		
		Layout.dim.set(Layout.bWidth,Layout.h);
		colorUpdate = new ButtonAdapter(screen, "colorUpdate", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				updateColorInfluencer();
			}
		};
		colorUpdate.setText("Apply List");
		ic.addChild(colorUpdate);
		form.addFormElement(colorUpdate);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lColorRandom = Layout.getNewLabel(screen, "Random Start Color: ");
		ic.addChild(lColorRandom);
		
		Layout.incCol(lColorRandom);
		Layout.dim.set(Layout.h,Layout.h);
		colorRandom = new CheckBox(screen, "colorRandom", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				ColorInfluencer ci = builder.emitter.getEmitter().getInfluencer(ColorInfluencer.class);
				ci.setUseRandomStartColor(toggled);
			}
		};
		ic.addChild(colorRandom);
		form.addFormElement(colorRandom);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lColorEnabled = Layout.getNewLabel(screen, "Enabled: ");
		ic.addChild(lColorEnabled);
		
		Layout.incCol(lColorEnabled);
		Layout.dim.set(Layout.h,Layout.h);
		colorEnabled = new CheckBox(screen, "colorEnabled", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				ColorInfluencer ci = builder.emitter.getEmitter().getInfluencer(ColorInfluencer.class);
				ci.setEnabled(toggled);
			}
		};
		ic.addChild(colorEnabled);
		form.addFormElement(colorEnabled);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lColorDur = Layout.getNewLabel(screen, "Set Fixed Duration: ");
		ic.addChild(lColorDur);
		
		Layout.incCol(lColorDur);
		Layout.dim.set(Layout.floatW,Layout.h);
		colorDur = new TextField(screen, "colorDur", Layout.pos, Layout.dim);
		colorDur.setType(TextField.Type.NUMERIC);
		ic.addChild(colorDur);
		form.addFormElement(colorDur);
		
		Layout.incCol(colorDur);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applyColorDur = new ButtonAdapter(screen, "applyColorDur", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(colorDur.getText())) {
					ColorInfluencer ci = builder.emitter.getEmitter().getInfluencer(ColorInfluencer.class);
					ci.setFixedDuration(
						Float.valueOf(colorDur.getText())
					);
				}
			}
		};
		applyColorDur.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applyColorDur);
		form.addFormElement(applyColorDur);
		
		Layout.incCol(applyColorDur);
		Layout.dim.set(300,Layout.h);
		Label lColorDurExp = Layout.getNewLabel(screen, "NOTE: A duration of 0 = fixed duration disabled.");
		lColorDurExp.setTextAlign(BitmapFont.Align.Left);
		ic.addChild(lColorDurExp);
		
		ic.sizeToContent();
		
		createWindow("Color Influencer");
		
		colorUpdate.setX(ic.getWidth()-colorUpdate.getWidth());
		colorAdd.setX(ic.getWidth()-colorAdd.getWidth());
		colorEdit.setX(ic.getWidth()-colorEdit.getWidth());
		colorCancel.setX(ic.getWidth()-(colorCancel.getWidth()*2+Layout.pad));
	}
	
	private void updateColorInfluencer() {
		if (builder.emitter != null) {
			builder.startChangeState();
			
			ColorInfluencer ci = builder.emitter.getEmitter().getInfluencer(ColorInfluencer.class);
			ci.removeAll();
			for (ListItem li : colorList.items.getListItems()) {
				ColorStruct cs = (ColorStruct)li.getValue();
				ci.addColor(cs.getColor(),cs.getInterpolation());
			}
			
			builder.resetChangeState();
			builder.endChangeState();
		}
	}
	private void addColor() {
		if (builder.validateFloatValue(colorR.getText()) &&
			builder.validateFloatValue(colorG.getText()) &&
			builder.validateFloatValue(colorB.getText()) &&
			builder.validateFloatValue(colorA.getText())) {
			Interpolation interp = InterpolationUtil.getInterpolationByIndex(
				(Integer)colorIn.getSelectedListItem().getValue()
			);
			ColorStruct color = new ColorStruct(
				new ColorRGBA(
					Float.valueOf(colorR.getText()),
					Float.valueOf(colorG.getText()),
					Float.valueOf(colorB.getText()),
					Float.valueOf(colorA.getText())),
				interp
			);
			colorList.items.addListItem(
				color.getColor().toString() + " : " + colorIn.getText(),
				color
			);
		}
	}
	private void updateColor() {
		if (!colorList.getSelectList().getSelectedListItems().isEmpty()) {
			if (builder.validateFloatValue(colorR.getText()) &&
				builder.validateFloatValue(colorG.getText()) &&
				builder.validateFloatValue(colorB.getText()) &&
				builder.validateFloatValue(colorA.getText())) {
				ListItem item = colorList.getSelectList().getSelectedListItems().get(0);
				ColorStruct struct = (ColorStruct)item.getValue();
				struct.setColor(
					Float.valueOf(colorR.getText()),
					Float.valueOf(colorG.getText()),
					Float.valueOf(colorB.getText()),
					Float.valueOf(colorA.getText())
				);
				struct.setInterpolation(
					InterpolationUtil.getInterpolationByIndex(
						(Integer)colorIn.getSelectedListItem().getValue()
					)
				);
				int index = colorList.getSelectList().getSelectedIndex();
				if (index < colorList.getSelectList().getListItems().size()-1) {
					colorList.getSelectList().removeListItem(index);
					colorList.getSelectList().insertListItem(
						index,
						struct.getColor().toString() + " : " + colorIn.getText(),
						struct);
					colorList.getSelectList().setSelectedIndex(index);
				} else if (index == colorList.getSelectList().getListItems().size()-1) {
					colorList.getSelectList().removeListItem(index);
					colorList.getSelectList().addListItem(
						struct.getColor().toString() + " : " + colorIn.getText(),
						struct);
					colorList.getSelectList().setSelectedIndex(index);
				}
				colorEdit.hide();
				colorCancel.hide();
				colorAdd.show();
			}
		}
	}
	
	@Override
	public void loadProperties(ParticleInfluencer influencer) {
		ColorInfluencer inf = (ColorInfluencer)influencer;
		
		colorList.getSelectList().removeAllListItems();
		int index = 0;
		for (ColorRGBA color : inf.getColors()) {
			Interpolation interp = inf.getInterpolations()[index];
			ColorStruct struct = new ColorStruct(
				color,
				interp
			);
			colorList.getSelectList().addListItem(
				struct.getColor().toString() + " : " + InterpolationUtil.getInterpolationName(struct.getInterpolation()),
				struct);
			index++;
		}
		colorRandom.setIsChecked(inf.getUseRandomStartColor());
		colorEnabled.setIsChecked(inf.isEnabled());
		colorDur.setText(String.valueOf(inf.getFixedDuration()));
	}

	@Override
	public void showGUIWindow() {
		builder.getApplication().getStateManager().attach(this);
		colorEdit.hide();
		colorCancel.hide();
	}
}
