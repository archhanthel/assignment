package com.speer.assignment.controllers;

import com.speer.assignment.dto.NoteDto;
import com.speer.assignment.dto.ShareNoteDto;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {

    @GetMapping
    public List<NoteDto> getAllNotes() {
        return Collections.emptyList();
    }

    @GetMapping("/{id}")
    public NoteDto getNoteById(@PathVariable("id") String id) {
        return null;
    }

    @PostMapping
    public NoteDto createNote(@RequestBody NoteDto noteDto) {
        return null;
    }

    @PutMapping("/{id}")
    public NoteDto updateNote(@PathVariable("id") String id, @RequestBody NoteDto noteDto) {
        return null;
    }

    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable("id") String id) {
    }

    @PostMapping("/{id}/share")
    public void shareNote(@PathVariable("id") String id, @RequestBody ShareNoteDto shareNoteDto) {
    }
}
