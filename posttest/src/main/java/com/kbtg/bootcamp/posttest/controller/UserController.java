package com.kbtg.bootcamp.posttest.controller;

import com.kbtg.bootcamp.posttest.dto.UserDto;
import com.kbtg.bootcamp.posttest.model.User;
import com.kbtg.bootcamp.posttest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @Operation(summary = "get member")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "get member",
                    content = {
                            @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = String.class)))
                    })
    })
    @PreAuthorize("hasAuthority('USER_READ') or hasAuthority('USER_MANAGE') or hasRole('ADMIN')")
    @GetMapping("")
    public ResponseEntity getUserResource() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/{username}")
    public ResponseEntity getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.findByUsername(username));
    }

//    @PreAuthorize("hasAuthority('USER_CREATE') or hasAuthority('USER_MANAGE') or hasRole('ADMIN')")
    @PostMapping("")
    public ResponseEntity createUserResource(@RequestBody UserDto request) throws BadRequestException {

        //TODO improve return value
        return new ResponseEntity(userService.createUserResource(request), HttpStatus.CREATED);
    }
}



