package emitterbuilder.gui;

import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector4f;
import com.jme3.scene.Node;
import com.jme3.texture.Texture;
import java.io.File;
import java.util.concurrent.Callable;
import tonegod.gui.controls.extras.OSRViewPort;
import tonegod.gui.controls.lists.SelectList;
import tonegod.gui.core.Element;
import tonegod.gui.core.ElementManager;
import tonegod.gui.core.utils.UIDUtil;

/**
 *
 * @author t0neg0d
 */
public abstract class FileBrowser extends Element {
	private File file;
	private String basePath, currentPath;
	private File[] files;
	public SelectList fileList;
	private Element preview;
	private OSRViewPort subView = null;
	private String[] extensions = null;
	private Node newScene = new Node("OSR");
	private Material wireFrame;
	private Node model = null;
	private Texture tex;
	private Vector2f listDim, previewPos, previewDim;
	private boolean init = false;
	
	public FileBrowser(ElementManager screen, Vector2f position, boolean showPreviewPanel) {
		this(screen, UIDUtil.getUID(), position,
			new Vector2f(400,120),
			Vector4f.ZERO,
			null,
			showPreviewPanel
		);
	}
	
	public FileBrowser(ElementManager screen, Vector2f position, Vector2f dimensions, boolean showPreviewPanel) {
		this(screen, UIDUtil.getUID(), position, dimensions,
			Vector4f.ZERO,
			null,
			showPreviewPanel
		);
	}
	
	public FileBrowser(ElementManager screen, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, boolean showPreviewPanel) {
		this(screen, UIDUtil.getUID(), position, dimensions,resizeBorders,defaultImg, showPreviewPanel);
	}
	
	public FileBrowser(ElementManager screen, String UID, Vector2f position, boolean showPreviewPanel) {
		this(screen, UID, position,
			new Vector2f(400,120),
			Vector4f.ZERO,
			null,
			showPreviewPanel
		);
	}
	
	public FileBrowser(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, boolean showPreviewPanel) {
		this(screen, UID, position, dimensions,
			Vector4f.ZERO,
			null,
			showPreviewPanel
		);
	}
	
