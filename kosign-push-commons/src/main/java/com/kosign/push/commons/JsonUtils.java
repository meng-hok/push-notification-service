package com.kosign.push.commons;

import com.kosign.push.logging.AppLogManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;

public class JsonUtils {
	public static final String JSON_V7_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-07/schema#";
	public static final String JSON_V6_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-06/schema#";
	public static final String JSON_V4_SCHEMA_IDENTIFIER = "http://json-schema.org/draft-04/schema#";
	public static final String JSON_SCHEMA_IDENTIFIER_ELEMENT = "$schema";

	public static JsonNode getJsonNode(String jsonText) throws IOException {
		return JsonLoader.fromString(jsonText);
	}

	public static JsonNode getJsonNode(File jsonFile) throws IOException {
		return JsonLoader.fromFile(jsonFile);
	}

	public static JsonNode getJsonNode(URL url) throws IOException {
		return JsonLoader.fromURL(url);
	}

	public static JsonNode getJsonNodeFromResource(String resource) throws IOException {
		return JsonLoader.fromResource(resource);
	}

	public static JsonSchema getSchemaNode(String schemaText) throws IOException, ProcessingException {
		JsonNode schemaNode = getJsonNode(schemaText);
		return _getSchemaNode(schemaNode);
	}

	public static JsonSchema getSchemaNode(File schemaFile) throws IOException, ProcessingException {
		JsonNode schemaNode = getJsonNode(schemaFile);
		return _getSchemaNode(schemaNode);
	}

	public static JsonSchema getSchemaNode(URL schemaFile) throws IOException, ProcessingException {
		JsonNode schemaNode = getJsonNode(schemaFile);
		return _getSchemaNode(schemaNode);
	}

	public static JsonSchema getSchemaNodeFromResource(String resource) throws IOException, ProcessingException {
		JsonNode schemaNode = getJsonNodeFromResource(resource);
		return _getSchemaNode(schemaNode);
	}

	public static void validateJson(JsonSchema jsonSchemaNode, JsonNode jsonNode) throws ProcessingException {
		ProcessingReport report = jsonSchemaNode.validate(jsonNode);
		if (!(report.isSuccess())) {
			Iterator i$ = report.iterator();
			if (!(i$.hasNext()))
				return;
			ProcessingMessage processingMessage = (ProcessingMessage) i$.next();
			throw new ProcessingException(processingMessage);
		}
	}

	public static boolean isValidJson(JsonSchema jsonSchemaNode, JsonNode jsonNode) throws ProcessingException {
		ProcessingReport report = jsonSchemaNode.validateUnchecked(jsonNode);

		if (report.isSuccess()) {
			return true;
		}
		for (ProcessingMessage processingMessage : report) {
			if (processingMessage.getLogLevel() != LogLevel.WARNING) {
				AppLogManager.error(processingMessage.toString());
			}
		}

		return false;
	}

	public static boolean isValidJson(String schemaText, String jsonText) throws ProcessingException, IOException {
		JsonSchema schemaNode = getSchemaNode(schemaText);
		JsonNode jsonNode = getJsonNode(jsonText);
		return isValidJson(schemaNode, jsonNode);
	}

	public static boolean isValidJson(File schemaFile, String jsonText) throws ProcessingException, IOException {
		JsonSchema schemaNode = getSchemaNode(schemaFile);
		JsonNode jsonNode = getJsonNode(jsonText);
		return isValidJson(schemaNode, jsonNode);
	}

	public static boolean isValidJson(File schemaFile, File jsonFile) throws ProcessingException, IOException {
		JsonSchema schemaNode = getSchemaNode(schemaFile);
		JsonNode jsonNode = getJsonNode(jsonFile);
		return isValidJson(schemaNode, jsonNode);
	}

	public static boolean isValidJson(URL schemaURL, URL jsonURL) throws ProcessingException, IOException {
		JsonSchema schemaNode = getSchemaNode(schemaURL);
		JsonNode jsonNode = getJsonNode(jsonURL);
		return isValidJson(schemaNode, jsonNode);
	}

	public static void validateJson(String schemaText, String jsonText) throws IOException, ProcessingException {
		JsonSchema schemaNode = getSchemaNode(schemaText);
		JsonNode jsonNode = getJsonNode(jsonText);
		validateJson(schemaNode, jsonNode);
	}

	public static void validateJson(File schemaFile, File jsonFile) throws IOException, ProcessingException {
		JsonSchema schemaNode = getSchemaNode(schemaFile);
		JsonNode jsonNode = getJsonNode(jsonFile);
		validateJson(schemaNode, jsonNode);
	}

	public static void validateJson(URL schemaDocument, URL jsonDocument) throws IOException, ProcessingException {
		JsonSchema schemaNode = getSchemaNode(schemaDocument);
		JsonNode jsonNode = getJsonNode(jsonDocument);
		validateJson(schemaNode, jsonNode);
	}

	public static void validateJsonResource(String schemaResource, String jsonResource)
			throws IOException, ProcessingException {
		JsonSchema schemaNode = getSchemaNode(schemaResource);
		JsonNode jsonNode = getJsonNodeFromResource(jsonResource);
		validateJson(schemaNode, jsonNode);
	}

	private static JsonSchema _getSchemaNode(JsonNode jsonNode) throws ProcessingException {
		JsonNode schemaIdentifier = jsonNode.get("$schema");
		if (null == schemaIdentifier) {
			((ObjectNode) jsonNode).put("$schema", "http://json-schema.org/draft-07/schema#");
		}

		JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
		return factory.getJsonSchema(jsonNode);
	}
}