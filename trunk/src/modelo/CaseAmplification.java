package modelo;

public class CaseAmplification extends Weight{
	private double rho;
	private Weight origw;
	
	public CaseAmplification(Weight w,double amp){
		rho=amp;
		origw=w;
	}
	
	@Override
	public double weightcalculate(User a, User i) {
		double wai=origw.weight(a, i);
		if (wai>=0){
			return Math.pow(wai, rho);
		}else{
			return (-1*(Math.pow(-1*wai, rho)));
		}
	}

}
