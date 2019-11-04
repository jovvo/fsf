package com.tlapps.test.fsf.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public final class RegistrationResponse implements Serializable {
    private String message;
}
