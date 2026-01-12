package com.example.adk.controller;

import com.google.adk.agents.RunConfig;
import com.google.adk.events.Event;
import com.google.adk.runner.InMemoryRunner;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/api/agent")
public class AgentController {

    private final InMemoryRunner runner;

    public AgentController(InMemoryRunner runner) {
        this.runner = runner;
    }

    @PostMapping("/ask")
    public ResponseEntity<String> askAgent(@RequestParam("question") String input,
                                           @RequestHeader(value = "X-Session-Id", required = false) String sessionId) {
        RunConfig config = RunConfig.builder().build();


        Session session;
        if (sessionId != null && !sessionId.isEmpty()) {
            session = runner.sessionService()
                    .getSession(runner.appName(), "web-user", sessionId, Optional.empty())
                    .blockingGet();
            if (session == null) {
                session = runner.sessionService()
                        .createSession(runner.appName(), "web-user")
                        .blockingGet();
            }
        } else {
            session = runner.sessionService()
                    .createSession(runner.appName(), "web-user")
                    .blockingGet();
        }

        Content userContent = Content.fromParts(Part.fromText(input));

        AtomicReference<String> agentReply = new AtomicReference<>("");

        Flowable<Event> flow = runner.runAsync(
                session.userId(), session.id(), userContent, config);

        flow.blockingForEach(event -> {
            if (event.finalResponse()) {
                agentReply.set(event.stringifyContent());
            }
        });

        return ResponseEntity.ok().header("X-Session-Id", session.id()).body(agentReply.get());
    }
}
