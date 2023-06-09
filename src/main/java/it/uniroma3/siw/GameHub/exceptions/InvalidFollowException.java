package it.uniroma3.siw.GameHub.exceptions;

public class InvalidFollowException extends Exception {
    public InvalidFollowException(String s) {
        super(s);
        System.out.println("InvalidFollowException: " + s);
    }
}
