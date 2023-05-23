package com.example.workflow;
import javax.inject.Named;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import java.text.DecimalFormat;

@Named
public class CalculateInEUR implements JavaDelegate {
	private static final DecimalFormat decfor = new DecimalFormat("0.00"); 
	@Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
		
            String cash = delegateExecution.getVariable("money").toString();
            double cashDouble = Double.parseDouble(cash);
            String exchangeRate = delegateExecution.getVariable("srednji_tecaj").toString();
            double exchangeRateDouble = Double.parseDouble(exchangeRate);
            
            double cashEUR = cashDouble * exchangeRateDouble;
            
            String cashEURString = String.valueOf(decfor.format(cashEUR));
                        
            delegateExecution.setVariable("Pack_Cash_EUR", cashEURString);
    }
}