package emitterbuilder.gui.utils;

import emitter.Interpolation;
import tonegod.gui.controls.lists.SelectBox;

/**
 *
 * @author t0neg0d
 */
public final class InterpolationUtil {
	public static void populateInterpolations(SelectBox sb) {
		sb.addListItem("bounce", 0);
		sb.addListItem("bounceIn", 1);
		sb.addListItem("bounceOut", 2);
		sb.addListItem("circle", 3);
		sb.addListItem("circleIn", 4);
		sb.addListItem("circleOut", 5);
		sb.addListItem("elastic", 6);
		sb.addListItem("elasticIn", 7);
		sb.addListItem("elasticOut", 8);
		sb.addListItem("exp10", 9);
		sb.addListItem("exp10In", 10);
		sb.addListItem("exp10Out", 11);
		sb.addListItem("exp5", 12);
		sb.addListItem("exp5In", 13);
		sb.addListItem("exp5Out", 14);
		sb.addListItem("fade", 15);
		sb.addListItem("linear", 16);
		sb.addListItem("pow2", 17);
		sb.addListItem("pow2In", 18);
		sb.addListItem("pow2Out", 19);
		sb.addListItem("pow3", 20);
		sb.addListItem("pow3In", 21);
		sb.addListItem("pow3Out", 22);
		sb.addListItem("pow4", 23);
		sb.addListItem("pow4In", 24);
		sb.addListItem("pow4Out", 25);
		sb.addListItem("pow5", 26);
		sb.addListItem("pow5In", 27);
		sb.addListItem("pow5Out", 28);
		sb.addListItem("sine", 29);
		sb.addListItem("sineIn", 30);
		sb.addListItem("sineOut", 31);
		sb.addListItem("swing", 32);
		sb.addListItem("swingIn", 33);
		sb.addListItem("swingOut", 34);
	}
	public static Interpolation getInterpolationByIndex(int index) {
		Interpolation ret = Interpolation.linear;
		switch (index) {
			case 0: ret = Interpolation.bounce; break;
			case 1: ret = Interpolation.bounceIn; break;
			case 2: ret = Interpolation.bounceOut; break;
			case 3: ret = Interpolation.circle; break;
			case 4: ret = Interpolation.circleIn; break;
			case 5: ret = Interpolation.circleOut; break;
			case 6: ret = Interpolation.elastic; break;
			case 7: ret = Interpolation.elasticIn; break;
			case 8: ret = Interpolation.elasticOut; break;
			case 9: ret = Interpolation.exp10; break;
			case 10: ret = Interpolation.exp10In; break;
			case 11: ret = Interpolation.exp10Out; break;
			case 12: ret = Interpolation.exp5; break;
			case 13: ret = Interpolation.exp5In; break;
			case 14: ret = Interpolation.exp5Out; break;
			case 15: ret = Interpolation.fade; break;
			case 16: ret = Interpolation.linear; break;
			case 17: ret = Interpolation.pow2; break;
			case 18: ret = Interpolation.pow2In; break;
			case 19: ret = Interpolation.pow2Out; break;
			case 20: ret = Interpolation.pow3; break;
			case 21: ret = Interpolation.pow3In; break;
			case 22: ret = Interpolation.pow3Out; break;
			case 23: ret = Interpolation.pow4; break;
			case 24: ret = Interpolation.pow4In; break;
			case 25: ret = Interpolation.pow4Out; break;
			case 26: ret = Interpolation.pow5; break;
			case 27: ret = Interpolation.pow5In; break;
			case 28: ret = Interpolation.pow5Out; break;
			case 29: ret = Interpolation.sine; break;
			case 30: ret = Interpolation.sineIn; break;
			case 31: ret = Interpolation.sineOut; break;
			case 32: ret = Interpolation.swing; break;
			case 33: ret = Interpolation.swingIn; break;
			case 34: ret = Interpolation.swingOut; break;
		}
		return ret;
	}
	public static String getInterpolationName(Interpolation interp) {
		String ret = "";
		if (interp == Interpolation.bounce) ret = "bounce";
		else if (interp == Interpolation.bounceIn) ret = "bounceIn";
		else if (interp == Interpolation.bounceOut) ret = "bounceOut";
		else if (interp == Interpolation.circle) ret = "circle";
		else if (interp == Interpolation.circleIn) ret = "circleIn";
		else if (interp == Interpolation.circleOut) ret = "circleOut";
		else if (interp == Interpolation.elastic) ret = "elastic";
		else if (interp == Interpolation.elasticIn) ret = "elasticIn";
		else if (interp == Interpolation.elasticOut) ret = "elasticOut";
		else if (interp == Interpolation.exp10) ret = "exp10";
		else if (interp == Interpolation.exp10In) ret = "exp10In";
		else if (interp == Interpolation.exp10Out) ret = "exp10Out";
		else if (interp == Interpolation.exp5) ret = "exp5";
		else if (interp == Interpolation.exp5In) ret = "exp5In";
		else if (interp == Interpolation.exp5Out) ret = "exp5Out";
		else if (interp == Interpolation.fade) ret = "fade";
		else if (interp == Interpolation.linear) ret = "linear";
		else if (interp == Interpolation.pow2) ret = "pow2";
		else if (interp == Interpolation.pow2In) ret = "pow2In";
		else if (interp == Interpolation.pow2Out) ret = "pow2Out";
		else if (interp == Interpolation.pow3) ret = "pow3";
		else if (interp == Interpolation.pow3In) ret = "pow3In";
		else if (interp == Interpolation.pow3Out) ret = "pow3Out";
		else if (interp == Interpolation.pow4) ret = "pow4";
		else if (interp == Interpolation.pow4In) ret = "pow4In";
		else if (interp == Interpolation.pow4Out) ret = "pow4Out";
		else if (interp == Interpolation.pow5) ret = "pow5";
		else if (interp == Interpolation.pow5In) ret = "pow5In";
		else if (interp == Interpolation.pow5Out) ret = "pow5Out";
		else if (interp == Interpolation.sine) ret = "sine";
		else if (interp == Interpolation.sineIn) ret = "sineIn";
		else if (interp == Interpolation.sineOut) ret = "sineOut";
		else if (interp == Interpolation.swing) ret = "swing";
		else if (interp == Interpolation.swingIn) ret = "swingIn";
		else if (interp == Interpolation.swingOut) ret = "swingOut";
		return ret;
	}
	public static Interpolation getInterpolationByName(String name) {
		Interpolation ret = null;
		if (name.equals("bounce")) ret = Interpolation.bounce;
		else if (name.equals("bounceIn")) ret = Interpolation.bounceIn;
		else if (name.equals("bounceOut")) ret = Interpolation.bounceOut;
		else if (name.equals("circle")) ret = Interpolation.circle;
		else if (name.equals("circleIn")) ret = Interpolation.circleIn;
		else if (name.equals("circleOut")) ret = Interpolation.circleOut;
		else if (name.equals("elastic")) ret = Interpolation.elastic;
		else if (name.equals("elasticIn")) ret = Interpolation.elasticIn;
		else if (name.equals("elasticOut")) ret = Interpolation.elasticOut;
		else if (name.equals("exp10")) ret = Interpolation.exp10;
		else if (name.equals("exp10In")) ret = Interpolation.exp10In;
		else if (name.equals("exp10Out")) ret = Interpolation.exp10Out;
		else if (name.equals("exp5")) ret = Interpolation.exp5;
		else if (name.equals("exp5In")) ret = Interpolation.exp5In;
		else if (name.equals("exp5Out")) ret = Interpolation.exp5Out;
		else if (name.equals("fade")) ret = Interpolation.fade;
		else if (name.equals("linear")) ret = Interpolation.linear;
		else if (name.equals("pow2")) ret = Interpolation.pow2;
		else if (name.equals("pow2In")) ret = Interpolation.pow2In;
		else if (name.equals("pow2Out")) ret = Interpolation.pow2Out;
		else if (name.equals("pow3")) ret = Interpolation.pow3;
		else if (name.equals("pow3In")) ret = Interpolation.pow3In;
		else if (name.equals("pow3Out")) ret = Interpolation.pow3Out;
		else if (name.equals("pow4")) ret = Interpolation.pow4;
		else if (name.equals("pow4In")) ret = Interpolation.pow4In;
		else if (name.equals("pow4Out")) ret = Interpolation.pow4Out;
		else if (name.equals("pow5")) ret = Interpolation.pow5;
		else if (name.equals("pow5In")) ret = Interpolation.pow5In;
		else if (name.equals("pow5Out")) ret = Interpolation.pow5Out;
		else if (name.equals("sine")) ret = Interpolation.sine;
		else if (name.equals("sineIn")) ret = Interpolation.sineIn;
		else if (name.equals("sineOut")) ret = Interpolation.sineOut;
		else if (name.equals("swing")) ret = Interpolation.swing;
		else if (name.equals("swingIn")) ret = Interpolation.swingIn;
		else if (name.equals("swingOut")) ret = Interpolation.swingOut;
		return ret;
	}
}
