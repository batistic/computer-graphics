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
package edu.hawhamburg.shared.datastructures.mesh;

import edu.hawhamburg.shared.datastructures.mesh.ITriangleMesh;
import edu.hawhamburg.shared.math.Vector;

/**
 * Create some simple triangle meshes.
 */
public class TriangleMeshGenerator {
    /**
     * Create a cylinder mesh.
     */
    public static void createCylinder(ITriangleMesh mesh, double radius, double height,
                                      int resCircle, int resHeight,
                                      ITriangleMeshFactory triangleMeshFactory) {
        Vector color = new Vector(0.75, 0.25, 0.25, 1);
        double angleCircleDelta = 2.0 * Math.PI / resCircle;
        double deltaHeight = height / (resHeight - 1);
        for (int j = 0; j < resHeight; j++) {
            double h = j * deltaHeight;
            for (int i = 0; i < resCircle; i++) {
                Vector v = new Vector(Math.sin(angleCircleDelta * i) * radius, h, Math.cos(angleCircleDelta * i) * radius);
                mesh.addVertex(v);
                if (j > 0) {
                    int i00 = (j - 1) * resCircle + i;
                    int i01 = (j - 1) * resCircle + (i + 1) % resCircle;
                    int i10 = j * resCircle + i;
                    int i11 = j * resCircle + (i + 1) % resCircle;
                    AbstractTriangle t0 = triangleMeshFactory.createTriangle();
                    t0.setVertexIndices(i00, i01, i11);
                    AbstractTriangle t1 = triangleMeshFactory.createTriangle();
                    t1.setVertexIndices(i00, i11, i10);
                    t0.setColor(color);
                    t1.setColor(color);
                    mesh.addTriangle(t0);
                    mesh.addTriangle(t1);
                }

                if (j == resHeight - 1) {
                    // Create top
                    int iBase = j * resCircle;
                    int i10 = j * resCircle + i;
                    int i11 = j * resCircle + (i + 1) % resCircle;
                    AbstractTriangle tTop = triangleMeshFactory.createTriangle();
                    tTop.setVertexIndices(iBase, i10, i11);
                    tTop.setColor(color);
                    mesh.addTriangle(tTop);

                    // Create base
                    iBase = 0;
                    i10 = i;
                    i11 = (i + 1) % resCircle;
                    AbstractTriangle tBase = triangleMeshFactory.createTriangle();
                    tBase.setVertexIndices(iBase, i11, i10);
                    tBase.setColor(color);
                    mesh.addTriangle(tBase);
                }
            }
        }


        mesh.computeTriangleNormals();
    }

    /**
     * Create sphere.
     */
    public static void createSphere(ITriangleMesh mesh, double radius,
                                    int resolution) {
        mesh.clear();
        float dTheta = (float) (Math.PI / (resolution + 1));
        float dPhi = (float) (Math.PI * 2.0 / resolution);
        // Create vertices

        // 0-180 degrees: i, theta
        for (int i = 0; i < resolution; i++) {

            // 0-360 degres: j, phi
            for (int j = 0; j < resolution; j++) {
                Vector p0 = evaluateSpherePoint((i + 1) * dTheta, j * dPhi, radius);
                mesh.addVertex(p0);
            }
        }
        int leftIndex = mesh.addVertex(new Vector(0, 0, radius));
        int rightIndex = mesh.addVertex(new Vector(0, 0, -radius));
        // Add triangles
        for (int i = 0; i < resolution - 1; i++) {
            for (int j = 0; j < resolution; j++) {
                mesh.addTriangle(getSphereIndex(i, j, resolution),
                        getSphereIndex(i + 1, j, resolution),
                        getSphereIndex(i + 1, j + 1, resolution));
                mesh.addTriangle(getSphereIndex(i, j, resolution),
                        getSphereIndex(i + 1, j + 1, resolution),
                        getSphereIndex(i, j + 1, resolution));
            }
        }
        for (int j = 0; j < resolution; j++) {
            mesh.addTriangle(getSphereIndex(0, j, resolution),
                    getSphereIndex(0, (j + 1) % resolution, resolution), leftIndex);
            mesh.addTriangle(getSphereIndex(resolution - 1, j, resolution),
                    rightIndex,
                    getSphereIndex(resolution - 1, (j + 1) % resolution, resolution));
        }

        mesh.computeTriangleNormals();
    }

    private static Vector evaluateSpherePoint(double theta, double phi,
                                              double radius) {
        float x = (float) (radius * Math.sin(theta) * Math.cos(phi));
        float y = (float) (radius * Math.sin(theta) * Math.sin(phi));
        float z = (float) (radius * Math.cos(theta));
        return new Vector(x, y, z);
    }

