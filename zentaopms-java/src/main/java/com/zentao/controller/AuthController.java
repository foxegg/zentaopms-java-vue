package com.zentao.controller;

import com.zentao.security.JwtTokenProvider;
import com.zentao.security.PasswordEncoder;
import com.zentao.security.UserPrincipal;
import com.zentao.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证控制器 - 登录/登出
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        return ResponseEntity.ok(Map.of("result", "success", "message", "已登出"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.account(), request.password()));
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal.getUsername());

        return ResponseEntity.ok(Map.of(
                "result", "success",
                "message", "登录成功",
                "token", token,
                "user", Map.of(
                        "id", principal.getUser().getId(),
                        "account", principal.getUser().getAccount(),
                        "realname", principal.getUser().getRealname() != null ? principal.getUser().getRealname() : "",
                        "role", principal.getUser().getRole() != null ? principal.getUser().getRole() : ""
                )
        ));
    }

    public record LoginRequest(String account, String password) {}
}
