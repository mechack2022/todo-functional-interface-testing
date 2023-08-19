package com.fragile.todofunctionalinterface.dto.request;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest  {

    private String email;
    private String password;

}