    private static int getSphereIndex(int i, int j, int resolution) {
        return (i % resolution) * resolution + (j % resolution);
    }

    public static void createSquare(ITriangleMesh mesh, float extend) {
        mesh.clear();
        mesh.addVertex(new Vector(-extend, 0, -extend));
        mesh.addVertex(new Vector(extend, 0, -extend));
        mesh.addVertex(new Vector(extend, 0, extend));
        mesh.addVertex(new Vector(-extend, 0, extend));
        mesh.addTriangle(0, 2, 1);
        mesh.addTriangle(0, 3, 2);
        mesh.computeTriangleNormals();
    }

    public static void createCube(ITriangleMesh mesh, ITriangleMeshFactory triangleMeshFactory) {
        createCube(mesh, new Vector(0, 0, 0), triangleMeshFactory);
    }

    public static void createCube(ITriangleMesh mesh, Vector offset, ITriangleMeshFactory triangleMeshFactory) {
        mesh.clear();

        double d = 0.5;
        int v000 = mesh
                .addVertex(new Vector(-d, -d, -d).add(offset));
        int v010 = mesh
                .addVertex(new Vector(-d, d, -d).add(offset));
        int v110 = mesh
                .addVertex(new Vector(d, d, -d).add(offset));
        int v100 = mesh
                .addVertex(new Vector(d, -d, -d).add(offset));
        int v001 = mesh
                .addVertex(new Vector(-d, -d, d).add(offset));
        int v011 = mesh
                .addVertex(new Vector(-d, d, d).add(offset));
        int v111 = mesh.addVertex(new Vector(d, d, d).add(offset));
        int v101 = mesh
                .addVertex(new Vector(d, -d, d).add(offset));

        // front
        mesh.addTriangle(triangleMeshFactory.createTriangle(v000, v110, v100));
        mesh.addTriangle(triangleMeshFactory.createTriangle(v000, v010, v110));
        // right
        mesh.addTriangle(triangleMeshFactory.createTriangle(v100, v111, v101));
        mesh.addTriangle(triangleMeshFactory.createTriangle(v100, v110, v111));
        // back
        mesh.addTriangle(triangleMeshFactory.createTriangle(v101, v011, v001));
        mesh.addTriangle(triangleMeshFactory.createTriangle(v101, v111, v011));
        // left
        mesh.addTriangle(triangleMeshFactory.createTriangle(v001, v010, v000));
        mesh.addTriangle(triangleMeshFactory.createTriangle(v001, v011, v010));
        // top
        mesh.addTriangle(triangleMeshFactory.createTriangle(v010, v111, v110));
        mesh.addTriangle(triangleMeshFactory.createTriangle(v010, v011, v111));
        // bottom
        mesh.addTriangle(triangleMeshFactory.createTriangle(000, v101, v100));
        mesh.addTriangle(triangleMeshFactory.createTriangle(v000, v001, v101));

        mesh.computeTriangleNormals();
    }

    /**
     * Create a plane mesh.
     */
    public static void createPlane(ITriangleMesh mesh, Vector center, Vector normal,
                                   double sideLength, ITriangleMeshFactory triangleMeshFactory) {

        Vector t0 = null;
        if (Math.abs(normal.multiply(Vector.VECTOR_3_X)) > 0.95) {
            t0 = normal.cross(Vector.VECTOR_3_Y).getNormalized();
        } else {
            t0 = normal.cross(Vector.VECTOR_3_X).getNormalized();
        }
        Vector t1 = normal.cross(t0).getNormalized();
        t0 = t0.multiply(sideLength / 2.0);
        t1 = t1.multiply(sideLength / 2.0);
        Vector v00 = center.subtract(t0).subtract(t1);
        Vector v01 = center.subtract(t0).add(t1);
        Vector v11 = center.add(t0).add(t1);
        Vector v10 = center.add(t0).subtract(t1);

        mesh.clear();
        mesh.addVertex(v00);
        mesh.addVertex(v01);
        mesh.addVertex(v11);
        mesh.addVertex(v10);
        AbstractTriangle tri0 = triangleMeshFactory.createTriangle(0, 2, 1);
        AbstractTriangle tri1 = triangleMeshFactory.createTriangle(0, 3, 2);
        tri0.setColor(new Vector(0.25, 0.5, 0.25, 1));
        tri1.setColor(new Vector(0.25, 0.5, 0.25, 1));
        mesh.addTriangle(tri0);
        mesh.addTriangle(tri1);
    }
}
