/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.synthesis.dreamcoder.DbEvaluationDataset;

import ai.synthesis.dreamcoder.EvaluateGameState.BehavioralFeature;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author rubens
 */
@Entity
@Table(name = "pymrts_behaviorfeature", catalog = "", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Behaviorfeature.findAll", query = "SELECT b FROM Behaviorfeature b"),
    @NamedQuery(name = "Behaviorfeature.findById", query = "SELECT b FROM Behaviorfeature b WHERE b.id = :id"),
    @NamedQuery(name = "Behaviorfeature.findByWorker", query = "SELECT b FROM Behaviorfeature b WHERE b.worker = :worker"),
    @NamedQuery(name = "Behaviorfeature.findByLight", query = "SELECT b FROM Behaviorfeature b WHERE b.light = :light"),
    @NamedQuery(name = "Behaviorfeature.findByRanged", query = "SELECT b FROM Behaviorfeature b WHERE b.ranged = :ranged"),
    @NamedQuery(name = "Behaviorfeature.findByHeavy", query = "SELECT b FROM Behaviorfeature b WHERE b.heavy = :heavy"),
    @NamedQuery(name = "Behaviorfeature.findByBase", query = "SELECT b FROM Behaviorfeature b WHERE b.base = :base"),
    @NamedQuery(name = "Behaviorfeature.findByBarrack", query = "SELECT b FROM Behaviorfeature b WHERE b.barrack = :barrack"),
    @NamedQuery(name = "Behaviorfeature.findBySavedresource", query = "SELECT b FROM Behaviorfeature b WHERE b.savedresource = :savedresource")})
public class Behaviorfeature implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "worker", nullable = false)
    private int worker;
    @Basic(optional = false)
    @Column(name = "light", nullable = false)
    private int light;
    @Basic(optional = false)
    @Column(name = "ranged", nullable = false)
    private int ranged;
    @Basic(optional = false)
    @Column(name = "heavy", nullable = false)
    private int heavy;
    @Basic(optional = false)
    @Column(name = "base", nullable = false)
    private int base;
    @Basic(optional = false)
    @Column(name = "barrack", nullable = false)
    private int barrack;
    @Basic(optional = false)
    @Column(name = "savedresource", nullable = false)
    private int savedresource;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "p1featuresId", fetch = FetchType.LAZY)
    private List<Battles> battlesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "p2featuresId", fetch = FetchType.LAZY)
    private List<Battles> battlesList1;

    public Behaviorfeature() {
    }

    public Behaviorfeature(Long id) {
        this.id = id;
    }

    public Behaviorfeature(Long id, int worker, int light, int ranged, int heavy, int base, int barrack, int savedresource) {
        this.id = id;
        this.worker = worker;
        this.light = light;
        this.ranged = ranged;
        this.heavy = heavy;
        this.base = base;
        this.barrack = barrack;
        this.savedresource = savedresource;
    }
    public Behaviorfeature(BehavioralFeature beh) {
        this.worker = beh.getWorker();
        this.light = beh.getLight();
        this.ranged = beh.getRanged();
        this.heavy = beh.getHeavy();
        this.base = beh.getBase();
        this.barrack = beh.getBarrack();
        this.savedresource = beh.getSaved_resource();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getWorker() {
        return worker;
    }

    public void setWorker(int worker) {
        this.worker = worker;
    }

    public int getLight() {
        return light;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public int getRanged() {
        return ranged;
    }

    public void setRanged(int ranged) {
        this.ranged = ranged;
    }

    public int getHeavy() {
        return heavy;
    }

    public void setHeavy(int heavy) {
        this.heavy = heavy;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getBarrack() {
        return barrack;
    }

    public void setBarrack(int barrack) {
        this.barrack = barrack;
    }

    public int getSavedresource() {
        return savedresource;
    }

    public void setSavedresource(int savedresource) {
        this.savedresource = savedresource;
    }

    @XmlTransient
    public List<Battles> getBattlesList() {
        return battlesList;
    }

    public void setBattlesList(List<Battles> battlesList) {
        this.battlesList = battlesList;
    }

    @XmlTransient
    public List<Battles> getBattlesList1() {
        return battlesList1;
    }

    public void setBattlesList1(List<Battles> battlesList1) {
        this.battlesList1 = battlesList1;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Behaviorfeature)) {
            return false;
        }
        Behaviorfeature other = (Behaviorfeature) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ai.synthesis.dreamcoder.DbEvaluationDataset.Behaviorfeature[ id=" + id + " ]";
    }
    
}
