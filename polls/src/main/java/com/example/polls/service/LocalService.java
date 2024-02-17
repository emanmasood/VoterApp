package com.example.polls.service;

import com.example.polls.exception.BadRequestException;
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

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LocalService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;


    @Autowired
    private Admin1Repository admin1Repository;

    @Autowired
    private Admin2Repository admin2Repository;

    @Autowired
    private Admin3Repository admin3Repository;

    @Autowired
    private Admin4Repository admin4Repository;

    @Autowired
    private LocalChoiceRepository choiceRepository;

    private static final Logger logger = LoggerFactory.getLogger(LocalService.class);

    public ResponseEntity<?> getLocalChoices(String item, String city){

        Poll poll = pollRepository.findByItem(item);

//        if(poll.getExpirationDateTime().minusSeconds(43200).isAfter(Instant.now())) {
//            return ResponseEntity.ok()
//                    .body(new ApiResponse(false,"عذراً! هذا الاقتراع لم يبدأ بعد"));
//        }

        if(poll.getExpirationDateTime().isBefore(Instant.now())) {
            return ResponseEntity.ok().body(new ApiResponse(false,"عذراً! هذا الاقتراع قد انتهت مدته"));
        }

        Admin4 admin4 = admin4Repository.getAdmin4(city);

        return ModelMapper.mapLocalPollToPollResponse(poll,admin4);
        //return choiceRepository.get(city);
    }

    public ResponseEntity<?> castVote(String item, ListVoteRequest voteRequest, UserPrincipal currentUser) {

        Poll poll = pollRepository.findByItem(item);
        Admin4 admin4 = admin4Repository.getAdmin4(currentUser.getCity());

        if(poll.getExpirationDateTime().isBefore(Instant.now())) {
            return ResponseEntity.ok().body(new ApiResponse(false,"عذراً! هذا الاقتراع قد انتهت مدته"));
        }

        User user = userRepository.getOne(currentUser.getId());

        List<Long> choiceIds = voteRequest.getChoiceId();
        List<LocalChoice> selectedChoices = admin4.getChoices().stream()
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
                    .body(new ApiResponse(true,"عذراً! لقد قمت بالتصويت مسبقاً على هذا الاقتراع"));
        }

        selectedChoices.forEach(localChoice -> localChoice.setVoteCount(localChoice.getVoteCount()+1));//.setVoteCount(selectedChoice.getVoteCount()+1);

        for (LocalChoice choice: selectedChoices) {
            choiceRepository.save(choice);
        }

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "تمت إضافة صوتك بنجاح"));
    }

    public ResponseEntity<?> countVote(String item, UserPrincipal currentUser) {

        Poll poll = pollRepository.findByItem(item);
        Admin4 admin4 = admin4Repository.getAdmin4(currentUser.getCity());

        if(!(poll.getExpirationDateTime().isBefore(Instant.now()))) {
            return ResponseEntity.ok().body(new ApiResponse(false,"عذراً! هذا الاقتراع لم تنته مدته بعد."));
        }

        return ModelMapper.mapEndLocalPollToPollResponse(poll, admin4);
    }
}
