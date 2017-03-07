package com.steel.mapgen.procGen;

import com.steel.mapgen.game.Settlement;
import com.steel.mapgen.map.overworld.BiomeType;
import com.steel.mapgen.map.overworld.OverWorld;
import com.steel.mapgen.map.overworld.OverWorldChunk;
import com.stewsters.util.math.MatUtils;
import com.stewsters.util.math.Point2i;
import com.stewsters.util.noise.OpenSimplexNoise;
import com.stewsters.util.pathing.twoDimention.pathfinder.AStarPathFinder2d;
import com.stewsters.util.pathing.twoDimention.shared.FullPath2d;
import com.stewsters.util.pathing.twoDimention.shared.Mover2d;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.stewsters.util.math.MatUtils.d;

public class WorldGenerator {

    private final static Logger log = Logger.getLogger(WorldGenerator.class.getName());
    private static final int uncalculatedRegion = -1;
    private static final int blockedRegion = -2;
    OpenSimplexNoise el;
    OpenSimplexNoise mo;
    Random r;

    public WorldGenerator(long seed) {
        r = new Random(seed);
        el = new OpenSimplexNoise(r.nextLong());
        mo = new OpenSimplexNoise(r.nextLong());
    }

    public WorldGenerator() {
        r = new Random();
        el = new OpenSimplexNoise(r.nextLong());
        mo = new OpenSimplexNoise(r.nextLong());
    }

    public void generateElevation(OverWorld overWorld) {

        Line2D line = new Line2D.Float(new Point2D.Float(00, overWorld.getPreciseYSize()), new Point2D.Float(overWorld.getPreciseXSize(), 0));
        ShapeMod mod = (x, y) -> (1.2 - Math.max(line.ptLineDist(x, y) / 160f, 1));

        IntStream.range(0, overWorld.xSize).parallel().forEach(x -> {
            IntStream.range(0, overWorld.ySize).parallel().forEach(y -> {
                overWorld.chunks[x][y] = generateChunkedHeightMap(overWorld, Arrays.asList(mod), x, y);
            });
        });
    }

    // Initial step the can be done per chunk
    public OverWorldChunk generateChunkedHeightMap(OverWorld overWorld, List<ShapeMod> shapeMods, int chunkX, int chunkY) {

        OverWorldChunk overWorldChunk = new OverWorldChunk();

        for (int x = 0; x < OverWorldChunk.chunkSize; x++) {
            for (int y = 0; y < OverWorldChunk.chunkSize; y++) {

                //global coord
                int nx = chunkX * OverWorldChunk.chunkSize + x;
                int ny = chunkY * OverWorldChunk.chunkSize + y;

                double ridginess = fbm(nx, ny, 6, 1 / 320D, 1, 2D, 0.5D);
                ridginess = Math.abs(ridginess) * -1;

                double elevation = Math.max((fbm(nx, ny, 6, 1 / 200D, 1, 2D, 0.5D)), (ridginess));

                for (ShapeMod mod : shapeMods) {
                    elevation += mod.modify(nx, ny);
                }

                overWorldChunk.elevation[x][y] = (float) elevation;
            }
        }
        return overWorldChunk;
    }

    public double fbm(double x, double y, int octaves, double frequency, double amplitude, double lacunarity, double gain) {
        double total = 0.0;
        for (int i = 0; i < octaves; ++i) {
            total += el.eval(x * frequency, y * frequency) * amplitude;
            frequency *= lacunarity;
            amplitude *= gain;
        }
        return total;
    }

    public void dropEdges(OverWorld overWorld) {
        int xSize = overWorld.getPreciseXSize();
        int ySize = overWorld.getPreciseYSize();


        int xCenter = overWorld.xSize * OverWorldChunk.chunkSize / 2;
        int yCenter = overWorld.ySize * OverWorldChunk.chunkSize / 2;

        double maxDist = Math.sqrt(xCenter * xCenter + yCenter * yCenter);

        for (int y = 0; y < ySize; y++) {
            for (int x = 0; x < xSize; x++) {
                double elev = overWorld.getElevation(x, y);
                double dist = Math.sqrt(Math.pow(xCenter - x, 2) + Math.pow(yCenter - y, 2));

                // Elevation - decreases near edges
                elev = MatUtils.limit(elev - (Math.pow(dist / maxDist, 4)), -1, 1);
                overWorld.setElevation(x, y, (float) elev);
            }
        }
    }

