package com.seminar.pendaftaran.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendaftaranRequest {
    private String fullName;
    private String email;
    private String instance;
    private String topicId; // ID topik yang dipilih
    private String paymentMethod;
}