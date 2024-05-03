package ai.competition.Ragnar;

import ai.synthesis.ComplexDSL.LS_CFG.*;
import ai.synthesis.ComplexDSL.Synthesis_Base.AIs.Interpreter;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.*;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Actions.*;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG_Condition.OpponentHasUnitThatKillsUnitInOneAttack;
import rts.units.UnitTypeTable;

public class Ragnar extends Interpreter {

  public Ragnar(UnitTypeTable a_utt) {
    super(a_utt, getStrategy());
  }

  public static Node_LS getStrategy() {//let's create a tree piece by piece, note that the command order matters
    S_LS S1 = new S_LS(new For_S_LS(new S_LS(new C_LS(new Idle()))));
    S_LS S2 = new S_LS(new C_LS(new Build(new Type("Barracks"), new Direction("EnemyDir"), new N("1"))));
    S_LS S3 = new S_LS(new For_S_LS(new S_LS(new C_LS(new Train(new Type("Worker"), new Direction("Left"), new N("5"))))));
    S_LS S4 = new S_LS(new C_LS(new Train(new Type("Heavy"), new Direction("Up"), new N("10"))));
    S_LS S5 = new S_LS(new C_LS(new Harvest(new N("9"))));
    S_LS S6 = new S_LS(new If_B_then_S_LS(new B_LS(new OpponentHasUnitThatKillsUnitInOneAttack()), new S_LS(new C_LS(new Attack(new OpponentPolicy("Weakest"))))));
    S_LS S7 = new S_LS(new C_LS(new Attack(new OpponentPolicy("Closest"))));

    S_LS S8 = new S_LS(new S_S_LS(S1, S2));
    S_LS S9 = new S_LS(new S_S_LS(S8, S3));
    S_LS S10 = new S_LS(new S_S_LS(S9, S4));
    S_LS S11 = new S_LS(new S_S_LS(S10, S5));
    S_LS S12 = new S_LS(new S_S_LS(S11, S6));
    S_LS S13 = new S_LS(new S_S_LS(S12, S7));

    S_LS root = new S_LS(new For_S_LS(S13));

    return root;
  }

}
