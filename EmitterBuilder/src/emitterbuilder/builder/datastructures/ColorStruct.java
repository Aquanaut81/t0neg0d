package emitterbuilder.builder.datastructures;

import com.jme3.math.ColorRGBA;
import emitter.Interpolation;

/**
 *
 * @author t0neg0d
 */
public class ColorStruct {
	ColorRGBA color = new ColorRGBA();
	Interpolation interpolation;

	public ColorStruct(ColorRGBA color, Interpolation interpolation) {
		this.color.set(color);
		this.interpolation = interpolation;
	}

	public void setColor(ColorRGBA color) {
		this.color.set(color);
	}

	public void setColor(float r, float g, float b, float a) {
		this.color.set(r,g,b,a);
	}

	public ColorRGBA getColor() { return this.color; }

	public void setInterpolation(Interpolation interpolation) {
		this.interpolation = interpolation;
	}

	public Interpolation getInterpolation() { return this.interpolation; }
}
