package emitterbuilder.builder;

import emitterbuilder.builder.influencers.Influencers;
import emitterbuilder.builder.shapes.Shapes;
import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.font.BitmapFont;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import emitter.Emitter;
import emitter.Emitter.BillboardMode;
import emitter.EmitterMesh.DirectionType;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.ParticleInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.particle.ParticleDataTriMesh;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.builder.options.LoadProjectDialog;
import emitterbuilder.builder.options.Options;
import emitterbuilder.builder.options.SaveProjectDialog;
import emitterbuilder.builder.particles.Particles;
import emitterbuilder.builder.project.LoadProject;
import emitterbuilder.builder.project.SaveProject;
import emitterbuilder.builder.scripts.ScriptBuilder;
import emitterbuilder.gui.FileBrowser;
import emitterbuilder.gui.utils.Layout;
import emitterbuilder.testscene.TestScene;
import java.util.HashMap;
import java.util.Map;
import tonegod.gui.controls.buttons.ButtonAdapter;
import tonegod.gui.controls.buttons.CheckBox;
import tonegod.gui.controls.form.Form;
import tonegod.gui.controls.lists.SelectBox;
import tonegod.gui.controls.text.Label;
import tonegod.gui.controls.text.TextField;
import tonegod.gui.controls.windows.Panel;
import tonegod.gui.controls.windows.Window;
import tonegod.gui.core.Element;
import tonegod.gui.core.Screen;

/**
 *
 * @author t0neg0d
 */
public class EmitterBuilder extends AbstractAppState implements ActionListener {
	private Map<String,EmitterStruct> emitters = new HashMap<String,EmitterStruct>();
	
	private Application app;
	private Node rootNode;
	private Screen screen;
	public EmitterStruct emitter;
	private String projectName = "";
	
	private Panel mainWin;
	private Form mvForm, matForm;
	private Window matWin;
	private Element mainIC, mvListIC, matIC;
	private SelectBox shapeType, matDefSelect, matBlendSelect;
	private ButtonAdapter addEmitter, matDefConfig, applyTexPath;
	private TextField mvName, texPath, texNumCol, texNumRow;
	private FileBrowser texBrowser;
	
	private Properties properties;
	private Influencers influencers;
	private Particles particles;
	private Shapes shapes;
	private Options options;
	private CodeOutput codeOutput;
	private TestScene testScene;
	private ScriptBuilder scriptBuilder;
	private SaveProject saver;
	private LoadProject loader;
	private LoadProjectDialog loaderWin;
	private SaveProjectDialog saverWin;
	
	private Vector3f defCamLoc = new Vector3f(0,0,10);
	private float[] defCamRot = new float[] { 0, 3.1415927f, 0 };
	
	public EmitterBuilder(Application app, Screen screen) {
		this.app = app;
		this.screen = screen;
		this.rootNode = (Node)app.getViewPort().getScenes().get(0);
		
		testScene = new TestScene(this);
		
		properties = new Properties(this,screen);
		app.getStateManager().attach(properties);
		
		influencers = new Influencers(this,screen);
		app.getStateManager().attach(influencers);
		
		particles = new Particles(this,screen);
		app.getStateManager().attach(particles);
		
		shapes = new Shapes(this,screen);
		app.getStateManager().attach(shapes);
		
		codeOutput = new CodeOutput(this,shapes,influencers);
		
		scriptBuilder = new ScriptBuilder(this,screen);
		
		saver = new SaveProject(this);
		loader = new LoadProject(this);
		loaderWin = new LoadProjectDialog(this,screen);
		saverWin = new SaveProjectDialog(this,screen);
		options = new Options(this,screen);
		
		addMainView();
		addMatWin();
		
		particles.setParentWindow(properties.getWindow());
		influencers.setParentWindow(particles.getWindow());
	}
	
	public Application getApplication() { return this.app; }
	public Node getRootNode() { return this.rootNode; }
	public Map<String,EmitterStruct> getEmitters() { return this.emitters; }
	
