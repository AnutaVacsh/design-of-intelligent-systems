package ru.lama.expertCookingSystem.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.lama.expertCookingSystem.dto.request.LoginRequest;
import ru.lama.expertCookingSystem.dto.request.SignupRequest;
import ru.lama.expertCookingSystem.dto.response.ApiResponse;
import ru.lama.expertCookingSystem.dto.response.AuthResponse;
import ru.lama.expertCookingSystem.util.ApiPaths;

@RequestMapping(ApiPaths.AUTH)
public interface AuthApi {

  @PostMapping(ApiPaths.SIGNUP)
  ResponseEntity<ApiResponse<AuthResponse>> signup(@RequestBody SignupRequest request);

  @PostMapping(ApiPaths.LOGIN)
  ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request);
}

