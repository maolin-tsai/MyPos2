package com.pos.techicalservices;

public enum DatabaseContents {

    DATABASE("com.pos.db1"),
    TABLE_SALE("sale"),
    TABLE_PRODUCT_CATALOG("product_catalog"),
    TABLE_STOCK("stock"),
    TABLE_SALE_LINEITEM("sale_lineitem"),
    TABLE_STOCK_SUM("stock_sum"),
    LANGUAGE("language");

    private String name;

    private DatabaseContents(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

}
