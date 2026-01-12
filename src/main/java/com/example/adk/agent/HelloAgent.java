package com.example.adk.agent;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.mcp.McpToolset;
import com.google.adk.tools.mcp.StreamableHttpServerParameters;
import lombok.extern.slf4j.Slf4j;

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

}
