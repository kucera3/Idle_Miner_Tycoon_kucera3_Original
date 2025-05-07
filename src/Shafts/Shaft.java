package Shafts;

import Materials.Material;
import Workable.Miner;

import java.util.ArrayList;
import java.util.List;

public class Shaft {
    private final int depth;
    private final Material material;
    private final List<Miner> miners;

    public Shaft(int depth, Material material) {
        this.depth = depth;
        this.material = material;
        this.miners = new ArrayList<>();
    }

    public int getDepth() {
        return depth;
    }

    public Material getMaterial() {
        return material;
    }

    public List<Miner> getMiners() {
        return miners;
    }

    public void addMiner(Miner miner) {
        if (!miners.contains(miner)) {
            miners.add(miner);
        }
    }

    public void mineAll() {
        for (Miner miner : miners) {
            miner.work(material);
        }
    }

    public int getTotalOutput() {
        int total = 0;
        for (Miner miner : miners) {
            total += miner.getOutput();
        }
        return total;
    }
}