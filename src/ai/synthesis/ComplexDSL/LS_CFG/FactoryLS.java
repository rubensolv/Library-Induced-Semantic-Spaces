package ai.synthesis.ComplexDSL.LS_CFG;

import ai.synthesis.ComplexDSL.LS_Actions.Harvest_LS;
import ai.synthesis.ComplexDSL.LS_Actions.moveToUnit_LS;
import ai.synthesis.ComplexDSL.LS_Actions.Idle_LS;
import ai.synthesis.ComplexDSL.LS_Actions.Attack_LS;
import ai.synthesis.ComplexDSL.LS_Actions.Build_LS;
import ai.synthesis.ComplexDSL.LS_Actions.Train_LS;
import ai.synthesis.ComplexDSL.LS_Actions.MoveAway_LS;
import ai.synthesis.ComplexDSL.LS_Condition.HasUnitThatKillsInOneAttack_LS;
import ai.synthesis.ComplexDSL.LS_Condition.OpponentHasUnitInPlayerRange_LS;
import ai.synthesis.ComplexDSL.LS_Condition.OpponentHasNumberOfUnits_LS;
import ai.synthesis.ComplexDSL.LS_Condition.HasUnitWithinDistanceFromOpponent_LS;
import ai.synthesis.ComplexDSL.LS_Condition.HaveQtdUnitsAttacking_LS;
import ai.synthesis.ComplexDSL.LS_Condition.CanAttack_LS;
import ai.synthesis.ComplexDSL.LS_Condition.HasLessNumberOfUnit_LS;
import ai.synthesis.ComplexDSL.LS_Condition.HasNumberOfUnits_LS;
import ai.synthesis.ComplexDSL.LS_Condition.OpponentHasUnitThatKillsUnitInOneAttack_LS;
import ai.synthesis.ComplexDSL.LS_Condition.HasUnitInOpponentRange_LS;
import ai.synthesis.ComplexDSL.LS_Condition.Is_Type_LS;
import ai.synthesis.ComplexDSL.LS_Condition.HasNumberOfWorkersHarvesting_LS;
import ai.synthesis.ComplexDSL.LS_Condition.CanHarvest_LS;
import ai.synthesis.ComplexDSL.LS_Condition.Is_Builder_LS;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.AlmostTerminal;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.B;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.ChildB;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.ChildC;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.ChildS;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Direction;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Factory;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.N;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Node;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.OpponentPolicy;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.S;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.TargetPlayer;
import ai.synthesis.ComplexDSL.Synthesis_Base.CFG.Type;

public class FactoryLS implements Factory {

	public FactoryLS() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public Node build_S() {
		// TODO Auto-generated method stub
		return new S_LS();
	}

	@Override
	public Node build_S(ChildS s) {
		// TODO Auto-generated method stub
		return new S_LS(s);
	}

	@Override
	public Node build_S_S() {
		// TODO Auto-generated method stub
		return new S_S_LS();
	}

	@Override
	public Node build_S_S(S leftS, S rightS) {
		// TODO Auto-generated method stub
		return new S_S_LS(leftS,rightS);
	}

	@Override
	public Node build_For_S() {
		// TODO Auto-generated method stub
		return new For_S_LS();
	}

	@Override
	public Node build_For_S(S child) {
		// TODO Auto-generated method stub
		return new For_S_LS(child);
	}

	@Override
	public Node build_If_B_then_S_else_S() {
		// TODO Auto-generated method stub
		return new If_B_then_S_else_S_LS();
	}

	@Override
	public Node build_If_B_then_S_else_S(B b, S thenS, S elseS) {
		// TODO Auto-generated method stub
		return new If_B_then_S_else_S_LS(b,thenS,elseS);
	}

	@Override
	public Node build_If_B_then_S() {
		// TODO Auto-generated method stub
		return new If_B_then_S_LS();
	}

	@Override
	public Node build_If_B_then_S(B b, S s) {
		// TODO Auto-generated method stub
		return new If_B_then_S_LS(b,s);
	}

	@Override
	public Node build_Empty() {
		// TODO Auto-generated method stub
		return new Empty_LS();
	}

	@Override
	public Node build_C() {
		// TODO Auto-generated method stub
		return new C_LS();
	}

	@Override
	public Node build_C(ChildC childc) {
		// TODO Auto-generated method stub
		return new C_LS(childc);
	}

	@Override
	public Node build_B() {
		// TODO Auto-generated method stub
		return new B_LS();
	}

	@Override
	public Node build_B(ChildB childb) {
		// TODO Auto-generated method stub
		return new B_LS(childb);
	}

	@Override
	public Node build_Attack() {
		// TODO Auto-generated method stub
		return new Attack_LS();
	}

	@Override
	public Node build_Attack(OpponentPolicy op) {
		// TODO Auto-generated method stub
		return new Attack_LS(op);
	}

	@Override
	public Node build_Build() {
		// TODO Auto-generated method stub
		return new Build_LS();
	}

	@Override
	public Node build_Build(Type type, Direction direc,N n) {
		// TODO Auto-generated method stub
		return new Build_LS(type,direc,n);
	}

	@Override
	public Node build_Harvest() {
		// TODO Auto-generated method stub
		return new Harvest_LS();
	}

	@Override
	public Node build_Idle() {
		// TODO Auto-generated method stub
		return new Idle_LS();
	}

	@Override
	public Node build_MoveAway() {
		// TODO Auto-generated method stub
		return new MoveAway_LS();
	}

	@Override
	public Node build_moveToUnit() {
		// TODO Auto-generated method stub
		return new moveToUnit_LS();
	}

