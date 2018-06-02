package edu.hawhamburg.app.vuforia;

import android.view.animation.Transformation;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.CircularHermiteSpline;
import edu.hawhamburg.shared.datastructures.mesh.HalfEdge;
import edu.hawhamburg.shared.datastructures.mesh.HalfEdgeTriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshTools;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.CubeNode;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.LineStripNode;
import edu.hawhamburg.shared.scenegraph.SphereNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TranslationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.vuforia.VuforiaMarkerNode;

/**
 * Dummy implementation of a scene with a Vuforia marker
 *
 * @author Philipp Jenke
 */
public class DefaultVuforiaScene extends Scene {

    int k = 0;

    List<TriangleMesh> meshSpaceShuttle;
    List<TriangleMesh> meshPlanet;
    List<TriangleMesh> meshXwing;

    private TranslationNode translationNode1;
    private TranslationNode translationNode2;

    private TriangleMeshNode spaceShuttleNode;
    private TriangleMeshNode planetNode;
    private TriangleMeshNode xwingNode;

    private VuforiaMarkerNode marker;

    private TransformationNode planetTrans = new TransformationNode();
    private TransformationNode transformationNode1;
    private TransformationNode transformationNode2;

    private CircularHermiteSpline spline1;
    private CircularHermiteSpline spline2;
    /*
    private TriangleMeshNode planet;

    private TriangleMeshNode ship1;
    private TriangleMeshNode ship2;

    private LineStripNode spline1 = new LineStripNode();
    private LineStripNode spline2 = new LineStripNode();

    CircularHermiteSpline circularHermiteSpline1 = new CircularHermiteSpline(10);
    CircularHermiteSpline circularHermiteSpline2 = new CircularHermiteSpline(10);

    private TranslationNode translationNode1 = new TranslationNode(new Vector(3));
    private TranslationNode translationNode2 = new TranslationNode(new Vector(3));

    private TransformationNode planetTrans = new TransformationNode();

    private TransformationNode ship1Trans = new TransformationNode();
    private TransformationNode ship2Trans = new TransformationNode();

    private TransformationNode spline1Trans = new TransformationNode();
    private TransformationNode spline2Trans = new TransformationNode();

    private VuforiaMarkerNode marker = new VuforiaMarkerNode("elphi");
*/


    //private TriangleMeshNode observer;
    //private TriangleMeshNode observed = new TriangleMeshNode();
    //private VuforiaMarkerNode marker2 = new VuforiaMarkerNode("campus");
    //private TransformationNode tra1 = new TransformationNode();
    //private TransformationNode tra2 = new TransformationNode();

    public DefaultVuforiaScene() {
        super(100, INode.RenderMode.REGULAR);
    }

