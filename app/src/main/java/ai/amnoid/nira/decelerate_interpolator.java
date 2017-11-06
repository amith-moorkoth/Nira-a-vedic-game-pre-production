package ai.amnoid.nira;

import android.view.animation.Interpolator;

/**
 * Created by Amith Moorkoth on 10/15/2017.
 */

public class decelerate_interpolator implements Interpolator {
        public decelerate_interpolator() {}
        public float getInterpolation(float t) {
            double x=1-Math.pow(1-t,2*t);
            return (float) x;
        }
    }