	@Override
	public Node build_moveToUnit(TargetPlayer tp, OpponentPolicy op) {
		// TODO Auto-generated method stub
		return new moveToUnit_LS(tp,op);
	}

	@Override
	public Node build_Train() {
		// TODO Auto-generated method stub
		return new Train_LS();
	}

	@Override
	public Node build_Train(Type type, Direction direc,N n) {
		// TODO Auto-generated method stub
		return new Train_LS(type,direc,n);
	}

	@Override
	public Node build_OpponentHasNumberOfUnits() {
		// TODO Auto-generated method stub
		return new OpponentHasNumberOfUnits_LS();
	}

	@Override
	public Node build_is_Type() {
		// TODO Auto-generated method stub
		return new Is_Type_LS();
	}

	@Override
	public Node build_Is_Builder() {
		// TODO Auto-generated method stub
		return new Is_Builder_LS() ;
	}

	@Override
	public Node build_HaveQtdUnitsAttacking() {
		// TODO Auto-generated method stub
		return new HaveQtdUnitsAttacking_LS();
	}

	@Override
	public Node build_HasUnitWithinDistanceFromOpponent() {
		// TODO Auto-generated method stub
		return new HasUnitWithinDistanceFromOpponent_LS();
	}

	@Override
	public Node build_HasUnitThatKillsInOneAttack() {
		// TODO Auto-generated method stub
		return new HasUnitThatKillsInOneAttack_LS();
	}

	@Override
	public Node build_HasUnitInOpponentRange() {
		// TODO Auto-generated method stub
		return new HasUnitInOpponentRange_LS();
	}

	@Override
	public Node build_HasNumberOfWorkersHarvesting() {
		// TODO Auto-generated method stub
		return new HasNumberOfWorkersHarvesting_LS();
	}

	@Override
	public Node build_HasNumberOfUnits() {
		// TODO Auto-generated method stub
		return new HasNumberOfUnits_LS();
	}

	@Override
	public Node build_HasLessNumberOfUnits() {
		// TODO Auto-generated method stub
		return new HasLessNumberOfUnit_LS();
	}

	@Override
	public Node build_CanHarvest() {
		// TODO Auto-generated method stub
		return new CanHarvest_LS();
	}

	@Override
	public Node build_CanAttack() {
		// TODO Auto-generated method stub
		return new CanAttack_LS();
	}

	@Override
	public Node build_OpponentHasUnitInPlayerRange() {
		// TODO Auto-generated method stub
		return new OpponentHasUnitInPlayerRange_LS();
	}

	@Override
	public Node build_OpponentHasUnitThatKillsUnitInOneAttack() {
		// TODO Auto-generated method stub
		return new OpponentHasUnitThatKillsUnitInOneAttack_LS();
	}

	@Override
	public Node build_OpponentHasNumberOfUnits(Type type, N n) {
		// TODO Auto-generated method stub
		return new OpponentHasNumberOfUnits_LS(type,n);
	}

	@Override
	public Node build_is_Type(Type type) {
		// TODO Auto-generated method stub
		return new Is_Type_LS(type);
	}

	@Override
	public Node build_HaveQtdUnitsAttacking(N n) {
		// TODO Auto-generated method stub
		return new HaveQtdUnitsAttacking_LS(n);
	}

	@Override
	public Node build_HasUnitWithinDistanceFromOpponent(N n) {
		// TODO Auto-generated method stub
		return new HasUnitWithinDistanceFromOpponent_LS(n);
	}

	@Override
	public Node build_HasNumberOfWorkersHarvesting(N n) {
		// TODO Auto-generated method stub
		return new HasNumberOfWorkersHarvesting_LS(n);
	}

	@Override
	public Node build_HasNumberOfUnits(Type type, N n) {
		// TODO Auto-generated method stub
		return new HasNumberOfUnits_LS(type,n);
	}

	@Override
	public Node build_HasLessNumberOfUnits(Type type, N n) {
		// TODO Auto-generated method stub
		return new HasLessNumberOfUnit_LS(type,n);
	}

	@Override
	public AlmostTerminal build_Type() {
		// TODO Auto-generated method stub
		return new Type();
	}

	@Override
	public AlmostTerminal build_Direction() {
		// TODO Auto-generated method stub
		return new Direction();
	}

	@Override
	public AlmostTerminal build_N() {
		// TODO Auto-generated method stub
		return new N();
	}

	@Override
	public AlmostTerminal build_TargetPlayer() {
		// TODO Auto-generated method stub
		return new TargetPlayer();
	}

	@Override
	public AlmostTerminal build_OpponentPolicy() {
		// TODO Auto-generated method stub
		return new OpponentPolicy();
	}

	@Override
	public AlmostTerminal build_Type(String value) {
		// TODO Auto-generated method stub
		return new Type(value);
	}

	@Override
	public AlmostTerminal build_Direction(String value) {
		// TODO Auto-generated method stub
		return new Direction(value);
	}

	@Override
	public AlmostTerminal build_N(String value) {
		// TODO Auto-generated method stub
		return new N(value);
	}

	@Override
	public AlmostTerminal build_TargetPlayer(String value) {
		// TODO Auto-generated method stub
		return new TargetPlayer(value);
	}

	@Override
	public AlmostTerminal build_OpponentPolicy(String value) {
		// TODO Auto-generated method stub
		return new OpponentPolicy(value);
	}

	@Override
	public Node build_Harvest(N n) {
		// TODO Auto-generated method stub
		return new Harvest_LS(n);
	}

}
