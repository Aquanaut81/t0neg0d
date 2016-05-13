package emitterbuilder.builder.influencers;

import com.jme3.font.BitmapFont;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.Vector3f;
import emitter.Interpolation;
import emitter.influencers.ParticleInfluencer;
import emitter.influencers.SizeInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.SizeStruct;
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
public class SizeWindow extends InfluencerWindow {
	TextField sizeX, sizeY, sizeZ, sizeTol, sizeDur;
	SelectBox sizeIn;
	SelectListEditor sizeList;
	CheckBox sizeRandom, sizeEnabled;
	ButtonAdapter sizeCancel, sizeEdit, sizeAdd, sizeUpdate;
	
	public SizeWindow(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		populateWindow();
	}
	
	private void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSizeX = Layout.getNewLabel(screen, "Size X: ");
		ic.addChild(lSizeX);
		
		// Size X TextField
		Layout.incCol(lSizeX);
		Layout.dim.set(Layout.floatW,Layout.h);
		sizeX = new TextField(screen, "sizeX", Layout.pos, Layout.dim);
		sizeX.setType(TextField.Type.NUMERIC);
		ic.addChild(sizeX);
		form.addFormElement(sizeX);
		
		Layout.incCol(sizeX);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lSizeY = Layout.getNewLabel(screen, "Y: ");
		ic.addChild(lSizeY);
		
		Layout.incCol(lSizeY);
		Layout.dim.set(Layout.floatW,Layout.h);
		sizeY = new TextField(screen, "sizeY", Layout.pos, Layout.dim);
		sizeY.setType(TextField.Type.NUMERIC);
		ic.addChild(sizeY);
		form.addFormElement(sizeY);
		
		Layout.incCol(sizeY);
		Layout.dim.set(Layout.lWidthS,Layout.h);
		Label lSizeZ = Layout.getNewLabel(screen, "Z: ");
		ic.addChild(lSizeZ);
		
		Layout.incCol(lSizeZ);
		Layout.dim.set(Layout.floatW,Layout.h);
		sizeZ = new TextField(screen, "sizeZ", Layout.pos, Layout.dim);
		sizeZ.setType(TextField.Type.NUMERIC);
		ic.addChild(sizeZ);
		form.addFormElement(sizeZ);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSizeIn = Layout.getNewLabel(screen, "Interpolation: ");
		ic.addChild(lSizeIn);
		
