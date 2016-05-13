package emitterbuilder.builder.influencers;

import com.jme3.app.Application;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.event.MouseButtonEvent;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.DestinationInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.ImpulseInfluencer;
import emitter.influencers.ParticleInfluencer;
import emitter.influencers.PhysicsInfluencer;
import emitter.influencers.RadialVelocityInfluencer;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.gui.CollapsableWindow;
import emitterbuilder.gui.SelectListEditor;
import emitterbuilder.gui.utils.Layout;
import java.util.ArrayList;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.lists.SelectList;
import tonegod.gui.controls.lists.SelectList.ListItem;
import tonegod.gui.controls.text.Label;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class Influencers extends CollapsableWindow {
	GravityWindow gravityWin;
	ColorWindow colorWin;
	SizeWindow sizeWin;
	AlphaWindow alphaWin;
	RotationWindow rotationWin;
	SpriteWindow spriteWin;
	ImpulseWindow impulseWin;
	DestinationWindow destinationWin;
	RadialWindow radialWin;
	PhysicsWindow physicsWin;
	
	SelectBox infListA;
	SelectListEditor infList;
	ButtonAdapter addInf;
	
	public Influencers(EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		loadInfluencerWindows();
	}
	
	@Override
	protected void populateWindow() {
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lAvailableInf = Layout.getNewLabel(screen, "Available Influencers: ");
		ic.addChild(lAvailableInf);
		
		Layout.incCol(lAvailableInf);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		infListA = new SelectBox(screen, "infListA", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				
			}
		};
		ic.addChild(infListA);
		form.addFormElement(infListA);
		
		Layout.incCol(infListA);
		Layout.x += Layout.h;
		Layout.pos.set(Layout.x,Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		addInf = new ButtonAdapter(screen, "addInf", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				addInfluencer();
			}
		};
		addInf.setText("Add");
		ic.addChild(addInf);
		form.addFormElement(addInf);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lCurrentInf = Layout.getNewLabel(screen, "Current Influencers: ");
		ic.addChild(lCurrentInf);
		
		Layout.incCol(lCurrentInf);
		Layout.dim.set(Layout.sbWidth+Layout.bWidth-(Layout.h),Layout.h*4);
		infList = new SelectListEditor(screen, Layout.pos, Layout.dim) {
			@Override
			public void onEditSelectedItem(int index, ListItem updated) {
				editInfluencer();
			}
			@Override
			public void onRemoveSelectedItem(int index, ListItem removed) {
				removeInfluencer(removed);
			}
			@Override
			public void onSelectListUpdate(List<ListItem> items) {
				
			}
		};
		infList.getSelectList().setIsMultiselect(false);
		infList.getSelectList().addListItem("Color", 1);
		infList.getSelectList().addListItem("Alpha", 2);
		infList.getSelectList().addListItem("Size", 3);
		ic.addChild(infList);
		form.addFormElement(infList.items);
		
		populatInfListA();
		
		ic.sizeToContent();
		
		createWindow("");
	}
	@Override
	public void loadProperties() {
		win.setWindowTitle("Influencer Properties (" + builder.emitter.getEmitter().getName() + ")");
		infList.items.removeAllListItems();
		for (ParticleInfluencer inf : builder.emitter.getEmitter().getInfluencers()) {
			int index = getInfluencerIndex(inf.getInfluencerClass().getSimpleName());
			String name = inf.getInfluencerClass().getSimpleName();
			name = name.substring(0,name.indexOf("Influencer"));
			infList.items.addListItem(name, index);
		}
		populatInfListA();
	}
	
	//<editor-fold desc="Properties Config">
	public void populatInfListA() {
		infListA.removeAllListItems();
		infListA.addListItem("Gravity",0);
		infListA.addListItem("Color",1);
		infListA.addListItem("Alpha",2);
		infListA.addListItem("Size",3);
		infListA.addListItem("Sprite",4);
		infListA.addListItem("Rotation",5);
		infListA.addListItem("RadialVelocity",6);
		infListA.addListItem("Impulse",7);
		infListA.addListItem("Destination",9);
		infListA.addListItem("Physics",10);
		for (ListItem li : infList.items.getListItems()) {
			infListA.removeListItem(li.getCaption());
		}
		infListA.sortList();
	}
	public int getInfluencerIndex(String name) {
		int ret = -1;
		if (name.equals("GravityInfluencer")) ret = 0;
		else if (name.equals("ColorInfluencer")) ret = 1;
		else if (name.equals("AlphaInfluencer")) ret = 2;
		else if (name.equals("SizeInfluencer")) ret = 3;
		else if (name.equals("SpriteInfluencer")) ret = 4;
		else if (name.equals("RotationInfluencer")) ret = 5;
		else if (name.equals("RadialVelocityInfluencer")) ret = 6;
		else if (name.equals("ImpulseInfluencer")) ret = 7;
		else if (name.equals("DirectionInfluencer")) ret = 8;
		else if (name.equals("DestinationInfluencer")) ret = 9;
		else if (name.equals("PhysicsInfluencer")) ret = 10;
		return ret;
	}
	public void addInfluencer() {
		if (infListA.getSelectedListItem() != null) {
			String name = infListA.getSelectedListItem().getCaption();
			int value = (Integer)infListA.getSelectedListItem().getValue();
			int index = (Integer)infListA.getSelectIndex();
			builder.startChangeState();
			switch (value) {
				case 0: // Gravity
					GravityInfluencer gi = new GravityInfluencer();
					gi.setGravity(0, 4, 0);
					builder.emitter.getEmitter().addInfluencer(gi);
					break;
				case 1: // Color
					ColorInfluencer ci = new ColorInfluencer();
					builder.emitter.getEmitter().addInfluencer(ci);
					break;
				case 2: // Alpha
					AlphaInfluencer ai = new AlphaInfluencer();
					builder.emitter.getEmitter().addInfluencer(ai);
					break;
				case 3: // Size
					SizeInfluencer si = new SizeInfluencer();
					builder.emitter.getEmitter().addInfluencer(si);
					break;
				case 4: // Sprite
					SpriteInfluencer spi = new SpriteInfluencer();
					builder.emitter.getEmitter().addInfluencer(spi);
					break;
				case 5: // Rotation
					RotationInfluencer ri = new RotationInfluencer();
					builder.emitter.getEmitter().addInfluencer(ri);
					break;
				case 6: // Radial
					RadialVelocityInfluencer rvi = new RadialVelocityInfluencer();
					builder.emitter.getEmitter().addInfluencer(rvi);
					break;
				case 7: // Impulse
					ImpulseInfluencer ii = new ImpulseInfluencer();
					builder.emitter.getEmitter().addInfluencer(ii);
					break;
				case 8: // Direction
					break;
				case 9: // Desination
					DestinationInfluencer desti = new DestinationInfluencer();
					builder.emitter.getEmitter().addInfluencer(desti);
					break;
				case 10: // Physics
					PhysicsInfluencer phi = new PhysicsInfluencer();
					if (builder.getApplication().getStateManager().hasState(builder.getTestScene())) {
						phi.addCollidable(builder.getTestScene().getGeom());
					}
					builder.emitter.getEmitter().addInfluencer(phi);
					break;
			}
			builder.resetChangeState();
			builder.endChangeState();
			infListA.removeListItem(name);
			infListA.sortList();
			infList.getSelectList().addListItem(name, value);
			infList.items.setSelectedIndex(infList.items.getListItems().size()-1);
			infList.scrollToSelected();
		}
	}
	public void editInfluencer() {
		SelectList.ListItem li = infList.getSelectList().getListItem(infList.getSelectList().getSelectedIndex());
		switch ((Integer)li.getValue()) {
			case 0: // Gravity
				showInfluencerWindow(GravityWindow.class);
				break;
			case 1: // Color
				showInfluencerWindow(ColorWindow.class);
				break;
			case 2: // Alpha
				showInfluencerWindow(AlphaWindow.class);
				break;
			case 3: // Size
				showInfluencerWindow(SizeWindow.class);
				break;
			case 4: // Sprite
				showInfluencerWindow(SpriteWindow.class);
				break;
			case 5: // Rotation
				showInfluencerWindow(RotationWindow.class);
				break;
			case 6: // Radial
				showInfluencerWindow(RadialWindow.class);
				break;
			case 7: // Impulse
				showInfluencerWindow(ImpulseWindow.class);
				break;
			case 8: // Direction
				break;
			case 9: // Desination
				showInfluencerWindow(DestinationWindow.class);
				break;
			case 10: // Physics
				showInfluencerWindow(PhysicsWindow.class);
				break;
		}
	}
	public void removeInfluencer(ListItem item) {
		if (!infList.getSelectList().getSelectedListItems().isEmpty()) {
			String name = item.getCaption();
			int value = (Integer)item.getValue();
			switch (value) {
				case 0: // Gravity
					builder.emitter.getEmitter().removeInfluencer(GravityInfluencer.class);
					closeInfluencerWindow(GravityWindow.class);
					break;
				case 1: // Color
					builder.emitter.getEmitter().removeInfluencer(ColorInfluencer.class);
					closeInfluencerWindow(ColorWindow.class);
					break;
				case 2: // Alpha
					builder.emitter.getEmitter().removeInfluencer(AlphaInfluencer.class);
					closeInfluencerWindow(AlphaWindow.class);
					break;
				case 3: // Size
					builder.emitter.getEmitter().removeInfluencer(SizeInfluencer.class);
					closeInfluencerWindow(SizeWindow.class);
					break;
				case 4: // Sprite
					builder.emitter.getEmitter().removeInfluencer(SpriteInfluencer.class);
					closeInfluencerWindow(SpriteWindow.class);
					break;
				case 5: // Rotation
					builder.emitter.getEmitter().removeInfluencer(RotationInfluencer.class);
					closeInfluencerWindow(RotationWindow.class);
					break;
				case 6: // Radial
					builder.emitter.getEmitter().removeInfluencer(RadialVelocityInfluencer.class);
					closeInfluencerWindow(RadialWindow.class);
					break;
				case 7: // Impulse
					builder.emitter.getEmitter().removeInfluencer(ImpulseInfluencer.class);
					closeInfluencerWindow(ImpulseWindow.class);
					break;
				case 8: // Direction
					break;
				case 9: // Desination
					builder.emitter.getEmitter().removeInfluencer(DestinationInfluencer.class);
					closeInfluencerWindow(DestinationWindow.class);
					break;
				case 10: // Physics
					builder.emitter.getEmitter().removeInfluencer(PhysicsInfluencer.class);
					closeInfluencerWindow(PhysicsWindow.class);
					break;
			}
			infList.getSelectList().removeListItem(name);
			infListA.addListItem(name, value);
			infListA.sortList();
		}
	}
	public void moveInfluencerUp() {
		if (!infList.getSelectList().getSelectedListItems().isEmpty()) {
			ListItem item = infList.getSelectList().getSelectedListItems().get(0);
			String name = item.getCaption();
			int value = (Integer)item.getValue();
			int index = infList.getSelectList().getSelectedIndexes().get(0);
			int nextIndex = index-1;
			if (index > 0) {
				builder.startChangeState();
				List<ParticleInfluencer> influencers = new ArrayList<ParticleInfluencer>();
				for (ListItem li : infList.getSelectList().getListItems()) {
					switch ((Integer)li.getValue()) {
						case 0: // Gravity
							influencers.add(builder.emitter.getEmitter().getInfluencer(GravityInfluencer.class));
							break;
						case 1: // Color
							influencers.add(builder.emitter.getEmitter().getInfluencer(ColorInfluencer.class));
							break;
						case 2: // Alpha
							influencers.add(builder.emitter.getEmitter().getInfluencer(AlphaInfluencer.class));
							break;
						case 3: // Size
							influencers.add(builder.emitter.getEmitter().getInfluencer(SizeInfluencer.class));
							break;
						case 4: // Sprite
							influencers.add(builder.emitter.getEmitter().getInfluencer(SpriteInfluencer.class));
							break;
						case 5: // Rotation
							influencers.add(builder.emitter.getEmitter().getInfluencer(RotationInfluencer.class));
							break;
						case 6: // Radial
							influencers.add(builder.emitter.getEmitter().getInfluencer(RadialVelocityInfluencer.class));
							break;
						case 7: // Impulse
							influencers.add(builder.emitter.getEmitter().getInfluencer(ImpulseInfluencer.class));
							break;
						case 8: // Direction
							break;
						case 9: // Desination
							influencers.add(builder.emitter.getEmitter().getInfluencer(DestinationInfluencer.class));
							break;
						case 10: // Physics
							influencers.add(builder.emitter.getEmitter().getInfluencer(PhysicsInfluencer.class));
							break;
					}
				}
				builder.emitter.getEmitter().removeAllInfluencers();
				for (ParticleInfluencer pi : influencers) {
					builder.emitter.getEmitter().addInfluencer(pi);
				}
				builder.resetChangeState();
				builder.endChangeState();
				infList.getSelectList().removeListItem(name);
				infList.getSelectList().insertListItem(nextIndex, name, value);
				infList.getSelectList().setSelectedIndex(nextIndex);
				infList.scrollToSelected();
			}
		}
	}
	public void moveInfluencerDown() {
		if (!infList.getSelectList().getSelectedListItems().isEmpty()) {
			ListItem item = infList.getSelectList().getSelectedListItems().get(0);
			String name = item.getCaption();
			int value = (Integer)item.getValue();
			int index = infList.getSelectList().getSelectedIndexes().get(0);
			int nextIndex = index+1;
			int count = infList.getSelectList().getListItems().size();
			if (index < count-1) {
				infList.getSelectList().removeListItem(name);
				if (nextIndex < count-1)
					infList.getSelectList().insertListItem(nextIndex, name, value);
				else
					infList.getSelectList().addListItem(name, value);
				infList.getSelectList().setSelectedIndex(nextIndex);
				infList.scrollToSelected();
			}
		}
	}
	//</editor-fold>
	//<editor-fold desc="Common Window Methods">
	private void loadInfluencerWindows() {
		gravityWin = new GravityWindow(builder, screen);
		colorWin = new ColorWindow(builder, screen);
		alphaWin = new AlphaWindow(builder, screen);
		sizeWin = new SizeWindow(builder, screen);
		rotationWin = new RotationWindow(builder, screen);
		spriteWin = new SpriteWindow(builder, screen);
		impulseWin = new ImpulseWindow(builder, screen);
		destinationWin = new DestinationWindow(builder, screen);
		radialWin = new RadialWindow(builder, screen);
		physicsWin = new PhysicsWindow(builder, screen);
	}
	public void showInfluencerWindow(Class c) {
		if (c == GravityWindow.class) {
			gravityWin.loadProperties(builder.emitter.getEmitter().getInfluencer(GravityInfluencer.class));
			gravityWin.showGUIWindow();
		} else if (c == ColorWindow.class) {
			colorWin.loadProperties(builder.emitter.getEmitter().getInfluencer(ColorInfluencer.class));
			colorWin.showGUIWindow();
		} else if (c == AlphaWindow.class) {
			alphaWin.loadProperties(builder.emitter.getEmitter().getInfluencer(AlphaInfluencer.class));
			alphaWin.showGUIWindow();
		} else if (c == SizeWindow.class) {
			sizeWin.loadProperties(builder.emitter.getEmitter().getInfluencer(SizeInfluencer.class));
			sizeWin.showGUIWindow();
		} else if (c == RotationWindow.class) {
			rotationWin.loadProperties(builder.emitter.getEmitter().getInfluencer(RotationInfluencer.class));
			rotationWin.showGUIWindow();
		} else if (c == SpriteWindow.class) {
			spriteWin.loadProperties(builder.emitter.getEmitter().getInfluencer(SpriteInfluencer.class));
			spriteWin.showGUIWindow();
		} else if (c == ImpulseWindow.class) {
			impulseWin.loadProperties(builder.emitter.getEmitter().getInfluencer(ImpulseInfluencer.class));
			impulseWin.showGUIWindow();
		} else if (c == DestinationWindow.class) {
			destinationWin.loadProperties(builder.emitter.getEmitter().getInfluencer(DestinationInfluencer.class));
			destinationWin.showGUIWindow();
		} else if (c == RadialWindow.class) {
			radialWin.loadProperties(builder.emitter.getEmitter().getInfluencer(RadialVelocityInfluencer.class));
			radialWin.showGUIWindow();
		} else if (c == PhysicsWindow.class) {
			physicsWin.loadProperties(builder.emitter.getEmitter().getInfluencer(PhysicsInfluencer.class));
			physicsWin.showGUIWindow();
		}
	}
	public void closeInfluencerWindow(Class c) {
		if (c == GravityWindow.class) {
			builder.getApplication().getStateManager().detach(gravityWin);
		} else if (c == ColorWindow.class) {
			builder.getApplication().getStateManager().detach(colorWin);
		} else if (c == AlphaWindow.class) {
			builder.getApplication().getStateManager().detach(alphaWin);
		} else if (c == SizeWindow.class) {
			builder.getApplication().getStateManager().detach(sizeWin);
		} else if (c == RotationWindow.class) {
			builder.getApplication().getStateManager().detach(rotationWin);
		} else if (c == SpriteWindow.class) {
			builder.getApplication().getStateManager().detach(spriteWin);
		} else if (c == ImpulseWindow.class) {
			builder.getApplication().getStateManager().detach(impulseWin);
		} else if (c == DestinationWindow.class) {
			builder.getApplication().getStateManager().detach(destinationWin);
		} else if (c == RadialWindow.class) {
			builder.getApplication().getStateManager().detach(radialWin);
		} else if (c == PhysicsWindow.class) {
			builder.getApplication().getStateManager().detach(physicsWin);
		}
	}
	public void closeAllInfluencerWindows() {
		closeInfluencerWindow(GravityWindow.class);
		closeInfluencerWindow(ColorWindow.class);
		closeInfluencerWindow(AlphaWindow.class);
		closeInfluencerWindow(SizeWindow.class);
		closeInfluencerWindow(RotationWindow.class);
		closeInfluencerWindow(SpriteWindow.class);
		closeInfluencerWindow(ImpulseWindow.class);
		closeInfluencerWindow(DestinationWindow.class);
		closeInfluencerWindow(RadialWindow.class);
		closeInfluencerWindow(PhysicsWindow.class);
	}
	//</editor-fold>
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
	}
	
	@Override
	public void update(float tpf) {  }
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
}
