package com.example.chatbot.dto;

import lombok.Data;


@Data
public class AnswerUser {
    private String stage;

    private Integer age;

    private Integer sex;

    private float weight;

    private Integer height;

    private String exerciseIntensity;

    private String habit;

    private String sport;

    @Override
    public String toString() {
        String sex = this.sex == 1 ? "nam" : "nữ";
        return "Thông tin của User {" +
                "stage='" + stage + '\'' +
                ", age=" + age + " tuổi" +
                ", sex=" + sex +
                ", weight=" + weight + "kg" +
                ", height=" + height + "cm" +
                ", exerciseIntensity='" + exerciseIntensity + '\'' +
                ", habit='" + habit + '\'' +
                ", sport='" + sport + '\'' +
                "} ";
    }
}
