package com.librarymanagementsystem;

import com.librarymanagementsystem.data.User;
import com.librarymanagementsystem.response.Response;
import org.hibernate.Session;

import java.util.LinkedList;
import java.util.List;

public final class UserUtil {

    private final Session session;

    public UserUtil(Session session) {
        this.session = session;
    }

    public Response addMinimalUser(String name, String phoneNumber) {
        int code = 200;
        String message = "added";
        List<String> errorMessages = new LinkedList<>();

        InternalResponse ir = errorCheck(name, phoneNumber);

        if (ir.isError()) {
            return new Response(ir.getCode(), ir.getMessage(), ir.getErrorMessages());
        }

        session.persist(new User(name, phoneNumber));
        session.beginTransaction().commit();
        return new Response(code, message, errorMessages);
    }

    public Response addCompleteUserDetails(String name, String phoneNumber, String emailAddress, String libraryCard) {
        int code = 200;
        String message = "added";
        List<String> errorMessages = new LinkedList<>();

        InternalResponse ir = errorCheck(name, phoneNumber);

        if (ir.isError()) {
            return new Response(ir.getCode(), ir.getMessage(), ir.getErrorMessages());
        }

        session.persist(new User(name, phoneNumber, emailAddress, libraryCard));
        session.beginTransaction().commit();
        return new Response(code, message, errorMessages);
    }

    private InternalResponse errorCheck(String name, String phoneNumber) {

        InternalResponse ir = new InternalResponse();

        if ((null == name) || name.isBlank()) {
            ir.setCode(400);
            ir.setMessage("error");
            ir.addErrorMessage("Name cannot be null or blank!");
            ir.setError();
        }

        if ((null == phoneNumber) || phoneNumber.isBlank()) {
            ir.setCode(400);
            ir.setMessage("error");
            ir.addErrorMessage("Phone Number cannot be null or blank!");
            ir.setError();
        }

        return ir;
    }

    private static class InternalResponse {
        private int code;
        private String message;
        private final List<String> errorMessages = new LinkedList<>();
        private boolean isError = false;

        public void setCode(int code) {
            this.code = code;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void addErrorMessage(String errorMessage) {
            errorMessages.add(errorMessage);
        }

        public void setError() {
            isError = true;
        }

        public int getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public List<String> getErrorMessages() {
            return errorMessages;
        }

        public boolean isError() {
            return isError;
        }
    }
}
