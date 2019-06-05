package it.uniroma2.dicii.sdcc.teaching_material_management.error;

public class InternalServerError extends Exception {

    private String message;
    private Throwable cause;

    public InternalServerError(String message, Throwable cause){
        this.message = message;
        this.cause = cause;
    }
}
