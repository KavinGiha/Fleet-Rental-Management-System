package lk.ijse.fleet;

import java.util.Scanner;
//import java.util.List;
import java.util.ArrayList;

public class Main {

    public static FleetManager fleetManager = new FleetManager();
    public static UserManager userManager= new UserManager();
    public static Scanner scanner = new Scanner(System.in);
    public static SystemUser loggedInUser = null;
    
    
    public static void main(String[] args) {
        
        run();
        
    }
    
    public static void run(){
        fleetManager.setRentalEventListener(agreement -> System.out.println("[EVENT] New rental created: " + agreement.getAgreementId()));
        
        Thread statusThread = new Thread(new VehicleStatusMonitor(fleetManager));
        statusThread.setDaemon(true);
        statusThread.start();
        
        Thread overdueThread = new Thread(new OverdueReminderWorker(fleetManager));
        overdueThread.setDaemon(true);
        overdueThread.start();
        showAuthMenu();
    }
    
    public static void showAuthMenu(){
        while(loggedInUser== null){
            System.out.println("========================================================");
            System.out.println("FLEET RENTAL MANAGEMENT SYSTEM (FRMS)");
            System.out.println("========================================================");
            System.out.println("[1] Login");
            System.out.println("[2] Register New User");
            System.out.println("[3] Exit");
            System.out.print("Enter choice > ");
            String choice = scanner.nextLine();
            switch(choice){
                case "1":
                    System.out.print("Enter the username : ");
                    String uname = scanner.nextLine();
                    System.out.print("Enter the password : ");
                    String upass = scanner.nextLine();
                    loggedInUser = userManager.login(uname, upass);
                    if(loggedInUser!=null){
                        showMainMenu();
                    }else{
                        System.out.println("INVALID USERNAME or PASSWORD");
                    }
                    break;
                    
                case "2":
                    System.out.print("Enter the Full name : ");
                    String fullname = scanner.nextLine();
                    System.out.print("Enter the username  : ");
                    String newUserName = scanner.nextLine();
                    System.out.print("Enter the password  : ");
                    String newUserPass = scanner.nextLine();
                    
                    
                    userManager.setUsers(userManager.registerUser(newUserName, newUserPass,fullname));
                    
                    break;
                    
                case "3":
                    System.exit(0);
                    
                default:
                    System.out.println("Invalid choice");
            }
        }    
    }
    
    public static void showMainMenu(){
        while(true){
            System.out.println("========================================================");
            System.out.println("FRMS MAIN MENU | Logged in as :" + loggedInUser.getFullName());
            System.out.println("========================================================");
            System.out.println("[1] Add Vehicle");
            System.out.println("[2] Add Customer");
            System.out.println("[3] View Available Vehicle");
            System.out.println("[4] Create Rental Agreement");
            System.out.println("[5] Return Vehicle");
            System.out.println("[6] View Revenue Report");
            System.out.println("[7] View Vehicles by Fuel type");
            System.out.println("[8] View Combined fleet & Customer Report");
            System.out.println("[9] View All Customers");
            System.out.println("[10]View All rental agreements ");
            System.out.println("[0] Logout");
            System.out.print("\nENTER CHOICE         -> ");

            String choice = scanner.nextLine();
            switch (choice) {
                case "1":
                    addVehicle();
                    break;

                case "2":
                    addCustomer();
                    break;
                case "3":
                    viewAvailbleVehicle();
                    break;
                case "4":
                    createRentalAgrement();
                    break;

                case "5":
                    returnVehicle();
                    break;
                case "6":
                    viewRevenueReport();
                    break;
                case "7":
                    viewVehicleByFuelType();
                    break;
                case "8":
                    combineFleetCustomerReport();
                    break;
                case "9":
                    viewAllCustomer();
                    break;
                case "10":
                    viewAllRenatl();
                    break;
                case "0":
                    loggedInUser=null;
                    showAuthMenu();
                    break;
            }
        }
        
    }
    public static void combineFleetCustomerReport(){
        System.out.println("================================================================");
        System.out.println("Combined Fleet & Customer Report");
        System.out.println("================================================================");
        fleetManager.printAllReportables();
    }
    public static void viewRevenueReport(){
        System.out.println("""
                           ================================================================
                           FLEET REVENUE REPORT
                           ================================================================
                           Total Revenue: """ + fleetManager.generateRevenueReport() + " \n"
                + "================================================================");

    }
     
