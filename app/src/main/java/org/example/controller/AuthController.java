package org.example.controller;

import org.example.entities.RefreshToken;
import org.example.models.UserInfoDto;
import org.example.response.JwtResponseDto;
import org.example.services.JwtService;
import org.example.services.RefreshTokenService;
import org.example.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
public class AuthController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RefreshTokenService refreshTokenService;

    @Autowired
    private UserService userService;

    @PostMapping("auth/v1/signup")
    public ResponseEntity Signup(@RequestBody UserInfoDto userInfoDto) {
        try {
            Boolean isRegistered = userService.signupUser(userInfoDto);
            if (!isRegistered) {
                return new ResponseEntity<>("There might be invalid email or invalid password or user already exist",
                        HttpStatus.BAD_REQUEST);
            }
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userInfoDto.getUsername());
            String jwtToken = jwtService.GenerateToken(userInfoDto.getUsername());
            return new ResponseEntity<>(JwtResponseDto.builder()
                    .accessToken(jwtToken)
                    .token(refreshToken.getToken())
                    .build(),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Something Went Wrong", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
