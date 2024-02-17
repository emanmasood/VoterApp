package com.example.polls.controller;

import com.example.polls.model.Poll;
import com.example.polls.payload.*;
import com.example.polls.repository.ChoiceRepository;
import com.example.polls.repository.PollRepository;
import com.example.polls.service.AdminService;
import com.example.polls.service.LocalService;
import com.example.polls.service.PollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private PollService pollService;

    @Autowired
    private LocalService localService;

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private AdminService adminService;

    private static String city;

    private static final Logger logger = LoggerFactory.getLogger(PollController.class);


    @PostMapping("/createpoll")
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Qualifier("defaultTransactionManager")
    public ResponseEntity<?> createPoll(@Valid @RequestBody PollRequest pollRequest) {

        List<Poll> poll = pollRepository.findAllByItem(pollRequest.getItem());

        if (!(poll.isEmpty()))
            if (poll.iterator().next().getExpirationDateTime().isAfter(Instant.now()))
                return ResponseEntity.badRequest().body(new ApiResponse(false,"Poll already exists and not expired!"));

        adminService.createPoll(pollRequest);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Poll Created Successfully"));
    }

    @PostMapping("/createlocalpoll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createLocalPoll(@Valid @RequestBody PollRequest pollRequest) {

        List<Poll> poll = pollRepository.findAllByItem(pollRequest.getItem());

        if (!(poll.isEmpty()))
            if (poll.iterator().next().getExpirationDateTime().isAfter(Instant.now()))
                return ResponseEntity.badRequest().body(new ApiResponse(false,"Poll already exists and not expired!"));

            adminService.createLocalPoll(pollRequest);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Poll Created Successfully"));
    }

    @GetMapping("/getpolls")
    @ResponseBody
    public List<PollResponse> getPolls() {
        return pollService.getAllPolls();
    }

    @GetMapping("/{item}")
    @ResponseBody
    public ResponseEntity<?> getPollById(@PathVariable String item){

        return pollService.getPollByItem(item);
    }

    @GetMapping("/local/{pollid}")
    @ResponseBody
    public ResponseEntity<?> getLocalPollById(@PathVariable String item, @Valid @RequestBody ChoiceRequest choiceRequest){

        city = choiceRequest.getCity();

        return localService.getLocalChoices(item,city);
    }

    @PostMapping("/{pollid}/remove")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> remove(@PathVariable Long pollid,
                                    @Valid @RequestBody ListVoteRequest voteRequest){

        return adminService.remove(pollid,voteRequest);
    }

    @PostMapping("/{pollid}/removeLocal")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> removeLocal(@PathVariable Long pollid,
                                    @Valid @RequestBody ListVoteRequest voteRequest){

        return adminService.removeLocal(pollid,voteRequest,city);
    }

    @PostMapping("/{pollid}/extension")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> extension(@PathVariable Long pollid,@Valid @RequestBody Integer hours){

        return adminService.extension(pollid,hours);
    }

}
