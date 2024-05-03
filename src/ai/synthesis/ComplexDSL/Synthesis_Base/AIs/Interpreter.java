package ai.synthesis.ComplexDSL.Synthesis_Base.AIs;

import java.util.List;
import java.util.Random;

import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Node;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.AStarPathFinding;
import ai.core.AI;
import ai.core.ParameterSpecification;
import java.util.Objects;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitType;
import rts.units.UnitTypeTable;

public class Interpreter extends AbstractionLayerAI {

    public UnitTypeTable utt;
    UnitType workerType;
    UnitType baseType;
    UnitType barracksType;
    UnitType lightType;
    Node node;
    public int resource;
    private String name;

    public Interpreter(UnitTypeTable a_utt) {
        this(a_utt, new AStarPathFinding());
        // TODO Auto-generated constructor stub
    }

    public Interpreter(UnitTypeTable a_utt, Node n) {
        this(a_utt, new AStarPathFinding());
        // TODO Auto-generated constructor stub
        this.node = n;
    }

    public Interpreter(UnitTypeTable a_utt, AStarPathFinding a_pf) {
        super(a_pf);
        reset(a_utt);
    }
    
    public Interpreter(UnitTypeTable a_utt, Node n, String name) {
        this(a_utt, new AStarPathFinding());
        // TODO Auto-generated constructor stub
        this.node = n;
        this.name = name;
    }

    @Override
    public void reset(UnitTypeTable a_utt) {
        utt = a_utt;
        workerType = utt.getUnitType("Worker");
        baseType = utt.getUnitType("Base");
        barracksType = utt.getUnitType("Barracks");
        lightType = utt.getUnitType("Light");

    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    @Override
    public PlayerAction getAction(int player, GameState gs) throws Exception {
        // TODO Auto-generated method stub
        this.resource = gs.getPlayer(player).getResources();
        this.actions.clear();

        node.interpret(gs, player, null, this);
        return translateActions(player, gs);
    }

    /*
	 Strongest: Select as target the opponent unit able to cause the biggest damage in the actual game. It means that the policy considers just the units that exist in the game.
	 Weakest: Select as targeting the opponent unit able to cause less damage in the actual game.
	 Closest: Select as targeting the opponent unit closest.
	 Farthest: Select as targeting the opponent unit farthest.
	 Less Healthy: Select as targeting the opponent unit with the smaller HP.
	 Most Healthy: Select as targeting the opponent unit with the higher HP.
	 Random:
     */

    public Unit getUnitStrongest(GameState gs, Player p, Unit u) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Unit closestStrongest = null;
        int closestDistance = 0;
        int Strongest = -1;

        for (Unit u2 : pgs.getUnits()) {
            if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID() && u.getID() != u2.getID()) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestStrongest == null || Strongest < u2.getMaxDamage()) {
                    Strongest = u2.getMaxDamage();
                    closestStrongest = u2;
                    closestDistance = d;
                } else if (Strongest == u2.getMaxDamage()) {

                    if (closestStrongest == null || d < closestDistance) {
                        closestStrongest = u2;
                        closestDistance = d;
                        Strongest = u2.getMaxDamage();
                    }
                }
            }
        }
        return closestStrongest;
    }

    public Unit getUnitWeakest(GameState gs, Player p, Unit u) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Unit closestWeakest = null;
        int closestDistance = 0;
        int Weakest = 10000;

        for (Unit u2 : pgs.getUnits()) {
            if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID() && u.getID() != u2.getID()) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestWeakest == null || Weakest > u2.getMaxDamage()) {
                    Weakest = u2.getMaxDamage();
                    closestWeakest = u2;
                    closestDistance = d;
                } else if (Weakest == u2.getMaxDamage()) {

                    if (closestWeakest == null || d < closestDistance) {
                        closestWeakest = u2;
                        closestDistance = d;
                        Weakest = u2.getMaxDamage();
                    }
                }
            }
        }
        return closestWeakest;
    }

    public Unit getUnitClosest(GameState gs, Player p, Unit u) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Unit closestEnemy = null;
        int closestDistance = 0;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID() && u.getID() != u2.getID()) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestEnemy == null || d < closestDistance) {
                    closestEnemy = u2;
                    closestDistance = d;
                }
            }
        }
        return closestEnemy;
    }

    public Unit getUnitFarthest(GameState gs, Player p, Unit u) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Unit FarthestEnemy = null;
        int FarthestDistance = 1000000;
        for (Unit u2 : pgs.getUnits()) {
            if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID() && u.getID() != u2.getID()) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (FarthestEnemy == null || d > FarthestDistance) {
                    FarthestEnemy = u2;
                    FarthestDistance = d;
                }
            }
        }
        return FarthestEnemy;
    }

    public Unit getUnitLessHealthy(GameState gs, Player p, Unit u) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Unit closestHealthy = null;
        int closestDistance = 0;
        int Healthy = 10000;

        for (Unit u2 : pgs.getUnits()) {
            if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID() && u.getID() != u2.getID()) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestHealthy == null || Healthy > u2.getMaxHitPoints()) {
                    Healthy = u2.getMaxHitPoints();
                    closestHealthy = u2;
                    closestDistance = d;
                } else if (Healthy == u2.getMaxHitPoints()) {

                    if (closestHealthy == null || d < closestDistance) {
                        Healthy = u2.getMaxHitPoints();
                        closestHealthy = u2;
                        closestDistance = d;
                    }
                }
            }
        }
        return closestHealthy;
    }

    public Unit getUnitMostHealthy(GameState gs, Player p, Unit u) {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Unit closestHealthy = null;
        int closestDistance = 0;
        int Healthy = 0;

        for (Unit u2 : pgs.getUnits()) {
            if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID() && u.getID() != u2.getID()) {
                int d = Math.abs(u2.getX() - u.getX()) + Math.abs(u2.getY() - u.getY());
                if (closestHealthy == null || Healthy < u2.getMaxHitPoints()) {
                    Healthy = u2.getMaxHitPoints();
                    closestHealthy = u2;
                    closestDistance = d;
                } else if (Healthy == u2.getMaxHitPoints()) {

                    if (closestHealthy == null || d < closestDistance) {
                        Healthy = u2.getMaxHitPoints();
                        closestHealthy = u2;
                        closestDistance = d;
                    }
                }
            }
        }
        return closestHealthy;
    }

    public Unit getUnitRandom(GameState gs, Player p, Unit u) {
        Random gerador = new Random();
        int r = gerador.nextInt(6);
        if (r == 0) {
            return this.getUnitStrongest(gs, p, u);
        }
        if (r == 1) {
            return this.getUnitWeakest(gs, p, u);
        }
        if (r == 2) {
            return this.getUnitClosest(gs, p, u);
        }
        if (r == 3) {
            return this.getUnitFarthest(gs, p, u);
        }
        if (r == 4) {
            return this.getUnitLessHealthy(gs, p, u);
        }
        if (r == 5) {
            return this.getUnitMostHealthy(gs, p, u);
        }
        return null;
    }

    @Override
    public AI clone() {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public AI clone_for_Thread() {
        // TODO Auto-generated method stub
        return new Interpreter(utt, node, name);
    }

    @Override
    public List<ParameterSpecification> getParameters() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return name;
    }        

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.node);
        hash = 89 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Interpreter other = (Interpreter) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return Objects.equals(this.node, other.node);
    }
    
    

}
