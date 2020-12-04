package api;

public class EdgeData implements edge_data
{
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    public EdgeData(int s, int d)
    {
        src = s;
        dest = d;
        weight = 0;
        info = "";
        tag = 0;
    }

    public EdgeData(int s, int d, double w)
    {
        this(s,d);
        weight = w;
    }

    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        tag = t;
    }

    public void setWeight(double w)
    {
        weight = w;
    }
}
