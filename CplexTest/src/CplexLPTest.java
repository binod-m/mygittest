import java.util.ArrayList;
import java.util.List;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.concert.IloRange;
import ilog.cplex.IloCplex;

public class CplexLPTest {

	public static void solveLP() {
		try {
			IloCplex cplex = new IloCplex();
			//variable
			IloNumVar x = cplex.numVar(0, Double.MAX_VALUE,"x");
			IloNumVar y = cplex.numVar(0, Double.MAX_VALUE,"y");
			
			//expression
			IloLinearNumExpr obj = cplex.linearNumExpr();
			obj.addTerm(0.12, x);
			obj.addTerm(0.15, y);
			
			//define objective
			cplex.addMinimize(obj);
			
			//define constraints
			List<IloRange> constraints = new ArrayList<IloRange>();
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(60, x), cplex.prod(60, y)), 300)); //constraint 1
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(12, x), cplex.prod(6, y)), 36)); //constraint 2
			constraints.add(cplex.addGe(cplex.sum(cplex.prod(10, x), cplex.prod(30, y)), 90)); //constraint 3
			
			//
			IloLinearNumExpr expr_num = cplex.linearNumExpr();
			expr_num.addTerm(2,x);
			expr_num.addTerm(-1, y);
			constraints.add(cplex.addEq(expr_num, 0)); //constraint 4
			expr_num = cplex.linearNumExpr();
			expr_num.addTerm(1, y);
			expr_num.addTerm(-1, x);
			constraints.add(cplex.addLe(expr_num, 8));//constraint 5
			
			cplex.setParam(IloCplex.IntParam.Simplex.Display, 0);
			
			//solve
			if(cplex.solve()) {
				System.out.println("objective = " + cplex.getObjValue());
				System.out.println("x = " + cplex.getValue(x));
				System.out.println("y = " + cplex.getValue(y));
				for(int i=0; i<constraints.size(); i++) {
					System.out.println("Dual of constraints " + (i+1) + " = "  + cplex.getDual(constraints.get(i)));
					System.out.println("Slack of constraints " + (i+1) + " = "  + cplex.getSlack(constraints.get(i)));
				}
			} else {
				System.out.println("Model cannot be solved ");
			};
			
			cplex.end();
		}
		catch(IloException exc){exc.printStackTrace();
		}
		/*github.com/zonbeka/cplex-Examples/*/
	}


}