    private static void displayVehicleListInfo(ArrayList<Vehicle> vehicles) {
        for (Vehicle v : vehicles) {

            System.out.println("Vehicle ID         : " + v.getVehicleId());
            System.out.println("Registration number: " + v.getRegistrationNumber());
            System.out.println("Brand              : " + v.getBrand());
            System.out.println("Model              : " + v.getModel());

            if (v instanceof Car) {
                System.out.println("Type: Car");
            } else if (v instanceof Van) {
                System.out.println("Type: Van");
            } else if (v instanceof Truck) {
                System.out.println("Type: Truck");
            }
            System.out.println("========================================================");
        }
    }
    public static void viewVehicleByFuelType() {
        System.out.println("========================================================");
        System.out.println("FRMS VIEW VEHICLES BY FUEL TYPE | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");
        System.out.println("Fuel Type:\n[1] Petrol\n[2] Diesel\n[3] Electric\n[4] Hybrid\nChoice: ");
        String c = scanner.nextLine();
        switch (c) {
            case "1" -> {
                if (fleetManager.getVehiclesByFuelType(FuelType.PETROL) == null) {
                    System.out.println("No Petrol Vehicles..!\n");
                    return;
                }
                System.out.println("===== Petrol Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.PETROL));
            }
            case "2" -> {
                if (fleetManager.getVehiclesByFuelType(FuelType.DIESEL) == null) {
                    System.out.println("No Diesel Vehicles..!\n");
                    return;
                }
                System.out.println("===== Diesel Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.DIESEL));
            }
            case "3" -> {
                if (fleetManager.getVehiclesByFuelType(FuelType.ELECTRIC) == null) {
                    System.out.println("No Electric Vehicles..!\n");
                    return;
                }
                System.out.println("===== Electric Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.ELECTRIC));
            }
            case "4" -> {
                if (fleetManager.getVehiclesByFuelType(FuelType.HYBRID) == null) {
                    System.out.println("No Hybrid Vehicles..!\n");
                    return;
                }
                System.out.println("===== Hybrid Vehicle List =====");
                displayVehicleListInfo(fleetManager.getVehiclesByFuelType(FuelType.HYBRID));
            }
            default -> System.out.println("Invalid fuel type..!\n");
        }
    }
    public static void viewAllRenatl(){
        ArrayList<RentalAgreement> agre=fleetManager.getAllAgreements();
        if (agre == null) {
            System.out.println("No agreements added yet");
            return;
        }
        for (RentalAgreement ag : agre) {
            System.out.println("ID           :" + ag.getAgreementId());
            System.out.println("CUSTOMER     :" + ag.getCustomer().getFullName());
            System.out.println("RENTAL DAYS  :" + ag.getRentalDays());

        }
    }   
    public static void viewAllCustomer(){
        ArrayList<Customer> cus =fleetManager.getAllCustomers();
        if(cus==null){
            System.out.println("No Customers added yet");
            return;
        }
        for (Customer cu : cus) {
            System.out.println("ID     :"+cu.getCustomerId());
            System.out.println("NAME   :"+cu.getFullName());
            System.out.println("PHONE  :"+cu.getContactNumber());
           
        }
       
    }    
    public static void returnVehicle(){
        System.out.println("================================================================");
        System.out.println("RETURN VEHICLE");
        System.out.println("================================================================");
        System.out.print("Enter Agreement ID : ");
        String id = scanner.nextLine();
        fleetManager.returnVehicle(id);
    }
    public static void createRentalAgrement(){
        try{
            System.out.print("Enter vehicle id  : ");
            String vid = scanner.nextLine();
            System.out.print("Enter customer id : ");
            String cid = scanner.nextLine();
            System.out.print("Enter Rental days : ");
            int days = scanner.nextInt();
            scanner.nextLine();
            RentalAgreement agreement =fleetManager.createRentalAgreement(vid, cid, days);
            
        }catch(NumberFormatException | InsufficientCreditException | VehicleNotAvailableException e){
            
        }    
        
    }    
    public static void viewAvailbleVehicle(){
        for (Vehicle v : fleetManager.getAvailableVehicles()) {
            if(v instanceof Car ){
                System.out.println("CAR     Id :"+v.getVehicleId()+"\tBrand :"+v.getBrand()+"\tModel :"+v.getModel());
            }else if(v instanceof Van){
                System.out.println("VAN     Id :"+v.getVehicleId()+"\tBrand :"+v.getBrand()+"\tModel :"+v.getModel());
            }else if(v instanceof Truck){
                System.out.println("TRUCK   Id :"+v.getVehicleId()+"\tBrand :"+v.getBrand()+"\tModel :"+v.getModel());
            }
        }
        
    }
    public static void addCustomer(){
        System.out.println("========================================================");
        System.out.println("FRMS ADD CUSTOMER | Logged in as: " + loggedInUser.getFullName());
        System.out.println("========================================================");

        System.out.print("Enter full name: ");
        String fullName = scanner.nextLine();
        System.out.print("Enter contact: ");
        String contact = scanner.nextLine();

        while (true) {
            System.out.println("Customer Types: ");
            System.out.println("[1]. Individual");
            System.out.println("[2]. Corporate");
            System.out.print("Choice > ");

            String c = scanner.nextLine();

            switch (c) {
                case "1" -> {
                    System.out.print("Enter loyal points: ");
                    int loyalPoints = scanner.nextInt();
                    scanner.nextLine();
                    IndividualCustomer ic = new IndividualCustomer(fullName, contact, loyalPoints);
                    fleetManager.addCustomer(ic);
                    return;
                }
                case "2" -> {
                    System.out.print("Enter credit limit: ");
                    double cl = scanner.nextDouble();
                    scanner.nextLine();
                    CorporateCustomer cc = new CorporateCustomer(cl, fullName, contact);
                    fleetManager.addCustomer(cc);
                    return;
                }
                default ->{
                    System.out.println("Invalid choice..!");
                    break;
                }    
            }
        }

    }
    
    public static void addVehicle(){
        System.out.println("========================================================");
        System.out.println("FRMS ADD VEHICLE | Logged in as :"+loggedInUser.getFullName());
        System.out.println("========================================================");
        
        System.out.print("Enter REG Number : ");
        String regnumber = scanner.nextLine();
        System.out.print("Enter the Brand  : ");
        String brand = scanner.nextLine();
        System.out.print("Enter the Model  : ");
        String model = scanner.nextLine();
        System.out.print("Enter Daily Rate : ");
        double rate = scanner.nextDouble();
        scanner.nextLine();
        
       
        FuelType type = null;
        Integer i;
        while(type==null){
            System.out.println("Fuel Type:\n[1] Petrol\n[2] Diesal\n[3] Electric\n[4] Hybrid");
            System.out.print("SELECT TYPE    : ");
            String choice = scanner.nextLine();
            switch(choice){
                case "1"-> type=type.PETROL;
                case "2"-> type=type.DIESEL;
                case "3"-> type=type.ELECTRIC;
                case "4"-> type=type.HYBRID;
                default -> System.out.println("Invalid choice");
            }
            
        } 
        while(true){
            System.out.println("VEHICLE TYPE  : ");
            System.out.println("[1] CAR");
            System.out.println("[2] VAN");
            System.out.println("[3] TRUCK");
            System.out.print("CHOICE        :");
            String cho = scanner.nextLine();
            
            switch(cho){
                case "1":
                    Car car = new Car(regnumber,brand, model ,rate,type);
                    fleetManager.addVehicle(car);
                    return;
                case "2":
                    int seats=0;
                    try{
                        System.out.print("Enter passenger seats : ");
                        seats = scanner.nextInt();
                        scanner.nextLine();
                    }catch(Exception e){
                        System.out.println(e.getMessage());
                    }
                    
                    Van van = new Van(regnumber,brand, model ,rate,type,seats);
                    fleetManager.addVehicle(van);
                    return;
                case "3":
                    System.out.print("Enter the Cargo Capacity(TONS)  :");
                    double capacity = scanner.nextDouble();
                    Truck truck = new Truck(capacity,regnumber,brand, model ,rate,type);
                    fleetManager.addVehicle(truck);
                    return;
                    
                default :
                    System.out.println("Invalid choice");
                    break;
            }
        }
        
    }
    
}
