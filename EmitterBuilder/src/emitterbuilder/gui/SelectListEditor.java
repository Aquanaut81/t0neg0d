package emitterbuilder.gui;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import java.util.List;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.lists.SelectList;
import tonegod.gui.controls.lists.SelectList.ListItem;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public abstract class SelectListEditor extends Element {
	public SelectList items;
	private ButtonAdapter editItem, removeItem, moveUp, moveDown;
	
	public SelectListEditor(ElementManager screen, Vector2f position) {
		this(screen, UIDUtil.getUID(), position,
			new Vector2f(100,100),
			Vector4f.ZERO,
			null
		);
	}
	
	public SelectListEditor(ElementManager screen, Vector2f position, Vector2f dimensions) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			Vector4f.ZERO,
			null
		);
	}
	
	public SelectListEditor(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		this(screen, UIDUtil.getUID(), position, dimensions,resizeBorders,defaultImg);
	}
	
	public SelectListEditor(ElementManager screen, String UID, Vector2f position) {
		this(screen, UID, position,
			new Vector2f(100,100),
			Vector4f.ZERO,
			null
		);
	}
	
	public SelectListEditor(ElementManager screen, String UID, Vector2f position, Vector2f dimensions) {
		this(screen, UID, position, dimensions,
			Vector4f.ZERO,
			null
		);
	}
	
	public SelectListEditor(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
		this.setAsContainerOnly();
		
		float size = screen.getStyle("Common").getFloat("defaultControlSize");
		
		items = new SelectList(screen, UID + "items", Vector2f.ZERO, new Vector2f(dimensions.x,dimensions.y)) {
			@Override
			public void onChange() {
				
			}
		};
		this.addChild(items);
		
		moveUp = new ButtonAdapter(screen, UID + "moveUp",
			new Vector2f(items.getWidth()+size,0),
			new Vector2f(size,items.getHeight()*0.5f),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!items.getSelectedListItems().isEmpty()) {
					int index = items.getSelectedIndex();
					if (index > 0) {
						ListItem item = items.getListItem(index);
						items.removeListItem(index);
						items.insertListItem(index-1, item.getCaption(), item.getValue());
						items.setSelectedIndex(index-1);
						scrollToSelected();
						onSelectListUpdate(items.getListItems());
					}
				}
			}
		};
		moveUp.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowUp"));
		moveUp.setScaleEW(false);
		moveUp.setScaleNS(false);
		this.addChild(moveUp);
		
		moveDown = new ButtonAdapter(screen, UID + "moveDown",
			new Vector2f(items.getWidth()+size,items.getHeight()*0.5f),
			new Vector2f(size,items.getHeight()*0.5f),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!items.getSelectedListItems().isEmpty()) {
					int index = items.getSelectedIndex();
					if (index < items.getListItems().size()-2) {
						ListItem item = items.getListItem(index);
						items.removeListItem(index);
						items.insertListItem(index+1, item.getCaption(), item.getValue());
						items.setSelectedIndex(index+1);
						scrollToSelected();
						onSelectListUpdate(items.getListItems());
					} else if (index < items.getListItems().size()-1) {
						ListItem item = items.getListItem(index);
						items.removeListItem(index);
						items.addListItem(item.getCaption(), item.getValue());
						items.setSelectedIndex(index+1);
						scrollToSelected();
						onSelectListUpdate(items.getListItems());
					}
				}
			}
		};
		moveDown.setButtonIcon(18, 18, screen.getStyle("Common").getString("arrowDown"));
		moveDown.setScaleEW(false);
		moveDown.setScaleNS(false);
		this.addChild(moveDown);
		
		editItem = new ButtonAdapter(screen, UID + "editItem",
			new Vector2f(0,items.getHeight()),
			new Vector2f(moveUp.getX()/2,size),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!items.getSelectedListItems().isEmpty())
					onEditSelectedItem(items.getSelectedIndex(), items.getSelectedListItems().get(0));
			}
		};
		editItem.setText("Edit Selected");
		editItem.setScaleEW(false);
		editItem.setScaleNS(false);
		this.addChild(editItem);
		
		removeItem = new ButtonAdapter(screen, UID + "removeItem",
			new Vector2f(moveUp.getX()/2,items.getHeight()),
			new Vector2f(moveUp.getX()/2,size),
			screen.getStyle("Button").getVector4f("resizeBorders"),
			screen.getStyle("Button").getString("defaultImg")
		) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (!items.getSelectedListItems().isEmpty()) {
					ListItem ret = items.getSelectedListItems().get(0);
					items.removeListItem(ret.getCaption());
					int listSize = items.getListItems().size();
					if (listSize > 0) {
						if (items.getSelectedIndex() < listSize)
							items.setSelectedIndex(items.getSelectedIndex());
						else
							items.setSelectedIndex(items.getSelectedIndex()-1);
						scrollToSelected();
					}
					onRemoveSelectedItem(items.getSelectedIndex(), ret);
				}
			}
		};
		removeItem.setText("Remove Selected");
		removeItem.setScaleEW(false);
		removeItem.setScaleNS(false);
		this.addChild(removeItem);
		
		
		
		this.sizeToContent();
	}
	
	public SelectList getSelectList() {
		return this.items;
	}
	
	public void scrollToSelected() {
		int rIndex = items.getSelectedIndex();
		float diff = (rIndex+1) * items.getListItemHeight();
		
		float y = -(items.getScrollableHeight()-diff);
		
		if (FastMath.abs(y) > items.getScrollableHeight()) {
			y = items.getScrollableHeight();
		}
		
		this.items.scrollThumbYTo(
			( y )
		);
	}
	
	public abstract void onEditSelectedItem(int index, ListItem updated);
	public abstract void onRemoveSelectedItem(int index, ListItem removed);
	public abstract void onSelectListUpdate(List<ListItem> items);
}
