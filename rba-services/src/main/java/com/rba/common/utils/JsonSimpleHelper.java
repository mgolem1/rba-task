package com.rba.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rba.common.exceptions.AppError;
import com.rba.common.exceptions.AppException;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

public class JsonSimpleHelper {
    /**
     * main mapper object
     */
    public static ObjectMapper objmap = new ObjectMapper();

    static {
        // excludes null values from serialisation
        objmap.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objmap.registerModule(new JavaTimeModule());
    }

    public static <T> T deserialise(String payload, Class<T> objectClass) throws AppException {
        if (payload == null) return null;
        try {
            return objmap.readValue(payload, objectClass);
        } catch (IOException exc) {
            // JsonParseException | JsonMappingException are both caught by IOException
            throw new AppException(AppError.JSON_PARSE_ERROR, exc);
        }
    }

    public static <T> T deserialise(String payload, TypeReference<T> valueTypeRef) throws AppException {
        if (payload == null) return null;
        try {
            return objmap.readValue(payload, valueTypeRef);
        } catch (IOException exc) {
            // JsonParseException | JsonMappingException are both caught by IOException
            throw new AppException(AppError.JSON_PARSE_ERROR, exc);
        }
    }

    public static <T> T deserialise(JsonNode node, Class<T> objectClass) throws AppException {
        if (node == null) return null;
        try {
            return objmap.treeToValue(node, objectClass);
        } catch (IOException exc) {
            // JsonParseException | JsonMappingException are both caught by IOException
            throw new AppException(AppError.JSON_PARSE_ERROR, exc);
        }
    }

    /**
     * to deserialise list, use following call:
     * JsonSimpleHelper.deserialise(listNode, new TypeReference<List<String>>() {});
     */
    public static <T> T deserialise(JsonNode node, TypeReference<T> valueTypeRef) throws AppException {
        if (node == null) return null;
        try {
            return objmap.readValue(objmap.treeAsTokens(node), objmap.getTypeFactory().constructType(valueTypeRef));
        } catch (IOException exc) {
            // JsonParseException | JsonMappingException are both caught by IOException
            throw new AppException(AppError.JSON_PARSE_ERROR, exc);
        }
    }

    /**
     * serialises the given object and returns the result;
     */
    public static String serialise(Object obj) throws AppException {
        try {
            return objmap.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException exc) {
            // JsonParseException | JsonMappingException are both caught by IOException
            throw new AppException(AppError.JSON_PARSE_ERROR, exc);
        }
    }

    /**
     * turns the given object into a JSON tree;
     */
    public static JsonNode turnIntoJsonTree(Object obj) {
        return objmap.valueToTree(obj);
    }

    /**
     * turns the given json string into a JSON tree;
     */
    public static JsonNode turnIntoJsonTree(String jsonString) throws AppException {
        try {
            return ((jsonString == null) || jsonString.trim().isEmpty()) ? null : objmap.readTree(jsonString);
        } catch (Exception exc) {
            throw new AppException(AppError.JSON_PARSE_ERROR, exc);
        }
    }

    /**
     * add a branch object to main object on variable/node called var and return in json form;
     */
    public static String combineAndSerialise(Object mainObject, String var, Object branchObject) throws AppException {
        JsonNode main = turnIntoJsonTree(mainObject);
        JsonNode branch = turnIntoJsonTree(branchObject);
        JsonTreeHelper.addElementToJsonTree(main, var, branch);
        try {
            return objmap.writerWithDefaultPrettyPrinter().writeValueAsString(main);
        } catch (IOException exc) {
            // JsonParseException | JsonMappingException are both caught by IOException
            throw new AppException(AppError.JSON_PARSE_ERROR, exc);
        }
    }

    /**
     * creates an object of the given class from the value of json message on key var;
     */
    public static Object extractBranchObject(String serialisedObject, String var, Class<?> type) throws AppException {
        try {
            JsonNode tree = objmap.readTree(serialisedObject);
            JsonNode branch = tree.path(var);
            Object obj = objmap.treeToValue(branch, type);
            return obj;
        } catch (IOException exc) {
            // JsonParseException | JsonMappingException are both caught by IOException
            throw new AppException(AppError.JSON_PARSE_ERROR, exc);
        }
    }

    /* <filter functionality> */

    /**
     * create a filter to exclude listed node during serialisation;
     */
    public static FilterProvider createFilter(String filterName, String propertyToExclude) {
        return new SimpleFilterProvider().addFilter(filterName, SimpleBeanPropertyFilter.serializeAllExcept(propertyToExclude));
    }

    /**
     * create a filter to exclude listed nodes during serialisation;
     */
    public static FilterProvider createFilter(String filterName, List<String> propertiesToExclude) {
        return new SimpleFilterProvider().addFilter(filterName, SimpleBeanPropertyFilter.serializeAllExcept(new HashSet<String>(propertiesToExclude)));
    }

    /**
     * serialises the given object while using the provided filter and returns the result;
     */
    public static String serialise(Object obj, FilterProvider filter) throws AppException {
        try {
            return objmap.writer(filter).writeValueAsString(obj);
        } catch (IOException exc) {
            // JsonParseException | JsonMappingException are both caught by IOException
            throw new AppException(AppError.JSON_PARSE_ERROR, exc);
        }
    }

    /**
     * add a branch object to main object on variable/node called var and return in json form while using the provided filter;
     */
    public static String combineAndSerialise(Object mainObject, String var, Object branchObject, FilterProvider filter) throws AppException {
        JsonNode main = turnIntoJsonTree(mainObject);
        JsonNode branch = turnIntoJsonTree(branchObject);
        JsonTreeHelper.addElementToJsonTree(main, var, branch);
        try {
            return objmap.writer(filter).writeValueAsString(main);
        } catch (IOException exc) {
            // JsonParseException | JsonMappingException are both caught by IOException
            throw new AppException(AppError.JSON_PARSE_ERROR, exc);
        }
    }

    /* </filter functionality> */
}
