package ai.competition.NIlSiBot;


import ai.abstraction.pathfinding.GreedyPathFinding;
import ai.core.AI;
import ai.abstraction.AbstractionLayerAI;
import ai.abstraction.pathfinding.PathFinding;
import ai.core.ParameterSpecification;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import rts.GameState;
import rts.PhysicalGameState;
import rts.Player;
import rts.PlayerAction;
import rts.units.Unit;
import rts.units.UnitTypeTable;
import rts.units.UnitType;


/**
 * @author Nils Treuheit
 * Creted on my own aside from the adapted copy of the currently available 
 * LightRush and LightDefense code in the official MicroRTS Repository 
 * and with inspirations by LightRush, Mayari and WorkerRushPlusPlus.
 * Behavior largely different from the above as long as no ranged units or bugs 
 * are encountered, otherwise a fallback to a selective rush behavior or 
 * LightDefense derivative takes place.
 * This AI is a fixed strategy/rule-based AI.
 */
public class NIlSiBot extends AbstractionLayerAI{
    UnitTypeTable gameUTT = null;
    HashMap<Long,Unit> Harvesters = new HashMap<>(2);
    HashMap<Long,Unit> WorkersNextNearestResrc = new HashMap<>(3);
    HashMap<Long,int[]> WorkersWanderPosi = new HashMap<>(3);
    List<UnitType> agentTypes = new ArrayList<>(4);
    UnitType BASE_TYPE = null;
    UnitType LIGHT_TYPE = null;
    UnitType HEAVY_TYPE = null;
    UnitType WORKER_TYPE = null;
    UnitType RANGED_TYPE = null;
    UnitType BARRACKS_TYPE = null;
    UnitType RESOURCE_TYPE = null;
    
    //Own Memory
    HashMap<UnitType,List<Unit>> ownMemory = new HashMap<>(7);
    double ownBP = 0.0;   
    //metric for meassuring Battle Power as Force Potential: 
    // norm_range*time_freq*avg_dmg*norm_hp = n_q*d_t*avg_dmg*n_hp
    
    //Enemy Memory
    HashMap<UnitType,List<Unit>> enemyMemory = new HashMap<>(7);
    double enemyBP = 0.0;
    
    final void init()
    {
        for (UnitType type:this.gameUTT.getUnitTypes())
        {
           this.enemyMemory.put(type, new ArrayList<>());
           this.ownMemory.put(type, new ArrayList<>());
           if(type.canMove) agentTypes.add(type);
        }
        this.BASE_TYPE = this.gameUTT.getUnitType("Base");
        this.BARRACKS_TYPE = this.gameUTT.getUnitType("Barracks");
        this.WORKER_TYPE = this.gameUTT.getUnitType("Worker");
        this.RESOURCE_TYPE = this.gameUTT.getUnitType("Resource");
        this.LIGHT_TYPE = this.gameUTT.getUnitType("Light");
        this.RANGED_TYPE = this.gameUTT.getUnitType("Ranged");
        this.HEAVY_TYPE = this.gameUTT.getUnitType("Heavy");
    }
    
    //constructors
    public NIlSiBot(UnitTypeTable utt, PathFinding pf) {
        super(pf);
        if(utt != null) 
        {
            this.gameUTT = utt;
            init();
        }
    }
    public NIlSiBot(UnitTypeTable utt) {this(utt,new GreedyPathFinding());}
    public NIlSiBot() { this(null,new GreedyPathFinding());}
    
    // resets with specified pathfinding object
    public final void reset(PathFinding pf)
    {  
        super.reset();
        this.setPathFinding(pf);
        for (UnitType type:this.gameUTT.getUnitTypes())
        {
           this.enemyMemory.replace(type, new ArrayList<>());
           this.ownMemory.replace(type, new ArrayList<>());
        }
        this.Harvesters = new HashMap<>(2);
        this.WorkersNextNearestResrc = new HashMap<>(3);
        this.WorkersWanderPosi = new HashMap<>(3);
    }
    @Override
    public final void reset()
    { this.reset(new GreedyPathFinding()); }
    
    // create AI clone
    @Override
    public AI clone(){ return new NIlSiBot(this.gameUTT, this.pf); }
    
