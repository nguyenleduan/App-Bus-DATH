package com.google.appbegin;

public class ProfileUser {
    public String Name;
    public String Email;
    public String Phonenumber;
    public String AvatarLink;

    public ProfileUser() {
    }


    public ProfileUser(String avatarLink, String email, String name, String phonenumber) {
        this.Name = name;
        this.Email = email;
        this.Phonenumber = phonenumber;
        this.AvatarLink = avatarLink;
    }
}
