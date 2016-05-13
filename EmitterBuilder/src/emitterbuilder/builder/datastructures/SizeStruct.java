package emitterbuilder.builder.datastructures;

import com.jme3.math.Vector3f;
import emitter.Interpolation;

/**
 *
 * @author t0neg0d
 */
public class SizeStruct {
	Vector3f size = new Vector3f();
	Interpolation interpolation;

	public SizeStruct(Vector3f size, Interpolation interpolation) {
		this.size.set(size);
		this.interpolation = interpolation;
	}

	public void setSize(Vector3f size) {
		this.size.set(size);
	}

	public void setSize(float x, float y, float z) {
		this.size.set(x,y,z);
	}

	public Vector3f getSize() { return this.size; }

	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}

	public Interpolation getInterpolation() { return this.interpolation; }
}