    // return AI Parameters
    @Override
    public List<ParameterSpecification> getParameters()
    {
        List<ParameterSpecification> parameters = new ArrayList<>();
        parameters.add(new ParameterSpecification("PathFinding", 
                       PathFinding.class, new GreedyPathFinding()));
        return parameters;
    }
    
    // determine if opponents can be overpowered
    double battleSuccess(){ return this.ownBP-(this.enemyBP*1.25); }
    
    // create necessary memory for the aaction determination
    int populate_memory(GameState gs, int pID)
    {
        for (UnitType type:this.gameUTT.getUnitTypes())
        {
           this.enemyMemory.replace(type, new ArrayList<>());
           this.ownMemory.replace(type, new ArrayList<>());
        }
        int count = 0;  // count total ammount of workers 
        this.ownBP = 0.0;
        this.enemyBP = 0.0;
        List<Unit> uig = gs.getUnits();
        for (Unit unit: uig)
        {
            int owner = unit.getPlayer();
            UnitType type = unit.getType();
            if (owner == pID)
            {
                if(type==this.WORKER_TYPE) ++count;
                // gather inactive agents and all base and barrack stations
                if(gs.getActionAssignment(unit)==null || 
                   type == this.BASE_TYPE || type == this.BARRACKS_TYPE)
                    this.ownMemory.get(type).add(unit);
                if(type.canMove)  // Moving Agents (incl. Workers)
                    this.ownBP += ((double)unit.getMoveTime()/
                            unit.getAttackTime())*
                        ((double)unit.getAttackRange()/
                            this.gameUTT.getMaxAttackRange())* 
                        ((unit.getMaxDamage()+unit.getMinDamage())*0.5)*
                        ((double)(unit.getHitPoints()/unit.getMaxHitPoints()));
                else if(type.isStockpile)  // Base
                    this.ownBP += 0.33*((double)unit.getHitPoints()/
                            unit.getMaxHitPoints());
                else this.ownBP += 0.5*((double)unit.getHitPoints()/
                        unit.getMaxHitPoints());   // Barracks
            }
            else //same but for enemies
            {
                this.enemyMemory.get(type).add(unit);
                if(type.canMove)  // Moving Agents (incl. Workers)
                    this.enemyBP += ((double)unit.getMoveTime()/
                            unit.getAttackTime())*
                        ((double)unit.getAttackRange()/
                            this.gameUTT.getMaxAttackRange())* 
                        ((unit.getMaxDamage()+unit.getMinDamage())*0.5)*
                        ((double)(unit.getHitPoints()/unit.getMaxHitPoints()));
                else if(type.isStockpile)  // Base
                    this.enemyBP += 0.33*((double)unit.getHitPoints()/
                            unit.getMaxHitPoints());
                else this.enemyBP += 0.5*((double)unit.getHitPoints()/
                        unit.getMaxHitPoints());  // Barracks
            }  
        }
        return count;
    }
    
    // distance calculation function
    int getManhattenDist(Unit u1, Unit u2)
    {
        return Math.abs(u2.getX() - u1.getX()) + 
                Math.abs(u2.getY() - u1.getY());
    }
    
    // Strongly Based on LightDefense Behaivor (https://github.com/Farama-Foundation/MicroRTS/blob/master/src/ai/abstraction/LightDefense.java) 
    // as my Fallback Behavior in case:
    // - encounter LightRush on wrong side of the field [bug with harvester]
    public PlayerAction defense(int player, GameState gs, UnitType type) 
    {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Player p = gs.getPlayer(player);

        // behavior of bases:
        int[] pos = new int[2];
        for (Unit u : pgs.getUnits()) 
            if (u.getType() == this.BASE_TYPE
                && u.getPlayer() == player)
            {
                pos[0] = u.getX(); pos[1]=u.getY();
                if (gs.getActionAssignment(u) == null) 
                {
                    int nworkers = 0;
                    for (Unit u2 : pgs.getUnits()) 
                        if (u2.getType() == this.WORKER_TYPE
                            && u2.getPlayer() == p.getID()) 
                            nworkers++;
                    if (nworkers < 1 && p.getResources() >= this.WORKER_TYPE.cost) 
                        train(u, this.WORKER_TYPE);
                }
            }

        // behavior of barracks:
        for (Unit u : pgs.getUnits())
            if (u.getType() == this.BARRACKS_TYPE
                && u.getPlayer() == player
                && gs.getActionAssignment(u) == null 
                && p.getResources() >= type.cost)
                    train(u, type);
            
        // behavior of melee units:
        for (Unit u : pgs.getUnits()) 
            if (u.getType().canAttack && !u.getType().canHarvest
                && u.getPlayer() == player
                && gs.getActionAssignment(u) == null) 
            {
                Unit closestEnemy = null;
                int closestDistance = 0;
                int mybase = 0;
                for (Unit u2 : pgs.getUnits()) 
                    if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID()) 
                    {
                       int d = this.getManhattenDist(u2, u);
                       if (closestEnemy == null || d < closestDistance) {
                           closestEnemy = u2;
                           closestDistance = d;
                       }
                    }
                    else if(u2.getPlayer()==p.getID() && 
                            u2.getType() == this.BASE_TYPE)
                        mybase = this.getManhattenDist(u2, u);
                if (closestEnemy!=null && (closestDistance < pgs.getHeight()/2 
                    || mybase < pgs.getHeight()/2)) 
                    attack(u,closestEnemy);
                else move(u, pos[0]+2,pos[1]+2); 
              // changed this to aggregate troops closer to own base 
            }
        
