package com.example.polls.controller;

import com.example.polls.model.Candinfo;
import com.example.polls.model.Choice;
import com.example.polls.payload.*;
import com.example.polls.repository.PollRepository;
import com.example.polls.repository.UserRepository;
import com.example.polls.security.CurrentUser;
import com.example.polls.security.UserPrincipal;
import com.example.polls.service.LocalService;
import com.example.polls.service.PollService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PollService pollService;

    @Autowired
    private LocalService localService;


    private static final Logger logger = LoggerFactory.getLogger(PollController.class);

    @GetMapping("/getpolls")
    @ResponseBody
    public List<PollResponse> getPolls() {
        return pollService.getAllPolls();
    }
    @GetMapping("/getchoices")
    @ResponseBody
    public List<Choice> getchoice() {
        return pollService.getAllChoice();
    }


//    @GetMapping("/{pollid}")
//    @ResponseBody
//    public ResponseEntity<?> getPollById(@PathVariable Long pollid){
//
//            return pollService.getPollById(pollid);
//    }

     @GetMapping("/{item}")
     @ResponseBody
     public ResponseEntity<?> getPollByItem(@PathVariable String item,@CurrentUser UserPrincipal currentuser){
        if(item.equals("local"))
            return localService.getLocalChoices(item,currentuser.getCity());

        else
            return pollService.getPollByItem(item);
     }

//    @GetMapping("/local/{pollid}")
//    @ResponseBody
//    public ResponseEntity<?> getLocalPollById(@PathVariable Long pollid, @CurrentUser UserPrincipal currentUser){
//
//        return localService.getLocalChoices(pollid,currentUser.getCity());
//    }

    @PostMapping("/{item}/votes")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> castVote(@CurrentUser UserPrincipal currentUser,
                                                @PathVariable String item,
                                                @Valid @RequestBody VoteRequest voteRequest) {

         return pollService.castVote(item, voteRequest, currentUser);
    }

    @PostMapping("/{item}/votesParlament")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> castVoteParlament(@CurrentUser UserPrincipal currentUser,
                                            @PathVariable String item,
                                            @Valid @RequestBody ListVoteRequest voteRequest) {

        return pollService.castVoteParlament(item, voteRequest, currentUser);

    }

    @GetMapping("/{item}/results")
    public ResponseEntity<?> countVote(@PathVariable String item,@CurrentUser UserPrincipal currentUser){

            return pollService.countVote(item);

    }

    @PostMapping("/{item}/votesl")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> castVoteLocaly(@CurrentUser UserPrincipal currentUser,
                                      @PathVariable String item,
                                      @Valid @RequestBody ListVoteRequest voteRequest) {

        return localService.castVote(item, voteRequest, currentUser);
    }

    @GetMapping("/{item}/localresults")
    public ResponseEntity<?> countVoteLocaly(@PathVariable String item,@CurrentUser UserPrincipal currentUser){

        return localService.countVote(item,currentUser);

    }

    /*private List<?> getLocalChoices(@CurrentUser UserPrincipal currentUser){
        return localService.getChoices(currentUser.getCity());
    }*/


    @PostMapping("/addinfo")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> addcand(@Valid @RequestBody CandRequest candRequest)
    {
        return pollService.addcand(candRequest);
    }
    @GetMapping("/getcand/{name}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CandResponse> getcand(@PathVariable("name") String name){
        return pollService.getcandbyname(name);
    }
}

