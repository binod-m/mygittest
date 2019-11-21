
	import ilog.concert.*;
	import ilog.cplex.*;

	public class CplexLPTest2 {

		public static void solveMe() {
			int n = 4; //cargos
	        int m = 3; //compartments
	        double[] p = {310.0, 380.0, 350.0, 285.0}; //profit
	        double[] v = {480.0, 650.0, 580.0, 390.0}; //volume per ton of cargo
	        double[] a = {18.0, 15.0, 23.0, 12.0}; //available weight
	        double[] c = {10.0, 16.0, 8.0}; //capacity of compartment
	        double[] V = {6800.0, 8700.0, 5300.0}; //volume capacity of airplane
	        try {
	       //define the model 	
	       IloCplex cplex = new IloCplex();
	       //create variables
	       IloNumVar[][] x = new IloNumVar[n][];
	       
	       for(int i=0;i<n; i++) {
	    	   x[i]=cplex.numVarArray(m, 0, Double.MAX_VALUE);
	       }
	       
	       IloNumVar y = cplex.numVar(0, Double.MAX_VALUE);
	       //expression 
	       IloLinearNumExpr [] usedWeightCapacity = new IloLinearNumExpr[m];
	       IloLinearNumExpr [] usedVolumeCapacity = new IloLinearNumExpr[m];	  
	       
	       for (int j =0 ; j<m ; j++) {
	    	   usedWeightCapacity[j]=cplex.linearNumExpr();
	    	   usedVolumeCapacity[j]=cplex.linearNumExpr();
	    	   
	    	   for (int i=0; i<n; ++i) {
	    		   usedWeightCapacity[j].addTerm(1.0, x[i][j]);
	    		   usedVolumeCapacity[j].addTerm(v[i], x[i][j]);
	    	   }
	    	   
	       }
	        	
	       IloLinearNumExpr obj = cplex.linearNumExpr();
	       for(int j=0; j<m;++j) {
	    	   for (int i=0;i<n;++i) {
		       obj.addTerm(p[i],x[i][j] );
	    	   }
	       }
	       
	       //define obj
	       cplex.addMaximize(obj);
	       
	       //constraints
	       for(int i=0; i<n; i++) {//you can but you dont need to loop through j for xij since it is taking the sum accross i, cplex will take care of it on its own
	    	   cplex.addLe(cplex.sum(x[i]), a[i]);
	       }
	       
	       for(int j=0; j<m;j++) {
	    	   cplex.addLe(usedWeightCapacity[j],c[j]);
	    	   cplex.addLe(usedVolumeCapacity[j],v[j]);
	    	   cplex.addEq(cplex.prod(1/c[j], usedWeightCapacity[j]), y);
	       }
	       
	       cplex.setParam(IloCplex.Param.Simplex.Display,2);
	       //solve
	       if(cplex.solve()) {
	    	   System.out.println("Objective " + cplex.getObjValue() );
	       }
	       else {
	    	   System.out.println("Model cannot be solved ");
	       }
	       
	       cplex.end();
	        }catch(IloException ex) {
	        	ex.printStackTrace();
	        }

		}
	}