        // behavior of workers:
        List<Unit> workers = new LinkedList<>();
        for (Unit u : pgs.getUnits())
            if (u.getType().canHarvest
                    && u.getPlayer() == player)
                workers.add(u);
        
        int nbases = 0;
        int nbarracks = 0;

        int resourcesUsed = 0;
        List<Unit> freeWorkers = new LinkedList<>(workers);

        for (Unit u2 : pgs.getUnits())
            if (u2.getType() == this.BASE_TYPE
                && u2.getPlayer() == p.getID()) 
                nbases++;
            else if (u2.getType() == this.BARRACKS_TYPE
                && u2.getPlayer() == p.getID()) 
                nbarracks++;
            

        List<Integer> reservedPositions = new LinkedList<>();
        if ((nbases == 0 && !freeWorkers.isEmpty()) 
            && (p.getResources() >= this.BASE_TYPE.cost + resourcesUsed)) 
        {
            // build a base:
            Unit u = freeWorkers.remove(0);
            buildIfNotAlreadyBuilding(u,this.BASE_TYPE,
                u.getX(),u.getY(),reservedPositions,p,pgs);
            resourcesUsed += this.BASE_TYPE.cost;
        }

        if ((nbarracks == 0) && (p.getResources() >= this.BARRACKS_TYPE.cost +
            resourcesUsed && !freeWorkers.isEmpty())) 
        {
            // build a barracks:
            Unit u = freeWorkers.remove(0);
            buildIfNotAlreadyBuilding(u,this.BARRACKS_TYPE,
                u.getX(),u.getY(),reservedPositions,p,pgs);
        }


