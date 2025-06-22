package com.expense.tracking.backend.expensetrackingbackend.controller;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expense.tracking.backend.expensetrackingbackend.dto.MessageTreeDto;
import com.expense.tracking.backend.expensetrackingbackend.dto.ExpenseTreeDto;
import com.expense.tracking.backend.expensetrackingbackend.model.Message;
import com.expense.tracking.backend.expensetrackingbackend.repository.ExpenseRepository;
import com.expense.tracking.backend.expensetrackingbackend.repository.HashtagRepository;
import com.expense.tracking.backend.expensetrackingbackend.repository.MessageRepository;
import com.expense.tracking.backend.expensetrackingbackend.service.ExpenseParserService;

@RestController
@RequestMapping("api/v1/expense-tree")
public class MessageTreeHandler {
    private final MessageRepository messageRepository;
    private final ExpenseParserService expenseParserService;

    public MessageTreeHandler(ExpenseRepository expenseRepository, MessageRepository messageRepository, 
                                HashtagRepository hashtagRepository, ExpenseParserService expenseParserService) {
        this.messageRepository = messageRepository;
        this.expenseParserService = expenseParserService;
    }

    private ResponseEntity<MessageTreeDto> getResponseFromMessageTreeDto(Optional<Message> optionalMessage) {
        if (optionalMessage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        Message message = optionalMessage.get();
        ExpenseTreeDto expenseTreeDto = this.expenseParserService.parseToTreeDto(message.getExpenses()); 
        MessageTreeDto messageTreeDto = new MessageTreeDto(message.getId(), message.getTimestamp(), expenseTreeDto);
        return ResponseEntity.ok(messageTreeDto);
    }

    private ResponseEntity<List<MessageTreeDto>> getResponseFromMessageTreeDtoList(List<Message> messages) {
        if (messages.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<MessageTreeDto> messageTreeDtoList = messages.stream()
                        .map(message -> new MessageTreeDto(message.getId(), message.getTimestamp(), expenseParserService.parseToTreeDto(message.getExpenses())))
                        .toList(); 
        return ResponseEntity.ok(messageTreeDtoList);
    }

    @GetMapping("/msgs_timestamps") // added for convenience
    public ResponseEntity<List<LocalDateTime>> getAllMessageTreesTimestamps() {
        List<LocalDateTime> timestamps = messageRepository.findAll().stream()
            .map(Message::getTimestamp).sorted(Comparator.reverseOrder()).toList();
        if (timestamps.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(timestamps);
    }

    @GetMapping("/")
    public ResponseEntity<List<MessageTreeDto>> getAllMessageTrees() {
        List<Message> messages = messageRepository.findAll();
        return getResponseFromMessageTreeDtoList(messages);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MessageTreeDto> getMessageTreeById(@PathVariable Long id) {
        return getResponseFromMessageTreeDto(messageRepository.findById(id));
    }

    @GetMapping("/by-timestamp")
    public ResponseEntity<MessageTreeDto> getMessageTreeByTimestamp(
                @RequestParam("value") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime timestamp) {

        return getResponseFromMessageTreeDto(messageRepository.findByTimestamp(timestamp));
    }

    @GetMapping("/latest")
    public ResponseEntity<MessageTreeDto> getLatestMessageTree() {
        return getResponseFromMessageTreeDto(messageRepository.findLatestMessage());
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<MessageTreeDto>> getMessageTreesByDateRange(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        return getResponseFromMessageTreeDtoList(messageRepository.findByTimestampBetween(start, end));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            messageRepository.deleteById(id);
            return ResponseEntity.ok("Message with ID " + id + " was deleted successfully.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message with ID " + id + " was not found.");
    }
}
