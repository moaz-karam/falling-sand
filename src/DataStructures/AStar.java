package DataStructures;

import Main.*;

import java.util.Hashtable;

public class AStar {
    private final Particle source;
    private int[] position;
    private boolean positionFound;
    private final Hashtable<Particle, Integer> distTo;
    private final MinHeap<Particle> heap;
    private int edgeDistance;


    public AStar(Particle s) {
        source = s;
        distTo = new Hashtable<>();
        positionFound = false;
        heap = new MinHeap<>();
        edgeDistance = 1;
        findPosition();
    }

    private boolean validEdge(int x, int y) {
        return ParticleHandler.validPoint(x, y) && ParticleHandler.getType(x, y) != Constants.WOOD;
    }

    private double heuristic(Particle p) {
        return p.getY();
    }

    private int edgeType(int x, int y) {
        return ParticleHandler.getType(x, y);
    }

    private void addEdgeHelper(Particle p, int x, int y) {

        if (edgeType(x, y) == 0) {
            positionFound = true;
            position = new int[] {x, y};
            return;
        }

        Particle edge = ParticleHandler.getParticle(x, y);

        if (edge.getType() == Constants.SAND) {
            Sand sand = (Sand) edge;
            if (sand.getWaterAbsorbed() < Sand.WATER_CAPACITY) {
                sand.absorbWater((Water)source);
                positionFound = true;
                return;
            }
        }

        if (!distTo.containsKey(edge)) {
            distTo.put(edge, distTo.get(p) + edgeDistance);
            heap.add(edge, distTo.get(edge) + heuristic(edge));
        }
    }

    private void addEdges(Particle p) {
        int x = p.getX();
        int y = p.getY();

        int up = y - edgeDistance;
        int right = x + edgeDistance;
        int left = x - edgeDistance;

        if (!positionFound && validEdge(right, y)) {
            addEdgeHelper(p, right, y);
        }
        if (!positionFound && validEdge(left, y)) {
            addEdgeHelper(p, left, y);
        }
        if (!positionFound && validEdge(x, up)) {
            addEdgeHelper(p, x, up);
        }
    }

    private void findPosition() {
        Particle current = source;
        distTo.put(current, 0);
        addEdges(current);
        int edgeCounter = 0;
        while (!positionFound && heap.getSize() > 0) {
            if (edgeCounter > Constants.PARTICLE_SPEED) {
                edgeDistance *= 2;
                edgeCounter = 0;
            }
            current = heap.removeSmallest();
            addEdges(current);
            edgeCounter += 1;
        }
    }

    public int[] getPosition() {
        return position;
    }
}
