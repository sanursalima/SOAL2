package com.seminar.pendaftaran.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendaftaranResponse {
    private String message;
    private String registrationId; // ID pendaftaran yang dihasilkan (opsional)
}