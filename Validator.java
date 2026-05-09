// Paquete de utilidades del proyecto
package com.sakila.utils;

// Importamos Pattern y Matcher para expresiones regulares (requerimiento del proyecto)
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author  Proyecto Final INF514 - Java Programming 2020
 * @version 1.0
 * @since   2020
 *
 * Validator: Clase utilitaria con expresiones regulares para validar datos.
 * Cumple el requerimiento 6e: "Expresiones regulares para fechas, cedulas,
 * teléfonos y otros."
 * Todos los métodos son estáticos — no necesita instanciarse.
 */
public class Validator {

    // -------------------------------------------------------------------------
    // PATRONES DE EXPRESIONES REGULARES (compilados una sola vez para eficiencia)
    // -------------------------------------------------------------------------

    // Patrón para emails: usuario@dominio.extensión
    // ^ = inicio, $ = fin de cadena
    // [A-Za-z0-9+_.-]+ = caracteres válidos para el usuario
    // @ = arroba obligatorio
    // [A-Za-z0-9.-]+ = nombre del dominio
    // \\. = punto literal
    // [A-Za-z]{2,} = extensión de 2 o más letras (.com, .org, .net, etc.)
    private static final Pattern PATRON_EMAIL =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    // Patrón para fechas en formato YYYY-MM-DD (ISO 8601)
    // \\d{4} = exactamente 4 dígitos para el año
    // - = guión literal separador
    // (0[1-9]|1[0-2]) = mes del 01 al 12
    // (0[1-9]|[12][0-9]|3[01]) = día del 01 al 31
    private static final Pattern PATRON_FECHA =
            Pattern.compile("^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$");

    // Patrón para cédulas de identidad dominicanas (formato: NNN-NNNNNNN-N)
    // 3 dígitos - 7 dígitos - 1 dígito verificador
    // \\d{3} = exactamente 3 dígitos
    // - = guión literal
    // \\d{7} = exactamente 7 dígitos
    // \\d{1} = 1 dígito verificador
    private static final Pattern PATRON_CEDULA =
            Pattern.compile("^\\d{3}-\\d{7}-\\d{1}$");

    // Patrón para teléfonos (acepta varios formatos internacionales)
    // [+]? = signo + opcional (para código de país)
    // [(]? = paréntesis abierto opcional
    // [0-9]{1,4} = 1 a 4 dígitos de código de área/país
    // [)]? = paréntesis cerrado opcional
    // [-\\s./0-9]{7,15} = resto del número (puede tener guiones, espacios, puntos)
    private static final Pattern PATRON_TELEFONO =
            Pattern.compile("^[+]?[(]?[0-9]{1,4}[)]?[-\\s./0-9]{7,15}$");

    // Patrón para SSN (Social Security Number de EE.UU.) formato: NNN-NN-NNNN
    // \\d{3} = 3 dígitos del área
    // - = guión literal
    // \\d{2} = 2 dígitos del grupo
    // - = guión literal
    // \\d{4} = 4 dígitos del serial
    private static final Pattern PATRON_SSN =
            Pattern.compile("^\\d{3}-\\d{2}-\\d{4}$");

    // Patrón para nombres (solo letras, espacios, tildes y ñ)
    // [A-Za-zÀ-ÿ]+ = letras incluyendo caracteres con tilde
    // (\\s[A-Za-zÀ-ÿ]+)* = permite espacios entre palabras (nombres compuestos)
    private static final Pattern PATRON_NOMBRE =
            Pattern.compile("^[A-Za-zÀ-ÿ]+(\\s[A-Za-zÀ-ÿ]+)*$");

    // -------------------------------------------------------------------------
    // MÉTODOS DE VALIDACIÓN (todos estáticos para uso sin instanciar)
    // -------------------------------------------------------------------------

    /**
     * validarEmail — Verifica si un email tiene formato válido.
     * Usa la expresión regular PATRON_EMAIL definida arriba.
     *
     * @param email  Cadena de texto con el email a validar
     * @return       true si el email tiene formato válido
     */
    public static boolean validarEmail(String email) {
        if (email == null || email.isEmpty()) return false; // null o vacío no es válido
        // matches() aplica el patrón a toda la cadena
        return PATRON_EMAIL.matcher(email).matches(); // Retorna true si coincide con el patrón
    }

    /**
     * validarFecha — Verifica si una fecha tiene formato YYYY-MM-DD válido.
     *
     * @param fecha  Cadena de texto con la fecha en formato ISO
     * @return       true si la fecha tiene el formato correcto
     */
    public static boolean validarFecha(String fecha) {
        if (fecha == null || fecha.isEmpty()) return false; // null o vacío no es válido
        return PATRON_FECHA.matcher(fecha).matches(); // Verifica contra el patrón de fecha
    }

    /**
     * validarCedula — Verifica el formato de cédula dominicana (NNN-NNNNNNN-N).
     *
     * @param cedula  Número de cédula con guiones
     * @return        true si tiene el formato correcto
     */
    public static boolean validarCedula(String cedula) {
        if (cedula == null || cedula.isEmpty()) return false; // null no es válido
        return PATRON_CEDULA.matcher(cedula).matches(); // Verifica el patrón de cédula
    }

    /**
     * validarTelefono — Verifica si un número de teléfono tiene formato válido.
     * Acepta formatos con código de país, paréntesis y separadores.
     *
     * @param telefono  Número de teléfono a validar
     * @return          true si tiene formato válido
     */
    public static boolean validarTelefono(String telefono) {
        if (telefono == null || telefono.isEmpty()) return false; // null no es válido
        return PATRON_TELEFONO.matcher(telefono).matches(); // Verifica el formato
    }

    /**
     * validarSSN — Verifica el formato de SSN americano (NNN-NN-NNNN).
     *
     * @param ssn  Número de seguro social en formato con guiones
     * @return     true si el formato es correcto
     */
    public static boolean validarSSN(String ssn) {
        if (ssn == null || ssn.isEmpty()) return false; // null no es válido
        return PATRON_SSN.matcher(ssn).matches(); // Verifica contra el patrón SSN
    }

    /**
     * validarNombre — Verifica que un nombre solo contenga letras y espacios.
     * Acepta nombres con tildes y la letra ñ.
     *
     * @param nombre  Texto del nombre a validar
     * @return        true si solo contiene caracteres válidos para nombres
     */
    public static boolean validarNombre(String nombre) {
        if (nombre == null || nombre.isEmpty()) return false; // null no es válido
        return PATRON_NOMBRE.matcher(nombre).matches(); // Verifica solo letras
    }

    /**
     * extraerAnio — Extrae el año de una fecha en formato YYYY-MM-DD.
     * Usa Matcher con grupo de captura para extraer solo el año.
     *
     * @param fecha  Cadena con la fecha en formato YYYY-MM-DD
     * @return       Año como entero, o -1 si el formato no es válido
     */
    public static int extraerAnio(String fecha) {
        if (!validarFecha(fecha)) return -1; // Retorna -1 si el formato no es válido

        // Patrón con grupo de captura para el año: (\\d{4}) captura los 4 primeros dígitos
        Pattern patronConGrupo = Pattern.compile("^(\\d{4})-.*$");
        Matcher matcher = patronConGrupo.matcher(fecha); // Aplica el patrón a la fecha

        if (matcher.matches()) {
            return Integer.parseInt(matcher.group(1)); // Extrae y retorna el año como int
        }
        return -1; // No pudo extraer el año
    }
}
