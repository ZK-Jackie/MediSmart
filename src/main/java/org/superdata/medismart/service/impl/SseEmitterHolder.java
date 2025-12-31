package org.superdata.medismart.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class SseEmitterHolder {
    private static final ConcurrentHashMap<String, SseEmitter> contextHolder = new ConcurrentHashMap<>();

    public static SseEmitter getConnect(String sessionId) {
        SseEmitter context = contextHolder.get(sessionId);
        if (context == null) {
            log.warn("SseEmitter not found for sessionId: {} when getting it", sessionId);
        }
        return context;
    }

    public static void createConnect(String sessionId, SseEmitter sseEmitter) {
        if (sseEmitter == null) {
            log.warn("SseEmitter is null for sessionId: {}", sessionId);
            return;
        }
        if (contextHolder.containsKey(sessionId)) {
            log.warn("SseEmitter already exists for sessionId: {}", sessionId);
            return;
        }
        contextHolder.put(sessionId, sseEmitter);
        log.info("SseEmitter created for sessionId: {}", sessionId);
    }

    public static void removeConnect(String sessionId) {
        if (contextHolder.containsKey(sessionId)) {
            contextHolder.remove(sessionId);
            log.info("SseEmitter removed for sessionId: {}", sessionId);
        } else {
            log.warn("SseEmitter not found for sessionId: {} when deleting it", sessionId);
        }
    }

    public static void clearConnect() {
        contextHolder.clear();
        log.info("SseEmitter context cleared");
    }

    public static boolean existsConnect(String sessionId) {
        return contextHolder.containsKey(sessionId);
    }
}
