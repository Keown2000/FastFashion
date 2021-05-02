package keown.cantrell.grade.activities;
                                                                                                    //this userhelperclass is responsible for preparing and organising the data for contact activity
public class UserHelperClass1 {


String name, message, email, phoneNo, password;                                                        //calling strings



    public UserHelperClass1(String name, String message, String email, String phoneNo) {
        this.name = name;
        this.message = message;
        this.email = email;
        this.phoneNo = phoneNo;
    }

    public String getName() { return name; }                                            //public class that gets name and returns it as name

    public void setName(String name) { this.name = name; }                              //public class that set the name as name and returns nothing

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {this.message = message;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }}
