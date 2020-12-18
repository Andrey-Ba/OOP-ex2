package gameClient;



//This class is used to give valuable information on each node and make it reachable in O(1).
//For each node it saves for any given edge how far it is from the node and which node should
//be crossed to for the fastest way to get to the edge.
//Example 1-(0.5)->2-(0.5)->3
//If I start an node 1 and want to get to edge 2->3 (to end up on 3)
//The table will save the the edge 2->3 and node 1 the values: distance = 1 and next node = 2
public class ForwardingTable {
    private int destedge;
    private double distance;
    private int nextnode;

    public ForwardingTable(int dest, double dis, int next)
    {
        destedge = dest;
        distance = dis;
        nextnode = next;
    }

    public int getDestedge() {
        return destedge;
    }

    public double getDistance() {
        return distance;
    }

    public int getNextnode() {
        return nextnode;
    }

    @Override
    public String toString() {
        return "ForwardingTable{" +
                "destedge=" + destedge +
                ", distance=" + distance +
                ", nextnode=" + nextnode +
                '}';
    }
}
