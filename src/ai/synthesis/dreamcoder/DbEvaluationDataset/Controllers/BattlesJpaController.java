/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.synthesis.dreamcoder.DbEvaluationDataset.Controllers;

import ai.synthesis.dreamcoder.DbEvaluationDataset.Battles;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Behaviorfeature;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Controllers.exceptions.NonexistentEntityException;
import ai.synthesis.dreamcoder.DbEvaluationDataset.Strategies;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

/**
 *
 * @author rubens
 */
public class BattlesJpaController implements Serializable {

    public BattlesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Battles battles) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Behaviorfeature p1featuresId = battles.getP1featuresId();
            if (p1featuresId != null) {
                p1featuresId = em.getReference(p1featuresId.getClass(), p1featuresId.getId());
                battles.setP1featuresId(p1featuresId);
            }
            Behaviorfeature p2featuresId = battles.getP2featuresId();
            if (p2featuresId != null) {
                p2featuresId = em.getReference(p2featuresId.getClass(), p2featuresId.getId());
                battles.setP2featuresId(p2featuresId);
            }
            Strategies player1Id = battles.getPlayer1Id();
            if (player1Id != null) {
                player1Id = em.getReference(player1Id.getClass(), player1Id.getId());
                battles.setPlayer1Id(player1Id);
            }
            Strategies player2Id = battles.getPlayer2Id();
            if (player2Id != null) {
                player2Id = em.getReference(player2Id.getClass(), player2Id.getId());
                battles.setPlayer2Id(player2Id);
            }
            em.persist(battles);
            if (p1featuresId != null) {
                p1featuresId.getBattlesList().add(battles);
                p1featuresId = em.merge(p1featuresId);
            }
            if (p2featuresId != null) {
                p2featuresId.getBattlesList().add(battles);
                p2featuresId = em.merge(p2featuresId);
            }
            if (player1Id != null) {
                player1Id.getBattlesList().add(battles);
                player1Id = em.merge(player1Id);
            }
            if (player2Id != null) {
                player2Id.getBattlesList().add(battles);
                player2Id = em.merge(player2Id);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Battles battles) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Battles persistentBattles = em.find(Battles.class, battles.getId());
            Behaviorfeature p1featuresIdOld = persistentBattles.getP1featuresId();
            Behaviorfeature p1featuresIdNew = battles.getP1featuresId();
            Behaviorfeature p2featuresIdOld = persistentBattles.getP2featuresId();
            Behaviorfeature p2featuresIdNew = battles.getP2featuresId();
            Strategies player1IdOld = persistentBattles.getPlayer1Id();
            Strategies player1IdNew = battles.getPlayer1Id();
            Strategies player2IdOld = persistentBattles.getPlayer2Id();
            Strategies player2IdNew = battles.getPlayer2Id();
            if (p1featuresIdNew != null) {
                p1featuresIdNew = em.getReference(p1featuresIdNew.getClass(), p1featuresIdNew.getId());
                battles.setP1featuresId(p1featuresIdNew);
            }
            if (p2featuresIdNew != null) {
                p2featuresIdNew = em.getReference(p2featuresIdNew.getClass(), p2featuresIdNew.getId());
                battles.setP2featuresId(p2featuresIdNew);
            }
            if (player1IdNew != null) {
                player1IdNew = em.getReference(player1IdNew.getClass(), player1IdNew.getId());
                battles.setPlayer1Id(player1IdNew);
            }
            if (player2IdNew != null) {
                player2IdNew = em.getReference(player2IdNew.getClass(), player2IdNew.getId());
                battles.setPlayer2Id(player2IdNew);
            }
            battles = em.merge(battles);
            if (p1featuresIdOld != null && !p1featuresIdOld.equals(p1featuresIdNew)) {
                p1featuresIdOld.getBattlesList().remove(battles);
                p1featuresIdOld = em.merge(p1featuresIdOld);
            }
            if (p1featuresIdNew != null && !p1featuresIdNew.equals(p1featuresIdOld)) {
                p1featuresIdNew.getBattlesList().add(battles);
                p1featuresIdNew = em.merge(p1featuresIdNew);
            }
            if (p2featuresIdOld != null && !p2featuresIdOld.equals(p2featuresIdNew)) {
                p2featuresIdOld.getBattlesList().remove(battles);
                p2featuresIdOld = em.merge(p2featuresIdOld);
            }
            if (p2featuresIdNew != null && !p2featuresIdNew.equals(p2featuresIdOld)) {
                p2featuresIdNew.getBattlesList().add(battles);
                p2featuresIdNew = em.merge(p2featuresIdNew);
            }
            if (player1IdOld != null && !player1IdOld.equals(player1IdNew)) {
                player1IdOld.getBattlesList().remove(battles);
                player1IdOld = em.merge(player1IdOld);
            }
            if (player1IdNew != null && !player1IdNew.equals(player1IdOld)) {
                player1IdNew.getBattlesList().add(battles);
                player1IdNew = em.merge(player1IdNew);
            }
            if (player2IdOld != null && !player2IdOld.equals(player2IdNew)) {
                player2IdOld.getBattlesList().remove(battles);
                player2IdOld = em.merge(player2IdOld);
            }
            if (player2IdNew != null && !player2IdNew.equals(player2IdOld)) {
                player2IdNew.getBattlesList().add(battles);
                player2IdNew = em.merge(player2IdNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = battles.getId();
                if (findBattles(id) == null) {
                    throw new NonexistentEntityException("The battles with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Battles battles;
            try {
                battles = em.getReference(Battles.class, id);
                battles.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The battles with id " + id + " no longer exists.", enfe);
            }
            Behaviorfeature p1featuresId = battles.getP1featuresId();
            if (p1featuresId != null) {
                p1featuresId.getBattlesList().remove(battles);
                p1featuresId = em.merge(p1featuresId);
            }
            Behaviorfeature p2featuresId = battles.getP2featuresId();
            if (p2featuresId != null) {
                p2featuresId.getBattlesList().remove(battles);
                p2featuresId = em.merge(p2featuresId);
            }
            Strategies player1Id = battles.getPlayer1Id();
            if (player1Id != null) {
                player1Id.getBattlesList().remove(battles);
                player1Id = em.merge(player1Id);
            }
            Strategies player2Id = battles.getPlayer2Id();
            if (player2Id != null) {
                player2Id.getBattlesList().remove(battles);
                player2Id = em.merge(player2Id);
            }
            em.remove(battles);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Battles> findBattlesEntities() {
        return findBattlesEntities(true, -1, -1);
    }

    public List<Battles> findBattlesEntities(int maxResults, int firstResult) {
        return findBattlesEntities(false, maxResults, firstResult);
    }

    private List<Battles> findBattlesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Battles.class));
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

    public Battles findBattles(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Battles.class, id);
        } finally {
            em.close();
        }
    }

    public int getBattlesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Battles> rt = cq.from(Battles.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
    
    /**
     * Check if a battle for both strategies exists in the database
     * @param id1 Strategy
     * @param id2 Strategy
     * @return False if there is no battle in the DB, true otherwise.
     */
    public Boolean checkIfBattleExistByPlayersID(Strategies id1, Strategies id2) {
        List check = getEntityManager().createQuery(
                "SELECT b FROM Battles b WHERE b.player1Id = :id1 and b.player2Id =:id2")
                .setParameter("id1", id1)
                .setParameter("id2", id2)
                .setMaxResults(10)
                .getResultList();
        if (check.isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

}
