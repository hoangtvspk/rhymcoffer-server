package bui.dev.rhymcaffer.dto.request;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String username;
    private String email;
    private String password;
    private String displayName;
    private String bio;
    private String imageUrl;
}