package emitterbuilder.builder.project;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.animation.LoopMode;
import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetKey;
import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Torus;
import emitter.Emitter;
import emitter.Emitter.BillboardMode;
import emitter.Emitter.ForcedStretchAxis;
import emitter.Emitter.ParticleEmissionPoint;
import emitter.EmitterMesh.DirectionType;
import emitter.Interpolation;
import emitter.influencers.AlphaInfluencer;
import emitter.influencers.ColorInfluencer;
import emitter.influencers.DestinationInfluencer;
import emitter.influencers.GravityInfluencer;
import emitter.influencers.GravityInfluencer.GravityAlignment;
import emitter.influencers.ImpulseInfluencer;
import emitter.influencers.ParticleInfluencer;
import emitter.influencers.PhysicsInfluencer;
import emitter.influencers.PhysicsInfluencer.CollisionReaction;
import emitter.influencers.RadialVelocityInfluencer;
import emitter.influencers.RadialVelocityInfluencer.RadialPullAlignment;
import emitter.influencers.RadialVelocityInfluencer.RadialPullCenter;
import emitter.influencers.RadialVelocityInfluencer.RadialUpAlignment;
import emitter.influencers.RotationInfluencer;
import emitter.influencers.SizeInfluencer;
import emitter.influencers.SpriteInfluencer;
import emitter.particle.ParticleDataImpostorMesh;
import emitter.particle.ParticleDataPointMesh;
import emitter.particle.ParticleDataTemplateMesh;
import emitter.particle.ParticleDataTriMesh;
import emitterbuilder.builder.EmitterBuilder;
import emitterbuilder.builder.datastructures.EmitterStruct;
import emitterbuilder.builder.datastructures.ScriptStruct;
import emitterbuilder.builder.scripts.ScriptBuilder;
import emitterbuilder.builder.scripts.ScriptBuilder.ScriptType;
import emitterbuilder.gui.utils.InterpolationUtil;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import tonegod.gui.core.utils.XMLHelper;

/**
 *
 * @author t0neg0d
 */
public class LoadProject {
	EmitterBuilder builder;
	AssetManager assetManager;
	Document doc;
	String path;
	AssetKey key;
	AssetInfo info;
	
	public LoadProject(EmitterBuilder builder) {
		this.builder = builder;
		this.assetManager = builder.getApplication().getAssetManager();
		
		try {
			path = getClass().getClassLoader().getResource(
				"emitterbuilder/builder/project/locator.xml"
			).toString();
			path = path.substring(path.indexOf("/")+1, path.indexOf("build"));
			path += "assets/XML/saves/";
		} catch (Exception ex1) {  }
	}
	