    /**
     * Build Settlements
     * <p>
     * Human settlements should be built near a source of water, preferably a river.
     * Most should be built at a low elevation, near an area with high river flux
     * Generating a good distance from other towns
     * <p>
     * Population should follow https://en.wikipedia.org/wiki/Zipf%27s_law
     *
     * @param overWorld
     */
    public void populateSettlements(OverWorld overWorld) {
//        Bus.bus.post("Populating Settlements").now();
        int xSize = overWorld.getPreciseXSize();
        int ySize = overWorld.getPreciseYSize();
        int totalSettlements = 20;
        int propositions = 1000;

        // Build Settlements
        for (int i = 0; i < totalSettlements; i++) {

            ArrayList<Point2i> possibleLocations = new ArrayList<>();
            int pop = d(1000);

            // Propose new settlement locations
            for (int j = 0; j < propositions; j++) {
                int x = MatUtils.getIntInRange(0, xSize);
                int y = MatUtils.getIntInRange(0, ySize);
                possibleLocations.add(new Point2i(x, y));
            }

            float bestScore = Float.NEGATIVE_INFINITY;
            Point2i bestLocal = null;

            for (Point2i p : possibleLocations) {

                float score = 0;
                BiomeType biomeType = overWorld.getTileType(p.x, p.y);

                if (biomeType.water) {
                    score -= 1000;
                }

                for (Settlement s : Settlement.settlements) {
                    float dist = p.getChebyshevDistance(s.pos);
                    score -= s.population * pop / (dist * dist);
                }

                float percip = overWorld.getPrecipitation(p.x, p.y);

                score += percip * 10;

                if (score > bestScore) {
                    bestScore = score;
                    bestLocal = p;
                }

            }

            if (bestLocal != null) {
//                Bus.bus.post(bestLocal.toString()).now();
                overWorld.buildSettlement(bestLocal.x, bestLocal.y, pop);
            }
        }
    }

    // http://roadtrees.com/creating-road-trees/
    public void createRoadNetwork(OverWorld overWorld) {
        //Step One Determine which cities to link together first. Larger closer cities should go first

        HashMap<Integer, ArrayList<Settlement>> settlementsPerContinent = new HashMap<>();

        for (Settlement settlement : Settlement.settlements) {
            int id = overWorld.getRegionId(settlement.pos.x, settlement.pos.y);

            ArrayList<Settlement> settlements = settlementsPerContinent.get(id);
            if (settlements == null) {
                settlements = new ArrayList<>();
                settlementsPerContinent.put(id, settlements);
            }
            settlements.add(settlement);
        }

        settlementsPerContinent.values().parallelStream().forEach(settlements -> {

            ArrayList<RankedSettlementPair> pairs = new ArrayList<>();

            for (int a = 0; a < settlements.size() - 1; a++) {
                for (int b = a + 1; b < settlements.size(); b++) {
                    pairs.add(new RankedSettlementPair(settlements.get(a), settlements.get(b)));
                }
            }
            Collections.sort(pairs);

            AStarPathFinder2d pathFinder2d = new AStarPathFinder2d(overWorld, overWorld.getPreciseXSize() * overWorld.getPreciseYSize());
            Mover2d mover2d = new RoadRunnerMover(overWorld);

            for (RankedSettlementPair p : pairs) {

                Settlement a = Settlement.settlements.get(p.a);
                Settlement b = Settlement.settlements.get(p.b);
                FullPath2d path = pathFinder2d.findPath(mover2d, a.pos.x, a.pos.y, b.pos.x, b.pos.y);

                if (path != null) {

                    for (int i = 0; i < path.getLength(); i++) {
                        FullPath2d.Step step = path.getStep(i);
                        overWorld.setRoad(step.getX(), step.getY());
                    }

                }
            }

        });


        // Use A* to link cities.  Cost should reflect slope and terrain type.  Bridges are possible, but expensive.

        //The first cities that are linked will have busy roads, and they will shrink down as


    }

    // This finds contiguous land masses
    public void generateContinents(OverWorld overWorld) {

        //Reset
        for (int x = 0; x < overWorld.getPreciseXSize(); x++) {
            for (int y = 0; y < overWorld.getPreciseYSize(); y++) {
                if (overWorld.getElevation(x, y) < 0)
                    overWorld.setRegion(x, y, blockedRegion);
                else
                    overWorld.setRegion(x, y, uncalculatedRegion);
            }
        }

        int i = 0;
        for (int x = 0; x < overWorld.getPreciseXSize(); x++) {
            for (int y = 0; y < overWorld.getPreciseYSize(); y++) {
                if (overWorld.getRegionId(x, y) == uncalculatedRegion) {
                    floodFillBFS(overWorld, x, y, uncalculatedRegion, i++);
                }
            }
        }

    }

