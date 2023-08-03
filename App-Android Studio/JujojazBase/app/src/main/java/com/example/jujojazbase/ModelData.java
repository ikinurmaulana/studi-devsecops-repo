package com.example.jujojazbase;

public class ModelData {
    private int id;
    private String image;
    private String from_name;
    private String car_name;
    private String merk;
    private String tipe;
    private String servis_dimulai;
    private String pajak_dimulai;
    private boolean isExpand;

    public ModelData(int id, String image, String from_name, String car_name, String merk, String tipe, String servis_dimulai, String pajak_dimulai) {
        this.id = id;
        this.image = image;
        this.from_name = from_name;
        this.car_name = car_name;
        this.merk = merk;
        this.tipe = tipe;
        this.servis_dimulai = servis_dimulai;
        this.pajak_dimulai = pajak_dimulai;
        this.isExpand = false;
    }

    public ModelData(int id, String merk, String tipe) {
        this.id = id;
        this.merk = merk;
        this.tipe = tipe;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFrom_name() {
        return from_name;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public String getCar_name() {
        return car_name;
    }

    public void setCar_name(String car_name) {
        this.car_name = car_name;
    }

    public String getMerk() {
        return merk;
    }

    public void setMerk(String merk) {
        this.merk = merk;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getServis_dimulai() {
        return servis_dimulai;
    }

    public void setServis_dimulai(String servis_dimulai) {
        this.servis_dimulai = servis_dimulai;
    }

    public String getPajak_dimulai() {
        return pajak_dimulai;
    }

    public void setPajak_dimulai(String pajak_dimulai) {
        this.pajak_dimulai = pajak_dimulai;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }
}
