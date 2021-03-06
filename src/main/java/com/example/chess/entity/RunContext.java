package com.example.chess.entity;

import com.example.chess.entity.base.Room;
import lombok.Getter;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by chanming on 16/7/21.
 */
public class RunContext {
    private @Getter
    ConcurrentHashMap<String, Room> rooms;

    public RunContext(ConcurrentHashMap<String, Room> rooms) {
        this.rooms = rooms;
    }
}