	//<editor-fold desc="Common Builder Methods">
	public boolean validateFloatValue(String f) {
		boolean ret = true;
		try {
			Float.valueOf(f);
		} catch (Exception ex) {
			ret = false;
		}
		return ret;
	}
	public boolean validateIntegerValue(String i) {
		boolean ret = true;
		try {
			Integer.valueOf(i);
		} catch (Exception ex) {
			ret = false;
		}
		return ret;
	}
	public void startChangeState() {
		emitter.setWasEnabled(emitter.getEmitter().isEnabled());
		emitter.getEmitter().setEnabled(false);
	}
	public void resetChangeState() {
		//emitter.getEmitter().reset();
	}
	public void endChangeState() {
		emitter.getEmitter().setEnabled(emitter.getWasEnabled());
	}
	public void setProjectName(String name) { this.projectName = name; }
	public String getProjectName() { return this.projectName; }
	//</editor-fold>
	//<editor-fold desc="AppStates">
	public Properties getProperties() { return this.properties; }
	public Influencers getInfluencers() { return this.influencers; }
	public Particles getParticles() { return this.particles; }
	public Shapes getShapeProperties() { return this.shapes; }
	public LoadProject getLoader() { return this.loader; }
	public SaveProject getSaver() { return this.saver; }
	public CodeOutput getCodeOutput() { return this.codeOutput; }
	public Options getOptions() { return this.options; }
	public TestScene getTestScene() { return this.testScene; }
	public LoadProjectDialog getLoaderWindow() { return this.loaderWin; }
	public SaveProjectDialog getSaverWindow() { return this.saverWin; }
	public ScriptBuilder getScriptBuilder() { return this.scriptBuilder; }
	//</editor-fold>
	//<editor-fold desc="Scene">
	public Vector3f getDefaultCamLoc() {
		return this.defCamLoc;
	}
	public Quaternion getDefaultCamRot() {
		return new Quaternion().fromAngles(defCamRot);
	}
	public void setCamLocation(Vector3f pos) {
		getApplication().getCamera().setLocation(pos);
	}
	public void setCamRotation(float[] angles) {
		Quaternion q = new Quaternion().fromAngles(angles);
		getApplication().getCamera().setRotation(q);
	}
	//</editor-fold>
	
	//<editor-fold desc="Main View">
	private void addMainView() {
		mvForm = new Form(screen);
		
		Layout.reset();
		Layout.pos.set(Layout.pad,Layout.pad);
		Layout.dim.set(100,100);
		mainIC = new Element(screen, "mainIC", Layout.pos, Layout.dim, Vector4f.ZERO, null);
		mainIC.setAsContainerOnly();
		mainIC.setIsMovable(false);
		mainIC.setIsResizable(false);
		mainIC.setResizeE(false);
		mainIC.setResizeW(false);
		mainIC.setResizeN(false);
		mainIC.setResizeS(false);
		
		// Add content
		Layout.reset();
		Layout.dim.set(Layout.w,Layout.h);
		mvName = new TextField(screen, "mvName", Layout.pos, Layout.dim);
		mvName.setType(TextField.Type.ALPHANUMERIC_NOSPACE);
		mainIC.addChild(mvName);
		mvForm.addFormElement(mvName);
		
		Layout.incCol(mvName);
		Layout.dim.set(Layout.h*4+Layout.pad,Layout.h);
		addEmitter = new ButtonAdapter(screen, "addEmitter", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean isToggled) {
				addNewEmitter();
			}
		};
		addEmitter.setText("Create");
		mainIC.addChild(addEmitter);
		mvForm.addFormElement(addEmitter);
		
		mainIC.sizeToContent();
		
		Layout.pos.set(0,0);
		Layout.dim.set((Layout.pad*2)+mainIC.getWidth(),(Layout.pad*2)+mainIC.getHeight());
		mainWin = new Panel(screen,"mainWin",Layout.pos,Layout.dim);
		mainWin.addChild(mainIC);
		mainWin.setIsMovable(false);
		mainWin.setIsResizable(false);
		