    @Override
    public void onSetup(InnerNode rootNode) {
        marker = new VuforiaMarkerNode("elphi");
        ITriangleMeshFactory meshesFactorySpaceShuttle = new TriangleMeshFactory();
        ITriangleMeshFactory meshesFactorySphere = new TriangleMeshFactory();
        ITriangleMeshFactory meshesFactoryXwing = new TriangleMeshFactory();

        ObjReader objReaderSpaceShuttle = new ObjReader();
        ObjReader objReaderSphere = new ObjReader();
        ObjReader objReaderXwing = new ObjReader();

        meshSpaceShuttle = objReaderSpaceShuttle.readTriangleMesh( meshesFactorySpaceShuttle, "meshes/spaceshuttle.obj");
        meshPlanet = objReaderSphere.readTriangleMesh( meshesFactorySphere, "meshes/sphere.obj");
        meshXwing = objReaderXwing.readTriangleMesh( meshesFactoryXwing, "meshes/xwing.obj");

        TriangleMeshTools.fitToUnitBox(meshSpaceShuttle.get(0));
        TriangleMeshTools.fitToUnitBox(meshPlanet.get(0));
        TriangleMeshTools.fitToUnitBox(meshXwing.get(0));

        TriangleMeshTools.placeOnXZPlane(meshSpaceShuttle.get(0));
        TriangleMeshTools.placeOnXZPlane(meshPlanet.get(0));
        TriangleMeshTools.placeOnXZPlane(meshXwing.get(0));



        planetNode = new TriangleMeshNode();
        planetNode.createVbo1(meshPlanet.get(0));

        xwingNode = new TriangleMeshNode();
        transformationNode2 = new TransformationNode();
        xwingNode = new TriangleMeshNode();
        xwingNode.createVbo1(meshXwing.get(0));

        spaceShuttleNode = new TriangleMeshNode();
        transformationNode1 = new TransformationNode();
        spaceShuttleNode.createVbo1(meshSpaceShuttle.get(0));


        spline1 = new CircularHermiteSpline(10);
        LineStripNode node1 = new LineStripNode();
        List<Vector> points = spline1.getPoints();
        for( Vector v : points)
            node1.addPoint(v);

        spline2 = new CircularHermiteSpline(10);
        LineStripNode node2 = new LineStripNode();
        List<Vector> points1 = spline2.getPoints();
        for( Vector v : points1)
            node2.addPoint(v);

        TransformationNode node1Trans = new TransformationNode();
        TransformationNode node2Trans = new TransformationNode();
        node1Trans.addChild(node1);
        node2Trans.addChild(node2);

        planetTrans.addChild(planetNode);
        transformationNode1.addChild(spaceShuttleNode);
        transformationNode2.addChild(xwingNode);

        translationNode1 = new TranslationNode(new Vector(3));
        translationNode2 = new TranslationNode(new Vector(3));

        translationNode1.addChild(transformationNode1);
        translationNode2.addChild(transformationNode2);

        node1Trans.addChild(translationNode1);
        node2Trans.addChild(translationNode2);

        planetTrans.addChild(node1Trans);
        planetTrans.addChild(node2Trans);

        marker.addChild(planetTrans);

        rootNode.addChild(marker);

        /*List<TriangleMesh> triangleMeshes = new ArrayList<TriangleMesh>();
        HalfEdgeTriangleMesh halfEdgeTriangleMesh = new HalfEdgeTriangleMesh();
        ObjReader objReader = new ObjReader();
        triangleMeshes = objReader.readTriangleMesh(new TriangleMeshFactory(), "meshes/sphere.obj");
        for(int i=0;i<triangleMeshes.size();i++){
            halfEdgeTriangleMesh = new TriangleMeshFactory().triangleToHalfEdge(triangleMeshes.get(i));
            TriangleMeshTools.fitToUnitBox(halfEdgeTriangleMesh);
            TriangleMeshTools.placeOnXZPlane(halfEdgeTriangleMesh);
            planet = new TriangleMeshNode(halfEdgeTriangleMesh);

            planetTrans.addChild(planet);
            marker1.addChild(planetTrans);
        }

        for(int i=0;i<circularHermiteSpline1.getNumberOfPoints();i++){
            spline1.addPoint(circularHermiteSpline1.getPoints().get(i));
        }
        for(int i=0;i<circularHermiteSpline2.getNumberOfPoints();i++){
            spline2.addPoint(circularHermiteSpline2.getPoints().get(i));
        }

        spline1Trans.addChild(spline1);
        planetTrans.addChild(spline1Trans);
        spline2Trans.addChild(spline2);
        planetTrans.addChild(spline2Trans);

        triangleMeshes.clear();
        triangleMeshes = objReader.readTriangleMesh(new TriangleMeshFactory(), "meshes/spaceshuttle.obj");
        for(int i=0;i<triangleMeshes.size();i++){
            TriangleMeshTools.fitToUnitBox(triangleMeshes.get(i));
            TriangleMeshTools.placeOnXZPlane(triangleMeshes.get(i));
            ship1 = new TriangleMeshNode();
            ship1.createVbo1((triangleMeshes.get(i)));

            ship1Trans.addChild(ship1);
            spline1Trans.addChild(ship1Trans);
        }

        triangleMeshes.clear();
        triangleMeshes = objReader.readTriangleMesh(new TriangleMeshFactory(), "meshes/xwing.obj");
        for(int i=0;i<triangleMeshes.size();i++){
            TriangleMeshTools.fitToUnitBox(triangleMeshes.get(i));
            TriangleMeshTools.placeOnXZPlane(triangleMeshes.get(i));
            ship2 = new TriangleMeshNode();
            ship2.createVbo1((triangleMeshes.get(i)));

            ship2Trans.addChild(ship2);
            spline2Trans.addChild(ship2Trans);
            rootNode.addChild(spline2Trans);
        }

        rootNode.addChild(marker1);
        rootNode.addChild(planetTrans);
        rootNode.addChild(spline1Trans);
        rootNode.addChild(spline2Trans);
*/
        /*
        List<TriangleMesh> triangleMeshes = new ArrayList<TriangleMesh>();
        HalfEdgeTriangleMesh halfEdgeTriangleMesh = new HalfEdgeTriangleMesh();
        ObjReader objReader = new ObjReader();
        triangleMeshes = objReader.readTriangleMesh(new TriangleMeshFactory(), "meshes/max_planck.obj");
        for(int i=0;i<triangleMeshes.size();i++){
            halfEdgeTriangleMesh = new TriangleMeshFactory().triangleToHalfEdge(triangleMeshes.get(i));
            //observer.createVbo1(triangleMeshes.get(i));
            TriangleMeshTools.fitToUnitBox(halfEdgeTriangleMesh);
            TriangleMeshTools.placeOnXZPlane(halfEdgeTriangleMesh);
            observer = new TriangleMeshNode(halfEdgeTriangleMesh);

            tra1.addChild(observer);
            marker1.addChild(tra1);
            rootNode.addChild(marker1);
        }
        triangleMeshes.clear();
        triangleMeshes = objReader.readTriangleMesh(new TriangleMeshFactory(), "meshes/deer.obj");
        for(int i=0;i<triangleMeshes.size();i++){
            TriangleMeshTools.fitToUnitBox(triangleMeshes.get(i));
            TriangleMeshTools.placeOnXZPlane(triangleMeshes.get(i));
            observed.createVbo1(triangleMeshes.get(i));

            tra2.addChild(observed);
            marker2.addChild(tra2);
            rootNode.addChild(marker2);
        }
        *//*
        VuforiaMarkerNode marker = new VuforiaMarkerNode("elphi");
        double sideLength = 0.25;
        CubeNode cubeNode = new CubeNode(sideLength);
        TranslationNode translationNode = new TranslationNode(new Vector(0, sideLength, 0));
        translationNode.addChild(cubeNode);
        marker.addChild(translationNode);
        rootNode.addChild(marker);
        */
    }

