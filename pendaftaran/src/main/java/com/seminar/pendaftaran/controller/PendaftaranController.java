package com.seminar.pendaftaran.controller;

import com.seminar.pendaftaran.model.PendaftaranRequest;
import com.seminar.pendaftaran.model.PendaftaranResponse;
import com.seminar.pendaftaran.model.Topik;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger; // Menggunakan java.util.logging.Logger

@RestController // Menandakan bahwa ini adalah REST Controller
@RequestMapping("/api") // Base path untuk semua endpoint di controller ini
@CrossOrigin(origins = "http://localhost:8080") // Izinkan akses dari frontend yang berjalan di port 8080 (sesuaikan jika frontend Anda di port lain)
public class PendaftaranController {

    private static final Logger logger = Logger.getLogger(PendaftaranController.class.getName());

    // Data in-memory untuk topik seminar
    private List<Topik> topics = Arrays.asList(
            new Topik("T001", "Pengembangan Web dengan Spring Boot"),
            new Topik("T002", "Deep Learning untuk Pengolahan Citra"),
            new Topik("T003", "Data Science dan Big Data Analytics")
    );

    // List untuk menyimpan pendaftaran (in-memory, akan hilang setelah aplikasi restart)
    private List<PendaftaranRequest> registrations = new ArrayList<>();

    // Endpoint untuk mendapatkan daftar topik seminar
    @GetMapping("/topik")
    public ResponseEntity<List<Topik>> getTopikSeminar() {
        logger.info("Menerima permintaan GET /api/topik");
        return ResponseEntity.ok(topics); // Mengembalikan daftar topik dengan status OK (200)
    }

    // Endpoint untuk mengirim data pendaftaran peserta
    @PostMapping("/pendaftaran")
    public ResponseEntity<PendaftaranResponse> submitPendaftaran(@RequestBody PendaftaranRequest request) {
        logger.info("Menerima permintaan POST /api/pendaftaran dengan data: " + request);

        // --- Lakukan validasi data di sini (jika belum dilakukan di frontend) ---
        if (request.getFullName() == null || request.getFullName().isEmpty()) {
            return ResponseEntity.badRequest().body(new PendaftaranResponse("Nama lengkap wajib diisi.", null));
        }
        if (request.getEmail() == null || !request.getEmail().matches("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")) {
            return ResponseEntity.badRequest().body(new PendaftaranResponse("Email tidak valid.", null));
        }
        if (request.getTopicId() == null || request.getTopicId().isEmpty()) {
            return ResponseEntity.badRequest().body(new PendaftaranResponse("Topik seminar wajib dipilih.", null));
        }
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isEmpty()) {
            return ResponseEntity.badRequest().body(new PendaftaranResponse("Metode pembayaran wajib dipilih.", null));
        }

        // Simulasikan penyimpanan data
        registrations.add(request);
        String registrationId = UUID.randomUUID().toString(); // Contoh ID pendaftaran unik

        logger.info("Pendaftaran berhasil untuk: " + request.getEmail() + ", ID: " + registrationId);
        return ResponseEntity.status(HttpStatus.CREATED) // Mengembalikan status CREATED (201)
                .body(new PendaftaranResponse("Pendaftaran berhasil!", registrationId));
    }

    // Endpoint opsional: melihat semua pendaftaran yang tersimpan (untuk debugging)
    @GetMapping("/pendaftaran/all")
    public ResponseEntity<List<PendaftaranRequest>> getAllRegistrations() {
        logger.info("Menerima permintaan GET /api/pendaftaran/all");
        return ResponseEntity.ok(registrations);
    }
}