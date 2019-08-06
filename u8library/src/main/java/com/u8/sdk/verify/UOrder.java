package com.u8.sdk.verify;


public class UOrder {
    private String order;
    private String extension;
    private String productID;

    public UOrder(String order, String ext, String productID) {
        this.order = order;
        this.extension = ext;
        this.productID = productID;
    }


    public String getOrder() {
        return this.order;
    }


    public void setOrder(String order) {
        this.order = order;
    }


    public String getExtension() {
        return this.extension;
    }


    public void setExtension(String extension) {
        this.extension = extension;
    }


    public String getProductID() {
        return this.productID;
    }


    public void setProductID(String productID) {
        this.productID = productID;
    }
}


