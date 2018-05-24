package com.organization.jeptagsalpha.wordpress.model.wc;

import java.util.ArrayList;

/**
 * Created by Lord-Shiva on 10/13/2016.
 */

public class BatchProduct  {
    private ArrayList<Product> update;

    public ArrayList<Product> getUpdate ()
    {
        return update;
    }

    public void setUpdate(ArrayList<Product> arrayProduct)
    {
        this.update = arrayProduct;
    }

}
