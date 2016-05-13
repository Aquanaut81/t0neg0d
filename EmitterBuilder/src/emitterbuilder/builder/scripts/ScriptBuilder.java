package emitterbuilder.builder.scripts;

import com.jme3.input.event.MouseButtonEvent;
import emitter.Emitter;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.builder.datastructures.ScriptStruct;
import emitterbuilder.gui.GUIWindow;
import emitterbuilder.gui.utils.Layout;
import emitterbuilder.gui.SelectListEditor;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Callable;
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
public class ScriptBuilder extends GUIWindow {
	public static enum ScriptType {
		Emit,
		Emit_For_Duration
	}
	public static enum ScriptFunction {
		Add,
		Edit
	}
	
	private LinkedList<ScriptStruct> tasks = new LinkedList<ScriptStruct>();
	
	EmitScript esScript;
	EmitForDurationScript edsScript;
	
	SelectBox emitters, scriptTypeList;
	SelectListEditor scriptList;
	ButtonAdapter newScript, execScripts;
	
	public ScriptBuilder(final EmitterBuilder builder, Screen screen) {
		super(builder, screen);
		
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lScriptType = Layout.getNewLabel(screen, "Script Type: ");
		ic.addChild(lScriptType);
		
		Layout.incCol(lScriptType);
		Layout.dim.set(Layout.sbWidth, Layout.h);
		scriptTypeList = new SelectBox(screen, "scriptType", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				
			}
		};
		for (ScriptType sType : ScriptType.values()) {
			scriptTypeList.addListItem(sType.name(), sType);
		}
		ic.addChild(scriptTypeList);
		form.addFormElement(scriptTypeList);
		
