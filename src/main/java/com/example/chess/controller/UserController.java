package com.example.chess.controller;

import com.example.chess.entity.base.Result;
import com.example.chess.entity.catan.CatanPlayer;
import com.example.chess.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/query/{token}")
    public Result<CatanPlayer> getUser(@PathVariable("token") String token) {
        CatanPlayer catanPlayer = userService.getUser(token);
        return Result.success(catanPlayer);
    }

    @PostMapping("/update/{token}")
    public Result setUser(@PathVariable("token") String token, @RequestBody String player) {
        return userService.setUser(token, player);
    }
}
