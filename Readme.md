# spring-adk-demo

Spring Boot demo app that runs a Google ADK `LlmAgent` behind a REST API and connects the agent to an MCP tool server via `McpToolset` \(Streamable HTTP/SSE\).

## What this project does

- Starts a Spring Boot web server \(`server.port: 8081`\)
- Creates an ADK `InMemoryRunner` bean that runs an agent \(`HelloAgent.ROOT_AGENT`\)
- Exposes an API endpoint to send a prompt to the agent and return the final response
- Connects to an MCP server \(default: `http://localhost:8080/mcp`\) to discover and use tools

## Tech stack

- Java 21
- Spring Boot 3\.2\.1
- Maven
- Google ADK `0.5.0`
- Spring AI MCP client starter \(`spring-ai-starter-mcp-client`\)

## Project structure

- `src/main/java/com/example/adk/SpringAdkDemoApplication.java` \- Spring Boot entry point
- `src/main/java/com/example/adk/agent/HelloAgent.java` \- builds the ADK `LlmAgent` and MCP toolset
- `src/main/java/com/example/adk/config/AdkConfig.java` \- provides the `InMemoryRunner` bean
- `src/main/java/com/example/adk/controller/AgentController.java` \- REST controller for running the agent
- `src/main/resources/application.yml` \- server/app configuration

## Prerequisites

- Java 21 installed
- Maven installed
- An MCP server running at `http://localhost:8080/mcp` \(or update the URL in `HelloAgent`\)

## Configuration

### Server port

Configured in `src/main/resources/application.yml`:

- App runs on port `8081`

### MCP server URL

Configured in `src/main/java/com/example/adk/agent/HelloAgent.java`:

- Default MCP endpoint: `http://localhost:8080/mcp`

## Run locally

```bash
mvn spring-boot:run