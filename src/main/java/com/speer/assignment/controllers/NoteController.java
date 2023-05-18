package com.speer.assignment.controllers;

import com.speer.assignment.dto.NoteDto;
import com.speer.assignment.dto.ShareNoteDto;
import com.speer.assignment.entity.Note;
import com.speer.assignment.repository.NoteRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @GetMapping
    public List<NoteDto> getAllNotes() {
        return Collections.emptyList();
    }

    @GetMapping("/{id}")
    public NoteDto getNoteById(@PathVariable("id") String id) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            return convertToDto(note);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found");
        }
    }
    @PostMapping
    public NoteDto createNote(@RequestBody NoteDto noteDto) {
        Note note = convertToEntity(noteDto);
        Note createdNote = noteRepository.save(note);
        return convertToDto(createdNote);
    }

    @PutMapping("/{id}")
    public NoteDto updateNote(@PathVariable("id") String id, @RequestBody NoteDto noteDto) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            note.setTitle(noteDto.getTitle());
            note.setContent(noteDto.getContent());

            Note updatedNote = noteRepository.save(note);
            return convertToDto(updatedNote);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found");
        }
    }
    private NoteDto convertToDto(Note note) {
        NoteDto noteDto = new NoteDto();
        noteDto.setId(note.getId());
        noteDto.setTitle(note.getTitle());
        noteDto.setContent(note.getContent());
        return noteDto;
    }
    @DeleteMapping("/{id}")
    public void deleteNote(@PathVariable("id") String id) {
        noteRepository.deleteById(id);
    }

    @PostMapping("/{id}/share")
    public void shareNote(@PathVariable("id") String id, @RequestBody ShareNoteDto shareNoteDto) {
        Optional<Note> optionalNote = noteRepository.findById(id);
        if (optionalNote.isPresent()) {
            Note note = optionalNote.get();
            List<String> sharedWith = note.getSharedWith();
            if (sharedWith == null) {
                sharedWith = new ArrayList<>();
            }
            sharedWith.add(shareNoteDto.getSharedWith());
            note.setSharedWith(sharedWith);
            noteRepository.save(note);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found");
        }
    }

    @GetMapping("/search")
    public List<NoteDto> searchNotes(@RequestParam("q") String query) {
        List<Note> matchingNotes = noteRepository.findByContentContaining(query);


        List<NoteDto> matchingNoteDtos = new ArrayList<>();
        for (Note note : matchingNotes) {
            NoteDto noteDto = convertToDto(note);
            matchingNoteDtos.add(noteDto);
        }

        return matchingNoteDtos;
    }

    private Note convertToEntity(NoteDto noteDto) {
        Note note = new Note();
        note.setId(noteDto.getId());
        note.setTitle(noteDto.getTitle());
        note.setContent(noteDto.getContent());

        return note;
    }
}