	public void load(String fileName) {
		scrubScene();
		
		try {
			String filePath = path + fileName;
			filePath = filePath.substring(filePath.indexOf("XML"), filePath.length());
			
			InputStream file = null;
			
			key = new AssetKey(filePath);
			info = assetManager.locateAsset(key);

			try {
				file = info.openStream();
			} catch (Exception ex) {
				System.out.println("Failed to open asset as stream.");
				System.out.println(ex);
			}
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			doc = db.parse(file);
			doc.getDocumentElement().normalize();
			
			// Start Parse
			NodeList nodeLst = doc.getElementsByTagName("project");
			
			Node project = nodeLst.item(0);
			if (project.getNodeType() == Node.ELEMENT_NODE) {
				builder.setProjectName(XMLHelper.getNodeAttributeValue(project, "name"));
				
				NodeList projectNodes = project.getChildNodes();
				Node testScene = null;
				Node camLoc = null;
				Node camRot = null;
				Node emitters;
				Node scripts;
				
				//<editor-fold desc="Parse Scene">
				for (int i = 0; i < projectNodes.getLength(); i++) {
					if (projectNodes.item(i).getNodeName().equals("param")) {
						if (XMLHelper.getNodeAttributeValue(projectNodes.item(i), "name").equals("testScene")) {
							testScene = projectNodes.item(i);
						} else if (XMLHelper.getNodeAttributeValue(projectNodes.item(i), "name").equals("camLoc")) {
							camLoc = projectNodes.item(i);
						} else if (XMLHelper.getNodeAttributeValue(projectNodes.item(i), "name").equals("camRot")) {
							camRot = projectNodes.item(i);
						}
					} else if (projectNodes.item(i).getNodeName().equals("emitters")) {
						emitters = projectNodes.item(i);
					} else if (projectNodes.item(i).getNodeName().equals("scripts")) {
						scripts = projectNodes.item(i);
					}
				}
				if (camLoc != null) {
					builder.setCamLocation(
						new Vector3f(
							Float.valueOf(XMLHelper.getNodeAttributeValue(camLoc, "x")),
							Float.valueOf(XMLHelper.getNodeAttributeValue(camLoc, "y")),
							Float.valueOf(XMLHelper.getNodeAttributeValue(camLoc, "z"))
						)
					);
				}
				if (camRot != null) {
					float[] angles = new float[] {
						Float.valueOf(XMLHelper.getNodeAttributeValue(camRot, "x")),
						Float.valueOf(XMLHelper.getNodeAttributeValue(camRot, "y")),
						Float.valueOf(XMLHelper.getNodeAttributeValue(camRot, "z"))
					};
					builder.setCamRotation(angles);
				}
				builder.getOptions().setUseTestScene(Boolean.valueOf(XMLHelper.getNodeAttributeValue(testScene, "value")));
				//</editor-fold>
				
				// Parse emitters
				NodeList emitterLst = doc.getElementsByTagName("emitter");
				NodeList scriptLst = doc.getElementsByTagName("scripts");
				Node scrpts = scriptLst.item(0);
				
				for (int i = 0; i < emitterLst.getLength(); i++) {
					Node em = emitterLst.item(i);
					int maxParts = 0;
					
					NodeList emNodeLst = em.getChildNodes();
					List<Node> params = new ArrayList();
					Node shape = null;
					Node part = null;
					Node mat = null;
					Node infs = null;
					
					/****************************************
					 *  PARSE EMITTER PARAMS & DATA GROUPS  *
					 ****************************************/
					//<editor-fold desc="Emitter Params">
					for (int p = 0; p < emNodeLst.getLength(); p++) {
						Node emNode = emNodeLst.item(p);
						
						if (emNode.getNodeName().equals("param"))
							params.add(emNode);
						else if (emNode.getNodeName().equals("shape"))
							shape = emNode;
						else if (emNode.getNodeName().equals("particle"))
							part = emNode;
						else if (emNode.getNodeName().equals("material"))
							mat = emNode;
						else if (emNode.getNodeName().equals("influencers"))
							infs = emNode;
					}
					
					//</editor-fold>
					
					/**************************
					 *  VARIABLE DECLARATION  *
					 **************************/
					//<editor-fold desc="Variables">
					String emName = XMLHelper.getNodeAttributeValue(em, "name");
					Vector3f loc = new Vector3f();
					Vector3f scale = new Vector3f();
					float[] angles = new float[3];
					boolean testModeShape = false;
					boolean testModeParticles = false;
					int emissionsPerSecond = 0;
					int particlesPerEmission = 0;
					boolean useRandomEmissionPoint = false;
					boolean useSequentialEmissionFace = false;
					boolean useSequentialSkipPattern = false;
					Interpolation interpolation = Interpolation.linear;
					String shapeType = "";
					String shapeClass = "";
					DirectionType directionType = DirectionType.Random;
					String shapePath = "Models/AnimTest.j3o";
					boolean animate = true;
					String animationName = "";
					float animSpeed = 1, blendTime = 1;
					com.jme3.scene.Node esNode = null;
					AnimControl animControl = null;
					AnimChannel animChannel = null;;
					float ESF1 = 0, ESF2 = 0, ESF3 = 0;
					int ESI1 = 0, ESI2 = 0;
					float PMF1 = 0, PMF2 = 0, PMF3 = 0;
					int PMI1 = 0, PMI2 = 0;
					Mesh emitterMesh = null;
					ParticleEmissionPoint particleEmissionPoint = ParticleEmissionPoint.Particle_Center;
					BillboardMode billboardMode = BillboardMode.Camera;
					float forceMin = 0, forceMax = 0, lifeMin = 0, lifeMax = 0;
					boolean particlesFollowEmitter = false;
					boolean useStaticParticles = false;
					boolean useVelocityStretching = false;
					float velocityStretchFactor = 0.5f;
					ForcedStretchAxis forcedStretchAxis = ForcedStretchAxis.Y;
					String particleType = "";
					String particleMeshType = "";
					String particleMesh = "";
					Mesh particleShapeMesh = null;
					String particleMeshPath = "";
					String matDef = "Common/MatDefs/Misc/Particle.j3md";
					BlendMode blendMode = BlendMode.AlphaAdditive;
					String texturePath = "Textures/default.png";
					int spriteColCount = 1, spriteRowCount = 1;
					//</editor-fold>
					
					/**************************
					 *  PARSE EMITTER PARAMS  *
					 **************************/
					//<editor-fold desc="Emitter Params">
					for (Node n : params) {
						String paramName = XMLHelper.getNodeAttributeValue(n, "name");
						
						if (paramName.equals("maxParticles"))
							maxParts = Integer.valueOf(XMLHelper.getNodeAttributeValue(n, "value"));
						else if (paramName.equals("translation")) {
							loc.set(
								Float.valueOf(XMLHelper.getNodeAttributeValue(n, "x")),
								Float.valueOf(XMLHelper.getNodeAttributeValue(n, "y")),
								Float.valueOf(XMLHelper.getNodeAttributeValue(n, "z"))
							);
						} else if (paramName.equals("scale")) {
							scale.set(
								Float.valueOf(XMLHelper.getNodeAttributeValue(n, "x")),
								Float.valueOf(XMLHelper.getNodeAttributeValue(n, "y")),
								Float.valueOf(XMLHelper.getNodeAttributeValue(n, "z"))
							);
						} else if (paramName.equals("rotation")) {
							angles[0] = Float.valueOf(XMLHelper.getNodeAttributeValue(n, "x"));
							angles[1] = Float.valueOf(XMLHelper.getNodeAttributeValue(n, "y"));
							angles[2] = Float.valueOf(XMLHelper.getNodeAttributeValue(n, "z"));
						} else if (paramName.equals("testModeShape"))
							testModeShape = Boolean.valueOf(XMLHelper.getNodeAttributeValue(n, "value"));
						else if (paramName.equals("emissionsPerSecond"))
							emissionsPerSecond = Integer.valueOf(XMLHelper.getNodeAttributeValue(n, "value"));
						else if (paramName.equals("particlesPerEmission"))
							particlesPerEmission = Integer.valueOf(XMLHelper.getNodeAttributeValue(n, "value"));
						else if (paramName.equals("useRandomEmissionPoint"))
							useRandomEmissionPoint = Boolean.valueOf(XMLHelper.getNodeAttributeValue(n, "value"));
						else if (paramName.equals("useSequentialEmissionFace"))
							useSequentialEmissionFace = Boolean.valueOf(XMLHelper.getNodeAttributeValue(n, "value"));
						else if (paramName.equals("useSequentialSkipPattern"))
							useSequentialSkipPattern = Boolean.valueOf(XMLHelper.getNodeAttributeValue(n, "value"));
						else if (paramName.equals("interpolation"))
							interpolation = InterpolationUtil.getInterpolationByName(XMLHelper.getNodeAttributeValue(n, "value"));
					}
					//</editor-fold>
					
					/******************************
					 *  PARSE EMITTER SHAPE DATA  *
					 ******************************/
					//<editor-fold desc="Shape Data">
					if (shape != null) {
						NodeList shapeNodeLst = shape.getChildNodes();
						for (int p = 0; p < shapeNodeLst.getLength(); p++) {
							Node param = shapeNodeLst.item(p);
							String paramName = XMLHelper.getNodeAttributeValue(param, "name");
							if (paramName.equals("shapeType"))
								shapeType = XMLHelper.getNodeAttributeValue(param, "value");
							else if (paramName.equals("shapeClass"))
								shapeClass = XMLHelper.getNodeAttributeValue(param, "value");
							else if (paramName.equals("directionType"))
								directionType = DirectionType.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("ESF1"))
								ESF1 = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("ESF2"))
								ESF2 = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("ESF3"))
								ESF3 = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("ESI1"))
								ESI1 = Integer.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("ESI2"))
								ESI2 = Integer.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("shapePath"))
								shapePath = String.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("animate"))
								animate = Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("animationName"))
								animationName = String.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("animSpeed"))
								animSpeed = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("blendTime"))
								blendTime = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
						}
					}
					//</editor-fold>
					
					/*************************
					 *  PARSE PARTICLE DATA  *
					 *************************/
					//<editor-fold desc="Particle Data">
					if (part != null) {
						NodeList partNodeLst = part.getChildNodes();
						for (int p = 0; p < partNodeLst.getLength(); p++) {
							Node param = partNodeLst.item(p);
							String paramName = XMLHelper.getNodeAttributeValue(param, "name");
							if (paramName.equals("testModeParticles"))
								testModeParticles = Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("particleEmissionPoint"))
								particleEmissionPoint = ParticleEmissionPoint.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("billboardMode"))
								billboardMode = BillboardMode.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("billboardMode"))
								billboardMode = BillboardMode.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("forceMin"))
								forceMin = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("forceMax"))
								forceMax = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("lifeMin"))
								lifeMin = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("lifeMax"))
								lifeMax = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("particlesFollowEmitter"))
								particlesFollowEmitter = Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("useStaticParticles"))
								useStaticParticles = Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("useVelocityStretching"))
								useVelocityStretching = Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("velocityStretchFactor"))
								velocityStretchFactor = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("forcedStretchAxis"))
								forcedStretchAxis = ForcedStretchAxis.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("particleType"))
								particleType = String.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("particleMeshType"))
								particleMeshType = String.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("particleMesh"))
								particleMesh = String.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("PMF1"))
								PMF1 = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("PMF2"))
								PMF2 = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("PMF3"))
								PMF3 = Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("PMI1"))
								PMI1 = Integer.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("PMI2"))
								PMI2 = Integer.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
						}
					}
					//</editor-fold>
					
					/********************
					 *  PARSE MATERIAL  *
					 ********************/
					//<editor-fold desc="Material Data">
					if (mat != null) {
						NodeList matNodeLst = mat.getChildNodes();
						for (int p = 0; p < matNodeLst.getLength(); p++) {
							Node param = matNodeLst.item(p);
							String paramName = XMLHelper.getNodeAttributeValue(param, "name");
							if (paramName.equals("matDef"))
								matDef = String.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("blendMode"))
								blendMode = BlendMode.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("texturePath"))
								texturePath = String.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("spriteColCount"))
								spriteColCount = Integer.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
							else if (paramName.equals("spriteRowCount"))
								spriteRowCount = Integer.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
						}
					}
					//</editor-fold>
					
					Material material = new Material(assetManager, matDef);
					material.getAdditionalRenderState().setBlendMode(blendMode);
					material.getAdditionalRenderState().setFaceCullMode(RenderState.FaceCullMode.Off);
					
					/********************
					 *  CREATE EMITTER  *
					 ********************/
					//<editor-fold desc="Create New Emitter">
					Emitter emitter = new Emitter();
                                        emitter.setName(emName);
                                        emitter.setMaxParticles(maxParts);
					
					emitter.setLocalTranslation(loc);
					emitter.setLocalScale(scale);
					emitter.setLocalRotation(new Quaternion().fromAngles(angles));
					
					emitter.setEmitterTestMode(
						testModeShape,
						testModeParticles
					);
					
					// Shape & Emissions
					if (shapeType.equals("Simple Emitter")) {
						emitter.setShapeSimpleEmitter();
					} else if (shapeType.equals("JME Primitive Emitter")) {
						if (shapeClass.equals("Box")) {
							Box box = new Box(ESF1,ESF2,ESF3);
							emitterMesh = box;
							emitter.setShape(box);
						} else if (shapeClass.equals("Cylinder")) {
							Cylinder cylinder = new Cylinder(ESI1,ESI2,ESF1,ESF2);
							emitterMesh = cylinder;
							emitter.setShape(cylinder);
						} else if (shapeClass.equals("Dome")) {
							Dome dome = new Dome(ESI1,ESI2,ESF1);
							emitterMesh = dome;
							emitter.setShape(dome);
						} else if (shapeClass.equals("Quad")) {
							Quad quad = new Quad(ESF1, ESF2);
							emitterMesh = quad;
							emitter.setShape(quad);
						} else if (shapeClass.equals("Sphere")) {
							Sphere sphere = new Sphere(ESI1,ESI2,ESF1);
							emitterMesh = sphere;
							emitter.setShape(sphere);
						} else if (shapeClass.equals("Torus")) {
							Torus torus = new Torus(ESI1,ESI2,ESF1,ESF2);
							emitterMesh = torus;
							emitter.setShape(torus);
						}
					} else if (shapeType.equals("Mesh-base Emitter")) {
						esNode = (com.jme3.scene.Node)assetManager.loadModel(shapePath);
						emitterMesh = ((Geometry)esNode.getChild(0)).getMesh();
						builder.getRootNode().attachChild(esNode);
						emitter.setShape(emitterMesh);
						if (animate) {
							animControl = esNode.getControl(AnimControl.class);
							animChannel = animControl.createChannel();
							animChannel.setAnim(animationName, blendTime);
							animChannel.setLoopMode(LoopMode.Loop);
							animChannel.setSpeed(animSpeed);
						}
					}
					emitter.setDirectionType(directionType);
					emitter.setEmissionsPerSecond(emissionsPerSecond);
					emitter.setParticlesPerEmission(particlesPerEmission);
					emitter.setUseRandomEmissionPoint(useRandomEmissionPoint);
					emitter.setUseSequentialEmissionFace(useSequentialEmissionFace);
					emitter.setUseSequentialSkipPattern(useSequentialSkipPattern);
					emitter.setInterpolation(interpolation);
					
					// Particle props
					emitter.setParticleEmissionPoint(particleEmissionPoint);
					emitter.setBillboardMode(billboardMode);
					emitter.setForceMinMax(forceMin,forceMax);
					emitter.setLifeMinMax(lifeMin,lifeMax);
					emitter.setParticlesFollowEmitter(particlesFollowEmitter);
					emitter.setUseStaticParticles(useStaticParticles);
					emitter.setUseVelocityStretching(useVelocityStretching);
					emitter.setVelocityStretchFactor(velocityStretchFactor);
					emitter.setForcedStretchAxis(forcedStretchAxis);
					
					Class particleData;
					
					if (particleType.equals("Quad")) {
						particleData = ParticleDataTriMesh.class;
						emitter.setParticleType(ParticleDataTriMesh.class);
					} else if (particleType.equals("Point")) {
						particleData = ParticleDataPointMesh.class;
						emitter.setParticleType(ParticleDataPointMesh.class);
					} else if (particleType.equals("Impostor")) {
						particleData = ParticleDataImpostorMesh.class;
						emitter.setParticleType(ParticleDataImpostorMesh.class);
					} else if (particleType.equals("Primitive As Template")) {
						particleData = ParticleDataTemplateMesh.class;
						if (particleMesh.equals("Box")) {
							Box box = new Box(PMF1,PMF2,PMF3);
							particleShapeMesh = box;
						} else if (particleMesh.equals("Cylinder")) {
							Cylinder cylinder = new Cylinder(PMI1,PMI2,PMF1,PMF2);
							particleShapeMesh = cylinder;
						} else if (particleMesh.equals("Dome")) {
							Dome dome = new Dome(PMI1,PMI2,PMF1);
							particleShapeMesh = dome;
						} else if (particleMesh.equals("Quad")) {
							Quad quad = new Quad(PMF1, PMF2);
							particleShapeMesh = quad;
						} else if (particleMesh.equals("Sphere")) {
							Sphere sphere = new Sphere(PMI1,PMI2,PMF1);
							particleShapeMesh = sphere;
						} else if (particleMesh.equals("Torus")) {
							Torus torus = new Torus(PMI1,PMI2,PMF1,PMF2);
							particleShapeMesh = torus;
						}
						emitter.setParticleType(ParticleDataTemplateMesh.class, particleShapeMesh);
					} else {
						particleData = ParticleDataTemplateMesh.class;
						emitter.setParticleType(ParticleDataTemplateMesh.class, particleShapeMesh);
					}
					
					emitter.setSprite(texturePath, spriteColCount, spriteRowCount);
					emitter.setMaterial(material);
				//	emitter.setSprite(texturePath, spriteColCount, spriteRowCount);
					
					//</editor-fold>
					
					/***********************
					 *  PARSE INFLUENCERS  *
					 ***********************/
					//<editor-fold desc="Influencer Data">
					if (infs != null) {
						NodeList infNodeLst = infs.getChildNodes();
						for (int p = 0; p < infNodeLst.getLength(); p++) {
							Node inf = infNodeLst.item(p);
							String infType = XMLHelper.getNodeAttributeValue(inf, "type");
							ParticleInfluencer in = null;
							if (infType.equals("GravityInfluencer")) {
								in = parseGravityInfluencer(inf);
							} else if (infType.equals("ColorInfluencer")) {
								in = parseColorInfluencer(inf);
							} else if (infType.equals("AlphaInfluencer")) {
								in = parseAlphaInfluencer(inf);
							} else if (infType.equals("SizeInfluencer")) {
								in = parseSizeInfluencer(inf);
							} else if (infType.equals("RotationInfluencer")) {
								in = parseRotationInfluencer(inf);
							} else if (infType.equals("SpriteInfluencer")) {
								in = parseSpriteInfluencer(inf);
							} else if (infType.equals("ImpulseInfluencer")) {
								in = parseImpulseInfluencer(inf);
							} else if (infType.equals("PhysicsInfluencer")) {
								in = parsePhysicsInfluencer(inf);
							} else if (infType.equals("RadialVelocityInfluencer")) {
								in = parseRadialVelocityInfluencer(inf);
							} else if (infType.equals("DestinationInfluencer")) {
								in = parseDestinationInfluencer(inf);
							}
							emitter.addInfluencer(in);
						}
					}
					//</editor-fold>
					emitter.initialize(assetManager);
                                        
					builder.getRootNode().addControl(emitter);
					emitter.setEnabled(true);
					
					EmitterStruct emStruct = new EmitterStruct(emitter);
					emStruct.setShapeType(shapeType);
					emStruct.setEmitterMesh(emitterMesh);
					if (shapeType.equals("JME Primitive Emitter")) {
						if (shapeClass.equals("Box")) {
							emStruct.setESF1(ESF1);
							emStruct.setESF2(ESF2);
							emStruct.setESF3(ESF3);
						} else if (shapeClass.equals("Cylinder")) {
							emStruct.setESI1(ESI1);
							emStruct.setESI2(ESI2);
							emStruct.setESF1(ESF1);
							emStruct.setESF2(ESF2);
						} else if (shapeClass.equals("Dome")) {
							emStruct.setESI1(ESI1);
							emStruct.setESI2(ESI2);
							emStruct.setESF1(ESF1);
						} else if (shapeClass.equals("Quad")) {
							emStruct.setESF1(ESF1);
							emStruct.setESF2(ESF2);
						} else if (shapeClass.equals("Sphere")) {
							emStruct.setESI1(ESI1);
							emStruct.setESI2(ESI2);
							emStruct.setESF1(ESF1);
						} else if (shapeClass.equals("Torus")) {
							emStruct.setESI1(ESI1);
							emStruct.setESI2(ESI2);
							emStruct.setESF1(ESF1);
							emStruct.setESF2(ESF2);
						}
					} else if (shapeType.equals("Mesh-base Emitter")) {
						emStruct.setAssetMeshPath(shapePath);
						emStruct.setEmitterShapeNode(esNode);
						emStruct.setAnimControl(animControl);
						emStruct.setAnimChannel(animChannel);
						emStruct.setAnimate(animate);
						emStruct.setAnimationName(animationName);
						emStruct.setAnimSpeed(animSpeed);
						emStruct.setBlendTime(blendTime);
					}
					
					Class particleClass = null;
					
					emStruct.setParticleType(particleType);
					if (particleMesh.equals("Box")) {
						particleClass = Box.class;
						emStruct.setParticleShapeMesh(Box.class);
						emStruct.setPMF1(PMF1);
						emStruct.setPMF2(PMF2);
						emStruct.setPMF3(PMF3);
					} else if (particleMesh.equals("Cylinder")) {
						particleClass = Cylinder.class;
						emStruct.setParticleShapeMesh(Cylinder.class);
						emStruct.setPMI1(PMI1);
						emStruct.setPMI2(PMI2);
						emStruct.setPMF1(PMF1);
						emStruct.setPMF2(PMF2);
					} else if (particleMesh.equals("Dome")) {
						particleClass = Dome.class;
						emStruct.setParticleShapeMesh(Dome.class);
						emStruct.setPMI1(PMI1);
						emStruct.setPMI2(PMI2);
						emStruct.setPMF1(PMF1);
					} else if (particleMesh.equals("Quad")) {
						particleClass = Quad.class;
						emStruct.setParticleShapeMesh(Quad.class);
						emStruct.setPMF1(PMF1);
						emStruct.setPMF2(PMF2);
					} else if (particleMesh.equals("Sphere")) {
						particleClass = Sphere.class;
						emStruct.setParticleShapeMesh(Sphere.class);
						emStruct.setPMI1(PMI1);
						emStruct.setPMI2(PMI2);
						emStruct.setPMF1(PMF1);
					} else if (particleMesh.equals("Torus")) {
						particleClass = Torus.class;
						emStruct.setParticleShapeMesh(Torus.class);
						emStruct.setPMI1(PMI1);
						emStruct.setPMI2(PMI2);
						emStruct.setPMF1(PMF1);
						emStruct.setPMF2(PMF2);
					}
					emStruct.setParticleShapeMesh(particleClass);
					emStruct.setParticleMesh(particleShapeMesh);
					emStruct.setParticleMeshPath(particleMeshPath);
					emStruct.setParticleMeshType(particleData);
					
					emStruct.setMaterialPath(matDef);
					emStruct.setTexturePath(texturePath);
					
					builder.getEmitters().put(emName, emStruct);
					builder.rebuildMainView();
					
				}
				parseScripts(scrpts);
			}
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	
	public void scrubScene() {
		// TODO: wipe scene of all current spatials, reset emitters & scripts
		builder.setProjectName("");
		
		builder.getOptions().setUseTestScene(false);
		builder.getApplication().getCamera().setLocation(builder.getDefaultCamLoc());
		builder.getApplication().getCamera().setRotation(builder.getDefaultCamRot());
		
		EmitterStruct[] emitters = new EmitterStruct[builder.getEmitters().values().size()];
		builder.getEmitters().values().toArray(emitters);
		for (EmitterStruct struct : emitters) {
			builder.removeEmitter(struct);
		}
		
		ScriptStruct[] scripts = new ScriptStruct[builder.getScriptBuilder().getTasks().size()];
		builder.getScriptBuilder().getTasks().toArray(scripts);
		for (ScriptStruct struct : scripts) {
			builder.getScriptBuilder().removeTask(struct);
		}
		builder.getScriptBuilder().clearScriptList();
	}
	
	private void parseScripts(Node n) {
		if (n != null) {
			NodeList nNodeLst = n.getChildNodes();
			for (int p = 0; p < nNodeLst.getLength(); p++) {
				Node script = nNodeLst.item(p);
				NodeList params = script.getChildNodes();
				
				String emitterName = "";
				ScriptType type = null;
				int emitPerSec = 0;
				int partsPerEmit = 0;
				long delay = 0;
				long duration = 0;
				boolean emitAll = false;
				int numParticles = 0;
				
				for (int s = 0; s < params.getLength(); s++) {
					Node param = params.item(s);
					String paramName = XMLHelper.getNodeAttributeValue(param, "name");
					if (paramName.equals("emitterName"))
						emitterName = XMLHelper.getNodeAttributeValue(param, "value");
					if (paramName.equals("ScriptType"))
						type = ScriptType.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
					if (paramName.equals("emitPerSec"))
						emitPerSec = Integer.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
					if (paramName.equals("partsPerEmit"))
						partsPerEmit = Integer.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
					if (paramName.equals("delay"))
						delay = Long.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
					if (paramName.equals("duration"))
						duration = Long.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
					if (paramName.equals("emitAll"))
						emitAll = Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
					if (paramName.equals("numParticles"))
						numParticles = Integer.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
				}
				
				Emitter em = builder.getEmitters().get(emitterName).getEmitter();
				//ScriptStruct struct = new ScriptStruct(emitterName, type);
				if (type == ScriptType.Emit_For_Duration) {
					builder.getScriptBuilder().addDurationScript(em, emitPerSec, partsPerEmit, delay, duration);
				} else {
					builder.getScriptBuilder().addEmitScript(em, emitAll, numParticles, delay);
				}
			}
		}
	}
	private GravityInfluencer parseGravityInfluencer(Node n) {
		GravityInfluencer inf = new GravityInfluencer();
		
		NodeList list = n.getChildNodes();
		
		for (int i = 0; i < list.getLength(); i++) {
			Node param = list.item(i);
			String paramName = XMLHelper.getNodeAttributeValue(param, "name");
			if (paramName.equals("GravityAlignment"))
				inf.setAlignment(GravityAlignment.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("gravity")) {
				inf.setGravity(
					Float.valueOf(XMLHelper.getNodeAttributeValue(param, "x")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(param, "y")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(param, "z"))
				);
			} else if (paramName.equals("magnitude"))
				inf.setMagnitude(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("enabled"))
				inf.setEnabled(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
		}
		
		return inf;
	}
	private ColorInfluencer parseColorInfluencer(Node n) {
		ColorInfluencer inf = new ColorInfluencer();
		
		List<Node> params = new ArrayList();
		NodeList list = n.getChildNodes();
		Node colors = null;
		Node interps = null;
		
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("param"))
				params.add(list.item(i));
			else if (list.item(i).getNodeName().equals("colors"))
				colors = list.item(i);
			else if (list.item(i).getNodeName().equals("interps"))
				interps = list.item(i);
		}
		
		for (Node param : params) {
			String paramName = XMLHelper.getNodeAttributeValue(param, "name");
			if (paramName.equals("fixedDuration"))
				inf.setFixedDuration(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("enabled"))
				inf.setEnabled(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("useRandomStartColor"))
				inf.setUseRandomStartColor(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
		}
		
		NodeList colorList = colors.getChildNodes();
		NodeList interpsList = interps.getChildNodes();
		for (int i = 0; i < colorList.getLength(); i++) {
			Node color = colorList.item(i);
			Node interp = interpsList.item(i);
			inf.addColor(
				new ColorRGBA(
					Float.valueOf(XMLHelper.getNodeAttributeValue(color, "r")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(color, "g")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(color, "b")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(color, "a"))
				),
				InterpolationUtil.getInterpolationByName(
					XMLHelper.getNodeAttributeValue(interp, "value")
				)
			);
		}
		
		return inf;
	}
	private AlphaInfluencer parseAlphaInfluencer(Node n) {
		AlphaInfluencer inf = new AlphaInfluencer();
		
		List<Node> params = new ArrayList();
		NodeList list = n.getChildNodes();
		Node alphas = null;
		Node interps = null;
		
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("param"))
				params.add(list.item(i));
			else if (list.item(i).getNodeName().equals("alphas"))
				alphas = list.item(i);
			else if (list.item(i).getNodeName().equals("interps"))
				interps = list.item(i);
		}
		
		for (Node param : params) {
			String paramName = XMLHelper.getNodeAttributeValue(param, "name");
			if (paramName.equals("fixedDuration"))
				inf.setFixedDuration(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("enabled"))
				inf.setEnabled(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
		}
		
		NodeList alphaList = alphas.getChildNodes();
		NodeList interpsList = interps.getChildNodes();
		for (int i = 0; i < alphaList.getLength(); i++) {
			Node alpha = alphaList.item(i);
			Node interp = interpsList.item(i);
			inf.addAlpha(
				Float.valueOf(XMLHelper.getNodeAttributeValue(alpha, "value")),
				InterpolationUtil.getInterpolationByName(
					XMLHelper.getNodeAttributeValue(interp, "value")
				)
			);
		}
		
		return inf;
	}
	private SizeInfluencer parseSizeInfluencer(Node n) {
		SizeInfluencer inf = new SizeInfluencer();
		
		List<Node> params = new ArrayList();
		NodeList list = n.getChildNodes();
		Node sizes = null;
		Node interps = null;
		
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("param"))
				params.add(list.item(i));
			else if (list.item(i).getNodeName().equals("sizes"))
				sizes = list.item(i);
			else if (list.item(i).getNodeName().equals("interps"))
				interps = list.item(i);
		}
		
		for (Node param : params) {
			String paramName = XMLHelper.getNodeAttributeValue(param, "name");
			if (paramName.equals("fixedDuration"))
				inf.setFixedDuration(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("enabled"))
				inf.setEnabled(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("useRandomSize"))
				inf.setUseRandomSize(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("randomSizeTolerance"))
				inf.setRandomSizeTolerance(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
		}
		
		NodeList sizesList = sizes.getChildNodes();
		NodeList interpsList = interps.getChildNodes();
		for (int i = 0; i < sizesList.getLength(); i++) {
			Node size = sizesList.item(i);
			Node interp = interpsList.item(i);
			inf.addSize(
				new Vector3f(
					Float.valueOf(XMLHelper.getNodeAttributeValue(size, "x")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(size, "y")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(size, "z"))
				),
				InterpolationUtil.getInterpolationByName(
					XMLHelper.getNodeAttributeValue(interp, "value")
				)
			);
		}
		return inf;
	}
	private RotationInfluencer parseRotationInfluencer(Node n) {
		RotationInfluencer inf = new RotationInfluencer();
		
		List<Node> params = new ArrayList();
		NodeList list = n.getChildNodes();
		Node rots = null;
		Node interps = null;
		
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("param"))
				params.add(list.item(i));
			else if (list.item(i).getNodeName().equals("rots"))
				rots = list.item(i);
			else if (list.item(i).getNodeName().equals("interps"))
				interps = list.item(i);
		}
		
		boolean rotX = false, rotY = false, rotZ = false;
		
		for (Node param : params) {
			String paramName = XMLHelper.getNodeAttributeValue(param, "name");
			if (paramName.equals("useRandomSpeed"))
				inf.setUseRandomSpeed(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("enabled"))
				inf.setEnabled(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("useRandomDirection"))
				inf.setUseRandomDirection(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("useRandomStartRotationX"))
				rotX = Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
			else if (paramName.equals("useRandomStartRotationY"))
				rotY = Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
			else if (paramName.equals("useRandomStartRotationZ"))
				rotZ = Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
		}
		inf.setUseRandomStartRotation(rotX, rotY, rotZ);
		
		NodeList sizesList = rots.getChildNodes();
		NodeList interpsList = interps.getChildNodes();
		for (int i = 0; i < sizesList.getLength(); i++) {
			Node rot = sizesList.item(i);
			Node interp = interpsList.item(i);
			inf.addRotationSpeed(
				new Vector3f(
					Float.valueOf(XMLHelper.getNodeAttributeValue(rot, "x")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(rot, "y")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(rot, "z"))
				),
				InterpolationUtil.getInterpolationByName(
					XMLHelper.getNodeAttributeValue(interp, "value")
				)
			);
		}
		
		return inf;
	}
	private SpriteInfluencer parseSpriteInfluencer(Node n) {
		SpriteInfluencer inf = new SpriteInfluencer();
		
		NodeList list = n.getChildNodes();
		
		String frameSequence = "";
		
		for (int i = 0; i < list.getLength(); i++) {
			Node param = list.item(i);
			String paramName = XMLHelper.getNodeAttributeValue(param, "name");
			if (paramName.equals("fixedDuration"))
				inf.setFixedDuration(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("animate"))
				inf.setAnimate(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("enabled"))
				inf.setEnabled(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("useRandomStartImage"))
				inf.setUseRandomStartImage(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("frameSequence"))
				frameSequence = String.valueOf(XMLHelper.getNodeAttributeValue(param, "value"));
		}
		if (!frameSequence.equals("")) {
			StringTokenizer st = new StringTokenizer(frameSequence, ",");
			int[] frames = new int[st.countTokens()];
			for (int i = 0; i < st.countTokens(); i++) {
				frames[i] = Integer.valueOf(st.nextToken());
			}
			inf.setFrameSequence(frames);
		}
		return inf;
	}
	private ImpulseInfluencer parseImpulseInfluencer(Node n) {
		ImpulseInfluencer inf = new ImpulseInfluencer();
		
		NodeList list = n.getChildNodes();
		
		for (int i = 0; i < list.getLength(); i++) {
			Node param = list.item(i);
			String paramName = XMLHelper.getNodeAttributeValue(param, "name");
			if (paramName.equals("chance"))
				inf.setChance(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("strength"))
				inf.setStrength(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("magnitude"))
				inf.setMagnitude(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("enabled"))
				inf.setEnabled(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
		}
		
		return inf;
	}
	private PhysicsInfluencer parsePhysicsInfluencer(Node n) {
		PhysicsInfluencer inf = new PhysicsInfluencer();
		
		NodeList list = n.getChildNodes();
		
		for (int i = 0; i < list.getLength(); i++) {
			Node param = list.item(i);
			String paramName = XMLHelper.getNodeAttributeValue(param, "name");
			if (paramName.equals("collisionReaction"))
				inf.setCollisionReaction(CollisionReaction.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("restitution"))
				inf.setRestitution(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			 else if (paramName.equals("enabled"))
				inf.setEnabled(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
		}
		
		return inf;
	}
	private RadialVelocityInfluencer parseRadialVelocityInfluencer(Node n) {
		RadialVelocityInfluencer inf = new RadialVelocityInfluencer();
		
		NodeList list = n.getChildNodes();
		
		for (int i = 0; i < list.getLength(); i++) {
			Node param = list.item(i);
			String paramName = XMLHelper.getNodeAttributeValue(param, "name");
			if (paramName.equals("radialPullAlignment"))
				inf.setRadialPullAlignment(RadialPullAlignment.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("radialPullCenter"))
				inf.setRadialPullCenter(RadialPullCenter.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("radialUpAlignment"))
				inf.setRadialUpAlignment(RadialUpAlignment.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("radialPull"))
				inf.setRadialPull(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("tangentForce"))
				inf.setTangentForce(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("useRandomDirection"))
				inf.setUseRandomDirection(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("enabled"))
				inf.setEnabled(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
		}
		
		return inf;
	}
	private DestinationInfluencer parseDestinationInfluencer(Node n) {
		DestinationInfluencer inf = new DestinationInfluencer();
		
		List<Node> params = new ArrayList();
		NodeList list = n.getChildNodes();
		Node dests = null;
		Node weights = null;
		Node interps = null;
		
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("param"))
				params.add(list.item(i));
			else if (list.item(i).getNodeName().equals("dests"))
				dests = list.item(i);
			else if (list.item(i).getNodeName().equals("weights"))
				weights = list.item(i);
			else if (list.item(i).getNodeName().equals("interps"))
				interps = list.item(i);
		}
		
		for (Node param : params) {
			String paramName = XMLHelper.getNodeAttributeValue(param, "name");
			if (paramName.equals("fixedDuration"))
				inf.setFixedDuration(Float.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("enabled"))
				inf.setEnabled(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
			else if (paramName.equals("useRandomStartDestination"))
				inf.setUseRandomStartDestination(Boolean.valueOf(XMLHelper.getNodeAttributeValue(param, "value")));
		}
		
		NodeList destsList = dests.getChildNodes();
		NodeList weightsList = weights.getChildNodes();
		NodeList interpsList = interps.getChildNodes();
		for (int i = 0; i < destsList.getLength(); i++) {
			Node dest = destsList.item(i);
			Node weight = weightsList.item(i);
			Node interp = interpsList.item(i);
			inf.addDestination(
				new Vector3f(
					Float.valueOf(XMLHelper.getNodeAttributeValue(dest, "x")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(dest, "y")),
					Float.valueOf(XMLHelper.getNodeAttributeValue(dest, "z"))
				),
				Float.valueOf(XMLHelper.getNodeAttributeValue(weight, "value")),
				InterpolationUtil.getInterpolationByName(
					XMLHelper.getNodeAttributeValue(interp, "value")
				)
			);
		}
		
		return inf;
	}
}