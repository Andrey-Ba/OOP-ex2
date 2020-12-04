package api;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

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