		Layout.incCol(lSizeIn);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		sizeIn = new SelectBox(screen, "sizeIn", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				
			}
		};
		InterpolationUtil.populateInterpolations(sizeIn);
		sizeIn.setSelectedByCaption("linear", false);
		ic.addChild(sizeIn);
		form.addFormElement(sizeIn);
		
		Layout.incCol(sizeIn);
		Layout.x += Layout.h;
		Layout.pos.setX(Layout.x);
		Layout.dim.set(Layout.bWidth,Layout.h);
		sizeCancel = new ButtonAdapter(screen, "sizeCancel", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				sizeAdd.show();
				sizeEdit.hide();
				sizeCancel.hide();
			}
		};
		sizeCancel.setText("Cancel");
		ic.addChild(sizeCancel);
		form.addFormElement(sizeCancel);
		
		Layout.incCol(sizeCancel);
	//	Layout.pos.set(colorA.getX()+colorA.getWidth()-Layout.bWidth,y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		sizeEdit = new ButtonAdapter(screen, "sizeEdit", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				updateSize();
			}
		};
		sizeEdit.setText("Update");
		ic.addChild(sizeEdit);
		form.addFormElement(sizeEdit);
		
		Layout.pos.set(sizeEdit.getX(),Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		sizeAdd = new ButtonAdapter(screen, "sizeAdd", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				addSize();
			}
		};
		sizeAdd.setText("Add");
		ic.addChild(sizeAdd);
		form.addFormElement(sizeAdd);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSizeList = Layout.getNewLabel(screen, "Current Sizes: ");
		ic.addChild(lSizeList);
		
		Layout.incCol(lSizeList);
		Layout.dim.set(400,Layout.h*4);
		sizeList = new SelectListEditor(screen, Layout.pos, Layout.dim) {
			@Override
			public void onEditSelectedItem(int index, ListItem updated) {
				if (!sizeList.getSelectList().getSelectedListItems().isEmpty()) {
					sizeAdd.hide();
					sizeEdit.show();
					sizeCancel.show();
					SizeStruct struct = (SizeStruct)sizeList.getSelectList().getSelectedListItems().get(0).getValue();
					sizeX.setText(String.valueOf(struct.getSize().x));
					sizeY.setText(String.valueOf(struct.getSize().y));
					sizeZ.setText(String.valueOf(struct.getSize().z));
					sizeIn.setSelectedByCaption(
						InterpolationUtil.getInterpolationName(struct.getInterpolation()),
						false
					);
				}
			}
			@Override
			public void onRemoveSelectedItem(int index, ListItem removed) {
				
			}
			@Override
			public void onSelectListUpdate(List<ListItem> items) {
				
			}
		};
		ic.addChild(sizeList);
		form.addFormElement(sizeList.items);
		
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.dim.set(Layout.bWidth,Layout.h);
		sizeUpdate = new ButtonAdapter(screen, "sizeUpdate", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				updateSizeInfluencer();
			}
		};
		sizeUpdate.setText("Apply List");
		ic.addChild(sizeUpdate);
		form.addFormElement(sizeUpdate);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSizeRandom = Layout.getNewLabel(screen, "Random Size: ");
		ic.addChild(lSizeRandom);
		
		Layout.incCol(lSizeRandom);
		Layout.dim.set(Layout.h,Layout.h);
		sizeRandom = new CheckBox(screen, "sizeRandom", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				SizeInfluencer si = (SizeInfluencer)builder.emitter.getEmitter().getInfluencer(SizeInfluencer.class);
				si.setUseRandomSize(toggled);
			}
		};
		ic.addChild(sizeRandom);
		form.addFormElement(sizeRandom);
		
		Layout.incCol(sizeRandom);
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSizeTol = Layout.getNewLabel(screen, "Size Variation Tolerence: ");
		ic.addChild(lSizeTol);
		
		Layout.incCol(lSizeTol);
		Layout.dim.set(Layout.floatW,Layout.h);
		sizeTol = new TextField(screen, "sizeTol", Layout.pos, Layout.dim);
		sizeTol.setType(TextField.Type.NUMERIC);
		ic.addChild(sizeTol);
		form.addFormElement(sizeTol);
		
		Layout.incCol(sizeTol);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applySizeTol = new ButtonAdapter(screen, "applySizeTol", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(sizeTol.getText())) {
					SizeInfluencer si = builder.emitter.getEmitter().getInfluencer(SizeInfluencer.class);
					si.setRandomSizeTolerance(
						Float.valueOf(sizeTol.getText())
					);
				}
			}
		};
		applySizeTol.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applySizeTol);
		form.addFormElement(applySizeTol);
		
		Layout.incCol(applySizeTol);
		Layout.dim.set(150,Layout.h);
		Label lSizeTolDef = Layout.getNewLabel(screen, "EX: 0.8 = 80%-100% of defined sizes");
		lSizeTolDef.setTextAlign(BitmapFont.Align.Left);
		ic.addChild(lSizeTolDef);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSizeEnabled = Layout.getNewLabel(screen, "Enabled: ");
		ic.addChild(lSizeEnabled);
		
		Layout.incCol(lSizeEnabled);
		Layout.dim.set(Layout.h,Layout.h);
		sizeEnabled = new CheckBox(screen, "sizeEnabled", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				SizeInfluencer si = (SizeInfluencer)builder.emitter.getEmitter().getInfluencer(SizeInfluencer.class);
				si.setEnabled(toggled);
			}
		};
		ic.addChild(sizeEnabled);
		form.addFormElement(sizeEnabled);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSizeDur = Layout.getNewLabel(screen, "Set Fixed Duration: ");
		ic.addChild(lSizeDur);
		
		Layout.incCol(lSizeDur);
		Layout.dim.set(Layout.floatW,Layout.h);
		sizeDur = new TextField(screen, "sizeDur", Layout.pos, Layout.dim);
		sizeDur.setType(TextField.Type.NUMERIC);
		ic.addChild(sizeDur);
		form.addFormElement(sizeDur);
		
		Layout.incCol(sizeDur);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter applySizeDur = new ButtonAdapter(screen, "applySizeDur", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (builder.validateFloatValue(sizeDur.getText())) {
					SizeInfluencer si = builder.emitter.getEmitter().getInfluencer(SizeInfluencer.class);
					si.setFixedDuration(
						Float.valueOf(sizeDur.getText())
					);
				}
			}
		};
		applySizeDur.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowRight"));
		ic.addChild(applySizeDur);
		form.addFormElement(applySizeDur);
		
		Layout.incCol(applySizeDur);
		Layout.dim.set(300,Layout.h);
		Label lSizeDurExp = Layout.getNewLabel(screen, "NOTE: A duration of 0 = fixed duration disabled.");
		lSizeDurExp.setTextAlign(BitmapFont.Align.Left);
		ic.addChild(lSizeDurExp);
		
		ic.sizeToContent();
		
		createWindow("Size Influencer");
		
		sizeUpdate.setX(ic.getWidth()-sizeUpdate.getWidth());
		sizeAdd.setX(ic.getWidth()-sizeAdd.getWidth());
		sizeEdit.setX(ic.getWidth()-sizeEdit.getWidth());
		sizeCancel.setX(ic.getWidth()-(sizeCancel.getWidth()*2+Layout.pad));
	}
	
	private void updateSizeInfluencer() {
		if (builder.emitter != null) {
			builder.startChangeState();
			
			SizeInfluencer si = builder.emitter.getEmitter().getInfluencer(SizeInfluencer.class);
			si.removeAll();
			for (ListItem li : sizeList.items.getListItems()) {
				SizeStruct cs = (SizeStruct)li.getValue();
				si.addSize(cs.getSize(),cs.getInterpolation());
			}
			
			builder.resetChangeState();
			builder.endChangeState();
		}
	}
	private void addSize() {
		if (builder.validateFloatValue(sizeX.getText()) &&
			builder.validateFloatValue(sizeY.getText()) &&
			builder.validateFloatValue(sizeZ.getText())) {
			Interpolation interp = InterpolationUtil.getInterpolationByIndex(
				(Integer)sizeIn.getSelectedListItem().getValue()
			);
			SizeStruct size = new SizeStruct(
				new Vector3f(
					Float.valueOf(sizeX.getText()),
					Float.valueOf(sizeY.getText()),
					Float.valueOf(sizeZ.getText())),
				interp
			);
			sizeList.items.addListItem(
				size.getSize().toString() + " : " + sizeIn.getText(),
				size
			);
		}
	}
	private void updateSize() {
		if (!sizeList.getSelectList().getSelectedListItems().isEmpty()) {
			if (builder.validateFloatValue(sizeX.getText()) &&
				builder.validateFloatValue(sizeY.getText()) &&
				builder.validateFloatValue(sizeZ.getText())) {
				ListItem item = sizeList.getSelectList().getSelectedListItems().get(0);
				SizeStruct struct = (SizeStruct)item.getValue();
				struct.setSize(
					Float.valueOf(sizeX.getText()),
					Float.valueOf(sizeY.getText()),
					Float.valueOf(sizeZ.getText()));
				struct.setInterpolation(
					InterpolationUtil.getInterpolationByIndex(
						(Integer)sizeIn.getSelectedListItem().getValue()
					)
				);
				int index = sizeList.getSelectList().getSelectedIndex();
				if (index < sizeList.getSelectList().getListItems().size()-1) {
					sizeList.getSelectList().removeListItem(index);
					sizeList.getSelectList().insertListItem(
						index,
						struct.getSize().toString() + " : " + sizeIn.getText(),
						struct);
					sizeList.getSelectList().setSelectedIndex(index);
				} else if (index == sizeList.getSelectList().getListItems().size()-1) {
					sizeList.getSelectList().removeListItem(index);
					sizeList.getSelectList().addListItem(
						struct.getSize().toString() + " : " + sizeIn.getText(),
						struct);
					sizeList.getSelectList().setSelectedIndex(index);
				}
				sizeEdit.hide();
				sizeCancel.hide();
				sizeAdd.show();
			}
		}
	}
	
	@Override
	public void loadProperties(ParticleInfluencer influencer) {
		SizeInfluencer inf = (SizeInfluencer)influencer;
		
		sizeList.getSelectList().removeAllListItems();
		int index = 0;
		for (Vector3f size : inf.getSizes()) {
			Interpolation interp = inf.getInterpolations()[index];
			SizeStruct struct = new SizeStruct(
				size,
				interp
			);
			sizeList.getSelectList().addListItem(
				struct.getSize().toString() + " : " + InterpolationUtil.getInterpolationName(struct.getInterpolation()),
				struct);
			index++;
		}
		sizeRandom.setIsChecked(inf.getUseRandomSize());
		sizeEnabled.setIsChecked(inf.isEnabled());
		sizeDur.setText(String.valueOf(inf.getFixedDuration()));
	}

	@Override
	public void showGUIWindow() {
		builder.getApplication().getStateManager().attach(this);
		sizeEdit.hide();
		sizeCancel.hide();
	}
}
