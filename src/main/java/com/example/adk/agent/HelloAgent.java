package com.example.adk.agent;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.Annotations.Schema;
import com.google.adk.tools.mcp.McpToolset;
import com.google.adk.tools.mcp.StreamableHttpServerParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.ToolCallbackProvider;

import java.util.Arrays;
import java.util.Map;

@Slf4j
public class HelloAgent {

    public static final BaseAgent AGENT = initAgent();


    private static BaseAgent initAgent() {
        // 1. Configure Connection Parameters for the Streamable HTTP (SSE) server
        // Replace with your actual MCP server URL and any required headers (e.g., Auth)
        StreamableHttpServerParameters connectionParams = StreamableHttpServerParameters.builder()
                .url("http://localhost:8080/mcp") // MCP server URL
                .build();

        // 2. Initialize the MCP Toolset
        // This toolset will automatically discover all tools from the server
        McpToolset mcpToolset = new McpToolset(connectionParams);

//        return LlmAgent.builder()
//                .name("shipment-agent")
//                .description("Shipment Agent of FedEx. Answers user queries about shipments. Creates and manages shipments.")
//                .instruction("""
//                            You are a FedEx shipment agent. You help users with their shipment-related queries.
//                            You can create shipments, track shipments, and provide information about shipping options.
//                            You can also calculate shipping costs based on weight, dimensions, and destination.
//                            Use the provided tools to assist users with their shipment needs
//                            Make sure to extract all necessary information (as mentioned in the tool schema) from the user before proceeding with any shipment-related tasks.
//                        """)
//                .model("gemini-2.5-flash") // model; change if needed
//                .tools(mcpToolset)
//                .build();

        return LlmAgent.builder()
                .name("calculator-agent")
                .description("Calculator Agent that can perform basic arithmetic operations.")
                .instruction("""
                            You are a helpful calculator agent. You can perform basic arithmetic operations like addition, subtraction, multiplication, and division.
                            Use the provided tools to perform calculations as requested by the user.
                            Make sure to extract all necessary information (as mentioned in the tool schema) from the user before proceeding with any calculations.
                        """)
                .model("gemini-2.5-flash") // model; change if needed
                .tools(mcpToolset)
                .build();
    }

    @Schema(description = "Generate a friendly response")
    public static Map<String, String> simpleEcho(@Schema(name = "query") String query) {
        return Map.of("response", "You asked: " + query);
    }

    @Schema(description = "Get the rate and transit time for a shipment")
    public static Double getRatesAndTransitTime(
            @Schema(name = "source", description = "Source origin of the shipment") String source,
            @Schema(name = "destination", description = "Destination of the shipment") String destination,
            @Schema(name = "weight", description = "Weight of the shipment in pounds") double weight) {
        return 250.0;
    }
}