		Layout.incCol(scriptTypeList);
		Layout.x += Layout.h;
		Layout.pos.set(Layout.x,Layout.y);
		Layout.dim.set(Layout.bWidth, Layout.h);
		newScript = new ButtonAdapter(screen, "newScript", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				reloadEmitterLists();
				if (scriptTypeList.getSelectedListItem().getCaption().equals(ScriptType.Emit.name())) {
					esScript.setScriptFunction(ScriptFunction.Add);
					builder.getApplication().getStateManager().attach(esScript);
				} else if (scriptTypeList.getSelectedListItem().getCaption().equals(ScriptType.Emit_For_Duration.name())) {
					edsScript.setScriptFunction(ScriptFunction.Add);
					builder.getApplication().getStateManager().attach(edsScript);
				}
			}
		};
		newScript.setText("Add New");
		ic.addChild(newScript);
		form.addFormElement(newScript);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lScripts = Layout.getNewLabel(screen, "Current Scripts: ");
		ic.addChild(lScripts);
		
		Layout.incCol(lScripts);
		Layout.dim.set(400,Layout.h*4);
		scriptList = new SelectListEditor(screen, Layout.pos, Layout.dim) {
			@Override
			public void onEditSelectedItem(int index, SelectList.ListItem updated) {
				if (!scriptList.getSelectList().getSelectedListItems().isEmpty()) {
					ScriptStruct struct = (ScriptStruct)updated.getValue();
					switch(struct.type) {
						case Emit:
							esScript.loadProperties(struct);
							esScript.setScriptFunction(ScriptFunction.Edit);
							builder.getApplication().getStateManager().attach(esScript);
							break;
						case Emit_For_Duration:
							edsScript.loadProperties(struct);
							edsScript.setScriptFunction(ScriptFunction.Edit);
							builder.getApplication().getStateManager().attach(edsScript);
							break;
					}
				}
			}
			@Override
			public void onRemoveSelectedItem(int index, SelectList.ListItem removed) {
				if (!scriptList.getSelectList().getSelectedListItems().isEmpty()) {
					handleRemoveItem(removed);
				}
			}
			@Override
			public void onSelectListUpdate(List<SelectList.ListItem> items) {
				if (!scriptList.getSelectList().getSelectedListItems().isEmpty()) {
					tasks.clear();
					for (ListItem li : items) {
						ScriptStruct struct = (ScriptStruct)li.getValue();
						tasks.add(struct);
					}
					ListItem selected = this.items.getSelectedListItems().get(0);
					this.items.removeAllListItems();
					int index = -1;
					int count = 0;
					for (ListItem li : items) {
						if (li == selected)
							index = count;
						this.items.addListItem(li.getCaption(), li.getValue());
						count++;
					}
					this.items.setSelectedIndex(index);
				}
			}
		};
		ic.addChild(scriptList);
		form.addFormElement(scriptList.items);
		
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		
		Layout.x = scriptList.getX()+scriptList.getWidth()-(Layout.bWidth*2);
		Layout.updatePos();
		Layout.dim.set(Layout.bWidth*2,Layout.h);
		execScripts = new ButtonAdapter(screen, "execScripts", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				executeScripts();
			}
		};
		execScripts.setText("Run Scripts");
		ic.addChild(execScripts);
		form.addFormElement(execScripts);
		
		ic.sizeToContent();
		
		createWindow("Script Editor");
		
		esScript = new EmitScript(builder, screen);
		edsScript = new EmitForDurationScript(builder, screen);
	}
	
	public LinkedList<ScriptStruct> getTasks() {
		return this.tasks;
	}
	
	public void reloadEmitterLists() {
		esScript.getEmitterList().removeAllListItems();
		edsScript.getEmitterList().removeAllListItems();
		for (EmitterStruct struct : builder.getEmitters().values()) {
			esScript.getEmitterList().addListItem(struct.getEmitter().getName(), struct);
			edsScript.getEmitterList().addListItem(struct.getEmitter().getName(), struct);
		}
	}
	
	public void loadProperties() {
		
	}
	
	private void handleRemoveItem(ListItem removed) {
		ScriptStruct struct = (ScriptStruct)removed.getValue();
		Emitter emitter = null;
		for (EmitterStruct es : builder.getEmitters().values()) {
			if (struct.emitterName.equals(es.getEmitter().getName())) {
				emitter = es.getEmitter();
				break;
			}
		}
		if (emitter != null) {
			emitter.setEmissionsPerSecond(0);
			emitter.setParticlesPerEmission(0);
			for (EmitterTask tt : struct.tasks) {
				if (tt.getTimer() != null) {
					try { tt.getTimer().cancel(); } catch (Exception ex) {  }
					try { tt.getTimer().purge(); } catch (Exception ex) {  }
					tt = null;
				}
			}
			tasks.remove(struct);
		}
	}
	
	public void removeTask(ScriptStruct struct) {
		Emitter emitter = null;
		for (EmitterStruct es : builder.getEmitters().values()) {
			if (struct.emitterName.equals(es.getEmitter().getName())) {
				emitter = es.getEmitter();
				break;
			}
		}
		if (emitter != null) {
			emitter.setEmissionsPerSecond(0);
			emitter.setParticlesPerEmission(0);
			for (EmitterTask tt : struct.tasks) {
				if (tt.getTimer() != null) {
					try { tt.getTimer().cancel(); } catch (Exception ex) {  }
					try { tt.getTimer().purge(); } catch (Exception ex) {  }
					tt = null;
				}
			}
			tasks.remove(struct);
		}
	}
	
	public void clearScriptList() {
		scriptList.items.removeAllListItems();
	}
	
	@Override
	public void cleanup() {
		super.cleanup();
		screen.removeElement(win);
		
		for (ScriptStruct ss : tasks) {
			for (EmitterTask tt : ss.tasks) {
				if (tt.getTimer() != null) {
					try { tt.getTimer().cancel(); } catch (Exception ex) {  }
					try { tt.getTimer().purge(); } catch (Exception ex) {  }
					tt = null;
				}
			}
		}
	}
	
	public void addEmitScript(Emitter emitter, boolean emitAll, int numParticles, long delay) {
		ScriptStruct struct = new ScriptStruct(emitter.getName(), ScriptType.Emit);
		struct.emitAll = emitAll;
		struct.numParticles = numParticles;
		struct.delay = delay;
		tasks.add(struct);
		
		String caption = emitter.getName() + " : Emit ";
		caption += (struct.emitAll) ? "all " : String.valueOf(numParticles) + " ";
		caption += "particles at " + String.valueOf((float)((float)delay/1000)) + " seconds.";
		scriptList.items.addListItem(caption, struct);
		
		int index = scriptList.items.getListItemIndexByCaption(caption);
		scriptList.items.setSelectedIndex(index);
		scriptList.scrollToSelected();
		
		builder.getApplication().getStateManager().detach(esScript);
	}
	
	public void updateEmitScript(ScriptStruct struct) {
		int index = -1;
		int count = 0;
		for (ScriptStruct ss : tasks) {
			if (ss == struct) {
				index = count;
				break;
			}
			count++;
		}
		if (index != -1) {
			tasks.add(index, struct);
			tasks.remove(index+1);
			
			String caption = struct.emitterName + " : Emit ";
			caption += (struct.emitAll) ? "all " : String.valueOf(struct.numParticles) + " ";
			caption += "particles at " + String.valueOf((float)((float)struct.delay/1000)) + " seconds.";
			
			index = -1;
			count = 0;
			for (ListItem li : scriptList.items.getListItems()) {
				if (struct == (ScriptStruct)li.getValue()) {
					index = count;
					break;
				}
				count++;
			}
			scriptList.items.insertListItem(index, caption, struct);
			scriptList.items.removeListItem(index+1);
			scriptList.items.setSelectedIndex(index);
			scriptList.scrollToSelected();
		
			builder.getApplication().getStateManager().detach(esScript);
		}
	}
	
	public void addDurationScript(Emitter emitter, int emitsPerSec, int partsPerEmit, long delay, long duration) {
		ScriptStruct struct = new ScriptStruct(emitter.getName(), ScriptType.Emit_For_Duration);
		struct.emitPerSec = emitsPerSec;
		struct.partsPerEmit = partsPerEmit;
		struct.delay = delay;
		struct.duration = duration;
		tasks.add(struct);
		
		String caption = emitter.getName() + " : Emit ";
		caption += String.valueOf(partsPerEmit) + " particles " + String.valueOf(emitsPerSec) + "x per sec ";
		caption += "at " + String.valueOf((float)((float)delay/1000)) + "s ";
		caption += "for " + String.valueOf((float)((float)duration/1000)) + "s.";
		scriptList.items.addListItem(caption, struct);
		
		int index = scriptList.items.getListItemIndexByCaption(caption);
		scriptList.items.setSelectedIndex(index);
		scriptList.scrollToSelected();
		
		builder.getApplication().getStateManager().detach(edsScript);
	}
	
	public void updateDurationScript(ScriptStruct struct) {
		int index = -1;
		int count = 0;
		for (ScriptStruct ss : tasks) {
			if (ss == struct) {
				index = count;
				break;
			}
			count++;
		}
		if (index != -1) {
			tasks.add(index, struct);
			tasks.remove(index+1);
			
			String caption = struct.emitterName + " : Emit ";
			caption += String.valueOf(struct.partsPerEmit) + " particles " + String.valueOf(struct.emitPerSec) + "x per sec ";
			caption += "at " + String.valueOf((float)((float)struct.delay/1000)) + "s ";
			caption += "for " + String.valueOf((float)((float)struct.duration/1000)) + "s.";
			
			index = -1;
			count = 0;
			for (ListItem li : scriptList.items.getListItems()) {
				if (struct == (ScriptStruct)li.getValue()) {
					index = count;
					break;
				}
				count++;
			}
			scriptList.items.insertListItem(index, caption, struct);
			scriptList.items.removeListItem(index+1);
			scriptList.items.setSelectedIndex(index);
			scriptList.scrollToSelected();
		
			builder.getApplication().getStateManager().detach(edsScript);
		}
	}
	
	public void executeScripts() {
		for (ScriptStruct ss : tasks) {
			for (EmitterTask tt : ss.tasks) {
				if (tt.getTimer() != null) {
					try { tt.getTimer().cancel(); } catch (Exception ex) {  }
					try { tt.getTimer().purge(); } catch (Exception ex) {  }
					tt = null;
				}
			}
			Emitter emitter = null;
			for (EmitterStruct struct : builder.getEmitters().values()) {
				if (struct.getEmitter().getName().equals(ss.emitterName)) {
					emitter = struct.getEmitter();
					break;
				}
			}
			if (emitter != null) {
				switch (ss.type) {
					case Emit:
						EmitTask et = new EmitTask(
							emitter,
							ss.emitAll,
							ss.numParticles,
							ss.delay);
						Timer t = new Timer();
						et.setTimer(t);
						t.schedule(et,et.getDelay());
						ss.tasks.add(et);
						break;
					case Emit_For_Duration:
						EmitAtConstantRateTask ecr1t = new EmitAtConstantRateTask(
							emitter,
							ss.emitPerSec,
							ss.partsPerEmit,
							ss.delay);
						Timer t1 = new Timer();
						ecr1t.setTimer(t1);
						t1.schedule(ecr1t,ecr1t.getDelay());
						ss.tasks.add(ecr1t);

						EmitAtConstantRateTask ecr2t = new EmitAtConstantRateTask(
							emitter,
							0,
							0,
							ss.delay+ss.duration);
						Timer t2 = new Timer();
						ecr2t.setTimer(t2);
						t2.schedule(ecr2t,ecr2t.getDelay());
						ss.tasks.add(ecr2t);
						break;
				}
			}
		}
	}
	
	public abstract class EmitterTask extends TimerTask {
		Timer timer;
		public abstract long getDelay();
		
		public void setTimer(Timer t) { this.timer = t; }
		public Timer getTimer() { return this.timer; }
	}

	public class EmitTask extends EmitterTask {
		Emitter emitter;
		boolean emitAll;
		int numParticles;
		long delay;
		
		public EmitTask(Emitter emitter, boolean emitAll, int numParticles, long delay) {
			this.emitter = emitter;
			this.emitAll = emitAll;
			this.numParticles = numParticles;
			this.delay = delay;
		}
		
		public long getDelay() {
			return delay;
		}
		
		@Override
		public void run() {
			builder.getApplication().enqueue(
				new Callable() {
					public Object call() throws Exception {
						emitter.resetInterval();
						if (emitAll)
							emitter.emitAllParticles();
						else
							emitter.emitNumParticles(numParticles);
						return null;
					}
				}
			);
		}
	}
	
	public class EmitAtConstantRateTask extends EmitterTask {
		Emitter emitter;
		boolean emitAll;
		int emissionsPerSec;
		int partsPerEmit;
		long delay;
		
		public EmitAtConstantRateTask(Emitter emitter, int emissionsPerSec, int partsPerEmit, long delay) {
			this.emitter = emitter;
			this.emissionsPerSec = emissionsPerSec;
			this.partsPerEmit = partsPerEmit;
			this.delay = delay;
		}
		
		public long getDelay() {
			return delay;
		}
		
		@Override
		public void run() {
			builder.getApplication().enqueue(
				new Callable() {
					public Object call() throws Exception {
						emitter.resetInterval();
						emitter.setEmissionsPerSecond(emissionsPerSec);
						emitter.setParticlesPerEmission(partsPerEmit);
						return null;
					}
				}
			);
		}
	}
}
