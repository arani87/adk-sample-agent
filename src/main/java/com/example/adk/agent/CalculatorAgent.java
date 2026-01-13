package com.example.adk.agent;

import com.google.adk.agents.BaseAgent;
import com.google.adk.agents.LlmAgent;
import com.google.adk.tools.BaseTool;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class CalculatorAgent {

    public static BaseAgent ROOT_AGENT;

    private final List<McpSyncClient> mcpSyncClients;

    @Autowired
    public CalculatorAgent(List<McpSyncClient> mcpSyncClients) {
        this.mcpSyncClients = mcpSyncClients;
    }

    @PostConstruct
    public void init() {
        ROOT_AGENT = initAgent();
    }

    private BaseAgent initAgent() {
        // Flatten tools from all connected MCP servers
        List<BaseTool> mcpToolset = mcpSyncClients.stream()
                .flatMap(client -> client.listTools().tools().stream()
                        .map(mcpTool -> new BaseTool(mcpTool.name(), mcpTool.description()) {
                            public Object getInputSchema() {
                                return mcpTool.inputSchema();
                            }

                            public String call(Map<String, Object> arguments) {
                                var response = client.callTool(
                                        new McpSchema.CallToolRequest(mcpTool.name(), arguments)
                                );
                                return response.content().get(0).toString();
                            }
                        }))
                .collect(Collectors.toList());

        return LlmAgent.builder()
                .name("calculator-agent")
                .description("Agent that can perform math operations.")
                .instruction("You are a math assistant. Use tools for calculations.")
                .model("gemini-2.5-flash") // ADK 0.5.0 supports 2.0
                .tools(mcpToolset)
                .build();
    }
}