package edu.hawhamburg.shared.datastructures.mesh;

/**
 * Created by User on 25/04/2018.
 */

public class HalfEdge {

    private Vertex startVertex;
    private TriangleFacet facet;
    private HalfEdge nextHedge;
    private HalfEdge opposite;



    public HalfEdge(){

    }


    public void setFacet(TriangleFacet facet){
        this.facet = facet;
    }

    public TriangleFacet getFacet() {
        return facet;
    }

    public void setNext(HalfEdge nextH){
        this.nextHedge = nextH;
    }

    public HalfEdge getNext(){
        return nextHedge;
    }

    public Vertex getStartVertex(){
        return startVertex;
    }

    public void setStartVertex(Vertex startVertex){
        this.startVertex = startVertex;
    }

    public void setOpposite(HalfEdge opposite){
        this.opposite = opposite;
    }

    public HalfEdge getOpposite(){
        return opposite;
    }

}
