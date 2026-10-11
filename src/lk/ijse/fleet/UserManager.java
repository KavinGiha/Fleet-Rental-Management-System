
package lk.ijse.fleet;

import java.util.LinkedList;

public class UserManager {
    public UserManager(){
        
    }
    private LinkedList<SystemUser> users=new LinkedList();

    public UserManager(LinkedList<SystemUser> users) {
        this.users = users;
    }

    public LinkedList<SystemUser> getUsers() {
        return users;
    }

    public void setUsers(SystemUser user) {
        if(user!=null){
            this.users.add(user);            
        }

    }
    
    public SystemUser registerUser(String username, String password, String fullName){
        String use = findByUsername(username);
        if(use!=null){
            SystemUser newuser = new SystemUser(username,password,fullName);
            return newuser;
        }
        
        
        return null;
    }
    
    public SystemUser login(String username, String password){
        for (SystemUser user : users) {
            if(user.getUsername().equals(username)&&user.getPassword().equals(password)){
                return user;
            }
            
        }
    
        return null;
    }
    
    public String findByUsername(String username){
        for (SystemUser user : users) {
            if(user.getUsername().equals(username)){
                System.out.println("Username already exsist");
                return null;
            }    
        }
    
        return username;
    }
}
