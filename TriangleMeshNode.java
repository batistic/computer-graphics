package edu.hawhamburg.shared.scenegraph;

import android.opengl.GLES20;

import java.util.ArrayList;
import java.util.List;

import edu.hawhamburg.shared.datastructures.mesh.AbstractTriangle;
import edu.hawhamburg.shared.datastructures.mesh.HalfEdge;
import edu.hawhamburg.shared.datastructures.mesh.HalfEdgeTriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.TriangleMesh;
import edu.hawhamburg.shared.datastructures.mesh.Vertex;
import edu.hawhamburg.shared.math.AxisAlignedBoundingBox;
import edu.hawhamburg.shared.math.Matrix;
import edu.hawhamburg.shared.math.Vector;
import edu.hawhamburg.shared.rendering.RenderVertex;
import edu.hawhamburg.shared.rendering.Shader;
import edu.hawhamburg.shared.rendering.ShaderAttributes;
import edu.hawhamburg.shared.rendering.VertexBufferObject;


public class TriangleMeshNode extends ITriangleMeshNode {

    public static enum RenderNormals {
        PER_TRIANGLE_NORMAL, PER_VERTEX_NORMAL
    }
    /**
     * Select which normals should be used for rendering: triangle vs. vertex normals.
     */
    private RenderNormals renderNormals = RenderNormals.PER_TRIANGLE_NORMAL;

    private HalfEdgeTriangleMesh tmesh;
    private TriangleMesh mesh;
    private VertexBufferObject vbo = new VertexBufferObject();

    /**
     * VBOs: normals
     */
    private VertexBufferObject vboNormals = new VertexBufferObject();
    /**
     * Debugging: Show normals.
     */
    private boolean showNormals = false;

    /**
     * Constructor.
     */

    public TriangleMeshNode(){

    }

    public TriangleMeshNode(HalfEdgeTriangleMesh triangleMesh) {
        tmesh = triangleMesh;
        createVbo();
    }



    private void createVbo(){
        List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
        tmesh.computeTriangleNormals();
        for(int i=0;i<tmesh.getNumberOfTriangles();i++){
            Vector p0 = tmesh.getFacet(i).getHalfEdge().getStartVertex().getPosition();
            Vector p1 = tmesh.getFacet(i).getHalfEdge().getNext().getStartVertex().getPosition();
            Vector p2 = tmesh.getFacet(i).getHalfEdge().getNext().getNext().getStartVertex().getPosition();
            Vector n1 = tmesh.getFacet(i).getHalfEdge().getStartVertex().getNormal();
            Vector n2 = tmesh.getFacet(i).getHalfEdge().getNext().getStartVertex().getNormal();
            Vector n3 = tmesh.getFacet(i).getHalfEdge().getNext().getNext().getStartVertex().getNormal();
            Vector color = tmesh.getFacet(i).getColor();
            AddSideVertices(renderVertices,p0,p1,p2,n1, n2, n3,color);
            /*
            Vector p0 = mesh.getVertex(mesh.getTriangle(i),0).getPosition();
            Vector p1 = mesh.getVertex(mesh.getTriangle(i),1).getPosition();
            Vector p2 = mesh.getVertex(mesh.getTriangle(i),2).getPosition();
            Vector n = mesh.getTriangle(i).getNormal();*/
        }
        vbo.setup(renderVertices, GLES20.GL_TRIANGLES);
    }


    /**
     * Add 3 vertices to the array
     */
    private void AddSideVertices(List<RenderVertex> renderVertices, Vector p0,
                                 Vector p1, Vector p2, Vector n1, Vector n2, Vector n3, Vector color) {
        renderVertices.add(new RenderVertex(p0, n1, color));
        renderVertices.add(new RenderVertex(p1, n2, color));
        renderVertices.add(new RenderVertex(p2, n3, color));
    }


