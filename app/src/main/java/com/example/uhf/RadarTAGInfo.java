package com.example.uhf;

import com.rscja.deviceapi.entity.UHFTAGInfo;

public class RadarTAGInfo {
    private UHFTAGInfo tagInfo;
    private int angle;

    public RadarTAGInfo(UHFTAGInfo uhftagInfo, int angle) {
        this.tagInfo = uhftagInfo;
        this.angle = angle;
    }

    public UHFTAGInfo getTagInfo() {
        return tagInfo;
    }

    public void setTagInfo(UHFTAGInfo uhftagInfo) {
        this.tagInfo = uhftagInfo;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "RadarTAGInfo{" +
                "angle=" + angle +
                ", uhftagInfo=" + tagInfo +
                '}';
    }
}
