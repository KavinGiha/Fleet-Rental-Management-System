package lk.ijse.fleet;

//import java.util.List;
import java.util.LinkedList;

public class FleetManager {
    private LinkedList <Vehicle> vehicles= new LinkedList<>();
    private LinkedList <Customer> customers= new LinkedList<>();
    private LinkedList <RentalAgreement> agreements= new LinkedList<>();
    private RentalEventListener rentalEvenListner;
    
    public void addVehicle(Vehicle v){
        if(v != null){
            vehicles.add(v);
            System.out.println("Vehicle added successfully");
        }
    }
    
    public void addCustomer(Customer c){
        if(c!=null){
            customers.add(c);
            System.out.println("Customer added successfully");
        }
        
    }
    
    public Vehicle findVehicleById(String id){
        for (Vehicle vehicle : vehicles) {
            if(vehicle.getVehicleId().equals(id)){
                return vehicle;
            }
        }
        System.out.println("Vehicle not found!");
        return null;
    }
    
    public Customer findCustomerById(String id){
        for (Customer customer : customers) {
            if(customer.getCustomerId().equals(id)){
                return customer;
            }
        }
        System.out.println("Customer not found!");
        return null;
    }
    
    public void setRentalEventListener(RentalEventListener listener){
        
    }
    
    public LinkedList<Vehicle> getAvailableVehicles(){
        LinkedList<Vehicle> availableVehicles = new LinkedList<>();
        for (Vehicle vehicle : vehicles) {
            if(vehicle.getStatus().equals(RentalStatus.AVAILABLE)){
                availableVehicles.add(vehicle);
            }
        }
        return availableVehicles;
    }
    
    public RentalAgreement createRentalAgreement(String vehicleId, String customerId, int rentalDays) throws VehicleNotAvailableException, InsufficientCreditException{
        Vehicle v = findVehicleById(vehicleId);
        if (v == null){
            return null;
        }
        
        if(v.getStatus() != RentalStatus.AVAILABLE){
            throw new VehicleNotAvailableException("Vehicle is not available!");            
        }
        
        Customer c = findCustomerById(customerId);
        
        if(c == null){
            return null;
        }
        
        RentalAgreement agreement = new RentalAgreement(v, c, rentalDays);
        
        double cost = agreement.getTotalCost();
        
        if(c instanceof CorporateCustomer){
            if(!((CorporateCustomer) c).hasSufficientCredit(cost)){
                throw new InsufficientCreditException("Insufficient credits!");                
            }
        }
                
        agreements.add(agreement);
        v.setStatus(RentalStatus.RENTED);
                
        return agreement;
    }
    public void returnVehicle(String agreementId){
        for (RentalAgreement agreement : agreements) {
            if(agreement.getAgreementId().equals(agreementId)){
                agreement.getVehicle().setStatus(RentalStatus.AVAILABLE);
                System.out.println("Vehicle return confirmed!");
                return;
            }            
        }
        System.out.println("Agreement not found!");
    }
    
    public FleetSnapshot generateSnapshot(){
        return null;
    }
    
    synchronized public void printFleetStatus(){
        generateSnapshot().printSummary();
    }
    
    synchronized public void checkOverdueAgreements(){
        
    }
    
    public LinkedList<Vehicle> getVehiclesByFuelType(FuelType type){
        return null;
    }
    
    public LinkedList<Customer> getCustomerSortedByName(){
        return null;
    }
    
    public double generateRevenueReport(){
        return 0;
    }
    
    public void printAllReportables(){
        
    }
    
    public LinkedList<Customer> getAllCustomers(){
        if(customers==null){
            return null;
        }
        LinkedList<Customer> cus=new LinkedList<>();
        for (Customer c : customers) {
            cus.add(c);
        }
        return cus;
    }
    
    public LinkedList<RentalAgreement> getAllAgreements(){
         if(agreements==null){
            return null;
        }
        LinkedList<RentalAgreement> agre=new LinkedList<>();
        for (RentalAgreement a : agreements) {
            agre.add(a);
        }
        return agre;
        
    }
    
    class FleetSnapshot{
        private int availableCount;
        private int rentedCount;
        private double totalRevenue;
        
        public FleetSnapshot(){
            
        }
        
        public void printSummary(){
            
        }
    }
}   
