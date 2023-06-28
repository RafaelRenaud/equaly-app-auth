package com.br.equaly.auth.app.util;

public class Constants {

    //JWT Claims Constants
    public static final String EQUALY_ISSUER = "eQualy Authentication Service";
    public static final String EQUALY_AUDIENCE = "equaly.api.com.br";
    public static final String EQUALY_APPLICATION_KEY = "application_key";
    public static final String EQUALY_CORPORATION_NAME = "corporation_name";
    public static final String EQUALY_USER_DEPARTMENT = "user_department";
    public static final String EQUALY_USER_NAME = "user_name";
    public static final String EQUALY_USER_ALIAS = "user_nickname";
    public static final String EQUALY_USER_ROLE = "user_role";
    public static final String EQUALY_USER_SUBROLE = "user_subrole";

    //Token Constants
    public static final String EQUALY_JWT_ID = "jti";
    public static final String EQUALY_JWT_CONTENT = "jwt";
    public static final String EQUALY_JWT_SUBJECT = "sub";
    public static final String EQUALY_TOKEN_PREFIX = "Bearer ";
}
