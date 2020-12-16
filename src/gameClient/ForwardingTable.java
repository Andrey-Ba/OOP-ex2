package gameClient;

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
