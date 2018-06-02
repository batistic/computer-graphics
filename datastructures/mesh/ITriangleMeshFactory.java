package edu.hawhamburg.shared.datastructures.mesh;

/**
 * This factory allows for the creation of mesh objects.
 */
public interface ITriangleMeshFactory {
    /**
     * Create an empty mesh object.
     */
    public HalfEdgeTriangleMesh createMesh();

    /**
     * Create an empty triangle mesh object.
     */
    public TriangleMesh createTriangleMesh();

    /**
     * Converts Triangle Mesh to Half Edge Triangle Mesh
     */
    public HalfEdgeTriangleMesh triangleToHalfEdge(TriangleMesh mesh);

    /**
     * Create a triangle object.
     */
    public AbstractTriangle createTriangle();

    /**
     * Create a triangle object from three vertex indices.
     */
    public AbstractTriangle createTriangle(int v0, int v1, int v2);

    /**
     * Create a triangle object.
     */
    public AbstractTriangle createFacet();

    /**
     * Create a triangle object from three vertex indices.
     */
    public AbstractTriangle createFacet(int v0, int v1, int v2);
}
