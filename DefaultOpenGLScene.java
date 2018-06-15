/**
 * Diese Datei gehört zum Android/Java Framework zur Veranstaltung "Computergrafik für
 * Augmented Reality" von Prof. Dr. Philipp Jenke an der Hochschule für Angewandte
 * Wissenschaften (HAW) Hamburg. Weder Teile der Software noch das Framework als Ganzes dürfen
 * ohne die Einwilligung von Philipp Jenke außerhalb von Forschungs- und Lehrprojekten an der HAW
 * Hamburg verwendet werden.
 * <p>
 * This file is part of the Android/Java framework for the course "Computer graphics for augmented
 * reality" by Prof. Dr. Philipp Jenke at the University of Applied (UAS) Sciences Hamburg. Neither
 * parts of the framework nor the complete framework may be used outside of research or student
 * projects at the UAS Hamburg.
 */
package edu.hawhamburg.app.opengl;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.HalfEdge;
import edu.hawhamburg.shared.datastructures.mesh.HalfEdgeTriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.ObjReader;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshFactory;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.CubeNode;
import edu.hawhamburg.shared.scenegraph.INode;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;
import edu.hawhamburg.shared.scenegraph.TranslationNode;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;

/**
 * Dummy scene with rather simple content.
 *
 * @author Philipp Jenke
 */
public class DefaultOpenGLScene extends Scene {
    public DefaultOpenGLScene() {
        super(100, INode.RenderMode.REGULAR);
    }

    //TriangleMeshNode triangle = new TriangleMeshNode();

    //triangle[0] =

    @Override
        public void onSetup(InnerNode rootNode) {
        TriangleMeshNode cowNodeA;
        TriangleMeshNode cowNodeB = new TriangleMeshNode();


        TranslationNode tralaA = new TranslationNode(new Vector(0,0,0.3));
        TranslationNode tralaB = new TranslationNode(new Vector(0,0,-0.3));



        //CubeNode cubeNode = new CubeNode(0.5);
        HalfEdgeTriangleMesh halfEdgeTriangleMesh = new HalfEdgeTriangleMesh();
        List<HalfEdgeTriangleMesh> halfEdgeTriangleMeshes = new ArrayList<HalfEdgeTriangleMesh>();
        List<ITriangleMesh> triangleMeshes = new ArrayList<ITriangleMesh>();
        ObjReader objReader = new ObjReader();
        triangleMeshes = objReader.readTriangleMesh(new TriangleMeshFactory(),  "meshes/cow.obj");
        halfEdgeTriangleMeshes = objReader.read(new TriangleMeshFactory(), "meshes/cow.obj");
        System.out.println("SIZE = "+triangleMeshes.size());
        for(int i=0;i<triangleMeshes.size();i++){
//----
            //rootNode.addChild(new TriangleMeshNode(triangleMeshes.get(i)));
            halfEdgeTriangleMesh = new TriangleMeshFactory().triangleToHalfEdge((TriangleMesh)triangleMeshes.get(i));
            cowNodeA = new TriangleMeshNode(halfEdgeTriangleMesh);
            cowNodeB = new TriangleMeshNode(halfEdgeTriangleMeshes.get(i));

            tralaA.addChild(cowNodeA);
            tralaB.addChild(cowNodeB);

            getRoot().addChild(tralaA);
            getRoot().addChild(tralaB);
        }
        /*
        for(int i=0;i<halfEdgeTriangleMeshes.size();i++){
//----
            //rootNode.addChild(new TriangleMeshNode(halfEdgeTriangleMeshes.get(i)));
            cowNodeA = new TriangleMeshNode(halfEdgeTriangleMeshes.get(i));
            cowNodeB.createVbo1(halfEdgeTriangleMeshes.get(i));

            tralaA.addChild(cowNodeA);
            tralaB.addChild(cowNodeB);

            getRoot().addChild(tralaA);
            getRoot().addChild(tralaB);
        }
        */
    }

    @Override
    public void onTimerTick(int counter) {
    }

    @Override
    public void onSceneRedraw() {

    }
}