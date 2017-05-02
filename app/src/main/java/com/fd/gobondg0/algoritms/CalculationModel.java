package com.fd.gobondg0.algoritms;


public abstract class CalculationModel {

    protected ArgsStore mArgsStore;

    public void setArgsStore(ArgsStore store){
        mArgsStore = store;
    }

    abstract public float[] calculateParity() throws NullPointerException;

    abstract public float[] calculateParity(double T) throws NullPointerException;

}
