/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.synthesis.dreamcoder.DbEvaluationDataset;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
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
@Table(name = "pymrts_strategies", catalog = "", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Strategies.findAll", query = "SELECT s FROM Strategies s"),
    @NamedQuery(name = "Strategies.findById", query = "SELECT s FROM Strategies s WHERE s.id = :id"),
    @NamedQuery(name = "Strategies.findByStrategy", query = "SELECT s FROM Strategies s WHERE s.strategy = :strategy"),
    @NamedQuery(name = "Strategies.findByMap", query = "SELECT s FROM Strategies s WHERE s.map = :map"),
    @NamedQuery(name = "Strategies.findByLearner", query = "SELECT s FROM Strategies s WHERE s.learner = :learner"),
    @NamedQuery(name = "Strategies.findByCollectedFile", query = "SELECT s FROM Strategies s WHERE s.collectedFile = :collectedFile"),
    @NamedQuery(name = "Strategies.findByAlgorithm", query = "SELECT s FROM Strategies s WHERE s.algorithm = :algorithm"),
    @NamedQuery(name = "Strategies.findByTime", query = "SELECT s FROM Strategies s WHERE s.time = :time"),
    @NamedQuery(name = "Strategies.findByBudget", query = "SELECT s FROM Strategies s WHERE s.budget = :budget")})
public class Strategies implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "strategy", nullable = false, length = 1800)
    private String strategy;
    @Basic(optional = false)
    @Column(name = "map", nullable = false, length = 100)
    private String map;
    @Basic(optional = false)
    @Column(name = "learner", nullable = false, length = 4)
    private String learner;
    @Column(name = "collected_file", length = 200)
    private String collectedFile;
    @Basic(optional = false)
    @Column(name = "algorithm", nullable = false, length = 4)
    private String algorithm;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "time", precision = 18, scale = 3)
    private BigDecimal time;
    @Column(name = "budget")
    private BigInteger budget;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player1Id", fetch = FetchType.LAZY)
    private List<Battles> battlesList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "player2Id", fetch = FetchType.LAZY)
    private List<Battles> battlesList1;

    public Strategies() {
    }

    public Strategies(Long id) {
        this.id = id;
    }

    public Strategies(Long id, String strategy, String map, String learner, String algorithm) {
        this.id = id;
        this.strategy = strategy;
        this.map = map;
        this.learner = learner;
        this.algorithm = algorithm;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public String getLearner() {
        return learner;
    }

    public void setLearner(String learner) {
        this.learner = learner;
    }

    public String getCollectedFile() {
        return collectedFile;
    }

    public void setCollectedFile(String collectedFile) {
        this.collectedFile = collectedFile;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public BigDecimal getTime() {
        return time;
    }

    public void setTime(BigDecimal time) {
        this.time = time;
    }

    public BigInteger getBudget() {
        return budget;
    }

    public void setBudget(BigInteger budget) {
        this.budget = budget;
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
        if (!(object instanceof Strategies)) {
            return false;
        }
        Strategies other = (Strategies) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ai.synthesis.dreamcoder.DbEvaluationDataset.Strategies[ id=" + id + " ]";
    }
    
}
