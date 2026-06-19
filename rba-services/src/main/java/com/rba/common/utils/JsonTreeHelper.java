package com.rba.common.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonTreeHelper
{
    /**
     * creates an empty jsonnode object, so it can be expanded into a tree;
     */
    public static JsonNode getEmptyJsonTree()
    {
        JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
        return nodeFactory.objectNode();
    }

    /**
     * adds an empty object into a JsonTree;
     * @return the node just created
     */
    public static JsonNode addEmptyObjToJsonTree(JsonNode mainTree, String nodeName)
    {
        if (mainTree.has(nodeName))
        {
            ((ObjectNode) mainTree).remove(nodeName);
        }
        ObjectNode newNode = (ObjectNode) getEmptyJsonTree();
        ((ObjectNode) mainTree).put(nodeName, newNode);
        return newNode;
    }

    /**
     * adds an int value to the jsontree;
     */
    public static void addElementToJsonTree(JsonNode mainTree, String nodeName, boolean nodeValue)
    {
        if (mainTree.has(nodeName))
        {
            ((ObjectNode) mainTree).remove(nodeName);
        }
        ((ObjectNode) mainTree).put(nodeName, nodeValue);
    }

    /**
     * adds an int value to the jsontree;
     */
    public static void addElementToJsonTree(JsonNode mainTree, String nodeName, int nodeValue)
    {
        if (mainTree.has(nodeName))
        {
            ((ObjectNode) mainTree).remove(nodeName);
        }
        ((ObjectNode) mainTree).put(nodeName, nodeValue);
    }

    /**
     * adds a string value to the jsontree;
     */
    public static void addElementToJsonTree(JsonNode mainTree, String nodeName, String nodeValue)
    {
        if (mainTree.has(nodeName))
        {
            ((ObjectNode) mainTree).remove(nodeName);
        }
        ((ObjectNode) mainTree).put(nodeName, nodeValue);
    }

    /**
     * obtains the boolean value of a given node;
     */
    public static Boolean getBoolFromJsonTree(JsonNode mainTree, String nodeName)
    {
        return getBoolFromJsonTree(mainTree, nodeName, null);
    }

    /**
     * obtains the integer value of a given node;
     */
    public static Integer getIntFromJsonTree(JsonNode mainTree, String nodeName)
    {
        return getIntFromJsonTree(mainTree, nodeName, null);
    }

    /**
     * obtains the integer value of a given node;
     */
    public static Integer getIntFromJsonTree(JsonNode mainTree, int nodeIndex)
    {
        return getIntFromJsonTree(mainTree, nodeIndex, null);
    }

    /**
     * obtains the String value of a given node;
     */
    public static String getStringFromJsonTree(JsonNode mainTree, String nodeName)
    {
        return getStringFromJsonTree(mainTree, nodeName, null);
    }

    /**
     * obtains the boolean value of a given node;
     * returns defaultVal if node does not exist;
     */
    public static Boolean getBoolFromJsonTree(JsonNode mainTree, String nodeName, Boolean defaultVal)
    {
        if ((mainTree == null) || !mainTree.has(nodeName)) return defaultVal;
        JsonNode node = mainTree.path(nodeName);
        if (node.isNull()) return defaultVal;
        return node.asBoolean();
    }

    /**
     * obtains the integer value of a given node;
     * returns defaultVal if node does not exist;
     */
    public static Integer getIntFromJsonTree(JsonNode mainTree, String nodeName, Integer defaultVal)
    {
        if ((mainTree == null) || !mainTree.has(nodeName)) return defaultVal;
        JsonNode node = mainTree.path(nodeName);
        if (node.isNull()) return defaultVal;
        return node.asInt();
    }

    /**
     * obtains the integer value of a given node;
     * returns defaultVal if node does not exist;
     */
    public static Integer getIntFromJsonTree(JsonNode mainTree, Integer nodeIndex, Integer defaultVal)
    {
        if ((mainTree == null) || !mainTree.has(nodeIndex)) return defaultVal;
        JsonNode node = mainTree.path(nodeIndex);
        if (node.isNull()) return defaultVal;
        return node.asInt();
    }

    public static Long getLongFromJsonTree(JsonNode mainTree, String nodeName)
    {
        return getLongFromJsonTree(mainTree, nodeName, null);
    }

    public static Long getLongFromJsonTree(JsonNode mainTree, String nodeName, Long defaultVal)
    {
        if ((mainTree == null) || !mainTree.has(nodeName)) return defaultVal;
        JsonNode node = mainTree.path(nodeName);
        if (node.isNull()) return defaultVal;
        return node.asLong();
    }

    public static Double getDoubleFromJsonTree(JsonNode mainTree, String nodeName)
    {
        return getDoubleFromJsonTree(mainTree, nodeName, null);
    }

    public static Double getDoubleFromJsonTree(JsonNode mainTree, String nodeName, Double defaultVal)
    {
        if ((mainTree == null) || !mainTree.has(nodeName)) return defaultVal;
        JsonNode node = mainTree.path(nodeName);
        if (node.isNull()) return defaultVal;
        return node.asDouble();
    }

    /**
     * obtains the String value of a given node;
     * returns defaultVal if node does not exist;
     */
    public static String getStringFromJsonTree(JsonNode mainTree, String nodeName, String defaultVal)
    {
        if ((mainTree == null) || !mainTree.has(nodeName)) return defaultVal;
        JsonNode node = mainTree.path(nodeName);
        if (node.isNull()) return defaultVal;
        return node.asText();
    }

    /**
     * adds a branch json tree to the main json tree structure;
     */
    public static void addElementToJsonTree(JsonNode mainTree, String nodeName, JsonNode branch)
    {
        if (mainTree.has(nodeName))
        {
            ((ObjectNode) mainTree).remove(nodeName);
        }
        ((ObjectNode) mainTree).put(nodeName, branch);
    }

    public static JsonNode getNodeFromTree(JsonNode mainTree, String nodeName)
    {
        JsonNode result = null;

        if (mainTree != null && mainTree.has(nodeName))
        {
            result = mainTree.path(nodeName);
        }

        return result;
    }
}
