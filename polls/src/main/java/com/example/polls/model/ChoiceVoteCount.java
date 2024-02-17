package com.example.polls.model;

public class ChoiceVoteCount {

    private Long choiceId;
    private String governorate;

    public ChoiceVoteCount(Long choiceId, String governorate) {
        this.choiceId = choiceId;
        this.governorate = governorate;
    }

    public Long getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(Long choiceId) {
        this.choiceId = choiceId;
    }

    public String getGovernorate() {
        return governorate;
    }

    public void setGovernorate(String governorate) {
        this.governorate = governorate;
    }
}
