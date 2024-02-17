package com.example.polls.util;

import com.example.polls.model.Admin4;
import com.example.polls.model.Poll;
import com.example.polls.payload.ChoiceResponse;
import com.example.polls.payload.PollResponse;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class ModelMapper {

    public static List<PollResponse> mapPollsToPollResponse(List<Poll> polls){

        List<PollResponse> pollsResponse = new ArrayList<>();
        
        for (Poll poll:
             polls) {
            PollResponse pollResponse = new PollResponse();
            pollResponse.setId(poll.getId());
            pollResponse.setItem(poll.getItem());
            pollsResponse.add(pollResponse);
        }

        return pollsResponse;
    }

    public static ResponseEntity<?> mapPollToPollResponse(Poll poll) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setItem(poll.getItem());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());

            /*if(choiceVotesMap.containsKey(choice.getId())) {
                choiceResponse.setVoteCount(choiceVotesMap.get(choice.getId()));
            } else {
                choiceResponse.setVoteCount(0);
            }*/
            return choiceResponse;
        }).collect(Collectors.toList());

        pollResponse.setChoices(choiceResponses);

        /*if(userVote != null) {
            pollResponse.setSelectedChoice(userVote);
        }

        //long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        //pollResponse.setTotalVotes(totalVotes);*/

        return ResponseEntity.ok().body(pollResponse);
    }

    public static ResponseEntity<PollResponse> mapLocalPollToPollResponse(Poll poll, Admin4 admin4) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setItem(poll.getItem());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = admin4.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());
            return choiceResponse;
        }).collect(Collectors.toList());

        pollResponse.setChoices(choiceResponses);

        return ResponseEntity.ok().body(pollResponse);
    }

    public static ResponseEntity<PollResponse> mapEndLocalPollToPollResponse(Poll poll, Admin4 admin4) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setItem(poll.getItem());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = admin4.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());
            choiceResponse.setVoteCount(choice.getVoteCount());
            return choiceResponse;
        }).collect(Collectors.toList());

        pollResponse.setChoices(choiceResponses);

        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);

        return ResponseEntity.ok().body(pollResponse);
    }


    public static ResponseEntity<?> mapEndPollToPollResponse(Poll poll) {
        PollResponse pollResponse = new PollResponse();
        pollResponse.setId(poll.getId());
        pollResponse.setItem(poll.getItem());
        pollResponse.setCreationDateTime(poll.getCreatedAt());
        pollResponse.setExpirationDateTime(poll.getExpirationDateTime());
        Instant now = Instant.now();
        pollResponse.setExpired(poll.getExpirationDateTime().isBefore(now));

        List<ChoiceResponse> choiceResponses = poll.getChoices().stream().map(choice -> {
            ChoiceResponse choiceResponse = new ChoiceResponse();
            choiceResponse.setId(choice.getId());
            choiceResponse.setText(choice.getText());
            choiceResponse.setVoteCount(choice.getVoteCount());
            return choiceResponse;
        }).collect(Collectors.toList());

        pollResponse.setChoices(choiceResponses);

        long totalVotes = pollResponse.getChoices().stream().mapToLong(ChoiceResponse::getVoteCount).sum();
        pollResponse.setTotalVotes(totalVotes);

        return ResponseEntity.ok().body(pollResponse);
    }

}
