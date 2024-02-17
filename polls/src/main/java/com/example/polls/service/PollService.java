package com.example.polls.service;

import com.example.polls.exception.ResourceNotFoundException;
import com.example.polls.model.*;
import com.example.polls.payload.*;
import com.example.polls.repository.*;
import com.example.polls.security.UserPrincipal;
import com.example.polls.util.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private LocalService localService;

    @Autowired
    private CandInfoRepository candInfoRepository;

    private static final Logger logger = LoggerFactory.getLogger(PollService.class);

    public List<PollResponse> getAllPolls(){

        List<Poll> polls = pollRepository.findAll();


        return ModelMapper.mapPollsToPollResponse(polls);
    }
    public List<Choice> getAllChoice(){

        List<Choice> choices = choiceRepository.findAll();

        return choices;
    }

    public ResponseEntity<?> getPollByItem(String item) {

        Poll poll = pollRepository.findByItem(item);

        if(poll.getExpirationDateTime().minusSeconds(43200).isAfter(Instant.now())) {
            return ResponseEntity.ok()
                    .body(new ApiResponse(false,"Sorry! this poll not start yet"));
        }

        if(poll.getExpirationDateTime().isBefore(Instant.now())) {
            return ResponseEntity.ok()
                    .body(new ApiResponse(false,"Sorry! this poll is expired"));
        }

        return ModelMapper.mapPollToPollResponse(poll);
    }

    public ResponseEntity<?> castVote(String item, VoteRequest voteRequest, UserPrincipal currentUser) {

        Poll poll = pollRepository.findByItem(item);

        if(poll.getExpirationDateTime().isBefore(Instant.now())) {
            return ResponseEntity.ok().body(new ApiResponse(false,"Sorry! this poll is expired"));
        }

        User user = userRepository.getOne(currentUser.getId());

        Choice selectedChoice = poll.getChoices().stream()
                .filter(choice -> choice.getId().equals(voteRequest.getChoiceId()))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Choice", "id", voteRequest.getChoiceId()));


        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setUser(user);

        try {
            vote = voteRepository.save(vote);
        } catch (DataIntegrityViolationException ex) {
            logger.info("User {} has already voted in Poll {}", currentUser.getIdnum(), poll.getItem());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(true,"Sorry! you are already cast you vote in this poll"));
        }

        selectedChoice.setVoteCount(selectedChoice.getVoteCount()+1);

        choiceRepository.save(selectedChoice);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Your Vote Added Successfully"));
    }

    public ResponseEntity<?> castVoteParlament(String item, ListVoteRequest voteRequest, UserPrincipal currentUser) {

        Poll poll = pollRepository.findByItem(item);

        if(poll.getExpirationDateTime().isBefore(Instant.now())) {
            return ResponseEntity.ok().body(new ApiResponse(false,"Sorry! this poll is expired"));
        }

        User user = userRepository.getOne(currentUser.getId());

        List<Long> choiceIds = voteRequest.getChoiceId();
        List<Choice> selectedChoices = poll.getChoices().stream()
                .filter(choice -> choiceIds.contains(choice.getId()))
                .collect(Collectors.toList());

        Vote vote = new Vote();
        vote.setPoll(poll);
        vote.setUser(user);

        try {
            vote = voteRepository.save(vote);
        } catch (DataIntegrityViolationException ex) {
            logger.info("User {} has already voted in Poll {}", currentUser.getIdnum(), poll.getItem());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse(true,"Sorry! you are already cast you vote in this poll"));
        }

        selectedChoices.forEach(Choice -> Choice.setVoteCount(Choice.getVoteCount()+1));                               //.setVoteCount(selectedChoice.getVoteCount()+1);

        for (Choice choice: selectedChoices) {
            choiceRepository.save(choice);
        }

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Your Vote Added Successfully"));
    }

    public ResponseEntity<?> countVote(String item) {

        Poll poll = pollRepository.findByItem(item);

        if(!(poll.getExpirationDateTime().isBefore(Instant.now()))) {
            return ResponseEntity.ok().body(new ApiResponse(false,"Sorry! this poll not expired yet"));
        }

       return ModelMapper.mapEndPollToPollResponse(poll);
    }

    public ResponseEntity<?> addcand(@Valid @RequestBody CandRequest candRequest)
    {
        Candinfo cand = new Candinfo(candRequest.getName(),candRequest.getCity(),candRequest.getText());

        candInfoRepository.save(cand);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Your cand information Added Successfully"));

    }

    public ResponseEntity<CandResponse> getcandbyname(String name){
        Candinfo cand = candInfoRepository.findByName(name);
        CandResponse candResponse = new CandResponse();
        candResponse.setId(cand.getId());
        candResponse.setCity(cand.getCity());
        candResponse.setName(cand.getName());
        candResponse.setText(cand.getText());

        return ResponseEntity.ok().body(candResponse);
    }
    }






