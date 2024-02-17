package com.example.polls.service;

import com.example.polls.model.Admin4;
import com.example.polls.model.Choice;
import com.example.polls.model.LocalChoice;
import com.example.polls.model.Poll;
import com.example.polls.payload.ListVoteRequest;
import com.example.polls.payload.PollRequest;
import com.example.polls.repository.Admin4Repository;
import com.example.polls.repository.ChoiceRepository;
import com.example.polls.repository.LocalChoiceRepository;
import com.example.polls.repository.PollRepository;
import com.example.polls.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private Admin4Repository admin4Repository;

    @Autowired
    private LocalChoiceRepository localChoiceRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    public Poll createPoll(PollRequest pollRequest) {

        Poll poll = new Poll();

        poll.setItem(pollRequest.getItem());

        pollRequest.getChoices().forEach(choiceRequest -> {
            poll.addChoice(new Choice(choiceRequest.getText(),0L));
        });

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = formatter.parse(pollRequest.getPollLength().getDay());

            Instant day = date.toInstant();

            Instant expirationDateTime = day.plusSeconds(43200);

            poll.setExpirationDateTime(expirationDateTime);
        } catch (Exception e) {
            // Handle the case where the user-provided date string is not in a valid format
            System.out.println("Invalid date format: ");
        }


        /*Instant now = Instant.now();
        Instant expirationDateTime = now.plus(Duration.ofDays(pollRequest.getPollLength().getDays()))
                .plus(Duration.ofHours(pollRequest.getPollLength().getHours()));*/


        return pollRepository.save(poll);
    }

    public Poll createLocalPoll(PollRequest pollRequest) {

        Poll poll = new Poll();

        poll.setItem(pollRequest.getItem());

        pollRequest.getChoices().forEach(choiceRequest -> {
            Admin4 admin4 = admin4Repository.getAdmin4(choiceRequest.getCity());
            admin4.addChoice(new LocalChoice(choiceRequest.getText(),0L,admin4));
            localChoiceRepository.save(new LocalChoice(choiceRequest.getText(),0L,admin4));
        });

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            formatter.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = formatter.parse(pollRequest.getPollLength().getDay());

            Instant day = date.toInstant();

            Instant expirationDateTime = day.plusSeconds(43200);

            poll.setExpirationDateTime(expirationDateTime);
        } catch (Exception e) {
            // Handle the case where the user-provided date string is not in a valid format
            System.out.println("Invalid date format: ");
        }

        /*Instant now = Instant.now();
        Instant expirationDateTime = now.plus(Duration.ofDays(pollRequest.getPollLength().getDays()))
                .plus(Duration.ofHours(pollRequest.getPollLength().getHours()));*/

        return pollRepository.save(poll);
    }

    public ResponseEntity<?> remove(Long id, ListVoteRequest voteRequest){

        Poll poll = pollRepository.getOne(id);



        List<Long> choiceIds = voteRequest.getChoiceId();
        List<Choice> selectedChoices = poll.getChoices().stream()
                .filter(choice -> choiceIds.contains(choice.getId()))
                .collect(Collectors.toList());

        for (Choice choice: selectedChoices) {
            poll.removeChoice(choice);
        }

        pollRepository.save(poll);

        return ResponseEntity.ok().body("removed successful");
    }

    public ResponseEntity<?> removeLocal(Long id, ListVoteRequest voteRequest, String city){

        Poll poll = pollRepository.getOne(id);
        Admin4 admin4 = admin4Repository.getAdmin4(city);

        List<Long> choiceIds = voteRequest.getChoiceId();
        List<LocalChoice> selectedChoices = admin4.getChoices().stream()
                .filter(choice -> choiceIds.contains(choice.getId()))
                .collect(Collectors.toList());

        for (LocalChoice choice: selectedChoices) {
            admin4.removeChoice(choice);
        }

        admin4Repository.save(admin4);

        return ResponseEntity.ok().body("removed successful");
    }

    public  ResponseEntity<?> extension(Long id,Integer hours){

        Poll poll = pollRepository.getOne(id);

        poll.setExpirationDateTime(poll.getExpirationDateTime().plus(Duration.ofHours(hours)));

        pollRepository.save(poll);
        return ResponseEntity.ok().body("extension successfully");
    }
}
