package edu.hawhamburg.shared.datastructures.mesh;

import java.util.ArrayList;
import java.util.Random;

import edu.hawhamburg.shared.math.Ray;
import edu.hawhamburg.shared.math.Vector;

public class CircularHermiteSpline {

    private ArrayList<Vector> controlPoints = new ArrayList<Vector>();
    private ArrayList<Vector> controlTangents = new ArrayList<Vector>();
    private ArrayList<Curve> curves = new ArrayList<Curve>();
    private ArrayList<Vector> points = new ArrayList<Vector>();
    private ArrayList<Vector> tangents = new ArrayList<Vector>();

    private double deltaT;

    public CircularHermiteSpline(){
    }

    public CircularHermiteSpline(int numberOfPoints){
        computeControlPoints(numberOfPoints);
        deltaT = 1.0/controlPoints.size();
        computeTangents();
        computeCurves();
        computePoints();
    }

    public int getNumberOfPoints(){
        return points.size();
    }

    public void computePoints(){
        double t = 0.01;
        for(int i=0;i<controlPoints.size();i++){
            points.add(controlPoints.get(i));
            tangents.add(controlTangents.get(i));
            for(int j=0;j<99;j++){
                points.add(curves.get(i).evaluateCurve(t));
                tangents.add(curves.get(i).evaluateTangent(t));
                t+=0.01;
            }
            t = 0;
        }
    }

    public ArrayList<Vector> getPoints(){
        return points;
    }

    public ArrayList<Vector> getTangents() {
        return tangents;
    }

    public void computeControlPoints(int size){
        double x, y, z;
        Vector newVector;
        for(int i=0; i<size; i++){
            x = 10*Math.random()-5;
            y = 10*Math.random()-5;
            z = 10*Math.random()-5;
            newVector = new Vector(x,y,z);
            newVector = newVector.multiply(0.75/newVector.getNorm());
            controlPoints.add(newVector);
        }
        for(int i=0; i<controlPoints.size(); i++){
            if(i==controlPoints.size()-1){
                if(new Ray(controlPoints.get(i), controlPoints.get(0)).getDistance(new Vector(0,0,0)) <= 0.5){
                    Vector newPoint = controlPoints.get(i).add(controlPoints.get(0)).multiply(0.5);
                    newPoint = newPoint.multiply(0.75/newPoint.getNorm());
                    controlPoints.set(0,newPoint);

                }
            }
            else {
                if(new Ray(controlPoints.get(i), controlPoints.get(i + 1)).getDistance(new Vector(0,0,0)) <= 0.5){
                    Vector newPoint = controlPoints.get(i).add(controlPoints.get(i+1)).multiply(0.5);
                    newPoint = newPoint.multiply(0.75/newPoint.getNorm());
                    controlPoints.set(i+1,newPoint);

                }
            }
        }
    }

    public void computeTangents(){
        Vector differenceVector;
        for(int i=0;i<controlPoints.size();i++) {
            if(i==controlPoints.size()-1)
                differenceVector = controlPoints.get(0).subtract(controlPoints.get(getIndex(getT(i)-deltaT)));
            else if(i==0)
                differenceVector = controlPoints.get(getIndex(deltaT)).subtract(controlPoints.get(getIndex(1-deltaT)));
            else
                differenceVector = controlPoints.get(getIndex(getT(i)+deltaT)).subtract(controlPoints.get(getIndex(getT(i)-deltaT)));

            controlTangents.add(differenceVector.getNormalized());
        }
    }

    public void computeCurves(){
        for(int i=0;i<controlPoints.size();i++){
            if(i==controlPoints.size()-1){
                curves.add(new Curve(controlPoints.get(i),controlPoints.get(0),controlTangents.get(i),controlTangents.get(0)));
            }
            else{
                curves.add(new Curve(controlPoints.get(i),controlPoints.get(i+1),controlTangents.get(i),controlTangents.get(i+1)));
            }
        }
    }

    public Vector getPosition(double t){
        return curves.get(getIndex(t)).evaluateCurve(getLocalT(t));
    }

    public Vector getDirection(double t){
        return curves.get(getIndex(t)).evaluateTangent(getLocalT(t));
    }

    public double getT(int index){
        return index*deltaT;
    }

    /*public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }*/

    public int getIndex(double t){
        Double index = new Double(t/deltaT);
        return index.intValue();
    }

    public double getLocalT(double t){
        return ( t - (getIndex(t) * deltaT) )/deltaT;
    }


}
