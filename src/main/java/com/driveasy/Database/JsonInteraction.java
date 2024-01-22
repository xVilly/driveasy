package com.driveasy.Database;

import java.util.ArrayList;

public interface JsonInteraction <T> {
    public void Save(Object object, String destination);
    public ArrayList<T> Get(String destination);
}
