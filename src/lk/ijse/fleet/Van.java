
package lk.ijse.fleet;


public class Van extends Vehicle {

    public Van(String registrationNumber, String brand, String model, double dailyRate, FuelType fuelType) {
        super(registrationNumber, brand, model, dailyRate, fuelType);
    }

    @Override
    public double calculateRentalCost(int rentalDays) {
        
        double rentalCost = (getDailyRate() * rentalDays * getFuelType().getCostMultiplier()) + (getDailyRate() * 0.20);
        return rentalCost;
    }
    
    
}
