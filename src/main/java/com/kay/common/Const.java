package com.kay.common;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by kay on 2018/3/19.
 */
public class Const {
    public static final String CURRENT_USER = "current_user";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";

    public interface ROLE {
        int NORMAL_USER = 0;
        int MANAGE_USER = 1;
    }

    public interface ProductListOrderBy{
        Set<String> PRICE_ASC_DESC = Sets.newHashSet("price_desc","price_asc");
    }

    public enum ProductStatusEnum{
        ON_SALE(1,"在售");

        private int code;
        private String value;

        ProductStatusEnum(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode() {
            return code;
        }

        public String getValue() {
            return value;
        }
    }
}
