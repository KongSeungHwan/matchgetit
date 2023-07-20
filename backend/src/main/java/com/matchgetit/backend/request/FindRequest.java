package com.matchgetit.backend.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FindRequest {
    private String name;
    private String email;
    private String temporaryPw;
    private String pn;
}
