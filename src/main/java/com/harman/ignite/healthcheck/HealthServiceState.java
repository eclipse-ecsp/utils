package com.harman.ignite.healthcheck;

public class HealthServiceState {

    private double state;
    private String message;

    public double getState() {
        return state;
    }

    public void setState(double state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HealthServiceState() {
    //default constructor
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        long temp;
        temp = Double.doubleToLongBits(state);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        HealthServiceState other = (HealthServiceState) obj;
        if (message == null) {
            if (other.message != null) return false;
        }
        else if (!message.equals(other.message)) return false;
        return (Double.doubleToLongBits(state) == Double.doubleToLongBits(other.state)) ;
    }

}
