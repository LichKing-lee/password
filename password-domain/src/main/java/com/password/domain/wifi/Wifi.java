package com.password.domain.wifi;

import com.password.domain.store.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "wifi")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Wifi {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wifi_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Column(name = "ssid", nullable = false)
    private String ssid;

    @Column(name = "password")
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public static Wifi open(Store store, String ssid) {
        return new Wifi(store, ssid, null);
    }

    public static Wifi secured(Store store, String ssid, String password) {
        return new Wifi(store, ssid, password);
    }

    private Wifi(Store store, String ssid, String password) {
        this.store = store;
        this.ssid = ssid;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public boolean isOpen() {
        return password == null;
    }

    public void updatePassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }
}
