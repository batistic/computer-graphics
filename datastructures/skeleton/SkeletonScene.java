package edu.hawhamburg.shared.datastructures.skeleton;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMeshFactory;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMeshGenerator;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.misc.Button;
import edu.hawhamburg.shared.misc.ButtonHandler;
import edu.hawhamburg.shared.misc.IButton;
import edu.hawhamburg.shared.misc.Scene;
import edu.hawhamburg.shared.scenegraph.ITriangleMeshNode;
import edu.hawhamburg.shared.scenegraph.ITriangleMeshNodeFactory;
import edu.hawhamburg.shared.scenegraph.InnerNode;

/**
 * Test scene for sceleton applications.
 *
 * @author Philipp Jenke
 */

public class SkeletonScene extends Scene {
    private double animationAlpha = 0;

    private Bone trunkBone;
    private Bone cylinderBottom;
    private Bone cylinderMiddle;
    private Bone cylinderTop;
    private ITriangleMeshNode meshNode;
    private ShowBones showBones = ShowBones.BONES_ONLY;
    private SkeletonNode skeletonNode;
    private ITriangleMeshFactory triangleMeshFactory;
    private ITriangleMeshNodeFactory triangleMeshNodeFactory;

    public enum ShowBones {
        BONES_ONLY, MESH_AND_BONES, MESH_ONLY;

        public ShowBones next() {
            int index = ordinal() + 1;
            if (index >= values().length) {
                index = 0;
            }
            return values()[index];
        }
    }

    public SkeletonScene(ITriangleMeshFactory triangleMeshFactory, ITriangleMeshNodeFactory triangleMeshNodeFactory) {
        this.triangleMeshFactory = triangleMeshFactory;
        this.triangleMeshNodeFactory = triangleMeshNodeFactory;
    }

    @Override
    public void onSetup(InnerNode rootNode) {


        IButton button = new Button("skeleton.png",
                -0.7, -0.7, 0.2, new ButtonHandler() {
            @Override
            public void handle() {
                showBones = showBones.next();
                updateRenderSettings();
            }
        }, triangleMeshFactory, triangleMeshNodeFactory);
        addButton(button);

        // Skeleton
        Skeleton skeleton = new Skeleton();
        trunkBone = new Bone(skeleton, 0.2);
        trunkBone.setRotation(Matrix.createRotationMatrix4(new Vector(0, 0, 1), 90 * Math.PI / 180.0));
        cylinderBottom = new Bone(trunkBone, 0.26);
        cylinderMiddle = new Bone(cylinderBottom, 0.26);
        cylinderTop = new Bone(cylinderMiddle, 0.26);
        // Preserve the current state of all bones as the rest state
        skeleton.setRestState();
        skeletonNode = new SkeletonNode(skeleton, triangleMeshFactory, triangleMeshNodeFactory);
        getRoot().addChild(skeletonNode);

        // Plane
        ITriangleMesh planeMesh = triangleMeshFactory.createMesh();
        TriangleMeshGenerator.createPlane(planeMesh, new Vector(0, 0, 0),
                new Vector(0, 1, 0), 1, triangleMeshFactory);
        rootNode.addChild(triangleMeshNodeFactory.createTriangleMeshNode(planeMesh));

        // Pine-Tree
        //ObjReader reader = new ObjReader();
        //List<ITriangleMesh> meshes = reader.read("meshes/pinetree.obj");
        //TriangleMesh mesh = (TriangleMesh) TriangleMeshTools.unite(meshes);

        // Cylinder
        ITriangleMesh mesh = triangleMeshFactory.createMesh();
        TriangleMeshGenerator.createCylinder(mesh, 0.025, 1, 8,
                20, triangleMeshFactory);

        meshNode = triangleMeshNodeFactory.createTriangleMeshNode(mesh);
        rootNode.addChild(meshNode);

        updateRenderSettings();
        getRoot().setLightPosition(new Vector(1, 2, 1));
    }

    private void updateRenderSettings() {
        switch (showBones) {
            case MESH_ONLY:
                skeletonNode.setActive(false);
                meshNode.setTransparency(1.0);
                meshNode.setActive(true);
                break;
            case MESH_AND_BONES:
                skeletonNode.setActive(true);
                meshNode.setTransparency(0.5);
                meshNode.setActive(true);
                break;
            case BONES_ONLY:
                skeletonNode.setActive(true);
                meshNode.setTransparency(0.5);
                meshNode.setActive(false);
                break;
        }
    }

    @Override
    public void onTimerTick(int counter) {

    }

    @Override
    public void onSceneRedraw() {
        // Animate skeleton
        double DELTA_ANIMATION_ALPHA = 0.03;
        animationAlpha += DELTA_ANIMATION_ALPHA;
        double angle = Math.cos(animationAlpha) * Math.PI / 180.0 * 8;
        trunkBone.setRotation(Matrix.createRotationMatrix4(new Vector(0, 0, 1), 90 * Math.PI / 180.0));
        cylinderTop.setRotation(Matrix.createRotationMatrix4(new Vector(0, 1, 0), angle));
        cylinderMiddle.setRotation(Matrix.createRotationMatrix4(new Vector(0, 1, 0), 2 * angle));
        cylinderBottom.setRotation(Matrix.createRotationMatrix4(new Vector(0, 1, 0), 3 * angle));

        // Update mesh based on skeleton
        // TODO

        meshNode.updateVbo();
    }
}