	public FileBrowser(ElementManager screen, String UID, Vector2f position, Vector2f dimensions, Vector4f resizeBorders, String defaultImg, boolean showPreviewPanel) {
		super(screen, UID, position, dimensions, resizeBorders, defaultImg);
	//	setAsContainerOnly();
		
		wireFrame = new Material(screen.getApplication().getAssetManager(), "Common/MatDefs/Misc/Unshaded.j3md");
		wireFrame.setColor("Color", ColorRGBA.Blue);
		wireFrame.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
		wireFrame.getAdditionalRenderState().setWireframe(true);
		
		listDim = new Vector2f(dimensions.x-dimensions.y, dimensions.y);
		previewDim = new Vector2f(dimensions.y,dimensions.y);
		previewPos = new Vector2f(dimensions.x-dimensions.y,0);
		
		fileList = new SelectList(screen, UID + ":SelectList",
			Vector2f.ZERO,
			listDim,
			screen.getStyle("Window").getVector4f("resizeBorders"),
			screen.getStyle("Window").getString("defaultImg")
		) {
			@Override
			public void onChange() {
				if (this.getListItem(this.getSelectedIndex()).getCaption().equals("..")) {
					currentPath = (String)this.getListItem(this.getSelectedIndex()).getValue();
					setFile();
					displayContents();
				} else if (((String)this.getListItem(this.getSelectedIndex()).getValue()).equals("DIR")) {
					String dir = (String)this.getListItem(this.getSelectedIndex()).getCaption();
					dir = dir.substring(dir.indexOf("<")+1,dir.indexOf(">"));
					dir += "/";
					currentPath += dir;
					setFile();
					displayContents();
				} else {
					String caption = this.getListItem(this.getSelectedIndex()).getCaption();
					if (caption.indexOf(".jpg") != -1 ||
						caption.indexOf(".jpeg") != -1 ||
						caption.indexOf(".png") != -1) {
						String path = currentPath;
						path = path.substring(path.indexOf("assets"), path.length());
						path = path.substring(path.indexOf("/")+1, path.length());
						Texture tex = screen.getApplication().getAssetManager().loadTexture(path + caption);
						float texWidth = tex.getImage().getWidth();
						float texHeight = tex.getImage().getHeight();
						preview.setTextureAtlasImage(tex, "x=0|y=0|w=" + texWidth + "|h=" + texHeight);
						preview.show();
					//	subView.hide();
						onFileSelect(path + caption);
					} else if (caption.indexOf(".j3o") != -1) {
						String path = currentPath;
						path = path.substring(path.indexOf("assets"), path.length());
						path = path.substring(path.indexOf("/")+1, path.length());
						if (init) {
							if (subView != null) {
								if (model != null) {
									try {
										model.removeFromParent();
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
								model = (Node)screen.getApplication().getAssetManager().loadModel(path + caption);
								model.setMaterial(wireFrame);
								try {
									newScene.attachChild(model);
								} catch (Exception ex) {
									ex.printStackTrace();
								}
								preview.hide();
							}
						}
					//	subView.show();
						onFileSelect(path + caption);
					}
					preview.setPosition(previewPos);
					preview.setDimensions(previewDim);
				}
			}
		};
		addChild(fileList);
		
	//	if (showPreviewPanel) {
			
			preview = new Element(screen, UID + ":Preview",
				previewPos,
				previewDim,
				Vector4f.ZERO,
				screen.getStyle("Common").getString("blankImg")
			);
			preview.getModel().setGradientFillVertical(ColorRGBA.Black, ColorRGBA.Black);
			addChild(preview);
	//	}
		
	//	sizeToContent();
		
		try {
			basePath = getClass().getClassLoader().getResource(
				"emitterbuilder/builder/project/locator.xml"
			).toString();
			basePath = basePath.substring(basePath.indexOf("/")+1, basePath.indexOf("build"));
			basePath += "assets/";
		} catch (Exception ex1) {  }
	}
	
	public void setValidExtensions(String... extension) {
		this.extensions = extension;
	}
	
	public void setRootFolder(String folder) {
		currentPath = basePath + folder;
		if (folder.indexOf("/") == -1)
			currentPath += "/";
		populateList();
	}
	
	private void populateList() {
		setFile();
		displayContents();
	}
	
	private void setFile() {
		file = new File(currentPath);
		files = file.listFiles();
	}
	
	private void displayContents() {
		fileList.removeAllListItems();
		if (!currentPath.equals(basePath)) {
			String tempPath = currentPath.substring(0,currentPath.length()-1);
			tempPath = tempPath.substring(0,tempPath.lastIndexOf("/")+1);
			
			fileList.addListItem("..",tempPath);
		}
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isDirectory()) {
				fileList.addListItem("<" + f.getName() + ">","DIR");
			}
		}
		for (int i = 0; i < files.length; i++) {
			File f = files[i];
			if (f.isFile()) {
				boolean show = false;
				if (extensions != null) {
					for (int x = 0; x < extensions.length; x++) {
						String fName = f.getName();
						fName = fName.substring(fName.lastIndexOf("."),fName.length());
						if (fName.equals(extensions[x]))
							show = true;
					}
					if (show)
						fileList.addListItem(f.getName(),f.getAbsolutePath());
				} else
					fileList.addListItem(f.getName(),f.getAbsolutePath());
			}
		}
	}
	
	public abstract void onFileSelect(String path);
	
	public void scrollToSelected() {
		int rIndex = fileList.getSelectedIndex();
		float diff = (rIndex+1) * fileList.getListItemHeight();
		
		float y = -(fileList.getScrollableHeight()-diff);
		
		if (FastMath.abs(y) > fileList.getScrollableHeight()) {
			y = fileList.getScrollableHeight();
		}
		
		this.fileList.scrollThumbYTo(
			( y )
		);
	}
	
	public OSRViewPort getOSRViewPort() {
		return this.subView;
	}
	
	public void add3DPreview() {
		if (!init) {
			subView = new OSRViewPort(screen, getUID() + ":SubView",
				previewPos,
				previewDim,
				Vector4f.ZERO,
				screen.getStyle("Common").getString("blankImg")
			);
			subView.setOSRBridge(newScene, 100, (int)listDim.y);
			subView.setBackgroundColor(ColorRGBA.Black);
			subView.setUseCameraControlRotate(true);
			subView.setUseCameraControlZoom(true);
			subView.setLeftMouseButtonRotation(true);
			subView.setCameraDistance(5f);
			subView.setCameraMinDistance(.5f);
			subView.setCameraMaxDistance(50f);
			subView.setCameraHorizonalRotation(90*FastMath.DEG_TO_RAD);
			subView.setCameraVerticalRotation(0);
			subView.setIsMovable(false);
			subView.setIsResizable(false);
			subView.setScaleEW(false);
			subView.setScaleNS(false);
		//	subView.getOSRBridge().setEnabled(false);
			addChild(subView);
			init = true;
		} else {
			attachChild(subView);
		}
	}
	
	public void remove3DPreview() {
		subView.removeFromParent();
	//	subView = null;
	}
}
