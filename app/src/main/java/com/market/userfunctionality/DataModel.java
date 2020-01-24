package com.market.userfunctionality;

import ir.mirrajabi.searchdialog.core.Searchable;

public class DataModel implements Searchable {

    String cat_name;
    public String cat_id;
    String cat_image_uri;

    //product data
    String product_id;
    String product_name;
    String product_image_uri;
    String product_price;
    String product_brand;
    String product_sku;
    String product_des;
//    String cat_id;
//    String cat_name;


    public DataModel() {
    }

    public DataModel(String product_id, String product_name, String product_image_uri,
                     String product_price, String product_sku, String product_brand, String product_des, String cat_name, String cat_id) {
        this.cat_name = cat_name;
        this.cat_id = cat_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_image_uri = product_image_uri;
        this.product_price = product_price;
        this.product_brand = product_brand;
        this.product_sku = product_sku;
        this.product_des = product_des;
    }

    public DataModel(String cat_name, String cat_id, String cat_image_uri) {
        this.cat_name = cat_name;
        this.cat_id = cat_id;
        this.cat_image_uri = cat_image_uri;
    }

    public DataModel(String product_id, String product_name, String product_image_uri, String product_price, String product_brand, String product_des) {
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_image_uri = product_image_uri;
        this.product_price = product_price;
        this.product_brand = product_brand;
        this.product_des = product_des;

    }

    public DataModel(String product_id, String product_name, String product_image_uri, String product_price, String product_sku, String product_brand, String product_des, String cat_id) {

        this.product_id = product_id;
        this.product_name = product_name;
        this.product_image_uri = product_image_uri;
        this.product_price = product_price;
        this.product_brand = product_brand;
        this.product_des = product_des;
        this.product_sku = product_sku;

    }

    public DataModel(ProductAdapter adapter_product) {

    }


    public String getCat_name() {
        return cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_image_uri() {
        return cat_image_uri;
    }

    public void setCat_image_uri(String cat_image_uri) {
        this.cat_image_uri = cat_image_uri;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image_uri() {
        return product_image_uri;
    }

    public void setProduct_image_uri(String product_image_uri) {
        this.product_image_uri = product_image_uri;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getProduct_brand() {
        return product_brand;
    }

    public void setProduct_brand(String product_brand) {
        this.product_brand = product_brand;
    }

    public String getProduct_sku() {
        return product_sku;
    }

    public void setProduct_sku(String product_sku) {
        this.product_sku = product_sku;
    }

    public String getProduct_des() {
        return product_des;
    }

    public void setProduct_des(String product_des) {
        this.product_des = product_des;
    }


    @Override
    public String getTitle() {
        return product_name;
    }
}
