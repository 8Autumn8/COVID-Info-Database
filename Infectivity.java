//Functions created by Benjamin Sun

class Infectivity {
  
	//Calculates the percent drop of new cases provided mask usage and two-way mask effectivity 
  public double[] infectivityDrop(double p, double exOut, double exIn) {
		double dropNoMask = (1 - (exOut * p));
		double dropMasked = (1 - (exOut* p)) * (1 - exIn);
		double totalDrop = (1 - (exIn * p)) * (1 - (exOut * p));
		double drop[] = new double [3];
		drop[0] = dropNoMask*100;
		drop[1] = dropMasked*100;
		drop[2] = totalDrop*100;
    return drop;
  }

	//Calculates the percent risk given yesterday's new cases, by projecting the above calculated percent drop and multiplying it on to yesterday's cases, then using that to determine risk
	//Rounds to three decimal places
	public double[] riskPercentCalculated(double newCases, double totalPop) {
		double p = 0.75;
		double exOut = 0.50;
		double exIn = 0.50;
		double a[] = infectivityDrop(p, exIn, exOut);
    System.out.println("Average drop in COVID transmission: " + (100 - a[2]) + "%, with parameters: \nMask-wearing fraction of the population: "+(100*p)+"%; \nMask effectiveness on inhale: "+(100*exIn)+"%; \nMask effectiveness on exhale: "+(100*exOut)+"%");
		double projectedCases = (a[2]/100)*newCases;
		double percent = (projectedCases/totalPop);
		double n = percent * 10000000.0;
		double k = Math.round(n);
		percent = k / 10000000.0;
		double[] data = new double[3];
		data[0] = percent;
		data[1] = projectedCases;
		data[2] = newCases;
 		return data;
	}
}
