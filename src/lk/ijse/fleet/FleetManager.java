package lk.ijse.fleet;

//import java.util.List;
import java.util.ArrayList;
import java.util.stream.*;
import java.util.Comparator;

public class FleetManager {
    private ArrayList <Vehicle> vehicles= new ArrayList<>();
    private ArrayList <Customer> customers= new ArrayList<>();
    private ArrayList <RentalAgreement> agreements= new ArrayList<>();
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
        this.rentalEvenListner = listener;
    }
    
    public ArrayList<Vehicle> getAvailableVehicles(){
        ArrayList<Vehicle> availableVehicles = new ArrayList<>();
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
        if (rentalEvenListner != null) {
            rentalEvenListner.onRentalCreated(agreement);
        }
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
        return new FleetSnapshot();
    }
    
    synchronized public void printFleetStatus(){
        //generateSnapshot().printSummary();
    }
    
    synchronized public void checkOverdueAgreements(){
        for (RentalAgreement agreement : agreements) {
            agreement.setDaysElapsed(agreement.getDaysElapsed() + 1);
            if(agreement.getDaysElapsed() > agreement.getRentalDays()){
                System.out.println("[OVERDUE ALERT] Agreement " + agreement.getAgreementId() +" has exceeded its " + agreement.getRentalDays() +"-day rental period.");
            }
        }
    }
    
    public ArrayList<Vehicle> getVehiclesByFuelType(FuelType type){
       return (ArrayList<Vehicle>)vehicles.stream()
                .filter(v -> v.getFuelType() == type)
                .collect(Collectors.toList());
    }
    
    public ArrayList<Customer> getCustomerSortedByName(){
       return (ArrayList<Customer>)customers.stream()
                .sorted(Comparator.comparing(c -> c.getFullName()))
                .collect(Collectors.toList());
    }
    
    public double generateRevenueReport(){
        return agreements.stream()
                .mapToDouble(a -> a.getTotalCost())
                .sum();
    }
    
    public void printAllReportables(){
        ArrayList<Reportable> reportables = new ArrayList<>();
        reportables.addAll(vehicles);
        reportables.addAll(customers);
        for (Reportable item : reportables) {
            System.out.println(item.getReportCategory() + ": " + item.getSummary());
        }
    }
    
    public ArrayList<Customer> getAllCustomers(){
        if(customers==null){
            return null;
        }
        ArrayList<Customer> cus=new ArrayList<>();
        for (Customer c : customers) {
            cus.add(c);
        }
        return cus;
    }
    
    public ArrayList<RentalAgreement> getAllAgreements(){
         if(agreements==null){
            return null;
        }
        ArrayList<RentalAgreement> agre=new ArrayList<>();
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
            int available = 0, rented = 0;
            for (Vehicle vehicle : vehicles) {
                if (vehicle.getStatus() == RentalStatus.AVAILABLE) {
                    available++;
                } else if (vehicle.getStatus() == RentalStatus.RENTED) {
                    rented++;
                }
            }
            
            double total = 0;
            for (RentalAgreement agreement : agreements) {
                total += agreement.getTotalCost();
            }
            
            this.availableCount = available;
            this.rentedCount = rented;
            this.totalRevenue = total;
        }
        
        public void printSummary(){            
            System.out.println("[FLEET STATUS] Available: " + availableCount);
            System.out.println("| Rented                : " + rentedCount );
            System.out.println("| Total Revenue         : " + totalRevenue);
        }
    }
}   
