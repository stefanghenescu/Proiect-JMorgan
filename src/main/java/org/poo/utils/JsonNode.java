package org.poo.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.fileio.CommandInput;

public class JsonNode {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    public static ObjectNode writeUsers(CommandInput command) {
        ObjectNode usersArray = MAPPER.createObjectNode();

        usersArray.put("command", command.getCommand());
        return usersArray;
    }
}
