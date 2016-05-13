package emitterbuilder.builder.shapes;

import com.jme3.input.event.MouseButtonEvent;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.gui.utils.Layout;
import emitterbuilder.gui.PropertiesWindow;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.text.Label;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class SelectPrimitive extends PropertiesWindow {
	SelectBox JMEPList;
	ButtonAdapter JMEPConfig;
	
	public SelectPrimitive(final EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lSelectJMEP = Layout.getNewLabel(screen, "Emitter Shape: ");
		ic.addChild(lSelectJMEP);
		
		Layout.incCol(lSelectJMEP);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		JMEPList = new SelectBox(screen, "JMEPList", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {  }
		};
		JMEPList.addListItem("Box", 0);
		JMEPList.addListItem("Cylinder", 1);
		JMEPList.addListItem("Dome", 2);
		JMEPList.addListItem("Quad", 3);
		JMEPList.addListItem("Sphere", 4);
		JMEPList.addListItem("Torus", 5);
		ic.addChild(JMEPList);
		form.addFormElement(JMEPList);
		
		Layout.incCol(JMEPList);
		Layout.x += Layout.h;
		Layout.pos.set(Layout.x,Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		JMEPConfig = new ButtonAdapter(screen, "JMEPConfig", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				switch (JMEPList.getSelectIndex()) {
					case 0:
						builder.getShapeProperties().showConfigWindow(Box.class);
						break;
					case 1:
						builder.getShapeProperties().showConfigWindow(Cylinder.class);
						break;
					case 2:
						builder.getShapeProperties().showConfigWindow(Dome.class);
						break;
					case 3:
						builder.getShapeProperties().showConfigWindow(Quad.class);
						break;
					case 4:
						builder.getShapeProperties().showConfigWindow(Sphere.class);
						break;
					case 5:
						builder.getShapeProperties().showConfigWindow(Torus.class);
						break;
				}
			}
		};
		ic.addChild(JMEPConfig);
		JMEPConfig.setText("Config");
		form.addFormElement(JMEPConfig);
		
		ic.sizeToContent();
		
		createWindow("Select Primitive");
	}
	
	@Override
	public void loadProperties(EmitterStruct struct) {
		
	}
}
