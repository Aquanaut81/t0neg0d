package emitterbuilder.builder.datastructures;

import com.jme3.math.Vector3f;
import emitter.Interpolation;

/**
 *
 * @author t0neg0d
 */
public class RotationStruct {
	Vector3f rot = new Vector3f();
	Interpolation interpolation;

	public RotationStruct(Vector3f rot, Interpolation interpolation) {
		this.rot.set(rot);
		this.interpolation = interpolation;
	}

	public void setRotation(Vector3f rot) {
		this.rot.set(rot);
	}
	
	public void setRotation(float x, float y, float z) {
		this.rot.set(x,y,z);
	}
	
	public Vector3f getRotation() { return this.rot; }

	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}

	public Interpolation getInterpolation() { return this.interpolation; }
}