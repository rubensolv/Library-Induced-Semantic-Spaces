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
import ai.synthesis.dreamcoder.DbEvaluationDataset.Behaviorfeature;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Controllers.exceptions.IllegalOrphanException;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Controllers.exceptions.NonexistentEntityException;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rubens
 */
public class BehaviorfeatureJpaController implements Serializable {

    public BehaviorfeatureJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Behaviorfeature behaviorfeature) {
        if (behaviorfeature.getBattlesList() == null) {
            behaviorfeature.setBattlesList(new ArrayList<Battles>());
        }
        if (behaviorfeature.getBattlesList1() == null) {
            behaviorfeature.setBattlesList1(new ArrayList<Battles>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Battles> attachedBattlesList = new ArrayList<Battles>();
            for (Battles battlesListBattlesToAttach : behaviorfeature.getBattlesList()) {
                battlesListBattlesToAttach = em.getReference(battlesListBattlesToAttach.getClass(), battlesListBattlesToAttach.getId());
                attachedBattlesList.add(battlesListBattlesToAttach);
            }
            behaviorfeature.setBattlesList(attachedBattlesList);
            List<Battles> attachedBattlesList1 = new ArrayList<Battles>();
            for (Battles battlesList1BattlesToAttach : behaviorfeature.getBattlesList1()) {
                battlesList1BattlesToAttach = em.getReference(battlesList1BattlesToAttach.getClass(), battlesList1BattlesToAttach.getId());
                attachedBattlesList1.add(battlesList1BattlesToAttach);
            }
            behaviorfeature.setBattlesList1(attachedBattlesList1);
            em.persist(behaviorfeature);
            for (Battles battlesListBattles : behaviorfeature.getBattlesList()) {
                Behaviorfeature oldP1featuresIdOfBattlesListBattles = battlesListBattles.getP1featuresId();
                battlesListBattles.setP1featuresId(behaviorfeature);
                battlesListBattles = em.merge(battlesListBattles);
                if (oldP1featuresIdOfBattlesListBattles != null) {
                    oldP1featuresIdOfBattlesListBattles.getBattlesList().remove(battlesListBattles);
                    oldP1featuresIdOfBattlesListBattles = em.merge(oldP1featuresIdOfBattlesListBattles);
                }
            }
            for (Battles battlesList1Battles : behaviorfeature.getBattlesList1()) {
                Behaviorfeature oldP2featuresIdOfBattlesList1Battles = battlesList1Battles.getP2featuresId();
                battlesList1Battles.setP2featuresId(behaviorfeature);
                battlesList1Battles = em.merge(battlesList1Battles);
                if (oldP2featuresIdOfBattlesList1Battles != null) {
                    oldP2featuresIdOfBattlesList1Battles.getBattlesList1().remove(battlesList1Battles);
                    oldP2featuresIdOfBattlesList1Battles = em.merge(oldP2featuresIdOfBattlesList1Battles);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Behaviorfeature behaviorfeature) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Behaviorfeature persistentBehaviorfeature = em.find(Behaviorfeature.class, behaviorfeature.getId());
            List<Battles> battlesListOld = persistentBehaviorfeature.getBattlesList();
            List<Battles> battlesListNew = behaviorfeature.getBattlesList();
            List<Battles> battlesList1Old = persistentBehaviorfeature.getBattlesList1();
            List<Battles> battlesList1New = behaviorfeature.getBattlesList1();
            List<String> illegalOrphanMessages = null;
            for (Battles battlesListOldBattles : battlesListOld) {
                if (!battlesListNew.contains(battlesListOldBattles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Battles " + battlesListOldBattles + " since its p1featuresId field is not nullable.");
                }
            }
            for (Battles battlesList1OldBattles : battlesList1Old) {
                if (!battlesList1New.contains(battlesList1OldBattles)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Battles " + battlesList1OldBattles + " since its p2featuresId field is not nullable.");
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
            behaviorfeature.setBattlesList(battlesListNew);
            List<Battles> attachedBattlesList1New = new ArrayList<Battles>();
            for (Battles battlesList1NewBattlesToAttach : battlesList1New) {
                battlesList1NewBattlesToAttach = em.getReference(battlesList1NewBattlesToAttach.getClass(), battlesList1NewBattlesToAttach.getId());
                attachedBattlesList1New.add(battlesList1NewBattlesToAttach);
            }
            battlesList1New = attachedBattlesList1New;
            behaviorfeature.setBattlesList1(battlesList1New);
            behaviorfeature = em.merge(behaviorfeature);
            for (Battles battlesListNewBattles : battlesListNew) {
                if (!battlesListOld.contains(battlesListNewBattles)) {
                    Behaviorfeature oldP1featuresIdOfBattlesListNewBattles = battlesListNewBattles.getP1featuresId();
                    battlesListNewBattles.setP1featuresId(behaviorfeature);
                    battlesListNewBattles = em.merge(battlesListNewBattles);
                    if (oldP1featuresIdOfBattlesListNewBattles != null && !oldP1featuresIdOfBattlesListNewBattles.equals(behaviorfeature)) {
                        oldP1featuresIdOfBattlesListNewBattles.getBattlesList().remove(battlesListNewBattles);
                        oldP1featuresIdOfBattlesListNewBattles = em.merge(oldP1featuresIdOfBattlesListNewBattles);
                    }
                }
            }
            for (Battles battlesList1NewBattles : battlesList1New) {
                if (!battlesList1Old.contains(battlesList1NewBattles)) {
                    Behaviorfeature oldP2featuresIdOfBattlesList1NewBattles = battlesList1NewBattles.getP2featuresId();
                    battlesList1NewBattles.setP2featuresId(behaviorfeature);
                    battlesList1NewBattles = em.merge(battlesList1NewBattles);
                    if (oldP2featuresIdOfBattlesList1NewBattles != null && !oldP2featuresIdOfBattlesList1NewBattles.equals(behaviorfeature)) {
                        oldP2featuresIdOfBattlesList1NewBattles.getBattlesList1().remove(battlesList1NewBattles);
                        oldP2featuresIdOfBattlesList1NewBattles = em.merge(oldP2featuresIdOfBattlesList1NewBattles);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = behaviorfeature.getId();
                if (findBehaviorfeature(id) == null) {
                    throw new NonexistentEntityException("The behaviorfeature with id " + id + " no longer exists.");
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
            Behaviorfeature behaviorfeature;
            try {
                behaviorfeature = em.getReference(Behaviorfeature.class, id);
                behaviorfeature.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The behaviorfeature with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            List<Battles> battlesListOrphanCheck = behaviorfeature.getBattlesList();
            for (Battles battlesListOrphanCheckBattles : battlesListOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Behaviorfeature (" + behaviorfeature + ") cannot be destroyed since the Battles " + battlesListOrphanCheckBattles + " in its battlesList field has a non-nullable p1featuresId field.");
            }
            List<Battles> battlesList1OrphanCheck = behaviorfeature.getBattlesList1();
            for (Battles battlesList1OrphanCheckBattles : battlesList1OrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Behaviorfeature (" + behaviorfeature + ") cannot be destroyed since the Battles " + battlesList1OrphanCheckBattles + " in its battlesList1 field has a non-nullable p2featuresId field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(behaviorfeature);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Behaviorfeature> findBehaviorfeatureEntities() {
        return findBehaviorfeatureEntities(true, -1, -1);
    }

    public List<Behaviorfeature> findBehaviorfeatureEntities(int maxResults, int firstResult) {
        return findBehaviorfeatureEntities(false, maxResults, firstResult);
    }

    private List<Behaviorfeature> findBehaviorfeatureEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Behaviorfeature.class));
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

    public Behaviorfeature findBehaviorfeature(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Behaviorfeature.class, id);
        } finally {
            em.close();
        }
    }

    public int getBehaviorfeatureCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Behaviorfeature> rt = cq.from(Behaviorfeature.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