		screen.addElement(mainWin);
		mainWin.show();
	}
	public void rebuildMainView() {
		if (mainWin.getChildElementById("mvListIC") != null) {
			mainWin.removeChild(mvListIC);
			mvListIC.removeAllChildren();
		}
		
		Layout.pos.set(Layout.pad,Layout.pad);
		Layout.dim.set(100,100);
		mvListIC = new Element(screen, "mvListIC", Layout.pos, Layout.dim, Vector4f.ZERO, null);
		mvListIC.setAsContainerOnly();
		mvListIC.removeAllChildren();
		mvListIC.setIsMovable(false);
		mvListIC.setIsResizable(false);
		
		Layout.reset();
		for (final EmitterStruct emStruct : emitters.values()) {
			Emitter e = emStruct.getEmitter();
			
			Layout.dim.set(Layout.lWidthM,Layout.h);
			Label lName = Layout.getNewLabel(screen, e.getName());
			lName.setTextAlign(BitmapFont.Align.Left);
			lName.setIgnoreMouse(true);
			mvListIC.addChild(lName);
			
			Layout.x = 100;
			Layout.pos.set(Layout.x,Layout.y);
			Layout.dim.set(Layout.h,Layout.h);
			CheckBox cb = new CheckBox(screen,Layout.pos,Layout.dim) {
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
					emStruct.getEmitter().setEnabled(toggled);
					//emStruct.getEmitter().reset();
				}
			};
			cb.setIsChecked(e.isEnabled());
			mvListIC.addChild(cb);
			
			Layout.x += Layout.h+Layout.pad;
			Layout.pos.set(Layout.x,Layout.y);
			Layout.dim.set(addEmitter.getWidth()*0.25f,Layout.h);
			ButtonAdapter ba1 = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
					populateProperties(properties.getWindow(), emStruct);
				}
			};
			ba1.setText("S");
			mvListIC.addChild(ba1);
			
			Layout.incCol(ba1);
			Layout.x -= Layout.pad;
			Layout.pos.set(Layout.x,Layout.y);
			Layout.dim.set(addEmitter.getWidth()*0.25f,Layout.h);
			ButtonAdapter ba2 = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
					populateProperties(particles.getWindow(), emStruct);
				}
			};
			ba2.setText("P");
			mvListIC.addChild(ba2);
			
			Layout.incCol(ba2);
			Layout.x -= Layout.pad;
			Layout.pos.set(Layout.x,Layout.y);
			Layout.dim.set(addEmitter.getWidth()*0.25f,Layout.h);
			ButtonAdapter ba3 = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
					populateProperties(influencers.getWindow(), emStruct);
				}
			};
			ba3.setText("I");
			mvListIC.addChild(ba3);
			
			Layout.incCol(ba3);
			Layout.x -= Layout.pad;
			Layout.pos.set(Layout.x,Layout.y);
			Layout.dim.set(addEmitter.getWidth()*0.25f,Layout.h);
			ButtonAdapter ba4 = new ButtonAdapter(screen,Layout.pos,Layout.dim) {
				@Override
				public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
					removeEmitter(emStruct);
				}
			};
			ba4.setText("X");
			mvListIC.addChild(ba4);
			
			Layout.incRow();
		}
		mvListIC.sizeToContent();
		mainWin.addChild(mvListIC);
		mainWin.setHeight((Layout.pad*2)+mainIC.getHeight()+mvListIC.getHeight());
		mainIC.setPosition(Layout.pad,mainWin.getHeight()-mainIC.getHeight()-Layout.pad);
		mvListIC.setPosition(Layout.pad,mainWin.getHeight()-mainIC.getHeight()-mvListIC.getHeight()-Layout.pad);
		mainWin.setPosition(0,screen.getHeight()-mainWin.getHeight());
		mvListIC.removeFromParent();
		mainWin.attachChild(mvListIC);
		mainWin.setZOrder(0);
	}
	private void addNewEmitter() {
		if (!emitters.containsKey(mvName.getText())) {
			Emitter em = new Emitter();
                        em.setName(mvName.getText());
                        em.setMaxParticles(100);
                        em.addInfluencers(new ColorInfluencer(),
                                          new AlphaInfluencer(),
                                          new SizeInfluencer());
			
			// Shape & Emissions
			em.setShapeSimpleEmitter();
			em.setDirectionType(DirectionType.Random);
			em.setEmissionsPerSecond(100);
			em.setParticlesPerEmission(1);
			
			// Particle props
                        em.setParticleType(ParticleDataTriMesh.class);
			em.setBillboardMode(BillboardMode.Camera);
			em.setForce(1);
			em.setLife(0.999f);
			em.setSprite("Textures/default.png");
			
			em.getInfluencer(SizeInfluencer.class).addSize(0.1f);
			em.getInfluencer(SizeInfluencer.class).addSize(0f);
			em.initialize(app.getAssetManager());
                        
			rootNode.addControl(em);
			em.setEnabled(true);
			
			EmitterStruct emStruct = new EmitterStruct(em);
			
			emitters.put(mvName.getText(), emStruct);
			rebuildMainView();
			
			mvName.setText("");
		}
	}
	public void rebuildEmitter() {
		if (emitter != null) {
			if (validateIntegerValue(properties.numParticles.getText())) {
				Emitter old = emitter.getEmitter();
				
				boolean enabled = old.isEnabled();
				old.setEnabled(false);
				rootNode.removeControl(old);
				old.reset();
				
				Emitter em = new Emitter();
                                em.setName(old.getName());
                                em.setMaxParticles(
                                        Integer.valueOf(properties.numParticles.getText())
				);
				for (ParticleInfluencer inf : old.getInfluencers()) {
					em.addInfluencer(inf);
				}
				// Shape & Emissions
				if (emitter.getShapeType().equals("Simple Emitter"))
					em.setShapeSimpleEmitter();
				else
					em.setShape(emitter.getEmitter().getShape().getMesh());
				em.setDirectionType(old.getDirectionType());
				em.setParticleEmissionPoint(old.getParticleEmissionPoint());
				em.setEmissionsPerSecond(old.getEmissionsPerSecond());
				em.setParticlesPerEmission(old.getParticlesPerEmission());
				em.setUseRandomEmissionPoint(old.getUseRandomEmissionPoint());
				em.setUseSequentialEmissionFace(old.getUseSequentialEmissionFace());
				em.setUseSequentialSkipPattern(old.getUseSequentialSkipPattern());
				em.setLocalTranslation(old.getLocalTranslation());
				em.setLocalRotation(old.getLocalRotation());
				em.setLocalScale(old.getLocalScale());
				em.setEmitterTestMode(
					old.getEmitterTestModeShape(),
					old.getEmitterTestModeParticles()
				);
				
				// Particle props
                                em.setParticleType(emitter.getParticleMeshType(), emitter.getParticleMesh());
			//	em.initParticles(ParticleDataTriMesh.class, null);
				
				em.setBillboardMode(old.getBillboardMode());
				em.setForceMin(old.getForceMin());
				em.setForceMax(old.getForceMax());
				em.setLifeMin(old.getLifeMin());
				em.setLifeMax(old.getLifeMax());
				em.setParticlesFollowEmitter(old.getParticlesFollowEmitter());
				em.setUseStaticParticles(old.getUseStaticParticles());
				em.setUseVelocityStretching(old.getUseVelocityStretching());
				em.setVelocityStretchFactor(old.getVelocityStretchFactor());
				em.setSprite(
					emitter.getTexturePath(),
					old.getSpriteColCount(),
					old.getSpriteRowCount()
				);
				em.initialize(app.getAssetManager());
                                
				rootNode.addControl(em);
				emitter.setEmitter(em);
				em.setEnabled(enabled);
			}
		}
	}
	public void removeEmitter(EmitterStruct em) {
		rootNode.removeControl(em.getEmitter());
		if (em.getEmitterShapeNode() != null) {
			try {
				rootNode.detachChild(em.getEmitterShapeNode());
			} catch (Exception ex) {  }
		}
		if (em == emitter) {
			properties.getWindow().hide();
			particles.getWindow().hide();
			influencers.getWindow().hide();
			matWin.hide();
			shapes.closeAllShapeWindows();
			influencers.closeAllInfluencerWindows();
		}
		emitters.remove(em.getEmitter().getName());
		rebuildMainView();
	}
	private void populateProperties(Window win, EmitterStruct em) {
		this.emitter = em;
		properties.loadProperties();
		influencers.loadProperties();
		particles.loadProperties();
		
		matDefSelect.setSelectedByCaption(em.getMaterialPath(), false);
		matBlendSelect.setSelectedByCaption(emitter.getEmitter().getMaterial().getAdditionalRenderState().getBlendMode().name(), false);
		texPath.setText(em.getTexturePath());
		texNumCol.setText(String.valueOf(emitter.getEmitter().getSpriteColCount()));
		texNumRow.setText(String.valueOf(emitter.getEmitter().getSpriteRowCount()));
		
		screen.resetTabFocusElement();
		
		shapes.closeAllShapeWindows();
		influencers.closeAllInfluencerWindows();
		
		properties.getWindow().show();
		particles.getWindow().show();
		influencers.getWindow().show();
		
		collapse(win, true);
	}
	public void collapse(Window win, boolean forceOpen) {
		if (win == properties.getWindow()) {
			if (properties.getWindow().getHeight() == 40 || forceOpen)
				properties.configWindow(false);
			else
				properties.configWindow(true);
			particles.configWindow(true);
			influencers.configWindow(true);
		} else if (win == particles.getWindow()) {
			properties.configWindow(true);
			if (particles.getWindow().getHeight() == 40 || forceOpen)
				particles.configWindow(false);
			else
				particles.configWindow(true);
			influencers.configWindow(true);
		} else if (win == influencers.getWindow()) {
			properties.configWindow(true);
			particles.configWindow(true);
			if (influencers.getWindow().getHeight() == 40 || forceOpen)
				influencers.configWindow(false);
			else
				influencers.configWindow(true);
		} else {
			properties.configWindow(true);
			particles.configWindow(true);
			influencers.configWindow(true);
		}
	}
	//</editor-fold>
	
	//<editor-fold desc="Particle Properties Window">
	private void addMatWin() {
		matForm = new Form(screen);
		
		Layout.pos.set(Layout.pad,30+Layout.pad);
		Layout.dim.set(100,100);
		matIC = new Element(screen, "matIC", Layout.pos, Layout.dim, Vector4f.ZERO, null);
		matIC.setAsContainerOnly();
		matIC.setIsMovable(false);
		matIC.setIsResizable(false);
		
		// TODO: Add content
		Layout.reset();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lMatDef = Layout.getNewLabel(screen, "Material Def: ");
		matIC.addChild(lMatDef);
		
		Layout.incCol(lMatDef);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		matDefSelect = new SelectBox(screen, "matDefSelect", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (emitter != null) {
					Material newMat;
					switch (selectedIndex) {
						case 0:
							newMat = new Material(app.getAssetManager(), matDefSelect.getSelectedListItem().getCaption());
							newMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.AlphaAdditive);
							newMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
							matBlendSelect.setSelectedByCaption(BlendMode.AlphaAdditive.name(), true);
							emitter.getEmitter().setMaterial(newMat);
							matDefConfig.setText("");
							matDefConfig.setIsEnabled(false);
							break;
						case  1:
							newMat = new Material(app.getAssetManager(), matDefSelect.getSelectedListItem().getCaption());
							newMat.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Color);
							newMat.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
							matBlendSelect.setSelectedByCaption(BlendMode.Color.name(), true);
							emitter.getEmitter().setMaterial(newMat);
							matDefConfig.setText("");
							matDefConfig.setIsEnabled(false);
							break;
						case 2:
							matDefConfig.setText("Configure");
							matDefConfig.setIsEnabled(true);
							break;
					}
				}
			}
		};
		matDefSelect.addListItem("Common/MatDefs/Misc/Particle.j3md", 0);
		matDefSelect.addListItem("emitter/shaders/Particle.j3md", 1);
		matDefSelect.addListItem("Custom Material ...", 2);
		matIC.addChild(matDefSelect);
		matForm.addFormElement(matDefSelect);
		
		Layout.incCol(matDefSelect);
		Layout.x += Layout.h;
		Layout.pos.set(Layout.x,Layout.y);
		Layout.dim.set(Layout.bWidth,Layout.h);
		matDefConfig = new ButtonAdapter(screen, "matDefConfig", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				switch (matDefSelect.getSelectIndex()) {
					case 1:
						shapes.showSelectPrimitive();
						break;
					case 2:
						shapes.showLoadMesh();
						break;
				}
			}
		};
		matIC.addChild(matDefConfig);
		matForm.addFormElement(matDefConfig);
		matDefConfig.setIsEnabled(false);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lBlendSelect = Layout.getNewLabel(screen, "Blend Mode: ");
		matIC.addChild(lBlendSelect);
		
		Layout.incCol(lBlendSelect);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		matBlendSelect = new SelectBox(screen, "matBlendSelect", Layout.pos, Layout.dim) {
			@Override
			public void onChange(int selectedIndex, Object value) {
				if (emitter != null) {
					emitter.getEmitter()
							.getMaterial()
							.getAdditionalRenderState()
							.setBlendMode(
								BlendMode.valueOf(
									matBlendSelect.getSelectedListItem().getCaption()
								)
							);
				}
			}
		};
		for (BlendMode mode : BlendMode.values()) {
			matBlendSelect.addListItem(mode.name(),mode);
		}
		matIC.addChild(matBlendSelect);
		matForm.addFormElement(matBlendSelect);
		
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lTexPath = Layout.getNewLabel(screen, "Particle Texture: ");
		matIC.addChild(lTexPath);
		
		Layout.incCol(lTexPath);
		Layout.dim.set(Layout.sbWidth,Layout.h);
		texPath = new TextField(screen, "texPath", Layout.pos, Layout.dim);
		texPath.setText("Textures/default.png");
		texPath.setIsEnabled(false);
		matIC.addChild(texPath);
		matForm.addFormElement(texPath);
		
		Layout.incCol(texPath);
		Layout.dim.set(Layout.bWidth,Layout.h);
		applyTexPath = new ButtonAdapter(screen, "applyTexPath", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				if (validateIntegerValue(texNumCol.getText()) &&
					validateIntegerValue(texNumRow.getText())) {
					boolean valid = true;
					try {
						app.getAssetManager().loadTexture(texPath.getText());
					} catch (Exception ex) { valid = false; }
					
					if (valid) {
						emitter.setTexturePath(texPath.getText());
						emitter.getEmitter().setSprite(
							texPath.getText(),
							Integer.valueOf(texNumCol.getText()),
							Integer.valueOf(texNumRow.getText())
						);
					}
				}
			}
		};
		applyTexPath.setText("Set");
		matIC.addChild(applyTexPath);
		matForm.addFormElement(applyTexPath);
		matDefConfig.setIsEnabled(false);
		
		Layout.incRow();
	//	Layout.incCol(lTexPath);
		Layout.dim.set(applyTexPath.getX()+applyTexPath.getWidth(),Layout.h*4);
		texBrowser = new FileBrowser(screen, Layout.pos, Layout.dim, true) {
			@Override
			public void onFileSelect(String path) {
				texPath.setText(path);
			}
		};
		texBrowser.setValidExtensions(".jpg",",jpeg",".png");
		texBrowser.setRootFolder("Textures");
		matIC.addChild(texBrowser);
		matForm.addFormElement(texBrowser);
		
		Layout.incRow();
		Layout.incRow();
		Layout.incRow();
		Layout.dim.set(Layout.lWidthN,Layout.h);
		Label lTexNumCol = Layout.getNewLabel(screen, "# of Images - Cols: ");
		matIC.addChild(lTexNumCol);
		
		Layout.incCol(lTexNumCol);
		Layout.dim.set(Layout.floatW,Layout.h);
		texNumCol = new TextField(screen, "texNumCol", Layout.pos, Layout.dim);
		texNumCol.setType(TextField.Type.NUMERIC);
		texNumCol.setText("1");
		matIC.addChild(texNumCol);
		matForm.addFormElement(texNumCol);
		
		Layout.incCol(texNumCol);
		Layout.dim.set(Layout.lWidthM,Layout.h);
		Label lTexNumRow = Layout.getNewLabel(screen, "Rows: ");
		matIC.addChild(lTexNumRow);
		
		Layout.incCol(lTexNumRow);
		Layout.dim.set(Layout.floatW,Layout.h);
		texNumRow = new TextField(screen, "texNumRow", Layout.pos, Layout.dim);
		texNumRow.setType(TextField.Type.NUMERIC);
		texNumRow.setText("1");
		matIC.addChild(texNumRow);
		matForm.addFormElement(texNumRow);
		
		matIC.sizeToContent();
		
		Layout.dim.set((Layout.pad*2)+matIC.getWidth(),30+(Layout.pad*2)+matIC.getHeight());
		matWin = new Window(screen,"matWin",Layout.pos,Layout.dim);
		matWin.addChild(matIC);
		matWin.setIsMovable(true);
		matWin.setIsResizable(false);
		matWin.setWindowTitle("Material Properties");
		
		Layout.pos.set(matWin.getDragBar().getWidth()-Layout.h-3,3);
		Layout.dim.set(Layout.h,Layout.h);
		ButtonAdapter matClose = new ButtonAdapter(screen, "matClose", Layout.pos, Layout.dim) {
			@Override
			public void onButtonMouseLeftUp(MouseButtonEvent evt, boolean toggled) {
				matWin.hide();
			}
		};
		matClose.setText("X");
		matWin.getDragBar().addChild(matClose);
		
		for (int i = 0; i < texBrowser.fileList.getListItems().size(); i++) {
			if (texBrowser.fileList.getListItem(i).getCaption().indexOf("default.png") != -1) {
				texBrowser.fileList.setSelectedIndex(i);
				texBrowser.scrollToSelected();
				break;
			}
		}
		
		
		screen.addElement(matWin);
		matWin.centerToParent();
		matWin.hide();
	}
	public void showMatWin() {
		try {
			matDefSelect.setSelectedByCaption(emitter.getMaterialPath(), true);
			matBlendSelect.setSelectedByCaption(emitter.getEmitter().getMaterial().getAdditionalRenderState().getBlendMode().name(), true);
			texPath.setText(emitter.getTexturePath());
			texNumCol.setText(String.valueOf(emitter.getEmitter().getSpriteColCount()));
			texNumRow.setText(String.valueOf(emitter.getEmitter().getSpriteRowCount()));
			matWin.show();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	//</editor-fold>
	
	@Override
	public void initialize(AppStateManager stateManager, Application app) {
		super.initialize(stateManager, app);
		app.getInputManager().addMapping("BUILDER_OPTIONS", new KeyTrigger(KeyInput.KEY_ESCAPE));
		app.getInputManager().addListener(this, "BUILDER_OPTIONS");
	}
	
	@Override
	public void update(float tpf) {  }
	
	@Override
	public void cleanup() {
		super.cleanup();
	}
	
	@Override
	public void onAction(String name, boolean isPressed, float tpf) {
		if (name.equals("BUILDER_OPTIONS") && !isPressed) {
			options.toggleOptionWin();
		}
	}
}