    //---------------------------------------------------------------------------
    @Override
    public void createVbo1(TriangleMesh mesh){
        this.mesh = mesh;
        List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
        this.mesh.computeTriangleNormals();
        for(int i=0;i<mesh.getNumberOfTriangles();i++){
            Vector p0 = this.mesh.getVertex(this.mesh.getTriangle(i),0).getPosition();
            Vector p1 = this.mesh.getVertex(this.mesh.getTriangle(i),1).getPosition();
            Vector p2 = this.mesh.getVertex(this.mesh.getTriangle(i),2).getPosition();

            Vector n = this.mesh.getTriangle(i).getNormal();

            Vector color = this.mesh.getTriangle(i).getColor();
            AddSideVertices1(renderVertices,p0,p1,p2,n,color);
        }
        vbo.setup(renderVertices, GLES20.GL_TRIANGLES);
        vboNormals.setup(createRenderVerticesNormals(), GLES20.GL_LINES);
    }


    private void AddSideVertices1(List<RenderVertex> renderVertices, Vector p0,
                                  Vector p1, Vector p2, Vector n, Vector color) {
        renderVertices.add(new RenderVertex(p0, n, color));
        renderVertices.add(new RenderVertex(p1, n, color));
        renderVertices.add(new RenderVertex(p2, n, color));
    }

    /**
     * Draw mesh regularly.
     */
    public void drawRegular() {
        vbo.draw();
        if (showNormals) {
            vboNormals.draw();
        }
    }

    public void setShowNormals(boolean showNormals) {
        this.showNormals = showNormals;
    }
    //-------------------------------------------------------------------------------------------------

    @Override
    public void drawGL(RenderMode mode, Matrix modelMatrix) {
        ShaderAttributes.getInstance().setShaderModeParameter(Shader.ShaderMode.PHONG);
        if (mode == RenderMode.REGULAR) {
            vbo.draw();
        }
    }

    @Override
    public AxisAlignedBoundingBox getBoundingBox() {
        return null;
    }


    public void setup(ITriangleMesh mesh) {
        this.mesh = (TriangleMesh) mesh;
        createVbo1(this.mesh);

    }

    @Override
    public void updateVbo() {
        vbo.setup(createRenderVertices(), GLES20.GL_TRIANGLES);
        vboNormals.setup(createRenderVerticesNormals(), GLES20.GL_LINES);
        vbo.invalidate();
    }

    @Override
    public void setTransparency(double v) {

    }

    /**
     * Create vbo data for mesh rendering
     */
    private List<RenderVertex> createRenderVertices() {
        List<RenderVertex> renderVertices = new ArrayList<>();
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            AbstractTriangle t = mesh.getTriangle(i);
            for (int j = 0; j < 3; j++) {
                Vertex vertex = mesh.getVertex(t, j);
                Vector normal = (renderNormals == RenderNormals.PER_VERTEX_NORMAL) ? vertex.getNormal() : t.getNormal();
                RenderVertex renderVertex = null;
                renderVertex = new RenderVertex(vertex.getPosition(), normal, t.getColor(), new Vector(0, 0));
                renderVertices.add(renderVertex);
            }
        }
        return renderVertices;
    }

    /**
     * Create vbo data for normal rendering.
     */
    private List<RenderVertex> createRenderVerticesNormals() {
        List<RenderVertex> renderVertices = new ArrayList<RenderVertex>();
        double normalScale = 0.03;
        Vector color = new Vector(0.5, 0.5, 0.5, 1);
        for (int i = 0; i < mesh.getNumberOfTriangles(); i++) {
            AbstractTriangle t = mesh.getTriangle(i);
            Vector p = mesh.getVertex(t, 0).getPosition()
                    .add(mesh.getVertex(t, 1).getPosition())
                    .add(mesh.getVertex(t, 2).getPosition())
                    .multiply(1.0 / 3.0);
            renderVertices.add(new RenderVertex(p, t.getNormal(), color));
            renderVertices.add(new RenderVertex(
                    p.add(t.getNormal().multiply(normalScale)), t.getNormal(), color));
        }
        return renderVertices;
    }
}
