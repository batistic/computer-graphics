package edu.hawhamburg.shared.datastructures.mesh;

import android.util.Half;

import edu.hawhamburg.shared.math.Vector;


/**
 * Created by User on 25/04/2018.
 */

public class TriangleFacet extends AbstractTriangle{
    private HalfEdge halfEdge;
    private Vector vNormal;
    //private int []
    protected  int[] vertexIndices = new int[3];

    TriangleFacet(){
        super();
        this.halfEdge = null;
    }

    TriangleFacet(int tf1, int tf2, int tf3){
        super();
        this.halfEdge = null;

        setVertexIndices(tf1, tf2, tf3);
    }

    TriangleFacet(AbstractTriangle triangle){
        super(triangle);
        this.halfEdge = null;
    }

    public void setHalfEdge(HalfEdge halfEdge){
        this.halfEdge = halfEdge;
    }

    public HalfEdge getHalfEdge(){
        return halfEdge;
    }

    @Override
    public int getVertexIndices(int index) {
        return vertexIndices[index];
    }

    public void setNormal(Vector vNormal){
        this.vNormal = vNormal;
    }

    public Vector getNormal(){
        return vNormal;
    }

    @Override
    public void addVertexIndexOffset(int vertexOffset) {

    }

    @Override
    public AbstractTriangle clone() {
        return null;
    }

    @Override
    public void setVertexIndices(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        vertexIndices[0] = vertexIndex1;
        vertexIndices[1] = vertexIndex2;
        vertexIndices[2] = vertexIndex3;
    }


    public double getArea() {
        Vector v0 = halfEdge.getStartVertex().getPosition();
        Vector v1 = halfEdge.getNext().getStartVertex().getPosition();
        Vector v2 = halfEdge.getNext().getNext().getStartVertex().getPosition();
        return v1.subtract(v0).cross(v2.subtract(v0)).getNorm() / 2.0;
    }

    public Vector getCentroid() {
        Vector v0 = halfEdge.getStartVertex().getPosition();
        Vector v1 = halfEdge.getNext().getStartVertex().getPosition();
        Vector v2 = halfEdge.getNext().getNext().getStartVertex().getPosition();
        return (v0.add(v1).add(v2)).multiply(1.0 / 3.0);
    }
}
