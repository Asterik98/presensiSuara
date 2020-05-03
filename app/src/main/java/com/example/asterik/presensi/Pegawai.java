package com.example.asterik.presensi;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Pegawai {
    String name;
    String status;
    String jam;

    public Pegawai(String name, String Status, String Jam) {
        this.name = name;
        this.status = Status;
        this.jam = Jam;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJam() {
        return jam;
    }

    public void setJam(String jam) {
        this.jam = jam;
    }
}
