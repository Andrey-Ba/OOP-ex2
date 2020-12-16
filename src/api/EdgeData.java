package api;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EdgeData implements edge_data
{
    //Used
    private int ID;

    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;
    private double tw;
    static int id=0;

    public EdgeData(int s, int d)
    {
        ID=id;
        src = s;
        dest = d;
        weight = -1;
        info = "";
        tag = -1;
        tw = -1;
        id++;
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

    public int getID() {
        return ID;
    }

    public double gettw() {
        return tw;
    }

    public void settw(double tw) {
        this.tw = tw;
    }

    @Override
    public String toString() {
        return "EdgeData{" +
                "src=" + src +
                ", dest=" + dest +
                ", weight=" + weight +
                ", info='" + info + '\'' +
                ", tag=" + tag +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeData edgeData = (EdgeData) o;
        return src == edgeData.src && dest == edgeData.dest && Double.compare(edgeData.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(src, dest, weight);
    }
}
