package edu.hawhamburg.shared.datastructures.mesh;

public class TriangleMeshFactory implements ITriangleMeshFactory {

    public TriangleMeshFactory(){

    }

    @Override
    public HalfEdgeTriangleMesh createMesh(){
        HalfEdgeTriangleMesh mesh = new HalfEdgeTriangleMesh();
        return mesh;
    }

    @Override
    public TriangleMesh createTriangleMesh(){
        TriangleMesh mesh = new TriangleMesh();
        return mesh;
    }

    @Override
    public HalfEdgeTriangleMesh triangleToHalfEdge(TriangleMesh mesh){
        HalfEdgeTriangleMesh newMesh = new HalfEdgeTriangleMesh();
        for(int i=0;i<mesh.getNumberOfVertices();i++){
            newMesh.addVertex(mesh.getVertex(i));
        }
        for(int i=0;i<mesh.getNumberOfTriangles();i++){
            newMesh.addTriangle(mesh.getTriangle(i).getVertexIndices(0),mesh.getTriangle(i).getVertexIndices(1),mesh.getTriangle(i).getVertexIndices(2));
        }
        return newMesh;
    }

    @Override
    public AbstractTriangle createTriangle(){
        //TriangleFacet triangle = new TriangleFacet();
        return new Triangle();
    }

    @Override
    public AbstractTriangle createTriangle(int v0, int v1, int v2){
        Triangle triangle = new Triangle(v0,v1,v2);
        return triangle;
    }

    @Override
    public AbstractTriangle createFacet(){
        //TriangleFacet triangle = new TriangleFacet();
        return new TriangleFacet();
    }

    @Override
    public AbstractTriangle createFacet(int v0, int v1, int v2){
        TriangleFacet triangle = new TriangleFacet(v0,v1,v2);
        return triangle;
    }
}
