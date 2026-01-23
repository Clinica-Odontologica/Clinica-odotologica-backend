package com.joao.dev.clinica_odontologica.config;

public class Constant {
    // API Version
    public static final String API_VERSION = "/api/v1";

    // GlobalResponse - error messages
    public static final String GR_ERROR_NO_HANDLER = "El endpoint solicitado no existe";
    public static final String GR_ERROR_PARAMETER_TYPE = "El parámetro '%s' debe ser de tipo '%s'";
    public static final String GR_ERROR_DETAILS = "[%s] %s";

    // tables
    public static final String TABLE_USUARIOS = "usuarios";
    public static final String AUTH = "auth";
    public static final String TABLE_PATIENTS = "pacientes";
    public static final String TABLE_DOCTOR = "doctores";
    public static final String TABLE_TURN = "turnos";
    public static final String TABLE_TREATMENT = "tratamientos";
    public static final String TABLE_CLINICAL = "clinica";

}
