package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.repository.RoleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Operation(summary = "get roles")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "get roles",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
                    })
    })
    @PreAuthorize("hasAuthority('USER_READ') or hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity getRoleResource() {
        return ResponseEntity.ok(roleRepository.findAll());
    }


}
