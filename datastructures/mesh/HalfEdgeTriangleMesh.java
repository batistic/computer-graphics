package edu.hawhamburg.shared.datastructures.mesh;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.rendering.Texture;
import edu.hawhamburg.shared.scenegraph.TriangleMeshNode;

/**
 * Created by User on 24/04/2018.
 */

public class HalfEdgeTriangleMesh implements ITriangleMesh {

    private List<Vertex> vList;
    private List<HalfEdge> hEList;
    private List<TriangleFacet> tFList;

    public HalfEdgeTriangleMesh(){
        this.vList = new ArrayList<>();
        this.hEList = new ArrayList<>();
        this.tFList = new ArrayList<>();
    }


    @Override
    public int addVertex(Vector position) {
        Vertex newVertex = new Vertex(position);
        vList.add(newVertex);
        return vList.size();
    }

    @Override
    public int addVertex(Vertex vertex) {
        vList.add(vertex);
        return vList.indexOf(vertex);
    }

    @Override
    public Vertex getVertex(int index) {
        return vList.get(index);
    }

    @Override
    public Vertex getVertex(AbstractTriangle triangle, int index) {
        return vList.get(triangle.getVertexIndices(index));
    }

    @Override
    public int getNumberOfVertices() {
        return vList.size();
    }

    @Override
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        TriangleFacet tfacet = new TriangleFacet();

        List<Vertex> tempVList = new ArrayList<>();
        tempVList.add(vList.get(vertexIndex1));
        tempVList.add(vList.get(vertexIndex2));
        tempVList.add(vList.get(vertexIndex3));

        List<HalfEdge> tempHList = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            tempHList.add(new HalfEdge());
        }

        for(int i = 0; i < 3; i++){
            tempHList.get(i).setStartVertex(tempVList.get(i));
            tempVList.get(i).setHalfEdge(tempHList.get(i));
            tempHList.get(i).setFacet(tfacet);
            tempHList.get(i).setNext(tempHList.get((i + 1) % 3));
            hEList.add(tempHList.get(i));
        }

        tfacet.setHalfEdge(tempHList.get(0));
        tFList.add(tfacet);
        vList.get(vertexIndex1).setHalfEdge(tempVList.get(0).getHalfEdge());
        vList.get(vertexIndex2).setHalfEdge(tempVList.get(1).getHalfEdge());
        vList.get(vertexIndex3).setHalfEdge(tempVList.get(2).getHalfEdge());

        calculateOppositeHalfEdge(tfacet);
    }

    private void calculateOppositeHalfEdge(TriangleFacet tf){
        HalfEdge he = tf.getHalfEdge();
        List<HalfEdge> tempHList = new ArrayList<>();
        tempHList.add(he);
        tempHList.add(he.getNext());
        tempHList.add(he.getNext().getNext());
        for(HalfEdge halfEdge : tempHList){
            for(Iterator<HalfEdge> itHalfEdge = hEList.iterator(); itHalfEdge.hasNext();){
                HalfEdge hE = itHalfEdge.next();

                Vertex startVertex = halfEdge.getStartVertex();
                Vertex endVertex = halfEdge.getNext().getStartVertex();

                Vertex startVertexOpposite = hE.getStartVertex();
                Vertex endVertexOpposite = hE.getNext().getStartVertex();

                if (startVertex.equals(endVertexOpposite) && endVertex.equals(startVertexOpposite)) {
                    halfEdge.setOpposite(hE);
                    hE.setOpposite(halfEdge);
                }
            }
        }
    }

    public TriangleFacet getFacet(int triangleIndex) {
        return tFList.get(triangleIndex);
    }

    @Override
    public void addTriangle(AbstractTriangle t) {
        TriangleFacet tfacet = new TriangleFacet();

        List<Vertex> tempVList = new ArrayList<>();
        tempVList.add(vList.get(t.getVertexIndices(0)));
        tempVList.add(vList.get(t.getVertexIndices(1)));
        tempVList.add(vList.get(t.getVertexIndices(2)));

        List<HalfEdge> tempHList = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            tempHList.add(new HalfEdge());
        }

        for(int i = 0; i < 3; i++){
            tempHList.get(i).setStartVertex(tempVList.get(i));
            tempVList.get(i).setHalfEdge(tempHList.get(i));
            tempHList.get(i).setFacet(tfacet);
            tempHList.get(i).setNext(tempHList.get((i + 1) % 3));
            hEList.add(tempHList.get(i));
        }

        tfacet.setHalfEdge(tempHList.get(0));
        tfacet.setVertexIndices(t.getVertexIndices(0), t.getVertexIndices(1), t.getVertexIndices(2));
        tFList.add(tfacet);
        calculateOppositeHalfEdge(tfacet);
    }

    @Override
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3, int texCoordIndex1, int texCoordIndex2, int texCoordIndex3) {
        /*
        Triangle t = new Triangle();
        t.setVertexIndices(vertexIndex1, vertexIndex2, vertexIndex3);
        t.setTextureCoordinates(texCoordIndex1, texCoordIndex2,texCoordIndex3);
        computeTriangleNormals();
        triangles.add(t);
        */
    }

    @Override
    public int getNumberOfTriangles() {
        return tFList.size();
    }

    @Override
    public AbstractTriangle getTriangle(int triangleIndex) {
        return tFList.get(triangleIndex);
    }

    @Override
    public void clear() {
        vList.clear();
        hEList.clear();
        tFList.clear();
    }

    @Override
    public void computeTriangleNormals() {
        for (ListIterator<TriangleFacet> itTF = tFList.listIterator(); itTF.hasNext();) {
            TriangleFacet tempFacet = itTF.next();

            Vector v1, v2, v3;
            v1 = tempFacet.getHalfEdge().getStartVertex().getPosition();
            v2 = tempFacet.getHalfEdge().getNext().getStartVertex().getPosition();
            v3 = tempFacet.getHalfEdge().getNext().getNext().getStartVertex().getPosition();

            Vector vNormal = (v2.subtract(v1)).cross((v3.subtract(v1)));

            vNormal = vNormal.getNormalized();

            tempFacet.setNormal(vNormal);
        }
        //-------------
        computeVertexNormals();
    }

    public void computeVertexNormals() {
        for (ListIterator<Vertex> itVertex = vList.listIterator(); itVertex.hasNext();) {
            Vertex v = itVertex.next();

            HalfEdge startEdge = v.getHalfEdge();
            Vector result = new Vector(0, 0, 0);

            HalfEdge currentEdge = startEdge;

            do {
                result = result.add(currentEdge.getNext().getFacet().getNormal());
                currentEdge = currentEdge.getOpposite().getNext();

            } while (!(currentEdge.equals(startEdge)));

            result = result.getNormalized();
            v.setNormal(result);
        }
    }

    @Override
    public Vector getTextureCoordinate(int index) {
        return null;
    }

    @Override
    public void addTextureCoordinate(Vector t) {

    }

    @Override
    public Texture getTexture() {
        return null;
    }

    @Override
    public int getNumberOfTextureCoordinates() {
        return 0;
    }

    @Override
    public boolean hasTexture() {
        return false;
    }

    @Override
    public AxisAlignedBoundingBox getBoundingBox() {
        return null;
    }

    @Override
    public void setTextureName(String textureFilename) {

    }

    @Override
    public void setColor(Vector color) {

    }

    @Override
    public void setTransparency(double alpha) {

    }
}
