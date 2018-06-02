package edu.hawhamburg.shared.datastructures.skeleton;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMeshFactory;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.scenegraph.ITriangleMeshNode;
import edu.hawhamburg.shared.scenegraph.ITriangleMeshNodeFactory;
import edu.hawhamburg.shared.scenegraph.InnerNode;
import edu.hawhamburg.shared.scenegraph.ScaleNode;
import edu.hawhamburg.shared.scenegraph.TransformationNode;

/**
 * Render a skeleton and all its bones
 *
 * @author Philipp Jenke
 */
public class SkeletonNode extends InnerNode {

    /**
     * Reference to the skeleton
     */
    private final Skeleton skeleton;

    /**
     * The bone mesh is only created once.
     */
    private ITriangleMesh boneMesh = null;

    /**
     * Remember the transformation node for each bone.
     */
    private Map<Bone, TransformationNode> transformationNodeMap = new HashMap<Bone, TransformationNode>();

    /**
     * Constructor
     *
     * @param skeleton                Sceleton (root node of the bones).
     * @param triangleMeshFactory     Factory for a triangle mesh.
     * @param triangleMeshNodeFactory Factory for a triangle mesh node.
     */
    public SkeletonNode(Skeleton skeleton, ITriangleMeshFactory triangleMeshFactory, ITriangleMeshNodeFactory triangleMeshNodeFactory) {
        boneMesh = triangleMeshFactory.createMesh();
        createBone(boneMesh);
        this.skeleton = skeleton;
        for (Iterator<Bone> it = skeleton.getBonesIterator(); it.hasNext(); ) {
            Bone bone = it.next();
            TransformationNode transformationNode = new TransformationNode(bone.getTransformationAtStart());
            transformationNodeMap.put(bone, transformationNode);
            ScaleNode scaleNode = new ScaleNode(bone.getLength());
            ITriangleMeshNode meshNode = triangleMeshNodeFactory.createTriangleMeshNode(boneMesh);
            scaleNode.addChild(meshNode);
            transformationNode.addChild(scaleNode);
            addChild(transformationNode);
        }
    }

    @Override
    public void traverse(RenderMode mode, Matrix modelMatrix) {
        super.traverse(mode, modelMatrix);

        // Update transformations
        for (Iterator<Bone> it = skeleton.getBonesIterator(); it.hasNext(); ) {
            Bone bone = it.next();
            transformationNodeMap.get(bone).setTransformation(bone.getTransformationAtStart());
        }
    }

    /**
     * Create a bone mesh.
     */
    private void createBone(ITriangleMesh mesh) {
        mesh.clear();
        double thickPointOffset = 0.3;
        double thickness = 0.1;
        mesh.addVertex(new Vector(0, 0, 0));
        mesh.addVertex(new Vector(thickPointOffset, -thickness, -thickness));
        mesh.addVertex(new Vector(thickPointOffset, -thickness, thickness));
        mesh.addVertex(new Vector(thickPointOffset, thickness, thickness));
        mesh.addVertex(new Vector(thickPointOffset, thickness, -thickness));
        mesh.addVertex(new Vector(1, 0, 0));
        mesh.addTriangle(0, 1, 2);
        mesh.addTriangle(0, 2, 3);
        mesh.addTriangle(0, 3, 4);
        mesh.addTriangle(0, 4, 1);
        mesh.addTriangle(5, 2, 1);
        mesh.addTriangle(5, 3, 2);
        mesh.addTriangle(5, 4, 3);
        mesh.addTriangle(5, 1, 4);
        mesh.setColor(new Vector(0.75, 0.75, 0.75, 1));
        mesh.computeTriangleNormals();
    }
}
