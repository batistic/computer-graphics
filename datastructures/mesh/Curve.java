package edu.hawhamburg.shared.datastructures.mesh;

import edu.hawhamburg.shared.math.Vector;

public class Curve {

    private Vector firstPoint;
    private Vector lastPoint;
    private Vector firstTangent;
    private Vector lastTangent;

    public Curve(Vector fPoint, Vector lPoint, Vector fTangent, Vector lTangent){
        firstPoint = fPoint;
        lastPoint = lPoint;
        firstTangent = fTangent.getNormalized();
        lastTangent = lTangent.getNormalized();
    }

    public double h03(double t){
        return (1.0-t)*(1.0-t)*(1.0+2*t);
    }

    public double h13(double t){
        return t*(1.0-t)*(1.0-t);
    }

    public double h23(double t){
        return -(t*t*(1.0-t));
    }

    public double h33(double t){
        return (3.0-2.0*t)*t*t;
    }

    public double dH03(double t){
        return 6 * (t*t) - 6 * t;
    }
    public double dH13(double t){
        return 3 * (t*t) - 4 * t + 1;
    }
    public double dH23(double t){
        return 3 * (t*t) - 2*t;
    }
    public double dH33(double t){
        return 6 * t - 6 * (t*t);
    }

    public Vector evaluateCurve(double t) {
        return firstPoint.multiply(h03(t)).add(firstTangent.multiply(h13(t))).add(lastTangent.multiply(h23(t))).add(lastPoint.multiply(h33(t)));
    }

    public Vector evaluateTangent(double t){
        return firstPoint.multiply(dH03(t)).add(firstTangent.multiply(dH13(t))).add(lastTangent.multiply(dH23(t))).add(lastPoint.multiply(dH33(t))).getNormalized();
    }

}
