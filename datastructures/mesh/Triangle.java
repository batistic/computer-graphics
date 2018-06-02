package edu.hawhamburg.shared.datastructures.mesh;

import edu.hawhamburg.shared.math.Vector;

/**
 * Created by User on 19/04/2018.
 */

public class Triangle extends AbstractTriangle {

    protected  int[] vertexIndices = {-1, -1, -1};

    public Triangle(){super();}

    public Triangle(AbstractTriangle triangle){super(triangle);}

    public Triangle(int vA, int vB, int vC, int tC1, int tC2, int tC3){
        super();

        setVertexIndices(vA, vB, vC);
        setTextureCoordinates(tC1, tC2, tC3);
    }

    public Triangle(int v0, int v1, int v2){
        super();
        setVertexIndices(v0,v1,v2);
    }

    public Triangle(int tA, int tB, int tC, Vector normal){ super(tA, tB, tC, normal);}

    public Triangle(Vector normal){super(normal);}

    @Override
    public void addVertexIndexOffset(int vertexOffset) {

    }

    @Override
    public AbstractTriangle clone() { return new Triangle(this);  }

    @Override
    public void setVertexIndices(int vertexIndex1, int vertexIndex2, int vertexIndex3) {
        vertexIndices[0] = vertexIndex1;
        vertexIndices[1] = vertexIndex2;
        vertexIndices[2] = vertexIndex3;
    }

    @Override
    public  int getVertexIndices(int index){
        if(index < 3)
            return vertexIndices[index];
        return -1;
    }
}
