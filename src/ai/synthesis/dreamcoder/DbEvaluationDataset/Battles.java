/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ai.synthesis.dreamcoder.DbEvaluationDataset;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author rubens
 */
@Entity
@Table(name = "pymrts_battles", catalog = "", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Battles.findAll", query = "SELECT b FROM Battles b"),
    @NamedQuery(name = "Battles.findById", query = "SELECT b FROM Battles b WHERE b.id = :id"),
    @NamedQuery(name = "Battles.findByWinner", query = "SELECT b FROM Battles b WHERE b.winner = :winner"),
    @NamedQuery(name = "Battles.findByMap", query = "SELECT b FROM Battles b WHERE b.map = :map")})
public class Battles implements Serializable {

    

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "map", length = 100)
    private String map;
    @JoinColumn(name = "p1features_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Behaviorfeature p1featuresId;
    @JoinColumn(name = "p2features_id", referencedColumnName = "id", nullable = true)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Behaviorfeature p2featuresId;
    @JoinColumn(name = "player1_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Strategies player1Id;
    @JoinColumn(name = "player2_id", referencedColumnName = "id", nullable = false)
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Strategies player2Id;
    @Column(name = "winner")
    private Integer winner;

    public Battles() {
    }

    public Battles(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }

    public Behaviorfeature getP1featuresId() {
        return p1featuresId;
    }

    public void setP1featuresId(Behaviorfeature p1featuresId) {
        this.p1featuresId = p1featuresId;
    }

    public Behaviorfeature getP2featuresId() {
        return p2featuresId;
    }

    public void setP2featuresId(Behaviorfeature p2featuresId) {
        this.p2featuresId = p2featuresId;
    }

    public Strategies getPlayer1Id() {
        return player1Id;
    }

    public void setPlayer1Id(Strategies player1Id) {
        this.player1Id = player1Id;
    }

    public Strategies getPlayer2Id() {
        return player2Id;
    }

    public void setPlayer2Id(Strategies player2Id) {
        this.player2Id = player2Id;
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
        if (!(object instanceof Battles)) {
            return false;
        }
        Battles other = (Battles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ai.synthesis.dreamcoder.DbEvaluationDataset.Battles[ id=" + id + " ]";
    }

    public Integer getWinner() {
        return winner;
    }

    public void setWinner(Integer winner) {
        this.winner = winner;
    }
    
}
