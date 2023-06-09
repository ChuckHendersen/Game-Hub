package it.uniroma3.siw.GameHub.exceptions;

public class UserNotFoundException extends Exception{

    public UserNotFoundException(String s) {
        super(s);
        System.out.println("UserNotFoundException: " + s);
    }
}