    @Override
    public void onTimerTick(int counter) {
        // Timer tick event
    }

    @Override
    public void onSceneRedraw() {

        if(k+1==1000)
            k=0;
        else
            k++;

        translationNode1.setTranslation(spline1.getPoints().get(k));
        translationNode2.setTranslation(spline2.getPoints().get(k));

        Vector z1 = spline1.getTangents().get(k).getNormalized();
        Vector y1 = spline1.getPoints().get(k).getNormalized();
        Vector x1 = y1.cross(z1);
        y1 = z1.cross(x1);

        Vector z2 = spline2.getTangents().get(k).getNormalized();
        Vector y2 = spline2.getPoints().get(k).getNormalized();
        Vector x2 = y2.cross(z2);
        y2 = z2.cross(x2);

        Matrix transformation1 = new Matrix(x1, y1, z1);
        transformation1 = Matrix.makeHomogenious(transformation1);
        transformationNode1.setTransformation(transformation1);

        Matrix transformation2 = new Matrix(x2, y2, z2);
        transformation2 = Matrix.makeHomogenious(transformation2);
        transformationNode2.setTransformation(transformation2);

        /*
        Matrix observerWorld = marker1.getCombinedTransformation();
        Matrix targetWorld = marker2.getCombinedTransformation();

        Vector observerObserver = new Vector(observerWorld.get(0,3),observerWorld.get(1,3),observerWorld.get(2,3),1);
        Vector targetTarget = new Vector(targetWorld.get(0,3),targetWorld.get(1,3),targetWorld.get(2,3),1);

        Vector observerTarget = observerWorld.getInverse().multiply(targetWorld).multiply(targetTarget);

        Vector lineOfSight = observerTarget.subtract(observerObserver).xyz();

        //x = normalized line of sight
        //z = x cross product (0,1,0) // up-direction, approximately along the y-axis.
        //y = z cross product x
        Vector x = lineOfSight.getNormalized();
        Vector z = x.cross(new Vector(0,1,0)).getNormalized();
        Vector y = z.cross(x).getNormalized();

        Matrix transMatrix = new Matrix(x,y,z);
        transMatrix = Matrix.makeHomogenious(transMatrix);

        tra1.setTransformation(transMatrix);
        */
    }
}