    private void floodFillBFS(OverWorld overWorld, int sx, int sy, int target, int replacement) {

        Point2i q = new Point2i(sx, sy);
        int xSize = overWorld.getPreciseXSize();
        int ySize = overWorld.getPreciseYSize();

        if (q.y < 0 || q.y >= ySize || q.x < 0 || q.x >= xSize)
            return;

        Deque<Point2i> stack = new ArrayDeque<>();
        stack.push(q);
        while (stack.size() > 0) {
            Point2i p = stack.pop();
            int x = p.x;
            int y = p.y;
            if (y < 0 || y >= ySize || x < 0 || x >= xSize)
                continue;
            int val = overWorld.getRegionId(x, y);
            if (val == target) {
                overWorld.setRegion(x, y, replacement);

                if (x + 1 < xSize && overWorld.getRegionId(x + 1, y) == target)
                    stack.push(new Point2i(x + 1, y));
                if (x - 1 > 0 && overWorld.getRegionId(x - 1, y) == target)
                    stack.push(new Point2i(x - 1, y));
                if (y + 1 < ySize && overWorld.getRegionId(x, y + 1) == target)
                    stack.push(new Point2i(x, y + 1));
                if (y - 1 > 0 && overWorld.getRegionId(x, y - 1) == target)
                    stack.push(new Point2i(x, y - 1));
            }
        }

    }

    public void expandRealms(OverWorld overWorld) {

        float[][] distance = new float[overWorld.getPreciseXSize()][overWorld.getPreciseYSize()];
        for (int x = 0; x < overWorld.xSize; x++) {
            for (int y = 0; y < overWorld.ySize; y++) {
                distance[x][y] = Float.MAX_VALUE;
            }
        }

        Map<Settlement, HashSet<Point2i>> borderlands = new HashMap<>();

        Settlement.settlements.stream()
                .sorted(Comparator.comparingInt(me -> me.population))
//                .limit(10)
                .forEach(settlement -> {

                    overWorld.setRealmId(settlement.pos.x, settlement.pos.y, settlement.id);
                    distance[settlement.pos.x][settlement.pos.y] = 0f;
                    borderlands.put(settlement, settlement.pos.mooreNeighborhood().stream()
                            .filter(it -> !overWorld.isOutside(it.x, it.y))
                            .collect(Collectors.toCollection(HashSet::new)));
                });

        for (int i = 0; i < 100000; i++) {

            borderlands.forEach((k, v) -> {
                // Take a tile from each openset.

                if (v.size() <= 0)
                    return;

//                int id = MatUtils.getIntInRange(0, v.size() - 1);
                Point2i p = v.stream().min((it, o) -> Float.compare(distance[it.x][it.y], distance[o.x][o.y])).get();

                if (overWorld.getRealmId(p.x, p.y) == -1) {

                    overWorld.setRealmId(p.x, p.y, k.id);

                    p.mooreNeighborhood().stream()
                            .filter(next -> overWorld.getRealmId(next.x, next.y) == -1)
                            .filter(next -> !overWorld.getTileType(next.x, next.y).water)
                            .forEach(next -> {

                                if (!v.contains(next)) {
                                    v.add(next);
                                }

                                float nextCost = distance[p.x][p.y] + 1f + next.getChebyshevDistance(p) / 100f;
//                                + overWorld.getElevation(p.x, p.y)
                                if (nextCost < distance[next.x][next.y]) {
                                    distance[next.x][next.y] = nextCost;
                                }
                            });
                }
                v.remove(p);
            });

        }
    }
}

class RankedSettlementPair implements Comparable<RankedSettlementPair> {
    int a;
    int b;
    double distance;

    public RankedSettlementPair(Settlement a, Settlement b) {
        this.a = a.id;
        this.b = b.id;

        distance = (a.population * b.population / Math.pow(a.pos.getChebyshevDistance(b.pos), 2));
    }

    @Override
    public int compareTo(RankedSettlementPair o) {
        if (distance == o.distance) {
            return 0;
        }
        return distance - o.distance > 0 ? 1 : -1;
    }
}
