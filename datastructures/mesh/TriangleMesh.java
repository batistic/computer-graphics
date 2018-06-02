package edu.hawhamburg.shared.datastructures.mesh;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.rendering.Texture;

/**
 * Created by User on 29/03/2018.
 */

public class TriangleMesh implements ITriangleMesh {

    private List<AbstractTriangle>triangles;
    private List<Vertex>vertices;

    public TriangleMesh(){
        triangles = new ArrayList<AbstractTriangle>();
        vertices = new ArrayList<Vertex>();
    }

    @Override
    public int addVertex(Vector position) {
        Vertex newVertex = new Vertex(position);
        vertices.add(newVertex);
        return 0;
    }

    @Override
    public int addVertex(Vertex vertex) {
        vertices.add(vertex);
        return 0;
    }

    @Override
    public Vertex getVertex(int index) {
         return vertices.get(index);
    }

    @Override
    public Vertex getVertex(AbstractTriangle triangle, int index) {

        int realIndex;
        for(int i=0;i<triangles.size();i++){
            if (triangle.equals(triangles.get(i))){
                realIndex = triangles.get(i).getVertexIndices(index);
                return vertices.get(realIndex);
            }
        }
        return null;
    }

    @Override
    public int getNumberOfVertices() {
        int count = 0;
        for(int i = 0; i < vertices.size(); i++){
            count++;
        }
        return count;
    }

    @Override
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        Triangle t = new Triangle();
        t.setVertexIndices(vertexIndex1,vertexIndex2,vertexIndex3);
        computeTriangleNormals();

        triangles.add(t);
    }

    @Override
    public void addTriangle(AbstractTriangle t) {
        triangles.add(t);
    }

    @Override
    public void addTriangle(int vertexIndex1, int vertexIndex2, int vertexIndex3, int texCoordIndex1, int texCoordIndex2, int texCoordIndex3) {
        Triangle t = new Triangle();
        t.setVertexIndices(vertexIndex1, vertexIndex2, vertexIndex3);
        t.setTextureCoordinates(texCoordIndex1, texCoordIndex2,texCoordIndex3);
        computeTriangleNormals();
        triangles.add(t);
    }

    @Override
    public int getNumberOfTriangles() {
        return triangles.size();
    }

    @Override
    public AbstractTriangle getTriangle(int triangleIndex) {
        return triangles.get(triangleIndex);
    }

    @Override
    public void clear() {
        triangles.clear();
        vertices.clear();

    }

    @Override
    public void computeTriangleNormals() {
        for(AbstractTriangle t: this.triangles){
            Vector a, b, c, ab, ac;

            a = vertices.get(t.getVertexIndices(0)).getPosition();
            b = vertices.get(t.getVertexIndices(1)).getPosition();
            c = vertices.get(t.getVertexIndices(2)).getPosition();

            ab = a.subtract(b);
            ac = c.subtract(a);

            t.setNormal(new Vector(ab.cross(ac).getNormalized()));
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
    public Texture getTexture() { return null; }

    @Override
    public int getNumberOfTextureCoordinates() { return 0; }

    @Override
    public boolean hasTexture() {
        return false;
    }

    @Override
    public AxisAlignedBoundingBox getBoundingBox() { return null; }

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
