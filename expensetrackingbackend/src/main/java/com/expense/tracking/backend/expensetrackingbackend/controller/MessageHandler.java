package com.expense.tracking.backend.expensetrackingbackend.controller;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expense.tracking.backend.expensetrackingbackend.dto.ExpenseDto;
import com.expense.tracking.backend.expensetrackingbackend.dto.ExpenseRequestDto;
import com.expense.tracking.backend.expensetrackingbackend.dto.HashtagDto;
import com.expense.tracking.backend.expensetrackingbackend.dto.MessageDto;
import com.expense.tracking.backend.expensetrackingbackend.model.Expense;
import com.expense.tracking.backend.expensetrackingbackend.model.Hashtag;
import com.expense.tracking.backend.expensetrackingbackend.model.Message;
import com.expense.tracking.backend.expensetrackingbackend.repository.MessageRepository;
import com.expense.tracking.backend.expensetrackingbackend.repository.HashtagRepository;
import com.expense.tracking.backend.expensetrackingbackend.service.ExpenseParserService;

@RestController
@RequestMapping("api/v1/expense")
public class MessageHandler {
    private final MessageRepository messageRepository;
    private final HashtagRepository hashtagRepository;
    private final ExpenseParserService expenseParserService;

    public MessageHandler(MessageRepository messageRepository, HashtagRepository hashtagRepository, ExpenseParserService expenseParserService) {
        this.messageRepository = messageRepository;
        this.hashtagRepository = hashtagRepository;
        this.expenseParserService = expenseParserService;
    }

    @PostMapping("/")
    public ResponseEntity<?> addExpenseMessage(@RequestBody List<ExpenseRequestDto> requests) {
        try {
            if (requests == null || requests.isEmpty()) {
                return ResponseEntity.badRequest().body("Expense list is empty or null.");
            }
            LocalDateTime msgTimestamp = LocalDateTime.now();
            Message message = new Message();
            message.setTimestamp(msgTimestamp);
            List<Expense> expenses = new ArrayList<>();
            for (ExpenseRequestDto req : requests) {
                Expense expense = new Expense();
                expense.setDescription(req.getExpense());
                expense.setAmount(expenseParserService.parseAmount(req.getExpense()));
                expense.setMessage(message);
                List<String> hashtags = expenseParserService.extractHashtags(req.getExpense());
                for (String tag : hashtags) {
                    Hashtag hashtag = hashtagRepository.findByName(tag).orElseGet(() -> {
                        Hashtag newTag = new Hashtag();
                        newTag.setName(tag);
                        return hashtagRepository.save(newTag);
                    });
                    expense.getHashtags().add(hashtag);
                }
                expenses.add(expense);
            }
            message.setExpenses(expenses);
            messageRepository.save(message); // cascade saves expenses
            return ResponseEntity.ok("Expenses saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save expenses: " + e.getMessage());
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<MessageDto>> getAllMessages() {
        List<Message> messages = messageRepository.findAll();
        if (messages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<MessageDto> messageDtoList = messages.stream()
            .sorted(Comparator.comparing(Message::getTimestamp).reversed())
            .map(msg -> {
                List<ExpenseDto> expenseDtos = msg.getExpenses().stream()
                    .map(exp -> new ExpenseDto(
                        exp.getAmount(),
                        exp.getDescription(),
                        exp.getHashtags().stream()
                            .map(hashtag -> new HashtagDto(
                                hashtag.getId(), hashtag.getName()))
                            .toList(),
                        msg.getTimestamp()
                    ))
                    .toList();
                return new MessageDto(
                    msg.getId(),
                    msg.getTimestamp(),
                    expenseDtos
                );
            })
            .toList();
        return ResponseEntity.ok(messageDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageDto> getMessageById(@PathVariable Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Message msg = optionalMessage.get();
        List<ExpenseDto> expenseDtos = msg.getExpenses().stream()
            .map(exp -> new ExpenseDto(
                exp.getAmount(),
                exp.getDescription(),
                exp.getHashtags().stream()
                    .map(hashtag -> new HashtagDto(hashtag.getId(), hashtag.getName()))
                    .toList(),
                msg.getTimestamp()
            ))
            .toList();
        MessageDto dto = new MessageDto(
            msg.getId(),
            msg.getTimestamp(),
            expenseDtos
        );
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/msgs_timestamps")
    public ResponseEntity<List<LocalDateTime>> getAllMessageTimestamps() {
        List<LocalDateTime> timestamps = messageRepository.findAll().stream()
            .map(Message::getTimestamp)
            .sorted(Comparator.reverseOrder())
            .toList();
        if (timestamps.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(timestamps);
    }

    @GetMapping("/latest")
    public ResponseEntity<MessageDto> getLatestMessage() {
        Optional<Message> optionalMessage = messageRepository.findLatestMessage();
        if (optionalMessage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Message message = optionalMessage.get();
        List<ExpenseDto> expenseDtos = message.getExpenses().stream()
            .map(exp -> new ExpenseDto(
                exp.getAmount(),
                exp.getDescription(),
                exp.getHashtags().stream()
                    .map(ht -> new HashtagDto(ht.getId(), ht.getName()))
                    .toList(),
                message.getTimestamp()
            ))
            .toList();

        MessageDto messageDto = new MessageDto(
            message.getId(),
            message.getTimestamp(),
            expenseDtos
        );
        return ResponseEntity.ok(messageDto);
    }

    @GetMapping("/by-timestamp")
    public ResponseEntity<MessageDto> getMessageByTimestamp(
        @RequestParam("value") 
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {
        
        Optional<Message> optionalMessage = messageRepository.findByTimestamp(timestamp);
        if (optionalMessage.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Message msg = optionalMessage.get();
        List<ExpenseDto> expenseDtos = msg.getExpenses().stream()
            .map(exp -> new ExpenseDto(
                exp.getAmount(),
                exp.getDescription(),
                exp.getHashtags().stream()
                    .map(hashtag -> new HashtagDto(hashtag.getId(), hashtag.getName()))
                    .toList(),
                msg.getTimestamp()
            ))
            .toList();
        MessageDto dto = new MessageDto(
            msg.getId(),
            msg.getTimestamp(),
            expenseDtos
        );
        return ResponseEntity.ok(dto);
    }
    
    @GetMapping("/by-date-range")
    public ResponseEntity<List<MessageDto>> getMessagesBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<Message> messages = messageRepository.findByTimestampBetween(start, end);
        if (messages.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<MessageDto> messageDtos = messages.stream()
            .sorted(Comparator.comparing(Message::getTimestamp).reversed())
            .map(message -> {
                List<ExpenseDto> expenseDtos = message.getExpenses().stream()
                    .map(exp -> new ExpenseDto(
                        exp.getAmount(),
                        exp.getDescription(),
                        exp.getHashtags().stream()
                            .map(ht -> new HashtagDto(ht.getId(), ht.getName()))
                            .toList(),
                        message.getTimestamp()
                    ))
                    .toList();
                return new MessageDto(message.getId(), message.getTimestamp(), expenseDtos);
            })
            .toList();
        return ResponseEntity.ok(messageDtos);
    }

}
