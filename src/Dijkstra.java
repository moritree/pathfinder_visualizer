import java.util.PriorityQueue;
import java.util.Comparator;
import java.awt.*;

public class Dijkstra {

    private int[] start;
    private int[] end;
    private boolean reached = false;
    private PriorityQueue<int[]> pq;
    private graphicsPanel p;

    public Dijkstra(graphicsPanel panel, String mode) {
        p = panel;
        start = p.findStart().clone();
        p.setTile(start[0], start[1], -1);
        end = p.findEnd().clone();
        p.setTile(end[0], end[1], -1);

        Comparator<int[]> comparator;
        if (mode.equals("A*")) comparator = new AStarComparator();
        else comparator = new dijkstraComparator();
        pq = new PriorityQueue<>(comparator);
        pq.add(new int[]{start[0], start[1], 0});
        if (pq.peek() != null) recursiveDijkstra(pq.peek());
        shortestPath(end);
    }

    /** Recursive algorithm to fill tiles */
    private void recursiveDijkstra(int[] tile) {
        if (!reached && p.getTile(tile[0], tile[1]) == -1) {
            p.setTile(tile[0], tile[1], tile[2]);
            int n = tile[2] + 1;

            // add adjacent tiles to queue
            if(tile[0] > 0 && p.getTile(tile[0] - 1, tile[1]) == -1) pq.add(new int[]{tile[0] - 1, tile[1], n});
            if(tile[0] < 34 && p.getTile(tile[0] + 1, tile[1]) == -1) pq.add(new int[]{tile[0] + 1, tile[1], n});
            if(tile[1] > 0 && p.getTile(tile[0], tile[1] - 1) == -1) pq.add(new int[]{tile[0], tile[1] - 1, n});
            if(tile[1] < 34 && p.getTile(tile[0], tile[1] + 1) == -1) pq.add(new int[]{tile[0], tile[1] + 1, n});

            try { Thread.sleep(100); }
            catch (Exception e) { System.exit(1); }

            if (tile[0] == end[0] && tile[1] == end[1]) reached = true;
            p.paintDijkstraTile(tile[0], tile[1]);
            pq.remove(tile);
        } else pq.remove(tile);
        if (pq.peek() != null) recursiveDijkstra(pq.peek());
    }

    /** Find shortest path through filled array */
    private void shortestPath(int[] tile) {
        if (tile[0] != start[0] || tile[1] != start[1]) {
            p.paintTile(tile[0], tile[1], new Color(255, 255, 255, 150));

            int min = p.getTile(tile[0], tile[1]);
            int[] mintile = new int[2];
            if(tile[0] > 1 && p.getTile(tile[0] - 1, tile[1]) < min && p.getTile(tile[0] - 1, tile[1]) >= 0) {
                min = p.getTile(tile[0] - 1, tile[1]);
                mintile[0] = -1;
            }
            if(tile[0] < 34 && p.getTile(tile[0] + 1, tile[1]) < min && p.getTile(tile[0] + 1, tile[1]) >= 0) {
                min = p.getTile(tile[0] + 1, tile[1]);
                mintile[0] = 1;
            }
            if(tile[1] > 0 && p.getTile(tile[0], tile[1] - 1) < min && p.getTile(tile[0], tile[1] - 1) >= 0) {
                min = p.getTile(tile[0], tile[1] - 1);
                mintile[0] = 0;
                mintile[1] = -1;
            }
            if(tile[1] < 34 && p.getTile(tile[0], tile[1] + 1) < min && p.getTile(tile[0], tile[1] + 1) >= 0)
            {
                min = p.getTile(tile[0], tile[1] + 1);
                mintile[0] = 0;
                mintile[1] = 1;
            }

            shortestPath(new int[]{tile[0] + mintile[0], tile[1] + mintile[1]});
        }
    }

    class dijkstraComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] x, int[] y) {
            if (x[2] > y[2]) return 1;
            else if (x[2] < y[2]) return -1;
            else return 0;
        }
    }

    class AStarComparator implements Comparator<int[]> {
        @Override
        public int compare(int[] x, int[] y) {
            double xDist = Math.hypot(x[0] - end[0], x[1] - end[1]);
            double yDist = Math.hypot(y[0] - end[0], y[1] - end[1]);
            if (xDist > yDist) return 1;
            else if (xDist < yDist) return -1;
            else if (x[2] > y[2]) return 1;
            else if (x[2] < y[2]) return -1;
            else return 0;
        }
    }
}


