/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.synthesis.dreamcoder.DbEvaluationDataset.Controllers;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Battles;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Controllers.exceptions.IllegalOrphanException;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Controllers.exceptions.NonexistentEntityException;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Strategies;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rubens
 */
public class StrategiesJpaController implements Serializable {

    public StrategiesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Strategies strategies) {
        if (strategies.getBattlesList() == null) {
            strategies.setBattlesList(new ArrayList<Battles>());
        }
        if (strategies.getBattlesList1() == null) {
            strategies.setBattlesList1(new ArrayList<Battles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Battles> attachedBattlesList = new ArrayList<Battles>();
            for (Battles battlesListBattlesToAttach : strategies.getBattlesList()) {
                battlesListBattlesToAttach = em.getReference(battlesListBattlesToAttach.getClass(), battlesListBattlesToAttach.getId());
                attachedBattlesList.add(battlesListBattlesToAttach);
            }
            strategies.setBattlesList(attachedBattlesList);
            List<Battles> attachedBattlesList1 = new ArrayList<Battles>();
            for (Battles battlesList1BattlesToAttach : strategies.getBattlesList1()) {
                battlesList1BattlesToAttach = em.getReference(battlesList1BattlesToAttach.getClass(), battlesList1BattlesToAttach.getId());
                attachedBattlesList1.add(battlesList1BattlesToAttach);
            }
            strategies.setBattlesList1(attachedBattlesList1);
            em.persist(strategies);
            for (Battles battlesListBattles : strategies.getBattlesList()) {
                Strategies oldPlayer1IdOfBattlesListBattles = battlesListBattles.getPlayer1Id();
                battlesListBattles.setPlayer1Id(strategies);
                battlesListBattles = em.merge(battlesListBattles);
                if (oldPlayer1IdOfBattlesListBattles != null) {
                    oldPlayer1IdOfBattlesListBattles.getBattlesList().remove(battlesListBattles);
                    oldPlayer1IdOfBattlesListBattles = em.merge(oldPlayer1IdOfBattlesListBattles);
                }
            }
            for (Battles battlesList1Battles : strategies.getBattlesList1()) {
                Strategies oldPlayer2IdOfBattlesList1Battles = battlesList1Battles.getPlayer2Id();
                battlesList1Battles.setPlayer2Id(strategies);
                battlesList1Battles = em.merge(battlesList1Battles);
                if (oldPlayer2IdOfBattlesList1Battles != null) {
                    oldPlayer2IdOfBattlesList1Battles.getBattlesList1().remove(battlesList1Battles);
                    oldPlayer2IdOfBattlesList1Battles = em.merge(oldPlayer2IdOfBattlesList1Battles);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Strategies strategies) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Strategies persistentStrategies = em.find(Strategies.class, strategies.getId());
            List<Battles> battlesListOld = persistentStrategies.getBattlesList();
            List<Battles> battlesListNew = strategies.getBattlesList();
            List<Battles> battlesList1Old = persistentStrategies.getBattlesList1();
            List<Battles> battlesList1New = strategies.getBattlesList1();
            List<String> illegalOrphanMessages = null;
            for (Battles battlesListOldBattles : battlesListOld) {
                if (!battlesListNew.contains(battlesListOldBattles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Battles " + battlesListOldBattles + " since its player1Id field is not nullable.");
                }
            }
            for (Battles battlesList1OldBattles : battlesList1Old) {
                if (!battlesList1New.contains(battlesList1OldBattles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Battles " + battlesList1OldBattles + " since its player2Id field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            List<Battles> attachedBattlesListNew = new ArrayList<Battles>();
            for (Battles battlesListNewBattlesToAttach : battlesListNew) {
                battlesListNewBattlesToAttach = em.getReference(battlesListNewBattlesToAttach.getClass(), battlesListNewBattlesToAttach.getId());
                attachedBattlesListNew.add(battlesListNewBattlesToAttach);
            }
            battlesListNew = attachedBattlesListNew;
            strategies.setBattlesList(battlesListNew);
            List<Battles> attachedBattlesList1New = new ArrayList<Battles>();
            for (Battles battlesList1NewBattlesToAttach : battlesList1New) {
                battlesList1NewBattlesToAttach = em.getReference(battlesList1NewBattlesToAttach.getClass(), battlesList1NewBattlesToAttach.getId());
                attachedBattlesList1New.add(battlesList1NewBattlesToAttach);
            }
            battlesList1New = attachedBattlesList1New;
            strategies.setBattlesList1(battlesList1New);
            strategies = em.merge(strategies);
            for (Battles battlesListNewBattles : battlesListNew) {
                if (!battlesListOld.contains(battlesListNewBattles)) {
                    Strategies oldPlayer1IdOfBattlesListNewBattles = battlesListNewBattles.getPlayer1Id();
                    battlesListNewBattles.setPlayer1Id(strategies);
                    battlesListNewBattles = em.merge(battlesListNewBattles);
                    if (oldPlayer1IdOfBattlesListNewBattles != null && !oldPlayer1IdOfBattlesListNewBattles.equals(strategies)) {
                        oldPlayer1IdOfBattlesListNewBattles.getBattlesList().remove(battlesListNewBattles);
                        oldPlayer1IdOfBattlesListNewBattles = em.merge(oldPlayer1IdOfBattlesListNewBattles);
                    }
                }
            }
            for (Battles battlesList1NewBattles : battlesList1New) {
                if (!battlesList1Old.contains(battlesList1NewBattles)) {
                    Strategies oldPlayer2IdOfBattlesList1NewBattles = battlesList1NewBattles.getPlayer2Id();
                    battlesList1NewBattles.setPlayer2Id(strategies);
                    battlesList1NewBattles = em.merge(battlesList1NewBattles);
                    if (oldPlayer2IdOfBattlesList1NewBattles != null && !oldPlayer2IdOfBattlesList1NewBattles.equals(strategies)) {
                        oldPlayer2IdOfBattlesList1NewBattles.getBattlesList1().remove(battlesList1NewBattles);
                        oldPlayer2IdOfBattlesList1NewBattles = em.merge(oldPlayer2IdOfBattlesList1NewBattles);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = strategies.getId();
                if (findStrategies(id) == null) {
                    throw new NonexistentEntityException("The strategies with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Strategies strategies;
            try {
                strategies = em.getReference(Strategies.class, id);
                strategies.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The strategies with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Battles> battlesListOrphanCheck = strategies.getBattlesList();
            for (Battles battlesListOrphanCheckBattles : battlesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Strategies (" + strategies + ") cannot be destroyed since the Battles " + battlesListOrphanCheckBattles + " in its battlesList field has a non-nullable player1Id field.");
            }
            List<Battles> battlesList1OrphanCheck = strategies.getBattlesList1();
            for (Battles battlesList1OrphanCheckBattles : battlesList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Strategies (" + strategies + ") cannot be destroyed since the Battles " + battlesList1OrphanCheckBattles + " in its battlesList1 field has a non-nullable player2Id field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(strategies);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Strategies> findStrategiesEntities() {
        return findStrategiesEntities(true, -1, -1);
    }
    
    public List<Strategies> getIdsForMap(String map){
        Query query = getEntityManager().createNamedQuery("Strategies.findByMap").setParameter("map", map);
        return query.getResultList();
    }

    public List<Strategies> findStrategiesEntities(int maxResults, int firstResult) {
        return findStrategiesEntities(false, maxResults, firstResult);
    }

    private List<Strategies> findStrategiesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Strategies.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Strategies findStrategies(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Strategies.class, id);
        } finally {
            em.close();
        }
    }

    public int getStrategiesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Strategies> rt = cq.from(Strategies.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
