package com.organization.jeptagsalpha.ui.activity.authorization;



public class UserLoginDetails {


    private String entity_id;
    private String firstname;
    private String lastname;
    private String email;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    public boolean isSavelogin() {
        return savelogin;
    }

    public void setSavelogin(boolean savelogin) {
        this.savelogin = savelogin;
    }

    private boolean savelogin;

    public UserLoginDetails(String entity_id, String firstname, String lastname, String email,String password, boolean savelogin){

         this.entity_id=entity_id;
        this.firstname=firstname;
        this.lastname=lastname;
        this.email=email;
        this.password=password;
        this.savelogin=savelogin;
    }

    public UserLoginDetails(){}

    public void setEntity_id(String entity_id) {
        this.entity_id = entity_id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEntity_id() {

        return entity_id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }


}
