package com.example.polls.payload;

import javax.validation.constraints.NotNull;
import java.util.List;

public class ListVoteRequest {

    @NotNull
    private List<Long> choiceId;

    public List<Long> getChoiceId() {
        return choiceId;
    }

    public void setChoiceId(List<Long> choiceId) {
        this.choiceId = choiceId;
    }
}