        // harvest with all the free workers:
        for (Unit u : freeWorkers) 
        {
            Unit closestBase = null;
            Unit closestResource = null;
            int closestDistance = 0;
            for (Unit u2 : pgs.getUnits())
                if (u2.getType().isResource) 
                {
                    int d = this.getManhattenDist(u2, u);
                    if (closestResource == null || d < closestDistance) 
                    {
                        closestResource = u2;
                        closestDistance = d;
                    }
                }
            
            closestDistance = 0;
            for (Unit u2 : pgs.getUnits()) 
                if (u2.getType().isStockpile && u2.getPlayer()==p.getID()) 
                {
                    int d = this.getManhattenDist(u2, u);
                    if (closestBase == null || d < closestDistance) 
                    {
                        closestBase = u2;
                        closestDistance = d;
                    }
                }
            
            if (closestResource != null && closestBase != null)
                harvest(u, closestResource, closestBase);  
        }
        return translateActions(player, gs);
    }
    
    // LightRush Behaivor (https://github.com/Farama-Foundation/MicroRTS/blob/master/src/ai/abstraction/LightRush.java) 
    // copied and generalized for all kinds of troops. 
    // This is used as my Fallback Behavior in case:
    // - fight against Ranged Units (continous fallback plan)
    // - all of my bases get destroyed (until base is rebuild)
    // - one of my harvester dies (one time step only)
    PlayerAction rush(int player, GameState gs, UnitType type) throws Exception
    {
        PhysicalGameState pgs = gs.getPhysicalGameState();
        Player p = gs.getPlayer(player);

        // behavior of bases:
        for (Unit u : pgs.getUnits()) {
            if (u.getType() == this.BASE_TYPE
                    && u.getPlayer() == player
                    && gs.getActionAssignment(u) == null) 
            {
                int nworkers = 0;
                for (Unit u2 : pgs.getUnits()) 
                    if (u2.getType() == this.WORKER_TYPE
                        && u2.getPlayer() == p.getID())
                        ++nworkers;
                if (nworkers < 1 && p.getResources() >= this.WORKER_TYPE.cost)
                    train(u, this.WORKER_TYPE);
            }
        }

        // behavior of barracks:
        for (Unit u : pgs.getUnits()) {
            if (u.getType() == this.BARRACKS_TYPE
                    && u.getPlayer() == player
                    && gs.getActionAssignment(u) == null) {
                if (p.getResources() >= type.cost)
                    train(u, type);
            }
        }

        // behavior of melee units:
        for (Unit u : pgs.getUnits()) {
            if (u.getType().canAttack && !u.getType().canHarvest
                    && u.getPlayer() == player
                    && gs.getActionAssignment(u) == null) 
            {
                Unit closestEnemy = null;
                int closestDistance = 0;
                for (Unit u2 : pgs.getUnits()) 
                {
                  if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID()) 
                  {
                    int d = this.getManhattenDist(u2,u);
                    if (closestEnemy == null || d < closestDistance) 
                    {
                        closestEnemy = u2;
                        closestDistance = d;
                    }
                  }
                }
                if (closestEnemy != null) attack(u, closestEnemy);
            }
        }

        // behavior of workers:
        List<Unit> workers = new LinkedList<>();
        for (Unit u : pgs.getUnits()) {
            if (u.getType().canHarvest
                    && u.getPlayer() == player) {
                workers.add(u);
            }
        }
        int nbases = 0;
        int nbarracks = 0;

        int resourcesUsed = 0;
        List<Unit> freeWorkers = new LinkedList<>(workers);

        for (Unit u2 : pgs.getUnits()) {
            if (u2.getType() == this.BASE_TYPE
                    && u2.getPlayer() == p.getID()) {
                nbases++;
            }
            if (u2.getType() == this.BARRACKS_TYPE
                    && u2.getPlayer() == p.getID()) {
                nbarracks++;
            }
        }

        List<Integer> reservedPositions = new LinkedList<>();
        if (nbases == 0 && !freeWorkers.isEmpty()) {
            // build a base:
            if (p.getResources() >= this.BASE_TYPE.cost + resourcesUsed) 
            {
                Unit u = freeWorkers.remove(0);
                buildIfNotAlreadyBuilding(u,this.BASE_TYPE,
                    u.getX(),u.getY(),reservedPositions,p,pgs);
                resourcesUsed += this.BASE_TYPE.cost;
            }
        }

        if (nbarracks == 0) {
            // build a barracks:
            if (p.getResources() >= this.BARRACKS_TYPE.cost + resourcesUsed && 
                !freeWorkers.isEmpty()) 
            {
                Unit u = freeWorkers.remove(0);
                buildIfNotAlreadyBuilding(u,this.BARRACKS_TYPE,
                    u.getX(),u.getY(),reservedPositions,p,pgs);
                //resourcesUsed += this.BARRACKS_TYPE.cost;
            }
        }


        // harvest with all the free workers:
        List<Unit> stillFreeWorkers = new LinkedList<>();
        for (Unit u : freeWorkers) 
        {
            Unit closestBase = null;
            Unit closestResource = null;
            int closestDistance = 0;
            for (Unit u2 : pgs.getUnits()) 
                if (u2.getType().isResource) 
                {
                    int d = this.getManhattenDist(u2,u);
                    if (closestResource == null || d < closestDistance) 
                    {
                        closestResource = u2;
                        closestDistance = d;
                    }
                }
            
            closestDistance = 0;
            for (Unit u2 : pgs.getUnits()) 
                if (u2.getType().isStockpile && u2.getPlayer()==p.getID()) 
                {
                    int d = this.getManhattenDist(u2,u);
                    if (closestBase == null || d < closestDistance) 
                    {
                        closestBase = u2;
                        closestDistance = d;
                    }
                }
                
            boolean workerStillFree = true;
            if (u.getResources() > 0) 
            {
                if (closestBase!=null) 
                {
                    harvest(u, null, closestBase);
                    workerStillFree = false;
                }
            } 
            else 
            {            
                harvest(u, closestResource, closestBase);
                workerStillFree = false;
            }
            
            if (workerStillFree) stillFreeWorkers.add(u);            
        }
        
        for(Unit u:stillFreeWorkers)  //act like melees
        {
            Unit closestEnemy = null;
            int closestDistance = 0;
            for (Unit u2 : pgs.getUnits()) 
            {
                if (u2.getPlayer() >= 0 && u2.getPlayer() != p.getID()) 
                {
                   int d = this.getManhattenDist(u2,u);
                   if (closestEnemy == null || d < closestDistance) 
                   {
                      closestEnemy = u2;
                      closestDistance = d;
                   }
                }
            }
            if (closestEnemy != null) attack(u, closestEnemy);
        }


        return translateActions(player, gs);
    }
    
    // generate workers if base not busy
    List<Unit> produceWorkers(int resources, List<Unit> bases, GameState gs)
    {
        List<Unit> unusedBases = new ArrayList();
        unusedBases.addAll(bases);
        for(Unit base: bases)
            if(gs.getActionAssignment(base)!=null)
                unusedBases.remove(base);
            else
            {
              if (resources>=this.WORKER_TYPE.cost)
              {
                train(base, this.WORKER_TYPE);
                resources -= this.WORKER_TYPE.cost;
                unusedBases.remove(base);
              }
              else break;
            }
        return unusedBases;
    }
    
    // wander to the next free position outside the "sight" radius
    void wander_exploration(Unit agent, boolean[][] free_fields, 
                            int size_x, int size_y)
    {
      // if already wandering continue journey 
      int[] posi = this.WorkersWanderPosi.getOrDefault(agent.getID(), 
         null);
      if(posi != null) 
      {
          move(agent,posi[0],posi[1]);
          return;
      }
      
      // find new wander destination
      int oldY = agent.getY();
      int oldX = agent.getX();
      posi = new int[2];
      boolean found = false;
      for(int y=15; y>0; --y)
      {
        boolean pos = (oldY+y)<size_y;
        boolean neg = (oldY-y)>0;   
        for(int x=15; x>0; --x)
        {
          if(pos && (oldX+x)<size_x && 
             free_fields[oldX+x][oldY+y])
          {
               posi[0] = oldX+x;
               posi[1] = oldY+y;
               found = true;
          }
          else if(pos && (oldX-x)>0 && 
                  free_fields[oldX-x][oldY+y])
          {
               posi[0] = oldX-x;
               posi[1] = oldY+y;
               found = true;
          }
          else if(neg && (oldX+x)<size_x && 
                  free_fields[oldX+x][oldY-y])
          {
               posi[0] = oldX+x;
               posi[1] = oldY-y;
               found = true;
          }
          else if(neg && (oldX-x)>0 && 
                  free_fields[oldX-x][oldY-y])
          {
               posi[0] = oldX-x;
               posi[1] = oldY-y;
               found = true;
          }
          if(found) break;
        }
        if(found) break;
      }
      move(agent,posi[0],posi[1]);
      this.WorkersWanderPosi.putIfAbsent(agent.getID(), posi);
    }

    // travel to the next nearest available not yet harvested resource
    boolean travelToNextNearestResrc(Unit agent, Unit closestResrc, boolean harvester)
    {
      // if target found and reached discontinue journey
      Unit NearResource = 
      this.WorkersNextNearestResrc.getOrDefault(
          agent.getID(), null);
      if(NearResource!=null && agent.getX()==NearResource.getX() && 
         agent.getY()==NearResource.getY())
      {
         this.WorkersNextNearestResrc.remove(agent.getID());
         return false;
      }
      
      // if no target so far find destination 
      if(NearResource == null && (closestResrc != null || harvester))
      {
         int nearRDist = 0;
         for(Unit resrc: this.enemyMemory.get(this.RESOURCE_TYPE))
         {
           int d = this.getManhattenDist(agent,resrc);
           if (resrc!=closestResrc && d < 32 && (harvester ||
               this.getManhattenDist(resrc, closestResrc) > 3) &&
               (NearResource==null || d<nearRDist))
           {
              NearResource = resrc;
              nearRDist = d;
           }
         }
      }
      
      // if target is determined start journey
      if(NearResource!=null)
      {
          this.WorkersNextNearestResrc.replace(
                 agent.getID(), NearResource);
          move(agent,
               NearResource.getX(),NearResource.getY());
          //System.out.println(agent+" moves to next resource");
          return true;
      }
      return false;
    }

    
    /* Own AI Implementation
     * -> my behavior is inspired by WorkerRushPlusPlus, LightRush, LightDefense
     *    and Mayari 
     * -> redefined heavily after intial tournaments with these 4 as well as 
     *    HeavyRush, RangedRush and RandomBiasedAI
     * -> finetuned by trial error tests  
     */
    @Override
    public PlayerAction getAction(int playerID, GameState gs) throws Exception 
    {
        // variable and memory initialization
        if(this.gameUTT == null) 
        {
            this.gameUTT=gs.getUnitTypeTable();
            init();
        }
        PhysicalGameState currentState = gs.getPhysicalGameState();
        Player p = gs.getPlayer(playerID);
        int workerCount = populate_memory(gs, playerID);
        
        // fall back to light rush strategy for ranged enemies
        if(!this.enemyMemory.get(this.RANGED_TYPE).isEmpty())
            return rush(playerID,gs,this.LIGHT_TYPE);
        
        // get free fields
        boolean[][] free_fields = currentState.getAllFree();
        int size_y = currentState.getHeight();
        int size_x = currentState.getWidth();
        
        // big fields will trigger a behavior 
        // -> that starts with a somewhat light defense strategy and 
        // -> eventually converts to a light rush strategy
        //    to win the game
        // -> to avoid harvester bug on one side of the field 
        //    use deviating strategy based on enemy units
        boolean big = (size_y*size_x)>256; 
        if(big && this.Harvesters.size()==1)
        {
           Unit w = gs.getUnit(this.Harvesters.keySet().iterator().next());
           if(w==null || (w.getX()>16 && w.getY()>16))
               if(!this.enemyMemory.get(this.HEAVY_TYPE).isEmpty())
                  return rush(playerID,gs,this.RANGED_TYPE);
               else if(!this.enemyMemory.get(this.WORKER_TYPE).isEmpty())
                  return defense(playerID,gs,this.LIGHT_TYPE);
        }
        
        // small fields are usally dealt with 
        // -> in a worker rush approach but even then
        // -> eventually convert to a light rush strategy 
        //    if the game takes long enough
        int resources = p.getResources();
        
        // base unit behavior
        List<Unit> unusedBases = null;
        if(workerCount<(big?1:5))
            unusedBases = produceWorkers(resources,
                          this.ownMemory.get(this.BASE_TYPE), gs);
            
        // baracks behaviour
        for(Unit barrack: this.ownMemory.get(this.BARRACKS_TYPE))
            if(gs.getActionAssignment(barrack)==null)
            {
              if(resources>=this.LIGHT_TYPE.cost)
              {
                train(barrack, this.LIGHT_TYPE);
                resources -= this.LIGHT_TYPE.cost;
              }
              else break;
            }
        
        // temporary worker variables
        List<Integer> BaseRP = new LinkedList<>();
        List<Integer> BarracksRP = new LinkedList<>();
        
        // agent type (moving units [worker and melees])
        for(UnitType type: this.agentTypes)
        {
            for(Unit agent: this.ownMemory.get(type))
            {
                // look around inspect range of "sight" 
                int range = agent.getAttackRange();
                Collection<Unit> closeBy = currentState.getUnitsAround(
                        agent.getX(), agent.getY(), (big?3:7));
                
                /* the following block was created 
                   looking at WorkerRushPlusPlus */
                // finding closest resource, enemy and calculate
                // the players local area battle power 
                Unit closestEnemy = null;
                Unit closestResrc = null;
                int closestEDist = 0;
                int closestRDist = 0;
                double localEnemyBP = 0;
                double localOwnBP = 0;
                for(Unit unit : closeBy)
                {   
                    int upid = unit.getPlayer();
                    int d = this.getManhattenDist(agent,unit);
                    if(upid>=0)
                    {
                      if(upid!=playerID)
                      {
                        if(unit.getType().canAttack)
                            localEnemyBP += ((unit.getMaxDamage() +
                                unit.getMinDamage())*0.5)*unit.getHitPoints();
                        if (d <= range &&
                            (closestEnemy==null || d<closestEDist)) 
                        {
                            closestEnemy = unit;
                            closestEDist = d;
                        }
                      }
                      else if(unit.getType().canAttack) 
                        localOwnBP += ((unit.getMaxDamage() +
                            unit.getMinDamage())*0.5)*unit.getHitPoints();
                    }
                    else if(unit.getType().isResource && 
                            (closestResrc==null || d<closestRDist))
                    {
                        closestResrc = unit;
                        closestRDist = d;
                    }    
                } 
                
                // for moving agent find overall closest base
                int closestBDist = 0;
                Unit closestBase = null;
                for(Unit base: this.ownMemory.get(this.BASE_TYPE))
                {
                    int d = this.getManhattenDist(agent,base);
                    if (closestBase==null || d<closestBDist)
                    {
                        closestBase = base;
                        closestBDist = d;
                    } 
                }
                
                
                // attack or flee closestEnemy  
                if(closestEnemy!=null)
                {
                  //re-group with other units by approaching base
                  if(localOwnBP < localEnemyBP && 
                     closestEnemy.getType().attackRange <= closestEDist)
                  {
                    if(closestBase == null) 
                        return rush(playerID,gs,this.LIGHT_TYPE);
                    move(agent,closestBase.getX(),closestBase.getY());
                  }
                  else //attack if can not avoid or strong enough
                    attack(agent,closestEnemy);
                }
                else // if no enimies close
                {
                  if(type!=this.WORKER_TYPE) // attack unit behaviour
                  {
                   // find nearest enemy base
                   if(closestBase == null) 
                       return rush(playerID,gs,this.LIGHT_TYPE);
                   Unit NN_EBase = null;
                   int nneb_d = 0;
                   for(Unit base: this.enemyMemory.get(this.BASE_TYPE))
                   {
                     int d = this.getManhattenDist(closestBase,base);
                     if (NN_EBase==null || d<nneb_d)
                     {
                        NN_EBase = base;
                        nneb_d = d;
                     }
                   }
                   if(NN_EBase != null)
                   {
                     // if battle likely succesfull attack base
                     if(this.battleSuccess()>0)
                         move(agent,NN_EBase.getX(),NN_EBase.getY());
                     else // troop building phase
                     {  
                        // in small fields idle around the center of the map
                        int nnebY = (int)(NN_EBase.getY()*0.5);
                        int nnebX = (int)(NN_EBase.getX()*0.5);
                        if(big)
                        {
                          // in big fields fence off base
                          nnebY = closestBase.getY()+2;
                          nnebX = closestBase.getX();
                        }
                        if (free_fields[nnebX][nnebY]) 
                            move(agent,nnebX,nnebY);
                        else for(int y=1; y<15; ++y)
                        {    
                          boolean pos = (nnebY+y)<size_y;
                          boolean neg = (nnebY-y)>0;        
                          for(int x=1; x<15; ++x)
                          {
                            if(pos && nnebX+x<size_x && 
                               free_fields[nnebX+x][nnebY+y])
                                move(agent,nnebX+x,nnebY+y);
                            else if(pos && nnebX-x>0 && 
                                    free_fields[nnebX-x][nnebY+y])
                                move(agent,nnebX-x,nnebY+y);
                            else if(neg && nnebX+x<size_x && 
                                    free_fields[nnebX+x][nnebY-y])
                                move(agent,nnebX+x,nnebY-y);
                            else if(neg && nnebX-x>0 && 
                                    free_fields[nnebX-x][nnebY-y])
                                move(agent,nnebX-x,nnebY-y);
                           }
                        }
                     }
                   }
                   else //attack remaining units start with closest
                   {   
                       Unit NNEUnit = null;
                       int nneuD = 0;
                       for(UnitType atype: this.agentTypes)
                        for(Unit enemy: this.enemyMemory.get(atype))
                        {
                         int d = this.getManhattenDist(agent,enemy);
                         if (NNEUnit==null || d<nneuD)
                         {
                            NNEUnit = enemy;
                            nneuD = d;
                         }
                        }
                       if(NNEUnit!=null) 
                        move(agent,NNEUnit.getX(),NNEUnit.getY());
                       else 
                        wander_exploration(agent, free_fields, size_x, size_y);
                   }
                  }
                  else // worker behaviour
                  {
                    // initialise worker if not doone
                    this.WorkersNextNearestResrc.putIfAbsent(
                            agent.getID(), null);
                    this.WorkersWanderPosi.putIfAbsent(
                            agent.getID(), null);
                    if(this.Harvesters.isEmpty() && 
                       closestRDist<7 && closestBDist < 7)
                        this.Harvesters.put(agent.getID(),closestResrc);
                    
                    // check for other harvesting workers
                    boolean harvester = false; 
                    int closestHDist = 10000000;
                    Unit closestHResrc = null;
                    for(long id: this.Harvesters.keySet())
                      if(agent.getID() == id)
                      { 
                        harvester = true;
                        closestHResrc = this.Harvesters.get(id);
                        break;
                      }
                      else 
                      { 
                        Unit hu = gs.getUnit(id);
                        if(hu==null)
                        {
                            // dead harvester fallback
                            this.Harvesters.remove(id);
                            return rush(playerID,gs,this.LIGHT_TYPE);
                        }
                        int dist = this.getManhattenDist(agent, 
                                   gs.getUnit(id));
                        if(dist<closestHDist)
                        {
                          closestHDist = dist;
                          closestHResrc = this.Harvesters.get(id);
                        }
                      }
                    
                    // build barracks
                    boolean building = false;
                    if(resources > this.BARRACKS_TYPE.cost && harvester &&
                       ((this.enemyMemory.get(this.BARRACKS_TYPE).size() >
                       this.ownMemory.get(this.BARRACKS_TYPE).size()+
                       BarracksRP.size()) || ( this.ownMemory.get(
                       this.BARRACKS_TYPE).isEmpty() &&
                       this.ownMemory.get(this.WORKER_TYPE).size()>=(big?1:5))))
                         building = buildIfNotAlreadyBuilding(
                                agent, this.BARRACKS_TYPE,
                                agent.getX(), agent.getY(), 
                                BarracksRP, p, currentState);
                    
                    // build new base
                    if(resources >= this.BASE_TYPE.cost && !building && 
                       !harvester && closestHDist>3 && closestResrc!=null && 
                       closestRDist<=2 && BaseRP.isEmpty()) 
                         building = buildIfNotAlreadyBuilding(
                                agent, this.BARRACKS_TYPE,
                                agent.getX(), agent.getY(), 
                                BaseRP, p, currentState);
                    
                    if(!building)
                    {
                      // create new harvesters
                      if(!harvester && closestRDist<=2 && closestHDist>8 && 
                         closestBase!=null && closestResrc!=null)
                      {
                        this.Harvesters.put(agent.getID(),closestResrc);
                        this.WorkersWanderPosi.replace(agent.getID(),
                                null);
                        this.WorkersNextNearestResrc.replace(agent.getID(),
                                null);
                        harvester = true;
                      }

                      // if worker is harvester then harvest close by Resource
                      if(harvester)
                      { 
                        if(closestResrc != null)
                        {
                            if(closestBase == null) // no base fallback
                              return rush(playerID,gs,this.LIGHT_TYPE);
                            harvest(agent, closestResrc, closestBase);
                        }
                        else  //nothing to harvest - find next resource
                        {
                          if(!travelToNextNearestResrc(agent, closestResrc, harvester))
                             wander_exploration(agent, free_fields, 
                             size_x, size_y);
                          this.Harvesters.remove(agent.getID());
                        }
                      }
                      else  //move to next best harvest location
                      {
                        if(!travelToNextNearestResrc(agent, 
                            closestHResrc, harvester))
                        {
                         int[] posi = this.WorkersWanderPosi.getOrDefault(
                                 agent.getID(), null);
                         if( posi != null && agent.getX()==posi[0] && 
                             agent.getX()==posi[1])
                             this.WorkersWanderPosi.replace(agent.getID(), 
                                    null);
                         wander_exploration(agent, free_fields, size_x, size_y);
                        }
                      }
                    }
                  }
                }
            }    
        }
        
        
        return super.translateActions(playerID, gs);
    }
}


