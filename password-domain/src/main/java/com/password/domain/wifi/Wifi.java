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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "wifi")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    public Wifi(Store store, String ssid, String password) {
        this.store = store;
        this.ssid = ssid;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public Wifi(Store store, String ssid) {
        this(store, ssid, null);
    }

    public boolean isOpen() {
        return password == null;
    }

    public void updatePassword(String password) {
        this.password = password;
        this.updatedAt = LocalDateTime.now();
    }
}
